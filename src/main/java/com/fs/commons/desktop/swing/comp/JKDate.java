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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.listeners.TransferFocusOnEnterKeyListener;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.validation.Problem;
import com.fs.commons.desktop.validation.Severity;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.desktop.validation.builtin.FSValidators;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.DateTimeUtil;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.FormatUtil;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class JKDate extends JDateChooser implements BindingComponent {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		final JKDate d = new JKDate();
		d.setDate(new Date());
		final JKPanel pnl = new JKPanel(d);
		SwingUtility.testPanel(pnl);

	}

	private Object defaultValue;

	private final FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	private Date minDate;

	private Date maxDate;

	private boolean transfer = true;

	/**
	 *
	 */
	public JKDate() {
		this("Date");
	}

	/**
	 *
	 * @param date
	 */
	public JKDate(final Date date) {
		this("");
		setDate(date);
	}

	/**
	 * This is the default constructor
	 */
	public JKDate(final String lableKey) {
		this(lableKey, 1930, 2020);
		setDate(new Date());
	}

	/**
	 *
	 * @param lableKey
	 * @param from
	 * @param to
	 */
	public JKDate(final String lableKey, final int from, final int to) {
		super();
		initialize();
	}

	@Override
	public void addActionListener(final ActionListener actionListener) {

	}

	@Override
	public synchronized void addKeyListener(final KeyListener l) {
		getEditor().addKeyListener(l);
	}

	@Override
	public void addValidator(final Validator validator) {
		this.fsWrapper.addValidator(validator);
	}

	@Override
	public void addValueChangeListener(final ValueChangeListener listener) {
		this.fsWrapper.addValueChangeListsner(listener);
	}

	public void checkAfterToday() throws ValidationException {
		if (getDate().after(new Date())) {
			throw new ValidationException("DATE_IS_AFTER_TODAY");
		}
	}

	public void checkValue() throws ValidationException {
		if (getValue() == null && !isEmpty()) {
			throw new ValidationException(this, new Problem("INVALID_DATE_VALUE", Severity.FATAL));
		}
	}

	@Override
	public void clear() {
		setValue(null);
	}

	// @Override
	// public void setEnabled(boolean enabled) {
	// cmbDay.setEnabled(enabled);
	// cmbMonth.setEnabled(enabled);
	// cmbYear.setEnabled(enabled);
	// }

	@Override
	public void filterValues(final BindingComponent comp1) {
		// TODO Auto-generated method stub
	}

	// //
	// ////////////////////////////////////////////////////////////////////////
	// public Date getValue() {
	// try {
	// checkValue();
	// return getDate();
	// } catch (ValidationException e) {
	// return null;
	// }
	// }

	// This method is called when editing is completed.
	// It must return the new value to be stored in the cell.
	public Object getCellEditorValue() {
		return getValue();
	}

	@Override
	public DataSource getDataSource() {
		return this.fsWrapper.getDataSource();
	}

	@Override
	public Date getDate() {
		Date date = super.getDate();
		if (date != null) {
			// Mojbaron Akhaka la batal :)
			if (getEditor().getForeground() == Color.red) {
				date = null;
			}
		}
		return date;
	}

	@Override
	public Object getDefaultValue() {
		return this.defaultValue;
	}

	private JFormattedTextField getEditor() {
		return (JTextFieldDateEditor) getDateEditor();
	}

	public Date getMaxDate() {
		return this.maxDate;
	}

	public Date getMinDate() {
		return this.minDate;
	}

	public java.sql.Date getSqlDate() {
		final Date date = getDate();
		if (date == null) {
			return null;
		}
		return new java.sql.Date(date.getTime());
	}

	@Override
	public Object getValue() {
		final Date date = getDate();
		return date;
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		// setFocusable(true);
		// getEditor().setFocusable(true);
		getEditor().setLocale(SwingUtility.getDefaultLocale());
		add(this.calendarButton, BorderLayout.LINE_END);
		setOpaque(true);
		setBorder(BorderFactory.createEmptyBorder());
		setLocale(SwingUtility.getDefaultLocale());
		setDateFormatString(FormatUtil.PATTERN_DEFAULT);
		addKeyListener(new TransferFocusOnEnterKeyListener(this));
	}

	@Override
	public boolean isAutoTransferFocus() {
		return this.transfer;
	}

	public boolean isEmpty() {
		if (((JFormattedTextField) getDateEditor()).getText().equals("")) {
			return true;
		}
		return false;
	}

	// public void setMinSelectableDate(Date date) {
	// getDateEditor().setMinSelectableDate(date);
	// }

	@Override
	public void requestFocus() {
		super.requestFocus();
		getEditor().requestFocus();
	}

	@Override
	public void reset() {
		setValue(getDefaultValue());
	}

	/**
	 *
	 *
	 */
	public void resetDate() {
		setDate(null);
	}

	@Override
	public void setAutoTransferFocus(final boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public void setBackground(final Color bg) {
		if (getEditor() == null) {
			// when called from constructor
			super.setBackground(bg);
		} else {
			getEditor().setBackground(bg);
		}
	}

	@Override
	public void setDataSource(final DataSource manager) {
		this.fsWrapper.setDataSource(manager);
	}

	@Override
	public void setDefaultValue(final Object defaultValue) {
		if (defaultValue != null) {
			if (defaultValue instanceof String && defaultValue.toString().toLowerCase().equals("now")) {
				// System.out.println("defult value from JKDate to now: " +
				// defaultValue);
				try {
					this.defaultValue = DaoUtil.getSystemDate();
				} catch (final Exception e) {
					ExceptionUtil.handleException(e);
				}
				return;
			}
		}
		this.defaultValue = defaultValue;
	}

	public void setMaxDate(final Date maxDate) {
		this.maxDate = maxDate;
		getDateEditor().setMaxSelectableDate(maxDate);
	}

	public void setMaxDate(final String maxDate) {
		try {
			if (maxDate != null && !maxDate.trim().equals("")) {
				setMaxDate(DateTimeUtil.parseShortDate(maxDate));
			}
		} catch (final ParseException e) {
			ExceptionUtil.handleException(e);
		}
	}

	public void setMinDate(final Date minDate) {
		this.minDate = minDate;
		getDateEditor().setMinSelectableDate(minDate);
	}

	public void setMinDate(final String date) {
		try {
			if (date != null && !date.trim().equals("")) {
				setMinDate(DateTimeUtil.parseShortDate(date));
			}
		} catch (final ParseException e) {
			ExceptionUtil.handleException(e);
		}
	}

	@Override
	public void setName(final String name) {
		super.setName(Lables.get(name));
	}

	public void setRequired(final boolean required) {
		if (required) {
			addValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		} else {
			this.fsWrapper.removeValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	@Override
	public void setValue(final Object value) {
		// System.out.println("setValue at FSDate = "+value+"with class
		// "+(value==null?"":
		// value.getClass()));
		if (value == null || value.equals("null")) {
			setDate(null);
			return;
		}
		if (value instanceof Date) {
			setDate((Date) value);
		} else if (value instanceof String) {
			try {
				setDate(DateTimeUtil.parseShortDate(value.toString()));
			} catch (final ParseException e) {
				ExceptionUtil.handleException(e);
			}

		}
	}

	// @Override
	// protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int
	// condition, boolean pressed) {
	// return getEditor().
	// }

	public String toMySqlString() {
		return FormatUtil.formatMysqlDate(getDate());
	}

	/**
	 *
	 * @return
	 */
	public String toSimpleString() {
		return FormatUtil.formatDate(getDate(), FormatUtil.PATTERN_SHORT_DATE);
	}

	/**
	 *
	 * @throws ValidationException
	 */
	// public void checkAfterToday() throws ValidationException {
	// if (getDate().after(new java.sql.Date(System.currentTimeMillis()))) {
	// throw new ValidationException("DATE_AFTER_TODAY_DATE_IS_NOT_ALLOWED");
	// }
	// }

	// /**
	// *
	// * @return
	// */
	// public boolean isNull() {
	// return cmbDay.getSelectedIndex() == -1 && cmbMonth.getSelectedIndex() ==
	// -1 && cmbYear.getSelectedIndex() == -1;
	// }

	@Override
	public String toString() {
		return FormatUtil.formatDate(getDate(), FormatUtil.PATTERN_DEFAULT);
	}

	/**
	 *
	 * @param pattern
	 * @return
	 */
	public String toString(final String pattern) {
		// SimpleDateFormat format = new SimpleDateFormat(pattern);
		return FormatUtil.formatDate(getDate(), pattern);

	}

	@Override
	public void validateValue() throws ValidationException {
		this.fsWrapper.validateValue();
		checkValue();
	}
}
