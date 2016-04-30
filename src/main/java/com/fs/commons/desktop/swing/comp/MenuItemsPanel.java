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
	public MenuItemsPanel(String title) {
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(Lables.get("MENU") + " " + title));
	}

}
