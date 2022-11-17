package net.kiwox.manager.dst.dao.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.kiwox.manager.dst.domain.TestResult;

@Repository
public interface ITestResultDao extends JpaRepository<TestResult, Long> {

    @Query(value = "SELECT tr"
            + " FROM TestResult tr"
            + " JOIN tr.controllerProbe probe"
            + " INNER JOIN FETCH tr.workorderSiteService wss"
            + " INNER JOIN FETCH wss.workorder w"
            + " INNER JOIN FETCH wss.site"
            + " INNER JOIN FETCH tr.test t"
            + " WHERE probe.id = :probeId"
            + " AND tr.state = 'PENDING'"
            + " AND t.testName <> 'App Entel Prepago Saldo' "
            + " AND w.plannedDatetime < CURRENT_TIMESTAMP"
            + " AND w.state IN ('TESTS_PROGRAMED', 'RUNNING_TESTS')")
    List<TestResult> getPendingByControllerProbe(@Param("probeId") long probeId);


    TestResult getById(long id);
    @Query(value = "SELECT tr"
            + " FROM TestResult tr"
            + " JOIN tr.workorderSiteService wss"
            + " JOIN wss.workorder w"
            + " WHERE w.id = :workorderId")
    List<TestResult> getByWorkorder(@Param("workorderId") long workorderId);

    @Query(value = "SELECT tr " +
            "FROM TestResult tr " +
            "JOIN FETCH tr.controllerProbe r " +
            "JOIN FETCH tr.test t " +
            "WHERE r.id = :controllerProbeId " +
            "AND tr.state = 'PENDING' ")
    List<TestResult> getPendingByControllerProbeDirect(@Param("controllerProbeId") long controllerProbeId);
}
