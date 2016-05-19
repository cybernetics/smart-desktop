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
import java.awt.ComponentOrientation;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.fs.commons.application.Application;
import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.Module;
import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.desktop.swing.comp.JKTabbedPane;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;
import com.jk.security.JKSecurityManager;
import com.jk.security.JKUser;

public class ApplicationPanel extends JKMainPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 3185142130653102626L;

	public static void main(final String[] args) throws FileNotFoundException, ApplicationException, UIOPanelCreationException {

		ApplicationManager.getInstance().init();
		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		final Application application = ApplicationManager.getInstance().getApplication();

		JKSecurityManager.setCurrentUser(new JKUser(1));
		// ArrayList<Module> modules = application.getModules();
		final ApplicationPanel m = new ApplicationPanel(application);

		SwingUtility.showPanelFrame(m, "");

		// SwingUtility.printInstalledLookAndFeel();
	}

	private final Application appliaction;

	public ApplicationPanel(final Application appliaction) throws UIOPanelCreationException {
		this.appliaction = appliaction;
		init();
	}

	private void init() throws UIOPanelCreationException {
		setLayout(new BorderLayout());
		final JTabbedPane tab = new JKTabbedPane();
		tab.setOpaque(true);
		tab.setUI(new AquaBarTabbedPaneUI());
		final List<Module> modules = this.appliaction.getModules();
		for (int i = 0; i < modules.size(); i++) {
			final Module module = modules.get(i);
			final String title = Lables.get(module.getModuleName());
			final JKPanel panel = new ModulePanel(module);
			final ImageIcon icon = GeneralUtility.getIcon(module.getIconName());
			tab.addTab(title, icon, panel, title);
		}

		add(tab);
	}

}
