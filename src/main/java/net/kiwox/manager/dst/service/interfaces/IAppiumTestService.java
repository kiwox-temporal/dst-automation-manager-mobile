package net.kiwox.manager.dst.service.interfaces;

import org.json.JSONObject;

import net.kiwox.manager.dst.domain.TestResult;
import net.kiwox.manager.dst.wrappers.AppiumTestTaskItem;

public interface IAppiumTestService {
	
	boolean checkApn(long probeId, AppiumTestTaskItem item, boolean onlyPing);
	
	void startWorkorderTest(long testResultId);
	
	void runTest(long probeId, long testResultId);
	
	void finishWorkorderTest(long testResultId);

	JSONObject readOutput(Process process);
	
	void markFailed(TestResult testResult);


}
