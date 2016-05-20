/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fs.commons.desktop.swing.comp.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.desktop.swing.comp.JKPasswordField;
import com.fs.commons.desktop.swing.comp.JKTextField;

public class JKLabledComponent extends JKPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private BindingComponent txt;

	private BindingComponent lbl;

	private int lableWidth = 75;

	private int labelWidth;

	public JKLabledComponent() {
		init();
	}

	/**
	 *
	 * @param lbl
	 *            JKLabel
	 * @param txt
	 *            JTextComponent
	 */
	public JKLabledComponent(final JKLabel lbl, final int labelWidth, final BindingComponent txt) {
		this.lbl = lbl;
		this.labelWidth = labelWidth;
		this.txt = txt;
		init();
//		add(lbl);
//		add((Component) txt);
	}

	/**
	 * fix the label width hard-coded value
	 * @param lbl
	 * @param txt
	 */
	public JKLabledComponent(final JKLabel lbl, final BindingComponent txt) {
		this(lbl,120,txt);
//		init();
//		add(lbl);
//		add(txt);
	}

	public JKLabledComponent(final String labelKey, final BindingComponent comp) {
		this(new JKLabel(labelKey), comp);
	}

	/**
	 *
	 * @param lbl
	 *            JKLabel
	 * @param txt
	 *            JTextComponent
	 */
	public JKLabledComponent(final String labelKey, final int labelWidth, final BindingComponent comp) {
		this(labelKey, labelWidth, comp, true);
	}

	public JKLabledComponent(final String labelKey, final int labelWidth, final BindingComponent comp, final boolean visible) {
		this(new JKLabel(labelKey), labelWidth, comp);
		setVisible(visible);
	}

	public JKLabledComponent(final String label, final int labelWidth, final JKTextField txt, final int txtWidth) {
		this(label, labelWidth, txt);
		txt.setWidth(txtWidth);
	}


	public int getLableWidth() {
		return this.lableWidth;
	}

	/**
	 *
	 */
	protected void init() {
		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		setFocusable(false);
		setLayout(new BorderLayout());
		// ((FlowLayout)getLayout()).setAlignment(FlowLayout.LEADING);
		SwingUtility.setFont(this);
		// txt.getPreferredSize().setSize(1, 30);
		applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		if(txt instanceof JKTextField){
			((JKTextField) txt).setPlaceholder(lbl.getValue().toString());
		}
		if(txt instanceof JKPasswordField){
			((JKPasswordField) txt).setPlaceholder(lbl.getValue().toString());
		}

		setLableWidth(labelWidth);
		
		final Dimension preferredSize = lbl.getPreferredSize();
		preferredSize.width = getLableWidth();
		this.lbl.setPreferredSize(preferredSize);
		add((Component)lbl, BorderLayout.LINE_START,0);
		add((Component) txt, BorderLayout.CENTER, 1);

	}

	public void setLableWidth(final int lableWidth) {
		this.lableWidth = lableWidth;
	}

	@Override
	public void setLayout(final LayoutManager mgr) {
		if (mgr instanceof BorderLayout) {
			super.setLayout(mgr);
		}
	}

}
