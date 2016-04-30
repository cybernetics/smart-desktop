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
package com.fs.commons.logging;

import java.io.File;
import java.io.IOException;

import com.fs.commons.util.DateTimeUtil;
import com.fs.commons.util.GeneralUtility;

/**
 *
 * @author mkiswani
 *
 */
public class FilesLogger implements LoggerManager {
	private final File file = new File("log_" + new java.sql.Date(System.currentTimeMillis()) + ".txt");

	public FilesLogger() throws IOException {
		if (!this.file.exists()) {
			this.file.createNewFile();
		}
	}

	@Override
	public void fatal(String msg) {
		try {
			msg = "FATAL:" + msg + "\n";
			GeneralUtility.writeDataToFile(msg.getBytes(), this.file, true);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void info(String msg) {
		try {
			msg = "INFO:" + msg + "\n";
			GeneralUtility.writeDataToFile(msg.getBytes(), this.file, true);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void printCurrentTime(final Object msg) {
		try {
			String currentTime = DateTimeUtil.getCurrentTime(msg + "");
			currentTime = "INFO:" + currentTime + "\n";
			GeneralUtility.writeDataToFile(currentTime.getBytes(), this.file, true);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
