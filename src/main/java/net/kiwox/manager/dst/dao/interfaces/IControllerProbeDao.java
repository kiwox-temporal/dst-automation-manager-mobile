package net.kiwox.manager.dst.dao.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.kiwox.manager.dst.domain.ControllerProbe;

@Repository
public interface IControllerProbeDao extends JpaRepository<ControllerProbe, Long> {
	
	@Query(value = "SELECT cp.*"
			+ " FROM `Controller` c"
			+ " JOIN `ControllerProbe` cp ON c.`id` = cp.`controller`"
			+ " WHERE c.`name` = :controllerName",
			nativeQuery = true)
	List<ControllerProbe> getByControllerName(@Param("controllerName") String controllerName);

	ControllerProbe getById(long id);


	//List<ControllerProbe> findAll
	
}
