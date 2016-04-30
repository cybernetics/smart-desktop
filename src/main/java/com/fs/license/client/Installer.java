package com.fs.license.client;

import java.io.File;

import com.fs.license.comm.HttpUtil;

public class Installer {
	
	/**
	 * Create two copies from the license file , the second copy will imported on the server
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		int id=(int) System.currentTimeMillis();
		String unqueId=MachineInfo.getMacAddress();//.toString().replace(",", ";");
		HashUtil hash=new HashUtil();
		byte[] licenseBytes = (hash.hash(id+"")+","+hash.hash(unqueId)).getBytes();
						
		HttpUtil.writeBytesToFile(HttpLicenseClient.getLicenseFullFileName(), licenseBytes);
		String licenseFileName=GeneralUtility.getLocalHostName();
		File folder=new File(com.fs.commons.util.GeneralUtility.getUserFolderPath(true)+ "licenses");
		if(!folder.exists()){
			folder.mkdir();
		}
		HttpUtil.writeBytesToFile(folder.getAbsolutePath()+"/License-"+licenseFileName+".lic", licenseBytes);
		
		System.out.println("Licensed Installed succ");
	}
}
