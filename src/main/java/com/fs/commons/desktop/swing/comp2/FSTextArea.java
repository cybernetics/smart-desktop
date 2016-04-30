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
package com.fs.commons.desktop.swing.comp2;

import javax.swing.text.Document;

import com.fs.commons.desktop.swing.comp.JKTextArea;

public class FSTextArea extends JKTextArea {

	/**
	 *
	 */
	private static final long serialVersionUID = 6570195595068213128L;

	public FSTextArea() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FSTextArea(final Document doc) {
		super(doc);
		// TODO Auto-generated constructor stub
	}

	public FSTextArea(final Document doc, final String text, final int rows, final int columns) {
		super(doc, text, rows, columns);
		// TODO Auto-generated constructor stub
	}

	public FSTextArea(final int rows, final int columns) {
		super(rows, columns);
		// TODO Auto-generated constructor stub
	}

	public FSTextArea(final String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public FSTextArea(final String text, final int rows, final int columns) {
		super(text, rows, columns);
		// TODO Auto-generated constructor stub
	}

}
