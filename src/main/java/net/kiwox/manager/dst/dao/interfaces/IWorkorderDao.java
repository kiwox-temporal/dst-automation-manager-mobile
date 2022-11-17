package net.kiwox.manager.dst.dao.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.kiwox.manager.dst.domain.Workorder;

@Repository
public interface IWorkorderDao extends JpaRepository<Workorder, Long> {
	
	@Query(value = "SELECT w"
			+ " FROM TestResult tr"
			+ " JOIN tr.workorderSiteService wss"
			+ " JOIN wss.workorder w"
			+ " WHERE tr.id = :testResultId")
	Workorder getByTestResult(@Param("testResultId") long testResultId);
	
}
