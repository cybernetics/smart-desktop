package com.fs.license;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fs.license.client.HashUtil;

public class License {	
	private static SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");	
	int licenseId;
	String userUniqueId;
	String userName;
	Date expiryDate;
	boolean enabled;

	/**
	 * @return the licenseId
	 */
	public int getLicenseId() {
		return licenseId;
	}

	/**
	 * @param licenseId
	 *            the licenseId to set
	 */
	public void setLicenseId(int licenseId) {
		this.licenseId = licenseId;
	}

	/**
	 * @return the userUniqueId
	 */
	public String getUserUniqueId() {
		return userUniqueId;
	}

	/**
	 * @param userUniqueId
	 *            the userUniqueId to set
	 */
	public void setUserUniqueId(String userUniqueId) {		
		this.userUniqueId = userUniqueId.replace(",", ";");//in case of mutiple macs
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate
	 *            the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 
	 * @return
	 */
	public byte[] toByteArray() {
		String line= getLicenseId()+","+getUserUniqueId()+","+getUserName()+","+ formatDate( getExpiryDate())+","+ isEnabled()+"";
		return line.getBytes();
	}
	
	/**
	 * 
	 * @param License
	 * @return
	 * @throws InvalidLicenseException 
	 */
	public static License parseLicense(byte[] License) throws InvalidLicenseException{
		String licenseText=new String(License);
		String[] arr = licenseText.split(",");
		if(arr.length!=5){			
			//invalid license bytes , length =
			throw new InvalidLicenseException(new HashUtil().deHash("105-110-118-97-108-105-100-32-108-105-99-101-110-115-101-32-98-121-116-101-115-32-44-32-108-101-110-103-116-104-32-61-")+arr.length);
		}
		License license=new License();		
		license.setLicenseId(Integer.parseInt(arr[0]));
		license.setUserUniqueId(arr[1]);
		license.setUserName(arr[2]);
		try {
			license.setExpiryDate(parseDate(arr[3]));
		} catch (ParseException e) {
			//Invalid license , expiry date  =
			throw new InvalidLicenseException(new HashUtil().deHash("73-110-118-97-108-105-100-32-108-105-99-101-110-115-101-32-44-32-101-120-112-105-114-121-32-100-97-116-101-32-32-61-")+arr[3]);
		}
		license.setEnabled(Boolean.parseBoolean(arr[4]));
		return license;
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 * @throws ParseException 
	 */
	protected static Date parseDate(String string) throws ParseException {
		return format.parse(string);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	protected String formatDate(Date date){		 
		return format.format(date);
	}
	
	/**
	 * 
	 * @param args
	 * @throws InvalidLicenseException 
	 */
	public static void main(String[] args) throws InvalidLicenseException {
	}

}
