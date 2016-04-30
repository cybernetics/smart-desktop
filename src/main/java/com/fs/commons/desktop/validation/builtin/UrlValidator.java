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

import java.net.MalformedURLException;
import java.net.URL;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
class UrlValidator implements Validator<String> {

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		try {
			final URL url = new URL(model);
			// java.net.url does not require US-ASCII host names,
			// but the spec does
			final String host = url.getHost();
			if (!"".equals(host)) { // NOI18N
				return new ValidHostNameOrIPValidator(true).validate(problems, compName, host);
			}
			final String protocol = url.getProtocol();
			if ("mailto".equals(protocol)) { // NOI18N
				String emailAddress = url.toString().substring("mailto:".length()); // NOI18N
				emailAddress = emailAddress == null ? "" : emailAddress;
				return new EmailAddressValidator().validate(problems, compName, emailAddress);
			}
			return true;
		} catch (final MalformedURLException e) {
			final String problem = NbBundle.getMessage(UrlValidator.class, "URL_NOT_VALID", model); // NOI18N
			problems.add(problem);
			return false;
		}
	}

}
