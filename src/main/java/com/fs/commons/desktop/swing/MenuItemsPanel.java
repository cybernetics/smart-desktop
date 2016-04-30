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

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fs.commons.application.Application;
import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.Module;
import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.application.ui.menu.MenuItem;
import com.fs.commons.desktop.swing.comp.JKTabbedPane;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.comp.panels.TitledPanel;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class MenuItemsPanel extends JKMainPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 8274406720718084124L;

	public static void main(final String[] args) throws FileNotFoundException, ApplicationException, UIOPanelCreationException {
		ApplicationManager.getInstance().init();
		final Application application = ApplicationManager.getInstance().getApplication();
		final ArrayList<Module> modules = application.getModules();
		final MenuItemsPanel m = new MenuItemsPanel(modules.get(4).getMenu().get(1));
		SwingUtility.testPanel(m);
	}

	private final Menu menu;

	public MenuItemsPanel(final Menu menu) throws UIOPanelCreationException {
		this.menu = menu;
		init();
	}

	private void init() throws UIOPanelCreationException {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createRaisedBevelBorder());
		final JTabbedPane tab = new JKTabbedPane();
		tab.setOpaque(true);

		tab.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI());
		tab.setTabPlacement(SwingUtility.getTabPaneLeadingPlacement());
		tab.setBackground(Colors.MI_BUTTON_BG);
		tab.setForeground(Colors.MI_BUTTON_FC);
		final ArrayList<MenuItem> items = this.menu.getItems();
		for (final MenuItem menuItem : items) {
			final String title = Lables.get(menuItem.getName());
			final JKPanel panel = new JKPanel();

			final ImageIcon icon = GeneralUtility.getIcon(menuItem.getIconName());
			tab.addTab(title, icon, panel, title);
		}
		tab.addChangeListener(new ChangeListener() {

			// This method is called whenever the selected tab changes
			@Override
			public void stateChanged(final ChangeEvent evt) {
				// Get current tab
				final int sel = tab.getSelectedIndex();
				if (sel >= 0 && sel < tab.getTabCount()) {
					// handle the index for visible components only ,
					// (priviloges)
					try {
						final MenuItem menuItem = items.get(sel);
						final String title = Lables.get(menuItem.getName());

						final ImageIcon icon = GeneralUtility.getIcon(menuItem.getIconName());
						final JKPanel panel = new TitledPanel(title, menuItem.createPanel(), menuItem.getIconName());

						tab.insertTab(title, icon, panel, title, sel);
					} catch (final UIOPanelCreationException e) {
						ExceptionUtil.handleException(e);
					}
				}
			}
		});
		add(tab);
	}

}
