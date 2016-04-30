package com.fs.commons.desktop.swing.comp;

import java.awt.Color;
import java.awt.Dimension;

import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.locale.Lables;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class JKMenuItem extends JKButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Dimension size = new Dimension(160, 40);

	// static Color bgColor = new Color(206, 222, 239);
	static Color backColor = Colors.MI_BUTTON_BG;//new Color(229, 238, 255);
	//static Font font=new java.awt.Font("tahoma",Font.BOLD,11);

	// static Color selectedBackColor = new Color(255, 211, 73);
	//static Color selectedBackColor = new Color(255, 211, 73);

	static Color forColor = Colors.MI_BUTTON_FC;//new Color(22, 125, 219);

	// static Border selectedBorder=BorderFactory.createLoweredBevelBorder();
	// static Border notSelectedBorder=BorderFactory.createEtchedBorder();

	// static final int lineCharCounts=10;

	public JKMenuItem(String str) {
		super(Lables.get(str));
		
		init();
		setFocusable(false);
		setBackground(backColor);
		setForeground(forColor);
		setPreferredSize(size);
		setHorizontalAlignment(LEADING);
		//setFont(font);
		
	}
	//
	// /**
	// *
	// */
	// @Override
	// void init() {
	// super.init();
	// // setBackground(bgColor);
	// // setBorder(notSelectedBorder);
	// }

}
