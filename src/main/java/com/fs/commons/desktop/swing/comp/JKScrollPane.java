package com.fs.commons.desktop.swing.comp;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;

public class JKScrollPane extends JScrollPane implements BindingComponent {
	private FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	private boolean transfer;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JKScrollPane() {
		super();
		init();
	}

	private void init() {
		setOpaque(false);
		getViewport().setOpaque(false);
		setFocusable(false);
		getViewport().setBackground(SwingUtility.getDefaultBackgroundColor());
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
	}

	public JKScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
		init();
	}

	public JKScrollPane(Component view) {
		super(view);
		init();
	}

	public JKScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
		init();
	}

	@Override
	public void setValue(Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultValue(Object t) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getDefaultValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
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
	public void filterValues(BindingComponent comp1) throws DaoException {
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
