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
package com.fs.commons.desktop.validation.conversion;

import javax.swing.ComboBoxModel;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
class StringToComboBoxModelConverter extends Converter<String, ComboBoxModel> {

	private static final class V implements Validator<ComboBoxModel> {
		private final Validator<String> wrapped;

		public V(final Validator<String> wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public boolean validate(final Problems problems, final String compName, final ComboBoxModel model) {
			final Object o = model.getSelectedItem();
			final String s = o == null ? null : o.toString();
			return this.wrapped.validate(problems, compName, s);
		}
	}

	@Override
	public Validator<ComboBoxModel> convert(final Validator<String> from) {
		return new V(from);
	}
}
