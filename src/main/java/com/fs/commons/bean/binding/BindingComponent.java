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
	public void addActionListener(ActionListener actionListener);

	public void addFocusListener(FocusListener l);

	public void addKeyListener(KeyListener listener);

	// /////////////////////////////////////////////////////////////////////
	public void addValidator(Validator validator);

	public void addValueChangeListener(ValueChangeListener listener);

	// /////////////////////////////////////////////////////////////////////
	public void clear();

	public void filterValues(BindingComponent comp1) throws DaoException;

	public ActionMap getActionMap();

	// /////////////////////////////////////////////////////////////////////
	public Border getBorder();

	public int getComponentCount();

	// /////////////////////////////////////////////////////////////////////
	public T getDefaultValue();

	public FocusListener[] getFocusListeners();

	public InputMap getInputMap(int whenInFocusedWindow);

	// /////////////////////////////////////////////////////////////////////
	public String getName();

	public Container getParent();

	// /////////////////////////////////////////////////////////////////////
	public String getToolTipText();

	// /////////////////////////////////////////////////////////////////////
	public T getValue();//
	// /////////////////////////////////////////////////////////////////////

	public boolean isAutoTransferFocus();

	public boolean isEnabled();

	public boolean isVisible();

	// /////////////////////////////////////////////////////////////////////
	public void requestFocus();

	// /////////////////////////////////////////////////////////////////////
	public void reset();

	public void setAutoTransferFocus(boolean transfer);

	// /////////////////////////////////////////////////////////////////////
	public void setBorder(Border border);

	public void setDefaultValue(T t);

	public void setEnabled(boolean enable);

	public void setFocusable(boolean b);

	public void setFont(java.awt.Font font);

	public void setName(String name);

	public void setPreferredSize(Dimension dim1);

	// /////////////////////////////////////////////////////////////////////
	public void setToolTipText(String text);

	// /////////////////////////////////////////////////////////////////////
	public void setValue(T value);

	public void transferFocus();

	// /////////////////////////////////////////////////////////////////////
	public void validateValue() throws ValidationException;
}
