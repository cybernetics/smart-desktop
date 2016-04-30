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

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class JKStatusBar extends JKPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Dimension PREFERRED_SIZE = new Dimension(800, 25);
	JKLabel lblMessage = new JKLabel("", false);

	public JKStatusBar() {
		init();
	}

	public String getText() {
		return this.lblMessage.getText();
	}

	private void init() {
		this.lblMessage.setCaptilize(true);
		setLayout(new BorderLayout());
		add(this.lblMessage);
		setFocusable(false);
		setBorder(BorderFactory.createLoweredBevelBorder());
	}

	/**
	 *
	 * @param text
	 * @return
	 */
	public void setText(final String text) {
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				JKStatusBar.this.lblMessage.setText(text);
			}
		};
		SwingUtilities.invokeLater(runnable);
	}
}
