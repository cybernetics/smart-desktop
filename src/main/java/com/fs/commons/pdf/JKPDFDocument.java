/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      18/05/2008     Jamil Shreet    -Modify the following filed : 
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
	private OutputStream out;

	/**
	 * @1.1
	 */
	private PdfWriter writer;

	/**
	 * 
	 * @param fileOutputStream
	 */
	public JKPDFDocument(OutputStream out) {
		this(out, null, null);
	}

	/**
	 * 
	 * @param out
	 * @throws DocumentException
	 * @throws IOException
	 */
	public JKPDFDocument(OutputStream out, String headerText, String footerText) {
		this.headerText = headerText;
		this.footerText = footerText;
		this.out = out;
	}

	/**
	 * 
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void openDocument() throws DocumentException, IOException {
		writer = PdfWriter.getInstance(this, out);

		if (headerText != null) {
			HeaderFooter header = new HeaderFooter(new Phrase(headerText),
					false);
			setHeader(header);
		}
		if (footerText != null) {
			HeaderFooter footer = new HeaderFooter(new Phrase(footerText),
					false);
			setFooter(footer);
		}
		super.open();
	}

	@Override
	public void open() {
		throw new IllegalStateException("Please call openDocument");
	}

	/**
	 * @return the headerText
	 */
	public String getHeaderText() {
		return headerText;
	}

	/**
	 * @param headerText
	 *            the headerText to set
	 */
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	/**
	 * @return the footerText
	 */
	public String getFooterText() {
		return footerText;
	}

	/**
	 * @param footerText
	 *            the footerText to set
	 */
	public void setFooterText(String footerText) {
		this.footerText = footerText;
	}

	/**
	 * @1.1
	 * @return
	 */
	public PdfWriter getWriter() {
		return writer;
	}

	/**
	 * @1.1
	 * @param writer
	 */
	public void setWriter(PdfWriter writer) {
		this.writer = writer;
	}

}
