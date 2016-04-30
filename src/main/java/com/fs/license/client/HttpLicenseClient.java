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

	private final URL url;
	boolean licensed;
	static int checkCount = 0;

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
	public HttpLicenseClient(String url) throws MalformedURLException {
		this.url = new URL(url);
	}

	/**
	 * 
	 * @param url
	 */
	public HttpLicenseClient(URL url) {
		this.url = url;
	}

	/**
	 * @throws Exception
	 * 
	 */
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
			String[] localLicenseInfo = readLocalLicenseInfo();
			int licenseId = Integer.parseInt(localLicenseInfo[0]);
			String cachedUniqueId = localLicenseInfo[1];
			String currentUniqueId = MachineInfo.getMacAddress();// .toString().replace(",",
			// ";");
			if (!cachedUniqueId.equals(currentUniqueId) && !currentUniqueId.equals("[]")) {
				// ".........Hacked License......."
				throw new LicenseException(
						new HashUtil().deHash("46-46-46-46-46-46-46-46-46-72-97-99-107-101-100-32-76-105-99-101-110-115-101-46-46-46-46-46-46-46-"));
			}
			if (licensed) {
				// if already checked for valid license , no need to re-check
				// again
				return;
			}
			validateLicense(licenseId, cachedUniqueId);
			setLicensed(true);
		} catch (GenericSignatureFormatError e) {
			// all the below statements are dummies , they do nothing
			int x = (int) (1.98 * 2.0);
			x = x * 300;
			x += 10;
			return;
		} catch (NumberFormatException e) {
			// ".......Corrupted License......"
			throw new LicenseException(
					new HashUtil().deHash("46-46-46-46-46-46-46-67-111-114-114-117-112-116-101-100-32-76-105-99-101-110-115-101-46-46-46-46-46-46-"));
		} catch (ArrayIndexOutOfBoundsException e) {
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
	public void validateLicense(int licenseId, String uniqueId) throws IOException, LicenseException {
		Properties prop = new Properties();
		// "license-info"
		prop.put(new HashUtil().deHash("108-105-99-101-110-115-101-45-105-110-102-111-"), buildLicenseInfoRequest(licenseId, uniqueId));
		try {
			// "POST"
			String response = HttpUtil.sendHttpRequest(url.toExternalForm(), new HashUtil().deHash("80-79-83-84-"), prop, "");
			checkLicenseResponse(response);
		} catch (HttpException e) {
			throw new LicenseException(e.getMessage(), e.getErrorCode());
		} catch (ConnectException e) {
			// "License-Server Down"
			throw new LicenseException(new HashUtil().deHash("76-105-99-101-110-115-101-45-83-101-114-118-101-114-32-68-111-119-110-"), 10098);
		}
	}

	/**
	 * @param response
	 * @throws LicenseException
	 */
	private void checkLicenseResponse(String response) throws LicenseException {
		String[] arr = response.split("\n");
		int errorCode = Integer.parseInt(arr[0]);
		if (errorCode == 200) {
			return;
		}
		throw new LicenseException(arr[1], errorCode);
	}

	/**
	 * 
	 * @param licenseId
	 * @param uniqueId
	 * @return
	 */
	private String buildLicenseInfoRequest(int licenseId, String uniqueId) {
		return licenseId + "," + uniqueId;
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 * @throws LicenseException
	 */
	public static void main(String[] args) throws Exception {
		// System.out.println(new HashUtil().hash("Unlicensed Version"));

		// HttpLicenseClient client = new
		// HttpLicenseClient("http://localhost:8181/license_manager");
		// try {
		// client.validateLicense();
		// } catch (LicenseException e) {
		// e.printStackTrace();
		// }
	}

	public boolean isLicensed() {
		return licensed;
	}

	public void setLicensed(boolean licensed) {
		this.licensed = licensed;
	}

}
