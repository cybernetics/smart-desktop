/**
 * 
 */
package com.fs.commons.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.SwingUtilities;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKSession;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.commons.desktop.JKReportViewer;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author u087
 */
public class JKReportsUtil {
	/**
	 * 
	 * @param report
	 * @param params
	 * @param dataSource
	 * @throws ReportException
	 */
	public static void printReport(JKReport report, HashMap params, JRDataSource dataSource) throws ReportException {
		Connection conn = null;
		try {
			conn = JKDataSourceFactory.getDefaultDataSource().getQueryConnection();
			params.put("FS_CONNECTION", conn);
			printReportWithDataSource(report, params, dataSource);
		} catch (JKDataAccessException e) {
			throw new ReportException(e);
		} finally {
			JKDataSourceFactory.getDefaultDataSource().close(conn);
		}
	}

	public static void printReport(JKReport report, HashMap params, int copies) throws ReportException {
		Connection connection = null;
		JKDataSource defaultDataSource = JKDataSourceFactory.getDefaultDataSource();
		try {
			connection = defaultDataSource.getQueryConnection();
			printReport(report, params, connection, copies);
		} catch (JKDataAccessException e) {
			throw new ReportException(e);
		} finally {
			defaultDataSource.close(connection);
		}
	}

	/**
	 * @param report
	 * @param params
	 * @param connection
	 * @throws ReportException
	 */
	private static void printReport(JKReport report, HashMap params, Connection connection, int copies) throws ReportException {
		InputStream inputStream =null;
		try {
			System.err.println("Printing Report : " + report.getSourceFileName());
			System.err.println("Params  :" + params);
			initParams(params);
			inputStream= report.getInputStream();
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, connection);
			if (jasperPrint.getPages().size() > 0) {
				final JKReportViewer viewer = new JKReportViewer(jasperPrint, false);
				viewer.setPrintCount(copies);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						viewer.setVisible(true);
					}
				});
			} else {
				throw new EmptyReportException("EMPTY_REPORT");
			}
		} catch (Exception ex) {			
			ex.printStackTrace();
			throw new ReportException(ex);
		}finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void printReport(JKReport report, HashMap params, JKSession session) throws ReportException {
		printReport(report, params, session.getConnection(), 1);
	}
	
	private static void initParams(HashMap params) {
		String path = JKReportsUtil.class.getResource("/resources/reports/").toString();
		params.put("SUBREPORT_DIR", path);
	}

	/**
	 * 
	 * @param report
	 * @param params
	 * @param dataSource
	 * @throws ReportException
	 */
	public static void printReportWithDataSource(JKReport report, HashMap<String, Object> params, JRDataSource dataSource) throws ReportException {
		// Logger.info("Printing Report : " + report.getAbsolutSourcePath());
		try {
			// params.put("SUBREPORT_DIR", GeneralUtility.getReportsOutPath() +
			// GeneralUtility.getFileSeparator());
			initParams(params);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report.getInputStream(), params, dataSource);
			if (jasperPrint.getPages().size() > 0) {
				final JKReportViewer viewer = new JKReportViewer(jasperPrint, false);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						viewer.setVisible(true);
					}
				});
			} else {
				throw new EmptyReportException("EMPTY_REPORT");
			}
		} catch (Exception ex) {
			throw new ReportException(ex);
		}
	}

	public static void printReport(JKReport report, HashMap hash, JKDataSource dataSource) throws ReportException {
		Connection connection = null;
		try { 
			connection = dataSource.getQueryConnection();
			printReport(report, hash, connection, 1);
		} catch (JKDataAccessException e) {
			throw new ReportException(e);
		} finally {
			dataSource.close(connection);
		}
	}

	/**
	 * @param report
	 * @param params
	 * @return
	 * @throws ReportException
	 */
	public byte[] printReportAsPdf(String reportName, HashMap params) throws ReportException {
		JKReport report = JKReportManager.getReport(reportName);
		return printReportAsPDF(report, params, null);
	}

	/**
	 * @param report
	 * @param params
	 * @throws ReportException
	 */
	public static void printReport(JKReport report, HashMap params) throws ReportException {
		printReport(report, params, JKDataSourceFactory.getDefaultDataSource());
	}

	/**
	 * 
	 * @param report
	 * @param paramters
	 * @return
	 * @throws JKDataAccessException
	 */
	public static byte[] printReportAsPDF(JKReport report, HashMap params, JRDataSource source) throws ReportException {
		Connection connection = null;
		try {
			initParams(params);
			JasperPrint jasperPrint;
			connection = JKDataSourceFactory.getDefaultDataSource().getQueryConnection();
			if (source == null) {
				jasperPrint = JasperFillManager.fillReport(report.getInputStream(), params, connection);
			} else {
				params.put("REPORT_CONNECTION", connection);
				jasperPrint = JasperFillManager.fillReport(report.getInputStream(), params, source);
			}
			if (jasperPrint.getPages().size() > 0) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				JasperExportManager.exportReportToPdfStream(jasperPrint, out);
				// GeneralUtility.writeDataToFile(out.toByteArray(), new
				// File("d:\\test.pdf"));
				return out.toByteArray();
			}
			// throw new EmptyReportException("Empty Report");
			return new byte[0];
		} catch (Exception ex) {
			throw new ReportException(ex);
		} finally {
			JKDataSourceFactory.getDefaultDataSource().close(connection);
		}
	}

	// public static void showReport(String reportName, Hashtable hash) {
	// printReport(ReportManager.getReport(reportName), hash);
	// }

	public static void printReport(JKReport report, Hashtable hash) {
		HashMap map = new HashMap(hash);
		printReport(report, map);

	}

	public static void showReport(String name, HashMap properties) {
		showReport(name, properties, 1);
	}

	public static void showReport(String name, HashMap properties, int reportCount) {
		JKReport report = JKReportManager.getReport(name);
		printReport(report, properties);
	}

}
