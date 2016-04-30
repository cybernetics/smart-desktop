package com.fs.license.client;

import java.io.IOException;

import com.fs.license.LicenseException;

public interface LicenseClient {
	
	public void validateLicense() throws IOException, LicenseException, Exception;
	/**
	 * 
	 * @param licenseId
	 * @param uniqueId
	 * @throws IOException
	 * @throws LicenseException
	 */
	public void validateLicense(int licenseId, String uniqueId) throws IOException, LicenseException ;
}
