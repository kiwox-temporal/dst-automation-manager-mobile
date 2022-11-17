package net.kiwox.manager.dst.appium.service.impl;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import net.kiwox.manager.dst.appium.service.interfaces.ISpeedTestService;
import net.kiwox.manager.dst.domain.ControllerProbe;
import net.kiwox.manager.dst.domain.Test;
import net.kiwox.manager.dst.domain.TestResult;
import net.kiwox.manager.dst.enums.Parameter;
import net.kiwox.manager.dst.enums.TestResultGrade;
import net.kiwox.manager.dst.enums.TestResultState;
import net.kiwox.manager.dst.service.interfaces.IAppiumTestService;
import net.kiwox.manager.dst.service.interfaces.ICodeMessageService;
import net.kiwox.manager.dst.service.interfaces.IParameterService;
import net.kiwox.manager.dst.service.interfaces.ITestResultService;
import net.kiwox.manager.dst.utils.Utils;
import net.kiwox.manager.dst.wrappers.TestResultDataWrapper;

@Service
public class SpeedTestServiceImpl implements ISpeedTestService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SpeedTestServiceImpl.class);
	
	@Autowired
	private IAppiumTestService appiumTestService;
	@Autowired
	private IParameterService parameterService;
	@Autowired
	private ICodeMessageService codeMessageService;
	@Autowired
	private ITestResultService testResultService;

	@Override
	public void runTest(ControllerProbe probe, TestResult result) {
		StringBuilder script = new StringBuilder();
		script.append(result.getTest().getScript());
		script.append(" -udid ");
		script.append(probe.getUdid());
		script.append(" -tr ");
		script.append(result.getId());
		
		Test test = result.getTest();
		
		script.append(" -it ");
		script.append(test.getIteration());
		
		Integer waitTimeout = parameterService.getInt(Parameter.APPIUM_WAIT_TIMEOUT);
		String pingScale = parameterService.getString(Parameter.PING_SCALE);
		if (waitTimeout != null) {
			script.append(" -wt ");
			script.append(waitTimeout);
		}
		
		LOGGER.info("Start running script: {}", script);
		
		long start = System.currentTimeMillis();
		Process process;
		try {
			process = new ProcessBuilder(Utils.splitCommandLine(script.toString())).start();
		} catch (IOException e) {
			codeMessageService.logInfo("DST-SPEEDTEST-001", probe.getId(), script);
			LOGGER.error("[DST-SPEEDTEST-001] Error starting process", e);
			appiumTestService.markFailed(result);
			return;
		}
		
		JSONObject json = appiumTestService.readOutput(process);
		if (json == null) {
			codeMessageService.logInfo("DST-SPEEDTEST-002", probe.getId(), script);
			appiumTestService.markFailed(result);
			return;
		}
		long dt = System.currentTimeMillis() - start;
		LOGGER.info("Finished running script ({}ms): {}", dt, script);
		
		String code = json.optString("code");
		String message = json.optString("message");
		boolean error = json.optBoolean("error");
		if (error && StringUtils.isNotEmpty(code)) {
			if ("DST-SPEEDTEST-003".equals(code)) {
				codeMessageService.logInfo(code, probe.getId(), script);
			} else {
				codeMessageService.logInfo(code, probe.getId(), message);
			}
			appiumTestService.markFailed(result);
			return;
		}

		Map<String, List<TestResultDataWrapper>> resultData = new HashMap<>();
		resultData.put("Velocidad upload", new LinkedList<>());
		resultData.put("Velocidad download", new LinkedList<>());
		resultData.put("Velocidad Ping", new LinkedList<>());
		List<TestResultGrade> grades = new LinkedList<>();
		try {
			JSONArray timeArr = new JSONArray(message);
			for (int i = 0; i < timeArr.length(); ++i) {
				JSONObject timeObj = timeArr.getJSONObject(i);
				
				String testName = test.getTestName();
				if (testName != null) {
					testName = testName.replace("{iteration}", ""+(i+1));
				}
				
				TestResultDataWrapper data = getDataWrapper(test, testName, timeObj.getBigDecimal("upload").floatValue(), error);
				resultData.get("Velocidad upload").add(data);
				grades.add(data.getResultGrade());
				
				data = getDataWrapper(test, testName, timeObj.getBigDecimal("download").floatValue(), error);
				resultData.get("Velocidad download").add(data);
				grades.add(data.getResultGrade());
				
				data = getDataWrapper(testName, timeObj.getLong("ping"), pingScale, error);
				resultData.get("Velocidad Ping").add(data);
				grades.add(data.getResultGrade());
			}
		} catch (JSONException e) {
			LOGGER.error("[DST-SPEEDTEST-002] Error getting speed from json", e);
			codeMessageService.logInfo("DST-SPEEDTEST-002", probe.getId(), script);
			appiumTestService.markFailed(result);
			return;
		}
		
		Gson gson = new Gson();
		
		result.setResultGrade(Utils.joinGrades(grades));
		result.setState(TestResultState.EXECUTED);
		result.setTestResultDatetime(new Date());
		result.setTestResultData(gson.toJson(resultData, Map.class));
		result.setTestResultLapse(1.0f * json.optLong("time", dt));
		
		LOGGER.info("Updating test result [{}]", result.getId());
		testResultService.save(result);
		LOGGER.info("Speedtest test executed successfully and stored in [{}]", result);
	}
	
	private TestResultDataWrapper getDataWrapper(Test test, String testName, float speed, boolean error) {
		TestResultDataWrapper data = new TestResultDataWrapper();
		data.setTestName(testName);
		data.setValue(speed);
		data.setMedition("Mbps");
		data.setResultGrade(Utils.getGrade(speed, test.getTestScales(), error));
		return data;
	}
	
	private TestResultDataWrapper getDataWrapper(String testName, long ping, String pingScale, boolean error) {
		TestResultDataWrapper data = new TestResultDataWrapper();
		data.setTestName(testName);
		data.setValue(ping);
		data.setMedition("ms");
		data.setResultGrade(getPingGrade(ping, pingScale, error));
		return data;
	}
	
	private TestResultGrade getPingGrade(long ping, String pingScale, boolean error) {
		if (error && ping == 0) {
			return TestResultGrade.FAILED;
		}
		
		String[] scales = pingScale.split(",");
		for (String scale : scales) {
			String[] grade = scale.split("!");
			if (grade.length < 2) {
				LOGGER.info("Could not parse ping scales from [{}]", scale);
				continue;
			}
			String[] values = grade[1].split("-");
			Long from = parseInteger(values[0]);
			Long to = values.length == 0 ? null : parseInteger(values[1]);
			if ((from == null || from < ping) && (to == null || ping <= to)) {
				try {
					return TestResultGrade.valueOf(grade[0]);
				} catch (IllegalArgumentException e) {
					String err = "Could not parse TestResultGrade from [" + grade[0] + "]";
					LOGGER.error(err, e);
				}
			}
		}
		return null;
	}
	
	private Long parseInteger(String value) {
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			// Not a number
		}
		return null;
	}

}
