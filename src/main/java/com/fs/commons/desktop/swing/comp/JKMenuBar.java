package com.fs.commons.desktop.swing.comp;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JMenuBar;
import javax.swing.border.Border;

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
public class JKMenuBar extends JMenuBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Color forColor = new Color(255, 240, 255);

	static Color backColor = new Color(22, 125, 219);

	static Color selectedBackColor = new Color(22, 125, 219);

	static Border FOCUS_BORDER = BorderFactory.createLineBorder(Color.orange);

	Border DEFAULT_BORDER = getBorder();

	static Dimension dim = new Dimension(130, 40);

	//////////////////////////////////////////////////
	public JKMenuBar() {
		//uncomment the follwing line to make it draw the caption on two lines
	//	super(SwingUtility.fixTwoLinesIssue(Lables.get(str)));
		init();
		setBackground(backColor);
		//setSelectedBGColor(selectedBackColor);
	}

	void init() {
		setPreferredSize(dim);
		setBackground(backColor);
		setForeground(forColor);
	}

}
