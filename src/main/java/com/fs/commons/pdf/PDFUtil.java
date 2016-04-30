/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      16/4/2008     Jamil Shreet    -Modify the following methods : 
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
	 * @return
	 * @throws DocumentException 
	 */
	public static Font createFont() throws DocumentException {
		return createFont(DEFAULT_SIZE, false);
	}
	
	/**
	 * @1.1
	 * @param fontName
	 * @param size
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */public static Font createFont(int size, boolean fontStyle) throws DocumentException{
		return createFont(DEFAULT_ARIAL,size, fontStyle);
	}
	
	/**
	 * @1.1
	 * @param fontName
	 * @param size
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Font createFont(String fontName,int size, boolean fontStyle) throws DocumentException{
		BaseFont baseFont;
		Font font =null;
		try {
			baseFont = BaseFont.createFont("c:/windows/fonts/"+fontName+".ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			if(fontStyle == true){
				font = new Font(baseFont, size, Font.BOLD);
			} else {
				font = new Font(baseFont, size, Font.NORMAL);
			}
			//return new Font(baseFont, size);
			return font;
		} catch (IOException e) {
			throw new DocumentException(e);
		}
	}

	/**
	 * 
	 * @param file
	 */
	public static void rotatePDFFile(File file) {
		// TODO Auto-generated method stub
		
	}
	
}
