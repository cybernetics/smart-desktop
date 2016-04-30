package com.fs.commons.application.util;

import java.io.InputStream;
import java.net.URL;

public class ResourceLoaderFactory {
	private static ResourceLoader resourceLoader;

	public static ResourceLoader getResourceLoaderImp() {
		if (resourceLoader == null) {
			return new DefaultResourceLoader();
		}
		return resourceLoader;
	}

	public static void setResourceLoaderImpl(ResourceLoader servletResourceLoader) {
		resourceLoader = servletResourceLoader;
	}
}

class DefaultResourceLoader implements ResourceLoader {

	@Override
	public InputStream getResourceAsStream(String resourceName) {
		return DefaultResourceLoader.class.getResourceAsStream(resourceName);
	}

	@Override
	public URL getResourceUrl(String fileName) {
		return DefaultResourceLoader.class.getResource(fileName);
	}
}
