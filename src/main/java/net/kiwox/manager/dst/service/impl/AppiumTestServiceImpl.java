package net.kiwox.manager.dst.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.kiwox.manager.dst.appium.service.interfaces.*;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import net.kiwox.manager.dst.domain.ControllerProbe;
import net.kiwox.manager.dst.domain.Test;
import net.kiwox.manager.dst.domain.TestResult;
import net.kiwox.manager.dst.domain.Workorder;
import net.kiwox.manager.dst.enums.Parameter;
import net.kiwox.manager.dst.enums.TestResultGrade;
import net.kiwox.manager.dst.enums.TestResultState;
import net.kiwox.manager.dst.enums.TestType;
import net.kiwox.manager.dst.enums.WorkorderState;
import net.kiwox.manager.dst.service.interfaces.IAppiumTestService;
import net.kiwox.manager.dst.service.interfaces.ICodeMessageService;
import net.kiwox.manager.dst.service.interfaces.IControllerProbeService;
import net.kiwox.manager.dst.service.interfaces.IParameterService;
import net.kiwox.manager.dst.service.interfaces.ITestResultService;
import net.kiwox.manager.dst.service.interfaces.IWorkorderService;
import net.kiwox.manager.dst.utils.AppiumTestErrorOutput;
import net.kiwox.manager.dst.utils.Utils;
import net.kiwox.manager.dst.wrappers.AppiumTestTaskItem;
import net.kiwox.manager.dst.wrappers.TestResultDataWrapper;

@Service
public class AppiumTestServiceImpl implements IAppiumTestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumTestServiceImpl.class);

    @Autowired
    private ICodeMessageService codeMessageService;
    @Autowired
    private IParameterService parameterService;
    @Autowired
    private IControllerProbeService controllerProbeService;
    @Autowired
    private ITestResultService testResultService;
    @Autowired
    private IWorkorderService workorderService;
    @Autowired
    private IUploadTestService uploadTestService;
    @Autowired
    private IDownloadTestService downloadTestService;
    @Autowired
    private ISpeedTestService speedTestService;
    @Autowired
    private IFacebookTestService facebookTestService;
    @Autowired
    private IInstagramTestService instagramTestService;
    @Autowired
    private IWhatsappTextTestService whatsappTextTestService;
    @Autowired
    private IWhatsappPhotoTestService whatsappPhotoTestService;
    @Autowired
    private ITwitterTestService twitterTestService;
    @Autowired
    private INationalWebTestService nationalWebTestService;
    @Autowired
    private IInternationalWebTestService internationalWebTestService;
    @Autowired
    private IYoutubeTestService youtubeTestService;
    @Autowired
    private ITikTokTestService tikTokTestService;

    @Value("${apn.script}")
    private String apnScript;

    @Override
    public boolean checkApn(long probeId, AppiumTestTaskItem item, boolean onlyPing) {
        TestResult testResult = testResultService.getById(item.getId());
        ControllerProbe probe = controllerProbeService.getById(probeId);

        StringBuilder script = new StringBuilder();
        script.append(apnScript);
        script.append(" -udid ");
        script.append(probe.getUdid());
        script.append(" -tr ");
        script.append(testResult.getId());

        if (!onlyPing) {
            script.append(" -apn \"");
            script.append(item.getApn());
            script.append("\"");
        }

        Integer waitTimeout = parameterService.getInt(Parameter.APPIUM_WAIT_TIMEOUT);
        if (waitTimeout != null) {
            script.append(" -wt ");
            script.append(waitTimeout);
        }

        String ping = parameterService.getString(Parameter.PING_ADDRESS);
        script.append(" -ping \"");
        script.append(ping);
        script.append("\"");

        LOGGER.info("Start running script: {}", script);

        Process process;
        try {
            process = new ProcessBuilder(Utils.splitCommandLine(script.toString())).start();
        } catch (IOException e) {
            codeMessageService.logInfo("DST-SELAPN-001", item.getApn(), probeId, script);
            LOGGER.error("[DST-SELAPN-001] Error starting process", e);
            testResult.setDisconnected(0);
            markFailed(testResult);
            return false;
        }

        JSONObject json = readOutput(process);
        if (json == null) {
            codeMessageService.logInfo("DST-SELAPN-002", item.getApn(), probeId, script);
            testResult.setDisconnected(0);
            markFailed(testResult);
            return false;
        }
        LOGGER.info("Finished running script: {}", script);

        String code = json.optString("code");
        String message = json.optString("message");

        LOGGER.debug("Error retornado {}", message);

        if (json.optBoolean("error")) {
            if ("DST-SELAPN-003".equals(code)) {
                codeMessageService.logInfo(code, item.getApn(), probeId, script);
            } else {
                codeMessageService.logInfo(code, item.getApn(), probeId, message);
            }

            testResult.setDisconnected(0);
            if (("DST-SELAPN-004".equals(code) && message != null && message.toLowerCase().contains("unable to create a new remote session"))
                    || "DST-SELAPN-007".equals(code)) {
                // Mensaje de telefono no conectado
                testResult.setDisconnected(1);
            }

            markFailed(testResult);
            return false;
        }

        if (StringUtils.isEmpty(code)) {
            LOGGER.info("APN {} configured successfully in controller probe [{}]", item.getApn(), probeId);
        } else {
            codeMessageService.logInfo(code, item.getApn(), probeId);
        }

        return true;
    }

    @Override
    public void startWorkorderTest(long testResultId) {
        Workorder workorder = workorderService.getByTestResult(testResultId);
        LOGGER.debug("REVISANDO SI DEBO MOVER EL WORKORDER {}", workorder.getId());
        if (workorder.getState() == WorkorderState.TESTS_PROGRAMED) {
            workorder.setState(WorkorderState.RUNNING_TESTS);
            workorderService.save(workorder);
        }
    }

    @Override
    public void runTest(long probeId, long testResultId) {
        ControllerProbe probe = controllerProbeService.getById(probeId);
        TestResult testResult = testResultService.getById(testResultId);
        Test test = testResult.getTest();
        Workorder workorder = testResult.getWorkorderSiteService().getWorkorder();

        WorkorderState state = workorder.getState();
        if (state != WorkorderState.RUNNING_TESTS) {
            LOGGER.error("Unexpected state [{}] for workorder [{}]", state, workorder.getId());
            return;
        }

        testResult.setDisconnected(0);
        if (!test.isActive()) {
            LOGGER.info("Test result [{}] is not active (test [{}]). Marking as failed...", testResultId, test.getId());
            testResult.setAttempt(test.getMaxExecutions());
            markFailed(testResult);
            return;
        }

        switch (test.getType()) {
            case UPLOAD:
                uploadTestService.runTest(probe, testResult);
                break;
            case DOWNLOAD:
                downloadTestService.runTest(probe, testResult);
                break;
            case SPEEDTEST:
                speedTestService.runTest(probe, testResult);
                break;
            case FACEBOOK:
                facebookTestService.runTest(probe, testResult);
                break;
            case INSTAGRAM:
                instagramTestService.runTest(probe, testResult);
                break;
            case WHATSAPP_TEXT:
                whatsappTextTestService.runTest(probe, testResult);
                break;
            case WHATSAPP_PHOTO:
                whatsappPhotoTestService.runTest(probe, testResult);
                break;
            case TWITTER:
                twitterTestService.runTest(probe, testResult);
                break;
            case NATIONAL_WEB:
                nationalWebTestService.runTest(probe, testResult);
                break;
            case INTERNATIONAL_WEB:
                internationalWebTestService.runTest(probe, testResult);
                break;
            case YOUTUBE:
                youtubeTestService.runTest(probe, testResult);
                break;
            case TIKTOK:
                tikTokTestService.runTest(probe, testResult);
                break;
            default:
                LOGGER.error("Unexpected test type {}", test.getType());
                break;
        }
    }

    @Override
    public synchronized void finishWorkorderTest(long testResultId) {
        Workorder workorder = workorderService.getByTestResult(testResultId);
        List<TestResult> allResults = testResultService.getByWorkorder(workorder.getId());

        if (allResults.stream().anyMatch(r -> r.getState() != TestResultState.EXECUTED)) {
            return;
        }

        Date maxDate = new Date(0);
        List<TestResultGrade> grades = new LinkedList<>();
        for (TestResult result : allResults) {
            Date date = result.getTestResultDatetime();
            if (date != null && date.after(maxDate)) {
                maxDate = date;
            }
            grades.add(result.getResultGrade());
        }

        workorder.setState(WorkorderState.TESTS_ENDED);
        workorder.setEndDatetime(maxDate);
        workorder.setTestResult(Utils.joinGrades(grades));
        workorderService.save(workorder);
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
    public void markFailed(TestResult testResult) { // NOSONAR
        Test test = testResult.getTest();
        if (test.getMaxExecutions() > testResult.getAttempt()) {
            // Seguiremos tratando
            testResult.setAttempt(testResult.getAttempt() + 1);
            LOGGER.info("Incresing attemps for test result [{}]", testResult.getId());
            testResultService.save(testResult);
            return;
        }

        Gson gson = new Gson();

        // Matamos el test por maximo intentos
        if (StringUtils.isNotEmpty(test.getIndexText())) {
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
        } else { // speedtest
            Map<String, List<TestResultDataWrapper>> resultData = new HashMap<>();
            List<TestResultGrade> grades = new LinkedList<>();
            resultData.put("Velocidad upload", new LinkedList<>());
            resultData.put("Velocidad download", new LinkedList<>());
            resultData.put("Velocidad Ping", new LinkedList<>());

            for (int i = 1; i <= test.getIteration(); i++) {
                String testName = test.getTestName();
                if (testName != null) {
                    testName = testName.replace("{iteration}", String.valueOf(i));
                }

                TestResultDataWrapper data = getDataWrapper(testName, "Mbps");
                resultData.get("Velocidad upload").add(data);
                grades.add(data.getResultGrade());
                data = getDataWrapper(testName, "Mbps");
                resultData.get("Velocidad download").add(data);
                grades.add(data.getResultGrade());
                data = getDataWrapper(testName, "ms");
                resultData.get("Velocidad Ping").add(data);
                grades.add(data.getResultGrade());
            }
            testResult.setTestResultData(gson.toJson(resultData, Map.class));
        }

        LOGGER.info("Mark test result [{}] as failed after many retries", testResult.getId());
        testResult.setResultGrade(TestResultGrade.FAILED);
        testResult.setState(TestResultState.EXECUTED);
        testResult.setTestResultDatetime(new Date());
        testResult.setTestResultLapse(0.0f);
        testResultService.save(testResult);
    }


    private TestResultDataWrapper getDataWrapper(String testName, String medition) {
        TestResultDataWrapper data = new TestResultDataWrapper();
        data.setTestName(testName);
        data.setValue(0);
        data.setMedition(medition);
        data.setResultGrade(TestResultGrade.FAILED);
        return data;
    }

}
