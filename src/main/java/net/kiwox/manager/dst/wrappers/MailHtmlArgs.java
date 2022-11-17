package net.kiwox.manager.dst.wrappers;

public class MailHtmlArgs {

	private String title;
	private String subtitle;
	private String salute;
	private String message;
	private String requestId;
	private String preSignature;
	private String signature;
	private String footer;
	private boolean smallTextCoding;
	
	public MailHtmlArgs() {
		this.smallTextCoding = false;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	public String getSalute() {
		return salute;
	}
	public void setSalute(String salute) {
		this.salute = salute;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getPreSignature() {
		return preSignature;
	}
	public void setPreSignature(String preSignature) {
		this.preSignature = preSignature;
	}
	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}

	public boolean isSmallTextCoding() {
		return smallTextCoding;
	}
	public void setSmallTextCoding(boolean smallTextCoding) {
		this.smallTextCoding = smallTextCoding;
	}
	
}
