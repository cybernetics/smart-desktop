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
package com.fs.commons.reports;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.ExceptionUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * TODO : refactor this class regarding compilation , since we dont compile
 * reports any more , its compiled in the anr script during the building process
 *
 * @author user
 *
 */
public class JKReportManager {
	private static ArrayList<JKReport> allReports = new ArrayList<JKReport>();

	private static Map<String, String> databaseReports = new HashMap<>();
//	static {
//		try {
////			loadDatabaseReports();
//		} catch (final JKDataAccessException e) {
//			ExceptionUtil.handle(e);
//		}
//	}

	// //////////////////////////////////////////////////////////////
	public static void addReports(final ArrayList<JKReport> reports) {
		allReports.addAll(reports);
	}

	// //////////////////////////////////////////////////////////////
	public static void clearReports() {
		allReports.clear();
	}

	/**
	 *
	 * @param report
	 * @throws JRException
	 * @throws IOException
	 */
	public static void compileReport(final JKReport report, final String outputFolder) throws JRException, IOException {
		final InputStream fileInputStream = GeneralUtility.getReportFileAsStream(report.getSourceFileName());
		final JasperDesign jasperDesign = JRXmlLoader.load(fileInputStream);
		JasperCompileManager.compileReportToFile(jasperDesign, outputFolder + report.getOutFileName());
		fileInputStream.close();
	}

	// // //////////////////////////////////////////////////////////////
	// private ReportManager(InputStream in) throws JKXmlException {
	// this(in, "", "");
	// }

	// //////////////////////////////////////////////////////////////
	public static JKReport getReport(final int index) {
		final JKReport report = allReports.get(index);
		return report;
	}

	// //////////////////////////////////////////////////////////////
	public static JKReport getReport(final String name) {
		for (int i = 0; i < allReports.size(); i++) {
			if (allReports.get(i).getName().equals(name)) {
				return allReports.get(i);
			}
		}
		return null;
	}

	// //////////////////////////////////////////////////////////////
	public static ArrayList<JKReport> getReports() {
		return allReports;
	}

	// /**
	// *
	// * @param outPath
	// */
	// private void checkOutpath(String outPath) {
	// File file = new File(outPath);
	// if (!file.exists()) {
	// file.mkdir();
	// }
	// }

	// //////////////////////////////////////////////////////////////
	public static int getReportsCount() {
		return allReports.size();
	}

	// //////////////////////////////////////////////////////////////
	public static void init(final InputStream in) throws JKXmlException {
		init(in, "");
	}

	// //////////////////////////////////////////////////////////////
	public static void init(final InputStream in, final String prefix) throws JKXmlException {
		final JKReportManager manager = new JKReportManager(in, prefix, "");
		addReports(manager.getInstanceReports());
	}

//	private static void loadDatabaseReports() throws JKRecordNotFoundException, JKDataAccessException {
//		final DynamicDao dao = DaoFactory.createDynamicDao("conf_reports");
//		final ArrayList<Record> records = dao.lstRecords();
//		for (final Record record : records) {
//			databaseReports.put(record.getFieldValueAsString("report_name"), record.getFieldValueAsString("report_file"));
//		}
//	}

	private ArrayList<JKReport> instanceReports = new ArrayList<JKReport>();

	// this variable will be useful in localized systems , where it could be
	// set according
	// to the system current locale , thus it would load all reports that
	// Begins with the prefix of that locale
	private final String prefix;

	private final String altPrefix;

	// ///////////////////////////////////////////////////////////////
	private JKReportManager(final ArrayList<JKReport> reports, final String prefix, final String altPrefix) {
		this.instanceReports = reports;
		this.prefix = prefix;
		this.altPrefix = altPrefix;
		compileReports();
	}

	// //////////////////////////////////////////////////////////////
	public JKReportManager(final InputStream in, final String prefix, final String altPrefix) throws JKXmlException {
		this(new ReportXmlParser().parse(in), prefix, altPrefix);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	private boolean allowCompile(final JKReport report) throws Exception {
		return false;
		// URL resource = GeneralUtility.getURL(report.getAbsolutSourcePath());
		// File sourceFile = new File(sourceFileName);
		// File outFile = new File(report.getAbsolutOutPath());
		// // if (GeneralUtility.isDebugMode()) {
		// // return true;
		// // }
		// if (!outFile.exists()) {
		// return true;
		// }
		// //
		// // System.out.println(sourceFile.lastModified() + "==" +
		// // outFile.lastModified());
		// // long lastModified = report.getSourceFileName().lastModified();
		// // Possibly compressed in jar file
		// // if (lastModified <= 0) {
		// // lastModified =
		// // GeneralUtility.getLastModified(report.getAbsolutSourcePath());
		// // }
		//
		// // if (outFile.lastModified() < lastModified) {
		// // return true;
		// // }
		// return false;
		// return true;
	}

	// //////////////////////////////////////////////////////////////
	private String checkReportFileName(final JKReport report) throws FileNotFoundException {
		InputStream inputStream = null;
		try {
			String name = this.prefix + report.getName() + ".jrxml";
			inputStream = GeneralUtility.getReportFileAsStream(name);
			if (inputStream != null) {
				return name;
			}
			name = this.altPrefix + report.getName() + ".jrxml";
			inputStream = GeneralUtility.getReportFileAsStream(name);
			if (inputStream != null) {
				return name;
			}
			name = report.getName() + ".jrxml";
			inputStream = GeneralUtility.getReportFileAsStream(name);
			if (inputStream != null) {
				return name;
			}
			throw new FileNotFoundException(report.getName());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	// //////////////////////////////////////////////////////////////
	private void compileReports() {
		final Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// System.out.println("Compiling Reports...");
				for (int i = 0; i < JKReportManager.this.instanceReports.size(); i++) {
					final JKReport report = JKReportManager.this.instanceReports.get(i);
					// if (i == 0) {
					// checkSourcePath(report.getSourcePath());
					// checkOutpath(report.getOutPath());
					// }
					initReport(report);
				}
				// System.out.println("Compiling Reports Done...");
			}
		};
		runnable.run();
		// Thread t = new Thread(runnable);
		// t.start();
	}

	// //////////////////////////////////////////////////////////////
	public ArrayList<JKReport> getInstanceReports() {
		return this.instanceReports;
	}

	private String getOutFileName(final JKReport report) throws JKRecordNotFoundException, JKDataAccessException {
		// TODO : refactor me and tune it to read all the reports only once
		String name = report.getName();
		if (databaseReports.get(report.getName()) != null) {
			name = databaseReports.get(report.getName());
		}
		return this.prefix + name + ".jasper";
	}

	// //////////////////////////////////////////////////////////////
	private void initReport(final JKReport report) {
		try {
			final String sourceFileName = checkReportFileName(report);
			report.setSourceFileName(sourceFileName);
			report.setOutFileName(getOutFileName(report));
			// report.setAbsolutOutPath(GeneralUtility.geto + "/" +
			// report.getOutFileName());
			// convert the condition to not
			if (allowCompile(report)) {
				// only compile reports if not exists
				// System.out.println("Compiling Report :
				// "+prefix+report.getName());
				compileReport(report, "");
				// JasperReport jr
				// =JasperCompileManager.compileReport(report.getAbsolutSourcePath());
			}
		} catch (final JRException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to compile report: " + report.getSourceFileName(), e);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
