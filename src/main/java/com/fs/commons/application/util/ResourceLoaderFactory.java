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
package com.fs.commons.application.util;

import java.io.InputStream;
import java.net.URL;

class DefaultResourceLoader implements ResourceLoader {

	@Override
	public InputStream getResourceAsStream(final String resourceName) {
		return DefaultResourceLoader.class.getResourceAsStream(resourceName);
	}

	@Override
	public URL getResourceUrl(final String fileName) {
		return DefaultResourceLoader.class.getResource(fileName);
	}
}

public class ResourceLoaderFactory {
	private static ResourceLoader resourceLoader;

	public static ResourceLoader getResourceLoaderImp() {
		if (resourceLoader == null) {
			return new DefaultResourceLoader();
		}
		return resourceLoader;
	}

	public static void setResourceLoaderImpl(final ResourceLoader servletResourceLoader) {
		resourceLoader = servletResourceLoader;
	}
}
