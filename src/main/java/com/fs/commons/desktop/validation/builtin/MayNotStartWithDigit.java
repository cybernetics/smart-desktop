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
final class MayNotStartWithDigit implements Validator<String> {

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		if (model.length() > 0) {
			final char c = model.charAt(0);
			if (Character.isDigit(c)) {
				problems.add(NbBundle.getMessage(MayNotStartWithDigit.class, "MAY_NOT_START_WITH_DIGIT", compName)); // NOI18N
			}
		}
		return true;
	}

}
