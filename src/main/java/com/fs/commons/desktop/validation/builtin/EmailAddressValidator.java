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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fs.commons.desktop.validation.Problem;
import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Severity;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
class EmailAddressValidator implements Validator<String> {

	static final Pattern ADDRESS_PATTERN = Pattern.compile("(.*?)<(.*)>$"); // NOI18N
	private final ValidHostNameOrIPValidator hv = new ValidHostNameOrIPValidator(false);

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		final Matcher m = ADDRESS_PATTERN.matcher(model);
		String realName = null;
		String address;
		if (m.lookingAt()) {
			if (m.groupCount() == 2) {
				address = m.group(2);
				realName = m.group(1);
			} else {
				address = m.group(1);
			}
		} else {
			address = model;
		}
		return validate(realName, address, problems, compName);
	}

	private boolean validate(final String realName, final String address, final Problems problems, final String compName) {
		final String[] nameAndHost = address.split("@");
		if (nameAndHost.length == 1 && nameAndHost[0].contains("@")) {
			problems.add(NbBundle.getMessage(EmailAddressValidator.class, "EMAIL_MISSING_HOST", compName, nameAndHost[0]));
			return false;
		}
		if (nameAndHost.length > 2) {
			problems.add(NbBundle.getMessage(EmailAddressValidator.class, "EMAIL_HAS_>1_@", compName, address));
			return false;
		}
		final String name = nameAndHost[0];
		if (name.length() == 0) {
			problems.add(NbBundle.getMessage(EmailAddressValidator.class, "EMAIL_MISSING_NAME", compName, name));
			return false;
		}
		if (name.length() > 64) {
			problems.add(new Problem(NbBundle.getMessage(EmailAddressValidator.class, "ADDRESS_MAY_BE_TOO_LONG", compName, name), Severity.WARNING));
		}
		final String host = nameAndHost.length >= 2 ? nameAndHost[1] : null;
		boolean result = host != null;
		if (result) {
			result = this.hv.validate(problems, compName, host);
			if (result) {
				final MayNotContainSpacesValidator v = new MayNotContainSpacesValidator();
				result = v.validate(problems, compName, name);
			}
			final Validator<String> v = new EncodableInCharsetValidator("US-ASCII");
			if (result) {
				result = v.validate(problems, compName, address);
			}
		} else {
			problems.add(NbBundle.getMessage(EmailAddressValidator.class, "EMAIL_MISSING_HOST", compName, nameAndHost[0]));
		}
		return result;
	}

	private boolean validate(final String realName, final String name, final String host, final Problems problems, final String compName) {
		return false;
	}
}
