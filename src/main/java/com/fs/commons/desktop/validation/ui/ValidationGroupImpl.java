/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package com.fs.commons.desktop.validation.ui;

import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import com.fs.commons.desktop.validation.Problem;
import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.desktop.validation.builtin.FSValidators;

/**
 * A group which holds validators that run against components.  The group is
 * passed an instance of <code>ValidationUI</code> in its constructor.  This
 * is typically something that is able to show the user that there is a problem
 * in some way, possibly enabling and disabling a dialog's OK button or similar.
 * <p>
 * There are two main reasons validation groups exist:
 * <ul>
 * <li>Often a change in one component triggers a change in the validity of
 * another component</li>
 * <li>There are several levels of problem, INFO, WARNING and FATAL.  The user
 * should see FATAL messages even if the component their currently interacting
 * with is offering a warning</li>
 * </ul>
 * <p>
 * A validation group is typically associated with a single panel.  For components
 * this library supports out-of-the-box such as <code>JTextField</code>s or
 * <code>JComboBox</code>es, simply call one of the <code>add()</code> methods
 * with your component and validators.  For validating your own components or
 * ones this class doesn't have methods for, you implement and add
 * <code><a href="ValidationListener.html">ValidationListener</a></code>s.
 * <p>
 * The contract
 * of how a validation group works is as follows:
 * <ul>
 * <li>When an appropriate change in a component occurs, its content shall be
 * validated</li>
 * <li>If either a non-fatal problem or no problem is found when validating
 * the component, all other validators in the group may be run as follows</li>
 * <ul>
 *  <li>If the validator for the component the user is currently interacting
 *   with <i>did not</i> produce a problem, the worst (by the definition of
 *   Problem.isWorseThan()) found is passed to the UI as the current problem.
 *   This may be the first problem with ProblemKind.FATAL which is encountered,
 *   based on the order validators were added to the group.</li>
 *  <li>If the validator for the component the user is currently interacting
 *   with <i>did</i> produce a problem, then if its <code>kind()</code> is
 *   <code>ProblemKind.FATAL</code>, the UI's setProblem() method is called
 *   with that problem.  If the problem from the component the user is
 *   currently interacting with is <code>ProblemKind.INFO</code> or
 *   <code>ProblemKind.WARNING</code>, then all other validators in the
 *   group are called.  If any problem of a greater severity is found, that
 *   problem will be passed to the group's UI's setProblem() method;  otherwise
 *   the problem from the current component is used.</li>
 *  <li>If no problem was produced by the component the user is interacting with,
 *   and not by any other, then the UI's <code>clearProblem()</code> method
 *   is invoked.</li>
 * </ul>
 * </ul>
 * <h4>Validator Ordering</h4>
 * <p>
 * For the methods which take multiple validators or multiple enum constants
 * from the <a href="../builtin/Validators.html"><code>Validators</code></a>
 * class, order is important.  Generally it is best to pass validators in order
 * of how specific they are - for example, if you are validating a URL you
 * would likely pass
 * <pre>
 * group.add(f, Validators.REQUIRE_NON_EMPTY_STRING,
 *          Validators.NO_WHITESPACE,
 *          Validators.URL_MUST_BE_VALID);
 * </pre>
 * so that the most general check is done first (if the string is empty or not);
 * the most specific test, which will actually try to construct a URL object
 * and report any <code>MalformedURLException</code> should be last.
 * <p/>
 * Note:  This class is only intended to be implemented by the Simple Validation
 * library.  If you are subclassing it, you are probably doing something wrong.
 *
 * @author Tim Boudreau
 */
final class ValidationGroupImpl {
    final MulticastValidationUI ui;
    private int suspendCount;
    private final List<ValidationListener> all = new ArrayList<ValidationListener>(10);
    private ComponentDecorator decorator = new ComponentDecorator();
    ValidationGroupImpl parent;

    ValidationGroupImpl(ValidationUI... ui) {
        if (ui == null) {
            throw new NullPointerException ("UI null");
        }
        this.ui = new MulticastValidationUI();
        for (ValidationUI uis : ui) {
            this.ui.add(uis);
        }
    }

    ValidationGroupImpl() {
        this (new ValidationUI[0]);
    }

    ValidationUI getUI() {
        return ui;
    }

    boolean isSuspended() {
        return suspendCount > 0 || parent != null && parent.isSuspended();
    }

    void addUI (ValidationUI real) {
        if (!contains(real)) {
            ui.add(real);
        }
    }

    void removeUI (ValidationUI ui) {
        if (contains(ui)) {
            this.ui.remove(ui);
        }
    }

    boolean contains(ValidationUI ui) {
        return this.ui == ui || this.ui.contains(ui);
    }

    void setParent (ValidationGroupImpl group) {
        if (parent != null && group != null) {
//            parent.setParent (group);
            throw new IllegalStateException ("Already has parent " + parent);
        } else {
            this.parent = group;
        }
    }

    ComponentDecorator getComponentDecorator() {
        return decorator;
    }

    public void setComponentDecorator (ComponentDecorator decorator) {
        assert EventQueue.isDispatchThread() : "Not on event thread"; //NOI18N
        if (decorator == null) {
            throw new NullPointerException("Null decorator"); //NOI18N
        }
        this.decorator = decorator;
    }

    /**
     * Disable validation and invoke a runnable.  This method is useful
     * in UIs where a change in one component can trigger changes in
     * another component, and you do not want validation to be triggered
     * because a component was programmatically updated.
     * <p>
     * For example, say you have a dialog that lets you create a new
     * Servlet source file.  As the user types the servlet name, web.xml
     * entries are updated to match, and these are also in fields in the same
     * dialog.  Since the updated web.xml entries are being programmatically
     * (and presumably correctly) generated, those changes should not
     * trigger a useless validation run.  Wrap such generation code in
     * a Runnable and pass it to this method when making programmatic
     * changes to the contents of the UI.
     * <p>
     * The runnable is run synchronously, but no changes made to components
     * while the runnable is running will trigger validation.
     * <p>
     * When the last runnable exits,
     * validateAll(null) will be called to run validation
     * against the entire newly updated UI.
     * <p>
     * This method is reentrant - a call to updateComponents can trigger
     * another call to updateComponents without triggering multiple
     * calls to validateAll() on each Runnable's exit.
     *
     * @param run A runnable which makes changes to the contents of one
     * or more components in the UI which should not trigger validation
     */
    public final void modifyComponents(Runnable run) {
        if (parent != null) {
            parent.modifyComponents(run);
        } else {
            suspendCount++;
            try {
                run.run();
            } finally {
                suspendCount--;
                if (!isSuspended()) {
                    validateAll(null);
                }
            }
        }
    }

    /**
     * Create a new ValidationGroup.
     * @param ui The user interface
     * @return A new ValidationGroup
     */
    static ValidationGroupImpl create (ValidationUI[] ui) {
        if (ui == null) {
            throw new NullPointerException();
        }
        return new ValidationGroupImpl (ui);
    }

    /**
     * Add a text component to be validated with ValidationStrategy.DEFAULT
     * using the passed validator
     * @param comp A text component such as a <code>JTextField</code>
     * @param validator A validator
     */
    public final void add (JTextComponent comp, Validator<Document> validator) {
        add (comp, ValidationStrategy.DEFAULT, validator);
    }

    /**
     * Add a text component to be validated with ValidationStrategy.DEFAULT
     * using the passed validators
     * @param comp A text component such as a <code>JTextField</code>
     * @param builtIns One or more of the enums from the <code>Validators</code>
     * class which provide standard validation of many things
     */
    public final void add (JTextComponent comp, Validator<String>... builtIns) {
        add (comp, ValidationStrategy.DEFAULT, builtIns);
    }

    /**
     * Add a combo box to be validated with ValidationStrategy.DEFAULT
     * using the passed validator
     * @param box A text component such as a <code>JTextField</code>
     * @param validator A validator
     */
    public final void add (JComboBox box, Validator<ComboBoxModel> validator) {
        add (box, ValidationStrategy.DEFAULT, validator);
    }

    /**
     * Add a combo box to be validated with ValidationStrategy.DEFAULT
     * using the passed validators
     * @param box A combo box component
     * @param builtIns One or more of the enums from the <code>Validators</code>
     * class which provide standard validation of many things
     */
    public final void add (JComboBox box, Validator<String>... builtIns) {
        add (box, ValidationStrategy.DEFAULT, builtIns);
    }

    /**
     * Add a text control to be validated with the passed validation strategy
     * using the passed validators
     * @param comp A text control such as a <code>JTextField</code>
     * @param builtIns One or more of the enums from the <code>Validators</code>
     * class which provide standard validation of many things
     */
    public final void add(JTextComponent comp, ValidationStrategy strategy, Validator<String>... builtIns) {
        Validator<Document> v = FSValidators.forDocument(true, builtIns);
        add(comp, strategy, v);
    }

    /**
     * Add a combo box to be validated with the passed validation strategy
     * using the passed validators
     * @param box A combo box component
     * @param builtIns One or more of the enums from the <code>Validators</code>
     * class which provide standard validation of many things
     */
    public final void add(JComboBox box, ValidationStrategy strategy, Validator<String>... builtIns) {
        Validator<ComboBoxModel> v = FSValidators.forComboBox(true, builtIns);
        add(box, strategy, v);
    }

    /**
     * Add a validator of button models - typically to see if any are selected.
     * @see com.fs.commons.desktop.validation.builtin.FSValidators
     * @param buttons The buttons
     * @param validator A validator
     */
    public final void add (AbstractButton[] buttons, Validator<ButtonModel[]> validator) {
        ButtonModel[] mdls = new ButtonModel[buttons.length];
        for (int i = 0; i < mdls.length; i++) {
            mdls[i] = buttons[i].getModel();
        }
        add (mdls, validator);
    }

    public void add(ValidationListener listener) {
        if (parent != null) {
            parent.add(listener);
        } else {
            listener.setValidationGroup(this);
        }
        all.add(listener);
    }

    public void add(final ButtonModel[] buttons, final Validator<ButtonModel[]> validator) {
        assert EventQueue.isDispatchThread() : "Must be called on event thread";
        class V extends ValidationListener implements ItemListener {

            public void itemStateChanged(ItemEvent e) {
                validate();
            }

            public boolean validate(Problems problems) {
                if (!enabled(buttons)) {
                    return true;
                }
                return validator.validate(problems, null, buttons);
            }

            private boolean enabled (ButtonModel[] b) {
                boolean result = true;
                for (ButtonModel m : b) {
                    result = m.isEnabled();
                    if (!result) {
                        break;
                    }
                }
                return result;
            }
        }
        V v = new V();
        add(v);
    }

    public void add(final JComboBox box, ValidationStrategy strategy, final Validator<ComboBoxModel> validator) {
        assert EventQueue.isDispatchThread() : "Must be called on event thread";
        final Border originalBorder = box.getBorder();
        class V extends ValidationListener implements ItemListener, FocusListener {

            public void focusLost(FocusEvent e) {
                validate();
            }

            public void itemStateChanged(ItemEvent e) {
                validate();
            }

            public void focusGained(FocusEvent e) {
            }

            public boolean validate(Problems problems) {
                if (!box.isEnabled()) {
                    return true;
                }
                boolean result = validator.validate(problems,
                        nameForComponent(box), box.getModel());
                if (originalBorder != null) {
                    if (result) {
                        //Test to avoid unncessary re-layout
                        if (box.getBorder() != originalBorder) {
                            box.setBorder(originalBorder);
                        }
                    } else {
                        Problem p = problems.getLeadProblem();
                            box.setBorder(decorator.createProblemBorder(box,
                                    originalBorder, p.severity()));
                    }
                }
                return result;
            }
        }
        final V v = new V();
        add(v);
        switch (strategy) {
            case DEFAULT:
            case ON_CHANGE_OR_ACTION:
                box.addItemListener(v);
                break;
            case ON_FOCUS_LOSS:
                box.addFocusListener(v);
                break;
            case INPUT_VERIFIER:
                box.setInputVerifier(v);
                break;
            default:
                throw new AssertionError();
        }
    }

    public void add(final JTextComponent field, ValidationStrategy strategy, final Validator<Document> validator) {
        assert EventQueue.isDispatchThread() : "Must be called on event thread";
        final Border originalBorder = field.getBorder();

        class V extends ValidationListener implements DocumentListener, FocusListener, Runnable {

            public void focusLost(FocusEvent e) {
                validate();
            }

            public void focusGained(FocusEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                removeUpdate(e);
            }

            public void removeUpdate(DocumentEvent e) {
                //Documents can be legally updated from another thread,
                //but we will not run validation outside the EDT
                if (!EventQueue.isDispatchThread()) {
                    EventQueue.invokeLater(this);
                } else {
                    validate();
                }
            }

            public void changedUpdate(DocumentEvent e) {
                removeUpdate(e);
            }

            public void run() {
                validate();
            }

            public boolean validate(Problems problems) {
                if (!field.isEnabled()) {
                    return true;
                }
                boolean result = validator.validate(problems,
                        nameForComponent(field), field.getDocument());
                if (originalBorder != null) {
                    if (result) {
                        //Test to avoid unncessary re-layout
                        if (field.getBorder() != originalBorder) {
                            field.setBorder(originalBorder);
                        }
                    } else {
                        assert field != null : "Field null"; //NOi18N
                        assert decorator != null : "Decorator null"; //NOI18N
                        Problem p = problems.getLeadProblem();
                        assert p != null : "A validator has returned false from" + //NOI18N
                                " validate(), but no validator has added any" + //NOI18N
                                "problems to the problem set."; //NOI18N
                        Border border = decorator.createProblemBorder(field,
                                    originalBorder, p.severity());
                        field.setBorder(border);
                    }
                }
                return result;
            }

        }
        final V v = new V();
        add(v);
        switch (strategy) {
            case DEFAULT:
            case ON_CHANGE_OR_ACTION:
                field.getDocument().addDocumentListener(v);
                break;
            case INPUT_VERIFIER:
                field.setInputVerifier(v);
                break;
            case ON_FOCUS_LOSS:
                field.addFocusListener(v);
                break;
        }
    }

    public Problem validateAll(ValidationListener trigger) {
        assert EventQueue.isDispatchThread() : "Must be called on event thread";
        if (parent != null) {
            return parent.validateAll(trigger);
        } else {
            if (isSuspended()) {
                return null;
            }
            Problems p = new Problems();
            for (ValidationListener a : all) {
                if (a == trigger) {
                    continue;
                }
                a.validate(p);
                if (p.hasFatal()) {
                    break;
                }
            }
            if (!p.isEmpty()) {
                Problem problem = p.getLeadProblem();
                return problem;
            }
            return null;
        }
    }

    public void addValidationGroup(ValidationGroupImpl group, boolean useUI) {
        assert EventQueue.isDispatchThread() : "Not in event thread"; //NOI18N
        assert noOverlap (all, group.all);
        if (group.parent != null) {
            throw new IllegalStateException ("Cannot add to a group that has " + //NOI18N
                    "already been added to another group"); //NOI18N
        }
        all.addAll (group.all);
        for (ValidationListener l : all) {
            l.setValidationGroup(this);
        }
        if (useUI) {
            addUI(new GroupSpecificValidationUI(group, group.ui));
        } else {
            //Clear any existing problem - validateAll() will recreate
            //it if need-be
            group.ui.clearProblem();
        }
        group.setParent(this);
        validateAll(null);
    }

    public void removeValidationGroup (ValidationGroupImpl group) {
        assert EventQueue.isDispatchThread() : "Not in event thread"; //NOI18N
        if (group == this) {
            throw new IllegalArgumentException ("Removing from self"); //NOI18N
        }
        all.removeAll(group.all);
        for (ValidationListener l : group.all) {
            l.setValidationGroup(group);
        }
        ui.removeUI(group);
        group.setParent(null);
        validateAll (null);
    }

    private boolean noOverlap(List<ValidationListener> all, List<ValidationListener> all0) {
        HashSet<ValidationListener> s = new HashSet<ValidationListener>();
        s.addAll (all);
        s.addAll (all0);
        return s.size() == all.size() + all0.size();
    }

}