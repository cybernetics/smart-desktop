/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

	public static void main(final String[] args) throws EmailException, IOException {
		final MailInfo info = new MailInfo();
		final Attachment attachment = new Attachment();
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
	public static void send(final MailInfo mailInfo) throws EmailException, IOException {
		final MultiPartEmail email = new MultiPartEmail();
		mailInfo.fillEmail(email);
		email.send();
	}
}
