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

	private Object defaultValue;
	private FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	private Date minDate;

	private Date maxDate;

	private boolean transfer=true;

	/**
	 * 
	 */
	public JKDate() {
		this("Date");
	}

	/**
	 * This is the default constructor
	 */
	public JKDate(String lableKey) {
		this(lableKey, 1930, 2020);
		setDate(new Date());
	}

	@Override
	public void setName(String name) {
		super.setName(Lables.get(name));
	}

	/**
	 * 
	 * @param date
	 */
	public JKDate(Date date) {
		this("");
		setDate(date);
	}

	/**
	 * 
	 * @param lableKey
	 * @param from
	 * @param to
	 */
	public JKDate(String lableKey, int from, int to) {
		super();
		initialize();
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

	private JFormattedTextField getEditor() {
		return ((JTextFieldDateEditor) getDateEditor());
	}

	/**
	 * 
	 * 
	 */
	public void resetDate() {
		setDate(null);
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
	 * @return
	 */
	public String toSimpleString() {
		return FormatUtil.formatDate(getDate(), FormatUtil.PATTERN_SHORT_DATE);
	}

	public java.sql.Date getSqlDate() {
		Date date = getDate();
		if (date == null) {
			return null;
		}
		return new java.sql.Date(date.getTime());
	}

	/**
	 * 
	 * @param pattern
	 * @return
	 */
	public String toString(String pattern) {
		// SimpleDateFormat format = new SimpleDateFormat(pattern);
		return FormatUtil.formatDate(getDate(), pattern);

	}

	// @Override
	// public void setEnabled(boolean enabled) {
	// cmbDay.setEnabled(enabled);
	// cmbMonth.setEnabled(enabled);
	// cmbYear.setEnabled(enabled);
	// }

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JKDate d = new JKDate();
		d.setDate(new Date());
		JKPanel pnl=new JKPanel(d);
		SwingUtility.testPanel(pnl);
		
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

	// ////////////////////////////////////////////////////////////////////////
	public void setValue(Object value) {
		// System.out.println("setValue at FSDate = "+value+"with class  "+(value==null?"":
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
			} catch (ParseException e) {
				ExceptionUtil.handleException(e);
			}

		}
	}

	public void setDefaultValue(Object defaultValue) {
		if (defaultValue != null) {
			if (defaultValue instanceof String && defaultValue.toString().toLowerCase().equals("now")) {
				// System.out.println("defult value from JKDate to now: " +
				// defaultValue);
				try {
					this.defaultValue = DaoUtil.getSystemDate();				
				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				}
				return;
			}
		}
		this.defaultValue = defaultValue;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void reset() {
		setValue(getDefaultValue());
	}

	public void clear() {
		setValue(null);
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
	public Object getValue() {
		Date date = getDate();
		return date;
	}

	public boolean isEmpty() {
		if (((JFormattedTextField) getDateEditor()).getText().equals("")) {
			return true;
		}
		return false;
	}

	public void checkValue() throws ValidationException {
		if (getValue() == null && !isEmpty()) {
			throw new ValidationException(this, new Problem("INVALID_DATE_VALUE", Severity.FATAL));
		}
	}

	@Override
	public void addValidator(Validator validator) {
		fsWrapper.addValidator(validator);
	}

	@Override
	public void validateValue() throws ValidationException {
		fsWrapper.validateValue();
		checkValue();
	}

	public void setRequired(boolean required) {
		if (required) {
			addValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		} else {
			fsWrapper.removeValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		}
	}

	// public void setMinSelectableDate(Date date) {
	// getDateEditor().setMinSelectableDate(date);
	// }

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
		getDateEditor().setMinSelectableDate(minDate);
	}

	public void setMinDate(String date) {
		try {
			if (date != null && !date.trim().equals("")) {
				setMinDate(DateTimeUtil.parseShortDate(date));
			}
		} catch (ParseException e) {
			ExceptionUtil.handleException(e);
		}
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
		getDateEditor().setMaxSelectableDate(maxDate);
	}

	public void setMaxDate(String maxDate) {
		try {
			if (maxDate != null && !maxDate.trim().equals("")) {
				setMaxDate(DateTimeUtil.parseShortDate(maxDate));
			}
		} catch (ParseException e) {
			ExceptionUtil.handleException(e);
		}
	}

	public Date getMinDate() {
		return minDate;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	// This method is called when editing is completed.
	// It must return the new value to be stored in the cell.
	public Object getCellEditorValue() {
		return getValue();
	}

	@Override
	public void requestFocus() {
		super.requestFocus();
		getEditor().requestFocus();
	}

	@Override
	public void filterValues(BindingComponent comp1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setBackground(Color bg) {
		if (getEditor() == null) {
			// when called from constructor
			super.setBackground(bg);
		} else {
			getEditor().setBackground(bg);
		}
	}

	@Override
	public synchronized void addKeyListener(KeyListener l) {
		getEditor().addKeyListener(l);
	}

	@Override
	public void setDataSource(DataSource manager) {
		fsWrapper.setDataSource(manager);
	}

	@Override
	public DataSource getDataSource() {
		return fsWrapper.getDataSource();
	}

	@Override
	public void addValueChangeListener(ValueChangeListener listener) {
		fsWrapper.addValueChangeListsner(listener);
	}

	// @Override
	// protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int
	// condition, boolean pressed) {
	// return getEditor().
	// }
	
	public String toMySqlString(){
		return FormatUtil.formatMysqlDate(getDate());
	}
	
	public void checkAfterToday() throws ValidationException{
		if(getDate().after(new Date())){
			throw new ValidationException("DATE_IS_AFTER_TODAY");
		}
	}

	@Override
	public void addActionListener(ActionListener actionListener) {
		
	}

	@Override
	public void setAutoTransferFocus(boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public boolean isAutoTransferFocus() {
		return transfer;
	}
}
