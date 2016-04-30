package com.fs.commons.desktop.swing.comp;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.fs.commons.desktop.swing.SwingUtility;

public class RightListCellRenderer extends BasicComboBoxRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RightListCellRenderer() {
		SwingUtility.setFont(this);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if(!SwingUtility.isLeftOrientation()){
			setHorizontalAlignment(JLabel.RIGHT);
		}
		return this;
	}
}
