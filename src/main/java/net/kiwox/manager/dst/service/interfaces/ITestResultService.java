package net.kiwox.manager.dst.service.interfaces;

import java.util.List;
import java.util.Map;

import net.kiwox.manager.dst.domain.ControllerProbe;
import net.kiwox.manager.dst.domain.Test;
import net.kiwox.manager.dst.domain.TestControllerProbe;
import net.kiwox.manager.dst.domain.TestResult;

public interface ITestResultService {


	List<TestResult> getPendingByControllerProbeDirect(long controllerProbeId);
	List<TestResult> getPendingByControllerProbe(long probeId);
	
	List<TestResult> getByWorkorder(long workorderId);
	
	TestResult getById(long id);
	
	void save(TestResult testResult);

    List<TestResult> addTests(Map<String, Object> paramsForCreate, int count);

	List<TestResult> createTestsForExecute(ControllerProbe controllerProbe, List<Test> testsForCreated, int count);
}
