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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfException;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class PDFMergeUtil {
	// ////////////////////////////////////////////////////////////////
	public static void concatPDFs(final List<byte[]> allPdfBytes, final OutputStream outputStream) throws PdfException {
		final ArrayList<InputStream> streams = new ArrayList<InputStream>();
		for (final byte[] pdfFile : allPdfBytes) {
			final ByteArrayInputStream pdfBytes = new ByteArrayInputStream(pdfFile);
			streams.add(pdfBytes);
		}
		concatPDFs(streams, outputStream, false);
	}

	// ////////////////////////////////////////////////////////////////
	public static void concatPDFs(final List<InputStream> pdfs, final OutputStream outputStream, final boolean paginate) throws PdfException {
		final Document document = new Document();
		try {
			final List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			final Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				final InputStream pdf = iteratorPDFs.next();
				final PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}

			// Create a writer for the outputstream
			final PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			// BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
			// BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			final PdfContentByte cb = writer.getDirectContent(); // Holds the
																	// PDF
			// data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			final Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				final PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					if (paginate) {
						cb.beginText();
						// cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}

			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (final Exception e) {
			throw new PdfException(e);
		} finally {
			if (document.isOpen()) {
				document.close();
			}
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (final IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	// /**
	// *
	// * @param args
	// */
	// public static void main(String[] args) {
	// try {
	// List<InputStream> pdfs = new ArrayList<InputStream>();
	// pdfs.add(new FileInputStream("c:\\pdf\\2.pdf"));
	// pdfs.add(new FileInputStream("c:\\pdf\\3.pdf"));
	// OutputStream output = new FileOutputStream("c:\\pdf\\merge.pdf");
	// PDFMergeUtil.concatPDFs(pdfs, output, true,false);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
}