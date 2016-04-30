package com.fs.commons.desktop.swing.comp.documents;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Document that only allows floating point values for a text field
 * 
 * @author Jeff Tassin
 */
public class FloatDocument extends PlainDocument {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int maxLen;

	/**
	 * 
	 */
	public FloatDocument() {
		this(10);
	}

	/**
	 * 
	 * @param maxLen
	 */
	public FloatDocument(int maxLen) {
		this.maxLen = maxLen;

	}

	/**
	 * 
	 */
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if (getContent().length() <= maxLen) {
			String tmpText = getText(0, offs) + str;
			if (offs < getLength()) {
				tmpText += getText(offs, getLength());
			}
			tmpText = tmpText.trim();
			try {
				Float.parseFloat(tmpText);
				// parsing for the validation
				super.insertString(offs, str.trim(), a);
			} catch (NumberFormatException e) {
				// if the user input the - sign
				if (offs == 0 && tmpText.equals("-")) {
					super.insertString(offs, str.trim(), a);
				}
			} catch (Throwable t) {
				if (str != null && str.length() != 0)
					Toolkit.getDefaultToolkit().beep();
			}
		}
	}
}
