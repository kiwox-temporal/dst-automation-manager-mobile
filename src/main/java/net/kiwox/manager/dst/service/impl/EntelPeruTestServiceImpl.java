package net.kiwox.manager.dst.service.impl;

import com.google.gson.Gson;
import net.kiwox.manager.dst.appium.service.interfaces.IEntelPeruAppService;
import net.kiwox.manager.dst.domain.ControllerProbe;
import net.kiwox.manager.dst.domain.Test;
import net.kiwox.manager.dst.domain.TestResult;
import net.kiwox.manager.dst.enums.TestResultGrade;
import net.kiwox.manager.dst.enums.TestResultState;
import net.kiwox.manager.dst.enums.TestType;
import net.kiwox.manager.dst.service.interfaces.*;
import net.kiwox.manager.dst.utils.AppiumTestErrorOutput;
import net.kiwox.manager.dst.wrappers.TestResultDataWrapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class EntelPeruTestServiceImpl implements IEntelPeruTestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntelPeruTestServiceImpl.class);

    @Autowired
    private IControllerProbeService controllerProbeService;
    @Autowired
    private ITestResultService testResultService;
    @Autowired
    private IEntelPeruAppService entelPeruAppService;

    @Override
    public void runTest(long probeId, long testResultId) {
        ControllerProbe controllerProbeFind = controllerProbeService.getById(probeId);
        TestResult testResultFind = testResultService.getById(testResultId);
        Test test = testResultFind.getTest();

        testResultFind.setDisconnected(0);
        if (!test.isActive()) {
            LOGGER.info("Test result [{}] is not active (test [{}]). Marking as failed...", testResultId, test.getId());
            testResultFind.setAttempt(test.getMaxExecutions());
            markFailed(testResultFind);
            return;
        }
        entelPeruAppService.runTest(controllerProbeFind, testResultFind);

    }

    @Override
    public JSONObject readOutput(Process process) {
        new Thread(new AppiumTestErrorOutput(process)).start();
        StringBuilder json = new StringBuilder();
        try (InputStream is = process.getInputStream();
             Reader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            boolean readJson = false;
            while ((line = reader.readLine()) != null) {
                if (readJson) {
                    json.append(line);
                } else if (line.equalsIgnoreCase("dst test result")) {
                    readJson = true;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error reading process", e);
            return null;
        }

        try {
            return new JSONObject(json.toString());
        } catch (JSONException e) {
            LOGGER.error("Error parsing JSON, received: {}", json);
            LOGGER.error("Error parsing JSON", e);
        }

        return null;
    }

    @Override
    public void markFailed(TestResult testResult) {
        Test test = testResult.getTest();
        if (test.getMaxExecutions() > testResult.getAttempt()) {
            // Seguiremos tratando
            testResult.setAttempt(testResult.getAttempt() + 1);
            LOGGER.info("Incresing attemps for test result [{}]", testResult.getId());
            testResultService.save(testResult);
            return;
        }
        Gson gson = new Gson();
        String medition = "ms";
        if (test.getType() == TestType.UPLOAD || test.getType() == TestType.DOWNLOAD) {
            medition = "Mbps";
        }

        List<TestResultDataWrapper> resultData = new LinkedList<>();
        for (int i = 1; i <= test.getIteration(); i++) {
            TestResultDataWrapper data = new TestResultDataWrapper();
            String testName = test.getTestName();
            if (testName != null) {
                testName = testName.replace("{iteration}", String.valueOf(i));
            }
            data.setTestName(testName);
            data.setValue(0.0f);
            data.setMedition(medition);
            data.setResultGrade(TestResultGrade.FAILED);
            resultData.add(data);
        }

        Map<String, List<TestResultDataWrapper>> dataMap = Collections.singletonMap(test.getIndexText(), resultData);
        testResult.setTestResultData(gson.toJson(dataMap, Map.class));

        LOGGER.info("Mark test result [{}] as failed after many retries", testResult.getId());
        testResult.setResultGrade(TestResultGrade.FAILED);
        testResult.setState(TestResultState.EXECUTED);
        testResult.setTestResultDatetime(new Date());
        testResult.setTestResultLapse(0.0f);
        testResultService.save(testResult);
    }


}


