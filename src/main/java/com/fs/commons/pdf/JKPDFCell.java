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

import com.fs.commons.locale.Lables;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;

public class JKPDFCell extends PdfPCell {

	private static final int _DEFAULT_FIXED_HEIGHT = 17;

	public JKPDFCell(final Object o, final boolean getLabel) throws DocumentException {
		this(o.toString(), PDFUtil.DEFAULT_SIZE, 1, ALIGN_CENTER, false, _DEFAULT_FIXED_HEIGHT, getLabel);
	}

	public JKPDFCell(final Object o, final int fontSize, final boolean getLabel) throws DocumentException {
		this(o.toString(), fontSize, 1, ALIGN_CENTER, false, _DEFAULT_FIXED_HEIGHT, getLabel);
	}

	/**
	 *
	 * @param text
	 * @param colSpan
	 * @throws DocumentException
	 */
	public JKPDFCell(final String text) throws DocumentException {
		this(text, 1);
	}

	/**
	 *
	 * @param text
	 * @param fontSize
	 * @param colSpan
	 * @throws DocumentException
	 */
	public JKPDFCell(final String text, final int colSpan) throws DocumentException {
		this(text, PDFUtil.DEFAULT_SIZE, colSpan);
	}

	/**
	 * @1.1
	 * 
	 * @param text
	 * @param fontSize
	 * @param colSpan
	 * @throws DocumentException
	 */
	public JKPDFCell(final String text, final int fontSize, final int colSpan) throws DocumentException {
		this(text, fontSize, colSpan, ALIGN_CENTER, false);
	}

	public JKPDFCell(final String text, final int fontSize, final int colSpan, final int fixedHeight) throws DocumentException {
		this(text, fontSize, colSpan, ALIGN_CENTER, false, fixedHeight);
	}

	/**
	 * @1.1 @1.2
	 * 
	 * @param text
	 * @param fontSize
	 * @param colSpan
	 * @throws DocumentException
	 */
	public JKPDFCell(final String text, final int fontSize, final int colSpan, final int hAlignment, final boolean fontStyle)
			throws DocumentException {
		this(text, fontSize, colSpan, hAlignment, fontStyle, _DEFAULT_FIXED_HEIGHT);
	}

	public JKPDFCell(final String text, final int fontSize, final int colSpan, final int hAlignment, final boolean fontStyle, final boolean getLable)
			throws DocumentException {
		this(text, fontSize, colSpan, hAlignment, fontStyle, _DEFAULT_FIXED_HEIGHT, getLable);
	}

	/**
	 *
	 * @param text
	 * @param fontSize
	 * @param colSpan
	 * @param hAlignment
	 * @param fontStyle
	 * @param fixedHeight
	 * @throws DocumentException
	 */
	public JKPDFCell(final String text, final int fontSize, final int colSpan, final int hAlignment, final boolean fontStyle, final int fixedHeight)
			throws DocumentException {
		this(text, fontSize, colSpan, hAlignment, fontStyle, fixedHeight, true);
	}

	public JKPDFCell(String text, final int fontSize, final int colSpan, final int hAlignment, final boolean fontStyle, final int fixedHeight,
			final boolean getLable) throws DocumentException {
		setColspan(colSpan);
		if (getLable) {
			text = Lables.get(text);
		}
		setPhrase(new Phrase(text, PDFUtil.createFont(fontSize, fontStyle)));
		setHorizontalAlignment(hAlignment);
		setVerticalAlignment(ALIGN_MIDDLE);
		setFixedHeight(fixedHeight);
	}

}
