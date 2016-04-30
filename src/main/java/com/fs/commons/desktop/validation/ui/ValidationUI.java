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

import com.fs.commons.desktop.validation.Problem;

/**
 * User interface controller which can show the user a problem,
 * possibly disabling closing a dialog or similar if the problem is
 * of Severity.FATAL.
 * <p>
 * <code><a href="ValidationPanel.html">ValidationPanel</a></code>
 * provides an implementation of this interface;  it is also
 * relatively easy to write
 * an adapter to drive an existing UI in an existing application.
 * <p/>
 * Note:  An instance of ValidationUI should only be used with one
 * ValidationGroup - otherwise one UI having no problems will clear
 * the problem passed by another UI.
 *
 * @author Tim Boudreau
 */
public interface ValidationUI {
    /**
     * Set the user interface to the state where no problem is displayed
     * and the user is free to continue.
     */
    void clearProblem();
    /**
     * Set the problem to be displayed to the user.  Depending on the
     * severity of the problem, the user interface may want to block the
     * user from continuing until it is fixed (for example, disabling the
     * Next button in a wizard or the OK button in a dialog)
     * @param problem A problem that the user should be shown, which may
     * affect the state of the UI as a whole
     */
    void setProblem (Problem problem);
}
