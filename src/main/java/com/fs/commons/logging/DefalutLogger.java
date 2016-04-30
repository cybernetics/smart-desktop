package com.fs.commons.logging;

import com.fs.commons.util.DateTimeUtil;

// ///////////////////////////////////////////////////////////////////////
class DefalutLogger implements LoggerManager {

		public void info(String msg) {
			System.out.println("-----------------------------------------------------------");
			System.out.print("INFO:");
			System.out.print(DateTimeUtil.getCurrentTime(""));
			System.out.print(" " + msg);
			System.out.println("\n-----------------------------------------------------------");

		}

		public void printCurrentTime(Object msg) {
			DateTimeUtil.printCurrentTime(msg);
		}

		public void fatal(String string) {
			System.err.println("-----------------------------------------------------------");
			System.err.print("FATAL:");
			System.err.print(DateTimeUtil.getCurrentTime(""));
			System.err.print(" " + string);
			System.err.println("\n-----------------------------------------------------------");

		}

	}