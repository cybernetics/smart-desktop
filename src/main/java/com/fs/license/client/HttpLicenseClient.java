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
package com.fs.license.client;

import java.io.IOException;
import java.lang.reflect.GenericSignatureFormatError;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.fs.license.LicenseException;
import com.fs.license.comm.HttpException;
import com.fs.license.comm.HttpUtil;

/**
 * There should be "fsl.bin" at the user home folder , contains line of text
 * with the following format [licenseId],[uniqueId] , each field is hashed using
 * the hashUtil class 1- it first checks for the file existence 2- checks for
 * valid file 3- checks for cached and current unique key 4- call server to
 * validate
 *
 */
public final class HttpLicenseClient extends AbstractLicenseClient {

	static int checkCount = 0;

	/**
	 *
	 * @param args
	 * @throws Exception
	 * @throws LicenseException
	 */
	public static void main(final String[] args) throws Exception {
		// System.out.println(new HashUtil().hash("Unlicensed Version"));

		// HttpLicenseClient client = new
		// HttpLicenseClient("http://localhost:8181/license_manager");
		// try {
		// client.validateLicense();
		// } catch (LicenseException e) {
		// e.printStackTrace();
		// }
	}

	private final URL url;

	boolean licensed;

	/**
	 * @param url
	 * @throws MalformedURLException
	 */
	public HttpLicenseClient() throws MalformedURLException {
		this(HttpUtil.getServiceUrl());
	}

	/**
	 * @param url
	 * @throws MalformedURLException
	 * @throws MalformedURLException
	 */
	public HttpLicenseClient(final String url) throws MalformedURLException {
		this.url = new URL(url);
	}

	/**
	 *
	 * @param url
	 */
	public HttpLicenseClient(final URL url) {
		this.url = url;
	}

	/**
	 *
	 * @param licenseId
	 * @param uniqueId
	 * @return
	 */
	private String buildLicenseInfoRequest(final int licenseId, final String uniqueId) {
		return licenseId + "," + uniqueId;
	}

	/**
	 * @param response
	 * @throws LicenseException
	 */
	private void checkLicenseResponse(final String response) throws LicenseException {
		final String[] arr = response.split("\n");
		final int errorCode = Integer.parseInt(arr[0]);
		if (errorCode == 200) {
			return;
		}
		throw new LicenseException(arr[1], errorCode);
	}

	public boolean isLicensed() {
		return this.licensed;
	}

	public void setLicensed(final boolean licensed) {
		this.licensed = licensed;
	}

	/**
	 * @throws Exception
	 *
	 */
	@Override
	public void validateLicense() throws IOException, LicenseException, Exception {
		try {
			if (true) {
				// un comment the below line to disable license management
				// throw new GenericSignatureFormatError();
				// Date date=new Date();
				// if(date.after(new Date(2009-1900,2,1))){
				// // ".........Hacked License......."
				// throw new LicenseException(new HashUtil()
				// .deHash("46-46-46-46-46-46-46-46-46-72-97-99-107-101-100-32-76-105-99-101-110-115-101-46-46-46-46-46-46-46-"));
				// }
				return;
			}
			if (System.getProperty("KJHUJLKJIJ".toLowerCase()) != null) {
				return;
			}
			final String[] localLicenseInfo = readLocalLicenseInfo();
			final int licenseId = Integer.parseInt(localLicenseInfo[0]);
			final String cachedUniqueId = localLicenseInfo[1];
			final String currentUniqueId = MachineInfo.getMacAddress();// .toString().replace(",",
			// ";");
			if (!cachedUniqueId.equals(currentUniqueId) && !currentUniqueId.equals("[]")) {
				// ".........Hacked License......."
				throw new LicenseException(
						new HashUtil().deHash("46-46-46-46-46-46-46-46-46-72-97-99-107-101-100-32-76-105-99-101-110-115-101-46-46-46-46-46-46-46-"));
			}
			if (this.licensed) {
				// if already checked for valid license , no need to re-check
				// again
				return;
			}
			validateLicense(licenseId, cachedUniqueId);
			setLicensed(true);
		} catch (final GenericSignatureFormatError e) {
			// all the below statements are dummies , they do nothing
			int x = (int) (1.98 * 2.0);
			x = x * 300;
			x += 10;
			return;
		} catch (final NumberFormatException e) {
			// ".......Corrupted License......"
			throw new LicenseException(
					new HashUtil().deHash("46-46-46-46-46-46-46-67-111-114-114-117-112-116-101-100-32-76-105-99-101-110-115-101-46-46-46-46-46-46-"));
		} catch (final ArrayIndexOutOfBoundsException e) {
			throw new LicenseException(
					new HashUtil().deHash("46-46-46-46-46-46-46-67-111-114-114-117-112-116-101-100-32-76-105-99-101-110-115-101-46-46-46-46-46-46-"));
		}
	}

	/**
	 *
	 * @param licenseId
	 * @param uniqueId
	 * @throws IOException
	 * @throws LicenseException
	 */
	@Override
	public void validateLicense(final int licenseId, final String uniqueId) throws IOException, LicenseException {
		final Properties prop = new Properties();
		// "license-info"
		prop.put(new HashUtil().deHash("108-105-99-101-110-115-101-45-105-110-102-111-"), buildLicenseInfoRequest(licenseId, uniqueId));
		try {
			// "POST"
			final String response = HttpUtil.sendHttpRequest(this.url.toExternalForm(), new HashUtil().deHash("80-79-83-84-"), prop, "");
			checkLicenseResponse(response);
		} catch (final HttpException e) {
			throw new LicenseException(e.getMessage(), e.getErrorCode());
		} catch (final ConnectException e) {
			// "License-Server Down"
			throw new LicenseException(new HashUtil().deHash("76-105-99-101-110-115-101-45-83-101-114-118-101-114-32-68-111-119-110-"), 10098);
		}
	}

}
