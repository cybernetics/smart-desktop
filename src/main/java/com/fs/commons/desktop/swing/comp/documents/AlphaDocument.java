package com.fs.commons.desktop.swing.comp.documents;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class AlphaDocument extends PlainDocument{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int max;

	/**
	 * 
	 * @param maxLength
	 *            int
	 */
	public AlphaDocument(int maxLength) {
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
		if (getContent().length() <= max && isAllApha(s)) {
			super.insertString(i, s, attributeset);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	private boolean isAllApha(String s) {
		for (int i = 0; i < s.length(); i++) {
			char charAt = s.charAt(i);
			if(!Character.isLetter(charAt) && charAt != '.'){
				return false;
			}
		}
		return true;
	}
}
