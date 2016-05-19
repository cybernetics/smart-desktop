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
package com.fs.commons.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.jk.exceptions.handler.ExceptionUtil;

public class FormatUtil {

	public static final String PATTERN_SHORT_DATE = "dd-MM-yyyy";
	public static final String PATTERN_DEFAULT = "dd/MM/yyyy";
	public static final String PATTERN_MYSQL = "yyyy-MM-dd";

	public static final String FULL_DB_PATTERN = "ddMMyyyy_kkmmss";
	private static DecimalFormat numberFormat = new DecimalFormat("###,###,##0.##");
	public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String capitalizeFirstLetters(String s) {
		for (int i = 0; i < s.length(); i++) {

			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}

			// Is this character a non-letter or non-digit? If so
			// then this is probably a word boundary so let's capitalize
			// the next character in the sequence.
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)), s.substring(i + 2));
				}
			}

		}
		return s;

	}

	public static String convertFromShortToMysqlDate(final String date) {
		Date date2;
		try {
			date2 = DateTimeUtil.parseShortDate(date);
			return FormatUtil.formatMysqlDate(date2);
		} catch (final ParseException e) {
			ExceptionUtil.handle(e);
			return null;
		}
	}

	public static boolean equals(final Date date1, final Date date2) {
		if (date1 == null && date2 != null || date1 != null && date2 == null) {
			return false;
		}
		return formatDate(date1, DEFAULT_DATE_PATTERN).equals(formatDate(date2, DEFAULT_DATE_PATTERN));
	}

	/**
	 *
	 * @param mark
	 * @return
	 */
	public static String formatAmount(final double mark) {
		final DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		format.setMaximumFractionDigits(2);
		// format.setDecimalSeparatorAlwaysShown(true);
		return format.format(mark);
	}

	/**
	 *
	 * @param date
	 *            Date
	 * @param pattren
	 *            String
	 * @return String
	 */
	public static String formatDate(final java.util.Date date) {
		return formatDate(date, PATTERN_DEFAULT);
	}

	public static String formatDate(final java.util.Date date, final String pattren) {
		final SimpleDateFormat formatter = new SimpleDateFormat(pattren, new Locale("en", "US"));
		if (date == null) {
			return "";
		}
		return formatter.format(date);
	} // formatDate

	public static String formatDouble(final Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 * @param mark
	 * @return
	 */
	public static String formatMark(final double mark) {
		final DecimalFormat format = new DecimalFormat("#0.00");
		return format.format(mark);
	}

	public static String formatMoney(final double amount) {
		final DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		format.setMaximumFractionDigits(3);
		// format.setDecimalSeparatorAlwaysShown(true);
		return format.format(amount);
	}

	public static String formatMysqlDate(final Date date) {
		return formatDate(date, PATTERN_MYSQL);
	}

	public static String formatNumber(final Number number) {
		return numberFormat.format(number);
	}

	/**
	 *
	 * @return
	 */
	public static String formatShortDate() {
		return formatShortDate(new Date());
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static String formatShortDate(final Date date) {
		return formatDate(date, PATTERN_SHORT_DATE);
	}

	/**
	 *
	 * @param timeFrom
	 * @return
	 */
	public static String formatTime(final Time time) {
		return formatDate(time, "hh:mm");
	}

	public static long getDayDifference(final Date startDate, final Date endDate) {
		final long startTime = startDate.getTime();
		final long endTime = endDate.getTime();
		final long diffTime = endTime - startTime;
		final long diffDays = diffTime / (1000 * 60 * 60 * 24);
		return diffDays;
	}

	public static int getDayOfWeek(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static long getDifferent(final Date timeFrom, final Date timeTo) throws ParseException {
		final DateFormat format = new SimpleDateFormat("HH:mm:ss");
		final Date date = format.parse(timeFrom.toString());
		final Date date2 = format.parse(timeTo.toString());
		final long difference = (date2.getTime() - date.getTime()) / 1000 / 60;
		return difference;
	}

	public static int getHour(final Date timeFrom) throws ParseException {
		final Calendar instance = Calendar.getInstance();
		instance.setTime(timeFrom);
		final int hour = instance.get(Calendar.HOUR);
		return hour;

	}

	/**
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isEqualedDates(final Date date1, final Date date2) {
		return formatShortDate(date1).equals(formatShortDate(date2));
	}

	public static void main(final String[] args) {
		// System.out.println(formatTime(new Time(System.currentTimeMillis())));
		System.out.println(formatAmount(516.238f));
	}
}
