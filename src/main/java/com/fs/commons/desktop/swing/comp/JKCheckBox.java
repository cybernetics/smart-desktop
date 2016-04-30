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

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.locale.Lables;

public class JKCheckBox extends JCheckBox implements BindingComponent<Object> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Font font = new Font("arial", Font.PLAIN, 12);
	boolean defaultValue;
	private final FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private boolean transfer;

	public JKCheckBox() {
		this("");
	}

	/**
	 *
	 * @param caption
	 *            String
	 */
	public JKCheckBox(final String caption) {
		super(Lables.get(caption));
		setBackground(SwingUtility.getDefaultBackgroundColor());
		init();
	}

	public JKCheckBox(final String caption, final boolean checked) {
		this(caption);
		setSelected(checked);
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
		setSelected(false);
	}

	@Override
	public void filterValues(final BindingComponent component) {

	}

	@Override
	public DataSource getDataSource() {
		return this.fsWrapper.getDataSource();
	}

	@Override
	public Object getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 *
	 */
	@Override
	public Boolean getValue() {
		return isSelected();
	}

	/**
	 *
	 */
	void init() {
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		setHorizontalAlignment(JKCheckBox.LEADING);
		setFont(this.font);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(final KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					transferFocus();
				}
			}
		});
	}

	@Override
	public boolean isAutoTransferFocus() {
		return this.transfer;
	}

	/**
	 * @return the defaultValue
	 */
	public boolean isDefaultValue() {
		return this.defaultValue;
	}

	/**
	 *
	 */
	@Override
	public void reset() {
		setSelected(this.defaultValue);
	}

	@Override
	public void setAutoTransferFocus(final boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public void setDataSource(final DataSource manager) {
		this.fsWrapper.setDataSource(manager);
	}

	@Override
	public void setDefaultValue(final Object defaultValue) {
		if (defaultValue != null) {
			if (defaultValue.equals("1")) {
				this.defaultValue = true;
			} else {
				this.defaultValue = Boolean.parseBoolean(defaultValue.toString());
			}
		} else {
			this.defaultValue = false;
		}
	}

	@Override
	public void setText(final String text) {
		super.setText(Lables.get(text));
	}

	/**
	 *
	 */
	@Override
	public void setValue(Object value) {
		if (value != null) {
			if (value.toString().trim().equals("1") || value.toString().trim().toLowerCase().equals("true")) {
				value = true;
			} else {
				value = false;
			}
		} else {
			value = false;
		}
		setSelected((Boolean) value);
	}

	@Override
	public void validateValue() throws ValidationException {
		// TODO Auto-generated method stub

	}
}
