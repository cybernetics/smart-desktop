package com.fs.commons.desktop.swing.comp2;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;

public class FSFixedSizePanel extends JKLabledComponent {
	public FSFixedSizePanel() {
		super();
	}

	public FSFixedSizePanel(JKLabel lbl, int labelWidth, BindingComponent txt) {
		super(lbl, labelWidth, txt);
		// TODO Auto-generated constructor stub
	}

	public FSFixedSizePanel(JKLabel lbl, JKTextField txt) {
		super(lbl, txt);
		// TODO Auto-generated constructor stub
	}

	public FSFixedSizePanel(String labelKey, BindingComponent comp) {
		super(labelKey, comp);
		// TODO Auto-generated constructor stub
	}

	public FSFixedSizePanel(String labelKey, int labelWidth, BindingComponent comp) {
		super(labelKey, labelWidth, comp);
		// TODO Auto-generated constructor stub
	}

}
