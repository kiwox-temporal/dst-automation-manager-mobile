package net.kiwox.manager.dst.service.interfaces;

import java.util.Map;

import net.kiwox.manager.dst.enums.Parameter;

public interface IParameterService {
	
	String getString(Parameter parameter);
	
	Integer getInt(Parameter parameter);
	
	Long getLong(Parameter parameter);
	
	Map<Parameter, String> getMailSenderParameters();
	
}
