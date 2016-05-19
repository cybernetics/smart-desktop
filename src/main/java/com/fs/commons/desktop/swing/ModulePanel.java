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
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.fs.commons.application.Application;
import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.Module;
import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;

public class ModulePanel extends JKPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1504201233802276568L;

	public static void main(final String[] args) throws FileNotFoundException, ApplicationException, UIOPanelCreationException {
		ApplicationManager.getInstance().init();
		// ApplicationManager.getInstance().start();
		final Application application = ApplicationManager.getInstance().getApplication();
		final List<Module> modules = application.getModules();
		final ModulePanel m = new ModulePanel(modules.get(7));
		SwingUtility.testPanel(m);
	}

	private final Module module;

	public ModulePanel(final Module module) throws UIOPanelCreationException {
		this.module = module;
		init();
	}

	private void init() throws UIOPanelCreationException {
		setLayout(new BorderLayout());
		final JTabbedPane tab = new JTabbedPane();
		tab.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI());
		tab.setBackground(Colors.MENU_BUTTON_BG);
		tab.setForeground(Colors.MENU_BUTTON_FC);
		final ArrayList<Menu> menus = this.module.getMenu();

		for (final Menu menu : menus) {
			final String title = Lables.get(menu.getName());
			final JKPanel panel = new MenuItemsPanel(menu);
			final ImageIcon icon = GeneralUtility.getIcon(menu.getIconName());
			tab.addTab(title, icon, panel, title);
		}
		add(tab);
	}

}
