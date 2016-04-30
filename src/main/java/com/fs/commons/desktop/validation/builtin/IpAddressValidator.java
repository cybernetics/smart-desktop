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
final class IpAddressValidator implements Validator<String> {

	@Override
	public boolean validate(final Problems problems, final String compName, final String s) {
		if (s.startsWith(".") || s.endsWith(".")) { // NOI18N
			problems.add(NbBundle.getMessage(IpAddressValidator.class, "HOST_STARTS_OR_ENDS_WITH_PERIOD", s)); // NOI18N
			return false;
		}
		if (s.indexOf(" ") >= 0 || s.indexOf("\t") >= 0) {
			problems.add(NbBundle.getMessage(IpAddressValidator.class, "IP_ADDRESS_CONTAINS_WHITESPACE", compName, s)); // NOI18N
			return false;
		}
		final String[] parts = s.split("\\.");
		if (parts.length > 4) {
			problems.add(NbBundle.getMessage(IpAddressValidator.class, "TOO_MANY_LABELS", s)); // NOI18N
			return false;
		}
		for (int i = 0; i < parts.length; i++) {
			final String part = parts[i];
			if (i == parts.length - 1 && part.indexOf(":") > 0) { // NOI18N
				final String[] pts = part.split(":"); // NOI18N
				try {
					final int addr = Integer.parseInt(pts[0]);
					if (addr < 0) {
						problems.add(NbBundle.getMessage(IpAddressValidator.class, "ADDR_PART_NEGATIVE", pts[1])); // NOI18N
						return false;
					}
					if (addr > 256) {
						problems.add(NbBundle.getMessage(IpAddressValidator.class, "ADDR_PART_HIGH", pts[1])); // NOI18N
						return false;
					}
				} catch (final NumberFormatException e) {
					problems.add(NbBundle.getMessage(IpAddressValidator.class, "ADDR_PART_BAD", pts.length >= 2 ? pts[1] : "''")); // NOI18N
					return false;
				}
				if (pts.length == 2 && pts[1].length() == 0) {
					problems.add(NbBundle.getMessage(IpAddressValidator.class, "INVALID_PORT", compName, "")); // NOI18N
					return false;
				}
				if (pts.length > 1) {
					try {
						final int port = Integer.parseInt(pts[1]);
						if (port < 0) {
							problems.add(NbBundle.getMessage(IpAddressValidator.class, "NEGATIVE_PORT", pts[1])); // NOI18N
							return false;
						} else if (port >= 65536) {
							problems.add(NbBundle.getMessage(IpAddressValidator.class, "PORT_TOO_HIGH", pts[1])); // NOI18N
							return false;
						}
					} catch (final NumberFormatException e) {
						problems.add(NbBundle.getMessage(IpAddressValidator.class, "INVALID_PORT", compName, pts[1])); // NOI18N
						return false;
					}
				}
			} else {
				try {
					final int addr = Integer.parseInt(part);
					if (addr < 0) {
						problems.add(NbBundle.getMessage(IpAddressValidator.class, "ADDR_PART_NEGATIVE", part)); // NOI18N
						return false;
					}
					if (addr > 256) {
						problems.add(NbBundle.getMessage(IpAddressValidator.class, "ADDR_PART_HIGH", part)); // NOI18N
						return false;
					}
				} catch (final NumberFormatException e) {
					problems.add(NbBundle.getMessage(IpAddressValidator.class, "ADDR_PART_BAD", part)); // NOI18N
					return false;
				}
			}
		}
		return true;
	}
}
