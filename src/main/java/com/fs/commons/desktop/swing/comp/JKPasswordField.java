package com.fs.commons.desktop.swing.comp;

import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPasswordField;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;

public class JKPasswordField extends JPasswordField implements BindingComponent<String> {
	private FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// static Border focusBorder =
	// BorderFactory.createLineBorder(SystemColor.infoText);
	// static Border lostFocusBorder =
	// BorderFactory.createLineBorder(SystemColor.activeCaptionBorder);
	static Dimension dim = new Dimension(200, 30);
	private String defaultValue;

	private boolean transfer;

	public JKPasswordField() {
		this(10,10);
	}
	
	public JKPasswordField(int maxlength, int col) {
		super(new TextDocument(maxlength), "", col);
		init();
	}

	/**
	 * init
	 */
	private void init() {
		setPreferredSize(dim);
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		SwingUtility.setFont(this);
		// setBorder(lostFocusBorder);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					transferFocus();
				}
			}
		});

		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// setBorder(focusBorder);
				setSelectionStart(0);
				setSelectionEnd(getText().length());
			}

			@Override
			public void focusLost(FocusEvent e) {
				// setBorder(lostFocusBorder);
			}
		});
	}

	public String getValue() {
		return getText();
	}

	/**
	 * 
	 */
	public void setValue(String value) {
		setText(value);
	}

	/**
	 * 
	 * @throws ValidationException
	 */
	public void checkEmpty() throws ValidationException {
		SwingValidator.checkEmpty(this);
	}

	/**
	 * 
	 */
	public void reset() {
		setText(getDefaultValue() != null ? getDefaultValue() : "");
	}

	/**
	 * 
	 * @param defaultValue
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void filterValues(BindingComponent component) {
		// TODO Auto-generated method stub

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
	public void setColumns(int columns) {
		// Just ignore to take the preffered size
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
