package com.fs.license.server;

import java.io.IOException;
import java.util.Date;

import com.fs.license.License;
import com.fs.license.LicenseException;

public final class LicenseFacade {

	/**
	 * 
	 */
	protected void validateLicense(int licenseId, String uniqueId) throws LicenseException, Exception {
		try {
			LicenseRepository reposiory = LicenseRepository.getInstance(true);
			License localLicense = reposiory.findLicenseByUniqueId(uniqueId);
			if (!uniqueId.equals(localLicense.getUserUniqueId())) {
				throw new LicenseException("Invalid License", LicenseConstants.ERROR_INVALID_USER_UNIQUE_ID);
			}
			if (new Date().after(localLicense.getExpiryDate())) {
				LicenseException licenseException = new LicenseException("License expired", LicenseConstants.ERROR_LICENSE_EXPIRED);
				localLicense.setEnabled(false);
				reposiory.saveLicenses();
				throw licenseException;
			}
			if (!localLicense.isEnabled()) {
				throw new LicenseException("licenses is disabled ", LicenseConstants.ERROR_LICENSE_DISABLED);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
