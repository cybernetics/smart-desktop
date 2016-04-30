package com.fs.commons.bean.binding;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.io.Serializable;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.border.Border;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.comp.DaoComponent;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;

public interface BindingComponent<T> extends DaoComponent, FormField, Serializable {
	// /////////////////////////////////////////////////////////////////////
	public void setValue(T value);

	// /////////////////////////////////////////////////////////////////////
	public T getValue();//
	// /////////////////////////////////////////////////////////////////////

	public void setDefaultValue(T t);

	// /////////////////////////////////////////////////////////////////////
	public T getDefaultValue();

	// /////////////////////////////////////////////////////////////////////
	public void reset();

	// /////////////////////////////////////////////////////////////////////
	public void clear();

	// /////////////////////////////////////////////////////////////////////
	public void addValidator(Validator validator);

	// /////////////////////////////////////////////////////////////////////
	public void validateValue() throws ValidationException;

	// /////////////////////////////////////////////////////////////////////
	public String getName();

	// /////////////////////////////////////////////////////////////////////
	public void requestFocus();

	// /////////////////////////////////////////////////////////////////////
	public Border getBorder();

	// /////////////////////////////////////////////////////////////////////
	public void setBorder(Border border);

	// /////////////////////////////////////////////////////////////////////
	public void setToolTipText(String text);

	// /////////////////////////////////////////////////////////////////////
	public String getToolTipText();

	public void filterValues(BindingComponent comp1) throws DaoException;

	public void addFocusListener(FocusListener l);

	public void setName(String name);

	public int getComponentCount();

	public void setEnabled(boolean enable);

	public void transferFocus();

	public boolean isEnabled();

	public boolean isVisible();

	public void setFocusable(boolean b);

	public void setPreferredSize(Dimension dim1);

	public void addKeyListener(KeyListener listener);

	public ActionMap getActionMap();

	public InputMap getInputMap(int whenInFocusedWindow);

	public void setFont(java.awt.Font font);

	public void addValueChangeListener(ValueChangeListener listener);

	public Container getParent();

	public FocusListener[] getFocusListeners();

	public void addActionListener(ActionListener actionListener);

	public void setAutoTransferFocus(boolean transfer);
	
	public boolean isAutoTransferFocus();
}
