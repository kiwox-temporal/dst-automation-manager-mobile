package net.kiwox.manager.dst.service.impl;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.kiwox.manager.dst.dao.interfaces.IParameterDao;
import net.kiwox.manager.dst.enums.Parameter;
import net.kiwox.manager.dst.enums.ParameterDataType;
import net.kiwox.manager.dst.service.interfaces.IParameterService;

@Service
public class ParameterServiceImpl implements IParameterService {
	
	@Autowired
	private IParameterDao parameterDao;

	@Override
	@Transactional(readOnly = true)
	public String getString(Parameter parameter) {
		return getValue(parameter, ParameterDataType.STRING);
	}

	@Override
	@Transactional(readOnly = true)
	public Integer getInt(Parameter parameter) {
		String value = getValue(parameter, ParameterDataType.INTEGER);
		if (value == null) {
			return null;
		}
		return Integer.parseInt(value);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getLong(Parameter parameter) {
		String value = getValue(parameter, ParameterDataType.LONG);
		if (value == null) {
			return null;
		}
		return Long.parseLong(value);
	}
	
	private String getValue(Parameter parameter, ParameterDataType type) {
		net.kiwox.manager.dst.domain.Parameter p = parameterDao.findById(parameter.toString()).orElse(null);
		if (p != null && p.getDataType() == type) {
			return p.getValue();
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Parameter, String> getMailSenderParameters() {
		Map<Parameter, String> params = new EnumMap<>(Parameter.class);
		for (net.kiwox.manager.dst.domain.Parameter p : parameterDao.getMailSenderParameters()) {
			params.put(Parameter.valueOf(p.getName()), p.getValue());
		}
		return params;
	}

}
