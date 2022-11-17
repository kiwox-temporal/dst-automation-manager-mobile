package net.kiwox.manager.dst.appium.service.impl;


import java.io.IOException;
import java.util.Collections;
import java.util.Date;
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

import net.kiwox.manager.dst.appium.service.interfaces.IYoutubeTestService;
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
public class YoutubeTestServiceImpl implements IYoutubeTestService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(YoutubeTestServiceImpl.class);
	
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
		addVideo(script, parameterService.getString(Parameter.YOUTUBE_VIDEO_1));
		addVideo(script, parameterService.getString(Parameter.YOUTUBE_VIDEO_2));
		addVideo(script, parameterService.getString(Parameter.YOUTUBE_VIDEO_3));
		
		Test test = result.getTest();
		
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
			codeMessageService.logInfo("DST-YOUTUBE-001", probe.getId(), script);
			LOGGER.error("[DST-YOUTUBE-001] Error starting process", e);
			appiumTestService.markFailed(result);
			return;
		}
		
		JSONObject json = appiumTestService.readOutput(process);
		if (json == null) {
			codeMessageService.logInfo("DST-YOUTUBE-002", probe.getId(), script);
			appiumTestService.markFailed(result);
			return;
		}
		long dt = System.currentTimeMillis() - start;
		LOGGER.info("Finished running script ({}ms): {}", dt, script);
		
		String code = json.optString("code");
		String message = json.optString("message");
		boolean error = json.optBoolean("error");
		if (error && StringUtils.isNotEmpty(code)) {
			if ("DST-YOUTUBE-003".equals(code)) {
				codeMessageService.logInfo(code, probe.getId(), script);
			} else {
				codeMessageService.logInfo(code, probe.getId(), message);
			}
			appiumTestService.markFailed(result);
			return;
		}

		List<TestResultDataWrapper> resultData = new LinkedList<>();
		List<TestResultGrade> grades = new LinkedList<>();
		try {
			JSONArray timeArr = new JSONArray(message);
			for (int i = 0; i < timeArr.length(); ++i) {
				JSONObject timeObj = timeArr.getJSONObject(i);
				long time = timeObj.getLong("time");
				String video = timeObj.optString("video", ""+(i+1));
				
				String testName = test.getTestName();
				if (testName != null) {
					testName = testName.replace("{iteration}", ""+(i+1)).replace("{video}", video);
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
		} catch (JSONException e) {
			LOGGER.error("[DST-YOUTUBE-002] Error getting time from json", e);
			codeMessageService.logInfo("DST-YOUTUBE-002", probe.getId(), script);
			appiumTestService.markFailed(result);
			return;
		}

		Map<String, List<TestResultDataWrapper>> dataMap = Collections.singletonMap(test.getIndexText(), resultData);
		Gson gson = new Gson();

		result.setResultGrade(Utils.joinGrades(grades));
		result.setState(TestResultState.EXECUTED);
		result.setTestResultDatetime(new Date());
		result.setTestResultData(gson.toJson(dataMap, Map.class));
		result.setTestResultLapse(1.0f * json.optLong("time", dt));
		
		LOGGER.info("Updating test result [{}]", result.getId());
		testResultService.save(result);
		LOGGER.info("Youtube test executed successfully and stored in [{}]", result);
	}
	
	private void addVideo(StringBuilder script, String video) {
		if (StringUtils.isEmpty(video)) {
			return;
		}
		script.append(" \"");
		script.append(video);
		script.append("\"");
	}

}
