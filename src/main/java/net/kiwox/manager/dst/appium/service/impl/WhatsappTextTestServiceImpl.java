package net.kiwox.manager.dst.appium.service.impl;


import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import net.kiwox.manager.dst.appium.service.interfaces.IWhatsappTextTestService;
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
public class WhatsappTextTestServiceImpl implements IWhatsappTextTestService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WhatsappTextTestServiceImpl.class);
	
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
		String contact = parameterService.getString(Parameter.WHATSAPP_CONTACT);
		
		StringBuilder script = new StringBuilder();
		script.append(result.getTest().getScript());
		script.append(" -udid ");
		script.append(probe.getUdid());
		script.append(" -tr ");
		script.append(result.getId());
		script.append(" -contact \"");
		script.append(contact);
		script.append("\"");
		
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
			codeMessageService.logInfo("DST-WATEXT-001", probe.getId(), script);
			LOGGER.error("[DST-WATEXT-001] Error starting process", e);
			appiumTestService.markFailed(result);
			return;
		}
		
		JSONObject json = appiumTestService.readOutput(process);
		if (json == null || !json.has("time")) {
			codeMessageService.logInfo("DST-WATEXT-002", probe.getId(), script);
			appiumTestService.markFailed(result);
			return;
		}
		long dt = System.currentTimeMillis() - start;
		LOGGER.info("Finished running script ({}ms): {}", dt, script);

		String code = json.optString("code");
		String message = json.optString("message");
		if (json.optBoolean("error")) {
			if ("DST-WATEXT-003".equals(code)) {
				codeMessageService.logInfo(code, probe.getId(), script);
			} else if ("DST-WATEXT-005".equals(code)) {
				codeMessageService.logInfo(code, probe.getId(), contact);
			} else {
				codeMessageService.logInfo(code, probe.getId(), message);
			}
			appiumTestService.markFailed(result);
			return;
		}
		
		long time;
		try {
			time = json.getLong("time");
		} catch (JSONException e) {
			LOGGER.error("[DST-WATEXT-002] Error getting time from json", e);
			codeMessageService.logInfo("DST-WATEXT-002", probe.getId(), script);
			appiumTestService.markFailed(result);
			return;
		}
		
		
		TestResultGrade grade = Utils.getGrade(time, test.getTestScales());
		TestResultDataWrapper data = new TestResultDataWrapper();
		data.setTestName(test.getTestName());
		data.setValue(time);
		data.setMedition("ms");
		data.setResultGrade(grade);
		
		Map<String, List<TestResultDataWrapper>> dataMap = Collections.singletonMap(test.getIndexText(), Collections.singletonList(data));
		Gson gson = new Gson();
		
		result.setResultGrade(grade);
		result.setState(TestResultState.EXECUTED);
		result.setTestResultDatetime(new Date());
		result.setTestResultData(gson.toJson(dataMap, Map.class));
		result.setTestResultLapse(1.0f * time);
		
		LOGGER.info("Updating test result [{}]", result.getId());
		testResultService.save(result);
		LOGGER.info("Whatsapp text test executed successfully and stored in [{}]", result);
	}

}
