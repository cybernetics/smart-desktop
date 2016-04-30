package com.fs.commons.desktop.swing.comp.renderers;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.fs.commons.desktop.swing.SwingUtility;

public class FSListCellRenderer extends BasicComboBoxRenderer {
	////////////////////////////////////////////////////////////////
	private static final long serialVersionUID = 1L;

	////////////////////////////////////////////////////////////////
	public FSListCellRenderer() {
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
	}

	////////////////////////////////////////////////////////////////
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		return this;
	}
}
