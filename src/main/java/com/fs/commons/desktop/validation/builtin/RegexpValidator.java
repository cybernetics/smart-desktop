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

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
final class RegexpValidator implements Validator<String> {
	private final Pattern pattern;
	private final String message;
	private final boolean acceptPartialMatches;

	RegexpValidator(final String pattern, final String message, final boolean acceptPartialMatches) {
		this.pattern = Pattern.compile(pattern);
		this.message = message;
		this.acceptPartialMatches = acceptPartialMatches;
	}

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		final Matcher m = this.pattern.matcher(model);
		final boolean result = this.acceptPartialMatches ? m.lookingAt() : m.matches();
		if (!result) {
			String prb = this.message;
			prb = MessageFormat.format(prb, new Object[] { compName, model });
			problems.add(prb);
		}
		return result;
	}

}
