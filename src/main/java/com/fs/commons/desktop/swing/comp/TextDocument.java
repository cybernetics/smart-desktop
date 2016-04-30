package com.fs.commons.desktop.swing.comp;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TextDocument extends PlainDocument {
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
	public TextDocument(int maxLength) {
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
		if (getContent().length() <= max) {
			super.insertString(i, s, attributeset);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

}
