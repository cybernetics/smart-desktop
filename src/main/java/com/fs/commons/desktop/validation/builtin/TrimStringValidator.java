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
package com.fs.commons.desktop.validation.builtin;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;

/**
 * Validator which wraps another String validator and simply calls trim() on the
 * value before passing it to the other validator.
 *
 * @author Tim Boudreau
 */
final class TrimStringValidator implements Validator<String> {
	private final Validator<String> other;

	TrimStringValidator(final Validator<String> other) {
		this.other = other;
	}

	@Override
	public String toString() {
		return "TrimStringValidator for " + this.other; // NOI18N
	}

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		return this.other.validate(problems, compName, model == null ? null : model.trim());
	}

}
