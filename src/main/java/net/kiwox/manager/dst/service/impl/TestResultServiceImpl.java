package net.kiwox.manager.dst.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.kiwox.manager.dst.dao.interfaces.IControllerProbeDao;
import net.kiwox.manager.dst.dao.interfaces.ITestDao;
import net.kiwox.manager.dst.domain.ControllerProbe;
import net.kiwox.manager.dst.domain.Test;
import net.kiwox.manager.dst.domain.TestControllerProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.kiwox.manager.dst.dao.interfaces.ITestResultDao;
import net.kiwox.manager.dst.domain.TestResult;
import net.kiwox.manager.dst.service.interfaces.ITestResultService;

import static net.kiwox.manager.dst.utils.Utils.*;

@Service
public class TestResultServiceImpl implements ITestResultService {

    @Autowired
    private ITestResultDao testResultDao;

    @Autowired
    private ITestDao testDao;

    @Autowired
    private IControllerProbeDao controllerProbeDao;

    @Override
    @Transactional(readOnly = true)
    public List<TestResult> getPendingByControllerProbeDirect(long controllerProbeId) {
        return testResultDao.getPendingByControllerProbeDirect(controllerProbeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestResult> getPendingByControllerProbe(long probeId) {
        return testResultDao.getPendingByControllerProbe(probeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestResult> getByWorkorder(long workorderId) {
        return testResultDao.getByWorkorder(workorderId);
    }

    @Override
    @Transactional(readOnly = true)
    public TestResult getById(long id) {
        return testResultDao.getById(id);
        //return testResultDao.getOne(id);
    }

    @Override
    @Transactional
    public void save(TestResult testResult) {
        testResultDao.saveAndFlush(testResult);
    }

    @Override
    @Transactional
    public List<TestResult> createTestsForExecute(ControllerProbe controllerProbe, List<Test> testsForCreated, int count) {
        List<TestResult> testResultsForCreate = new ArrayList<>();
        IntStream.range(0, count)
                .forEach(i -> {
                    List<TestResult> items = TestResult.buildTuples(testsForCreated, controllerProbe);
                    testResultsForCreate.addAll(items);
                });
        return testResultDao.saveAll(testResultsForCreate);
    }


    // Deprecated
    @Override
    public List<TestResult> addTests(Map<String, Object> paramsForCreate, int count) {
        try {
            List<TestResult> results = new ArrayList<>();
            List<TestResult> finalResults = new ArrayList<>();
            List<String> testsNames = (List<String>) paramsForCreate.getOrDefault("testsName", new ArrayList<>());
            //String testName = (String) paramsForCreate.getOrDefault("testName", "");
            long controllerProbeId = (long) paramsForCreate.getOrDefault("controllerProbeId", 0);
            ControllerProbe controllerProbe = controllerProbeDao.getById(controllerProbeId);
            for (String testName : testsNames) {
                Test test = testDao.getByTestName(testName);
                if (!isNull(test) && !isNull(controllerProbe)) {
                    IntStream.range(0, count).
                            forEach(i -> {
                                TestResult item = TestResult.buildTuple(test, controllerProbe);
                                //item= testResultDao.saveAndFlush(item);
                                results.add(item);
                            });
                    finalResults.addAll(testResultDao.saveAll(results));
                    results.clear();
                    //results = testResultDao.saveAll(results);
                }
                //return resultsCreated;
            }
            return finalResults;
        } catch (Exception e) {
            return null;
        }
    }

}
