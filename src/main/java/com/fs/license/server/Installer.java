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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.fs.license.InvalidLicenseException;
import com.fs.license.License;
import com.fs.license.LicenseAlreadyExistsException;
import com.fs.license.client.HashUtil;
import com.fs.license.comm.HttpUtil;

public class Installer {
	public static Date SEX_MONTHS_DATE = new Date(System.currentTimeMillis() + 30758400000L / 2);

	/**
	 * @param clientLicense
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InvalidLicenseException
	 * @throws Exception
	 * @throws LicenseAlreadyExistsException
	 */
	public static void importFile(final File clientLicense, final Date expiryDate)
			throws IOException, FileNotFoundException, InvalidLicenseException, Exception, LicenseAlreadyExistsException {
		final String data = HttpUtil.readStream(new FileInputStream(clientLicense));
		final String[] licenseInfo = data.split(",");
		final HashUtil hash = new HashUtil();
		for (int i = 0; i < licenseInfo.length; i++) {
			licenseInfo[i] = hash.deHash(licenseInfo[i]);
		}

		final License license = new License();
		license.setLicenseId(Integer.parseInt(licenseInfo[0]));
		license.setUserUniqueId(licenseInfo[1]);
		// the license will expire after 6 months
		license.setExpiryDate(expiryDate);
		license.setUserName("");
		license.setEnabled(true);
		final LicenseRepository instance = LicenseRepository.getInstance(false);
		instance.addLicense(license);
		instance.saveLicenses();
		// file.delete();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		if (args.length == 1) {
			final String[] fileNames = args[0].split(",");
			for (final String fileName : fileNames) {
				final File file = new File(fileName);
				importFile(file, SEX_MONTHS_DATE);
			}
		} else {
			System.err.println("No License Files found");
		}
	}

	/**
	 * @return
	 * @throws Exception
	 * @throws IOException
	 * @throws InvalidLicenseException
	 */
	public static String readServerLicenseRepositoroy() throws InvalidLicenseException, IOException, Exception {
		final LicenseRepository repo = new com.fs.license.server.LicenseRepository(false);
		final ArrayList<License> licenes = repo.lstLicenses();
		final StringBuffer buffer = new StringBuffer("License Id || User Name || Unique Id || Expiry Date \n");
		buffer.append("===========================================================\n");
		for (int i = 0; i < licenes.size(); i++) {
			final License lic = licenes.get(i);
			buffer.append(lic.getLicenseId() + " || " + lic.getUserName() + " || " + lic.getUserUniqueId() + " || " + lic.getExpiryDate());
			buffer.append("\n");
		}
		return buffer.toString();
	}
}
