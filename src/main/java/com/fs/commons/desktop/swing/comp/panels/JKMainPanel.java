package com.fs.commons.desktop.swing.comp.panels;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JComponent;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.desktop.graphics.GraphicsFactory.GradientType;

public class JKMainPanel extends JKPanel{

	private DataSource manager;

	public JKMainPanel() {
		super();
		initMe();
	}

	public JKMainPanel(JComponent component) {
		super(component);
		initMe();
	}

	public JKMainPanel(LayoutManager layout) {
		super(layout);
		initMe();
	}

	private void initMe() {
		 setOpaque(true);
		// setGradientType(GradientType.VERTICAL);
	}

	@Override
	public void paint(Graphics g) {
		if (getGradientType() == null) {
			if (getWidth() < getHeight()) {
				setGradientType(GradientType.HORIZENTAL_LINEAR);
			} else {
				setGradientType(GradientType.VERTICAL_LINEAR);
			}
		}
		super.paint(g);

	}


	// @Override
	// public void paint(Graphics g) {
	//		
	// super.paint(g);
	// }

}
