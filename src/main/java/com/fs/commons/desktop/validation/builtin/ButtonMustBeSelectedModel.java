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

import javax.swing.ButtonModel;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Severity;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
final class ButtonMustBeSelectedModel implements Validator<ButtonModel[]> {
	private final String message;

	ButtonMustBeSelectedModel(final String message) {
		this.message = message;
	}

	@Override
	public boolean validate(final Problems problems, final String compName, final ButtonModel[] model) {
		boolean sel = false;
		for (final ButtonModel m : model) {
			sel = m.isSelected();
			if (sel) {
				break;
			}
		}
		if (!sel) {
			problems.add(this.message, Severity.FATAL);
			return false;
		}
		return true;
	}

}
