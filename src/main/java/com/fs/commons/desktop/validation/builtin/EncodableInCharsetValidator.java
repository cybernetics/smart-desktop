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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
final class EncodableInCharsetValidator implements Validator<String> {

	private final String charsetName;

	EncodableInCharsetValidator() {
		this(Charset.defaultCharset().name());
	}

	EncodableInCharsetValidator(final String charsetName) {
		this.charsetName = charsetName;
		// Be fail-fast with respect to exceptions
		Charset.forName(charsetName);
	}

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		final char[] c = model.toCharArray();
		boolean result = true;
		String curr;
		for (final char element : c) {
			curr = new String(new char[] { element });
			try {
				final String nue = new String(curr.getBytes(this.charsetName));
				result = element == nue.charAt(0);
				if (!result) {
					problems.add(NbBundle.getMessage(EncodableInCharsetValidator.class, "INVALID_CHARACTER", compName, curr, this.charsetName)); // NOI18N
					break;
				}
			} catch (final UnsupportedEncodingException ex) {
				// Already tested in constructor
				throw new AssertionError(ex);
			}
		}
		return result;
	}
}
