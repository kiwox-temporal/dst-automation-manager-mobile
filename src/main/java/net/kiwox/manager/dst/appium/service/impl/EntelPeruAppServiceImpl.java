package net.kiwox.manager.dst.appium.service.impl;

import com.google.gson.Gson;
import net.kiwox.manager.dst.appium.service.interfaces.IEntelPeruAppService;
import net.kiwox.manager.dst.domain.ControllerProbe;
import net.kiwox.manager.dst.domain.Test;
import net.kiwox.manager.dst.domain.TestResult;
import net.kiwox.manager.dst.domain.TestResultDetail;
import net.kiwox.manager.dst.enums.Parameter;
import net.kiwox.manager.dst.enums.TestResultGrade;
import net.kiwox.manager.dst.enums.TestResultState;
import net.kiwox.manager.dst.service.interfaces.*;
import net.kiwox.manager.dst.utils.Utils;
import net.kiwox.manager.dst.wrappers.EntelTestResultDataWrapper;
import net.kiwox.manager.dst.wrappers.EntelTestResultDetailDataWrapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EntelPeruAppServiceImpl implements IEntelPeruAppService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntelPeruAppServiceImpl.class);
    @Autowired
    private IParameterService parameterService;
    @Autowired
    private ICodeMessageService codeMessageService;
    @Autowired
    private IAppiumTestService appiumTestService;
    @Autowired
    private ITestResultService testResultService;

    @Autowired
    private ICustomMailService customMailService;

    @Value("${smrpa.timeout.mail.subject.timeoutflow}")
    private String subjectTimeOutFlow;

    @Value("${test.name.login}")
    private String testTypeLogin;

    @Value("${smrpa.timeout.mail.message.timeoutflow}")
    private String messageTimeOutFlow;

    private StringBuilder parametizerScriptDST(ControllerProbe controllerProbe, TestResult testResult) {
        StringBuilder script = new StringBuilder();
        script.append(testResult.getTest().getScript());
        script.append(" -udid ");
        script.append(controllerProbe.getUdid());
        script.append(" -tr ");
        script.append(testResult.getId());
        script.append(" -phone \"");
        script.append(controllerProbe.getPhone_number());
        //script.append(parameterService.getString(Parameter.ENTEL_PERU_APP_POST_PAID_PHONE_NUMBER));
        script.append("\" -code \"");
        script.append(controllerProbe.getVerification_code());

        if (testResult.getTest().getPaymentType().equals(testTypeLogin)) {
            script.append("\" -popup_x ");
            script.append(controllerProbe.getPopup_close_point_x());
            script.append(" -popup_y ");
            script.append(controllerProbe.getPopup_close_point_y());
        } else {
            script.append("\"");
        }

        //script.append(parameterService.getString(Parameter.ENTEL_PERU_APP_VERIFICATION_CODE));


        return script;
    }

    @Override
    public void runTest(ControllerProbe controllerProbe, TestResult testResult) {
        LOGGER.info("TEST ENTEL PERU....");
        testResult.setCreatedOn(new Date()); // Updating created_on date according to init of test execution
        StringBuilder script = parametizerScriptDST(controllerProbe, testResult);
        Test test = testResult.getTest();
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
            String failedMessage = String.format("[DST-ENTEL-PERU-APP-ERROR-001-01] Error starting process : %s", e.getMessage());
            codeMessageService.logInfo("[DST-ENTEL-PERU-APP-ERROR-001-01]", controllerProbe.getId(), script);
            LOGGER.error(failedMessage, e);
            failedExecution(testResult, failedMessage);
            //appiumTestService.markFailed(testResult);
            return;
        }

        JSONObject json = appiumTestService.readOutput(process);
        if (json == null || !json.has("time")) {
            String failedMessage = "[DST-ENTEL-PERU-APP-ERROR-001-02] Error to search time field";
            codeMessageService.logInfo("[DST-ENTEL-PERU-APP-ERROR-001-02]", controllerProbe.getId(), script);
            failedExecution(testResult, failedMessage);
            //appiumTestService.markFailed(testResult);
            return;
        }

        long dt = System.currentTimeMillis() - start;
        LOGGER.info("Finished running script ({}ms): {}", dt, script);

        boolean catchErrorMobileTest = false;
        String code = json.optString("code");
        String message = json.optString("message");
        if (json.optBoolean("error")) {
            String defaultErrorMessage = String.format("code: %s,\t device: %s,\t message: %s", code, controllerProbe.getUdid(), message);
            String errorMessage = json.optString("errorMessage", defaultErrorMessage);
            codeMessageService.logInfo(code, controllerProbe.getId(), errorMessage);
            catchErrorMobileTest = true;
            //failedExecution(testResult, errorMessage);
            //appiumTestService.markFailed(testResult);
            //return;
        }

        long time;
        try {
            time = json.getLong("time");
        } catch (JSONException e) {
            String failedMessage = String.format("[DST-ENTEL-PERU-APP-ERROR-001-03] Error getting time from json: %s", e.getMessage());
            LOGGER.error(failedMessage, e);
            codeMessageService.logInfo("[DST-ENTEL-PERU-APP-ERROR-001-03]", controllerProbe.getId(), script);
            failedExecution(testResult, failedMessage);
            //appiumTestService.markFailed(testResult);
            return;
        }
        // retrieve JSON Object {List<TestResultDetail>}
        JSONArray testDetails;
        try {
            testDetails = json.getJSONArray("details");
        } catch (JSONException e) {
            String failedMessage = String.format("[DST-ENTEL-PERU-APP-ERROR-001-04] Error getting details list from json: %s", e.getMessage());
            LOGGER.error(failedMessage, e);
            codeMessageService.logInfo("[DST-ENTEL-PERU-APP-ERROR-001-04]", controllerProbe.getId(), script);
            failedExecution(testResult, failedMessage);
            //appiumTestService.markFailed(testResult);
            return;
        }

        TestResultGrade resultGrade = Utils.getGrade(time, test.getTestScales());
        EntelTestResultDataWrapper data = new EntelTestResultDataWrapper();
        data.setCode(code);
        data.setMessage(message);
        data.setMedition("ms");
        data.setTime(time);
        data.setResult(resultGrade);


        //Map<String, List<TestResultDataWrapper>> dataMap = Collections.singletonMap(test.getIndexText(), Collections.singletonList(data));
        Gson gson = new Gson();
        int indexFailed = 0;
        for (int i = 0; i < testDetails.length(); i++) {

            TestResultDetail itemForCreate = new TestResultDetail();
            JSONObject jsonItem = testDetails.getJSONObject(i);

            String codeForDetail = jsonItem.getString("code");
            String descForDetail = jsonItem.getString("detail");
            long detailTime = jsonItem.getLong("time");

            itemForCreate.setCode(codeForDetail);
            itemForCreate.setDetail(descForDetail);

            EntelTestResultDetailDataWrapper dataItemDetail = new EntelTestResultDetailDataWrapper();

            boolean hasError = jsonItem.getBoolean("errorDetected");
            TestResultGrade gradeDetail = Utils.getGrade(detailTime, test.getTestScales(), hasError);

            dataItemDetail.setResult(gradeDetail);
            if (hasError) {
                resultGrade = TestResultGrade.FAILED;
                data.setResult(TestResultGrade.FAILED);
                dataItemDetail.setResult(TestResultGrade.FAILED);
                if (gradeDetail.equals(TestResultGrade.FAILED)) {
                    indexFailed = i + 1;
                }
            }

            dataItemDetail.setCode(codeForDetail);
            dataItemDetail.setDetail(descForDetail);
            dataItemDetail.setTime(detailTime);
            dataItemDetail.setMedition("ms");


            itemForCreate.setResultGrade(gradeDetail);
            itemForCreate.setState(TestResultState.EXECUTED);
            itemForCreate.setTestResultData(gson.toJson(dataItemDetail));
            itemForCreate.setAttempt(0);
            itemForCreate.setDisconnected(0);
            itemForCreate.setCreatedOn(new Date());
            itemForCreate.setTestResultDatetime(new Date());
            itemForCreate.setTestResultLapse(1.0f * detailTime);

            testResult.addTestResultDetail(itemForCreate);
            data.addItemDetail(dataItemDetail);

        }
        testResult.setResultGrade(resultGrade);
        if (indexFailed > 0 && !catchErrorMobileTest) {
            EntelTestResultDetailDataWrapper failedItem = new EntelTestResultDetailDataWrapper();
            failedItem.setCode("DST-ENTEL_PERU-T000-D001");
            failedItem.setDetail(String.format("Ejecución de P% excedió el tiempo maximo estimado de ejecución.", indexFailed));
            failedItem.setMedition("ms");
            failedItem.setResult(TestResultGrade.FAILED);
            failedItem.setTime(0f);
            data.addItemDetail(failedItem);
        }

        //testResult.setTestResultDetails(testResultDetailForCreate);
        testResult.setState(TestResultState.EXECUTED);
        testResult.setTestResultDatetime(new Date());
        testResult.setTestResultData(gson.toJson(data, EntelTestResultDataWrapper.class));
        testResult.setTestResultLapse(1.0f * time);

        LOGGER.info("Updating test testResult [{}]", testResult.getId());

        String testExecutionCode = Utils.getTestExecutionCode(testResult);
        testResult.setCode(testExecutionCode);

        testResultService.save(testResult);


        List<EntelTestResultDetailDataWrapper> filterDetailList = data.getDetails()
                .stream()
                .filter(v -> !v.getCode().contains("D000"))
                .collect(Collectors.toList());

        /*float totalFlowTime = IntStream.range(0, filterDetailList.size())
                .filter(i -> i >= filterDetailList.size() - 2)
                .mapToObj(i -> filterDetailList.get(i))
                .map(v -> v.getTime())
                .reduce(Float::sum)
                .orElse(0f);*/

        float totalFlowTime = data.getDetails()
                .stream()
                .map(v -> v.getTime())
                .reduce(Float::sum)
                .orElse(0f);

        int countDetailsItems;
        float slaTime;
        if (!test.getExecution_code().contains("AALOG")) {
            countDetailsItems = filterDetailList.size();
            slaTime = filterDetailList
                    .get(countDetailsItems - 1)
                    .getTime();
        } else { // Authentication test
            countDetailsItems = data.getDetails().size();
            slaTime = data.getDetails()
                    .get(countDetailsItems - 1)
                    .getTime();
        }

                /*.stream()
                .map(v -> v.getTime())
                .reduce(Float::sum)
                .orElse(0f);*/

        // validate stages before of would send notification mail
        Map<String, Object> mailParams;


        float totalFlowTimeSeconds = time / 1000f;
        if (totalFlowTime > test.getTimeout_flow()) {
            String timeoutFlowMailSubject = subjectTimeOutFlow
                    .replaceAll("X", test.getDescription());
            String timeoutFlowMailMessage = messageTimeOutFlow
                    .replaceAll("X", String.format("%.2f", totalFlowTimeSeconds));
            //.replaceAll("Y", String.valueOf(time));
            mailParams = new HashMap<>();
            mailParams.put("subject", timeoutFlowMailSubject);
            mailParams.put("message", timeoutFlowMailMessage);
            mailParams.put("requestId", testExecutionCode);

            customMailService.run(mailParams);
        }
        float slaTimeSeconds = slaTime / 1000f;
        if (slaTime > test.getTimeout_sla()) {

            String mailSubjectSla = test.getMail_notification_subject();
            String mailMessageSla = test.getMail_notification_message()
                    .replaceAll("X", String.format("%.2f", slaTimeSeconds));
            //.replaceAll("Y", String.valueOf(time));
            mailParams = new HashMap<>();
            mailParams.put("subject", mailSubjectSla);
            mailParams.put("message", mailMessageSla);
            mailParams.put("requestId", testExecutionCode);

            customMailService.run(mailParams);
        }


        //LOGGER.info("Entel Peru App - Sign In test executed successfully and stored in [{}]", testResult);


        //customMailService.run(mailParams);
    }

    private void failedExecution(TestResult testResult, String errorMessage) {
        Gson gson = new Gson();

        String errorHeaderCode = "DST-ENTEL_PERU-E000-001", errorDetailCode = "DST-ENTEL_PERU-E000-D001", errorMedition = "ms";
        float errorTime = 0.0f;


        // Header Error
        testResult.setResultGrade(TestResultGrade.FAILED);
        testResult.setState(TestResultState.EXECUTED);
        testResult.setTestResultDatetime(new Date());

        EntelTestResultDataWrapper failedItemWrapper = new EntelTestResultDataWrapper();
        failedItemWrapper.setCode(errorHeaderCode);
        failedItemWrapper.setMessage(errorMessage);
        failedItemWrapper.setMedition(errorMedition);
        failedItemWrapper.setResult(TestResultGrade.FAILED);
        failedItemWrapper.setTime(errorTime);

        testResult.setTestResultData(gson.toJson(failedItemWrapper, EntelTestResultDataWrapper.class));
        testResult.setTestResultLapse(errorTime);


        // Detail item Error
        TestResultDetail testResultDetail = new TestResultDetail();
        testResultDetail.setCode(errorDetailCode);
        testResultDetail.setDetail(errorMessage);
        testResultDetail.setResultGrade(TestResultGrade.FAILED);
        testResultDetail.setState(TestResultState.EXECUTED);

        EntelTestResultDetailDataWrapper failedItemDetailWrapper = new EntelTestResultDetailDataWrapper();
        failedItemDetailWrapper.setCode(errorDetailCode);
        failedItemDetailWrapper.setDetail(errorMessage);
        failedItemDetailWrapper.setTime(errorTime);
        failedItemDetailWrapper.setMedition(errorMedition);
        failedItemDetailWrapper.setResult(TestResultGrade.FAILED);

        testResultDetail.setTestResultData(gson.toJson(failedItemDetailWrapper));
        testResultDetail.setAttempt(0);
        testResultDetail.setDisconnected(0);
        testResultDetail.setCreatedOn(new Date());
        testResultDetail.setTestResultDatetime(new Date());
        testResultDetail.setTestResultLapse(0.0f);

        testResult.addTestResultDetail(testResultDetail);

        testResultService.save(testResult);
    }

}
