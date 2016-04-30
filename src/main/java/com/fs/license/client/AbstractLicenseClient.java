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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.fs.commons.util.GeneralUtility;
import com.fs.license.LicenseException;
import com.fs.license.comm.HttpUtil;

public abstract class AbstractLicenseClient implements LicenseClient {
	/**
	 *
	 * @return
	 */
	protected static String getLicenseFileName() {
		// System.getProperty("user.home")+System.getProperty("file.separator")
		// String name="67-58-92-85-115-101-114-115-92-106-97-108-97-108-92-";
		// String
		// name=System.getProperty("user.home")+System.getProperty("file.separator");
		// // "fsl.lic"
		// name+="102-115-108-46-108-105-99-";
		// return new HashUtil().deHash(name);
		return GeneralUtility.getUserFolderPath(true) + GeneralUtility.getLocalHostName() + ".lic";
	}

	/**
	 *
	 * @return
	 */
	protected static String getLicenseFullFileName() {
		return getLicenseFileName();
	}

	/**
	 *
	 * @return
	 * @throws LicenseException
	 * @throws IOException
	 */
	protected String[] readLocalLicenseInfo() throws LicenseException, IOException {
		final File file = new File(getLicenseFullFileName());
		if (!file.exists()) {
			try {
				Installer.main(null);
			} catch (final Exception e) {
				// "Unlicensed Version"
				throw new LicenseException(new HashUtil().deHash("85-110-108-105-99-101-110-115-101-100-32-86-101-114-115-105-111-110-"));
			}
		}
		final FileInputStream in = new FileInputStream(file);
		final String info = HttpUtil.readStream(in);
		final String[] infoArr = info.split(",");
		final HashUtil hash = new HashUtil();
		for (int i = 0; i < infoArr.length; i++) {
			infoArr[i] = hash.deHash(infoArr[i]);
		}
		return infoArr;
	}

}
