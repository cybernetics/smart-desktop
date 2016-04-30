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

	private QueryTableModel model;

	private boolean showCounter = true;

	String reportTitle;

	// it will be handled auto. by dire="rtl"
	static SimpleDateFormat format = new SimpleDateFormat(SwingUtility.getDatePattern());

	/**
	 * 
	 * @param model
	 *            QueryTableModel
	 * @param title
	 *            String
	 */
	public TableModelHtmlBuilder(QueryTableModel model, String title) {
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
	public TableModelHtmlBuilder(QueryTableModel model, String title, boolean showCounter) {
		this.model = model;
		this.showCounter = showCounter;
		this.reportTitle = title;
	}

	/**
	 * 
	 * @return String
	 */
	public String buildHtml() {
		StringBuffer buf = new StringBuffer("<html "+getPageDirection()+">");
		buf.append("<head>\n");
		buf.append("<title>"+getReportTitle()+"</title>");
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
	 * @return
	 */
	private String getPageDirection() {
		return SwingUtility.isLeftOrientation()?"": " dir='rtl' ";
		
	}

	/**
	 * buildColunmsRow
	 * 
	 * @return Object
	 */
	private Object buildColunmsRow() {
		StringBuffer buf = new StringBuffer();
		buf.append("<tr>");
		/** @todo add optional counter checking */
		buf.append(buildDataCell("#"));
		for (int i = 0; i < model.getColumnCount(); i++) {
			if (model.isVisible(i)) {
				String cell = buildDataCell(Lables.get(model.getActualColumnName(i)),true);
				buf.append(cell);
			}
		}
		buf.append("</tr>\n");
		return buf.toString();
	}

	/**
	 * buildDataTable
	 * 
	 * @return Object
	 */
	private String buildDataTable() {
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < model.getRowCount(); i++) {
			buf.append(buildDataRow(i));
		}
		return buf.toString();
	}

	/**
	 * buildDataRow
	 * 
	 * @param rowIndex
	 *            int
	 * @return Object
	 */
	private Object buildDataRow(int rowIndex) {
		StringBuffer buf = new StringBuffer("<tr>");
		/** @todo add optional counter checking */
		buf.append(buildDataCell((rowIndex + 1) + ""));
		for (int i = 0; i < model.getColumnCount(); i++) {
			if (model.isVisible(i)) {
				Object valueAt = model.getValueAt(rowIndex, i);
				String value = valueAt==null?"-": valueAt.toString();
				if (model.getColumnType(i) == Types.DATE && !value.equals(QueryTableModel.EMPETY_STRING)) {
					value = format.format(Date.valueOf(value));
				}
				buf.append(buildDataCell(value));
			}
		}
		buf.append("</tr>");
		return buf.toString();
	}

	/**
	 * 
	 * @param data
	 * @param bold
	 * @return
	 */
	private String buildDataCell(String data) {
		return buildDataCell(data, false);
	}
	
	/**
	 * buildDataCell
	 * 
	 * @param data
	 *            Object
	 * @return Object
	 */
	private String buildDataCell(String data,boolean bold) {
		data=(data==null || data.equals("")?"-":data);
		if(bold){
			data="<b>"+data+"</b>";
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
//	private String buildDataCell(String data, int width) {
//		return "<td nowrap width='" + width + "'>" + data + "</td>\n";
//	}

	/**
	 * 
	 * @param args
	 *            String[]
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		QueryTableModel model = new QueryTableModel("select std_record_id,reg_date from students_info where std_record_id <100");
		// HtmlBuilder builder = new HtmlBuilder(model,"");
		PrintUtil.printQueryModel(model, "Test");
		// Utility.writeDataToFile(builder.buildHtml(), "c:\\test.html");
	}

	/**
	 * 
	 * @param name
	 *            String
	 */
	public static void setCompanyName(String name) {
		companyName = name;
	}

	public void setModel(QueryTableModel model) {
		this.model = model;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public void setShowCounter(boolean showCounter) {
		this.showCounter = showCounter;
	}

	/**
	 * 
	 * @param logo
	 *            byte[]
	 * @throws IOException
	 */
	public static void setCompanyLogo(byte[] logo)  {
		if (logo != null) {
			try {
				logoFile = GeneralUtility.writeDataToTempFile(logo, ".jpg");
			} catch (IOException e) {
				System.err.println("Unable to cache logo image");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @return String
	 */
	protected String getReportHeader() {
		StringBuffer header = new StringBuffer("");
		header.append("<div align=center style='font-family: Arial;font-size: 14pt;font-weight: bold;'>\n");
		if(companyName!=null){
			header.append(companyName);
		}
		header.append("<br>");
		if (logoFile != null && logoFile.getAbsolutePath()!=null) {
			header.append("<img src='" + logoFile.getAbsolutePath() + "' />");
			header.append("<p>");
		}
		header.append("</div>\n");
		if (reportTitle != null) {
			header.append("<h1 align=center>");
			header.append(reportTitle);
			header.append("</h1>\n");
		}
		return header.toString();
	}

	/**
	 * 
	 * @return QueryTableModel
	 */
	public QueryTableModel getModel() {
		return model;
	}

	public boolean isShowCounter() {
		return showCounter;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public static String getCompanyName() {
		return companyName;
	}

	
}
