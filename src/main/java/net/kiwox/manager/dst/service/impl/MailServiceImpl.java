package net.kiwox.manager.dst.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.kiwox.manager.dst.enums.Parameter;
import net.kiwox.manager.dst.service.interfaces.ICodeMessageService;
import net.kiwox.manager.dst.service.interfaces.IMailService;
import net.kiwox.manager.dst.service.interfaces.IParameterService;
import net.kiwox.manager.dst.wrappers.MailHtmlArgs;
import net.kiwox.manager.dst.wrappers.MailSenderArgs;

@Service
public class MailServiceImpl implements IMailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
	private static final Lock LOCK = new ReentrantLock();
	
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	@Autowired
	private IParameterService parameterService;
	
	@Autowired
	private ICodeMessageService messageService;
	
	@Resource(name = "freemarkerConfig")
	private Configuration freemarkerConfiguration;

	@Override
	public String[] splitAddresses(String x) {
		if (x == null) {
			return new String[0];
		}
		return Arrays.stream(x.split(","))
				.filter(StringUtils::isNotBlank)
				.distinct()
				.map(String::trim)
				.toArray(String[]::new);
	}

	@Override
	public String[] splitAddresses(List<String> x) {
		if (x == null) {
			return new String[0];
		}
		return x.stream()
				.filter(StringUtils::isNotBlank)
				.distinct()
				.map(String::trim)
				.toArray(String[]::new);
	}
	
	@Override
	public String getMailHtml(MailHtmlArgs args) {
		String title = args.getTitle();
		String subtitle = args.getSubtitle();
		String salute = args.getSalute();
		String requestId = args.getRequestId();
		String message = args.getMessage();
		String preSignature = args.getPreSignature();
		String signature = args.getSignature();
		String footer = args.getFooter();
		
		if (args.isSmallTextCoding()) {
			title = messageService.getMessage(title);
			subtitle = messageService.getMessage(subtitle);
			salute = messageService.getMessage(salute);
			preSignature = messageService.getMessage(preSignature);
			signature = messageService.getMessage(signature);
			footer = messageService.getMessage(footer);
		}
		
		if (salute == null) {
			salute = messageService.getMessage("mail.salute");
		}
		if (requestId == null) {
			requestId = messageService.getMessage("mail.requestid");
		}
		if (preSignature == null) {
			preSignature = messageService.getMessage("mail.preSignature");
		}
		if (signature == null) {
			signature = messageService.getMessage("mail.signature");
		}
		if (footer == null) {
			footer = messageService.getMessage("mail.footer");
		}

		salute =  salute.replaceAll("LBXXX","\n");
		signature = signature.replaceAll("LBXXX","\n");
		Map<String, Object> model = new HashMap<>();
		model.put("title", title);
		model.put("subtitle", subtitle);
		model.put("salute", salute);
		model.put("requestId", requestId);
		model.put("message", message);
		model.put("preSignature", preSignature);
		model.put("signature", signature);
		model.put("footer", footer);
		
		try (StringWriter out = new StringWriter()) {
			Template template = freemarkerConfiguration.getTemplate("mail_smrpa.ftl", Locale.forLanguageTag("es"));
			//Template template = freemarkerConfiguration.getTemplate("mail_smrpa.ftl", Locale.forLanguageTag("es"));
			template.process(model, out);
			return out.toString();
		} catch (TemplateException | IOException e) {
			LOGGER.error("Error rendering mail html", e);
		}
		return null;
	}
	
	@Override
	public boolean send(MailSenderArgs args) {
		if (args.getTo().length == 0) {
			LOGGER.info("[DST-MAIL-001] Undefined mail addresses to send. No mail was sent.");
			return false;
		}
		
		try {
			LOCK.lockInterruptibly();
		} catch (InterruptedException e) {
			LOGGER.info("[DST-MAIL-002] Couldn't get lock to send mail.", e);
			Thread.currentThread().interrupt();
			return false;
		}
		
		String from = null;
		try {
			Map<Parameter, String> mailSenderParams = parameterService.getMailSenderParameters();
			for (Entry<Parameter, String> entry : mailSenderParams.entrySet()) {
				switch (entry.getKey()) {
				case MAIL_SENDER_HOST: mailSender.setHost(entry.getValue()); break;
				case MAIL_SENDER_PORT: setPort(entry.getValue()); break;
				case MAIL_SENDER_USERNAME: mailSender.setUsername(entry.getValue()); break;
				case MAIL_SENDER_PASSWORD: mailSender.setPassword(entry.getValue()); break;
				case MAIL_SENDER_FROM: from = entry.getValue(); break;
				default: break;
				}
			}
			/*args.setSubject("test test");
			args.setText("TEST VALUE");*/

			Preparator prep = new Preparator(args, from);
			mailSender.send(prep);
			
			StringBuilder log = new StringBuilder();
			log.append("Mail [");
			log.append(args.getSubject());
			log.append("] successfully sent\n\tTo: ");
			log.append(Arrays.toString(args.getTo()));
			log.append("\n\tCC: ");
			log.append(Arrays.toString(args.getCc()));
			log.append("\n\tBCC: ");
			log.append(Arrays.toString(args.getBcc()));
			log.append("\n\tFrom: [");
			log.append(from);
			log.append('/');
			log.append(mailSender.getHost());
			log.append(':');
			log.append(mailSender.getPort());
			log.append("]");
			LOGGER.info("{}", log);
		} catch (Exception e) {
			StringBuilder err = new StringBuilder();
			err.append("[DST-MAIL-003] Unable to send mail [");
			err.append(args.getSubject());
			err.append("] to ");
			err.append(Arrays.toString(args.getTo()));
			err.append(" from [");
			err.append(from);
			err.append('/');
			err.append(mailSender.getHost());
			err.append(':');
			err.append(mailSender.getPort());
			err.append(']');
			LOGGER.error(err.toString(), e);
			return false;
		} finally {
			LOCK.unlock();
		}
		return true;
	}
	
	private void setPort(String port) {
		try {
			mailSender.setPort(Integer.parseInt(port));
		} catch (NumberFormatException e) {
			String err = "[DST-PARSE-001] Unable to parse int from parameter [" + Parameter.MAIL_SENDER_PORT + "] with value [" + port + "]";
			LOGGER.error(err, e);
		}
	}
	
	private class Preparator implements MimeMessagePreparator {

		private MailSenderArgs args;
		private String from;
		
		public Preparator(MailSenderArgs args, String from) {
			this.args = args;
			this.from = from;
		}

		@Override
		public void prepare(MimeMessage mimeMessage) throws Exception {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.toString());
			message.setTo(args.getTo());

			if (StringUtils.isNotBlank(from)) {
				message.setFrom(from);
			}

			if (args.getCc().length > 0) {
				message.setCc(args.getCc());
			}

			if (args.getBcc().length > 0) {
				message.setBcc(args.getBcc());
			}

			message.setSubject(args.getSubject());
			message.setText(args.getText(), args.isHtml());
			for (Entry<String, DataSource> entry : args.getAttachments().entrySet()) {
				message.addAttachment(entry.getKey(), entry.getValue());
			}
		}
		
	}

}
