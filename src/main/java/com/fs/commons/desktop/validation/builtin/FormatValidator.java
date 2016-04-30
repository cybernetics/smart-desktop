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

import java.text.Format;
import java.text.ParseException;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
final class FormatValidator implements Validator<String> {
	private final Format fmt;

	FormatValidator(final Format fmt) {
		this.fmt = fmt;
	}

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		try {
			this.fmt.parseObject(model);
		} catch (final ParseException ex) {
			problems.add(NbBundle.getMessage(FormatValidator.class, "MSG_DOES_NOT_MATCH_NUMBER_FORMAT", compName, model)); // NOI18N
			return false;
		}
		return true;
	}

}
