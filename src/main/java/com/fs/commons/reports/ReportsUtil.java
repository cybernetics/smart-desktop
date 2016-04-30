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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.SwingUtilities;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.JKReportViewer;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author u087
 */
public class ReportsUtil {
	private static void initParams(final HashMap<String, Object> params) {
		params.put("SUBREPORT_DIR", "/resources/reports/");
	}

	/**
	 * @param report
	 * @param params
	 * @throws ReportException
	 */
	public static void printReport(final Report report, final HashMap params) throws ReportException {
		printReport(report, params, DataSourceFactory.getDefaultDataSource());
	}

	public static void printReport(final Report report, final HashMap hash, final DataSource dataSource) throws ReportException {
		Connection connection = null;
		try {
			connection = dataSource.getQueryConnection();
			printReport(report, hash, connection, 1);
		} catch (final DaoException e) {
			throw new ReportException(e);
		} finally {
			dataSource.close(connection);
		}
	}

	public static void printReport(final Report report, final HashMap params, final int copies) throws ReportException {
		Connection connection = null;
		final DataSource defaultDataSource = DataSourceFactory.getDefaultDataSource();
		try {
			connection = defaultDataSource.getQueryConnection();
			printReport(report, params, connection, copies);
		} catch (final DaoException e) {
			throw new ReportException(e);
		} finally {
			defaultDataSource.close(connection);
		}
	}

	/**
	 *
	 * @param report
	 * @param params
	 * @param dataSource
	 * @throws ReportException
	 */
	public static void printReport(final Report report, final HashMap params, final JRDataSource dataSource) throws ReportException {
		Connection conn = null;
		try {
			conn = DataSourceFactory.getDefaultDataSource().getQueryConnection();
			params.put("FS_CONNECTION", conn);
			printReportWithDataSource(report, params, dataSource);
		} catch (final DaoException e) {
			throw new ReportException(e);
		} finally {
			DataSourceFactory.getDefaultDataSource().close(conn);
		}
	}

	/**
	 * @param report
	 * @param params
	 * @param connection
	 * @throws ReportException
	 */
	private static void printReport(final Report report, final HashMap<String, Object> params, final Connection connection, final int copies)
			throws ReportException {
		InputStream inputStream = null;
		try {
			System.err.println("Printing Report : " + report.getSourceFileName());
			System.err.println("Params  :" + params);
			initParams(params);
			inputStream = report.getInputStream();
			final JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, connection);
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
		} catch (final Exception ex) {
			ex.printStackTrace();
			throw new ReportException(ex);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void printReport(final Report report, final Hashtable hash) {
		final HashMap map = new HashMap(hash);
		printReport(report, map);

	}

	/**
	 *
	 * @param report
	 * @param paramters
	 * @return
	 * @throws DaoException
	 */
	public static byte[] printReportAsPDF(final Report report, final HashMap params, final JRDataSource source) throws ReportException {
		Connection connection = null;
		try {
			initParams(params);
			JasperPrint jasperPrint;
			connection = DataSourceFactory.getDefaultDataSource().getQueryConnection();
			if (source == null) {
				jasperPrint = JasperFillManager.fillReport(report.getInputStream(), params, connection);
			} else {
				params.put("REPORT_CONNECTION", connection);
				jasperPrint = JasperFillManager.fillReport(report.getInputStream(), params, source);
			}
			if (jasperPrint.getPages().size() > 0) {
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				JasperExportManager.exportReportToPdfStream(jasperPrint, out);
				// GeneralUtility.writeDataToFile(out.toByteArray(), new
				// File("d:\\test.pdf"));
				return out.toByteArray();
			}
			// throw new EmptyReportException("Empty Report");
			return new byte[0];
		} catch (final Exception ex) {
			throw new ReportException(ex);
		} finally {
			DataSourceFactory.getDefaultDataSource().close(connection);
		}
	}

	/**
	 *
	 * @param report
	 * @param params
	 * @param dataSource
	 * @throws ReportException
	 */
	public static void printReportWithDataSource(final Report report, final HashMap<String, Object> params, final JRDataSource dataSource)
			throws ReportException {
		// Logger.info("Printing Report : " + report.getAbsolutSourcePath());
		try {
			// params.put("SUBREPORT_DIR", GeneralUtility.getReportsOutPath() +
			// GeneralUtility.getFileSeparator());
			initParams(params);
			final JasperPrint jasperPrint = JasperFillManager.fillReport(report.getInputStream(), params, dataSource);
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
		} catch (final Exception ex) {
			throw new ReportException(ex);
		}
	}

	// public static void showReport(String reportName, Hashtable hash) {
	// printReport(ReportManager.getReport(reportName), hash);
	// }

	public static void showReport(final String name, final HashMap properties) {
		showReport(name, properties, 1);
	}

	public static void showReport(final String name, final HashMap properties, final int reportCount) {
		final Report report = ReportManager.getReport(name);
		printReport(report, properties);
	}

	/**
	 * @param report
	 * @param params
	 * @return
	 * @throws ReportException
	 */
	public byte[] printReportAsPdf(final String reportName, final HashMap params) throws ReportException {
		final Report report = ReportManager.getReport(reportName);
		return printReportAsPDF(report, params, null);
	}

}
