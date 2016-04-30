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
	/**
	 *
	 */
	private static final long serialVersionUID = -7483362430963916031L;
	BindingComponent comp;
	private final Vector<Validator> validators = new Vector<Validator>();
	private Border originalBorder;;
	private String originalTooltip;
	private transient DataSource manager;
	private final Vector<ValueChangeListener> valueListeners = new Vector<ValueChangeListener>();

	// /////////////////////////////////////////////////////////////////////////

	public FSAbstractComponent(final BindingComponent comp) {
		this.comp = comp;
		init();
	}

	// /////////////////////////////////////////////////////////////////////////

	public void addValidator(final Validator validator) {
		int index;
		if ((index = getValidatorIndex(validator)) != -1) {
			this.validators.remove(index);
		}
		this.validators.add(validator);
	}

	// /////////////////////////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////////////
	public void addValueChangeListsner(final ValueChangeListener valueChangeListener) {
		this.valueListeners.add(valueChangeListener);
	}

	// /////////////////////////////////////////////////////////////////////////
	private void applyDataSource() {
		if (this.comp instanceof Container) {
			SwingUtility.applyDataSource((Container) this.comp, this.manager);
		}
	}

	// ///////////////////////////////////////////////////////////////////////
	// this should be protected , but guess why i made it like this!!!!!
	public void fireValueChangeListener(final Object oldValue, final Object newValue) {
		if (!GeneralUtility.equals(oldValue, newValue)) {
			// if (oldValue == null && newValue == null) {
			// return;
			// }
			// if (oldValue != null && newValue != null &&
			// oldValue.equals(newValue)) {
			// return;
			// }
			for (final ValueChangeListener valueChangeListener : this.valueListeners) {
				valueChangeListener.valueChanged(oldValue, newValue);
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////

	private String getComponentName() {
		return this.comp.getName() == null ? "" : this.comp.getName();
	}

	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	public DataSource getDataSource() {
		if (this.manager != null) {
			return this.manager;
		}
		if (this.comp instanceof Container) {
			final Container cont = ((Container) this.comp).getParent();
			if (cont instanceof DaoComponent) {
				return ((DaoComponent) cont).getDataSource();
			}
		}
		return DataSourceFactory.getDefaultDataSource();
	}

	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	public int getValidatorIndex(final Validator validator) {
		for (int i = 0; i < this.validators.size(); i++) {
			final Validator v = this.validators.get(i);
			if (v.getClass().getName().equals(validator.getClass().getName())) {
				return i;
			}
		}
		return -1;
	}

	private void init() {
		if (this.comp instanceof JComponent) {
			final JComponent com = (JComponent) this.comp;
			com.setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		}
		this.comp.addFocusListener(new FocusListener() {

			private Object oldValue;

			@Override
			public void focusGained(final FocusEvent e) {
				this.oldValue = FSAbstractComponent.this.comp.getValue();
			}

			@Override
			public void focusLost(final FocusEvent e) {
				final Object newValue = FSAbstractComponent.this.comp.getValue();
				fireValueChangeListener(this.oldValue, newValue);
				// }
			}
		});
	}

	// /////////////////////////////////////////////////////////////////////////
	public void removeValidator(final FSValidators validator) {
		final int validatorIndex = getValidatorIndex(validator);
		if (validatorIndex != -1) {
			this.validators.remove(validatorIndex);
		}
	}

	private void setBorder(final Border border) {
		if (this.originalBorder == null) {
			this.originalBorder = border;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	public void setDataSource(final DataSource manager) {
		this.manager = manager;
		applyDataSource();
	}

	private void setTooltipText(final String toolTipText) {
		if (this.originalTooltip == null) {
			this.originalTooltip = toolTipText;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	public void validateValue() throws UIValidationException {
		// the first time this method called , we shuld save the component
		// border
		// becaus on error , we will change the border for something else
		// and at some point , we want to restore the original border to what it
		// was
		setBorder(this.comp.getBorder());
		setTooltipText(this.comp.getToolTipText());
		final Problems problems = new Problems();
		for (final Validator val : this.validators) {
			val.validate(problems, Lables.get(getComponentName()), this.comp.getValue());
		}
		if (!problems.isEmpty()) {
			throw new UIValidationException(this.comp, problems);
		}
		this.comp.setBorder(this.originalBorder);
		this.comp.setToolTipText(this.originalTooltip);
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
