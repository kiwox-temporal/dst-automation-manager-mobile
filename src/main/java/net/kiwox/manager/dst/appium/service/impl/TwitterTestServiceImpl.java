package net.kiwox.manager.dst.appium.service.impl;


import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import net.kiwox.manager.dst.appium.service.interfaces.ITwitterTestService;
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
public class TwitterTestServiceImpl implements ITwitterTestService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TwitterTestServiceImpl.class);
	
	@Autowired
	private IParameterService parameterService;
	@Autowired
	private ICodeMessageService codeMessageService;
	@Autowired
	private IAppiumTestService appiumTestService;
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
		script.append(" -user \"");
		script.append(parameterService.getString(Parameter.TWITTER_USERNAME));
		script.append("\" -pass \"");
		script.append(parameterService.getString(Parameter.TWITTER_PASSWORD));
		script.append("\"");
		
		Test test = result.getTest();
		
		Integer iterations = parameterService.getInt(Parameter.TWITTER_ITERATIONS);
		if (iterations != null) {
			script.append(" -it ");
			script.append(iterations);
		}
		
		Integer waitTimeout = parameterService.getInt(Parameter.APPIUM_WAIT_TIMEOUT);
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
			codeMessageService.logInfo("DST-TWITTER-001", probe.getId(), script);
			LOGGER.error("[DST-TWITTER-001] Error starting process", e);
			appiumTestService.markFailed(result);
			return;
		}
		
		JSONObject json = appiumTestService.readOutput(process);
		if (json == null || !json.has("time")) {
			codeMessageService.logInfo("DST-TWITTER-002", probe.getId(), script);
			appiumTestService.markFailed(result);
			return;
		}
		long dt = System.currentTimeMillis() - start;
		LOGGER.info("Finished running script ({}ms): {}", dt, script);

		String code = json.optString("code");
		String message = json.optString("message");
		boolean error = json.optBoolean("error");
		if (error && StringUtils.isNotEmpty(code)) {
			if ("DST-TWITTER-003".equals(code)) {
				codeMessageService.logInfo(code, probe.getId(), script);
			} else {
				codeMessageService.logInfo(code, probe.getId(), message);
			}
			appiumTestService.markFailed(result);
			return;
		}
		
		Gson gson = new Gson();
		
		List<TestResultDataWrapper> resultData = new LinkedList<>();
		List<TestResultGrade> grades = new LinkedList<>();
		try {
			long[] times = gson.fromJson(message, long[].class);
			for (int i = 0; i < times.length; ++i) {
				long time = times[i];
				
				String testName = test.getTestName();
				if (testName != null) {
					testName = testName.replace("{iteration}", ""+(i+1));
				}
				
				TestResultGrade grade = Utils.getGrade(time, test.getTestScales(), error);
				grades.add(grade);
				
				TestResultDataWrapper data = new TestResultDataWrapper();
				data.setTestName(testName);
				data.setValue(time);
				data.setMedition("ms");
				data.setResultGrade(grade);
				resultData.add(data);
			}
		} catch (JsonSyntaxException e) {
			LOGGER.error("[DST-TWITTER-002] Error parsing json", e);
			codeMessageService.logInfo("DST-TWITTER-002", probe.getId(), script);
			appiumTestService.markFailed(result);
			return;
		}

		Map<String, List<TestResultDataWrapper>> dataMap = Collections.singletonMap(test.getIndexText(), resultData);
		
		result.setResultGrade(Utils.joinGrades(grades));
		result.setState(TestResultState.EXECUTED);
		result.setTestResultDatetime(new Date());
		result.setTestResultData(gson.toJson(dataMap, Map.class));
		result.setTestResultLapse(1.0f * json.optLong("time", dt));
		
		LOGGER.info("Updating test result [{}]", result.getId());
		testResultService.save(result);
		LOGGER.info("Twitter test executed successfully and stored in [{}]", result);
	}

}
