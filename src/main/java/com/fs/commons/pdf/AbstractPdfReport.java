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
package com.fs.commons.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.reports.ReportException;
import com.fs.commons.util.GeneralUtility;

public abstract class AbstractPdfReport {

	/**
	 * @throws IOException
	 * @throws JKDataAccessException
	 * @throws ReportException
	 *
	 */
	public final File buildReport() throws ReportException, JKDataAccessException, IOException {
		File file = GeneralUtility.createTempFile("pdf");
		buildReport(file.getAbsolutePath());
		file = new File(file.getAbsolutePath());// to reload new information
		return file;
	}

	public abstract void buildReport(OutputStream out) throws ReportException, JKDataAccessException;

	public final void buildReport(final String fileName) throws IOException, ReportException, JKDataAccessException {
		OutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			buildReport(out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
