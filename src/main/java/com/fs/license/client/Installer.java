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

import com.fs.license.comm.HttpUtil;

public class Installer {

	/**
	 * Create two copies from the license file , the second copy will imported
	 * on the server
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		final int id = (int) System.currentTimeMillis();
		final String unqueId = MachineInfo.getMacAddress();// .toString().replace(",",
															// ";");
		final HashUtil hash = new HashUtil();
		final byte[] licenseBytes = (hash.hash(id + "") + "," + hash.hash(unqueId)).getBytes();

		HttpUtil.writeBytesToFile(HttpLicenseClient.getLicenseFullFileName(), licenseBytes);
		final String licenseFileName = GeneralUtility.getLocalHostName();
		final File folder = new File(com.fs.commons.util.GeneralUtility.getUserFolderPath(true) + "licenses");
		if (!folder.exists()) {
			folder.mkdir();
		}
		HttpUtil.writeBytesToFile(folder.getAbsolutePath() + "/License-" + licenseFileName + ".lic", licenseBytes);

		System.out.println("Licensed Installed succ");
	}
}
