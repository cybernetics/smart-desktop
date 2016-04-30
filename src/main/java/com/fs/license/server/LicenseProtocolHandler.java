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
	 * the license validation request should be like this licenseId&userUniqueId
	 * 
	 * @return
	 */
	public String validateLicense(String licenseValidationRequest) {
		String[] arr = licenseValidationRequest.split(",");
		LicenseFacade facade = new LicenseFacade();
		try {
			facade.validateLicense(Integer.parseInt(arr[0]), arr[1]);
			return buildLicenseValidationResponse("Success", LicenseConstants.LICENSE_IS_OK);
		} catch (ArrayIndexOutOfBoundsException e) {
			return buildLicenseValidationResponse("Invalid Request String : " + licenseValidationRequest,
					LicenseConstants.ERROR_INVALID_LICENSE_REQUEST);
		} catch (NumberFormatException e) {
			return buildLicenseValidationResponse("Invalid License Id : " + arr[0], LicenseConstants.ERROR_INVALID_LICENSE_REQUEST);
		} catch (LicenseException e) {
			return buildLicenseValidationResponse(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			return buildLicenseValidationResponse(e.getMessage(), LicenseConstants.ERROR_LICENSE_EXCEPTION);
		}
	}

	/**
	 * 
	 * @param string
	 * @param license_is_ok
	 * @return
	 */
	private String buildLicenseValidationResponse(String message, int errorCode) {
		return errorCode + "\n" + message;
	}
}
