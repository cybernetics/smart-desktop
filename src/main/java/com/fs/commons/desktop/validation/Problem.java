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
	 * 
	 * @param message
	 *            A localized, human readable message
	 * @param severity
	 *            The severity
	 */
	public Problem(final String message, final Severity severity) {
		if (message == null) {
			throw new NullPointerException("Null message"); // NOI18N
		}
		if (severity == null) {
			throw new NullPointerException("Null kind"); // NOI18N
		}
		this.message = message;
		this.severity = severity;
	}

	/**
	 * Compare, such that most severe Problems will appear last, least first
	 * 
	 * @param o
	 * @return the difference in severity as an integer
	 */
	@Override
	public int compareTo(final Problem o) {
		final int ix = this.severity.ordinal();
		final int oid = o.severity.ordinal();
		return ix - oid;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		boolean result = o != null && o.getClass() == Problem.class;
		if (result) {
			final Problem p = (Problem) o;
			result = p.severity == this.severity && p.getMessage().equals(getMessage());
		}
		return result;
	}

	/**
	 * Get the localized, human-readable description of the problem
	 * 
	 * @return The message
	 */
	public String getMessage() {
		return this.message;
	}

	@Override
	public int hashCode() {
		return this.message.hashCode() * (this.severity.hashCode() + 1);
	}

	/**
	 * Convenience method to determine if this problem is of Severity.FATAL
	 * severity
	 * 
	 * @return true if severity() == Severity.FATAL
	 */
	public boolean isFatal() {
		return this.severity == Severity.FATAL;
	}

	/**
	 * Determine if this problem is more severe than another
	 * 
	 * @param other
	 *            The other problem
	 * @return true if compareTo(other) &lt; 0;
	 */
	public boolean isWorseThan(final Problem other) {
		return other == null ? true : compareTo(other) > 0;
	}

	/**
	 * Get the severity of this problem. The severity indicates whether the user
	 * should be blocked from further action until the problem is corrected, or
	 * if continuing with a warning is reasonable. It also determines the
	 * warning icon which can be displayed to the user.
	 * 
	 * @return The severity
	 */
	public Severity severity() {
		return this.severity;
	}

	@Override
	public String toString() {
		return getMessage() + "(" + severity() + ")";
	}
}
