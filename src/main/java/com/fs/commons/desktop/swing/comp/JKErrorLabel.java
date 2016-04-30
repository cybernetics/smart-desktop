package com.fs.commons.desktop.swing.comp;

import java.awt.Color;

import javax.swing.Icon;

public class JKErrorLabel extends JKLabel{

	public JKErrorLabel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JKErrorLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	public JKErrorLabel(Icon image) {
		super(image);
		// TODO Auto-generated constructor stub
	}

	public JKErrorLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	public JKErrorLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	public JKErrorLabel(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void init() {
		super.init();
		setOpaque(false);
		setForeground(Color.red);
	}
	
}
