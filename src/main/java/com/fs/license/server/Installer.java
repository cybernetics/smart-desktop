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
	public static Date SEX_MONTHS_DATE = new Date(System.currentTimeMillis()+30758400000L/2);
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if(args.length==1){
			String []fileNames=args[0].split(",");
			for (int i = 0; i < fileNames.length; i++) {
				File file=new File(fileNames[i]);				
				importFile(file,SEX_MONTHS_DATE);
			}			
		}else{
			System.err.println("No License Files found");
		}
	}

	/**
	 * @param clientLicense
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InvalidLicenseException
	 * @throws Exception
	 * @throws LicenseAlreadyExistsException
	 */
	public static void importFile(File clientLicense,Date expiryDate) throws IOException,FileNotFoundException, InvalidLicenseException, Exception,LicenseAlreadyExistsException {
		String data=HttpUtil.readStream(new FileInputStream(clientLicense));
		String[] licenseInfo=data.split(",");
		HashUtil hash = new HashUtil();
		for (int i = 0; i < licenseInfo.length; i++) {
			licenseInfo[i] = hash.deHash(licenseInfo[i]);
		}

		License license=new License();
		license.setLicenseId(Integer.parseInt(licenseInfo[0]));
		license.setUserUniqueId(licenseInfo[1]);
		//the license will expire after 6 months
		license.setExpiryDate(expiryDate);
		license.setUserName("");
		license.setEnabled(true);
		LicenseRepository instance = LicenseRepository.getInstance(false);
		instance.addLicense(license);
		instance.saveLicenses();
		//file.delete();
	}

	/**
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 * @throws InvalidLicenseException 
	 */
	public static String readServerLicenseRepositoroy() throws InvalidLicenseException, IOException, Exception {
		LicenseRepository repo= new com.fs.license.server.LicenseRepository(false);
		ArrayList<License> licenes=repo.lstLicenses();
		StringBuffer buffer=new StringBuffer(("License Id || User Name || Unique Id || Expiry Date \n" ));
		buffer.append("===========================================================\n");
		for (int i = 0; i < licenes.size(); i++) {
			License lic=licenes.get(i);
			buffer.append(lic.getLicenseId()+" || "+lic.getUserName()+" || "+lic.getUserUniqueId()+" || "+lic.getExpiryDate());
			buffer.append("\n");
		}
		return buffer.toString();
	}
}
