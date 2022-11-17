package net.kiwox.manager.dst.appium.service.interfaces;

import net.kiwox.manager.dst.domain.ControllerProbe;
import net.kiwox.manager.dst.domain.TestResult;

public interface ITikTokTestService {
	
	void runTest(ControllerProbe probe, TestResult result);
	
}
