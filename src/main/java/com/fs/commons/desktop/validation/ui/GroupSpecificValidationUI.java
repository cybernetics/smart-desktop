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

import com.fs.commons.desktop.validation.Problem;

/**
 * Validation UI which knows what validation group it belongs to, so in the case
 * of a ValidationGroup parented to another one, the child's UI can be removed
 * from the parent's list of uis to proxy if it is removed from the parent
 * group.
 *
 *
 * @author Tim Boudreau
 */
final class GroupSpecificValidationUI implements ValidationUI {
	final ValidationUI ui;
	final ValidationGroupImpl owner;

	public GroupSpecificValidationUI(final ValidationGroupImpl owner, final ValidationUI ui) {
		this.owner = owner;
		this.ui = ui;
	}

	@Override
	public void clearProblem() {
		this.ui.clearProblem();
	}

	@Override
	public void setProblem(final Problem problem) {
		this.ui.setProblem(problem);
	}

}
