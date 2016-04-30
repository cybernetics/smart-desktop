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

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;

/**
 * @author u087
 *
 */
public class JKTabbedPane extends JTabbedPane {
	// private static final Color SELECTED_FOREGROUND_COLOR = new Color(22, 125,
	// 219);

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	static {
		UIManager.put("TabbedPane.selected", SwingUtility.getDefaultBackgroundColor());
	}

	/**
	 *
	 */
	public JKTabbedPane() {
		init();

	}

	public JKTabbedPane(final int tabPlacement) {
		super(tabPlacement);
		// TODO Auto-generated constructor stub
	}

	public JKTabbedPane(final int tabPlacement, final int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 * @param caption
	 * @param panel
	 */
	@Override
	public void addTab(final String title, final Component component) {
		super.addTab(Lables.get(title, true), component);
	}

	/**
	 *
	 * @param name
	 * @param icon
	 * @param panel
	 */
	@Override
	public void addTab(final String title, final Icon icon, final Component component) {
		super.addTab(Lables.get(title, true), icon, component);
	}

	/**
	 * \
	 *
	 * @param caption
	 * @param iconName
	 * @param pnlMaster
	 */
	public void addTab(final String title, final String iconName, final Component component) {
		if (iconName != null && !iconName.equals("") && GeneralUtility.getIconURL(iconName) != null) {
			// ImageIcon icon=new
			// ImageIcon(GeneralUtility.getIconURL(iconName));
			this.addTab(title, GeneralUtility.getIcon(iconName), component);
		} else {
			this.addTab(title, component);
		}
	}

	void init() {
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		setOpaque(false);
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent e) {
				transferFocus();
			}
		});
	}

	@Override
	public void insertTab(final String title, final Icon icon, final Component component, final String tip, final int index) {
		// TODO Auto-generated method stub
		super.insertTab(Lables.get(title, true), icon, component, tip, index);
	}

	/**
	 *
	 */
	@Override
	public void setSelectedIndex(final int index) {
		super.setSelectedIndex(index);
		getSelectedComponent().requestFocus();
	}

}
