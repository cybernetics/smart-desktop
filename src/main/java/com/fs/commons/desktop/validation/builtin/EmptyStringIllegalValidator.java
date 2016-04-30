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
 *
 * @author Tim Boudreau
 */
final class EmptyStringIllegalValidator implements Validator {
	@Override
	public boolean validate(final Problems problems, final String compName, final Object model) {
		final boolean result = model != null && model.toString().length() > 0;
		if (!result) {
			final String message = NbBundle.getMessage(EmptyStringIllegalValidator.class, "MSG_MAY_NOT_BE_EMPTY", compName); // NOI18N
			problems.add(message);
		}
		return result;
	}
}
