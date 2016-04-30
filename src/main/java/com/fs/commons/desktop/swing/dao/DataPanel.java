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
	public DataPanel(Class bindingBeanClass) throws BeanUtilException {
		setBorder(BorderFactory.createEtchedBorder());
		binding = new ModelViewBiding(bindingBeanClass, this);
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
	 * @return JComponent
	 */
	public BindingComponent getIdField() {
		return components.get(0);
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
	 * @param value
	 */
	public void setIdValue(Object value) {
		/** @todo add approp support for other comoponents types */
		((BindingComponent) getIdField()).setValue(value);
	}

	/**
	 * 
	 * @param enable
	 *            boolean
	 */
	public void enableIdField(boolean enable) {
		getIdField().setEnabled(enable);
		if (enable) {
			getIdField().requestFocus();
		}		
	}

	/**
	 * 
	 * @param enable
	 *            boolean
	 */
	public boolean enableDataFields(boolean enable) {
		if (components.size() > 1) {// some times the table only contains one
			// value which is the id , like the re_year
			// table in UM
			for (int i = 1; i < components.size(); i++) {
				if (components.get(i) instanceof Container) {
					SwingUtility.enableContainer((Container) components.get(i), enable);
				}
				components.get(i).setEnabled(enable);
			}
			if (enable) {
				components.get(1).requestFocus();
			} else {
				components.get(components.size() - 1).transferFocus();
			}			
		}
		return true;		
	}

	/**
	 * 
	 * @param enable
	 *            boolean
	 */
	public void enableAllComponents(boolean enable) {
		enableIdField(enable);
		enableDataFields(enable);
	}

	/**
	 * 
	 */
	public abstract void addComponents();

	/**
	 * 
	 * @param comp
	 */
	protected void addComponent(BindingComponent comp) {
		addComponent(comp,"");		
	}
	

	/**
	 * First component will be considered as the id
	 * 
	 * @param bindingComponent
	 *            JComponent
	 */
	public void addComponent(BindingComponent bindingComponent, String propertyName) {
		components.add(bindingComponent);
		if (bindingComponent instanceof BindingComponent) {
			addBindingComponent((BindingComponent) bindingComponent, propertyName);
		}
	}

	/**
	 * 
	 * @param comp
	 * @param propertyName
	 */
	protected void addBindingComponent(BindingComponent comp, String propertyName) {
		bindingComponents.put(propertyName, comp);
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
		for (int i = 0; i < components.size(); i++) {
			if (components.get(i) instanceof Container) {
				resetComponents((Container) components.get(i));
			}
			SwingUtility.resetComponent(components.get(i));
		}
		if (getIdField().isVisible() && getIdField().isEnabled()) {
			getIdField().requestFocus();
		} else {
			components.get(1).requestFocus();
		}
	}

	/**
	 * 
	 * @param container
	 *            Container
	 * @throws DaoException
	 */
	protected void resetComponents(Container container) throws DaoException {
		Component[] components = container.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof Container) {
				resetComponents((Container) components[i]);
			}
			SwingUtility.resetComponent(components[i]);
		}
	}

	/**
	 * 
	 * @param component
	 *            Component
	 * @throws DaoException
	 */
//	private static void resetComponent(Component component) throws DaoException {
//		if (component instanceof JTextComponent) {
//			((JTextComponent) component).setText("");
//		} else if (component instanceof DaoComboBox) {
//			((DaoComboBox) component).reloadData();
//			//((DaoComboBox) component).reset();
//		} else if (component instanceof DaoComboWithManagePanel) {
//			//((DaoComboWithManagePanel) component).reloadData();
//			((DaoComboWithManagePanel) component).getCombo().setSelectedIndex(-1);
//		} else if (component instanceof JComboBox) {
//			((JComboBox) component).setSelectedIndex(-1);
//		} else if (component instanceof JKDate) {
//			((JKDate) component).setDate(new Date());
//		} else if (component instanceof JList) {
//			((JList) component).setSelectedIndex(-1);
//		} else if (component instanceof JRadioButton) {
//			((JRadioButton) component).setSelected(false);
//		} else if (component instanceof JKCheckBox) {
//			((JKCheckBox) component).reset();
//		} else if (component instanceof JKPanel) {
//			((JKPanel) component).resetComponents();
//		}
//	}

	/**
	 * 
	 */
	public BindingComponent getViewComponent(String viewName) {
		return bindingComponents.get(viewName);
	}

	/**
	 * @throws BeanUtilException
	 * 
	 * 
	 */
	public void modelToView(Object bean) {
		try {
			binding.modelToView(bean);
		} catch (BeanUtilException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @return
	 * @throws BeanUtilException
	 */
	public Object viewToModel(){
		try {
			return binding.viewToModel();
		} catch (BeanUtilException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 */
	public Map getViewComponents() {
		return bindingComponents;
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
