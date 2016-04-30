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
package com.fs.commons.apps.feedback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.mail.EmailException;

import com.fs.commons.apps.backup.CompressionException;
import com.fs.commons.apps.backup.DatabaseInfo;
import com.fs.commons.apps.backup.MySqlUtil;
import com.fs.commons.mail.Attachment;
import com.fs.commons.mail.MailInfo;
import com.fs.commons.mail.MailSender;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.FormatUtil;

/**
 * @1.1
 * 
 * @author ASUS
 *
 */
public class FeedbackManager {
	private static final String SUPPORT_EMAIL_TITLE = "Bug Report";
	private static final String SUPPORT_TO = System.getProperty("mail-to", "support@final-solutions.net");
	private static final String SUPPORT_HOST = System.getProperty("mail-host", "final-solutions.net");

	/**
	 *
	 * @param msg
	 * @return
	 */
	protected String buildMessage(final Message msg) {
		final String message = msg.getPanelName() + "\n" + msg.getErrorDesc() + "\n" + msg.getErrorScenario() + "\n";
		return message;
	}

	/**
	 *
	 * @param info
	 * @param includeDatabaseBackup
	 * @return
	 * @throws IOException
	 */
	private String compressFiles(final DatabaseInfo info, final boolean includeDatabaseBackup) throws IOException {
		final String logFileName = ExceptionUtil.getLogFileName();
		final String dbCompressedFileName = getFileName("feeback", "rar");
		String[] filestoCompress;
		if (includeDatabaseBackup) {
			final String dbFileName = getFileName("DB", "sql");
			info.setFileName(dbFileName);
			MySqlUtil.export(info);
			filestoCompress = new String[] { dbFileName, logFileName };
		} else {
			filestoCompress = new String[] { logFileName };
		}

		// CompressionUtil.compressFiles(filestoCompress, dbCompressedFileName);
		return dbCompressedFileName;
	}

	/**
	 *
	 * @param dbCompressedFileName
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private Attachment createAttachement(final String dbCompressedFileName) throws IOException, FileNotFoundException {
		final Attachment attachement = new Attachment();
		attachement.setData(new FileInputStream(dbCompressedFileName));
		attachement.setMimeType("application/rar");
		attachement.setDescription("Bug report");
		attachement.setName("FeedBack.rar");
		return attachement;
	}

	/**
	 *
	 * @param from
	 * @return
	 */
	private MailInfo createMailInfo(final String from, final Message msg) {
		final MailInfo mailInfo = new MailInfo();
		mailInfo.setHost(SUPPORT_HOST);
		mailInfo.setTo(SUPPORT_TO);
		mailInfo.setFrom(from);
		mailInfo.setSubject(SUPPORT_EMAIL_TITLE);
		final String msgs = buildMessage(msg);
		mailInfo.setMsg(msgs);
		return mailInfo;
	}

	/**
	 * @return
	 */
	private String getFileName(final String pre, final String extension) {
		final String fileName = pre + "-" + FormatUtil.formatShortDate() + "." + extension;
		return fileName;
	}

	/**
	 * 1.2
	 * 
	 * @param msgs
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws CompressionException
	 * @throws EmailException
	 */
	public void sendFeddback(final String from, final DatabaseInfo info, final Message msg, final boolean includeDatabaseBackup)
			throws FileNotFoundException, IOException, CompressionException, EmailException {
		final String dbCompressedFileName = compressFiles(info, includeDatabaseBackup);
		final MailInfo mailInfo = createMailInfo(from, msg);
		final Attachment attachement = createAttachement(dbCompressedFileName);
		mailInfo.addAttachment(attachement);

		MailSender.send(mailInfo);
		System.err.println("Feed back sent succ...");
	}

	// /**
	// *
	// */
	// private void fillMap() {
	// map.put("mail-host", "final-solutions.net") ;
	// map.put("mail-port", "25") ;
	// map.put("mail-from", "SMART-COLLEGE") ;
	// map.put("mail-to", "support@final-solutions.net") ;
	// map.put("service-host", "localhost") ;
	// map.put("service-port", "8182") ;
	// }

	/**
	 *
	 * @param args
	 */
	// public static void main(String[] args) {
	// FeedbackManager manager = new FeedbackManager();
	// try {
	// Message msg = new Message();
	// msg.setWrongPanle("Test wrong Panel");
	// msg.setWrongDescription("Test wrong description");
	// msg.setWrongSample("Test wrong sample");
	// manager.sendFeddback(msg);
	//
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (CompressionException e) {
	// e.printStackTrace();
	// } catch (EmailException e) {
	// e.printStackTrace();
	// }
	// }
}
