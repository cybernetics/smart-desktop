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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.fs.commons.locale.Lables;

public class JKSmallButton extends JKButton {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Font font = new Font("Arial", Font.BOLD, 10);

	/**
	 *
	 * @param caption
	 *            String
	 */
	public JKSmallButton(final String caption) {
		super(Lables.get(caption));
		init();
		setFocusable(false);
	}

	/**
	 *
	 */
	@Override
	void init() {
		setFont(this.font);
		setPreferredSize(new Dimension(70, 17));
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(final KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					// transferFocus();
					doClick();
				}
			}
		});
	}

}
