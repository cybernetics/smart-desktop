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

import javax.swing.JComponent;

import com.fs.commons.desktop.swing.SwingUtility;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 *
 * <p>
 * Company:
 * </p>
 * d
 *
 * @author not attributable
 * @version 1.0
 */
public class JKSeparator extends JComponent {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param width
	 *            int
	 */
	public JKSeparator() {
		setBackground(SwingUtility.getDefaultBackgroundColor());
		setBorder(null);
		setPreferredSize(new Dimension(200, 2));
	}

	/**
	 *
	 * @param g
	 *            Graphics
	 */
	@Override
	public void paint(final Graphics g) {

		super.paint(g);

	}
}
