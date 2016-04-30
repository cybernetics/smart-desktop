package com.fs.commons.desktop.swing.comp.documents;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DoubleDocument extends PlainDocument {

	private final int maxLength;

	public DoubleDocument(int maxLength) {
		this.maxLength = maxLength;
	}

	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if (str.length() >= maxLength) {
			return;
		}
		if (str == null) {
			return;
		}
		String curVal = getText(0, getLength());
		boolean hasDot = curVal.indexOf('.') != -1;
		char buffer[] = str.toCharArray();
		char digit[] = new char[buffer.length];
		int j = 0;
		if (offs == 0 && buffer != null && buffer.length > 0 && buffer[0] == '-')
			digit[j++] = buffer[0];
		for (int i = 0; i < buffer.length; i++) {
			if (Character.isDigit(buffer[i]))
				digit[j++] = buffer[i];
			if (!hasDot && buffer[i] == '.') {
				digit[j++] = '.';
				hasDot = true;
			}
		}

		String added = new String(digit, 0, j);
		try {
			StringBuffer val = new StringBuffer(curVal);
			val.insert(offs, added);
			String valStr = val.toString();
			if (valStr.equals(".") || valStr.equals("-") || valStr.equals("-.")) {
				super.insertString(offs, added, a);
			} else {
				Double.valueOf(valStr);
				super.insertString(offs, added, a);
			}
		} catch (NumberFormatException e) {
		}
	}

	public void setValue(double d) {
		try {
			remove(0, getLength());
			insertString(0, String.valueOf(d), null);
		} catch (BadLocationException e) {
		}
	}

	public double getValue() {
		try {
			String t = getText(0, getLength());
			if (t != null && t.length() > 0)
				return Double.parseDouble(t);
			else
				return 0.0D;
		} catch (BadLocationException e) {
			throw new Error(e.getMessage());
		}
	}
}