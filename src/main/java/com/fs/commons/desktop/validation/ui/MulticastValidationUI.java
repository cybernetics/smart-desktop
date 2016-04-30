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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fs.commons.desktop.validation.Problem;

/**
 *
 * @author Tim Boudreau
 */
final class MulticastValidationUI implements ValidationUI {

	private final Set<ValidationUI> real;

	MulticastValidationUI(final ValidationUI... real) {
		this.real = new HashSet<ValidationUI>(Arrays.asList(real));
		assert validUIs(real);
	}

	public void add(final ValidationUI ui) {
		if (ui == null) {
			throw new NullPointerException();
		}
		assert EventQueue.isDispatchThread() : "Not on the event thread";
		assert !contains(ui) : "Already a member: " + ui;
		this.real.add(ui);
	}

	@Override
	public void clearProblem() {
		assert this.real != null;
		for (final ValidationUI ui : this.real) {
			ui.clearProblem();
		}
	}

	public boolean contains(final ValidationUI check) {
		boolean result = this.real.contains(check);
		if (!result) {
			for (final ValidationUI ui : this.real) {
				if (ui instanceof MulticastValidationUI) {
					if (result = ((MulticastValidationUI) ui).contains(check)) {
						break;
					}
				}
			}
		}
		return result;
	}

	public void remove(final ValidationUI ui) {
		if (ui == null) {
			throw new NullPointerException();
		}
		assert EventQueue.isDispatchThread() : "Not on the event thread";
		assert contains(ui) : "Not a member: " + ui;
		this.real.remove(ui);
	}

	public boolean removeUI(final ValidationGroupImpl owner) {
		for (final ValidationUI ui : this.real) {
			if (ui instanceof GroupSpecificValidationUI) {
				final GroupSpecificValidationUI g = (GroupSpecificValidationUI) ui;
				if (g.owner == owner) {
					remove(ui);
					return true;
				}
			} else if (ui instanceof MulticastValidationUI) {
				final MulticastValidationUI m = (MulticastValidationUI) ui;
				if (m.removeUI(owner)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void setProblem(final Problem problem) {
		assert this.real != null;
		for (final ValidationUI ui : this.real) {
			ui.setProblem(problem);
		}
	}

	public boolean validUIs(final ValidationUI[] uis) {
		for (int i = 0; i < uis.length; i++) {
			final ValidationUI ui = uis[i];
			if (ui == null) {
				throw new NullPointerException("Element " + i + " of ui " + "array is null");
			}
		}
		return true;
	}
}
