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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JComboBox;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.listeners.TransferFocusOnEnterKeyListener;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.desktop.validation.builtin.FSValidators;

public class JKComboBox extends JComboBox implements BindingComponent<Object> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private boolean transferFocusOnEnter = true;
	private boolean useTabEventForLostFocus = true;
	private Object defaultValue;
	protected FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private boolean required;
	// private boolean transfer;

	/**
	 *
	 *
	 */
	public JKComboBox() {
		init();
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
	}

	public void addItems(final List objects) {
		for (final Object object : objects) {
			addItem(object);
		}
	}

	@Override
	public void addValidator(final Validator validator) {
		this.fsWrapper.addValidator(validator);
	}

	@Override
	public void addValueChangeListener(final ValueChangeListener listener) {
		this.fsWrapper.addValueChangeListsner(listener);
	}

	/**
	 * @throws ValidationException
	 *
	 */
	public void checkEmpty() throws ValidationException {
		SwingValidator.checkEmpty(this);
	}

	@Override
	public void clear() {
		setSelectedIndex(-1);
	}

	@Override
	public void filterValues(final BindingComponent component) throws DaoException {
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
	public Object getValue() {
		if (getSelectedIndex() != -1) {
			return getSelectedItem();
		}
		;
		return null;
	}

	/**
	 *
	 */
	private void handleTransferFocus() {
		if (isUseTabEventForLostFocus()) {
			SwingUtility.pressKey(KeyEvent.VK_TAB);
		} else {
			transferFocus();
		}
	}

	/**
	 *
	 *
	 */
	private void init() {
		setOpaque(false);
		SwingUtility.setFont(this);
		setRenderer(new FSComboBoxListCellRenderer());
		addKeyListener(new TransferFocusOnEnterKeyListener(this));
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_DELETE) {
					setSelectedIndex(-1);
				}
				// if (transferFocusOnEnter && e.getKeyChar() ==
				// KeyEvent.VK_ENTER) {
				// handleTransferFocus();
				// // JKComboBox.this.transferFocus();
				// }
			}

			@Override
			public void keyTyped(final KeyEvent e) {
			}
		});
	}

	@Override
	public boolean isAutoTransferFocus() {
		return this.transferFocusOnEnter;
	}

	public boolean isRequired() {
		return this.required;
	}

	/**
	 * @return the transferFocusOnEnter
	 */
	public boolean isTransferFocusOnEnter() {
		return this.transferFocusOnEnter;
	}

	/**
	 * @return the useTabEventForLostFocus
	 */
	public boolean isUseTabEventForLostFocus() {
		return this.useTabEventForLostFocus;
	}

	@Override
	public void reset() {
		setSelectedItem(this.defaultValue);
	}

	@Override
	public void setAutoTransferFocus(final boolean transfer) {
		this.transferFocusOnEnter = transfer;
	}

	@Override
	public void setDataSource(final DataSource manager) {
		this.fsWrapper.setDataSource(manager);
	}

	@Override
	public void setDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setRequired(final boolean required) {
		addValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		this.required = required;
	}

	@Override
	public void setSelectedIndex(final int index) {
		// to avoid index outof bound exception
		super.setSelectedIndex(index >= getItemCount() ? -1 : index);
	}

	/**
	 * @param transferFocusOnEnter
	 *            the transferFocusOnEnter to set
	 */
	public void setTransferFocusOnEnter(final boolean transferFocusOnEnter) {
		this.transferFocusOnEnter = transferFocusOnEnter;
	}

	/**
	 * @param useTabEventForLostFocus
	 *            the useTabEventForLostFocus to set
	 */
	public void setUseTabEventForLostFocus(final boolean useTabEventForLostFocus) {
		this.useTabEventForLostFocus = useTabEventForLostFocus;
	}

	/**
	 *
	 */
	@Override
	public void setValue(final Object o) {
		setSelectedItem(o);
	}

	@Override
	public void validateValue() throws ValidationException {
		this.fsWrapper.validateValue();
	}
}
