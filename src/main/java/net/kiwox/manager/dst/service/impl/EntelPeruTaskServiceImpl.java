package net.kiwox.manager.dst.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.kiwox.manager.dst.domain.ControllerProbe;
import net.kiwox.manager.dst.domain.Test;
import net.kiwox.manager.dst.domain.TestControllerProbe;
import net.kiwox.manager.dst.domain.TestResult;
import net.kiwox.manager.dst.enums.Parameter;
import net.kiwox.manager.dst.service.interfaces.IEntelPeruTaskService;
import net.kiwox.manager.dst.service.interfaces.IEntelPeruTestService;
import net.kiwox.manager.dst.service.interfaces.IParameterService;
import net.kiwox.manager.dst.service.interfaces.ITestControllerProbeService;
import net.kiwox.manager.dst.service.interfaces.ITestResultService;
import net.kiwox.manager.dst.utils.AppiumTestTask;
import net.kiwox.manager.dst.utils.IAppiumTestTaskListener;
import net.kiwox.manager.dst.wrappers.AppiumTestTaskItem;

@Service
public class EntelPeruTaskServiceImpl implements IEntelPeruTaskService, IAppiumTestTaskListener {

    private static final String TEST_NAME_KEY = "testName";
    private static final String TESTS_NAMES_KEY = "testsName";
    private static final String CONTROLLER_PROBE_ID_KEY = "controllerProbeId";

    private static final String TEST_NAME_VALUE_SIGN_IN_000 = "App Entel Prepago Saldo 00";


    //TODO: ADD TEST IMPLEMENTED IN DST MOVIL
    //private static final String TEST_NAME_VALUE_VERIFY_POST_PAID_BALANCE_000 = "App Entel Prepago Saldo 00";
    private static final String TEST_NAME_VALUE_VERIFY_POST_PAID_BALANCE_001 = "App Entel Prepago Saldo 01";

    private static final List<String> TEST_NAMES_VALUES = Arrays.asList(
            "App Entel Prepago Saldo 00",
            "App Entel Prepago Saldo 01",
            "App Entel Prepago Saldo 02",
            "App Entel Prepago Saldo 03",
            "App Entel Prepago Saldo 04",
            //"App Entel Prepago Saldo 05",
            "App Entel Prepago Saldo 06",
            "App Entel Prepago Saldo 07",
            "App Entel Prepago Saldo 08",
            "App Entel Prepago Saldo 09",
            "App Entel Prepago Saldo 10"
    );
    private static final int COUNT_TEST_RESULT_REGISTERS = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(EntelPeruTaskServiceImpl.class);

    private static Map<Long, AppiumTestTask> tasks = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    private ITestControllerProbeService testControllerProbeService;

    @Autowired
    private ITestResultService testResultService;
    @Autowired
    private IEntelPeruTestService enterlPeruTestService;
    @Autowired
    private IParameterService parameterService;

    @Value("${controller.name}")
    private String controllerName;

    //	private Map<Long, Executor> executors = new HashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public void run() {

        LOGGER.info("THREAD ENTEL PERU....");

        //List<TestControllerProbe> controllerProbesForExecute = testControllerProbeService.findAllTestControllerProbe();
        List<TestControllerProbe> controllerProbesForExecute = testControllerProbeService.findByControllerName(controllerName);

        int apnMaxRetries = parameterService.getInt(Parameter.APN_MAX_RETRIES);

        Map<ControllerProbe, List<TestControllerProbe>> testPerControllerProbe = controllerProbesForExecute.stream()
                .collect(Collectors.groupingBy(TestControllerProbe::getControllerProbe));

        testPerControllerProbe.forEach((controllerProbe, testControllerProbeList) -> {
            AppiumTestTask taskManager = tasks.computeIfAbsent(controllerProbe.getId(), k -> {
                LOGGER.debug("TASK NULL PARA {}", controllerProbe);
                return new AppiumTestTask(this, k, apnMaxRetries);
            });
            List<AppiumTestTaskItem> taskItems = new LinkedList<>();
            List<Test> testsForCreated = testControllerProbeList.stream()
                    .map(v -> v.getTest())
                    .collect(Collectors.toList());
            List<TestResult> testResultsCreated = testResultService.createTestsForExecute(controllerProbe, testsForCreated, COUNT_TEST_RESULT_REGISTERS);

            testResultsCreated.stream()
                    .forEach(testResult -> {
                        AppiumTestTaskItem item = new AppiumTestTaskItem();
                        item.setId(testResult.getId());
                        item.setOnDemand(true);
                        taskItems.add(item);
                    });
            taskManager.updateQueue(taskItems);
            if (!taskManager.isRunning()) {
                executorService.execute(taskManager);
            }
        });
    }

    @Override
    public boolean runTest(long probeId, AppiumTestTaskItem item, boolean onlyPing) {
        enterlPeruTestService.runTest(probeId, item.getId());
        return true;
    }

    @Override
    public void markDisconnected(long testResultId) {
        TestResult testResult = testResultService.getById(testResultId);
        testResult.setDisconnected(1);
        enterlPeruTestService.markFailed(testResult);
    }
}
