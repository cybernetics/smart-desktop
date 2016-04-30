/**
 * 
 */
package com.fs.commons.mail;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

/**
 * @author ASUS
 * 
 */
public class MailSender {

	/**
	 * 
	 * @param hostName
	 * @param from
	 * @param to
	 * @param subject
	 * @param msgBody
	 * @param in
	 * @throws EmailException
	 * @throws IOException 
	 * @throws IOException 
	 */
	public static void send(MailInfo mailInfo) throws EmailException, IOException{
		MultiPartEmail email=new MultiPartEmail();
		mailInfo.fillEmail(email);
		email.send();
	}
	
	public static void main(String[] args) throws EmailException, IOException {
		MailInfo info=new MailInfo();
		Attachment attachment = new Attachment();
		attachment.setData(new FileInputStream("c:/app1-base-jo.sql"));
		attachment.setMimeType("text/html");
		attachment.setDescription("Test Attachment");
		attachment.setName("app1-base-jo.sql");
		info.setHost("mail.hotpop.com");
		info.setTo("jamilshreet@yahoo.com");
		info.setFrom("jamil.shreet@gmail.com");
		info.setSubject("Test for Commons");
		info.setMsg("Hi Jamil how are you this is a test for attachment file");
		info.addAttachment(attachment);
		send(info);
		System.out.println("Done");
		
	}
}
