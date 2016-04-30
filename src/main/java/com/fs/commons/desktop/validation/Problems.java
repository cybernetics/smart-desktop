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
	private boolean hasFatal;

	/**
	 * Add a problem
	 * 
	 * @param problem
	 *            The problem
	 */
	public final void add(final Problem problem) {
		this.problems.add(problem);
	}

	/**
	 * Convenience method to add a problem with the specified message and
	 * Severity.FATAL
	 * 
	 * @param problem
	 */
	public final void add(final String problem) {
		add(problem, Severity.FATAL);
	}

	/**
	 * Add a problem with the specified severity
	 * 
	 * @param problem
	 *            the message
	 * @param severity
	 *            the severity
	 */
	public final void add(final String problem, final Severity severity) {
		this.problems.add(new Problem(problem, severity));
		this.hasFatal |= severity == Severity.FATAL;
	}

	/**
	 * Get the first problem of the highest severity
	 *
	 * @return The most severe problem in this set
	 */
	public final Problem getLeadProblem() {
		Collections.sort(this.problems);
		return this.problems.isEmpty() ? null : this.problems.get(this.problems.size() - 1);
	}

	public List<Problem> getProblemsList() {
		return this.problems;
	}

	/**
	 * Determine if this set of problems includes any that are fatal.
	 * 
	 * @return true if a fatal problem has been encountered
	 */
	public final boolean hasFatal() {
		return this.hasFatal;
	}

	/**
	 * Determine if this set of problems is empty
	 * 
	 * @return true if there are no problems here
	 */
	public final boolean isEmpty() {
		return this.problems.isEmpty();
	}

	/**
	 * Dump all problems in another instance of Problems into this one.
	 * 
	 * @param problems
	 *            The other problems.
	 */
	public final void putAll(final Problems problems) {
		if (problems == this) {
			throw new IllegalArgumentException("putAll to self"); // NOI18N
		}
		this.problems.addAll(problems.problems);
	}

	public String toMutilLineString() {
		final String newLine = System.getProperty("line.separator");
		final StringBuffer problemStr = new StringBuffer();
		for (final Problem problem : this.problems) {
			problemStr.append(problem.getMessage() + newLine);
		}
		return problemStr.toString();
	}

	@Override
	public String toString() {
		return this.problems.toString();
	}
}
