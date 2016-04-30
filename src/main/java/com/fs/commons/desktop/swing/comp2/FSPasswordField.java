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

import com.fs.commons.desktop.swing.comp.JKPasswordField;

public class FSPasswordField extends JKPasswordField {
	/**
	 *
	 */
	private static final long serialVersionUID = 7195407906303569054L;
	private static final int DEFAULT_COLUMNS_SIZE = 20;
	private static final int DEFAULT_MAX_LENGTH = 20;

	public FSPasswordField() {
		this(DEFAULT_MAX_LENGTH, DEFAULT_COLUMNS_SIZE);
	}

	public FSPasswordField(final int maxlength, final int col) {
		super(maxlength, col);
		// TODO Auto-generated constructor stub
	}

}
