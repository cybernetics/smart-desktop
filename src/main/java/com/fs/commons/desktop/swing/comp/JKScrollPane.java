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
package com.fs.commons.desktop.swing.comp;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;

public class JKScrollPane extends JScrollPane implements BindingComponent {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	private boolean transfer;

	public JKScrollPane() {
		super();
		init();
	}

	public JKScrollPane(final Component view) {
		super(view);
		init();
	}

	public JKScrollPane(final Component view, final int vsbPolicy, final int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
		init();
	}

	public JKScrollPane(final int vsbPolicy, final int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
		init();
	}

	@Override
	public void addActionListener(final ActionListener actionListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addValidator(final Validator validator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addValueChangeListener(final ValueChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void filterValues(final BindingComponent comp1) throws JKDataAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public JKDataSource getDataSource() {
		return this.fsWrapper.getDataSource();
	}

	@Override
	public Object getDefaultValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	private void init() {
		setOpaque(false);
		getViewport().setOpaque(false);
		setFocusable(false);
		getViewport().setBackground(SwingUtility.getDefaultBackgroundColor());
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
	}

	@Override
	public boolean isAutoTransferFocus() {
		return this.transfer;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAutoTransferFocus(final boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public void setDataSource(final JKDataSource manager) {
		this.fsWrapper.setDataSource(manager);
	}

	@Override
	public void setDefaultValue(final Object t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(final Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateValue() throws ValidationException {
		// TODO Auto-generated method stub

	}
}
