package net.kiwox.manager.dst.service.interfaces;

import net.kiwox.manager.dst.domain.Workorder;

public interface IWorkorderService {
	
	Workorder getByTestResult(long testResultId);
	
	void save(Workorder workorder);
	
}
