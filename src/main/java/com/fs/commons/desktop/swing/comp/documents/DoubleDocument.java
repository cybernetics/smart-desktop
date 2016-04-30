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
package com.fs.commons.desktop.swing.comp.documents;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DoubleDocument extends PlainDocument {

	/**
	 *
	 */
	private static final long serialVersionUID = -1475402892857074379L;
	private final int maxLength;

	public DoubleDocument(final int maxLength) {
		this.maxLength = maxLength;
	}

	public double getValue() {
		try {
			final String t = getText(0, getLength());
			if (t != null && t.length() > 0) {
				return Double.parseDouble(t);
			} else {
				return 0.0D;
			}
		} catch (final BadLocationException e) {
			throw new Error(e.getMessage());
		}
	}

	@Override
	public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
		if (str.length() >= this.maxLength) {
			return;
		}
		if (str == null) {
			return;
		}
		final String curVal = getText(0, getLength());
		boolean hasDot = curVal.indexOf('.') != -1;
		final char buffer[] = str.toCharArray();
		final char digit[] = new char[buffer.length];
		int j = 0;
		if (offs == 0 && buffer != null && buffer.length > 0 && buffer[0] == '-') {
			digit[j++] = buffer[0];
		}
		for (final char element : buffer) {
			if (Character.isDigit(element)) {
				digit[j++] = element;
			}
			if (!hasDot && element == '.') {
				digit[j++] = '.';
				hasDot = true;
			}
		}

		final String added = new String(digit, 0, j);
		try {
			final StringBuffer val = new StringBuffer(curVal);
			val.insert(offs, added);
			final String valStr = val.toString();
			if (valStr.equals(".") || valStr.equals("-") || valStr.equals("-.")) {
				super.insertString(offs, added, a);
			} else {
				Double.valueOf(valStr);
				super.insertString(offs, added, a);
			}
		} catch (final NumberFormatException e) {
		}
	}

	public void setValue(final double d) {
		try {
			remove(0, getLength());
			insertString(0, String.valueOf(d), null);
		} catch (final BadLocationException e) {
		}
	}
}