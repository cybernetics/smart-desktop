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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.jk.exceptions.handler.ExceptionUtil;

public class JKDesktopPane extends JDesktopPane {

	/**
	 *
	 */
	private static final long serialVersionUID = -6686336167205700395L;
	private Image backGroundImage;

	public JKDesktopPane() {
		super();
		init();
	}

	@Override
	public Component add(final Component comp) {
		final Component added = super.add(comp);
		if (comp instanceof JInternalFrame) {
			setPosition((JInternalFrame) comp);
		}
		return added;
	}

	private void init() {
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		setBackground(Colors.MAIN_PANEL_BG);
		setOpaque(true);
	}

	@Override
	public void paintComponent(final Graphics g) {
		if (this.backGroundImage == null) {
			super.paintComponent(g);
		} else {
			final Graphics2D g2d = (Graphics2D) g;

			// scale the image to fit the size of the Panel
			final double mw = this.backGroundImage.getWidth(null);
			final double mh = this.backGroundImage.getHeight(null);

			final double sw = getWidth() / mw;
			final double sh = getHeight() / mh;

			g2d.scale(sw, sh);
			g2d.drawImage(this.backGroundImage, 0, 0, this);
		}
	}

	public void setBackGroundImage(final String path) {
		try {
			this.backGroundImage = SwingUtility.getImage(path);
		} catch (final Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private void setPosition(final JInternalFrame frame) {
		final Point location = frame.getLocation();
		if (location.getX() == 0 && location.getY() == 0) {
			final int length = getAllFrames().length;
			final int y = length * 20;
			int x;
			if (SwingUtility.isLeftOrientation()) {
				x = length * 20;
			} else {
				final int width = frame.getContentPane().getPreferredSize().width;
				x = (int) (getSize().getWidth() - length * 20) - width;
			}
			System.out.println("X=" + x);
			System.out.println("Y=" + y);
			System.out.println(frame.getContentPane().getPreferredSize());
			frame.setLocation(x, y);
		}
	}

}
