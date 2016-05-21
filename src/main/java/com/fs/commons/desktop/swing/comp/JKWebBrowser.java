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
package com.fs.commons.desktop.swing.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.GeneralUtility;
import com.fs.commons.util.WebUtil;

public class JKWebBrowser extends JKPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// public enum FileType {
	// HTML, PDF, FLASH;
	// @Override
	// public String toString() {
	// if (this == HTML) {
	// return "html";
	// }
	// if (this == PDF) {
	// return "pdf";
	// }
	// if (this == FLASH) {
	// return "swf";
	// }
	// return super.toString();
	// }
	// }

	// private InputStream in;
//	private JWebBrowser browser;
	private URL url;

	private Map<String, String> postParamters;

	private Map<String, String> headers;

	public JKWebBrowser() {
		init();
	}

	/**
	 * 
	 * @param in
	 * @throws IOException
	 */
	public JKWebBrowser(InputStream in) throws IOException {
		init();
		byte[] data = GeneralUtility.readStream(in);
		File file = GeneralUtility.writeDataToTempFile(data, ".html");
		setUrl(file.toURL());
	}

	/**
	 * 
	 * @param in
	 * @throws IOException
	 */
	public JKWebBrowser(URL url) throws IOException {
		init();
		setUrl(url);
	}

	public JKWebBrowser(String url) throws MalformedURLException {
		this();
		setUrl(new URL(url));
	}

	public void setUrl(final URL url) {
		this.url = url;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
//				browser.navigate(url.toString(), getParamters());
			}
		});
	}

	protected Object getParamters() {
//		WebBrowserNavigationParameters p = new WebBrowserNavigationParameters();
//		p.setPostData(postParamters);
//		p.setHeaders(headers);
//		return p;
		return null;
	}

	private void init() {
		setPreferredSize(new Dimension(800, 600));
		setLayout(new BorderLayout());
		add(getBrowserPanel(), BorderLayout.CENTER);
		initBrowser();
	}

	private void initBrowser() {
//		browser.setMenuBarVisible(true);
	}

	/**
	 * 
	 * @return
	 */
	private JPanel getBrowserPanel() {
//		browser = new JWebBrowser();
//		browser.setMenuBarVisible(false);
//		browser.setLocationBarVisible(false);
//
//		return browser;
		return new JPanel();
	}

	/* Standard main method to try that test as a standalone application. */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		SwingUtility.testPanel(new JKWebBrowser(new FileInputStream("c://a.htm")));
	}

	public void setUrl(String url) throws MalformedURLException {
		if (!url.toLowerCase().startsWith("http")) {
			url = WebUtil.getFSWebServerUrl(url);
		}
		setUrl(new URL(url));
	}

	public void setPostParamters(Map<String, String> postParamters) {
		this.postParamters = postParamters;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public static void clearSession(){
		//JWebBrowser.clearSessionCookies();
	}
}