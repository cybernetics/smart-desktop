package com.fs.commons.desktop.swing.comp;

import java.awt.Container;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.border.Border;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.desktop.validation.builtin.FSValidators;
import com.fs.commons.desktop.validation.exception.UIValidationException;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;

/**
 * If Java would support multiple inheritance , this class would be another
 * super class for all our custom components , but since it doesn't , this class
 * will be composed inside each component to encalsupate common business
 * 
 * @author JK
 * 
 */
public class FSAbstractComponent implements Serializable {
	BindingComponent comp;
	private Vector<Validator> validators = new Vector<Validator>();
	private Border originalBorder;;
	private String originalTooltip;
	private transient DataSource manager;
	private Vector<ValueChangeListener> valueListeners = new Vector<ValueChangeListener>();

	// /////////////////////////////////////////////////////////////////////////

	public FSAbstractComponent(BindingComponent comp) {
		this.comp = comp;
		init();
	}

	// /////////////////////////////////////////////////////////////////////////

	private void init() {
		if (comp instanceof JComponent) {
			JComponent com = (JComponent) comp;
			com.setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		}
		comp.addFocusListener(new FocusListener() {

			private Object oldValue;

			@Override
			public void focusGained(FocusEvent e) {
				oldValue = comp.getValue();
			}

			@Override
			public void focusLost(FocusEvent e) {
				Object newValue = comp.getValue();
				fireValueChangeListener(oldValue, newValue);
				// }
			}
		});
	}

	// /////////////////////////////////////////////////////////////////////////

	public void addValidator(Validator validator) {
		int index;
		if ((index = getValidatorIndex(validator)) != -1) {
			validators.remove(index);
		}
		validators.add(validator);
	}

	// /////////////////////////////////////////////////////////////////////////
	public int getValidatorIndex(Validator validator) {
		for (int i = 0; i < validators.size(); i++) {
			Validator v = validators.get(i);
			if (v.getClass().getName().equals(validator.getClass().getName())) {
				return i;
			}
		}
		return -1;
	}

	// /////////////////////////////////////////////////////////////////////////
	public void validateValue() throws UIValidationException {
		// the first time this method called , we shuld save the component
		// border
		// becaus on error , we will change the border for something else
		// and at some point , we want to restore the original border to what it
		// was
		setBorder(comp.getBorder());
		setTooltipText(comp.getToolTipText());
		Problems problems = new Problems();
		for (Validator val : validators) {
			val.validate(problems, Lables.get(getComponentName()), comp.getValue());
		}
		if (!problems.isEmpty()) {
			throw new UIValidationException(comp, problems);
		}
		comp.setBorder(originalBorder);
		comp.setToolTipText(originalTooltip);
	}

	// /////////////////////////////////////////////////////////////////////////

	private String getComponentName() {
		return comp.getName() == null ? "" : comp.getName();
	}

	// /////////////////////////////////////////////////////////////////////////

	private void setTooltipText(String toolTipText) {
		if (this.originalTooltip == null) {
			this.originalTooltip = toolTipText;
		}
	}

	// /////////////////////////////////////////////////////////////////////////

	private void setBorder(Border border) {
		if (this.originalBorder == null) {
			this.originalBorder = border;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	public void removeValidator(FSValidators validator) {
		int validatorIndex = getValidatorIndex(validator);
		if (validatorIndex != -1) {
			validators.remove(validatorIndex);
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	public void setDataSource(DataSource manager) {
		this.manager = manager;
		applyDataSource();
	}

	// /////////////////////////////////////////////////////////////////////////
	private void applyDataSource() {
		if (comp instanceof Container) {
			SwingUtility.applyDataSource((Container) comp, manager);
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	public DataSource getDataSource() {
		if (manager != null) {
			return manager;
		}
		if (comp instanceof Container) {
			Container cont = ((Container) comp).getParent();
			if (cont instanceof DaoComponent) {
				return ((DaoComponent) cont).getDataSource();
			}
		}
		return DataSourceFactory.getDefaultDataSource();
	}

	// ///////////////////////////////////////////////////////////////////////
	public void addValueChangeListsner(ValueChangeListener valueChangeListener) {
		valueListeners.add(valueChangeListener);
	}

	// ///////////////////////////////////////////////////////////////////////
	// this should be protected , but guess why i made it like this!!!!!
	public void fireValueChangeListener(Object oldValue, Object newValue) {
		if (!GeneralUtility.equals(oldValue, newValue)) {
			// if (oldValue == null && newValue == null) {
			// return;
			// }
			// if (oldValue != null && newValue != null &&
			// oldValue.equals(newValue)) {
			// return;
			// }
			for (ValueChangeListener valueChangeListener : valueListeners) {
				valueChangeListener.valueChanged(oldValue, newValue);
			}
		}
	}

	// public void filterValues(final BindingComponent targetComponent){
	// if(comp.getFocusListeners()){
	//
	// }
	// comp.addFocusListener(new FocusAdapter() {
	// @Override
	// public void focusLost(FocusEvent e) {
	// try {
	// targetComponent.filterValues(comp);
	// } catch (DaoException e1) {
	// ExceptionUtil.handleException(e1);
	// }
	// }
	// });
	//
	// }
}
