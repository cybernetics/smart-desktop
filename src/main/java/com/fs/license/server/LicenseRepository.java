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
	private ArrayList<License> licenses = new ArrayList<License>();
	public static String FILE_NAME = USER_HOME + COMPANY_NAME;
	// public static String FILE_NAME = COMPANY_NAME;
	EncDec encDec;

	/**
	 * @param b
	 * @return
	 * @throws IOException
	 * @throws InvalidLicenseException
	 */
	protected static LicenseRepository getInstance(boolean exitIfNoConfigAvailable) throws IOException, InvalidLicenseException, Exception {
		if (instance == null) {
			instance = new LicenseRepository(exitIfNoConfigAvailable);
		}
		return instance;
	}

	/**
	 * @param exitIfNoConfigAvailable
	 * @throws InvalidLicenseException
	 * @throws FileNotFoundException
	 * 
	 */
	LicenseRepository(boolean exitIfNoConfigAvailable) throws IOException, InvalidLicenseException, Exception {
		encDec = new EncDec(COMPANY_NAME.getBytes());
		System.out.println(FILE_NAME);
		if (new File(FILE_NAME).exists()) {
			FileInputStream inputStream = new FileInputStream(FILE_NAME);
			load(inputStream);
		} else {
			System.err.println("NO LICENSE FILE IS AVAILABLE AT " + FILE_NAME);
			if (exitIfNoConfigAvailable) {
				System.err.println("THE SYSTEM WILL EXIT");
				System.exit(0);
			}
		}
	}

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
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws InvalidLicenseException
	 */
	private synchronized void load(InputStream inputStream) throws IOException, InvalidLicenseException, Exception {
		int b;
		try {
			DataInputStream in = new DataInputStream(inputStream);
			while ((b = in.read()) != -1) {
				// first byte on each license is the encrypted license length
				License license = parseLicense(b, in);
				licenses.add(license);
			}
		} finally {
			inputStream.close();
		}
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
	private License parseLicense(int licenseSize, DataInputStream in) throws IOException, Exception, InvalidLicenseException {
		byte[] licenseEncBytes = new byte[licenseSize];
		in.readFully(licenseEncBytes);
		byte[] licenseDecBytes = encDec.decrypt(licenseEncBytes);
		License license = License.parseLicense(licenseDecBytes);
		return license;
	}

	/**
	 * 
	 * @param licenseId
	 * @return
	 * @throws LicenseNotFoundException
	 */
	protected License findLicense(int licenseId) throws LicenseNotFoundException {
		for (int i = 0; i < licenses.size(); i++) {
			License license = licenses.get(i);
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
	protected License findLicenseByUniqueId(String uniqueId) throws LicenseNotFoundException {
		for (int i = 0; i < licenses.size(); i++) {
			License license = licenses.get(i);
			if (license.getUserUniqueId().equals(uniqueId)) {
				return license;
			}
		}
		throw new LicenseNotFoundException(" license with unquie id : " + uniqueId + " not found");
	}

	/*
	 * 
	 */
	protected void addLicense(License license) throws LicenseAlreadyExistsException {
		try {
			findLicense(license.getLicenseId());
			throw new LicenseAlreadyExistsException("License with id : " + license.getLicenseId() + " already exists");
		} catch (LicenseNotFoundException e) {
			try {
				// remove the old license of already exists
				License foundLicese = findLicenseByUniqueId(license.getUserUniqueId());
				licenses.remove(foundLicese);
				// throw new LicenseAlreadyExistsException(
				// "License with unque id : "+license.getUserUniqueId()+" already exists"
				// );
			} catch (LicenseNotFoundException e1) {
				licenses.add(license);
			}
		}
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
	protected synchronized void saveLicenses(OutputStream outputStream) throws Exception {
		try {
			for (int i = 0; i < licenses.size(); i++) {
				License license = licenses.get(i);
				writeLicense(license, outputStream);
				// outputStream.write(LICENSE_TERMINATION_CHAR);
			}
		} catch (IOException e) {
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
	private synchronized void writeLicense(License license, OutputStream outputStream) throws Exception {
		byte[] licenseBytes = license.toByteArray();
		licenseBytes = encDec.encrypt(licenseBytes);

		outputStream.write(licenseBytes.length);
		outputStream.write(licenseBytes);
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		License license = new License();
		license.setLicenseId(101);
		license.setUserUniqueId("12345678923");
		license.setUserName("admin1");
		license.setExpiryDate(new Date());
		license.setEnabled(true);

		LicenseRepository repo = LicenseRepository.getInstance(true);
		repo.addLicense(license);
		repo.saveLicenses();
		System.out.println("license-saved-succ");
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<License> lstLicenses() {
		return licenses;
	}
}
