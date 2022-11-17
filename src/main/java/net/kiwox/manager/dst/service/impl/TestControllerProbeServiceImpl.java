package net.kiwox.manager.dst.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.kiwox.manager.dst.dao.interfaces.ITestControllerProbeDao;
import net.kiwox.manager.dst.domain.TestControllerProbe;
import net.kiwox.manager.dst.service.interfaces.ITestControllerProbeService;

@Service
public class TestControllerProbeServiceImpl implements ITestControllerProbeService {

    @Autowired
    private ITestControllerProbeDao controllerProbeDao;

    @Override
    @Transactional(readOnly = true)
    public List<TestControllerProbe> findAllTestControllerProbe() {
        return controllerProbeDao.findAll();
    }

	@Override
    @Transactional(readOnly = true)
	public List<TestControllerProbe> findByControllerName(String controllerName) {
		return controllerProbeDao.findByControllerName(controllerName);
	}
	
}
