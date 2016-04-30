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
package com.fs.commons.desktop.swing;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class AquaBarTabbedPaneUI extends BasicTabbedPaneUI {

	private class ColorSet {
		Color topGradColor1;
		Color topGradColor2;

		Color bottomGradColor1;
		Color bottomGradColor2;
	}

	private class RollOverListener implements MouseMotionListener, MouseListener {

		private void checkRollOver() {
			final int currentRollOver = getRolloverTab();
			if (currentRollOver != AquaBarTabbedPaneUI.this.lastRollOverTab) {
				AquaBarTabbedPaneUI.this.lastRollOverTab = currentRollOver;
				final Rectangle tabsRect = new Rectangle(0, 0, AquaBarTabbedPaneUI.this.tabPane.getWidth(), HEIGHT);
				AquaBarTabbedPaneUI.this.tabPane.repaint(tabsRect);
			}
		}

		@Override
		public void mouseClicked(final MouseEvent e) {
		}

		@Override
		public void mouseDragged(final MouseEvent e) {
		}

		@Override
		public void mouseEntered(final MouseEvent e) {
			checkRollOver();
		}

		@Override
		public void mouseExited(final MouseEvent e) {
			AquaBarTabbedPaneUI.this.tabPane.repaint();
		}

		@Override
		public void mouseMoved(final MouseEvent e) {
			checkRollOver();
		}

		@Override
		public void mousePressed(final MouseEvent e) {
		}

		@Override
		public void mouseReleased(final MouseEvent e) {
		}
	}

	private static final int HEIGHT = 40;
	private static final Insets NO_INSETS = new Insets(0, 0, 0, 0);

	public static ComponentUI createUI(final JComponent c) {
		return new AquaBarTabbedPaneUI();
	}

	private final ColorSet selectedColorSet;
	private final ColorSet defaultColorSet;
	private final ColorSet hoverColorSet;
	private boolean contentTopBorderDrawn = true;
	private final Color lineColor = new Color(158, 158, 158);

	private final Color dividerColor = new Color(200, 200, 200);

	private Insets contentInsets = new Insets(10, 10, 10, 10);

	private int lastRollOverTab = -1;

	public AquaBarTabbedPaneUI() {

		this.selectedColorSet = new ColorSet();
		this.selectedColorSet.topGradColor1 = new Color(233, 237, 248);
		this.selectedColorSet.topGradColor2 = new Color(158, 199, 240);

		this.selectedColorSet.bottomGradColor1 = new Color(112, 173, 239);
		this.selectedColorSet.bottomGradColor2 = new Color(183, 244, 253);

		this.defaultColorSet = new ColorSet();
		this.defaultColorSet.topGradColor1 = new Color(253, 253, 253);
		this.defaultColorSet.topGradColor2 = new Color(237, 237, 237);

		this.defaultColorSet.bottomGradColor1 = new Color(222, 222, 222);
		this.defaultColorSet.bottomGradColor2 = new Color(255, 255, 255);

		this.hoverColorSet = new ColorSet();
		this.hoverColorSet.topGradColor1 = new Color(244, 244, 244);
		this.hoverColorSet.topGradColor2 = new Color(223, 223, 223);

		this.hoverColorSet.bottomGradColor1 = new Color(211, 211, 211);
		this.hoverColorSet.bottomGradColor2 = new Color(235, 235, 235);

		this.maxTabHeight = HEIGHT;

		setContentInsets(0);
	}

	@Override
	protected int calculateMaxTabHeight(final int tabPlacement) {
		return HEIGHT;
	}

	@Override
	protected int calculateTabHeight(final int tabPlacement, final int tabIndex, final int fontHeight) {
		return 21;
	}

	@Override
	protected int calculateTabWidth(final int tabPlacement, final int tabIndex, final FontMetrics metrics) {
		int w = super.calculateTabWidth(tabPlacement, tabIndex, metrics);
		final int wid = metrics.charWidth('M');
		w += wid * 2;
		return w;
	}

	@Override
	protected Insets getContentBorderInsets(final int tabPlacement) {
		return this.contentInsets;
	}

	@Override
	protected int getTabLabelShiftY(final int tabPlacement, final int tabIndex, final boolean isSelected) {
		return 0;
	}

	@Override
	public int getTabRunCount(final JTabbedPane pane) {
		return 1;
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		final RollOverListener l = new RollOverListener();
		this.tabPane.addMouseListener(l);
		this.tabPane.addMouseMotionListener(l);

		this.tabAreaInsets = NO_INSETS;
		this.tabInsets = new Insets(0, 0, 0, 1);
	}

	@Override
	protected void paintContentBorderBottomEdge(final Graphics g, final int tabPlacement, final int selectedIndex, final int x, final int y,
			final int w, final int h) {
		// Do nothing
	}

	@Override
	protected void paintContentBorderLeftEdge(final Graphics g, final int tabPlacement, final int selectedIndex, final int x, final int y,
			final int w, final int h) {
		// Do nothing
	}

	@Override
	protected void paintContentBorderRightEdge(final Graphics g, final int tabPlacement, final int selectedIndex, final int x, final int y,
			final int w, final int h) {
		// Do nothing
	}

	@Override
	protected void paintContentBorderTopEdge(final Graphics g, final int tabPlacement, final int selectedIndex, final int x, final int y, final int w,
			final int h) {

	}

	@Override
	protected void paintFocusIndicator(final Graphics g, final int tabPlacement, final Rectangle[] rects, final int tabIndex,
			final Rectangle iconRect, final Rectangle textRect, final boolean isSelected) {
		// Do nothing
	}

	@Override
	protected void paintTabArea(final Graphics g, final int tabPlacement, final int selectedIndex) {
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(new GradientPaint(0, 0, this.defaultColorSet.topGradColor1, 0, 10, this.defaultColorSet.topGradColor2));
		g2d.fillRect(0, 0, this.tabPane.getWidth(), HEIGHT / 2);

		g2d.setPaint(new GradientPaint(0, HEIGHT / 2, this.defaultColorSet.bottomGradColor1, 0, HEIGHT + 1, this.defaultColorSet.bottomGradColor2));
		g2d.fillRect(0, HEIGHT / 2, this.tabPane.getWidth(), HEIGHT / 2 + 1);
		super.paintTabArea(g, tabPlacement, selectedIndex);

		if (this.contentTopBorderDrawn) {
			g2d.setColor(this.lineColor);
			g2d.drawLine(0, HEIGHT, this.tabPane.getWidth() - 1, HEIGHT);
		}
	}

	@Override
	protected void paintTabBackground(final Graphics g, final int tabPlacement, final int tabIndex, final int x, final int y, final int w,
			final int h, final boolean isSelected) {
		final Graphics2D g2d = (Graphics2D) g;
		ColorSet colorSet;

		final Rectangle rect = this.rects[tabIndex];

		if (isSelected) {
			colorSet = this.selectedColorSet;
		} else if (getRolloverTab() == tabIndex) {
			colorSet = this.hoverColorSet;
		} else {
			colorSet = this.defaultColorSet;
		}

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int width = rect.width;
		int xpos = rect.x;
		if (tabIndex > 0) {
			width--;
			xpos++;
		}

		g2d.setPaint(new GradientPaint(xpos, 0, colorSet.topGradColor1, xpos, 10, colorSet.topGradColor2));
		g2d.fillRect(xpos, 0, width, HEIGHT / 2);

		g2d.setPaint(new GradientPaint(0, HEIGHT / 2, colorSet.bottomGradColor1, 0, HEIGHT + 1, colorSet.bottomGradColor2));
		g2d.fillRect(xpos, HEIGHT / 2, width, HEIGHT / 2 + 1);

		if (this.contentTopBorderDrawn) {
			g2d.setColor(this.lineColor);
			g2d.drawLine(rect.x, HEIGHT, rect.x + rect.width - 1, HEIGHT);
		}
	}

	@Override
	protected void paintTabBorder(final Graphics g, final int tabPlacement, final int tabIndex, final int x, final int y, final int w, final int h,
			final boolean isSelected) {
		final Rectangle rect = getTabBounds(tabIndex, new Rectangle(x, y, w, h));
		g.setColor(this.dividerColor);
		g.drawLine(rect.x + rect.width, 0, rect.x + rect.width, HEIGHT);
	}

	protected boolean scrollableTabLayoutEnabled() {
		return false;
	}

	public void setContentInsets(final Insets i) {
		this.contentInsets = i;
	}

	public void setContentInsets(final int i) {
		this.contentInsets = new Insets(i, i, i, i);
	}

	public void setContentTopBorderDrawn(final boolean b) {
		this.contentTopBorderDrawn = b;
	}
}
