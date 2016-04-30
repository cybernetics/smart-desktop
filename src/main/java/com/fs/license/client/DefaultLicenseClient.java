package com.fs.license.client;

import java.io.IOException;

import com.fs.license.LicenseException;

public class DefaultLicenseClient extends AbstractLicenseClient{

	@Override
	public void validateLicense() throws IOException, LicenseException, Exception {		
	}

	@Override
	public void validateLicense(int licenseId, String uniqueId) throws IOException, LicenseException {		
	}

}
