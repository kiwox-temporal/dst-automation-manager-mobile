package net.kiwox.manager.dst.service.interfaces;

import java.util.List;

import net.kiwox.manager.dst.domain.ControllerProbe;

public interface IControllerProbeService {
	
	ControllerProbe getById(long id);
	
	List<ControllerProbe> getByControllerName(String controllerName);

	List<ControllerProbe> findAllControllersProbe();
}
