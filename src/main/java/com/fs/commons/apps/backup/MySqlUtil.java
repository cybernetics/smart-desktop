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
package com.fs.commons.apps.backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @1.1
 * 
 * @author ASUS
 *
 */
public class MySqlUtil {

	private static final Object EXPORT_UTIL_FILE_NAME = "mysqldump.exe";
	private static final String IMPORT_UTIL_FILE_NAME = "mysql.exe";

	/**
	 *
	 * @param buffer
	 * @throws IOException
	 */
	private static void execute(final StringBuffer buffer) throws IOException {
		final String command = buffer.toString();
		final String cmd[] = { "cmd", "/c", command };
		Process process = null;
		try {

			process = Runtime.getRuntime().exec(cmd);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.err.println(line);
			}
			if (process.exitValue() != 0) {
				final BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
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
			if (process != null) {
				process.destroy();
			}
		}
	}

	/**
	 *
	 * @param info
	 * @throws IOException
	 */
	public static void export(final DatabaseInfo info) throws IOException {
		final StringBuffer buffer = new StringBuffer();
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

		// String[] igonoredTbls={"reg_students","reg_student_eq_courses"};
		// should read from cofig file
		// for (String tblName: igonoredTbls) {
		// buffer.append(" --ignore-table=");
		// buffer.append(info.getDatabaseName()+"."+tblName);
		// }

		buffer.append(" " + info.getDatabaseName());
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
	public static void importDb(final DatabaseInfo info) throws IOException {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(IMPORT_UTIL_FILE_NAME);

		buffer.append(" --user=");
		buffer.append(info.getDatabaseUser());
		buffer.append(" --password=");
		buffer.append(info.getDatabasePassword());
		buffer.append(" --host=");
		buffer.append(info.getDatabaseHost());

		buffer.append(" " + info.getDatabaseName());
		buffer.append("<");
		buffer.append("\"" + info.getFileName() + "\"");
		System.err.println(buffer);
		execute(buffer);
		System.err.println("Done Import sql file : " + info.getFileName());
	}

	/**
	 *
	 * @param args
	 * @throws IOException
	 * @throws CompressionException
	 */
	public static void main(final String[] args) throws IOException, CompressionException {
		final DatabaseInfo info = new DatabaseInfo();
		info.setDatabaseHost("server");
		info.setDatabaseName("test-base-jo");
		info.setDatabasePassword("jk-dev");
		info.setDatabasePort(3306);
		info.setDatabaseUser("root");
		final String fileName = "c:\\app1-base-jo.sql";
		info.setFileName(fileName);
		export(info);
		System.out.println("Export Done");
		// CompressionUtil.compress(fileName, "c:/test-z.rar");
		System.out.println("Compression done");

	}
}
