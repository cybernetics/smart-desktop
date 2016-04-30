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
package com.fs.license.server;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LicenseServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		final String licenseInfo = req.getHeader("license-info");
		if (licenseInfo == null || licenseInfo.trim().equals("")) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid License Request, License info unavailable");
		} else {
			final String reponse = LicenseProtocolHandler.getInstance().validateLicense(licenseInfo);
			final Writer out = resp.getWriter();
			out.write(reponse);
			out.flush();
		}
	}
}
