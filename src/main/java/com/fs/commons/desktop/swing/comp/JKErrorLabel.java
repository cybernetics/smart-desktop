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
package com.fs.commons.desktop.swing.comp;

import java.awt.Color;

import javax.swing.Icon;

public class JKErrorLabel extends JKLabel {

	/**
	 *
	 */
	private static final long serialVersionUID = 5055539903428716688L;

	public JKErrorLabel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JKErrorLabel(final Icon image) {
		super(image);
		// TODO Auto-generated constructor stub
	}

	public JKErrorLabel(final Icon image, final int horizontalAlignment) {
		super(image, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	public JKErrorLabel(final String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public JKErrorLabel(final String text, final Icon icon, final int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	public JKErrorLabel(final String text, final int horizontalAlignment) {
		super(text, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		super.init();
		setOpaque(false);
		setForeground(Color.red);
	}

}
