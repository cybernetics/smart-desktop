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
package com.fs.commons.desktop.swing;

import java.io.File;
import java.io.IOException;

import com.fs.commons.desktop.swing.dao.PnlQueryFields;
import com.fs.commons.desktop.swing.dao.QueryTableModel;
import com.fs.commons.desktop.swing.dao.TableModelHtmlBuilder;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.JKExceptionUtil;

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
	 * @param args
	 *            String[]
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		final QueryTableModel model = new QueryTableModel("select * from courses");
		SwingUtility.testPanel(new PnlQueryFields(model));
		// System.out.println(Arrays.toString(model.getColunmsVisilbleArray()));
		printQueryModel(model, "");
	}

	/**
	 *
	 * @param data
	 *            String
	 * @throws IOException
	 */
	public static void printHtml(final String data) throws IOException {
		final File file = GeneralUtility.writeDataToTempFile(data, ".html");
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Runtime.getRuntime().exec("cmd /c start " +
					// REPORT_PANE_NAME + " " + file.getAbsolutePath());
					Runtime.getRuntime().exec("cmd /c start " + file.getAbsolutePath());
				} catch (final IOException ex) {
					JKExceptionUtil.handle(ex);
				}
			}
		});
		thread.start();
	}

	/**
	 *
	 * @param model
	 *            QueryModel
	 */
	public static void printQueryModel(final QueryTableModel model, final String title) {
		final TableModelHtmlBuilder builder = new TableModelHtmlBuilder(model, title);
		try {
			final String data = builder.buildHtml();
			printHtml(data);
		} catch (final Exception ex) {
			JKExceptionUtil.handle(new Exception("ERROR_PRINT" + "\n" + ex.getMessage(), ex));
		}
	}

}
