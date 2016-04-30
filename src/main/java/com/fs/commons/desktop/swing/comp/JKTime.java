/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      10/07/2008     Jamil Shreet    -Modify the following methods : 
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

	private JKComboBox cmbMin = null;

	private JKComboBox cmbHour = null;

	private int toHour;

	private int fromHour;

	private Object defaultValue;

	/**
	 * This is the default constructor
	 * 
	 * @param lableKey
	 *            String
	 */
	public JKTime(String lableKey) {
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
	public JKTime(String lableKey, int fromHour, int toHour) {
		super();
		this.fromHour = fromHour;
		this.toHour = toHour;
		// lbl = new JKLabel(lableKey);
		initialize();
	}

	public JKTime() {

	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		JKLabel lbl = new JKLabel(":");
		lbl.setPreferredSize(new Dimension(8, 25));
		add(getCmbHour());
		add(lbl);
		add(getCmbMin());
	}

	/**
	 * This method initializes cmbMin
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbMin() {
		if (cmbMin == null) {
			cmbMin = new JKComboBox();
			for (int i = 0; i < 60; i += 5) {
				cmbMin.addItem(new Integer(i));
			}
		}
		return cmbMin;
	}

	/**
	 * This method initializes cmbHour
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbHour() {
		if (cmbHour == null) {
			cmbHour = new JKComboBox();
			for (int i = fromHour; i <= toHour; i++) {
				cmbHour.addItem(new Integer(i));
			}
		}
		return cmbHour;
	}

	/**
	 * 
	 * 
	 */
	public void resetDate() {
		cmbMin.setSelectedIndex(-1);
		cmbHour.setSelectedIndex(-1);
	}

	/**
	 * 
	 * @param date
	 *            Date
	 */
	public void setTime(Date date) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cmbMin.setSelectedItem(cal.get(Calendar.MINUTE));
			cmbHour.setSelectedItem(cal.get(Calendar.HOUR_OF_DAY));
		} else {
			cmbMin.setSelectedIndex(-1);
			cmbHour.setSelectedIndex(-1);
		}
	}

	/**
	 * 
	 * @return java.util.Date
	 */
	public Date getTime() {
		if (isNull()) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		int min = (Integer) cmbMin.getSelectedItem();
		int hour = (Integer) cmbHour.getSelectedItem();

		cal.setTimeInMillis(0);

		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isNull() {
		return cmbMin.getSelectedIndex() == -1 && cmbHour.getSelectedIndex() == -1;
	}

	/**
	 * @1.1
	 * @return String
	 */
	public String getTimeAsString() {
		if (cmbMin.getSelectedIndex() == -1) {
			return null;
		} else {
			int min = (Integer) cmbMin.getSelectedItem();
			int hour = (Integer) cmbHour.getSelectedItem();
			return hour + ":" + min;
		}
	}

	/**
	 * @throws ValidationException
	 * 
	 * 
	 */
	public void checkValue() throws ValidationException {
		SwingValidator.checkEmpty(cmbHour);
		SwingValidator.checkEmpty(cmbMin);
	}

	/**
	 * 
	 */
	@Override
	public void requestFocus() {
		cmbHour.requestFocus();
	}

	public static void main(String[] args) {
		// JPanel pnl=new JPanel();
		JKTime t = new JKTime("Time ");
		// pnl.add(t);
		t.setTime(new Date());
		// SwingUtil.testPanel(pnl);
		System.out.println(t.getTime());
	}

	// //////////////////////////////////////////////////////////////////
	public Date getValue() {
		try {
			checkValue();
			return getTime();
		} catch (ValidationException e) {
			return null;
		}
	}

	// //////////////////////////////////////////////////////////////////
	public void setValue(Object value) {
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
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Object getDefaultValue() {
		return defaultValue;
	};

	@Override
	public void clear() {
		setValue(null);
	}

	@Override
	public void reset() {
		setValue(defaultValue);
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
		return format.format(getTime());
	}

	@Override
	public void setComponentOrientation(ComponentOrientation o) {
		// ignore this call by container.applyComponentOrientation
	}
}
