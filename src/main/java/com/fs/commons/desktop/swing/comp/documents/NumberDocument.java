package com.fs.commons.desktop.swing.comp.documents;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.fs.commons.desktop.swin.ConversionUtil;

public class NumberDocument extends PlainDocument {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_MAX_LENGTH = 10;
	long max;

	/**
	 * 
	 * @param maxLength
	 *            int
	 */
	public NumberDocument() {
		this(Long.MAX_VALUE);
	}

	/**
	 * 
	 * @param maxLength
	 *            long
	 */
	public NumberDocument(long maxLength) {
		this.max = maxLength;
	}

	/**
	 * 
	 * @param i
	 *            int
	 * @param s
	 *            String
	 * @param attributeset
	 *            AttributeSet
	 * @throws BadLocationException
	 */
	public void insertString(int i, String s, AttributeSet attributeset) throws BadLocationException {
		if (isNumeric(s) && getContent().length() <= max) {
			super.insertString(i, ConversionUtil.toInteger(s).toString(), attributeset);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	/**
	 * 
	 * @param s
	 *            String
	 * @return boolean
	 */
	private boolean isNumeric(String s) {
		try {
			Double.valueOf(s);
		} catch (NumberFormatException numberformatexception) {
			return false;
		}
		return true;
	}
}
