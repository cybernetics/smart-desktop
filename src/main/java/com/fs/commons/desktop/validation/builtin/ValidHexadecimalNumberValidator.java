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
final class ValidHexadecimalNumberValidator implements Validator<String> {

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		if (model.length() % 2 != 0) {
			problems.add(NbBundle.getMessage(ValidHexadecimalNumberValidator.class, "ODD_LENGTH_HEX", compName)); // NOI18N
			return false;
		}
		for (final char c : model.toCharArray()) {
			final boolean good = c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f' || c >= '0' && c <= '9';
			if (!good) {
				problems.add(NbBundle.getMessage(ValidHexadecimalNumberValidator.class, "INVALID_HEX", // NOI18N
						new String(new char[] { c }), compName));
				return false;
			}
		}
		return true;
	}

}
