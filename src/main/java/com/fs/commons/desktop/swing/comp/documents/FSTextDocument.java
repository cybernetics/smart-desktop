package com.fs.commons.desktop.swing.comp.documents;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FSTextDocument extends PlainDocument {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int maxLength;
	
	public FSTextDocument() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param maxLength
	 *            int
	 */
	public FSTextDocument(int maxLength) {
		this.maxLength = maxLength;
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
		if (getContent().length() <= maxLength) {
			super.insertString(i, s, attributeset);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
	

}
