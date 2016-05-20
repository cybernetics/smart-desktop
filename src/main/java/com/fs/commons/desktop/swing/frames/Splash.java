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
package com.fs.commons.desktop.swing.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Window;

import javax.swing.JPanel;

import com.fs.commons.desktop.swing.AnimationUtil;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.ImagePanel;
import com.fs.commons.util.GeneralUtility;
import com.sun.awt.AWTUtilities;

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
 *
 * @author not attributable
 * @version 1.0
 */
public class Splash extends Window {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final String imageName;
	private String header;

	/**
	 * p
	 *
	 * @param imageName
	 */
	public Splash(final String imageName) {
		super(new Frame());
		this.imageName = imageName;
		init();
	}

	@Override
	public void dispose() {
		// if (getWidth() <= 1) {
		super.dispose();
		// }else{
		// AnimationUtil.fadeOut(this, true);
		// }
	}

	/**
	 * init
	 */
	private void init() {
		setSize(new Dimension(640, 400));
		AWTUtilities.setWindowOpaque(this, false);
		final JPanel pnl = SwingUtility.buildImagePanel(GeneralUtility.getURL(this.imageName), ImagePanel.SCALED);
		setLocationRelativeTo(null);
		add(pnl, BorderLayout.CENTER);
	}

	@Override
	public void setVisible(final boolean show) {
		if (show) {
			AnimationUtil.fadeIn(this);
			super.setVisible(true);
		} else {
			// if (getWidth() <= 1) {
			super.setVisible(false);
			// } else {
			// AnimationUtil.fadeOut(this, false);
			// }

		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (header != null) {
//			setForeground(Colors.SPLASH_HEADER_COLOR);
//			// text
//			setFont(getFont().deriveFont(40f));
//			FontMetrics metrics = g.getFontMetrics(getFont());
//			int width = metrics.stringWidth(header);
//			g.drawString(header, (getWidth() / 2) - (width - 2), getHeight() / 2);
//			// shaddow
//			setForeground(Color.black);
//			g.drawString(header, (getWidth() / 2) - (width - 2) + 10, getHeight() / 2 + 10);
//
		}
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

}
