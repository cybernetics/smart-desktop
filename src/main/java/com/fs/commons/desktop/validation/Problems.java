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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A collection of problems, to which a Validator can add additional problems.
 *
 * @author Tim Boudreau
 */
public final class Problems {
    private final List<Problem> problems = new ArrayList<Problem>();
    public List<Problem> getProblemsList() {
		return problems;
	}

	private boolean hasFatal;
    /**
     * Convenience method to add a problem with the specified message and
     * Severity.FATAL
     * @param problem
     */
    public final void add (String problem) {
        add (problem, Severity.FATAL);
    }

    /**
     * Add a problem with the specified severity
     * @param problem the message
     * @param severity the severity
     */
    public final void add (String problem, Severity severity) {
        problems.add (new Problem (problem, severity));
        hasFatal |= severity == Severity.FATAL;
    }

    /**
     * Add a problem
     * @param problem The problem
     */
    public final void add (Problem problem) {
        problems.add (problem);
    }

    /**
     * Dump all problems in another instance of Problems into this one.
     * @param problems The other problems.
     */
    public final void putAll (Problems problems) {
        if (problems == this) throw new IllegalArgumentException (
                "putAll to self"); //NOI18N
        this.problems.addAll (problems.problems);
    }

    /**
     * Determine if this set of problems is empty
     * @return true if there are no problems here
     */
    public final boolean isEmpty() {
        return this.problems.isEmpty();
    }

    /**
     * Determine if this set of problems includes any that are fatal.
     * @return true if a fatal problem has been encountered
     */
    public final boolean hasFatal() {
        return hasFatal;
    }

    /**
     * Get the first problem of the highest severity
     *
     * @return The most severe problem in this set
     */
    public final Problem getLeadProblem() {
        Collections.sort (problems);
        return problems.isEmpty() ? null :
            problems.get(problems.size() - 1);
    }

    public String toString() {
        return problems.toString();
    }

	public String toMutilLineString() {
		String newLine = System.getProperty("line.separator");
		StringBuffer problemStr=new StringBuffer();
		for (Problem problem:problems) {
			problemStr.append(problem.getMessage()+newLine);
		}
		return problemStr.toString();
	}
}
