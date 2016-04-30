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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import com.fs.license.InvalidLicenseException;
import com.fs.license.License;
import com.fs.license.LicenseAlreadyExistsException;
import com.fs.license.LicenseNotFoundException;

public final class LicenseRepository {
	private static final String USER_HOME = System.getProperty("user.home") + System.getProperty("file.separator");
	private static final String COMPANY_NAME = "FINAL-SOLUTIONS-SOFTWARE";
	// private static final char LICENSE_TERMINATION_CHAR = '$';
	private static LicenseRepository instance;
	public static String FILE_NAME = USER_HOME + COMPANY_NAME;

	/**
	 * @param b
	 * @return
	 * @throws IOException
	 * @throws InvalidLicenseException
	 */
	protected static LicenseRepository getInstance(final boolean exitIfNoConfigAvailable) throws IOException, InvalidLicenseException, Exception {
		if (instance == null) {
			instance = new LicenseRepository(exitIfNoConfigAvailable);
		}
		return instance;
	}

	/**
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		final License license = new License();
		license.setLicenseId(101);
		license.setUserUniqueId("12345678923");
		license.setUserName("admin1");
		license.setExpiryDate(new Date());
		license.setEnabled(true);

		final LicenseRepository repo = LicenseRepository.getInstance(true);
		repo.addLicense(license);
		repo.saveLicenses();
		System.out.println("license-saved-succ");
	}

	private final ArrayList<License> licenses = new ArrayList<License>();

	// public static String FILE_NAME = COMPANY_NAME;
	EncDec encDec;

	// /**
	// *
	// * @param inputStream
	// * @throws IOException
	// * @throws InvalidLicenseException
	// */
	// private LicenseRepository(InputStream inputStream)
	// throws InvalidLicenseException, IOException, Exception {
	// load(inputStream);
	// }

	/**
	 * @param exitIfNoConfigAvailable
	 * @throws InvalidLicenseException
	 * @throws FileNotFoundException
	 *
	 */
	LicenseRepository(final boolean exitIfNoConfigAvailable) throws IOException, InvalidLicenseException, Exception {
		this.encDec = new EncDec(COMPANY_NAME.getBytes());
		System.out.println(FILE_NAME);
		if (new File(FILE_NAME).exists()) {
			final FileInputStream inputStream = new FileInputStream(FILE_NAME);
			load(inputStream);
		} else {
			System.err.println("NO LICENSE FILE IS AVAILABLE AT " + FILE_NAME);
			if (exitIfNoConfigAvailable) {
				System.err.println("THE SYSTEM WILL EXIT");
				System.exit(0);
			}
		}
	}

	/*
	 *
	 */
	protected void addLicense(final License license) throws LicenseAlreadyExistsException {
		try {
			findLicense(license.getLicenseId());
			throw new LicenseAlreadyExistsException("License with id : " + license.getLicenseId() + " already exists");
		} catch (final LicenseNotFoundException e) {
			try {
				// remove the old license of already exists
				final License foundLicese = findLicenseByUniqueId(license.getUserUniqueId());
				this.licenses.remove(foundLicese);
				// throw new LicenseAlreadyExistsException(
				// "License with unque id : "+license.getUserUniqueId()+"
				// already exists"
				// );
			} catch (final LicenseNotFoundException e1) {
				this.licenses.add(license);
			}
		}
	}

	/**
	 *
	 * @param licenseId
	 * @return
	 * @throws LicenseNotFoundException
	 */
	protected License findLicense(final int licenseId) throws LicenseNotFoundException {
		for (int i = 0; i < this.licenses.size(); i++) {
			final License license = this.licenses.get(i);
			if (license.getLicenseId() == licenseId) {
				return license;
			}
		}
		throw new LicenseNotFoundException(" license with id : " + licenseId + " not found");
	}

	/**
	 *
	 * @param uniqueId
	 * @return
	 * @throws LicenseNotFoundException
	 */
	protected License findLicenseByUniqueId(final String uniqueId) throws LicenseNotFoundException {
		for (int i = 0; i < this.licenses.size(); i++) {
			final License license = this.licenses.get(i);
			if (license.getUserUniqueId().equals(uniqueId)) {
				return license;
			}
		}
		throw new LicenseNotFoundException(" license with unquie id : " + uniqueId + " not found");
	}

	/**
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws InvalidLicenseException
	 */
	private synchronized void load(final InputStream inputStream) throws IOException, InvalidLicenseException, Exception {
		int b;
		try {
			final DataInputStream in = new DataInputStream(inputStream);
			while ((b = in.read()) != -1) {
				// first byte on each license is the encrypted license length
				final License license = parseLicense(b, in);
				this.licenses.add(license);
			}
		} finally {
			inputStream.close();
		}
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<License> lstLicenses() {
		return this.licenses;
	}

	/**
	 *
	 * @param b
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws Exception
	 * @throws InvalidLicenseException
	 */
	private License parseLicense(final int licenseSize, final DataInputStream in) throws IOException, Exception, InvalidLicenseException {
		final byte[] licenseEncBytes = new byte[licenseSize];
		in.readFully(licenseEncBytes);
		final byte[] licenseDecBytes = this.encDec.decrypt(licenseEncBytes);
		final License license = License.parseLicense(licenseDecBytes);
		return license;
	}

	/**
	 * @throws Exception
	 * @throws FileNotFoundException
	 *
	 */
	protected synchronized void saveLicenses() throws Exception {
		saveLicenses(new FileOutputStream(FILE_NAME));
	}

	/**
	 *
	 * @param fileOutputStream
	 * @throws Exception
	 */
	protected synchronized void saveLicenses(final OutputStream outputStream) throws Exception {
		try {
			for (int i = 0; i < this.licenses.size(); i++) {
				final License license = this.licenses.get(i);
				writeLicense(license, outputStream);
				// outputStream.write(LICENSE_TERMINATION_CHAR);
			}
		} catch (final IOException e) {
			System.err.println("Failed to save licenses to :" + FILE_NAME);
			throw e;
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	/**
	 *
	 * @param license
	 * @param outputStream
	 * @throws Exception
	 */
	private synchronized void writeLicense(final License license, final OutputStream outputStream) throws Exception {
		byte[] licenseBytes = license.toByteArray();
		licenseBytes = this.encDec.encrypt(licenseBytes);

		outputStream.write(licenseBytes.length);
		outputStream.write(licenseBytes);
	}
}
