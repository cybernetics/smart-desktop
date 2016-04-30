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
package com.fs.commons.mail;

import java.io.IOException;
import java.io.InputStream;

import com.fs.commons.util.GeneralUtility;

public class Attachment {
	public enum MimeType {
	}

	String name;
	byte[] data;
	String description;
	String mimeType;

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return this.data;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return this.mimeType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(final byte[] data) {
		this.data = data;
	}

	/**
	 *
	 * @param in
	 * @throws IOException
	 */
	public void setData(final InputStream in) throws IOException {
		final byte[] bytes = GeneralUtility.readInputStream(in);
		setData(bytes);
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @param mimeType
	 *            the mimeType to set
	 */
	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}
}
