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
	private Font font = new Font("arial", Font.PLAIN, 12);
	boolean defaultValue;
	private FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private boolean transfer;

	/**
	 * @return the defaultValue
	 */
	public boolean isDefaultValue() {
		return defaultValue;
	}

	public JKCheckBox() {
		this("");
	}

	/**
	 * 
	 * @param caption
	 *            String
	 */
	public JKCheckBox(String caption) {
		 super(Lables.get(caption));
		setBackground(SwingUtility.getDefaultBackgroundColor());
		init();
	}

	public JKCheckBox(String caption, boolean checked) {
		this(caption);
		setSelected(checked);
	}

	/**
	 * 
	 */
	void init() {
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		setHorizontalAlignment(JKCheckBox.LEADING);
		setFont(font);
		addKeyListener(new KeyAdapter() {
			@Override
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

	/**
	 * 
	 */
	public void reset() {
		setSelected(defaultValue);
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
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
	public void clear() {
		setSelected(false);
	}

	@Override
	public void filterValues(BindingComponent component) {

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
	}}
