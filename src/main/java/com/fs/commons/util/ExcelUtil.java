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
import com.fs.commons.parsers.ParserException;
import com.jk.logging.JKLogger;

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
	private static JFileChooser fileChooser = new JFileChooser();

	/**
	 *
	 * @param model
	 *            ArrayList
	 */
	public static void buildExcelSheet(final FSTableModel model) {
		final ExcelUtil sheet = new ExcelUtil(model);
		try {
			final File file = GeneralUtility.createTempFile("xls");
			sheet.writeTo(file);
			GeneralUtility.executeFile(file.getAbsolutePath());
			file.deleteOnExit();
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Object getCellValue(final HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		if (DateTimeUtil.isDate(cell.toString(), "dd-MMM-yyyy")) {
			return new Date(cell.getDateCellValue().getTime());
		}
		return cell.toString();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) {
		final ApplicationManager instance = ApplicationManager.getInstance();
		try {
			instance.init();
			final TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta("tmp_allocated_students");
			ExcelUtil.parseFile(new FileInputStream(new File("D:/Allocation Report sample - Copy.xls")), tableMeta, "faculty_id");
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static ArrayList<Record> parseFile(final InputStream selectedFile, final TableMeta tableMeta, final String... headers)
			throws ParserException {
		final ArrayList<Record> list = new ArrayList<Record>();
		try {
			final POIFSFileSystem fs = new POIFSFileSystem(selectedFile);
			final HSSFWorkbook wb = new HSSFWorkbook(fs);
			final HSSFSheet sheet = wb.getSheetAt(0);
			final Iterator rows = sheet.rowIterator();
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

		} catch (final Exception e) {
			throw new ParserException(e);
		}
		return list;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Record populateRecordByFieldsIndex(final HSSFRow row, final TableMeta tableMeta, final Hashtable<String, Integer> customHeaders) {
		final Record record = tableMeta.createEmptyRecord();
		int index = 0;
		for (final Field field : record.getFields()) {
			final HSSFCell cell = row.getCell(index++);
			field.setValue(getCellValue(cell));
		}
		return record;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Record populateRecordByHeaders(final HSSFRow row, final TableMeta tableMeta, final Hashtable<String, Integer> customHeaders) {
		final Record record = tableMeta.createEmptyRecord();
		final Set<String> keySet = customHeaders.keySet();
		for (final String key : keySet) {
			final Integer index = customHeaders.get(key);
			final HSSFCell cell = row.getCell(index);
			record.setFieldValue(key, getCellValue(cell));
		}
		return record;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Record populateRow(final TableMeta tableMeta, final HSSFRow row, final Hashtable<String, Integer> customHeaders)
			throws ParseException {
		if (customHeaders != null) {
			return populateRecordByHeaders(row, tableMeta, customHeaders);
		} else {
			return populateRecordByFieldsIndex(row, tableMeta, customHeaders);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	private static Hashtable<String, Integer> processHeaders(final String[] headers, final HSSFRow row) {
		final Hashtable<String, Integer> rowHeaderToIndex = new Hashtable<String, Integer>();
		final Iterator<Cell> cells = row.cellIterator();
		int index = 0;
		while (cells.hasNext()) {
			final Cell cell = cells.next();
			final String headerName = getCellValue((HSSFCell) cell).toString();
			rowHeaderToIndex.put(headerName, index++);
		}
		final Hashtable<String, Integer> customHeaderToIndex = new Hashtable<String, Integer>();

		for (final String header : headers) {
			final Integer headerIndex = rowHeaderToIndex.get(header);
			if (headerIndex != null) {
				customHeaderToIndex.put(header, headerIndex);
			}

		}
		return customHeaderToIndex;
	}

	// ArrayList of Records
	private final HSSFSheet sheet;

	private final HSSFWorkbook workbook;

	private final FSTableModel model;

	/**
	 *
	 * @param model2
	 *            ArrayList
	 */
	public ExcelUtil(final FSTableModel model) {
		this.workbook = new HSSFWorkbook();
		this.sheet = this.workbook.createSheet();
		this.model = model;
		createColumnHeaders();
		createRows();
		setColumnsWidth();
	}

	/**
	 */
	protected void createColumnHeaders() {
		final HSSFRow headersRow = this.sheet.createRow(0);
		final HSSFFont font = this.workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		final HSSFCellStyle style = this.workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		int counter = 1;
		for (int i = 0; i < this.model.getColumnCount(); i++) {
			final HSSFCell cell = headersRow.createCell(counter++);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(this.model.getColumnName(i));
			cell.setCellStyle(style);
		}
	}

	/**
	 *
	 * @param rowIndex
	 *            int
	 */
	protected void createRow(final int rowIndex) {
		final HSSFRow row = this.sheet.createRow(rowIndex + 1); // since the
																// rows in
		// excel starts from 1
		// not 0
		final HSSFCellStyle style = this.workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		int counter = 1;
		for (int i = 0; i < this.model.getColumnCount(); i++) {
			final HSSFCell cell = row.createCell(counter++);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			final Object value = this.model.getValueAt(rowIndex, i);
			setValue(cell, value);
			cell.setCellStyle(style);
		}
	}

	/**
	 */
	protected void createRows() {
		for (int i = 0; i < this.model.getRowCount(); i++) {
			createRow(i);
		}
	}

	/**
	 */
	protected void setColumnsWidth() {
		int counter = 1;
		for (int i = 0; i < this.model.getColumnCount(); i++) {
			if (this.model.isVisible(i)) {
				this.sheet.autoSizeColumn(counter++);
				// sheet.setColumnWidth((short) i, (short)
				// (model.getColunmWidth(i) * 255));
			}
		}
	}

	/**
	 * @param cell
	 * @param value
	 */
	private void setValue(final HSSFCell cell, final Object value) {
		if (value == null) {
			cell.setCellValue("-");
		} else if (value instanceof Float || value instanceof Double || value instanceof Integer || value instanceof Long
				|| value instanceof BigDecimal) {
			cell.setCellValue(Double.parseDouble(value.toString()));
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		} else if (value instanceof Date) {
			cell.setCellValue((Date) value);
		} else {
			JKLogger.info("No Special excel r endering for class : " + value.getClass().getName());
			cell.setCellValue(value.toString());
		}
	}

	/**
	 *
	 * @param file
	 *            File
	 * @throws IOException
	 */
	public void writeTo(final File file) throws IOException {
		final OutputStream fout = new FileOutputStream(file);
		this.workbook.write(fout);
		fout.close();
	}

	/**
	 *
	 * @param out
	 *            OutputStream
	 * @throws IOException
	 */
	public void writeTo(final OutputStream out) throws IOException {
		this.workbook.write(out);
	}
}
