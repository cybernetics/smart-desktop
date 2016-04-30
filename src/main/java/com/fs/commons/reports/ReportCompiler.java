//package com.fs.commons.reports;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.design.JasperDesign;
//import net.sf.jasperreports.engine.xml.JRXmlLoader;
//
//import com.fs.commons.util.GeneralUtility;
//
//public class ReportCompiler {
//	/**
//	 * @param sourcePath 
//	 * @param outPath 
//	 * @param prefix 
//	 * @param reports 
//	 *
//	 */
//	public static  void compileReports(String sourcePath, String outPath, String prefix, ArrayList<Report> reports) {
//		checkSourcePath(sourcePath);
//		checkOutpath(outPath);
//		for (int i = 0; i < reports.size(); i++) {
//			Report report = reports.get(i);
//			report.setAbsolutSourcePath(sourcePath + "/" + prefix + report.getName() + ".jrxml");
//			report.setAbsolutOutPath(outPath + "/" + prefix + report.getName() + ".jasper");
//
//			try {
//				JasperDesign jasperDesign = JRXmlLoader.load(GeneralUtility.getFileInputStream(report.getAbsolutSourcePath()));
//				JasperCompileManager.compileReportToFile(jasperDesign, report.getAbsolutOutPath());
//			} catch (JRException e) {
//				throw new RuntimeException("Unable to compile : " + report.getAbsolutSourcePath(), e);
//			} catch (FileNotFoundException e) {
//				throw new RuntimeException(e);
//			}
//		}
//	}
//	
//	/**
//	 * 
//	 * @param outPath
//	 */
//	private static void checkOutpath(String outPath) {
//		File file = new File(outPath);
//		if (!file.exists()) {
//			file.mkdir();
//		}
//	}
//
//
//	/**
//	 * 
//	 * @param sourcePath
//	 *            String
//	 */
//	protected static void checkSourcePath(String sourcePath) {
//		File file = new File(sourcePath);
//		if (!file.exists()) {
//			file.mkdir();
//		}
//	}
//
//
//}
