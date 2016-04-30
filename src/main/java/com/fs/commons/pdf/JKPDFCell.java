/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      16/4/2008     Jamil Shreet    -Modify the following methods : 
 * 1.2      28/05/2008     Jamil Shreet    -Modify the following methods : 
 */

package com.fs.commons.pdf;

import com.fs.commons.locale.Lables;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;

public class JKPDFCell extends PdfPCell{
	
	private static final int _DEFAULT_FIXED_HEIGHT = 17;

	/**
	 * 
	 * @param text
	 * @param colSpan
	 * @throws DocumentException
	 */
	public JKPDFCell(String text) throws DocumentException {
		this(text,1);
	}
	
	/**
	 * 
	 * @param text
	 * @param fontSize
	 * @param colSpan
	 * @throws DocumentException 
	 */
	public JKPDFCell(String text,int colSpan) throws DocumentException {
		this(text,PDFUtil.DEFAULT_SIZE,colSpan);
	}

	/**
	 * @1.1
	 * @param text
	 * @param fontSize
	 * @param colSpan
	 * @throws DocumentException 
	 */
	public JKPDFCell(String text,int fontSize,int colSpan) throws DocumentException {
		this(text,fontSize,colSpan,ALIGN_CENTER, false);
	}
	
	public JKPDFCell(String text,int fontSize,int colSpan,int fixedHeight) throws DocumentException {
		this(text,fontSize,colSpan,ALIGN_CENTER, false,fixedHeight);
	}

	/**
	 * @1.1
	 * @1.2
	 * @param text
	 * @param fontSize
	 * @param colSpan
	 * @throws DocumentException
	 */
	public JKPDFCell(String text,int fontSize,int colSpan,int hAlignment, boolean fontStyle) throws DocumentException {
		this(text,fontSize,colSpan,hAlignment,fontStyle,_DEFAULT_FIXED_HEIGHT);
	}
	
	public JKPDFCell(String text,int fontSize,int colSpan,int hAlignment, boolean fontStyle,boolean getLable) throws DocumentException {
		this(text,fontSize,colSpan,hAlignment,fontStyle,_DEFAULT_FIXED_HEIGHT,getLable);
	}
	

	public JKPDFCell(Object o, boolean getLabel) throws DocumentException {
		this(o.toString(),PDFUtil.DEFAULT_SIZE,1,ALIGN_CENTER,false,_DEFAULT_FIXED_HEIGHT,getLabel);
	}
	
	public JKPDFCell(Object o, int fontSize,boolean getLabel) throws DocumentException {
		this(o.toString(),fontSize,1,ALIGN_CENTER,false,_DEFAULT_FIXED_HEIGHT,getLabel);
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
	public JKPDFCell(String text,int fontSize,int colSpan,int hAlignment, boolean fontStyle,int fixedHeight) throws DocumentException {
		this(text, fontSize, colSpan, hAlignment, fontStyle, fixedHeight, true);
	}	

	public JKPDFCell(String text,int fontSize,int colSpan,int hAlignment, boolean fontStyle,int fixedHeight,boolean getLable) throws DocumentException {
		setColspan(colSpan);
		if(getLable){
			text=Lables.get(text);
		}
		setPhrase(new Phrase(text,PDFUtil.createFont(fontSize, fontStyle)));
		setHorizontalAlignment(hAlignment);
		setVerticalAlignment(ALIGN_MIDDLE);
		setFixedHeight(fixedHeight);
	}

}
