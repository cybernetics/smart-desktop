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

import javax.swing.JRadioButton;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.locale.Lables;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class JKRadioButton extends JRadioButton implements BindingComponent<Object> {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	private final FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private boolean defaultValue;
	private final Font font = new Font("Arial", Font.PLAIN, 10);

	private boolean transfer;

	public JKRadioButton() {
		this("");
	}

	/**
	 * @param caption
	 *            String
	 */
	public JKRadioButton(final String caption) {
		super(caption, true);
		setPreferredSize(null);
		setOpaque(false);
		init();
	}

	public JKRadioButton(final String string, final boolean focusable) {
		this(string);
		setFocusable(focusable);
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
	public void filterValues(final BindingComponent comp1) {
		// TODO Auto-generated method stub

	}

	@Override
	public DataSource getDataSource() {
		return this.fsWrapper.getDataSource();
	}

	@Override
	public Boolean getDefaultValue() {
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
	public void setDefaultValue(Object defaultValue) {
		if (defaultValue != null) {
			if (defaultValue.equals("1")) {
				setDefaultValue(true);
			} else {
				setDefaultValue(Boolean.parseBoolean(defaultValue.toString()));
			}
		} else {
			defaultValue = false;
		}
	}

	@Override
	public void setText(final String text) {
		super.setText(Lables.get(text));
	}

	@Override
	public void setValue(final Object value) {
		if (value != null) {
			setSelected(Boolean.parseBoolean(value.toString()));
		}
	}

	@Override
	public void validateValue() throws ValidationException {
		// TODO Auto-generated method stub

	}
}
