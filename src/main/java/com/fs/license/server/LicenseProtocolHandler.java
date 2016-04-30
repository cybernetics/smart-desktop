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

import com.fs.license.LicenseException;

public final class LicenseProtocolHandler {
	private static LicenseProtocolHandler instance;

	/**
	 *
	 */
	public static LicenseProtocolHandler getInstance() {
		if (instance == null) {
			instance = new LicenseProtocolHandler();
		}
		return instance;
	}

	/**
	 *
	 * @param string
	 * @param license_is_ok
	 * @return
	 */
	private String buildLicenseValidationResponse(final String message, final int errorCode) {
		return errorCode + "\n" + message;
	}

	/**
	 * the license validation request should be like this licenseId&userUniqueId
	 *
	 * @return
	 */
	public String validateLicense(final String licenseValidationRequest) {
		final String[] arr = licenseValidationRequest.split(",");
		final LicenseFacade facade = new LicenseFacade();
		try {
			facade.validateLicense(Integer.parseInt(arr[0]), arr[1]);
			return buildLicenseValidationResponse("Success", LicenseConstants.LICENSE_IS_OK);
		} catch (final ArrayIndexOutOfBoundsException e) {
			return buildLicenseValidationResponse("Invalid Request String : " + licenseValidationRequest,
					LicenseConstants.ERROR_INVALID_LICENSE_REQUEST);
		} catch (final NumberFormatException e) {
			return buildLicenseValidationResponse("Invalid License Id : " + arr[0], LicenseConstants.ERROR_INVALID_LICENSE_REQUEST);
		} catch (final LicenseException e) {
			return buildLicenseValidationResponse(e.getMessage(), e.getErrorCode());
		} catch (final Exception e) {
			return buildLicenseValidationResponse(e.getMessage(), LicenseConstants.ERROR_LICENSE_EXCEPTION);
		}
	}
}
