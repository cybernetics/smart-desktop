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

import javax.swing.JComponent;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;

/**
 * Convenience ValidationListener which implements Validator directly.
 * Suitable for use when you have a single component that needs custom
 * validation.
 *
 * @author Tim Boudreau
 */
public abstract class AbstractValidationListener<CompType extends JComponent, T>
        extends ValidationListener implements Validator<T> {
    private final CompType comp;
    /**
     * Create a new AbstractValidationListener for the single component
     * passed here as an argument.  If the component is not expected to
     * live after the validator is detached, you can add this object as a
     * listener to the component in the constructor (but remember that
     * this means the component will reference this validator forever).
     * @param comp
     */
    public AbstractValidationListener(CompType comp) {
        this.comp = comp;
    }

    /**
     * Get the name of the component which should be passed to
     * validate.  The default implementation delegates to
     * <code>nameForComponent</code> which will either return the
     * client-property based name or the result of getName() on
     * the component.
     *
     * @param comp The component
     * @return A localized name
     */
    protected String findComponentName (CompType comp) {
        return nameForComponent(comp);
    }

    /**
     * Get the model object that will be passed to validate
     * @param comp The component
     * @return The model object
     */
    protected abstract T getModelObject(CompType comp);

    /**
     * Called when validation runs.  The default implementation does nothing;
     * some validators may want to change the visual appearance of the component
     * to indicate an error.
     *
     * @param component The component
     * @param validationResult The result of validation
     */
    protected void onValidate(CompType component, boolean validationResult) {}

    @Override
    protected final boolean validate(Problems problems) {
        boolean result = validate(problems,
                nameForComponent(comp), getModelObject(comp));
        onValidate(comp, result);
        return result;
    }

    public abstract boolean validate(Problems problems, String compName, T model);

}
