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

	public static ComponentUI createUI(final JComponent c) {
		return new PSTabbedPaneUI();
	}

	/**
	 * The font to use for the selected tab
	 */
	private Font boldFont;

	/**
	 * The font metrics for the selected font
	 */
	private FontMetrics boldFontMetrics;

	// ------------------------------------------------------------------------------------------------------------------
	// Custom installation methods
	// ------------------------------------------------------------------------------------------------------------------

	/**
	 * The color to use to fill in the background
	 */
	private Color fillColor;

	@Override
	protected int calculateTabHeight(final int tabPlacement, final int tabIndex, final int fontHeight) {
		// int vHeight = fontHeight;
		// if (vHeight % 2 > 0) {
		// vHeight += 1;
		// }
		return HEIGHT;
	}

	// ------------------------------------------------------------------------------------------------------------------
	// Custom sizing methods
	// ------------------------------------------------------------------------------------------------------------------

	@Override
	protected int calculateTabWidth(final int tabPlacement, final int tabIndex, final FontMetrics metrics) {
		return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + metrics.getHeight();
	}

	@Override
	protected Insets getContentBorderInsets(final int tabPlacement) {
		return NO_INSETS;
	}

	@Override
	protected int getTabLabelShiftY(final int tabPlacement, final int tabIndex, final boolean isSelected) {
		return 0;
	}

	@Override
	public int getTabRunCount(final JTabbedPane pane) {
		return 1;
	}

	// ------------------------------------------------------------------------------------------------------------------
	// Custom painting methods
	// ------------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------------------------------------------
	// Methods that we want to suppress the behaviour of the superclass
	// ------------------------------------------------------------------------------------------------------------------

	@Override
	protected void installDefaults() {
		super.installDefaults();
		this.tabAreaInsets.left = 4;
		this.selectedTabPadInsets = new Insets(0, 0, 0, 0);
		this.tabInsets = this.selectedTabPadInsets;

		final Color background = this.tabPane.getBackground();
		this.fillColor = background.darker();

		this.boldFont = this.tabPane.getFont().deriveFont(Font.BOLD);
		this.boldFontMetrics = this.tabPane.getFontMetrics(this.boldFont);
	}

	@Override
	protected void paintContentBorderBottomEdge(final Graphics g, final int tabPlacement, final int selectedIndex, final int x, final int y,
			final int w, final int h) {

	}

	@Override
	protected void paintContentBorderLeftEdge(final Graphics g, final int tabPlacement, final int selectedIndex, final int x, final int y,
			final int w, final int h) {
		// Do nothing
	}

	@Override
	protected void paintContentBorderRightEdge(final Graphics g, final int tabPlacement, final int selectedIndex, final int x, final int y,
			final int w, final int h) {

	}

	@Override
	protected void paintContentBorderTopEdge(final Graphics g, final int tabPlacement, final int selectedIndex, final int x, final int y, final int w,
			final int h) {
		final Rectangle selectedRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, this.calcRect);

		selectedRect.width = selectedRect.width + selectedRect.height / 2 - 1;

		g.setColor(Color.BLACK);

		g.drawLine(x, y, selectedRect.x, y);
		g.drawLine(selectedRect.x + selectedRect.width + 1, y, x + w, y);

		g.setColor(Color.WHITE);

		g.drawLine(x, y + 1, selectedRect.x, y + 1);
		g.drawLine(selectedRect.x + 1, y + 1, selectedRect.x + 1, y);
		g.drawLine(selectedRect.x + selectedRect.width + 2, y + 1, x + w, y + 1);

		g.setColor(this.shadow);
		g.drawLine(selectedRect.x + selectedRect.width, y, selectedRect.x + selectedRect.width + 1, y + 1);
	}

	@Override
	protected void paintFocusIndicator(final Graphics g, final int tabPlacement, final Rectangle[] rects, final int tabIndex,
			final Rectangle iconRect, final Rectangle textRect, final boolean isSelected) {
		// Do nothing
	}

	@Override
	protected void paintTabArea(final Graphics g, final int tabPlacement, final int selectedIndex) {
		final int tw = this.tabPane.getBounds().width;
		g.setColor(new Color(158, 172, 191));
		g.fillRoundRect(0, 0, tw - 1, this.tabPane.getBounds().width + 6, 3, 3);
		// g.setColor(new Color(58,72,85));
		// g.drawRoundRect(0, 0, tw-1, tabPane.getBounds().width + 3,10,4);

		super.paintTabArea(g, tabPlacement, selectedIndex);
	}

	@Override
	protected void paintTabBackground(final Graphics g, final int tabPlacement, final int tabIndex, final int x, final int y, final int w,
			final int h, final boolean isSelected) {
		final Graphics2D g2d = (Graphics2D) g;
		final Polygon shape = new Polygon();
		new GradientPaint(x, y, new Color(165, 200, 219), x + w, y, new Color(166, 184, 219));

		final GradientPaint gradienteNormal = new GradientPaint(x, y, new Color(237, 237, 237), x + w / 2, y, new Color(154, 169, 188));

		shape.addPoint(x, y + h);
		shape.addPoint(x, y);
		shape.addPoint(x + w - h / 2, y);

		if (isSelected || tabIndex == this.rects.length - 1) {
			// g2d.setPaint(gradienteNormal);
			shape.addPoint(x + w + h / 2, y + h);
		} else {
			// g2d.setPaint(gradienteNormal);
			shape.addPoint(x + w, y + h / 2);
			shape.addPoint(x + w, y + h);
		}
		g2d.setPaint(gradienteNormal);
		g2d.setColor(this.tabPane.getBackground());

		g2d.fillPolygon(shape);
	}

	@Override
	protected void paintTabBorder(final Graphics g, final int tabPlacement, final int tabIndex, final int x, final int y, final int w, final int h,
			final boolean isSelected) {
		g.setColor(Color.BLACK);
		g.drawLine(x, y, x, y + h);
		g.drawLine(x, y, x + w - h / 2, y);
		g.drawLine(x + w - h / 2, y, x + w + h / 2, y + h);

		if (isSelected) {
			g.setColor(Color.WHITE);
			g.drawLine(x + 1, y + 1, x + 1, y + h);
			g.drawLine(x + 1, y + 1, x + w - h / 2, y + 1);

			g.setColor(this.shadow);
			g.drawLine(x + w - h / 2, y + 1, x + w + h / 2 - 1, y + h);
		}
	}

	@Override
	protected void paintText(final Graphics g, final int tabPlacement, final Font font, final FontMetrics metrics, final int tabIndex,
			final String title, final Rectangle textRect, final boolean isSelected) {
		if (isSelected) {
			final int vDifference = (int) this.boldFontMetrics.getStringBounds(title, g).getWidth() - textRect.width;
			textRect.x -= vDifference / 2;
			super.paintText(g, tabPlacement, this.boldFont, this.boldFontMetrics, tabIndex, title, textRect, isSelected);
		} else {
			super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
		}
	}
}
