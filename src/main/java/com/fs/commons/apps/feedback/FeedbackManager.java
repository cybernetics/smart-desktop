/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      03/07/2008     Jamil Shreet    -Add the following class : 
 * 1.2      21/08/2008     ahmad ali       - modefy sendFeddback method
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
 * @author ASUS
 * 
 */
public class FeedbackManager {
	private static final String SUPPORT_EMAIL_TITLE = "Bug Report";
	private static final String SUPPORT_TO = System.getProperty("mail-to","support@final-solutions.net");
	private static final String SUPPORT_HOST = System.getProperty("mail-host","final-solutions.net");

	/**
	 * 
	 * @param msg
	 * @return
	 */
	protected String buildMessage(Message msg) {
		String message = msg.getPanelName() + "\n" + msg.getErrorDesc()
				+ "\n" + msg.getErrorScenario() + "\n";
		return message;
	}

	/**
	 * 1.2
	 * @param msgs
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws CompressionException
	 * @throws EmailException 
	 */
	public void sendFeddback(String from,DatabaseInfo info, Message msg,boolean includeDatabaseBackup) throws FileNotFoundException,IOException, CompressionException, EmailException {		
		String dbCompressedFileName = compressFiles(info, includeDatabaseBackup);
		MailInfo mailInfo = createMailInfo(from,msg);		
		Attachment attachement = createAttachement(dbCompressedFileName);		
		mailInfo.addAttachment(attachement);
								
		MailSender.send( mailInfo);		
		System.err.println("Feed back sent succ...");
	}

	/**
	 * 
	 * @param from
	 * @return
	 */
	private MailInfo createMailInfo(String from, Message msg) {
		MailInfo mailInfo = new MailInfo();
		mailInfo.setHost(SUPPORT_HOST);
		mailInfo.setTo(SUPPORT_TO);
		mailInfo.setFrom(from);
		mailInfo.setSubject(SUPPORT_EMAIL_TITLE);
		String msgs = buildMessage(msg);
		mailInfo.setMsg(msgs);
		return mailInfo;
	}

	/**
	 * 
	 * @param dbCompressedFileName
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private Attachment createAttachement(String dbCompressedFileName)
			throws IOException, FileNotFoundException {
		Attachment attachement = new Attachment();
		attachement.setData(new FileInputStream(dbCompressedFileName));
		attachement.setMimeType("application/rar");
		attachement.setDescription("Bug report");
		attachement.setName("FeedBack.rar");
		return attachement;
	}

	/**
	 * 
	 * @param info
	 * @param includeDatabaseBackup
	 * @return
	 * @throws IOException
	 */
	private String compressFiles(DatabaseInfo info,
			boolean includeDatabaseBackup) throws IOException {
		String logFileName = ExceptionUtil.getLogFileName();
		String dbCompressedFileName = getFileName("feeback", "rar");
		String[] filestoCompress;
		if(includeDatabaseBackup){
			String dbFileName = getFileName("DB", "sql");													
			info.setFileName(dbFileName);
			MySqlUtil.export(info);		
			filestoCompress=new String[]{dbFileName,logFileName};
		}else{
			filestoCompress= new String[]{logFileName};
		}
		
//		CompressionUtil.compressFiles(filestoCompress, dbCompressedFileName);
		return dbCompressedFileName;
	}
	
	/**
	 * @return
	 */
	private String getFileName(String pre,String extension) {
		String fileName = pre+"-"+FormatUtil.formatShortDate()+"."+extension;
		return fileName;
	}

//	/**
//	 * 
//	 */
//	private void fillMap() {
//	map.put("mail-host", "final-solutions.net")	;
//	map.put("mail-port", "25")	;
//	map.put("mail-from", "SMART-COLLEGE")	;
//	map.put("mail-to", "support@final-solutions.net")	;
//	map.put("service-host", "localhost")	;
//	map.put("service-port", "8182")	;
//	}
	
	/**
	 * 
	 * @param args
	 */
//	public static void main(String[] args) {
//		FeedbackManager manager = new FeedbackManager();
//		try {
//			Message msg = new Message();
//			msg.setWrongPanle("Test wrong Panel");
//			msg.setWrongDescription("Test wrong description");
//			msg.setWrongSample("Test wrong sample");
//			manager.sendFeddback(msg);
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (CompressionException e) {
//			e.printStackTrace();
//		} catch (EmailException e) {
//			e.printStackTrace();
//		}
//	}
}
