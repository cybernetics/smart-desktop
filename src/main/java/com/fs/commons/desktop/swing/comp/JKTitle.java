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
import java.awt.Graphics;

import javax.swing.border.EmptyBorder;

import com.fs.commons.desktop.graphics.GraphicsFactory;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.locale.Lables;

public class JKTitle extends JKLabel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// public JKTitle(String label) {
	// this(label,true);
	// }

	public JKTitle() {
		this("");
	}

	public JKTitle(final String label) {
		super(label);
		// setOpaque(opaque);
		setText(Lables.get(label, true));
		setBorder(new EmptyBorder(2, 8, 2, 8));
		setAlignmentX(HORIZONTAL);
		setFont(SwingUtility.getDefaultTitleFont());
		setOpaque(true);
		setPreferredSize(new Dimension(150, 30));
	}

	@Override
	public void paint(final Graphics g) {
		GraphicsFactory.makeGradient(this, g, getBackground());
		super.paint(g);
	}
}
