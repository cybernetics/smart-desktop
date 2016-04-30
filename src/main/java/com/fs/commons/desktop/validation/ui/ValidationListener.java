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
import java.util.EventListener;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

import com.fs.commons.desktop.validation.Problem;
import com.fs.commons.desktop.validation.Problems;

/**
 * Abstract base class for listeners which listen on some component, and
 * which are added to a ValidationGroup.  Implement whatever listener interface
 * is necessary, add it as a listener to the component(s) it should listen to, and
 * then pass it to ValidationGroup.add().
 * <p>
 * Note that one validation listener may not belong to more than one
 * ValidationGroup.
 * <p>
 * When an event that should trigger validation occurs, call the
 * validate() method.
 *
 * @author Tim Boudreau
 */
public abstract class ValidationListener extends InputVerifier implements EventListener {
    /**
     * Client property which can be set to provide a component's name for use
     * in validation messages.  If not set, the component's <code>getName()</code>
     * method is used.
     */
    public static final String CLIENT_PROP_NAME = "_name";

    /**
     * Perform the validation.  The ValidationListener instance should have
     * access to the component and validator and simply call the validator's
     * validate() method with the appropriate arguments.
     * <p>
     * Typically you will not <i>call</i> this method yourself;  rather, the
     * infrastructure will call it.  Your subclass of <code>ValidationListener</code>
     * should implement some listener interface.  When an interesting event
     * occurs, call super.validate() and the rest will be taken care of.
     *
     * @param problems A set of problems which can be added to
     * @return true if no problems were found.
     */
    protected abstract boolean validate(Problems problems);
    private ValidationGroupImpl group;

    final void setValidationGroup(ValidationGroupImpl group) {
        assert EventQueue.isDispatchThread() : "Not in event thread"; //NOI18N
        this.group = group;
    }

    final ValidationGroupImpl getValidationGroup() {
        return group;
    }

    /**
     * Perform the validation logic, triggering a call to
     * Validate(Problems).
     * @return true if a problem was found with the component this
     * object validates (but not if a triggered call to ValidationGroup
     * cause a problem to be set).
     */
    protected final boolean validate() {
        if (group.isSuspended()) {
            return true;
        }
        Problems problems = new Problems();
        validate(problems);
        Problem p = problems.getLeadProblem();
        if (p != null) {
            if (p.isFatal()) {
                group.getUI().setProblem(p);
            } else {
                Problem other = getValidationGroup().validateAll(this);
                if (other != null && other.isWorseThan(p)) {
                    getValidationGroup().getUI().setProblem(other);
                } else {
                    getValidationGroup().getUI().setProblem(p);
                }
            }
        } else {
            Problem other = getValidationGroup().validateAll(this);
            if (other != null) {
                getValidationGroup().getUI().setProblem(other);
            } else {
                getValidationGroup().getUI().clearProblem();
            }
        }
        //Even though we may have set a problem from another component
        //via validateAll(), we only want to return if there was a
        //problem with *this* component, because otherwise we could
        //block focus transfer (if being used as an InputValidator)
        //due to a problem on another component, making the user
        //unable to fix it
        return problems.isEmpty();
    }

    /**
     * Implementation of InputVerifier.  Simply returns <code>validate()</code>.
     * @param input The component
     * @return true if there are no problems with this component
     */
    public final boolean verify(JComponent input) {
        return validate();
    }


    /**
     * Get a string name for a component using the following strategy:
     * <ol>
     * <li>Check <code>jc.getClientProperty(CLIENT_PROP_NAME)</code></li>
     * <li>If that returned null, call <code>jc.getName()</code>
     * </ol>
     * @param jc The component
     * @return its name, if any, or null
     */
    public static String nameForComponent(JComponent jc) {
        String result = (String) jc.getClientProperty(CLIENT_PROP_NAME);
        if (result == null) {
            result = jc.getName();
        }
        return result;
    }
    
    public static void setComponentName(JComponent comp, String localizedName) {
        comp.putClientProperty (CLIENT_PROP_NAME, localizedName);
    }
}
