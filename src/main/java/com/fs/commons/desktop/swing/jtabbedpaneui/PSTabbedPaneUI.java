package com.fs.commons.desktop.swing.jtabbedpaneui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 * An implementation of the TabbedPaneUI that looks like the tabs that are used
 * the Photoshop palette windows.
 * <p/>
 * Copyright (C) 2005 by Jon Lipsky
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. Y ou may obtain a copy
 * of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software d
 * istributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
public class PSTabbedPaneUI extends BasicTabbedPaneUI {
	private static final int HEIGHT = 30;

	private static final Insets NO_INSETS = new Insets(0, 0, 0, 0);

	/**
	 * The font to use for the selected tab
	 */
	private Font boldFont;

	/**
	 * The font metrics for the selected font
	 */
	private FontMetrics boldFontMetrics;

	/**
	 * The color to use to fill in the background
	 */
	private Color fillColor;

	// ------------------------------------------------------------------------------------------------------------------
	// Custom installation methods
	// ------------------------------------------------------------------------------------------------------------------

	public static ComponentUI createUI(JComponent c) {
		return new PSTabbedPaneUI();
	}

	protected void installDefaults() {
		super.installDefaults();
		tabAreaInsets.left = 4;
		selectedTabPadInsets = new Insets(0, 0, 0, 0);
		tabInsets = selectedTabPadInsets;

		Color background = tabPane.getBackground();
		fillColor = background.darker();

		boldFont = tabPane.getFont().deriveFont(Font.BOLD);
		boldFontMetrics = tabPane.getFontMetrics(boldFont);
	}

	// ------------------------------------------------------------------------------------------------------------------
	// Custom sizing methods
	// ------------------------------------------------------------------------------------------------------------------

	public int getTabRunCount(JTabbedPane pane) {
		return 1;
	}

	protected Insets getContentBorderInsets(int tabPlacement) {
		return NO_INSETS;
	}

	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
//		int vHeight = fontHeight;
//		if (vHeight % 2 > 0) {
//			vHeight += 1;
//		}
		return HEIGHT;
	}

	protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
		return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + metrics.getHeight();
	}

	// ------------------------------------------------------------------------------------------------------------------
	// Custom painting methods
	// ------------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------------------------------------------
	// Methods that we want to suppress the behaviour of the superclass
	// ------------------------------------------------------------------------------------------------------------------

	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		Graphics2D g2d = (Graphics2D) g;
		Polygon shape = new Polygon();
		new GradientPaint(x, y, new Color(165, 200, 219), x + w, y, new Color(166, 184, 219));

		GradientPaint gradienteNormal = new GradientPaint(x, y, new Color(237, 237, 237), x + w / 2, y, new Color(154, 169, 188));

		shape.addPoint(x, y + h);
		shape.addPoint(x, y);
		shape.addPoint(x + w - (h / 2), y);

		if (isSelected || (tabIndex == (rects.length - 1))) {
			// g2d.setPaint(gradienteNormal);
			shape.addPoint(x + w + (h / 2), y + h);
		} else {
			// g2d.setPaint(gradienteNormal);
			shape.addPoint(x + w, y + (h / 2));
			shape.addPoint(x + w, y + h);
		}
		g2d.setPaint(gradienteNormal);
		g2d.setColor(tabPane.getBackground());

		g2d.fillPolygon(shape);
	}

	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		g.setColor(Color.BLACK);
		g.drawLine(x, y, x, y + h);
		g.drawLine(x, y, x + w - (h / 2), y);
		g.drawLine(x + w - (h / 2), y, x + w + (h / 2), y + h);

		if (isSelected) {
			g.setColor(Color.WHITE);
			g.drawLine(x + 1, y + 1, x + 1, y + h);
			g.drawLine(x + 1, y + 1, x + w - (h / 2), y + 1);

			g.setColor(shadow);
			g.drawLine(x + w - (h / 2), y + 1, x + w + (h / 2) - 1, y + h);
		}
	}

	protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
		Rectangle selectedRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);

		selectedRect.width = selectedRect.width + (selectedRect.height / 2) - 1;

		g.setColor(Color.BLACK);

		g.drawLine(x, y, selectedRect.x, y);
		g.drawLine(selectedRect.x + selectedRect.width + 1, y, x + w, y);

		g.setColor(Color.WHITE);

		g.drawLine(x, y + 1, selectedRect.x, y + 1);
		g.drawLine(selectedRect.x + 1, y + 1, selectedRect.x + 1, y);
		g.drawLine(selectedRect.x + selectedRect.width + 2, y + 1, x + w, y + 1);

		g.setColor(shadow);
		g.drawLine(selectedRect.x + selectedRect.width, y, selectedRect.x + selectedRect.width + 1, y + 1);
	}

	protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {

	}

	protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
		// Do nothing
	}

	protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {

	}

	protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect,
			boolean isSelected) {
		// Do nothing
	}

	protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
		int tw = tabPane.getBounds().width;
		g.setColor(new Color(158, 172, 191));
		g.fillRoundRect(0, 0, tw - 1, tabPane.getBounds().width + 6, 3, 3);
		// g.setColor(new Color(58,72,85));
		// g.drawRoundRect(0, 0, tw-1, tabPane.getBounds().width + 3,10,4);

		super.paintTabArea(g, tabPlacement, selectedIndex);
	}

	protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect,
			boolean isSelected) {
		if (isSelected) {
			int vDifference = (int) (boldFontMetrics.getStringBounds(title, g).getWidth()) - textRect.width;
			textRect.x -= (vDifference / 2);
			super.paintText(g, tabPlacement, boldFont, boldFontMetrics, tabIndex, title, textRect, isSelected);
		} else {
			super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
		}
	}

	protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
		return 0;
	}
}
