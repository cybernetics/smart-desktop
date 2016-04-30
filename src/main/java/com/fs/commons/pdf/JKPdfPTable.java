/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      16/4/2008     Jamil Shreet    -Modify the following methods : 
 */
package com.fs.commons.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class JKPdfPTable extends PdfPTable {

	public JKPdfPTable(int colunms) {
		this(colunms, false);
	}

	public JKPdfPTable(int col, boolean rtl) {
		super(col);
		setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
	}

	public void addCellText(String text) throws DocumentException {
		super.addCell(new JKPDFCell(text));
	}

	@Override
	public void addCell(String text) {
		throw new IllegalArgumentException("Please call addCellText");
	}

	/**
	 * @1.1
	 * @param text
	 * @param vertical
	 * @param fontSize
	 * @throws DocumentException
	 */
	public void addCellText(String text, boolean vertical, int fontSize) throws DocumentException {
		JKPDFCell cell = new JKPDFCell(text, fontSize, 1);

		if (vertical) {
			cell.setRotation(270);
		}
		super.addCell(cell);

	}

	/**
	 * @1.1
	 * @param text
	 * @param fontSize
	 * @return
	 * @throws DocumentException
	 */
	public void addCellText(String text, int fontSize) throws DocumentException {
		JKPDFCell cell = new JKPDFCell(text, fontSize,1);

		super.addCell(cell);
	}

	public void addCellDummyText(Object o) throws DocumentException {
		JKPDFCell cell = new JKPDFCell(o.toString(), false);
		super.addCell(cell);
	}

}
