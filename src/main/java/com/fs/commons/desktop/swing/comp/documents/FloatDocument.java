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
	private final int maxLen;

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
	public FloatDocument(final int maxLen) {
		this.maxLen = maxLen;

	}

	/**
	 *
	 */
	@Override
	public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
		if (getContent().length() <= this.maxLen) {
			String tmpText = getText(0, offs) + str;
			if (offs < getLength()) {
				tmpText += getText(offs, getLength());
			}
			tmpText = tmpText.trim();
			try {
				Float.parseFloat(tmpText);
				// parsing for the validation
				super.insertString(offs, str.trim(), a);
			} catch (final NumberFormatException e) {
				// if the user input the - sign
				if (offs == 0 && tmpText.equals("-")) {
					super.insertString(offs, str.trim(), a);
				}
			} catch (final Throwable t) {
				if (str != null && str.length() != 0) {
					Toolkit.getDefaultToolkit().beep();
				}
			}
		}
	}
}
