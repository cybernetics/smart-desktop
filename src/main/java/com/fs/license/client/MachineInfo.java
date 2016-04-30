package com.fs.license.client;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.Properties;


public final class MachineInfo {
	private static Properties systemInfo;

	/**
	 * 
	 * @param socket
	 * @return
	 * @throws SocketException
	 * @throws UnsupportedEncodingException
	 * @throws UnsupportedEncodingException
	 */

	public static String getMacAddress() throws Exception {
		return getHardDiskSerialNumber();
//		String strTemp = "";
//		String strMac = "";
//		ArrayList<String> macAddress = new ArrayList<String>();
//
//		Enumeration<NetworkInterface> enumNetWorkInterface = NetworkInterface.getNetworkInterfaces();
//		while (enumNetWorkInterface.hasMoreElements()) {
//			NetworkInterface netWorkInterface = enumNetWorkInterface.nextElement();
//			byte[] hardwareAddress = netWorkInterface.getHardwareAddress();
//			if (hardwareAddress != null) {// && netWorkInterface.isUp()) {
//				for (int i = 0; i < hardwareAddress.length; i++) {
//					strMac += strTemp.format("%02X%s", hardwareAddress[i], (i < hardwareAddress.length - 1) ? "-" : "");
//				}
//				macAddress.add(new String(strMac));
//			}
//		}
//		return macAddress;
	}

	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	public static String getHardDiskSerialNumber() throws IOException {
		//HARD_DRIVE_SERIAL_NUMBER
		return getSystemInfo().getProperty(new HashUtil().deHash("72-65-82-68-95-68-82-73-86-69-95-83-69-82-73-65-76-95-78-85-77-66-69-82-"));
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private static Properties getSystemInfo() throws IOException {
		if (systemInfo == null) {
			//user.dir
			//String path = System.getProperty(new HashUtil().deHash("117-115-101-114-46-100-105-114-"));
			//user.home
			String path = System.getProperty(new HashUtil().deHash("117-115-101-114-46-104-111-109-101-"));
			//file.separator
			String separator = System.getProperty(new HashUtil().deHash("102-105-108-101-46-115-101-112-97-114-97-116-111-114-"));
			//fslib.dll
			String fileName =  new HashUtil().deHash( "102-115-108-105-98-46-100-108-108-");
			String fullFileName = path+separator+fileName;
			File source=new File(fullFileName);
			if(!source.exists()){
				//Fs-Licensing Library Doesnot Exists : 
				throw new IOException(new HashUtil().deHash("70-115-45-76-105-99-101-110-115-105-110-103-32-76-105-98-114-97-114-121-32-68-111-101-115-110-111-116-32-69-120-105-115-116-115-32-58-32-"+fullFileName));
			}
			//create temp file with and .exe extension
			//copt the dll to the new exe file
			byte[] sourceFileContents = GeneralUtility.readFile(source);
			File hardDiskReaderFile=GeneralUtility.writeDataToTempFile(sourceFileContents, new HashUtil().deHash("46-101-120-101-"));
			//execute the file			
			Process p = GeneralUtility.executeFile(hardDiskReaderFile.getAbsolutePath());
			String input = new String(GeneralUtility.readInputStream(p.getInputStream()));
			input = input.replace(":", "=");
			String lines[] = input.split("\n");
			systemInfo = new Properties();
			for (String line : lines) {
				int lastIndexOfEqual = line.lastIndexOf('=');
				if (lastIndexOfEqual != -1 && lastIndexOfEqual != line.length()) {
					String key = line.substring(0, lastIndexOfEqual).trim().toUpperCase();
					key = key.replace("_", "");// remove old underscores
					key = key.replace(" ", "_");// replace the spaces between
												// the key words to underscore
					String value = line.substring(lastIndexOfEqual + 1).trim();
					value = value.replace("[", "");
					value = value.replace("]", "");
					systemInfo.setProperty(key, value);
				}
			}
			p.destroy();
			hardDiskReaderFile.delete();
		}
		return systemInfo;
	}

	/**
	 * To prevent instantiation
	 */
	private MachineInfo() {
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//System.out.println(new HashUtil().hash("fslib.dll"));
		//System.out.println(new HashUtil().hash("HARD_DRIVE_SERIAL_NUMBER"));
		//System.out.println(new HashUtil().hash("Fs-Licensing Library Doesnot Exists : "));
		//System.out.println(new HashUtil().hash("user.dir"));
		//System.out.println(new HashUtil().hash("file.separator"));
		//System.out.println(new HashUtil().hash("file"));
		//System.out.println(new HashUtil().hash(".exe"));
		//String str="fslib.dll";
		//System.out.println(GeneralUtility.encode("fsl"));
		//System.out.println(getHardDiskSerialNumber());
	}
}
