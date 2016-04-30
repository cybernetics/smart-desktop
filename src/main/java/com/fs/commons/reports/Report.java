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
package com.fs.commons.reports;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import com.fs.commons.util.GeneralUtility;

/**
 * @author u087
 *
 */
public class Report {
	String name;

	String sourceFileName;

	String title;

	ArrayList<Paramter> paramters = new ArrayList<Paramter>();

	// String absolutSourcePath;

	// String absolutOutPath;

	private boolean visible = true;

	// private static String outPath=GeneralUtility.getReportsOutPath();

	// private String sourcePath;

	private String outFileName;

	boolean compiled;

	/**
	 * @param o
	 * @return
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean addParamter(final Paramter o) {
		return this.paramters.add(o);
	}

	public String getAbsolutOutPath1() {
		return GeneralUtility.getReportsOutPath() + System.getProperty("file.separator") + getOutFileName();
	}

	public InputStream getInputStream() throws FileNotFoundException {
		return GeneralUtility.getReportFileAsStream(getOutFileName());
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	public String getOutFileName() {
		return this.outFileName;
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.ArrayList#get(int)
	 */
	public Paramter getParamter(final int index) {
		return this.paramters.get(index);
	}

	/**
	 * @return the paramters
	 */
	public ArrayList<Paramter> getParamters() {
		return this.paramters;
	}

	/**
	 * @return
	 * @see java.util.ArrayList#size()
	 */
	public int getParamtersCount() {
		return this.paramters.size();
	}

	public String getSourceFileName() {
		return this.sourceFileName;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param elem
	 * @return
	 * @see java.util.ArrayList#indexOf(java.lang.Object)
	 */
	public int indexOfParamter(final Object elem) {
		return this.paramters.indexOf(elem);
	}

	// /**
	// * @return the absolutSourcePath
	// */
	// public String getAbsolutSourcePath() {
	// return this.absolutSourcePath;
	// }
	//
	// /**
	// * @param absolutSourcePath
	// * the absolutSourcePath to set
	// */
	// public void setAbsolutSourcePath(String absolutSourcePath) {
	// this.absolutSourcePath = absolutSourcePath;
	// }
	//
	// /**
	// * @return the absolutOutPath
	// */
	// public String getAbsolutOutPath() {
	// return this.absolutOutPath;
	// }
	//
	// /**
	// * @param absolutOutPath
	// * the absolutOutPath to set
	// */
	// public void setAbsolutOutPath(String absolutOutPath) {
	// this.absolutOutPath = absolutOutPath;
	// }

	public boolean isCompiled() {
		return this.compiled;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return this.visible;
	}

	// public void setOutPath(String outPath) {
	// this.outPath = GeneralUtility.getUserFolderPath();
	// }

	// public void setSourcePath(String sourcePath) {
	// this.sourcePath = sourcePath;
	// }

	// /**
	// * @return the outPath
	// */
	// public String getOutPath() {
	// return outPath;
	// }

	/**
	 * @return the sourcePath
	 */
	// public String getSourcePath() {
	// return sourcePath;
	// }

	public void setCompiled(final boolean compiled) {
		this.compiled = compiled;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * the generated and compiled file full name
	 *
	 * @param string
	 */
	public void setOutFileName(final String outFileName) {
		this.outFileName = outFileName;
	}

	/**
	 * @param paramters
	 *            the paramters to set
	 */
	public void setParamters(final ArrayList<Paramter> paramters) {
		this.paramters = paramters;
	}

	public void setSourceFileName(final String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @param parseBoolean
	 */
	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("{Report name:" + getName());
		buffer.append(", Title : " + getTitle());
		buffer.append(", Paramters " + this.paramters.toString());
		buffer.append("}");
		return buffer.toString();
	}

}
