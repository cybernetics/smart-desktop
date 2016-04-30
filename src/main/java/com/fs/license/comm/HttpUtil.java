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
	 * @param string
	 * @return
	 * @throws IOException
	 */
	public static File downloadFile(final String fileName) throws IOException {
		final String url = getServiceUrl().toExternalForm() + "?download?" + fileName;
		final File file = new File(System.getProperty("user.home") + System.getProperty("file.separator") + fileName);
		downloadFile(url, file.getAbsolutePath());
		return file;
	}

	/**
	 * @throws IOException
	 *
	 */
	public static void downloadFile(final String fileUrl, final String localFile) throws IOException {
		final URL url = new URL(fileUrl);
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		final String data = readStream(connection.getInputStream());
		writeBytesToFile(localFile, data.getBytes());
	}

	/**
	 * 
	 * @param fileUrl
	 * @param localFile
	 * @return
	 * @throws IOException
	 */
	public static File downloadFileToTemp(final String fileUrl, final String ext) throws IOException {
		final File file = File.createTempFile("fs", ext);
		downloadFile(fileUrl, file.getAbsolutePath());
		return file;
	}

	/**
	 *
	 * @return
	 * @throws MalformedURLException
	 */
	public static URL getServiceUrl() throws MalformedURLException {
		// localhost
		String serviceHost = new HashUtil().deHash("108-111-99-97-108-104-111-115-116-");
		int servicePort = 8181;
		// license_manager
		String serviceName = new HashUtil().deHash("108-105-99-101-110-115-101-95-109-97-110-97-103-101-114-");
		// service-host
		if (System.getProperty(new HashUtil().deHash("115-101-114-118-105-99-101-45-104-111-115-116-")) != null) {
			serviceHost = System.getProperty(new HashUtil().deHash("115-101-114-118-105-99-101-45-104-111-115-116-"));
		}
		// service-port
		if (System.getProperty(new HashUtil().deHash("115-101-114-118-105-99-101-45-112-111-114-116-")) != null) {
			servicePort = Integer.parseInt(System.getProperty(new HashUtil().deHash("115-101-114-118-105-99-101-45-112-111-114-116-")).trim());
		}
		// service-name
		if (System.getProperty(new HashUtil().deHash("115-101-114-118-105-99-101-45-110-97-109-101-")) != null) {
			serviceName = System.getProperty(new HashUtil().deHash("115-101-114-118-105-99-101-45-110-97-109-101-"));
		}
		// http://
		return new URL(new HashUtil().deHash("104-116-116-112-58-47-47-") + serviceHost + ":" + servicePort + "/" + serviceName);
	}

	/**
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String readStream(final InputStream inputStream) throws IOException {
		try {
			int ch;
			String message = "";
			while ((ch = inputStream.read()) != -1) {
				message += (char) ch;
			}
			return message;
		} finally {
			inputStream.close();
		}
	}

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
	public static String sendHttpRequest(final String url, final String method, final Properties header, final String body)
			throws IOException, HttpException {
		final URL siteUrl = new URL(url);
		final HttpURLConnection connection = (HttpURLConnection) siteUrl.openConnection();
		connection.setRequestMethod(method);
		final Enumeration<?> keys = header.keys();
		while (keys.hasMoreElements()) {
			final String key = (String) keys.nextElement();
			connection.addRequestProperty(key, header.getProperty(key));
		}
		connection.setDoOutput(true);

		final OutputStream out = connection.getOutputStream();
		out.write(body.getBytes());

		connection.connect();

		final int errorCode = connection.getResponseCode();
		if (errorCode != HttpURLConnection.HTTP_OK) {
			throw new HttpException(connection.getResponseMessage(), errorCode);
		}
		final String response = readStream(connection.getInputStream());
		return response;
	}

	/**
	 *
	 * @param fileName
	 * @param data
	 * @throws FileNotFoundException
	 */
	public static void writeBytesToFile(final String fileName, final byte[] data) throws IOException {
		final FileOutputStream out = new FileOutputStream(fileName);
		out.write(data);
		out.close();
	}

}
