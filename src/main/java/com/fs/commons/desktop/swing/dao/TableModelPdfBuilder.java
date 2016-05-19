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

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.fs.commons.locale.Lables;
import com.fs.commons.reports.ReportException;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.ExceptionUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @1.1
 * 
 * @author jamil
 *
 */
public class TableModelPdfBuilder {

	/**
	 *
	 * @param model
	 */
	public static void buildPdfDocument(final QueryTableModel model) {
		try {
			final TableModelPdfBuilder builer = new TableModelPdfBuilder(model);
			final File file = File.createTempFile("jksoft", ".pdf");

			builer.buildPdfDocument(file.getAbsolutePath());
			GeneralUtility.executeFile(file.getAbsolutePath());
		} catch (final ReportException e) {
			ExceptionUtil.handle(e);
		} catch (final IOException e) {
			ExceptionUtil.handle(e);
		}

	}

	private Document document;

	private PdfWriter writer;

	private final QueryTableModel model;

	String header = "PDF HEADER";

	String footer = "PDF FOOTER";

	boolean landscape;

	boolean rtl = true;

	private Rectangle pageSize = PageSize.A4;

	private Font font;

	private int rotationDegree = 0;

	/**
	 *
	 * @param model
	 * @param title
	 */
	public TableModelPdfBuilder(final QueryTableModel model) {
		this.model = model;
	}

	/**
	 * @throws ReportException
	 * @throws IOException
	 *
	 */
	public void buildPdfDocument(final OutputStream out) throws ReportException, IOException {
		this.document = new Document(getPageRotation());
		try {
			this.writer = PdfWriter.getInstance(this.document, out);
			this.document.open();
			final PdfPTable pdfPTable = createPdfTable();

			this.document.add(pdfPTable);
			this.document.close();
		} catch (final DocumentException e) {
			throw new ReportException(e);
		}
	}

	/**
	 *
	 * @param fileName
	 * @throws ReportException
	 * @throws IOException
	 */
	public void buildPdfDocument(final String fileName) throws ReportException, IOException {
		buildPdfDocument(new FileOutputStream(fileName));
	}

	/**
	 *
	 * @param pdfPTable
	 * @param font
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void createPdfFooter(final PdfPTable pdfPTable, Font font) throws DocumentException, IOException {
		final float width = pdfPTable.getWidthPercentage();
		final int headerWidth = (int) width;
		font = getFont();
		final PdfPCell footerCell = new PdfPCell(new Phrase(getFooter(), font));
		footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		footerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		footerCell.setColspan(headerWidth);
		footerCell.setBorder(0);
		footerCell.setUseDescender(true);
		pdfPTable.addCell(footerCell);
		pdfPTable.setFooterRows(1);
	}

	/**
	 *
	 * @param pdfPTable
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void createPdfHeaders(final PdfPTable pdfPTable, Font font) throws DocumentException, IOException {
		font = getFont();
		final float width = pdfPTable.getWidthPercentage();
		final int headerWidth = (int) width;
		final PdfPCell headersCells = new PdfPCell(new Phrase(getHeader(), font));
		headersCells.setHorizontalAlignment(Element.ALIGN_CENTER);
		headersCells.setColspan(headerWidth);
		headersCells.setUseDescender(true);
		headersCells.setBorder(0);
		pdfPTable.addCell(headersCells);
		for (int i = 0; i < this.model.getColumnCount(); i++) {
			if (this.model.isVisible(i)) {
				final PdfPCell cell = new PdfPCell(new Phrase(Lables.get(this.model.getActualColumnName(i)), font));
				cell.setRotation(getRotationDegree());
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfPTable.addCell(cell);
			}
		}
		// Please explain
		pdfPTable.setHeaderRows(3);
		createPdfFooter(pdfPTable, font);

	}

	/**
	 *
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public PdfPTable createPdfTable() throws DocumentException, IOException {
		final Font font = getFont();
		int columnCount = 0;
		for (int i = 0; i < this.model.getColumnCount(); i++) {
			if (this.model.isVisible(i)) {
				columnCount++;
			}
		}
		final PdfPTable pdfPTable = new PdfPTable(columnCount);
		pdfPTable.setRunDirection(getRunDirection());
		pdfPTable.getDefaultCell().setRunDirection(getRunDirection());
		pdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		pdfPTable.getDefaultCell().setNoWrap(true);

		createPdfHeaders(pdfPTable, font);
		loadDataTable(pdfPTable, font);
		return pdfPTable;

	}

	/**
	 * @throws DocumentException
	 * @throws IOException
	 *
	 */
	protected Font getFont() throws DocumentException, IOException {
		if (this.font == null) {
			try {
				// TODO : check the follwing statment
				final BaseFont bf = BaseFont.createFont("c:/windows/fonts/times.ttf", BaseFont.IDENTITY_H, true);
				this.font = new Font(bf, 10, Font.NORMAL, Color.BLACK);
				return this.font;
			} catch (final DocumentException e) {
				throw e;
			} catch (final IOException e) {
				throw e;
			}
		} else {
			return this.font;
		}
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return this.footer;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return this.header;
	}

	/**
	 *
	 * @return
	 */
	private Rectangle getPageRotation() {
		return isLandscape() ? this.pageSize.rotate() : this.pageSize;
	}

	/**
	 * @return the pageSize
	 */
	public Rectangle getPageSize() {
		return this.pageSize;
	}

	/**
	 *
	 * @return
	 */
	private int getRotationDegree() {
		// TODO Auto-generated method stub
		return this.rotationDegree;
	}

	/**
	 *
	 * @return
	 */
	private int getRunDirection() {
		return isRtl() ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR;
	}

	/**
	 * @return the landscape
	 */
	public boolean isLandscape() {
		return this.landscape;
	}

	/**
	 * @return the rtl
	 */
	public boolean isRtl() {
		return this.rtl;
	}

	/**
	 *
	 * @param pdfPTable
	 * @param font
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void loadDataTable(final PdfPTable pdfPTable, Font font) throws DocumentException, IOException {
		font = getFont();
		for (int row = 0; row < this.model.getRowCount(); row++) {
			for (int column = 0; column < this.model.getColumnCount(); column++) {
				if (this.model.isVisible(column)) {
					final PdfPCell cell = new PdfPCell(new Phrase((String) this.model.getValueAt(row, column), font));
					cell.setNoWrap(true);
					cell.setRunDirection(getRunDirection());
					// TODO : make the alignment according to the colunm
					// datatype
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pdfPTable.addCell(cell);
				}
			}
		}
	}

	/**
	 * @param footer
	 *            the footer to set
	 */
	public void setFooter(final String footer) {
		this.footer = footer;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(final String header) {
		this.header = header;
	}

	/**
	 * @param landscape
	 *            the landscape to set
	 */
	public void setLandscape(final boolean landscape) {
		this.landscape = landscape;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(final Rectangle pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @param rotationDegree
	 *            the rotationDegree to set
	 */
	public void setRotationDegree(final int rotationDegree) {
		this.rotationDegree = rotationDegree;
	}

	/**
	 * @param rtl
	 *            the rtl to set
	 */
	public void setRtl(final boolean rtl) {
		this.rtl = rtl;
	}
}
