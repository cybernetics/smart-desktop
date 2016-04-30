package com.fs.commons.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.reports.ReportException;
import com.fs.commons.util.GeneralUtility;

public abstract class AbstractPdfReport {

	/**
	 * @throws IOException
	 * @throws DaoException
	 * @throws ReportException
	 * 
	 */
	public final File buildReport() throws ReportException, DaoException, IOException {
		File file = GeneralUtility.createTempFile("pdf");
		buildReport(file.getAbsolutePath());
		file=new File(file.getAbsolutePath());//to reload new information
		return file;
	}

	public final void buildReport(String fileName) throws IOException, ReportException, DaoException {
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

	public abstract void buildReport(OutputStream out) throws ReportException, DaoException;
}
