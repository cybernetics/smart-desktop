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
final class HostNameValidator implements Validator<String> {

	private final boolean allowPort;

	HostNameValidator(final boolean allowPort) {
		this.allowPort = allowPort;
	}

	private boolean checkHostPart(final String label, final Problems problems, final String compName) {
		boolean result = true;
		if (label.length() > 63) {
			problems.add(NbBundle.getMessage(HostNameValidator.class, "LABEL_TOO_LONG", label)); // NOI18N
			result = false;
		}
		if (label.length() == 0) {
			problems.add(NbBundle.getMessage(HostNameValidator.class, "LABEL_EMPTY", compName, label)); // NOI18N
		}
		if (result) {
			try {
				Integer.parseInt(label);
				problems.add(NbBundle.getMessage(HostNameValidator.class, "NUMBER_PART_IN_HOSTNAME", label)); // NOI18N
				result = false;
			} catch (final NumberFormatException e) {
				// do nothing
			}
			if (result) {
				if (result) {
					result = new EncodableInCharsetValidator().validate(problems, compName, label);
					if (result) {
						for (final char c : label.toLowerCase().toCharArray()) {
							if (c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '-') { // NOI18N
								continue;
							}
							problems.add(NbBundle.getMessage(HostNameValidator.class, "BAD_CHAR_IN_HOSTNAME", new String(new char[] { c }))); // NOI18N
							result = false;
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		if (model.length() == 0) {
			problems.add(NbBundle.getMessage(HostNameValidator.class, "INVALID_HOST_NAME", compName, model)); // NOI18N
			return false;
		}
		if (model.startsWith(".") || model.endsWith(".")) { // NOI18N
			problems.add(NbBundle.getMessage(IpAddressValidator.class, "HOST_STARTS_OR_ENDS_WITH_PERIOD", model)); // NOI18N
			return false;
		}
		final String[] parts = model.split("\\.");
		if (parts.length > 4) {
			problems.add(NbBundle.getMessage(IpAddressValidator.class, "TOO_MANY_LABELS", model)); // NOI18N
			return false;
		}
		if (!this.allowPort && model.contains(":")) { // NOI18N
			problems.add(NbBundle.getMessage(HostNameValidator.class, "MSG_PORT_NOT_ALLOWED", compName, model)); // NOI18N
			return false;
		}
		final boolean result = new MayNotContainSpacesValidator().validate(problems, compName, model);
		if (!result) {
			return false;
		}
		if (model.endsWith("-") || model.startsWith("-")) {
			problems.add(NbBundle.getMessage(HostNameValidator.class, "INVALID_HOST_NAME", compName, model)); // NOI18N
			return false;
		}

		for (int i = 0; i < parts.length; i++) {
			final String label = parts[i];
			if (label.length() > 63) {
				problems.add(NbBundle.getMessage(HostNameValidator.class, "LABEL_TOO_LONG", label)); // NOI18N
				return false;
			}
			if (i == parts.length - 1 && label.indexOf(":") > 0) {
				final String[] labelAndPort = label.split(":");
				if (labelAndPort.length > 2) {
					problems.add(NbBundle.getMessage(HostNameValidator.class, "INVALID_PORT", compName, label)); // NOI18N
					return false;
				}
				if (labelAndPort.length == 1) {
					problems.add(NbBundle.getMessage(HostNameValidator.class, "INVALID_PORT", compName, "''")); // NOI18N
					return false;
				} else {
					if (label.endsWith(":")) {
						problems.add(NbBundle.getMessage(HostNameValidator.class, "TOO_MANY_COLONS", compName, label)); // NOI18N
						return false;
					}
					try {
						final int port = Integer.parseInt(labelAndPort[1]);
						if (port < 0) {
							problems.add(NbBundle.getMessage(IpAddressValidator.class, "NEGATIVE_PORT", port)); // NOI18N
							return false;
						} else if (port >= 65536) {
							problems.add(NbBundle.getMessage(IpAddressValidator.class, "PORT_TOO_HIGH", port)); // NOI18N
							return false;
						}
					} catch (final NumberFormatException e) {
						problems.add(NbBundle.getMessage(HostNameValidator.class, "INVALID_PORT", compName, labelAndPort[1])); // NOI18N
						return false;
					}
					if (!checkHostPart(labelAndPort[0], problems, compName)) {
						return false;
					}
				}
			} else {
				if (!checkHostPart(label, problems, compName)) {
					return false;
				}
			}
		}
		return true;
	}
}
