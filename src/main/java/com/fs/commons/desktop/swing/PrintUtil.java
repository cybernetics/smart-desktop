package com.fs.commons.desktop.swing;

import java.io.File;
import java.io.IOException;

import com.fs.commons.desktop.swing.dao.PnlQueryFields;
import com.fs.commons.desktop.swing.dao.QueryTableModel;
import com.fs.commons.desktop.swing.dao.TableModelHtmlBuilder;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class PrintUtil {
	public static final String REPORT_PANE_NAME = "JK-Viewer.exe";

	/**
	 * 
	 * @param model
	 *            QueryModel
	 */
	public static void printQueryModel(QueryTableModel model, String title) {
		TableModelHtmlBuilder builder = new TableModelHtmlBuilder(model, title);
		try {
			String data = builder.buildHtml();
			printHtml(data);
		} catch (Exception ex) {
			ExceptionUtil.handleException(new Exception("ERROR_PRINT" + "\n" + ex.getMessage(), ex));
		}
	}

	/**
	 * 
	 * @param data
	 *            String
	 * @throws IOException
	 */
	public static void printHtml(String data) throws IOException {
		final File file = GeneralUtility.writeDataToTempFile(data, ".html");
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					// Runtime.getRuntime().exec("cmd /c start " +
					// REPORT_PANE_NAME + " " + file.getAbsolutePath());
					Runtime.getRuntime().exec("cmd /c start " + file.getAbsolutePath());
				} catch (IOException ex) {
					ExceptionUtil.handleException(ex);
				}
			}
		});
		thread.start();
	}

	/**
	 * 
	 * @param args
	 *            String[]
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		QueryTableModel model = new QueryTableModel("select * from courses");
		SwingUtility.testPanel(new PnlQueryFields(model));
//		System.out.println(Arrays.toString(model.getColunmsVisilbleArray()));
		printQueryModel(model, "");
	}

}
