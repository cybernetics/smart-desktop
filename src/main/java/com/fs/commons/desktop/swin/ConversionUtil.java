package com.fs.commons.desktop.swin;

import java.text.ParseException;
import java.util.Date;

import com.fs.commons.util.DateTimeUtil;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.FormatUtil;

public class ConversionUtil {

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static double toDouble(Object value) {
		if (value == null) {
			return 0;
		}
		return new Double(value.toString());
	}

	public static Integer toInteger(Object value) {
		return (int) toDouble(value);
	}

	public static float toFloat(Object value) {
		if (value == null) {
			return 0;
		}
		return new Float(value.toString());
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean toBoolean(Object value) {
		boolean result = false;
		if (value != null) {
			if (value.toString().trim().equals("1") || value.toString().trim().toLowerCase().equals("true")) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		return result;

	}

	public static Date toDate(Object value) {
		if (value instanceof Date) {
			return (Date) value;
		}
		if (value == null || value.equals("null")) {
			return null;
		}
		if (value instanceof String) {
			try {
				return DateTimeUtil.parseDate(value.toString(), FormatUtil.PATTERN_DEFAULT);
			} catch (ParseException e) {
				ExceptionUtil.handleException(e);
			}
		}

		return null;
	}

	public static String toString(Object value) {
		return value == null ? null : value.toString();
	}

	public static java.sql.Time toTime(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof java.sql.Time) {
			return (java.sql.Time) value;
		}
		if (value instanceof java.util.Date) {
			Date date = (java.util.Date) value;
			return new java.sql.Time(date.getTime());
		}
		return null;
	}
	//
	// // time the day/month/year info from date value to solve the time before
	// and
	// // after issues
	// private static long trimDateInfo(Date date) {
	// return date.getTime() % 1000 * 60 * 60 * 24;
	// }
	//
}
