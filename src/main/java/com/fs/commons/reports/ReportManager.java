package com.fs.commons.reports;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

/**
 * TODO : refactor this class regarding compilation , since we dont compile
 * reports any more , its compiled in the anr script during the building process
 * 
 * @author user
 * 
 */
public class ReportManager {
	private static ArrayList<Report> allReports = new ArrayList<Report>();

	private ArrayList<Report> instanceReports = new ArrayList<Report>();
	private static Map<String, String> databaseReports = new HashMap<>();
	// this variable will be useful in localized systems , where it could be
	// set according
	// to the system current locale , thus it would load all reports that
	// Begins with the prefix of that locale
	private String prefix;
	private String altPrefix;
	static {
		try {
			loadDatabaseReports();
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// // //////////////////////////////////////////////////////////////
	// private ReportManager(InputStream in) throws JKXmlException {
	// this(in, "", "");
	// }

	// //////////////////////////////////////////////////////////////
	public ReportManager(InputStream in, String prefix, String altPrefix) throws JKXmlException {
		this(new ReportXmlParser().parse(in), prefix, altPrefix);
	}

	// ///////////////////////////////////////////////////////////////
	private ReportManager(ArrayList<Report> reports, String prefix, String altPrefix) {
		instanceReports = reports;
		this.prefix = prefix;
		this.altPrefix = altPrefix;
		compileReports();
	}

	// //////////////////////////////////////////////////////////////
	public ArrayList<Report> getInstanceReports() {
		return instanceReports;
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
	private void compileReports() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// System.out.println("Compiling Reports...");
				for (int i = 0; i < instanceReports.size(); i++) {
					Report report = instanceReports.get(i);
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
	private void initReport(Report report) {
		try {
			String sourceFileName = checkReportFileName(report);
			report.setSourceFileName(sourceFileName);
			report.setOutFileName(getOutFileName(report));
			// report.setAbsolutOutPath(GeneralUtility.geto + "/" +
			// report.getOutFileName());
			// convert the condition to not
			if (allowCompile(report)) {
				// only compile reports if not exists
				// System.out.println("Compiling Report : "+prefix+report.getName());
				compileReport(report, "");
				// JasperReport jr
				// =JasperCompileManager.compileReport(report.getAbsolutSourcePath());
			}
		} catch (JRException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to compile report: " + report.getSourceFileName(), e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getOutFileName(Report report) throws RecordNotFoundException, DaoException {
		// TODO : refactor me and tune it to read all the reports only once
		String name = report.getName();
		if (databaseReports.get(report.getName()) != null) {
			name = databaseReports.get(report.getName());
		}
		return prefix + name + ".jasper";
	}

	/**
	 * 
	 * @param report
	 * @throws JRException
	 * @throws IOException
	 */
	public static void compileReport(Report report, String outputFolder) throws JRException, IOException {
		InputStream fileInputStream = GeneralUtility.getReportFileAsStream(report.getSourceFileName());
		JasperDesign jasperDesign = JRXmlLoader.load(fileInputStream);
		JasperCompileManager.compileReportToFile(jasperDesign, outputFolder + report.getOutFileName());
		fileInputStream.close();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	private boolean allowCompile(Report report) throws Exception {
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
	private String checkReportFileName(Report report) throws FileNotFoundException {
		InputStream inputStream = null;
		try {
			String name = prefix + report.getName() + ".jrxml";
			inputStream = GeneralUtility.getReportFileAsStream(name);
			if (inputStream != null) {
				return name;
			}
			name = altPrefix + report.getName() + ".jrxml";
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
				} catch (IOException e) {
				}
			}
		}
	}

	// //////////////////////////////////////////////////////////////
	public static Report getReport(int index) {
		Report report = allReports.get(index);
		return report;
	}

	// //////////////////////////////////////////////////////////////
	public static int getReportsCount() {
		return allReports.size();
	}

	// //////////////////////////////////////////////////////////////
	public static Report getReport(String name) {
		for (int i = 0; i < allReports.size(); i++) {
			if (allReports.get(i).getName().equals(name)) {
				return allReports.get(i);
			}
		}
		return null;
	}

	// //////////////////////////////////////////////////////////////
	public static void addReports(ArrayList<Report> reports) {
		allReports.addAll(reports);
	}

	// //////////////////////////////////////////////////////////////
	public static ArrayList<Report> getReports() {
		return allReports;
	}

	// //////////////////////////////////////////////////////////////
	public static void init(InputStream in) throws JKXmlException {
		init(in, "");
	}

	// //////////////////////////////////////////////////////////////
	public static void init(InputStream in, String prefix) throws JKXmlException {
		ReportManager manager = new ReportManager(in, prefix, "");
		addReports(manager.getInstanceReports());
	}

	// //////////////////////////////////////////////////////////////
	public static void clearReports() {
		allReports.clear();
	}

	private static void loadDatabaseReports() throws RecordNotFoundException, DaoException {
		DynamicDao dao = DaoFactory.createDynamicDao("conf_reports");
		ArrayList<Record> records = dao.lstRecords();
		for (Record record : records) {
			databaseReports.put(record.getFieldValueAsString("report_name"), record.getFieldValueAsString("report_file"));
		}
	}

}
