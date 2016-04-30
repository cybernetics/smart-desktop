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
package com.fs.commons.desktop.swing.dao;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.bean.binding.ModelViewBiding;
import com.fs.commons.bean.binding.ViewContainer;
import com.fs.commons.bean.util.BeanUtilException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public abstract class DataPanel extends JKPanel implements DaoActionsListener, ViewContainer {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final short ARABIC = 1;

	public static final short ENGLISH = 2;

	ArrayList<BindingComponent> components = new ArrayList<BindingComponent>();

	Hashtable<String, BindingComponent> bindingComponents = new Hashtable<String, BindingComponent>();

	ModelViewBiding binding;

	/**
	 *
	 */
	public DataPanel() {
		setBorder(BorderFactory.createEtchedBorder());
	}

	/**
	 *
	 * @param bindingBeanClass
	 * @throws BeanUtilException
	 */
	public DataPanel(final Class bindingBeanClass) throws BeanUtilException {
		setBorder(BorderFactory.createEtchedBorder());
		this.binding = new ModelViewBiding(bindingBeanClass, this);
	}

	/**
	 *
	 * @param comp
	 * @param propertyName
	 */
	protected void addBindingComponent(final BindingComponent comp, final String propertyName) {
		this.bindingComponents.put(propertyName, comp);
	}

	/**
	 *
	 * @param comp
	 */
	protected void addComponent(final BindingComponent comp) {
		addComponent(comp, "");
	}

	/**
	 * First component will be considered as the id
	 *
	 * @param bindingComponent
	 *            JComponent
	 */
	public void addComponent(final BindingComponent bindingComponent, final String propertyName) {
		this.components.add(bindingComponent);
		if (bindingComponent instanceof BindingComponent) {
			addBindingComponent(bindingComponent, propertyName);
		}
	}

	/**
	 *
	 */
	public abstract void addComponents();

	/**
	 *
	 * @param enable
	 *            boolean
	 */
	public void enableAllComponents(final boolean enable) {
		enableIdField(enable);
		enableDataFields(enable);
	}

	/**
	 *
	 * @param enable
	 *            boolean
	 */
	public boolean enableDataFields(final boolean enable) {
		if (this.components.size() > 1) {// some times the table only contains
											// one
			// value which is the id , like the re_year
			// table in UM
			for (int i = 1; i < this.components.size(); i++) {
				if (this.components.get(i) instanceof Container) {
					SwingUtility.enableContainer((Container) this.components.get(i), enable);
				}
				this.components.get(i).setEnabled(enable);
			}
			if (enable) {
				this.components.get(1).requestFocus();
			} else {
				this.components.get(this.components.size() - 1).transferFocus();
			}
		}
		return true;
	}

	/**
	 *
	 * @param enable
	 *            boolean
	 */
	public void enableIdField(final boolean enable) {
		getIdField().setEnabled(enable);
		if (enable) {
			getIdField().requestFocus();
		}
	}

	/**
	 *
	 * @return JComponent
	 */
	public BindingComponent getIdField() {
		return this.components.get(0);
	}

	/**
	 *
	 * @return JComponent
	 */
	public int getIdFieldValueAsInteger() {
		return Integer.parseInt(getIdValue());
	}

	/**
	 *
	 * @return String
	 */
	public String getIdValue() {
		/** @todo add approp support for other comoponents types */
		return ((JTextField) getIdField()).getText();
	}

	/**
	 *
	 */
	@Override
	public BindingComponent getViewComponent(final String viewName) {
		return this.bindingComponents.get(viewName);
	}

	/**
	 *
	 */
	@Override
	public Map getViewComponents() {
		return this.bindingComponents;
	}

	/**
	 * @throws BeanUtilException
	 *
	 *
	 */
	public void modelToView(final Object bean) {
		try {
			this.binding.modelToView(bean);
		} catch (final BeanUtilException e) {
			throw new RuntimeException(e);
		}
	}

	// /**
	// *
	// * @param comp
	// * JComponent
	// */
	// public void removeComponent(JComponent comp) {
	// components.remove(comp);
	// bindingComponents.g
	// }

	/**
	 * @throws DaoException
	 *
	 */
	@Override
	public void resetComponents() throws DaoException {
		for (int i = 0; i < this.components.size(); i++) {
			if (this.components.get(i) instanceof Container) {
				resetComponents((Container) this.components.get(i));
			}
			SwingUtility.resetComponent(this.components.get(i));
		}
		if (getIdField().isVisible() && getIdField().isEnabled()) {
			getIdField().requestFocus();
		} else {
			this.components.get(1).requestFocus();
		}
	}

	/**
	 *
	 * @param container
	 *            Container
	 * @throws DaoException
	 */
	protected void resetComponents(final Container container) throws DaoException {
		final Component[] components = container.getComponents();
		for (final Component component2 : components) {
			if (component2 instanceof Container) {
				resetComponents((Container) component2);
			}
			SwingUtility.resetComponent(component2);
		}
	}

	/**
	 *
	 * @param component
	 *            Component
	 * @throws DaoException
	 */
	// private static void resetComponent(Component component) throws
	// DaoException {
	// if (component instanceof JTextComponent) {
	// ((JTextComponent) component).setText("");
	// } else if (component instanceof DaoComboBox) {
	// ((DaoComboBox) component).reloadData();
	// //((DaoComboBox) component).reset();
	// } else if (component instanceof DaoComboWithManagePanel) {
	// //((DaoComboWithManagePanel) component).reloadData();
	// ((DaoComboWithManagePanel) component).getCombo().setSelectedIndex(-1);
	// } else if (component instanceof JComboBox) {
	// ((JComboBox) component).setSelectedIndex(-1);
	// } else if (component instanceof JKDate) {
	// ((JKDate) component).setDate(new Date());
	// } else if (component instanceof JList) {
	// ((JList) component).setSelectedIndex(-1);
	// } else if (component instanceof JRadioButton) {
	// ((JRadioButton) component).setSelected(false);
	// } else if (component instanceof JKCheckBox) {
	// ((JKCheckBox) component).reset();
	// } else if (component instanceof JKPanel) {
	// ((JKPanel) component).resetComponents();
	// }
	// }

	/**
	 *
	 * @param value
	 */
	public void setIdValue(final Object value) {
		/** @todo add approp support for other comoponents types */
		getIdField().setValue(value);
	}

	/**
	 *
	 * @throws ValidationException
	 * @param validateId
	 *            boolean
	 * @throws DaoException
	 */
	public abstract void validateAddData(boolean validateId) throws ValidationException, DaoException;

	/**
	 *
	 * @throws ValidationException
	 * @throws DaoException
	 */
	public abstract void validateUpdateData() throws ValidationException, DaoException;

	/**
	 *
	 * @return
	 * @throws BeanUtilException
	 */
	public Object viewToModel() {
		try {
			return this.binding.viewToModel();
		} catch (final BeanUtilException e) {
			throw new RuntimeException(e);
		}
	}

	// @Override
	// public void requestFocus() {
	// for (int i = 0; i < components.size(); i++) {
	// if(components.get(i).isEnabled()){
	// components.get(i).requestFocus();
	// return;
	// }
	//
	// }
	// }
}
