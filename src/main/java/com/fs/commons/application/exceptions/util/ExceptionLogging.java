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
public class ExceptionLogging extends PrintStream {
	static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");

	/**
	 *
	 * @param args
	 * @throws FileNotFoundException
	 * @throws FileNotFoundException
	 */
	public static void main(final String[] args) throws FileNotFoundException {
		System.setErr(new ExceptionLogging("log.txt"));
		throw new RuntimeException("Test exception");
	}

	/**
	 * @param out
	 * @throws FileNotFoundException
	 */
	public ExceptionLogging(final String fileName) throws FileNotFoundException {
		super(new FileOutputStream(fileName, true), true);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#print(boolean)
	 */
	// @Override
	@Override
	public void print(final String b) {
		System.out.println(b);
		super.print(b);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.PrintStream#println(java.lang.Object)
	 */
	@Override
	public void println(final Object x) {
		System.out.println(x);
		super.println("\n\r" + format.format(new Date()) + "\t" + x);
	}
}
