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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;

import com.fs.commons.locale.Lables;

public class MenuItemsPanel extends JToolBar {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	static Dimension size = new Dimension(172, 500);

	static Color borderColor = new Color(226, 244, 245);

	/**
	 *
	 * @param title
	 *            String
	 */
	public MenuItemsPanel(final String title) {
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(Lables.get("MENU") + " " + title));
	}

}
