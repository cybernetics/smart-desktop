package com.fs.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFileChooser;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import com.fs.commons.application.ApplicationManager;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.comp.model.FSTableModel;
import com.fs.commons.logging.Logger;
import com.fs.commons.parsers.ParserException;

/**
 * 
 * <p>
 * Title: QAC
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ExcelUtil {
	// ArrayList of Records
	private HSSFSheet sheet;

	private HSSFWorkbook workbook;

	private static JFileChooser fileChooser = new JFileChooser();

	private FSTableModel model;

	/**
	 * 
	 * @param model
	 *            ArrayList
	 */
	public static void buildExcelSheet(FSTableModel model) {
		ExcelUtil sheet = new ExcelUtil(model);
		try {
			File file = GeneralUtility.createTempFile("xls");
			sheet.writeTo(file);
			GeneralUtility.executeFile(file.getAbsolutePath());
			file.deleteOnExit();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 
	 * @param model2
	 *            ArrayList
	 */
	public ExcelUtil(FSTableModel model) {
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet();
		this.model = model;
		createColumnHeaders();
		createRows();
		setColumnsWidth();
	}

	/**
	 */
	protected void setColumnsWidth() {
		int counter = 1;
		for (int i = 0; i < model.getColumnCount(); i++) {
			if (model.isVisible(i)) {
				sheet.autoSizeColumn(counter++);
				// sheet.setColumnWidth((short) i, (short)
				// (model.getColunmWidth(i) * 255));
			}
		}
	}

	/**
	 */
	protected void createColumnHeaders() {
		HSSFRow headersRow = sheet.createRow(0);
		HSSFFont font = this.workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle style = this.workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		int counter = 1;
		for (int i = 0; i < model.getColumnCount(); i++) {
			HSSFCell cell = headersRow.createCell(counter++);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(model.getColumnName(i));
			cell.setCellStyle(style);
		}
	}

	/**
	 */
	protected void createRows() {
		for (int i = 0; i < model.getRowCount(); i++) {
			createRow(i);
		}
	}

	/**
	 * 
	 * @param rowIndex
	 *            int
	 */
	protected void createRow(int rowIndex) {
		HSSFRow row = sheet.createRow(rowIndex + 1); // since the rows in
		// excel starts from 1
		// not 0
		HSSFCellStyle style = this.workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		int counter = 1;
		for (int i = 0; i < model.getColumnCount(); i++) {
			HSSFCell cell = row.createCell(counter++);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			Object value = model.getValueAt(rowIndex, i);
			setValue(cell, value);
			cell.setCellStyle(style);
		}
	}

	/**
	 * @param cell
	 * @param value
	 */
	private void setValue(HSSFCell cell, Object value) {
		if(value==null){
			cell.setCellValue("-");
		}
		else if (value instanceof Float || value instanceof Double || value instanceof Integer || value instanceof Long || value instanceof BigDecimal) {
			cell.setCellValue(Double.parseDouble(value.toString()));
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		} else if (value instanceof Date) {
			cell.setCellValue((Date) value);
		} else {
			Logger.info("No Special excel r endering for class : " + value.getClass().getName());
			cell.setCellValue(value.toString());
		}
	}

	/**
	 * 
	 * @param file
	 *            File
	 * @throws IOException
	 */
	public void writeTo(File file) throws IOException {
		OutputStream fout = new FileOutputStream(file);
		workbook.write(fout);
		fout.close();
	}

	/**
	 * 
	 * @param out
	 *            OutputStream
	 * @throws IOException
	 */
	public void writeTo(OutputStream out) throws IOException {
		workbook.write(out);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static ArrayList<Record> parseFile(InputStream selectedFile, TableMeta tableMeta, String... headers) throws ParserException {
		ArrayList<Record> list = new ArrayList<Record>();
		try {
			POIFSFileSystem fs = new POIFSFileSystem(selectedFile);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			Iterator rows = sheet.rowIterator();
			boolean firstRow = true;
			Hashtable<String, Integer> customHeaders = null;
			while (rows.hasNext()) {
				if (firstRow) {
					if (headers.length != 0) {
						customHeaders = processHeaders(headers, (HSSFRow) rows.next());
						firstRow = false;
					} else {
						rows.next();// to skip first row
						firstRow = false;
						continue;
					}
				}
				list.add(populateRow(tableMeta, (HSSFRow) rows.next(), customHeaders));
			}

		} catch (Exception e) {
			throw new ParserException(e);
		}
		return list;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	private static Hashtable<String, Integer> processHeaders(String[] headers, HSSFRow row) {
		Hashtable<String, Integer> rowHeaderToIndex = new Hashtable<String, Integer>();
		Iterator<Cell> cells = row.cellIterator();
		int index = 0;
		while (cells.hasNext()) {
			Cell cell = cells.next();
			String headerName = getCellValue(((HSSFCell) cell)).toString();
			rowHeaderToIndex.put(headerName, index++);
		}
		Hashtable<String, Integer> customHeaderToIndex = new Hashtable<String, Integer>();

		for (String header : headers) {
			Integer headerIndex = rowHeaderToIndex.get(header);
			if (headerIndex != null) {
				customHeaderToIndex.put(header, headerIndex);
			}

		}
		return customHeaderToIndex;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Record populateRow(TableMeta tableMeta, HSSFRow row, Hashtable<String, Integer> customHeaders) throws ParseException {
		if (customHeaders != null) {
			return populateRecordByHeaders(row, tableMeta, customHeaders);
		} else {
			return populateRecordByFieldsIndex(row, tableMeta, customHeaders);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Record populateRecordByFieldsIndex(HSSFRow row, TableMeta tableMeta, Hashtable<String, Integer> customHeaders) {
		Record record = tableMeta.createEmptyRecord();
		int index = 0;
		for (Field field : record.getFields()) {
			HSSFCell cell = row.getCell(index++);
			field.setValue(getCellValue(cell));
		}
		return record;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Record populateRecordByHeaders(HSSFRow row, TableMeta tableMeta, Hashtable<String, Integer> customHeaders) {
		Record record = tableMeta.createEmptyRecord();
		Set<String> keySet = customHeaders.keySet();
		for (String key : keySet) {
			Integer index = customHeaders.get(key);
			HSSFCell cell = row.getCell(index);
			record.setFieldValue(key, getCellValue(cell));
		}
		return record;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Object getCellValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		if (DateTimeUtil.isDate(cell.toString(), "dd-MMM-yyyy")) {
			return new Date(cell.getDateCellValue().getTime());
		}
		return cell.toString();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		ApplicationManager instance = ApplicationManager.getInstance();
		try {
			instance.init();
			TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta("tmp_allocated_students");
			ExcelUtil.parseFile(new FileInputStream(new File("D:/Allocation Report sample - Copy.xls")), tableMeta, "faculty_id");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
