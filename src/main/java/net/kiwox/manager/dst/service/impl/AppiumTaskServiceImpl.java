package net.kiwox.manager.dst.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.kiwox.manager.dst.domain.ControllerProbe;
import net.kiwox.manager.dst.domain.TestResult;
import net.kiwox.manager.dst.domain.WorkorderSiteService;
import net.kiwox.manager.dst.enums.Parameter;
import net.kiwox.manager.dst.service.interfaces.IAppiumTaskService;
import net.kiwox.manager.dst.service.interfaces.IAppiumTestService;
import net.kiwox.manager.dst.service.interfaces.IControllerProbeService;
import net.kiwox.manager.dst.service.interfaces.IParameterService;
import net.kiwox.manager.dst.service.interfaces.ITestResultService;
import net.kiwox.manager.dst.utils.AppiumTestTask;
import net.kiwox.manager.dst.utils.IAppiumTestTaskListener;
import net.kiwox.manager.dst.wrappers.AppiumTestTaskItem;

@Service
public class AppiumTaskServiceImpl implements IAppiumTaskService, IAppiumTestTaskListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppiumTaskServiceImpl.class);

    private static Map<Long, AppiumTestTask> tasks = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    private IControllerProbeService controllerProbeService;
    @Autowired
    private ITestResultService testResultService;
    @Autowired
    private IAppiumTestService appiumTestService;
    @Autowired
    private IParameterService parameterService;
//	@Autowired
//	private ThreadPoolTaskExecutor executor;

    @Value("${controller.name}")
    private String controllerName;

    //	private Map<Long, Executor> executors = new HashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public void run() {
        List<ControllerProbe> probes = controllerProbeService.getByControllerName(controllerName);
        LOGGER.debug("PROBES TRAIDOS {}", probes);

        int apnMaxRetries = parameterService.getInt(Parameter.APN_MAX_RETRIES);
        for (ControllerProbe probe : probes) {
//			if(!executors.containsKey(probe.getId())) {
//				executors.put(probe.getId(), Executors.newSingleThreadExecutor());
//			}
            AppiumTestTask task = tasks.computeIfAbsent(probe.getId(), k -> {
                LOGGER.debug("TASK NULL PARA {}", probe);
                return new AppiumTestTask(this, k, apnMaxRetries);
            });

            List<AppiumTestTaskItem> items = new LinkedList<>();
            for (TestResult result : testResultService.getPendingByControllerProbe(probe.getId())) {
                WorkorderSiteService wss = result.getWorkorderSiteService();
                AppiumTestTaskItem item = new AppiumTestTaskItem();
                item.setId(result.getId());
                item.setOnDemand(true);
                item.setApn(wss.getSite().getApn());
                item.setOnDemand(wss.getWorkorder().isOnDemand());
                items.add(item);
            }
            LOGGER.debug("RESULTS PARA {} - {}", probe, items);
            task.updateQueue(items);

            if (!task.isRunning()) {
                // executor.execute(task);
                // executors.get(probe.getId()).execute(task);
                executorService.execute(task);
            }
        }
    }


	/*public void run() {
		List<ControllerProbe> probes = controllerProbeService.getByControllerName(controllerName);
		LOGGER.debug("PROBES TRAIDOS {}", probes);
		
		int apnMaxRetries = parameterService.getInt(Parameter.APN_MAX_RETRIES);
		for (ControllerProbe probe : probes) {
//			if(!executors.containsKey(probe.getId())) {
//				executors.put(probe.getId(), Executors.newSingleThreadExecutor());
//			}
			AppiumTestTask task = tasks.computeIfAbsent(probe.getId(), k -> {
				LOGGER.debug("TASK NULL PARA {}", probe);
				return new AppiumTestTask(this, k, apnMaxRetries);
			});
			
			List<AppiumTestTaskItem> items = new LinkedList<>();
			for (TestResult result : testResultService.getPendingByControllerProbe(probe.getId())) {
				WorkorderSiteService wss = result.getWorkorderSiteService();
				AppiumTestTaskItem item = new AppiumTestTaskItem();
				item.setId(result.getId());
				item.setApn(wss.getSite().getApn());
				item.setOnDemand(wss.getWorkorder().isOnDemand());
				items.add(item);
			}
			LOGGER.debug("RESULTS PARA {} - {}", probe, items);
			task.updateQueue(items);
			
			if (!task.isRunning()) {
				// executor.execute(task);
				// executors.get(probe.getId()).execute(task);
				executorService.execute(task);
			}
		}
	}*/

    @Override
    public boolean runTest(long probeId, AppiumTestTaskItem item, boolean onlyPing) {
        if (!appiumTestService.checkApn(probeId, item, onlyPing)) {
            return false;
        }
        appiumTestService.startWorkorderTest(item.getId());
        appiumTestService.runTest(probeId, item.getId());
        appiumTestService.finishWorkorderTest(item.getId());
        return true;
    }

    @Override
    public void markDisconnected(long testResultId) {
        TestResult testResult = testResultService.getById(testResultId);
        testResult.setDisconnected(1);
        appiumTestService.markFailed(testResult);
    }

}
