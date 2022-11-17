package net.kiwox.manager.dst.scheduling;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.kiwox.manager.dst.service.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import net.kiwox.manager.dst.enums.Parameter;

@Configuration
public class AppiumTestScheduler implements SchedulingConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppiumTestScheduler.class);
	
	@Autowired
	private ThreadPoolTaskScheduler scheduler;
	
	@Autowired
	private IParameterService parameterService;
	
	@Autowired
	private IAppiumTaskService appiumTaskService;

	@Autowired
	private IEntelPeruTaskService entelPeruTaskService;
	
	@Autowired
	private IScreenshotUploadTaskService screenshotUploadTaskService;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(scheduler);
		/*taskRegistrar.addTriggerTask(
			appiumTaskService,
			triggerContext -> getNextExecutionTime(triggerContext, Parameter.APPIUM_TASK_INITIAL_DELAY, Parameter.APPIUM_TASK_EXECUTION_PERIOD)
		);*/
		taskRegistrar.addTriggerTask(
				entelPeruTaskService,
				triggerContext -> getNextExecutionTime(triggerContext, Parameter.APPIUM_TASK_INITIAL_DELAY, Parameter.APPIUM_TASK_EXECUTION_PERIOD)
		);
		taskRegistrar.addTriggerTask(
			screenshotUploadTaskService,
			triggerContext -> getNextExecutionTime(triggerContext, Parameter.SCREENSHOT_UPLOAD_TASK_INITIAL_DELAY, Parameter.SCREENSHOT_UPLOAD_TASK_EXECUTION_PERIOD)
		);
	}

	private Date getNextExecutionTime(TriggerContext triggerContext, Parameter initialDelay, Parameter executionPeriod) {
		Calendar nextExecutionTime = new GregorianCalendar();
		Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();

		int amount;
		if (lastActualExecutionTime == null) {
			nextExecutionTime.setTime(new Date());
			amount = parameterService.getInt(initialDelay);
		} else {
			nextExecutionTime.setTime(lastActualExecutionTime);
			amount = parameterService.getInt(executionPeriod);
		}

		nextExecutionTime.add(Calendar.SECOND, amount);

		LOGGER.info("Next execution time {}", nextExecutionTime.getTime());
		
		return nextExecutionTime.getTime();
	}

}
