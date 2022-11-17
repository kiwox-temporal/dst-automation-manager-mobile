package net.kiwox.manager.dst.appium.service.interfaces;

import net.kiwox.manager.dst.domain.ControllerProbe;
import net.kiwox.manager.dst.domain.TestResult;

public interface ISpeedTestService {
	
	void runTest(ControllerProbe probe, TestResult result);
	
}
