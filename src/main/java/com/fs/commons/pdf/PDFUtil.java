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

import java.io.File;
import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

public class PDFUtil {

	public static final int DEFAULT_SIZE = 10;
	public static final String DEFAULT_ARIAL = "arial";

	/**
	 * @1.1
	 * 
	 * @return
	 * @throws DocumentException
	 */
	public static Font createFont() throws DocumentException {
		return createFont(DEFAULT_SIZE, false);
	}

	/**
	 * @1.1
	 * 
	 * @param fontName
	 * @param size
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Font createFont(final int size, final boolean fontStyle) throws DocumentException {
		return createFont(DEFAULT_ARIAL, size, fontStyle);
	}

	/**
	 * @1.1
	 * 
	 * @param fontName
	 * @param size
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Font createFont(final String fontName, final int size, final boolean fontStyle) throws DocumentException {
		BaseFont baseFont;
		Font font = null;
		try {
			baseFont = BaseFont.createFont("c:/windows/fonts/" + fontName + ".ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			if (fontStyle == true) {
				font = new Font(baseFont, size, Font.BOLD);
			} else {
				font = new Font(baseFont, size, Font.NORMAL);
			}
			// return new Font(baseFont, size);
			return font;
		} catch (final IOException e) {
			throw new DocumentException(e);
		}
	}

	/**
	 *
	 * @param file
	 */
	public static void rotatePDFFile(final File file) {
		// TODO Auto-generated method stub

	}

}
