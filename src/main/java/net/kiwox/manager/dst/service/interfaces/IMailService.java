package net.kiwox.manager.dst.service.interfaces;

import java.util.List;

import net.kiwox.manager.dst.wrappers.MailHtmlArgs;
import net.kiwox.manager.dst.wrappers.MailSenderArgs;

public interface IMailService {

	String[] splitAddresses(String x);
	
	String[] splitAddresses(List<String> x);
	
	String getMailHtml(MailHtmlArgs args);
	
	boolean send(MailSenderArgs args);
	
}
