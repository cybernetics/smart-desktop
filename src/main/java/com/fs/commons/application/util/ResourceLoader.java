package com.fs.commons.application.util;

import java.io.InputStream;
import java.net.URL;

public interface ResourceLoader {
	
	/**
	 * @param resourceName
	 * @return
	 */
	public InputStream getResourceAsStream(String resourceName);

	public URL getResourceUrl(String fileName);
}
