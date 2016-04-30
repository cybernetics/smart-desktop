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

import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPasswordField;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;

public class JKPasswordField extends JPasswordField implements BindingComponent<String> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// static Border focusBorder =
	// BorderFactory.createLineBorder(SystemColor.infoText);
	// static Border lostFocusBorder =
	// BorderFactory.createLineBorder(SystemColor.activeCaptionBorder);
	static Dimension dim = new Dimension(200, 30);
	private final FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private String defaultValue;

	private boolean transfer;

	public JKPasswordField() {
		this(10, 10);
	}

	public JKPasswordField(final int maxlength, final int col) {
		super(new TextDocument(maxlength), "", col);
		init();
	}

	@Override
	public void addValidator(final Validator validator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addValueChangeListener(final ValueChangeListener listener) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @throws ValidationException
	 */
	public void checkEmpty() throws ValidationException {
		SwingValidator.checkEmpty(this);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void filterValues(final BindingComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public DataSource getDataSource() {
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

	/**
	 * init
	 */
	private void init() {
		setPreferredSize(dim);
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		SwingUtility.setFont(this);
		// setBorder(lostFocusBorder);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(final KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					transferFocus();
				}
			}
		});

		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent e) {
				// setBorder(focusBorder);
				setSelectionStart(0);
				setSelectionEnd(getText().length());
			}

			@Override
			public void focusLost(final FocusEvent e) {
				// setBorder(lostFocusBorder);
			}
		});
	}

	@Override
	public boolean isAutoTransferFocus() {
		return this.transfer;
	}

	/**
	 *
	 */
	@Override
	public void reset() {
		setText(getDefaultValue() != null ? getDefaultValue() : "");
	}

	@Override
	public void setAutoTransferFocus(final boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public void setColumns(final int columns) {
		// Just ignore to take the preffered size
	}

	@Override
	public void setDataSource(final DataSource manager) {
		this.fsWrapper.setDataSource(manager);
	}

	/**
	 *
	 * @param defaultValue
	 */
	@Override
	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 *
	 */
	@Override
	public void setValue(final String value) {
		setText(value);
	}

	@Override
	public void validateValue() throws ValidationException {
		// TODO Auto-generated method stub

	}
}
