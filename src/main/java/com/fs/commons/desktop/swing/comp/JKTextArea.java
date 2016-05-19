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

import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.text.Document;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;

public class JKTextArea extends JTextArea implements BindingComponent<String>, JKScrollable {

	private static final long serialVersionUID = 1L;

	private final FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private String defaultValue;

	private boolean transfer;

	public JKTextArea() {

	}

	public JKTextArea(final Document doc) {
		super(doc);
		init();
	}

	public JKTextArea(final Document doc, final String text, final int rows, final int columns) {
		super(doc, text, rows, columns);
		init();
	}

	public JKTextArea(final int rows, final int columns) {
		super(rows, columns);
		init();
	}

	public JKTextArea(final String text) {
		super(text);
		init();
	}

	public JKTextArea(final String text, final int rows, final int columns) {
		super(text, rows, columns);
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
		setValue(null);
	}

	@Override
	public void filterValues(final BindingComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public JKDataSource getDataSource() {
		return this.fsWrapper.getDataSource();
	}

	@Override
	public String getDefaultValue() {
		return this.defaultValue;
	}

	@Override
	public String getValue() {
		return getText();
	}

	void init() {
		// setFont(JKTextField.DEFAULT_FONT);
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
	}

	@Override
	public boolean isAutoTransferFocus() {
		return this.transfer;
	}

	@Override
	public void reset() {
		setValue(this.defaultValue);
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
	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public void setValue(final String value) {
		if (value != null) {
			setText(value.toString());
		} else {
			setText("");
		}
	}

	@Override
	public void validateValue() throws ValidationException {
		// TODO Auto-generated method stub

	}
}
