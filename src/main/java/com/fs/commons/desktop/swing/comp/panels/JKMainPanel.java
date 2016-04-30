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
package com.fs.commons.desktop.swing.comp.panels;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JComponent;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.desktop.graphics.GraphicsFactory.GradientType;

public class JKMainPanel extends JKPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -8131024627205038198L;
	private DataSource manager;

	public JKMainPanel() {
		super();
		initMe();
	}

	public JKMainPanel(final JComponent component) {
		super(component);
		initMe();
	}

	public JKMainPanel(final LayoutManager layout) {
		super(layout);
		initMe();
	}

	private void initMe() {
		setOpaque(true);
		// setGradientType(GradientType.VERTICAL);
	}

	@Override
	public void paint(final Graphics g) {
		if (getGradientType() == null) {
			if (getWidth() < getHeight()) {
				setGradientType(GradientType.HORIZENTAL_LINEAR);
			} else {
				setGradientType(GradientType.VERTICAL_LINEAR);
			}
		}
		super.paint(g);

	}

	// @Override
	// public void paint(Graphics g) {
	//
	// super.paint(g);
	// }

}
