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

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
final class CharsetValidator implements Validator<String> {

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		Exception e = null;
		try {
			Charset.forName(model);
		} catch (final IllegalCharsetNameException badName) {
			problems.add(NbBundle.getMessage(CharsetValidator.class, "ILLEGAL_CHARSET_NAME", compName, model)); // NOI18N
			e = badName;
		} catch (final UnsupportedCharsetException unsup) {
			problems.add(NbBundle.getMessage(CharsetValidator.class, "UNSUPPORTED_CHARSET_NAME", compName, model)); // NOI18N
			e = unsup;
		}
		return e == null;
	}

}
