package net.kiwox.manager.dst.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.kiwox.manager.dst.dao.interfaces.IControllerProbeDao;
import net.kiwox.manager.dst.domain.ControllerProbe;
import net.kiwox.manager.dst.service.interfaces.IControllerProbeService;

@Service
public class ControllerProbeServiceImpl implements IControllerProbeService {
	
	@Autowired
	private IControllerProbeDao controllerProbeDao;

	@Override
	@Transactional(readOnly = true)
	public ControllerProbe getById(long id) {
		return controllerProbeDao.getById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ControllerProbe> getByControllerName(String controllerName) {
		return controllerProbeDao.getByControllerName(controllerName);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ControllerProbe> findAllControllersProbe() {
		return controllerProbeDao.findAll();
	}

}
