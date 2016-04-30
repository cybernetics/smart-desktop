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

import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

public class JKPDFDocument extends Document {

	private String headerText;
	private String footerText;
	private final OutputStream out;

	/**
	 * @1.1
	 */
	private PdfWriter writer;

	/**
	 *
	 * @param fileOutputStream
	 */
	public JKPDFDocument(final OutputStream out) {
		this(out, null, null);
	}

	/**
	 *
	 * @param out
	 * @throws DocumentException
	 * @throws IOException
	 */
	public JKPDFDocument(final OutputStream out, final String headerText, final String footerText) {
		this.headerText = headerText;
		this.footerText = footerText;
		this.out = out;
	}

	/**
	 * @return the footerText
	 */
	public String getFooterText() {
		return this.footerText;
	}

	/**
	 * @return the headerText
	 */
	public String getHeaderText() {
		return this.headerText;
	}

	/**
	 * @1.1
	 * 
	 * @return
	 */
	public PdfWriter getWriter() {
		return this.writer;
	}

	@Override
	public void open() {
		throw new IllegalStateException("Please call openDocument");
	}

	/**
	 *
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void openDocument() throws DocumentException, IOException {
		this.writer = PdfWriter.getInstance(this, this.out);

		if (this.headerText != null) {
			final HeaderFooter header = new HeaderFooter(new Phrase(this.headerText), false);
			setHeader(header);
		}
		if (this.footerText != null) {
			final HeaderFooter footer = new HeaderFooter(new Phrase(this.footerText), false);
			setFooter(footer);
		}
		super.open();
	}

	/**
	 * @param footerText
	 *            the footerText to set
	 */
	public void setFooterText(final String footerText) {
		this.footerText = footerText;
	}

	/**
	 * @param headerText
	 *            the headerText to set
	 */
	public void setHeaderText(final String headerText) {
		this.headerText = headerText;
	}

	/**
	 * @1.1
	 * 
	 * @param writer
	 */
	public void setWriter(final PdfWriter writer) {
		this.writer = writer;
	}

}
