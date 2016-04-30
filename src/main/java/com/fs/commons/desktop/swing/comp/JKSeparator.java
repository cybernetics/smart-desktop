package com.fs.commons.desktop.swing.comp;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.fs.commons.desktop.swing.SwingUtility;

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
 * d
 * 
 * @author not attributable
 * @version 1.0
 */
public class JKSeparator extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param width
	 *            int
	 */
	public JKSeparator() {
		setBackground(SwingUtility.getDefaultBackgroundColor());
		setBorder(null);
		setPreferredSize(new Dimension(200, 2));
	}

	/**
	 * 
	 * @param g
	 *            Graphics
	 */
	@Override
	public void paint(Graphics g) {

		super.paint(g);

	}
}
