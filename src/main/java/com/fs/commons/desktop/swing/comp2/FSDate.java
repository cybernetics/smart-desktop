package com.fs.commons.desktop.swing.comp2;

import java.util.Date;

import com.fs.commons.desktop.swing.comp.JKDate;

public class FSDate extends JKDate{

	public FSDate() {
		super();
	}

	public FSDate(Date date) {
		super(date);
	}

	public FSDate(String lableKey, int from, int to) {
		super(lableKey, from, to);
	}

	public FSDate(String lableKey) {
		super(lableKey);
	}
}
