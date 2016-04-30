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
package com.fs.commons.desktop.validation.ui;

import java.awt.EventQueue;
import java.util.EventListener;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

import com.fs.commons.desktop.validation.Problem;
import com.fs.commons.desktop.validation.Problems;

/**
 * Abstract base class for listeners which listen on some component, and which
 * are added to a ValidationGroup. Implement whatever listener interface is
 * necessary, add it as a listener to the component(s) it should listen to, and
 * then pass it to ValidationGroup.add().
 * <p>
 * Note that one validation listener may not belong to more than one
 * ValidationGroup.
 * <p>
 * When an event that should trigger validation occurs, call the validate()
 * method.
 *
 * @author Tim Boudreau
 */
public abstract class ValidationListener extends InputVerifier implements EventListener {
	/**
	 * Client property which can be set to provide a component's name for use in
	 * validation messages. If not set, the component's <code>getName()</code>
	 * method is used.
	 */
	public static final String CLIENT_PROP_NAME = "_name";

	/**
	 * Get a string name for a component using the following strategy:
	 * <ol>
	 * <li>Check <code>jc.getClientProperty(CLIENT_PROP_NAME)</code></li>
	 * <li>If that returned null, call <code>jc.getName()</code>
	 * </ol>
	 * 
	 * @param jc
	 *            The component
	 * @return its name, if any, or null
	 */
	public static String nameForComponent(final JComponent jc) {
		String result = (String) jc.getClientProperty(CLIENT_PROP_NAME);
		if (result == null) {
			result = jc.getName();
		}
		return result;
	}

	public static void setComponentName(final JComponent comp, final String localizedName) {
		comp.putClientProperty(CLIENT_PROP_NAME, localizedName);
	}

	private ValidationGroupImpl group;

	final ValidationGroupImpl getValidationGroup() {
		return this.group;
	}

	final void setValidationGroup(final ValidationGroupImpl group) {
		assert EventQueue.isDispatchThread() : "Not in event thread"; // NOI18N
		this.group = group;
	}

	/**
	 * Perform the validation logic, triggering a call to Validate(Problems).
	 * 
	 * @return true if a problem was found with the component this object
	 *         validates (but not if a triggered call to ValidationGroup cause a
	 *         problem to be set).
	 */
	protected final boolean validate() {
		if (this.group.isSuspended()) {
			return true;
		}
		final Problems problems = new Problems();
		validate(problems);
		final Problem p = problems.getLeadProblem();
		if (p != null) {
			if (p.isFatal()) {
				this.group.getUI().setProblem(p);
			} else {
				final Problem other = getValidationGroup().validateAll(this);
				if (other != null && other.isWorseThan(p)) {
					getValidationGroup().getUI().setProblem(other);
				} else {
					getValidationGroup().getUI().setProblem(p);
				}
			}
		} else {
			final Problem other = getValidationGroup().validateAll(this);
			if (other != null) {
				getValidationGroup().getUI().setProblem(other);
			} else {
				getValidationGroup().getUI().clearProblem();
			}
		}
		// Even though we may have set a problem from another component
		// via validateAll(), we only want to return if there was a
		// problem with *this* component, because otherwise we could
		// block focus transfer (if being used as an InputValidator)
		// due to a problem on another component, making the user
		// unable to fix it
		return problems.isEmpty();
	}

	/**
	 * Perform the validation. The ValidationListener instance should have
	 * access to the component and validator and simply call the validator's
	 * validate() method with the appropriate arguments.
	 * <p>
	 * Typically you will not <i>call</i> this method yourself; rather, the
	 * infrastructure will call it. Your subclass of
	 * <code>ValidationListener</code> should implement some listener interface.
	 * When an interesting event occurs, call super.validate() and the rest will
	 * be taken care of.
	 *
	 * @param problems
	 *            A set of problems which can be added to
	 * @return true if no problems were found.
	 */
	protected abstract boolean validate(Problems problems);

	/**
	 * Implementation of InputVerifier. Simply returns <code>validate()</code>.
	 * 
	 * @param input
	 *            The component
	 * @return true if there are no problems with this component
	 */
	@Override
	public final boolean verify(final JComponent input) {
		return validate();
	}
}
