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
	public FSTextDocument(final int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMaxLength() {
		return this.maxLength;
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
	@Override
	public void insertString(final int i, final String s, final AttributeSet attributeset) throws BadLocationException {
		if (getContent().length() <= this.maxLength) {
			super.insertString(i, s, attributeset);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	public void setMaxLength(final int maxLength) {
		this.maxLength = maxLength;
	}

}
