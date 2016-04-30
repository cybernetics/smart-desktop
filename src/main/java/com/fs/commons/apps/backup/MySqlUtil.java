/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      30/06/2008     Jamil Shreet    -Add the following class : 
 */
package com.fs.commons.apps.backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @1.1
 * @author ASUS
 * 
 */
public class MySqlUtil {

	private static final Object EXPORT_UTIL_FILE_NAME = "mysqldump.exe";
	private static final String IMPORT_UTIL_FILE_NAME = "mysql.exe";

	/**
	 * 
	 * @param info
	 * @throws IOException
	 */
	public static void export(DatabaseInfo info) throws IOException {
		StringBuffer buffer = new StringBuffer();
		buffer.append(EXPORT_UTIL_FILE_NAME);
		buffer.append(" --skip-opt ");
		buffer.append("--add-drop-table ");
		buffer.append("--create-options ");
		buffer.append("--hex-blob");
		buffer.append(" -h ");
		buffer.append(info.getDatabaseHost());
		buffer.append(" -p");
		buffer.append(info.getDatabasePort());
		buffer.append(" -u ");
		buffer.append(info.getDatabaseUser());
		buffer.append(" -p");
		buffer.append(info.getDatabasePassword() + " ");
		
//		String[] igonoredTbls={"reg_students","reg_student_eq_courses"};  should read from cofig file
//		for (String tblName: igonoredTbls) {
//			buffer.append(" --ignore-table=");
//			buffer.append(info.getDatabaseName()+"."+tblName);
//		}
		
		buffer.append(" "+info.getDatabaseName());
		buffer.append(" >");
		buffer.append("\"" + info.getFileName() + "\"");
		System.err.println(buffer);
		execute(buffer);
		System.err.println("Done dump sql file : " + info.getFileName());
	}
	
	/**
	 * 
	 * @param info
	 * @throws IOException
	 */
	public static void importDb(DatabaseInfo info) throws IOException {
		StringBuffer buffer = new StringBuffer();
		buffer.append(IMPORT_UTIL_FILE_NAME);
		
		buffer.append(" --user=");
		buffer.append(info.getDatabaseUser());
		buffer.append(" --password=");
		buffer.append(info.getDatabasePassword());
		buffer.append(" --host=");
		buffer.append(info.getDatabaseHost());
		
		buffer.append(" "+info.getDatabaseName());
		buffer.append("<");
		buffer.append("\"" + info.getFileName() + "\"");
		System.err.println(buffer);
		execute(buffer);
		System.err.println("Done Import sql file : " + info.getFileName());
	}

	/**
	 * 
	 * @param buffer
	 * @throws IOException
	 */
	private static void execute(StringBuffer buffer) throws IOException {
		String command = buffer.toString();
		String cmd[] = { "cmd", "/c", command };
		Process process=null;
		try {
		
		process = Runtime.getRuntime().exec(cmd);
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
			while ((line = reader.readLine()) != null) {
				System.err.println(line);
			}
		if (process.exitValue() != 0) {
			BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String errorValue = "";
			while (errorValue != null) {
				errorValue = buf.readLine();
				System.err.println(errorValue);
			}
			buf.close();
			System.err.println("MySql Export failed with the following error code : " + process.exitValue());
			throw new IOException(errorValue);
		}
		} finally {
			if(process!=null ){
				process.destroy();
			}			
		}
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws CompressionException
	 */
	public static void main(String[] args) throws IOException, CompressionException {
		DatabaseInfo info = new DatabaseInfo();
		info.setDatabaseHost("server");
		info.setDatabaseName("test-base-jo");
		info.setDatabasePassword("jk-dev");
		info.setDatabasePort(3306);
		info.setDatabaseUser("root");
		String fileName = "c:\\app1-base-jo.sql";
		info.setFileName(fileName);
		export(info);
		System.out.println("Export Done");
//		CompressionUtil.compress(fileName, "c:/test-z.rar");
		System.out.println("Compression done");

	}
}
