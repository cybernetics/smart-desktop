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
	public FSProperties(String filePath) throws IOException, ValidationException {
		if (GeneralUtility.isEmpty(filePath)) {
			throw new ValidationException("FILE_PATH_CAN_NOT_BE_EMPTY");
		}
		File file = new File(filePath);
		if (!file.exists()) {
			throw new ValidationException("FILE_WITH " + file.getAbsolutePath() + "_IS_NOT_EXISTS");
		}
		init(new FileInputStream(file));
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public FSProperties(File file) throws IOException, ValidationException {
		if (file == null) {
			throw new ValidationException("FILE_CAN_NOT_BE_EMPTY");
		}
		init(new FileInputStream(file));
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public FSProperties(InputStream inputStream) throws IOException, ValidationException {
		if (inputStream == null) {
			throw new ValidationException("INPUT_STREAM_CAN_NOT_BE_EMPTY");
		}

		init(inputStream);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	private void init(InputStream inputStream) throws IOException, UnsupportedEncodingException {
		load(new InputStreamReader(inputStream, "UTF-8"));
	}
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
}
