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
package com.fs.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.fs.commons.application.exceptions.ValidationException;

public class FSProperties extends Properties {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public FSProperties() {

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public FSProperties(final File file) throws IOException, ValidationException {
		if (file == null) {
			throw new ValidationException("FILE_CAN_NOT_BE_EMPTY");
		}
		init(new FileInputStream(file));
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public FSProperties(final InputStream inputStream) throws IOException, ValidationException {
		if (inputStream == null) {
			throw new ValidationException("INPUT_STREAM_CAN_NOT_BE_EMPTY");
		}

		init(inputStream);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public FSProperties(final String filePath) throws IOException, ValidationException {
		if (GeneralUtility.isEmpty(filePath)) {
			throw new ValidationException("FILE_PATH_CAN_NOT_BE_EMPTY");
		}
		final File file = new File(filePath);
		if (!file.exists()) {
			throw new ValidationException("FILE_WITH " + file.getAbsolutePath() + "_IS_NOT_EXISTS");
		}
		init(new FileInputStream(file));
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	private void init(final InputStream inputStream) throws IOException, UnsupportedEncodingException {
		load(new InputStreamReader(inputStream, "UTF-8"));
	}
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
}
