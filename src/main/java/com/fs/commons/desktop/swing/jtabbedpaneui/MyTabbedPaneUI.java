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
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 * <pre>
 * Tabs are bigger (ADDED_TAB_HEIGTH)
 * Tabs are larger than the title they display (ADDED_TAB_WIDTH)
 * Tabs are spaced (SPACE_BETWEEN_TAB)
 * Tabs are gray (TAB_COLOR)
 * Selected tab is blue (SELECTED_TAB_COLOR)
 * Tabs title font is white and bold
 * </pre>
 *
 * @author Jerome Simard
 */
public class MyTabbedPaneUI extends BasicTabbedPaneUI {

	private static final int ADDED_TAB_HEIGTH = 2;
	private static final int ADDED_TAB_WIDTH = 10;
	private static final int SPACE_BETWEEN_TAB = 1;
	// private Color tabColor = Color.GRAY;
	private Color selectedTabBAckground = Color.BLUE;

	// public static ComponentUI createUI(JComponent c) {
	// return new MyTabbedPaneUI((JTabbedPane) c);
	// }

	public MyTabbedPaneUI() {
		// tabbedPane.setFont(tabbedPane.getFont().deriveFont(Font.BOLD));
		// tabbedPane.setForeground(tabForground);
		// tabbedPane.setBackground(selectedTabColor);
	}

	// overrided to add spaces between tabs.
	@Override
	protected LayoutManager createLayoutManager() {
		if (this.tabPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT) {
			return super.createLayoutManager();

		} else { /* WRAP_TAB_LAYOUT */
			return new TabbedPaneLayout() {

				@Override
				protected void padSelectedTab(final int tabPlacement, final int selectedIndex) {
					// don't pad only the selected tab, but all the tabs, to
					// space them.
					for (final Rectangle rect : MyTabbedPaneUI.this.rects) {
						final Rectangle selRect = rect;
						final Insets padInsets = getSelectedTabPadInsets(tabPlacement);
						selRect.x += padInsets.left;
						selRect.width -= padInsets.left + padInsets.right;
						selRect.y -= padInsets.top;
						selRect.height += padInsets.top + padInsets.bottom;
					}
				}
			};
		}
	}

	// overrided to add more space each side of the tab title and spacing
	// between tabs.
	@Override
	protected void installDefaults() {
		super.installDefaults();

		// changed to add more space each side of the tab title.
		this.tabInsets.left = this.tabInsets.left + ADDED_TAB_WIDTH;
		this.tabInsets.right = this.tabInsets.right + ADDED_TAB_WIDTH;
		this.tabInsets.top = this.tabInsets.top + ADDED_TAB_HEIGTH;
		this.tabInsets.bottom = this.tabInsets.bottom + ADDED_TAB_HEIGTH;

		// changed to define the spacing between tabs.
		this.selectedTabPadInsets.left = SPACE_BETWEEN_TAB;
		this.selectedTabPadInsets.right = SPACE_BETWEEN_TAB;
	}

	// overrided to paint the selected tab with a different color.
	@Override
	protected void paintTabBackground(final Graphics g, final int tabPlacement, final int tabIndex, final int x, final int y, final int w,
			final int h, final boolean isSelected) {
		// tabpane background color is not used to paint the unselected tabs
		// because we wants a different color for the background of the tabs
		// and the background of the tabpane (the line just below the tabs)
		g.setColor(!isSelected ? this.tabPane.getBackground() : this.selectedTabBAckground);
		switch (tabPlacement) {
		case LEFT:
			g.fillRect(x + 1, y + 1, w - 1, h - 3);
			break;
		case RIGHT:
			g.fillRect(x, y + 1, w - 2, h - 3);
			break;
		case BOTTOM:
			g.fillRect(x + 1, y, w - 3, h - 1);
			break;
		case TOP:
		default:
			g.fillRect(x + 1, y + 1, w - 3, h - 1);
		}
	}

	public void setSelectedTabBackground(final Color selectedTabBAckground) {
		this.selectedTabBAckground = selectedTabBAckground;
	}

	public void setSelectedTabForColor(final Color selectedTabForColor) {
	}

}
