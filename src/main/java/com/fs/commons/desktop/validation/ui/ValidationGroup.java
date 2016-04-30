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

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import com.fs.commons.desktop.validation.Problem;
import com.fs.commons.desktop.validation.Validator;

/**
 * A group which holds validators that run against components. The group is
 * passed an instance of <code>ValidationUI</code> in its constructor. This is
 * typically something that is able to show the user that there is a problem in
 * some way, possibly enabling and disabling a dialog's OK button or similar.
 * <p>
 * There are two main reasons validation groups exist:
 * <ul>
 * <li>Often a change in one component triggers a change in the validity of
 * another component</li>
 * <li>There are several levels of problem, INFO, WARNING and FATAL. The user
 * should see FATAL messages even if the component their currently interacting
 * with is offering a warning</li>
 * </ul>
 * <p>
 * A validation group is typically associated with a single panel. For
 * components this library supports out-of-the-box such as
 * <code>JTextField</code>s or <code>JComboBox</code>es, simply call one of the
 * <code>add()</code> methods with your component and validators. For validating
 * your own components or ones this class doesn't have methods for, you
 * implement and add
 * <code><a href="ValidationListener.html">ValidationListener</a></code>s.
 * <p>
 * The contract of how a validation group works is as follows:
 * <ul>
 * <li>When an appropriate change in a component occurs, its content shall be
 * validated</li>
 * <li>If either a non-fatal problem or no problem is found when validating the
 * component, all other validators in the group may be run as follows</li>
 * <ul>
 * <li>If the validator for the component the user is currently interacting with
 * <i>did not</i> produce a problem, the worst (by the definition of
 * Problem.isWorseThan()) found is passed to the UI as the current problem. This
 * may be the first problem with ProblemKind.FATAL which is encountered, based
 * on the order validators were added to the group.</li>
 * <li>If the validator for the component the user is currently interacting with
 * <i>did</i> produce a problem, then if its <code>kind()</code> is
 * <code>ProblemKind.FATAL</code>, the UI's setProblem() method is called with
 * that problem. If the problem from the component the user is currently
 * interacting with is <code>ProblemKind.INFO</code> or
 * <code>ProblemKind.WARNING</code>, then all other validators in the group are
 * called. If any problem of a greater severity is found, that problem will be
 * passed to the group's UI's setProblem() method; otherwise the problem from
 * the current component is used.</li>
 * <li>If no problem was produced by the component the user is interacting with,
 * and not by any other, then the UI's <code>clearProblem()</code> method is
 * invoked.</li>
 * </ul>
 * </ul>
 * <h4>Validator Ordering</h4>
 * <p>
 * For the methods which take multiple validators or multiple enum constants
 * from the <a href="../builtin/Validators.html"><code>Validators</code></a>
 * class, order is important. Generally it is best to pass validators in order
 * of how specific they are - for example, if you are validating a URL you would
 * likely pass
 * 
 * <pre>
 * group.add(f, Validators.REQUIRE_NON_EMPTY_STRING, Validators.NO_WHITESPACE, Validators.URL_MUST_BE_VALID);
 * </pre>
 * 
 * so that the most general check is done first (if the string is empty or not);
 * the most specific test, which will actually try to construct a URL object and
 * report any <code>MalformedURLException</code> should be last.
 * <p/>
 * Validators for custom components can be added to the group by implementing
 * ValidationListener over a listener you attach to the component in question,
 * by calling <code>add(ValidationListener)</code>.
 * <p>
 * <b>Note:</b> All methods on this class must be called from the AWT Event
 * Dispatch thread, or assertion errors will be thrown. Manipulating components
 * on other threads is not safe.
 * <p/>
 * Swing documents are type-safe, and can be modified from other threads. In the
 * case that a text component validator receives an event on another thread,
 * validation will be scheduled for later, <i>on</i> the event thread.
 *
 *
 * @author Tim Boudreau
 */
public final class ValidationGroup {

	/**
	 * Create a new ValidationGroup.
	 * 
	 * @param ui
	 *            The user interface
	 * @return A new ValidationGroup
	 */
	public static ValidationGroup create(final ValidationUI... ui) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		return new ValidationGroup(ValidationGroupImpl.create(ui));
	}

	private final ValidationGroupImpl delegate;

	ValidationGroup(final ValidationGroupImpl delegate) {
		this.delegate = delegate;
	}

	/**
	 * Add a set of button models, e.g. radio buttons in a radio button group
	 * 
	 * @param buttons
	 *            The button models
	 * @param validator
	 *            A validator
	 */
	public final void add(final AbstractButton[] buttons, final Validator<ButtonModel[]> validator) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.add(buttons, validator);
	}

	/**
	 * Add a set of button models, e.g. radio buttons in a radio button group
	 * 
	 * @param buttons
	 *            The button models
	 * @param validator
	 *            A validator
	 */
	public void add(final ButtonModel[] buttons, final Validator<ButtonModel[]> validator) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.add(buttons, validator);
	}

	/**
	 * Add a combo box using the passed validation strategy and validator
	 * 
	 * @param box
	 *            a combo box
	 * @param strategy
	 *            the validation strategy that determines when validation should
	 *            run
	 * @param validator
	 *            A validator for combo box models (e.g. from
	 *            Converters.convert() to use String validators in combo boxes)
	 */
	public void add(final JComboBox box, final ValidationStrategy strategy, final Validator<ComboBoxModel> validator) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.add(box, strategy, validator);
	}

	/**
	 * Add a combo box to be validated with the passed validation strategy using
	 * the passed validators
	 * 
	 * @param box
	 *            A combo box component
	 * @param builtIns
	 *            One or more of the enums from the <code>Validators</code>
	 *            class which provide standard validation of many things
	 */
	public final void add(final JComboBox box, final ValidationStrategy strategy, final Validator<String>... builtIns) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.add(box, strategy, builtIns);
	}

	/**
	 * Add a combo box to be validated with ValidationStrategy.DEFAULT using the
	 * passed validator
	 * 
	 * @param box
	 *            A text component such as a <code>JTextField</code>
	 * @param validator
	 *            A validator
	 */
	public final void add(final JComboBox box, final Validator<ComboBoxModel> validator) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.add(box, validator);
	}

	/**
	 * Add a combo box to be validated with ValidationStrategy.DEFAULT using the
	 * passed validators
	 * 
	 * @param box
	 *            A combo box component
	 * @param builtIns
	 *            One or more of the enums from the <code>Validators</code>
	 *            class which provide standard validation of many things
	 */
	public final void add(final JComboBox box, final Validator<String>... builtIns) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.add(box, builtIns);
	}

	/**
	 * Add a text component to be validated with ValidationStrategy.DEFAULT
	 * using the passed validator
	 * 
	 * @param comp
	 *            A text component such as a <code>JTextField</code>
	 * @param validator
	 *            A validator
	 */
	public void add(final JTextComponent comp, final ValidationStrategy strategy, final Validator<Document> validator) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.add(comp, strategy, validator);
	}

	/**
	 * Add a text control to be validated with the passed validation strategy
	 * using the passed validators
	 * 
	 * @param comp
	 *            A text control such as a <code>JTextField</code>
	 * @param builtIns
	 *            One or more of the enums from the <code>Validators</code>
	 *            class which provide standard validation of many things
	 */
	public final void add(final JTextComponent comp, final ValidationStrategy strategy, final Validator<String>... builtIns) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.add(comp, strategy, builtIns);
	}

	/**
	 * Add a text component to be validated with ValidationStrategy.DEFAULT
	 * using the passed validator
	 * 
	 * @param comp
	 *            A text component such as a <code>JTextField</code>
	 * @param validator
	 *            A validator
	 */
	public final void add(final JTextComponent comp, final Validator<Document> validator) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.add(comp, validator);
	}

	/**
	 * Add a text component to be validated with ValidationStrategy.DEFAULT
	 * using the passed validators
	 * 
	 * @param comp
	 *            A text component such as a <code>JTextField</code>
	 * @param builtIns
	 *            One or more of the enums from the <code>Validators</code>
	 *            class which provide standard validation of many things
	 */
	public final void add(final JTextComponent comp, final Validator<String>... builtIns) {
		this.delegate.add(comp, builtIns);
	}

	/**
	 * Add a validation listener. To validate custom components, implement
	 * ValidationListener and attach it as a listener to the component, calling
	 * ValidationListener.validate() on events that should trigger validation,
	 * and add it to the group.
	 */
	public void add(final ValidationListener listener) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.add(listener);
	}

	/**
	 * Add a UI which should be called on validation of this group or any
	 * components within it.
	 * <p/>
	 * This is useful in the case that you have multiple components which are
	 * provided separately and each want to respond to validation problems (for
	 * example, one UI controlling a dialog's OK button, another controlling
	 * display of error text.
	 *
	 * @param ui
	 *            An implementation of ValidationUI
	 */
	public final void addUI(final ValidationUI ui) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.addUI(ui);
	}

	/**
	 * Add a validation group whose components should be validated as a part of
	 * this group. This is useful in the case that you have a panel with its own
	 * validation group, and want to embed it another panel that also wants to
	 * do its own validation.
	 * <p/>
	 * Whenever there is a change in one of the child ValidationGroupc's
	 * components, it will drive the UI(s) belonging to this group, as well as
	 * (optionally) its own.
	 * <p/>
	 * This technique is mostly useful in the case of things like abstract
	 * customizer dialogs, where you have a ValidationUI to control the OK
	 * button, but you don't actually anything about the component provided as a
	 * customizer. This method makes it possible for a parent component (such as
	 * our customizer) to be affected by validation in a child component.
	 * <p/>
	 * If you are not composing together multiple panels which each have their
	 * own validation code, ignore this method.
	 *
	 * @param group
	 *            The validation group
	 * @param useUI
	 *            If true, the other validation group's UI should continue to be
	 *            updated
	 */
	public final void addValidationGroup(final ValidationGroup group, final boolean useUI) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		if (group == this || group.delegate == this.delegate) {
			throw new IllegalArgumentException("Adding group to itself");
		}
		this.delegate.addValidationGroup(group.delegate, useUI);
	}

	/**
	 * Create a label which will show the current problem if any, which can be
	 * added to a panel that uses validation
	 * 
	 * @return A JLabel
	 */
	public final JLabel createProblemLabel() {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		final JLabel result = new FixedHeightLabel();
		addUI(new ValidationUI() {

			@Override
			public void clearProblem() {
				result.setText("   ");
				result.setIcon(null);
			}

			@Override
			public void setProblem(final Problem problem) {
				result.setText(problem.getMessage());
				result.setIcon(problem.severity().icon());
				result.setForeground(problem.severity().color());
			}
		});
		return result;
	}

	/**
	 * Disable validation and invoke a runnable. This method is useful in UIs
	 * where a change in one component can trigger changes in another component,
	 * and you do not want validation to be triggered because a component was
	 * programmatically updated.
	 * <p>
	 * For example, say you have a dialog that lets you create a new Servlet
	 * source file. As the user types the servlet name, web.xml entries are
	 * updated to match, and these are also in fields in the same dialog. Since
	 * the updated web.xml entries are being programmatically (and presumably
	 * correctly) generated, those changes should not trigger a useless
	 * validation run. Wrap such generation code in a Runnable and pass it to
	 * this method when making programmatic changes to the contents of the UI.
	 * <p>
	 * The runnable is run synchronously, but no changes made to components
	 * while the runnable is running will trigger validation.
	 * <p>
	 * When the last runnable exits, validateAll(null) will be called to run
	 * validation against the entire newly updated UI.
	 * <p>
	 * This method is reentrant - a call to updateComponents can trigger another
	 * call to updateComponents without triggering multiple calls to
	 * validateAll() on each Runnable's exit.
	 *
	 * @param run
	 *            A runnable which makes changes to the contents of one or more
	 *            components in the UI which should not trigger validation
	 */
	public final void modifyComponents(final Runnable run) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.modifyComponents(run);
	}

	/**
	 * Remove a delegate UI which is being controlled by this validation group.
	 *
	 * @param ui
	 *            The UI
	 */
	public final void removeUI(final ValidationUI ui) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.removeUI(ui);
		ui.clearProblem();
	}

	/**
	 * Remove a child validation group from this one. In the case of composing
	 * together panels, each of which has its own validation group, this method
	 * is useful if the child panel changes on the fly.
	 * <p/>
	 * Once removed, the child validation group reverts to interacting only with
	 * its own UI, and validation running in it will not affect the UI of this
	 * ValidationGroup.
	 *
	 * @param group
	 */
	public final void removeValidationGroup(final ValidationGroup group) {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		this.delegate.removeValidationGroup(group.delegate);
	}

	/**
	 * Set the component decorator used modify components appearance to show
	 * that there is a problem with a component's content.
	 * 
	 * @param decorator
	 *            A decorator. May not be null.
	 */
	public void setComponentDecorator(final ComponentDecorator decorator) {
		this.delegate.setComponentDecorator(decorator);
	}

	/**
	 * Validate all components in this group, updating the UI with a new problem
	 * as necessary. Generally the validation framework handles validation;
	 * however you are using custom validators that rely on outside state, this
	 * method can be used to manually force validation.
	 *
	 * @return A problem, if any
	 */
	public Problem validateAll() {
		assert EventQueue.isDispatchThread() : "Must be called on event thread";
		return this.delegate.validateAll(null);
	}
}
