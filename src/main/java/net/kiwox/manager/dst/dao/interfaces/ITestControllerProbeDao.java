package net.kiwox.manager.dst.dao.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.kiwox.manager.dst.domain.TestControllerProbe;

@Repository
public interface ITestControllerProbeDao extends JpaRepository<TestControllerProbe, Long> {


    @Query("SELECT tcp" +
            " FROM TestControllerProbe tcp" +
            " INNER JOIN FETCH tcp.controllerProbe cp" +
            " WHERE cp.id = :controllerProbeId" +
            " AND cp.active = 1")
    List<TestControllerProbe> findAllTestsByControllerProbe(@Param("controllerProbeId") long controllerProbeId);
    
    @Query("SELECT tcp"
    		+ " FROM TestControllerProbe tcp"
    		+ " INNER JOIN FETCH tcp.controllerProbe cp"
    		+ " INNER JOIN FETCH cp.controller c"
    		+ " WHERE tcp.active = 1"
    		+ " AND cp.active = 1"
    		+ " AND c.name = :controllerName")
    List<TestControllerProbe> findByControllerName(@Param("controllerName") String controllerName);

}
