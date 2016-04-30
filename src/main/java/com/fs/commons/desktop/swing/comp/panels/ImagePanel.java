package com.fs.commons.desktop.swing.comp.panels;

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
import com.fs.commons.util.ExceptionUtil;

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

	public ImagePanel(BufferedImage image) {
		this(image, TILED);
	}

	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		setImage( new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_RGB));
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
		invalidate();
		repaint();
	}

	/**
	 * @return the style
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(int style) {
		this.style = style;
	}

	public ImagePanel(BufferedImage image, int style) {
		this.image = image;
		this.style = style;
		// setLayout(new BorderLayout());
		setBackground(Colors.MAIN_PANEL_BG);
	}

	public void setImageAlignmentX(float alignmentX) {
		this.alignmentX = alignmentX > 1.0f ? 1.0f : alignmentX < 0.0f ? 0.0f : alignmentX;
	}

	public void setImageAlignmentY(float alignmentY) {

	}

	public void add(JComponent component) {
		add(component, null);
	}

	public void add(JComponent component, Object constraints) {
		component.setOpaque(false);

		if (component instanceof JScrollPane) {
			JScrollPane scrollPane = (JScrollPane) component;
			JViewport viewport = scrollPane.getViewport();
			viewport.setOpaque(false);
			Component c = viewport.getView();

			if (c instanceof JComponent) {
				((JComponent) c).setOpaque(false);
			}
		}

		super.add(component, constraints);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image == null) {
			return;
		}

		switch (style) {
		case TILED:
			drawTiled(g);
			break;

		case SCALED:
			Dimension d = getSize();
			g.drawImage(image, 0, 0, d.width, d.height, null);
			break;

		case ACTUAL:
			drawActual(g);
			break;
		}
	}

	private void drawTiled(Graphics g) {
		Dimension d = getSize();
		int width = image.getWidth(null);
		int height = image.getHeight(null);

		for (int x = 0; x < d.width; x += width) {
			for (int y = 0; y < d.height; y += height) {
				g.drawImage(image, x, y, null, null);
			}
		}
	}

	private void drawActual(Graphics g) {
		Dimension d = getSize();
		float x = (d.width - image.getWidth()) * alignmentX;
		float y = 0;// (d.height - image.getHeight()) * alignmentY;
		g.drawImage(image, (int) x, (int) y, this);
	}

	public void setImage(byte[] image) {
		try {
			this.image = javax.imageio.ImageIO.read(new ByteArrayInputStream(image));
			invalidate();
			repaint();
		} catch (IOException e) {
			ExceptionUtil.handleException(e);
		}
	}

	public void removeImage() {
		image = null;
		invalidate();
		repaint();
	}

	public void setSizeToFitImage() {
		setPreferredSize(image.getWidth(), image.getHeight());
	}
}
