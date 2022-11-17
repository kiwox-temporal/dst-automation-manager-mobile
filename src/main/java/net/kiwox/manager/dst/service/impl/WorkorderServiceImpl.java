package net.kiwox.manager.dst.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.kiwox.manager.dst.dao.interfaces.IWorkorderDao;
import net.kiwox.manager.dst.domain.Workorder;
import net.kiwox.manager.dst.service.interfaces.IWorkorderService;

@Service
public class WorkorderServiceImpl implements IWorkorderService {
	
	@Autowired
	private IWorkorderDao workorderDao;

	@Override
	@Transactional(readOnly = true)
	public Workorder getByTestResult(long testResultId) {
		return workorderDao.getByTestResult(testResultId);
	}

	@Override
	@Transactional
	public void save(Workorder workorder) {
		workorderDao.saveAndFlush(workorder);
	}

}
