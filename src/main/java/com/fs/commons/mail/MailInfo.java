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
	ArrayList<Attachment> attachements = new ArrayList<Attachment>();

	/**
	 *
	 * @param attachment
	 */
	public void addAttachment(final Attachment attachment) {
		this.attachements.add(attachment);
	}

	/**
	 *
	 * @param email
	 * @throws EmailException
	 * @throws IOException
	 */
	public void fillEmail(final MultiPartEmail email) throws EmailException, IOException {
		email.setHostName(getHost());
		email.addTo(getTo());
		email.setFrom(getFrom());
		email.setSubject(getSubject());
		email.setMsg(getMsg());
		for (int i = 0; i < this.attachements.size(); i++) {
			final Attachment attachment = this.attachements.get(i);
			final ByteArrayDataSource ds = new ByteArrayDataSource(attachment.getData(), attachment.getMimeType());
			email.attach(ds, attachment.getName(), attachment.getDescription());
		}
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return this.from;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return this.msg;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return this.to;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(final String from) {
		this.from = from;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(final String msg) {
		this.msg = msg;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final String to) {
		this.to = to;
	}
}
