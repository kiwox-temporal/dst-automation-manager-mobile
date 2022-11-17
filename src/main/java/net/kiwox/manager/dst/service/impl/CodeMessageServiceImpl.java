package net.kiwox.manager.dst.service.impl;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import net.kiwox.manager.dst.service.interfaces.ICodeMessageService;

@Service
public class CodeMessageServiceImpl implements ICodeMessageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CodeMessageServiceImpl.class);
	
	@Autowired
	private MessageSource messageSource;

	@Override
	public void logInfo(String code, Object... args) {
		StringBuilder message = new StringBuilder();
		String m = messageSource.getMessage(code, null, null, Locale.ENGLISH);
		if (m == null) {
			message.append("[DST-UNKNOWN] Unknown error");
			if (args.length > 0) {
				message.append(":");
				for (int i = 0; i < args.length; ++i) {
					message.append(" {}");
				}
			}
		} else {
			message.append("[");
			message.append(code);
			message.append("] ");
			message.append(m);
		}
		
		m = message.toString();
		LOGGER.info(m, args);
	}

	@Override
	public String getMessage(String code, Object... args) {
		return messageSource.getMessage(code, args, null, Locale.ENGLISH);
	}

}
