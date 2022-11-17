package net.kiwox.manager.dst.utils;

import net.kiwox.manager.dst.wrappers.AppiumTestTaskItem;

public interface IAppiumTestTaskListener {
	
	boolean runTest(long probeId, AppiumTestTaskItem item, boolean onlyPing);
	
	void markDisconnected(long testResultId);
	
}
