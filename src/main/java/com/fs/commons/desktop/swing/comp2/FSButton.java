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

import javax.swing.ImageIcon;

import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.security.Privilige;

public class FSButton extends JKButton {

	/**
	 *
	 */
	private static final long serialVersionUID = 1447422768114852572L;

	public FSButton() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FSButton(final ImageIcon imageIcon) {
		super(imageIcon);
		// TODO Auto-generated constructor stub
	}

	public FSButton(final String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}

	public FSButton(final String caption, final boolean leadingAligned) {
		super(caption, leadingAligned);
		// TODO Auto-generated constructor stub
	}

	public FSButton(final String caption, final boolean leadingAligned, final String shortcut) {
		super(caption, leadingAligned, shortcut);
		// TODO Auto-generated constructor stub
	}

	public FSButton(final String caption, final String shortcut) {
		super(caption, shortcut);
		// TODO Auto-generated constructor stub
	}

	public FSButton(final String caption, final String shortcut, final boolean progress) {
		super(caption, shortcut, progress);
		// TODO Auto-generated constructor stub
	}

	public FSButton(final String caption, final String shortcut, final Privilige privlige) {
		super(caption, shortcut, privlige);
		// TODO Auto-generated constructor stub
	}

}
