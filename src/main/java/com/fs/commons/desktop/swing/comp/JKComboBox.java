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
	private boolean transferFocusOnEnter = true;
	private boolean useTabEventForLostFocus = true;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object defaultValue;
	protected  FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private boolean required;
//	private boolean transfer;

	/**
	 * 
	 * 
	 */
	public JKComboBox() {
		init();
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
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
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_DELETE) {
					setSelectedIndex(-1);
				}
//				if (transferFocusOnEnter && e.getKeyChar() == KeyEvent.VK_ENTER) {
//					handleTransferFocus();
//					// JKComboBox.this.transferFocus();
//				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
	}

	/**
	 * @return the transferFocusOnEnter
	 */
	public boolean isTransferFocusOnEnter() {
		return transferFocusOnEnter;
	}

	/**
	 * @param transferFocusOnEnter
	 *            the transferFocusOnEnter to set
	 */
	public void setTransferFocusOnEnter(boolean transferFocusOnEnter) {
		this.transferFocusOnEnter = transferFocusOnEnter;
	}

	/**
	 * 
	 */
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
	public void setValue(Object o) {
		setSelectedItem(o);
	}

	@Override
	public void setSelectedIndex(int index) {
		// to avoid index outof bound exception
		super.setSelectedIndex(index >= getItemCount() ? -1 : index);
	}

	/**
	 * @throws ValidationException
	 * 
	 */
	public void checkEmpty() throws ValidationException {
		SwingValidator.checkEmpty(this);
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
	 * @return the useTabEventForLostFocus
	 */
	public boolean isUseTabEventForLostFocus() {
		return useTabEventForLostFocus;
	}

	/**
	 * @param useTabEventForLostFocus
	 *            the useTabEventForLostFocus to set
	 */
	public void setUseTabEventForLostFocus(boolean useTabEventForLostFocus) {
		this.useTabEventForLostFocus = useTabEventForLostFocus;
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public void reset() {
		setSelectedItem(defaultValue);
	}

	@Override
	public void clear() {
		setSelectedIndex(-1);
	}

	@Override
	public void filterValues(BindingComponent component) throws DaoException {
	}

	@Override
	public void addValidator(Validator validator) {
		fsWrapper.addValidator(validator);
	}

	@Override
	public void validateValue() throws ValidationException {
		fsWrapper.validateValue();
	}

	public void setRequired(boolean required) {
		addValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		this.required = required;
	}

	public boolean isRequired() {
		return required;
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

	public void addItems(List objects ) {
		for (Object object : objects) {
			addItem(object);
		}
	}

	@Override
	public void setAutoTransferFocus(boolean transfer) {
		this.transferFocusOnEnter = transfer;
	}

	@Override
	public boolean isAutoTransferFocus() {
		return transferFocusOnEnter;
	}
}
