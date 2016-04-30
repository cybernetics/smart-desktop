/**
 * 
 */
package com.fs.commons.application.exceptions.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author u087
 * 
 */
public class ExceptionLogging extends PrintStream  {
	static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");

	/**
	 * @param out
	 * @throws FileNotFoundException
	 */
	public ExceptionLogging(String fileName) throws FileNotFoundException {
		super(new FileOutputStream(fileName, true),true);
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#println(java.lang.Object)
	 */
	@Override
	public void println(Object x) {
		System.out.println(x);
		super.println("\n\r" + format.format(new Date())+"\t" + x);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#print(boolean)
	 */
//	@Override
	public void print(String b) {
		System.out.println(b);
		super.print(b);
	}

	/**
	 * 
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		System.setErr(new ExceptionLogging("log.txt"));
		throw new RuntimeException("Test exception");
	}
}
