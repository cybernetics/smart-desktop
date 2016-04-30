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

	/**
	 * 
	 */
	private FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	private static final long serialVersionUID = 1L;
	private boolean defaultValue;
	private Font font = new Font("Arial", Font.PLAIN, 10);

	private boolean transfer;

	public JKRadioButton() {
		this("");
	}

	/**
	 * @param caption
	 *            String
	 */
	public JKRadioButton(String caption) {
		super(caption, true);
		setPreferredSize(null);
		setOpaque(false);
		init();
	}

	public JKRadioButton(String string, boolean focusable) {
		this(string);
		setFocusable(focusable);
	}

	/**
	 * 
	 */
	void init() {
		setFont(font);
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					transferFocus();
				}
			}
		});
	}

	/**
	 * 
	 */
	public Boolean getValue() {
		return isSelected();
	}

	@Override
	public Boolean getDefaultValue() {
		return defaultValue;
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
	public void reset() {
		setSelected(defaultValue);
	}

	@Override
	public void clear() {
		setSelected(false);
	}

	@Override
	public void setValue(Object value) {
		if (value != null) {
			setSelected(Boolean.parseBoolean(value.toString()));
		}
	}

	@Override
	public void addValidator(Validator validator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateValue() throws ValidationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void filterValues(BindingComponent comp1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setText(String text) {
		super.setText(Lables.get(text));
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
		// TODO Auto-generated method stub
		
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
