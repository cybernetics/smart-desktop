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
package com.fs.commons.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class JKPdfPTable extends PdfPTable {

	public JKPdfPTable(final int colunms) {
		this(colunms, false);
	}

	public JKPdfPTable(final int col, final boolean rtl) {
		super(col);
		setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
	}

	@Override
	public void addCell(final String text) {
		throw new IllegalArgumentException("Please call addCellText");
	}

	public void addCellDummyText(final Object o) throws DocumentException {
		final JKPDFCell cell = new JKPDFCell(o.toString(), false);
		super.addCell(cell);
	}

	public void addCellText(final String text) throws DocumentException {
		super.addCell(new JKPDFCell(text));
	}

	/**
	 * @1.1
	 * 
	 * @param text
	 * @param vertical
	 * @param fontSize
	 * @throws DocumentException
	 */
	public void addCellText(final String text, final boolean vertical, final int fontSize) throws DocumentException {
		final JKPDFCell cell = new JKPDFCell(text, fontSize, 1);

		if (vertical) {
			cell.setRotation(270);
		}
		super.addCell(cell);

	}

	/**
	 * @1.1
	 * 
	 * @param text
	 * @param fontSize
	 * @return
	 * @throws DocumentException
	 */
	public void addCellText(final String text, final int fontSize) throws DocumentException {
		final JKPDFCell cell = new JKPDFCell(text, fontSize, 1);

		super.addCell(cell);
	}

}
