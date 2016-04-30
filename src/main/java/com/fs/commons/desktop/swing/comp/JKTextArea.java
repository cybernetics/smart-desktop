package com.fs.commons.desktop.swing.comp;

import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.text.Document;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;

public class JKTextArea extends JTextArea implements BindingComponent<String>, JKScrollable {

	private FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	private static final long serialVersionUID = 1L;
	private String defaultValue;

	private boolean transfer;

	public JKTextArea() {

	}

	public JKTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		init();
	}

	public JKTextArea(Document doc) {
		super(doc);
		init();
	}

	public JKTextArea(int rows, int columns) {
		super(rows, columns);
		init();
	}

	public JKTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		init();
	}

	public JKTextArea(String text) {
		super(text);
		init();
	}

	void init() {
		// setFont(JKTextField.DEFAULT_FONT);
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
	}

	@Override
	public String getValue() {
		return getText();
	}

	@Override
	public void setValue(String value) {
		if (value != null) {
			setText(value.toString());
		} else {
			setText("");
		}
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public void reset() {
		setValue(defaultValue);
	}

	@Override
	public void clear() {
		setValue(null);
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
	public void addActionListener(ActionListener actionListener) {
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
