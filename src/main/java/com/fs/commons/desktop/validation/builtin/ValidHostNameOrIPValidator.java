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
final class ValidHostNameOrIPValidator implements Validator<String> {
	private final HostNameValidator hostVal;
	private final IpAddressValidator ipVal = new IpAddressValidator();

	ValidHostNameOrIPValidator() {
		this(true);
	}

	ValidHostNameOrIPValidator(final boolean allowPort) {
		this.hostVal = new HostNameValidator(allowPort);
	}

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		final String[] parts = model.split("\\.");
		boolean hasIntParts = false;
		boolean hasNonIntParts = false;
		if (model.indexOf(" ") > 0 || model.indexOf("\t") > 0) {
			problems.add(NbBundle.getMessage(ValidHostNameOrIPValidator.class, "HOST_MAY_NOT_CONTAIN_WHITESPACE", compName, model)); // NOI18N
			return false;
		}
		if (parts.length == 0) { // the string "."
			problems.add(NbBundle.getMessage(ValidHostNameOrIPValidator.class, "INVALID_HOST_OR_IP", compName, model)); // NOI18N
			return false;
		}
		for (int i = 0; i < parts.length; i++) {
			String s = parts[i];
			if (i == parts.length - 1 && s.contains(":")) { // NOI18N
				final String[] partAndPort = s.split(":"); // NOI18N
				if (partAndPort.length > 2) {
					problems.add(NbBundle.getMessage(ValidHostNameOrIPValidator.class, "TOO_MANY_COLONS", compName, model)); // NOI18N
					return false;
				}
				if (partAndPort.length == 0) { // the string ":"
					problems.add(NbBundle.getMessage(ValidHostNameOrIPValidator.class, "INVALID_HOST_OR_IP", compName, model)); // NOI18N
					return false;
				}
				s = partAndPort[0];
				if (partAndPort.length == 2) {
					try {
						Integer.parseInt(partAndPort[1]);
					} catch (final NumberFormatException nfe) {
						problems.add(NbBundle.getMessage(ValidHostNameOrIPValidator.class, "INVALID_PORT", compName, partAndPort[1])); // NOI18N
						return false;
					}
				}
			}
			try {
				Integer.parseInt(s);
				hasIntParts = true;
			} catch (final NumberFormatException nfe) {
				hasNonIntParts = true;
			}
			if (hasIntParts && hasNonIntParts) {
				problems.add(NbBundle.getMessage(ValidHostNameOrIPValidator.class, "ADDRESS_CONTAINS_INT_AND_NON_INT_LABELS", compName, model)); // NOI18N
				return false;
			}
		}

		final boolean validHost = hasNonIntParts && this.hostVal.validate(problems, compName, model);
		final boolean validIp = hasIntParts && this.ipVal.validate(problems, compName, model);
		return validHost || validIp;
	}

}
