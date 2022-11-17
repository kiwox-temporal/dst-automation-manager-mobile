package net.kiwox.manager.dst.service.interfaces;

import net.kiwox.manager.dst.domain.TestResult;
import org.json.JSONObject;

public interface IEntelPeruTestService {

    void runTest(long probeId, long testResultId);

    JSONObject readOutput(Process process);

    void markFailed(TestResult testResult);


}
