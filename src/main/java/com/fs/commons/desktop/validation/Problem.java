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
package com.fs.commons.desktop.validation;

/**
 * Represents a problem produced by a validator.
 *
 * @author Tim Boudreau
 */
public final class Problem implements Comparable<Problem> {
    private final String message;
    private final Severity severity;
    /**
     * Create a new problem with the given message and severity
     * @param message A localized, human readable message
     * @param severity The severity
     */
    public Problem(String message, Severity severity) {
        if (message == null) {
            throw new NullPointerException ("Null message"); //NOI18N
        }
        if (severity == null) {
            throw new NullPointerException ("Null kind"); //NOI18N
        }
        this.message = message;
        this.severity = severity;
    }

    /**
     * Get the severity of this problem.  The severity indicates whether
     * the user should be blocked from further action until the problem
     * is corrected, or if continuing with a warning is reasonable.
     * It also determines the warning icon which can be displayed to the
     * user.
     * @return The severity
     */
    public Severity severity() {
        return severity;
    }

    /**
     * Determine if this problem is more severe than another
     * @param other The other problem
     * @return true if compareTo(other) &lt; 0;
     */
    public boolean isWorseThan(Problem other) {
        return other == null ? true : compareTo(other) > 0;
    }

    /**
     * Convenience method to determine if this problem is of Severity.FATAL
     * severity
     * @return true if severity() == Severity.FATAL
     */
    public boolean isFatal() {
        return severity == Severity.FATAL;
    }

    /**
     * Get the localized, human-readable description of the problem
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Compare, such that most severe Problems will appear last, least first
     * @param o
     * @return the difference in severity as an integer
     */
    public int compareTo(Problem o) {
        int ix = severity.ordinal();
        int oid = o.severity.ordinal();
        return ix - oid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        boolean result = o != null && o.getClass() == Problem.class;
        if (result) {
            Problem p = (Problem) o;
            result = p.severity == severity && p.getMessage().equals(getMessage());
        }
        return result;
    }

    @Override
    public String toString() {
        return getMessage() + "(" + severity() + ")";
    }

    @Override
    public int hashCode() {
        return message.hashCode() * (severity.hashCode() + 1);
    }
}
