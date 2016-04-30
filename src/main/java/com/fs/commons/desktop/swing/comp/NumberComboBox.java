package com.fs.commons.desktop.swing.comp;

import com.fs.commons.desktop.swing.SwingUtility;

public class NumberComboBox extends JKComboBox  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NumberComboBox(int from, int to) {
		SwingUtility.setFont(this);
		for (int i = from; i <= to; i++) {
			addItem(i);
		}
	}

	/**
	 * 
	 */
	@Override
	public String getValue() {
		if (getSelectedItem() != null) {
			return ((Integer) getSelectedItem()).toString();
		}
		return null;
	}

	/**
	 * 
	 */
	@Override
	public void setValue(Object o) {
		setSelectedItem(o);
	}

}
