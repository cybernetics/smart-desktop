/**
 * 
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
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the paramters
	 */
	public ArrayList<Paramter> getParamters() {
		return this.paramters;
	}

	/**
	 * @param paramters
	 *            the paramters to set
	 */
	public void setParamters(ArrayList<Paramter> paramters) {
		this.paramters = paramters;
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean addParamter(Paramter o) {
		return this.paramters.add(o);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.ArrayList#get(int)
	 */
	public Paramter getParamter(int index) {
		return this.paramters.get(index);
	}

	/**
	 * @param elem
	 * @return
	 * @see java.util.ArrayList#indexOf(java.lang.Object)
	 */
	public int indexOfParamter(Object elem) {
		return this.paramters.indexOf(elem);
	}

	/**
	 * @return
	 * @see java.util.ArrayList#size()
	 */
	public int getParamtersCount() {
		return this.paramters.size();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{Report name:" + getName());
		buffer.append(", Title : " + getTitle());
		buffer.append(", Paramters " + paramters.toString());
		buffer.append("}");
		return buffer.toString();
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

	/**
	 * @param parseBoolean
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
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

	/**
	 * the generated and compiled file full name
	 * 
	 * @param string
	 */
	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

	public String getOutFileName() {
		return outFileName;
	}

	public String getAbsolutOutPath1() {
		return GeneralUtility.getReportsOutPath() + System.getProperty("file.separator") + getOutFileName();
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	public boolean isCompiled() {
		return compiled;
	}

	public void setCompiled(boolean compiled) {
		this.compiled = compiled;
	}

	public InputStream getInputStream() throws FileNotFoundException {
		return GeneralUtility.getReportFileAsStream(getOutFileName());
	}

}
