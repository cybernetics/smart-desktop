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

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
final class IsANumberValidator implements Validator<String> {
	private final Locale locale;

	IsANumberValidator() {
		this(null);
	}

	IsANumberValidator(final Locale l) {
		this.locale = l;
	}

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		final ParsePosition p = new ParsePosition(0);
		try {
			NumberFormat.getNumberInstance(this.locale == null ? Locale.getDefault() : this.locale).parse(model, p);
			if (model.length() != p.getIndex() || p.getErrorIndex() != -1) {
				problems.add(NbBundle.getMessage(IsANumberValidator.class, "NOT_A_NUMBER", model, compName)); // NOI18N
				return false;
			}
		} catch (final NumberFormatException nfe) {
			problems.add(NbBundle.getMessage(IsANumberValidator.class, "NOT_A_NUMBER", model, compName)); // NOI18N
			return false;
		}
		return true;
	}

}
