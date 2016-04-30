package com.fs.commons.desktop.swing.comp2;

import com.fs.commons.desktop.swing.comp.JKPasswordField;

public class FSPasswordField extends JKPasswordField {
	private static final int DEFAULT_COLUMNS_SIZE = 20;
	private static final int DEFAULT_MAX_LENGTH = 20;


	public FSPasswordField() {
		this(DEFAULT_MAX_LENGTH,DEFAULT_COLUMNS_SIZE);
	}
	
	
	public FSPasswordField(int maxlength, int col) {
		super(maxlength, col);
		// TODO Auto-generated constructor stub
	}

}
