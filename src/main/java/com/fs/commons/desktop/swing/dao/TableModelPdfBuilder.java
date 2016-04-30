/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      29/04/2008     Jamil Shreet    -Add the following class :
 */
package com.fs.commons.desktop.swing.dao;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.fs.commons.locale.Lables;
import com.fs.commons.reports.ReportException;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;
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
 * @author jamil
 * 
 */
public class TableModelPdfBuilder {

	private Document document;

	private PdfWriter writer;

	private QueryTableModel model;

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
	 */
	public static void buildPdfDocument(QueryTableModel model) {
		try {
			TableModelPdfBuilder builer = new TableModelPdfBuilder(model);
			File file = File.createTempFile("jksoft", ".pdf");

			builer.buildPdfDocument(file.getAbsolutePath());
			GeneralUtility.executeFile(file.getAbsolutePath());
		} catch (ReportException e) {
			ExceptionUtil.handleException(e);
		} catch (IOException e) {
			ExceptionUtil.handleException(e);
		}

	}

	/**
	 * @return the landscape
	 */
	public boolean isLandscape() {
		return landscape;
	}

	/**
	 * @param landscape
	 *            the landscape to set
	 */
	public void setLandscape(boolean landscape) {
		this.landscape = landscape;
	}

	/**
	 * 
	 * @param model
	 * @param title
	 */
	public TableModelPdfBuilder(QueryTableModel model) {
		this.model = model;
	}

	/**
	 * 
	 * @param fileName
	 * @throws ReportException
	 * @throws IOException
	 */
	public void buildPdfDocument(String fileName) throws ReportException,
			IOException {
		buildPdfDocument(new FileOutputStream(fileName));
	}

	/**
	 * @throws ReportException
	 * @throws IOException
	 * 
	 */
	public void buildPdfDocument(OutputStream out) throws ReportException,
			IOException {
		document = new Document(getPageRotation());
		try {
			writer = PdfWriter.getInstance(document, out);
			document.open();
			PdfPTable pdfPTable = createPdfTable();

			document.add(pdfPTable);
			document.close();
		} catch (DocumentException e) {
			throw new ReportException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private Rectangle getPageRotation() {
		return isLandscape() ? pageSize.rotate() : pageSize;
	}

	/**
	 * @throws DocumentException
	 * @throws IOException
	 * 
	 */
	protected Font getFont() throws DocumentException, IOException {
		if (font == null) {
			try {
				// TODO : check the follwing statment
				BaseFont bf = BaseFont.createFont("c:/windows/fonts/times.ttf",
						BaseFont.IDENTITY_H, true);
				font = new Font(bf, 10, Font.NORMAL, Color.BLACK);
				return font;
			} catch (DocumentException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			}
		} else {
			return font;
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public PdfPTable createPdfTable() throws DocumentException, IOException {
		Font font = getFont();
		int columnCount = 0;
		for (int i = 0; i < model.getColumnCount(); i++) {
			if (model.isVisible(i)) {
				columnCount++;
			}
		}
		PdfPTable pdfPTable = new PdfPTable(columnCount);
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
	 * 
	 * @return
	 */
	private int getRunDirection() {
		return isRtl() ? PdfWriter.RUN_DIRECTION_RTL
				: PdfWriter.RUN_DIRECTION_LTR;
	}

	/**
	 * 
	 * @param pdfPTable
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void createPdfHeaders(PdfPTable pdfPTable, Font font)
			throws DocumentException, IOException {
		font = getFont();
		float width = pdfPTable.getWidthPercentage();
		int headerWidth = (int) width;
		PdfPCell headersCells = new PdfPCell(new Phrase(getHeader(), font));
		headersCells.setHorizontalAlignment(Element.ALIGN_CENTER);
		headersCells.setColspan(headerWidth);
		headersCells.setUseDescender(true);
		headersCells.setBorder(0);
		pdfPTable.addCell(headersCells);
		for (int i = 0; i < model.getColumnCount(); i++) {
			if (model.isVisible(i)) {
				PdfPCell cell = new PdfPCell(new Phrase(Lables.get(model
						.getActualColumnName(i)), font));
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
	 */
	private int getRotationDegree() {
		// TODO Auto-generated method stub
		return rotationDegree;
	}

	/**
	 * 
	 * @param pdfPTable
	 * @param font
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void createPdfFooter(PdfPTable pdfPTable, Font font)
			throws DocumentException, IOException {
		float width = pdfPTable.getWidthPercentage();
		int headerWidth = (int) width;
		font = getFont();
		PdfPCell footerCell = new PdfPCell(new Phrase(getFooter(), font));
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
	 * @param font
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void loadDataTable(PdfPTable pdfPTable, Font font)
			throws DocumentException, IOException {
		font = getFont();
		for (int row = 0; row < model.getRowCount(); row++) {
			for (int column = 0; column < model.getColumnCount(); column++) {
				if (model.isVisible(column)) {
					PdfPCell cell = new PdfPCell(new Phrase((String) model
							.getValueAt(row, column), font));
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
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer
	 *            the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	/**
	 * @return the pageSize
	 */
	public Rectangle getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(Rectangle pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the rtl
	 */
	public boolean isRtl() {
		return rtl;
	}

	/**
	 * @param rtl
	 *            the rtl to set
	 */
	public void setRtl(boolean rtl) {
		this.rtl = rtl;
	}

	/**
	 * @param rotationDegree
	 *            the rotationDegree to set
	 */
	public void setRotationDegree(int rotationDegree) {
		this.rotationDegree = rotationDegree;
	}
}
