package com.fs.commons.mail;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class MailInfo {
	String host;
	String from;
	String to;
	String subject;
	String msg;
	ArrayList<Attachment> attachements=new ArrayList<Attachment>();

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * 
	 * @param email
	 * @throws EmailException 
	 * @throws IOException 
	 */
	public void fillEmail(MultiPartEmail email) throws EmailException, IOException{
		email.setHostName(getHost());
		email.addTo(getTo());
		email.setFrom(getFrom());
		email.setSubject(getSubject());
		email.setMsg(getMsg());
		for (int i = 0; i < attachements.size(); i++) {			
			Attachment attachment = attachements.get(i);
			ByteArrayDataSource ds=new ByteArrayDataSource(attachment.getData(),attachment.getMimeType());
			email.attach(ds, attachment.getName(), attachment.getDescription());
		}
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * 
	 * @param attachment
	 */
	public void addAttachment(Attachment attachment){
		attachements.add(attachment);
	}
}
