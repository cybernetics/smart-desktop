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
package com.fs.commons.desktop.swing.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Types;
import java.text.SimpleDateFormat;

import com.fs.commons.desktop.swing.PrintUtil;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;

public class TableModelHtmlBuilder {

	private static String companyName;

	private static File logoFile;

	// it will be handled auto. by dire="rtl"
	static SimpleDateFormat format = new SimpleDateFormat(SwingUtility.getDatePattern());

	public static String getCompanyName() {
		return companyName;
	}

	/**
	 *
	 * @param args
	 *            String[]
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		final QueryTableModel model = new QueryTableModel("select std_record_id,reg_date from students_info where std_record_id <100");
		// HtmlBuilder builder = new HtmlBuilder(model,"");
		PrintUtil.printQueryModel(model, "Test");
		// Utility.writeDataToFile(builder.buildHtml(), "c:\\test.html");
	}

	/**
	 *
	 * @param logo
	 *            byte[]
	 * @throws IOException
	 */
	public static void setCompanyLogo(final byte[] logo) {
		if (logo != null) {
			try {
				logoFile = GeneralUtility.writeDataToTempFile(logo, ".jpg");
			} catch (final IOException e) {
				System.err.println("Unable to cache logo image");
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @param name
	 *            String
	 */
	public static void setCompanyName(final String name) {
		companyName = name;
	}

	private QueryTableModel model;

	private boolean showCounter = true;

	String reportTitle;

	/**
	 *
	 * @param model
	 *            QueryTableModel
	 * @param title
	 *            String
	 */
	public TableModelHtmlBuilder(final QueryTableModel model, final String title) {
		this(model, title, true);
	}

	/**
	 *
	 * @param model
	 *            QueryTableModel
	 * @param title
	 *            String
	 * @param showCounter
	 *            boolean
	 */
	public TableModelHtmlBuilder(final QueryTableModel model, final String title, final boolean showCounter) {
		this.model = model;
		this.showCounter = showCounter;
		this.reportTitle = title;
	}

	/**
	 * buildColunmsRow
	 *
	 * @return Object
	 */
	private Object buildColunmsRow() {
		final StringBuffer buf = new StringBuffer();
		buf.append("<tr>");
		/** @todo add optional counter checking */
		buf.append(buildDataCell("#"));
		for (int i = 0; i < this.model.getColumnCount(); i++) {
			if (this.model.isVisible(i)) {
				final String cell = buildDataCell(Lables.get(this.model.getActualColumnName(i)), true);
				buf.append(cell);
			}
		}
		buf.append("</tr>\n");
		return buf.toString();
	}

	/**
	 *
	 * @param data
	 * @param bold
	 * @return
	 */
	private String buildDataCell(final String data) {
		return buildDataCell(data, false);
	}

	/**
	 * buildDataCell
	 *
	 * @param data
	 *            Object
	 * @return Object
	 */
	private String buildDataCell(String data, final boolean bold) {
		data = data == null || data.equals("") ? "-" : data;
		if (bold) {
			data = "<b>" + data + "</b>";
		}
		return "<td nowrap>" + data + "</td>\n";
	}

	/**
	 * buildDataCell
	 *
	 * @param data
	 *            Object
	 * @param width
	 *            int
	 * @return Object
	 */
	// private String buildDataCell(String data, int width) {
	// return "<td nowrap width='" + width + "'>" + data + "</td>\n";
	// }

	/**
	 * buildDataRow
	 *
	 * @param rowIndex
	 *            int
	 * @return Object
	 */
	private Object buildDataRow(final int rowIndex) {
		final StringBuffer buf = new StringBuffer("<tr>");
		/** @todo add optional counter checking */
		buf.append(buildDataCell(rowIndex + 1 + ""));
		for (int i = 0; i < this.model.getColumnCount(); i++) {
			if (this.model.isVisible(i)) {
				final Object valueAt = this.model.getValueAt(rowIndex, i);
				String value = valueAt == null ? "-" : valueAt.toString();
				if (this.model.getColumnType(i) == Types.DATE && !value.equals(QueryTableModel.EMPETY_STRING)) {
					value = format.format(Date.valueOf(value));
				}
				buf.append(buildDataCell(value));
			}
		}
		buf.append("</tr>");
		return buf.toString();
	}

	/**
	 * buildDataTable
	 *
	 * @return Object
	 */
	private String buildDataTable() {
		final StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < this.model.getRowCount(); i++) {
			buf.append(buildDataRow(i));
		}
		return buf.toString();
	}

	/**
	 *
	 * @return String
	 */
	public String buildHtml() {
		final StringBuffer buf = new StringBuffer("<html " + getPageDirection() + ">");
		buf.append("<head>\n");
		buf.append("<title>" + getReportTitle() + "</title>");
		buf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1256\" />");
		buf.append("</head>");
		buf.append("<body >\n");
		buf.append(getReportHeader());
		buf.append("<table border=1 cellpadding=2 cellspacing=0 align=center>\n");
		buf.append(buildColunmsRow());
		buf.append(buildDataTable());
		buf.append("</table>\n");
		buf.append("</body>\n");
		buf.append("</html>\n");
		return buf.toString();
	}

	/**
	 *
	 * @return QueryTableModel
	 */
	public QueryTableModel getModel() {
		return this.model;
	}

	/**
	 *
	 * @return
	 */
	private String getPageDirection() {
		return SwingUtility.isLeftOrientation() ? "" : " dir='rtl' ";

	}

	/**
	 *
	 * @return String
	 */
	protected String getReportHeader() {
		final StringBuffer header = new StringBuffer("");
		header.append("<div align=center style='font-family: Arial;font-size: 14pt;font-weight: bold;'>\n");
		if (companyName != null) {
			header.append(companyName);
		}
		header.append("<br>");
		if (logoFile != null && logoFile.getAbsolutePath() != null) {
			header.append("<img src='" + logoFile.getAbsolutePath() + "' />");
			header.append("<p>");
		}
		header.append("</div>\n");
		if (this.reportTitle != null) {
			header.append("<h1 align=center>");
			header.append(this.reportTitle);
			header.append("</h1>\n");
		}
		return header.toString();
	}

	public String getReportTitle() {
		return this.reportTitle;
	}

	public boolean isShowCounter() {
		return this.showCounter;
	}

	public void setModel(final QueryTableModel model) {
		this.model = model;
	}

	public void setReportTitle(final String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public void setShowCounter(final boolean showCounter) {
		this.showCounter = showCounter;
	}

}
