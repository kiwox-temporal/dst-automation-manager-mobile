package net.kiwox.manager.dst.service.interfaces;

public interface ICodeMessageService {
	
	void logInfo(String code, Object... args);
	
	String getMessage(String code, Object... args);
	
}
