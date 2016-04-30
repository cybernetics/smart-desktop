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
package com.fs.license;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fs.license.client.HashUtil;

public class License {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

	/**
	 *
	 * @param args
	 * @throws InvalidLicenseException
	 */
	public static void main(final String[] args) throws InvalidLicenseException {
	}

	/**
	 *
	 * @param string
	 * @return
	 * @throws ParseException
	 */
	protected static Date parseDate(final String string) throws ParseException {
		return format.parse(string);
	}

	/**
	 *
	 * @param License
	 * @return
	 * @throws InvalidLicenseException
	 */
	public static License parseLicense(final byte[] License) throws InvalidLicenseException {
		final String licenseText = new String(License);
		final String[] arr = licenseText.split(",");
		if (arr.length != 5) {
			// invalid license bytes , length =
			throw new InvalidLicenseException(new HashUtil()
					.deHash("105-110-118-97-108-105-100-32-108-105-99-101-110-115-101-32-98-121-116-101-115-32-44-32-108-101-110-103-116-104-32-61-")
					+ arr.length);
		}
		final License license = new License();
		license.setLicenseId(Integer.parseInt(arr[0]));
		license.setUserUniqueId(arr[1]);
		license.setUserName(arr[2]);
		try {
			license.setExpiryDate(parseDate(arr[3]));
		} catch (final ParseException e) {
			// Invalid license , expiry date =
			throw new InvalidLicenseException(new HashUtil().deHash(
					"73-110-118-97-108-105-100-32-108-105-99-101-110-115-101-32-44-32-101-120-112-105-114-121-32-100-97-116-101-32-32-61-") + arr[3]);
		}
		license.setEnabled(Boolean.parseBoolean(arr[4]));
		return license;
	}

	int licenseId;
	String userUniqueId;

	String userName;

	Date expiryDate;

	boolean enabled;

	/**
	 *
	 * @param date
	 * @return
	 */
	protected String formatDate(final Date date) {
		return format.format(date);
	}

	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return this.expiryDate;
	}

	/**
	 * @return the licenseId
	 */
	public int getLicenseId() {
		return this.licenseId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @return the userUniqueId
	 */
	public String getUserUniqueId() {
		return this.userUniqueId;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param expiryDate
	 *            the expiryDate to set
	 */
	public void setExpiryDate(final Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @param licenseId
	 *            the licenseId to set
	 */
	public void setLicenseId(final int licenseId) {
		this.licenseId = licenseId;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @param userUniqueId
	 *            the userUniqueId to set
	 */
	public void setUserUniqueId(final String userUniqueId) {
		this.userUniqueId = userUniqueId.replace(",", ";");// in case of mutiple
															// macs
	}

	/**
	 *
	 * @return
	 */
	public byte[] toByteArray() {
		final String line = getLicenseId() + "," + getUserUniqueId() + "," + getUserName() + "," + formatDate(getExpiryDate()) + "," + isEnabled()
				+ "";
		return line.getBytes();
	}

}
