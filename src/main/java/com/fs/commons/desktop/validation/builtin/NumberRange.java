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
final class NumberRange implements Validator<String> {
	private final Number max;
	private final Number min;

	NumberRange(final Number min, final Number max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		try {
			if (model == null) {
				return false;
			}
			final double val = Double.parseDouble(model);
			final double minn = this.min.doubleValue();
			final double maxx = this.max.doubleValue();
			final boolean result = val >= minn && val <= maxx;
			if (!result) {
				problems.add(NbBundle.getMessage(NumberRange.class, "VALUE_OUT_OF_RANGE", new Object[] { // NOI18N
						compName, model, this.min, this.max }));
			}
			return result;
		} catch (final NumberFormatException e) {
			// should be handled by another validator
		}
		return true;
	}

}
