package net.kiwox.manager.dst.dao.interfaces;

import net.kiwox.manager.dst.domain.Test;
import net.kiwox.manager.dst.domain.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITestDao extends JpaRepository<Test, Long> {

    Test getByTestName(String testName);
}
