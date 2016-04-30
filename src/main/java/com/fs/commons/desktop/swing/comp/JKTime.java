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

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.locale.Lables;

public class JKTime extends JKPanel<Object> {
	private static final long serialVersionUID = 1L;

	public static void main(final String[] args) {
		// JPanel pnl=new JPanel();
		final JKTime t = new JKTime("Time ");
		// pnl.add(t);
		t.setTime(new Date());
		// SwingUtil.testPanel(pnl);
		System.out.println(t.getTime());
	}

	private JKComboBox cmbMin = null;

	private JKComboBox cmbHour = null;

	private int toHour;

	private int fromHour;

	private Object defaultValue;

	public JKTime() {

	}

	/**
	 * This is the default constructor
	 *
	 * @param lableKey
	 *            String
	 */
	public JKTime(final String lableKey) {
		this(Lables.get(lableKey), 8, 24);
	}

	/**
	 *
	 * @param lableKey
	 *            String
	 * @param fromHour
	 *            int
	 * @param toHour
	 *            int
	 */
	public JKTime(final String lableKey, final int fromHour, final int toHour) {
		super();
		this.fromHour = fromHour;
		this.toHour = toHour;
		// lbl = new JKLabel(lableKey);
		initialize();
	}

	/**
	 * @throws ValidationException
	 *
	 *
	 */
	public void checkValue() throws ValidationException {
		SwingValidator.checkEmpty(this.cmbHour);
		SwingValidator.checkEmpty(this.cmbMin);
	}

	@Override
	public void clear() {
		setValue(null);
	}

	/**
	 * This method initializes cmbHour
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbHour() {
		if (this.cmbHour == null) {
			this.cmbHour = new JKComboBox();
			for (int i = this.fromHour; i <= this.toHour; i++) {
				this.cmbHour.addItem(new Integer(i));
			}
		}
		return this.cmbHour;
	}

	/**
	 * This method initializes cmbMin
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbMin() {
		if (this.cmbMin == null) {
			this.cmbMin = new JKComboBox();
			for (int i = 0; i < 60; i += 5) {
				this.cmbMin.addItem(new Integer(i));
			}
		}
		return this.cmbMin;
	}

	@Override
	public Object getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 *
	 * @return java.util.Date
	 */
	public Date getTime() {
		if (isNull()) {
			return null;
		}
		final Calendar cal = Calendar.getInstance();
		final int min = (Integer) this.cmbMin.getSelectedItem();
		final int hour = (Integer) this.cmbHour.getSelectedItem();

		cal.setTimeInMillis(0);

		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}

	/**
	 * @1.1
	 * 
	 * @return String
	 */
	public String getTimeAsString() {
		if (this.cmbMin.getSelectedIndex() == -1) {
			return null;
		} else {
			final int min = (Integer) this.cmbMin.getSelectedItem();
			final int hour = (Integer) this.cmbHour.getSelectedItem();
			return hour + ":" + min;
		}
	}

	// //////////////////////////////////////////////////////////////////
	@Override
	public Date getValue() {
		try {
			checkValue();
			return getTime();
		} catch (final ValidationException e) {
			return null;
		}
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		final JKLabel lbl = new JKLabel(":");
		lbl.setPreferredSize(new Dimension(8, 25));
		add(getCmbHour());
		add(lbl);
		add(getCmbMin());
	}

	/**
	 *
	 * @return
	 */
	public boolean isNull() {
		return this.cmbMin.getSelectedIndex() == -1 && this.cmbHour.getSelectedIndex() == -1;
	}

	/**
	 *
	 */
	@Override
	public void requestFocus() {
		this.cmbHour.requestFocus();
	}

	@Override
	public void reset() {
		setValue(this.defaultValue);
	}

	/**
	 *
	 *
	 */
	public void resetDate() {
		this.cmbMin.setSelectedIndex(-1);
		this.cmbHour.setSelectedIndex(-1);
	}

	@Override
	public void setComponentOrientation(final ComponentOrientation o) {
		// ignore this call by container.applyComponentOrientation
	};

	@Override
	public void setDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 *
	 * @param date
	 *            Date
	 */
	public void setTime(final Date date) {
		if (date != null) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			this.cmbMin.setSelectedItem(cal.get(Calendar.MINUTE));
			this.cmbHour.setSelectedItem(cal.get(Calendar.HOUR_OF_DAY));
		} else {
			this.cmbMin.setSelectedIndex(-1);
			this.cmbHour.setSelectedIndex(-1);
		}
	}

	// //////////////////////////////////////////////////////////////////
	@Override
	public void setValue(final Object value) {
		if (value == null) {
			setTime(null);
		} else if (value instanceof Date) {
			setTime((Date) value);
		} else {
			// TODO : add support for parsing setTime(DateTimeUtil.pa);
			setTime(null);
		}
	}

	@Override
	public String toString() {
		final SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
		return format.format(getTime());
	}
}
