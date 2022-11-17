package net.kiwox.manager.dst.wrappers;

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataSource;

public class MailSenderArgs {

	private String[] to;
	private String[] cc;
	private String[] bcc;
	private String subject;
	private String text;
	private boolean html;
	private Map<String, DataSource> attachments;
	
	public MailSenderArgs() {
		this.to = new String[0];
		this.cc = new String[0];
		this.bcc = new String[0];
		this.html = true;
		this.attachments = new HashMap<>();
	}

	public String[] getTo() {
		return to;
	}
	public void setTo(String[] to) {
		this.to = to;
	}

	public String[] getCc() {
		return cc;
	}
	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}
	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public boolean isHtml() {
		return html;
	}
	public void setHtml(boolean html) {
		this.html = html;
	}

	public Map<String, DataSource> getAttachments() {
		return attachments;
	}
	
}
