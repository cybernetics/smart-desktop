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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.fs.commons.desktop.swing.Colors;
import com.jk.exceptions.handler.JKExceptionUtil;

public class ImagePanel extends JKPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final int TILED = 0;

	public static final int SCALED = 1;

	public static final int ACTUAL = 2;

	private BufferedImage image;

	private int style;

	private float alignmentX = 0.5f;

	public ImagePanel() {
	}

	public ImagePanel(final BufferedImage image) {
		this(image, TILED);
	}

	public ImagePanel(final BufferedImage image, final int style) {
		this.image = image;
		this.style = style;
		setLayout(new BorderLayout());
		setBackground(Colors.MAIN_PANEL_BG);
	}

	public void add(final JComponent component) {
		add(component, null);
	}

	public void add(final JComponent component, final Object constraints) {
		component.setOpaque(false);

		if (component instanceof JScrollPane) {
			final JScrollPane scrollPane = (JScrollPane) component;
			final JViewport viewport = scrollPane.getViewport();
			viewport.setOpaque(false);
			final Component c = viewport.getView();

			if (c instanceof JComponent) {
				((JComponent) c).setOpaque(false);
			}
		}

		super.add(component, constraints);
	}

	private void drawActual(final Graphics g) {
		final Dimension d = getSize();
		final float x = (d.width - this.image.getWidth()) * this.alignmentX;
		final float y = 0;// (d.height - image.getHeight()) * alignmentY;
		g.drawImage(this.image, (int) x, (int) y, this);
	}

	private void drawTiled(final Graphics g) {
		final Dimension d = getSize();
		final int width = this.image.getWidth(null);
		final int height = this.image.getHeight(null);

		for (int x = 0; x < d.width; x += width) {
			for (int y = 0; y < d.height; y += height) {
				g.drawImage(this.image, x, y, null, null);
			}
		}
	}

	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return this.image;
	}

	/**
	 * @return the style
	 */
	public int getStyle() {
		return this.style;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		if (this.image == null) {
			return;
		}

		switch (this.style) {
		case TILED:
			drawTiled(g);
			break;

		case SCALED:
			final Dimension d = getSize();
			g.drawImage(this.image, 0, 0, d.width, d.height, null);
			break;

		case ACTUAL:
			drawActual(g);
			break;
		}
	}

	public void removeImage() {
		this.image = null;
		invalidate();
		repaint();
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(final BufferedImage image) {
		this.image = image;
		invalidate();
		repaint();
	}

	public void setImage(final byte[] image) {
		try {
			this.image = javax.imageio.ImageIO.read(new ByteArrayInputStream(image));
			invalidate();
			repaint();
		} catch (final IOException e) {
			JKExceptionUtil.handle(e);
		}
	}

	public void setImage(final ImageIcon image) {
		setImage(new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_RGB));
	}

	public void setImageAlignmentX(final float alignmentX) {
		this.alignmentX = alignmentX > 1.0f ? 1.0f : alignmentX < 0.0f ? 0.0f : alignmentX;
	}

	public void setImageAlignmentY(final float alignmentY) {

	}

	public void setSizeToFitImage() {
		setPreferredSize(this.image.getWidth(), this.image.getHeight());
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(final int style) {
		this.style = style;
	}
}
