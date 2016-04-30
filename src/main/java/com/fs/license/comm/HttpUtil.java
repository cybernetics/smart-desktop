package com.fs.license.comm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import com.fs.license.client.HashUtil;

public class HttpUtil {
	
	/**
	 * 
	 * @param url
	 * @param method
	 * @param header
	 * @param body
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static String sendHttpRequest(String url,String method,Properties header,String body) throws IOException, HttpException{
		URL siteUrl=new URL(url);
		HttpURLConnection connection=(HttpURLConnection)siteUrl.openConnection();
		connection.setRequestMethod(method);
		Enumeration<?> keys = header.keys();
		while(keys.hasMoreElements()){
			String key = (String) keys.nextElement();
			connection.addRequestProperty(key, header.getProperty(key));
		}
		connection.setDoOutput(true);

		OutputStream out=connection.getOutputStream();
		out.write(body.getBytes());
		
		connection.connect();				
		
		int errorCode=connection.getResponseCode();
		if(errorCode!=HttpURLConnection.HTTP_OK){
			throw new HttpException(connection.getResponseMessage(),errorCode);
		}
		String response=readStream(connection.getInputStream());
		return response;
	}
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException 
	 */
	public static String readStream(InputStream inputStream) throws IOException {
		try{
		int ch;
		String message="";
		while((ch=inputStream.read())!=-1){
			message+=(char)ch;
		}
		return message;
		}finally{
			inputStream.close();
		}
	}
	
	/**
	 * 	
	 * @param fileUrl
	 * @param localFile
	 * @return 
	 * @throws IOException
	 */
	public static File downloadFileToTemp(String fileUrl,String ext) throws IOException{
		File file=File.createTempFile("fs", ext);
		downloadFile(fileUrl, file.getAbsolutePath());
		return file; 
	}

	/**
	 * @throws IOException 
	 * 
	 */
	public static void downloadFile(String fileUrl,String localFile) throws IOException{
		URL url=new URL(fileUrl);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		String data=readStream(connection.getInputStream());
		writeBytesToFile(localFile, data.getBytes());
	}
	
	/**
	 * 
	 * @param fileName
	 * @param data
	 * @throws FileNotFoundException 
	 */
	public static void writeBytesToFile(String fileName,byte[] data) throws IOException{
		FileOutputStream out=new FileOutputStream(fileName);
		out.write(data);
		out.close();
	}

	/**
	 * 
	 * @return
	 * @throws MalformedURLException 
	 */
	public static URL getServiceUrl() throws MalformedURLException {
		//localhost
		String serviceHost=new HashUtil().deHash("108-111-99-97-108-104-111-115-116-");
		int servicePort=8181;		
		//license_manager
		String serviceName=new HashUtil().deHash("108-105-99-101-110-115-101-95-109-97-110-97-103-101-114-");
		//service-host
		if(System.getProperty(new HashUtil().deHash("115-101-114-118-105-99-101-45-104-111-115-116-"))!=null){
			serviceHost=System.getProperty(new HashUtil().deHash("115-101-114-118-105-99-101-45-104-111-115-116-"));
		}
		//service-port
		if(System.getProperty(new HashUtil().deHash("115-101-114-118-105-99-101-45-112-111-114-116-"))!=null){
			servicePort=Integer.parseInt(System.getProperty(new HashUtil().deHash( "115-101-114-118-105-99-101-45-112-111-114-116-")).trim());
		}
		//service-name
		if(System.getProperty(new HashUtil().deHash("115-101-114-118-105-99-101-45-110-97-109-101-"))!=null){
			serviceName=System.getProperty(new HashUtil().deHash("115-101-114-118-105-99-101-45-110-97-109-101-"));
		}
		//http://
		return new URL(new HashUtil().deHash("104-116-116-112-58-47-47-") +serviceHost+":"+servicePort+"/"+serviceName);
	}

	/**
	 * 
	 * @param string
	 * @return
	 * @throws IOException 
	 */
	public static File downloadFile(String fileName) throws IOException {
		String url=getServiceUrl().toExternalForm()+"?download?"+fileName;
		File file= new File(System.getProperty("user.home")+System.getProperty("file.separator")+ fileName);
		downloadFile(url, file.getAbsolutePath());
		return file;
	}
	
}
