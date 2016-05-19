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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.JKDataAccessException;
import com.jk.exceptions.handler.ExceptionUtil;

public class DateTimeUtil {
	public enum CompareDates {
		DATE1_LESS_THAN_DATE2, DATE1_GREATER_THAN_DATE2, DATE1_EQUAL_DATE2;
	}

	public static Date adddDaysToCurrentDate(final int numberOfDays) {
		final Date date = new Date();
		final Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(Calendar.DATE, numberOfDays);
		return instance.getTime();
	}

	public static Date adddMonths(final int numOfMonths) {
		return addMonths(new Date(), numOfMonths);
	}

	public static Date addMonths(final Date date, final int numOfMonths) {
		final Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(Calendar.MONTH, numOfMonths);
		return instance.getTime();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// Mohamed Kiswani
	//
	// /////////////////////////////////////////////////////////////////////////////////////
	public static CompareDates compareTwoDates(final Date date1, final Date date2) {
		final Date d1 = new Date(date1.getTime());// to unify the format of the
													// dates
		// before the compare
		final Date d2 = new Date(date2.getTime());
		if (d1.compareTo(d2) < 0) {
			return CompareDates.DATE1_LESS_THAN_DATE2;
		} else if (d1.compareTo(d2) > 0) {
			return CompareDates.DATE1_GREATER_THAN_DATE2;
		} else {
			return CompareDates.DATE1_EQUAL_DATE2;
		}
	}

	public static String formatCurrentDatabaseDate() {
		try {
			return FormatUtil.formatDate(DaoUtil.getSystemDate(), "yyyy-MM-dd");
		} catch (final JKDataAccessException e) {
			ExceptionUtil.handle(e);
			// unreachable
			return null;
		}
	}

	public static String formatCurrentDate() {
		return FormatUtil.formatDate(new Date(), "yyyy-MM-dd");
	}

	public static String formatFullTime(final Date date) {
		return FormatUtil.formatDate(date, "hh:mm:ss SSS");
	}

	public static String formatTime(final Date date) {
		return FormatUtil.formatDate(date, "hh:mm");
	}

	public static String getCurrentDate() {
		return getFormatedDate(System.currentTimeMillis());
	}

	/**
	 *
	 * @return
	 */
	public static int getCurrentDayInMonth() {
		final Date date = new Date();
		return getDayOfMonth(date);
	}

	public static int getCurrentMonth() {
		return getMonthFromDate(new Date());
	}

	public static String getCurrentTime(final String label) {
		final Date date = new Date();
		return formatFullTime(date) + "  :" + label;
	}

	public static Calendar getCurrentTime(final Time time) {
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
		calendar.set(Calendar.HOUR_OF_DAY, time.getMinutes());
		return calendar;
	}

	/**
	 *
	 * @return int
	 */
	public static int getCurrentYear() {
		final Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public static FSTimeObject getCurrntTime() {
		final FSTimeObject fsTimeObject = new FSTimeObject();
		final Calendar instance = Calendar.getInstance();
		fsTimeObject.setYear(instance.get(Calendar.YEAR));
		fsTimeObject.setMonth(instance.get(Calendar.MONTH));
		fsTimeObject.setDay(instance.get(Calendar.DAY_OF_MONTH));
		fsTimeObject.setHour(instance.get(Calendar.HOUR_OF_DAY));
		fsTimeObject.setMunite(instance.get(Calendar.MINUTE));
		return fsTimeObject;
	}

	private static int getDayOfMonth(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfWeek(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 *
	 * @param timeFrom
	 * @param timeTo
	 * @return
	 * @throws DateParseException
	 */
	public static long getDifferent(final Time timeFrom, final Time timeTo) throws ParseException {
		final DateFormat format = new SimpleDateFormat("HH:mm:ss");
		// the a means am/pm marker
		final Date date = format.parse(timeFrom.toString());
		final Date date2 = format.parse(timeTo.toString());
		final long difference = (date2.getTime() - date.getTime()) / 1000 / 60;
		return difference;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// Mohamed Kiswani
	//
	// /////////////////////////////////////////////////////////////////////////////////////
	public static String getFormatedDate(final long lastModified) {
		return FormatUtil.formatDate(new Date(lastModified), FormatUtil.PATTERN_MYSQL);
	}

	/**
	 *
	 * @return int / //@v 1.1 start Update Bashar Nadir
	 * @param date
	 *            Date
	 */
	public static int getMonthFromDate(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getNumOfMonths(final Date date1, final Date date2) {
		final Calendar firstDate = Calendar.getInstance();
		final Date date = new Date(date1.getTime());
		firstDate.setTime(date);
		final Calendar secondDate = Calendar.getInstance();
		final Date date3 = new Date(date2.getTime());
		secondDate.setTime(date3);
		final int months = firstDate.get(Calendar.MONTH) - secondDate.get(Calendar.MONTH);

		return months;
	}

	/**
	 *
	 * @return int
	 * @param date
	 *            Date
	 */
	public static int getYearFromData(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static boolean isCurrentTimeBetweenTowTimes(final Date fromDate, final Date fromTime, final Date toDate, final Date timeTo) {
		final FSTimeObject currntTime = getCurrntTime();
		final FSTimeObject fromTimeObject = new FSTimeObject();
		final FSTimeObject toTimeObject = new FSTimeObject();
		if (currntTime.after(fromTimeObject.toTimeObject(fromDate, fromTime)) && currntTime.before(toTimeObject.toTimeObject(toDate, timeTo))) {
			return true;
		}
		return false;
	}

	public static boolean isDate(final String strDate, final String pattern) {
		try {
			parseDate(strDate, pattern);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	public static boolean isDateEqaualed(final java.util.Date date1, final java.util.Date date2) {
		final String d1 = FormatUtil.formatDate(date1, FormatUtil.PATTERN_MYSQL);
		final String d2 = FormatUtil.formatDate(date2, FormatUtil.PATTERN_MYSQL);
		return d1.equalsIgnoreCase(d2);
	}

	/**
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws JKDataAccessException
	 */
	public static boolean isPeriodActive(final Date startDate, final Date endDate) throws JKDataAccessException {
		if (startDate == null && endDate == null) {
			return true;
		}
		if (startDate == null) {
			throw new JKDataAccessException("START_DATE_CAN_NOT_BE_NULL");
		}
		if (endDate == null) {
			throw new JKDataAccessException("END_DATE_CAN_NOT_BE_NULL");
		}
		if (compareTwoDates(startDate, endDate).equals(CompareDates.DATE1_GREATER_THAN_DATE2)) {
			throw new JKDataAccessException("START_DATE_MUST_BE_BEFORE_END_DATE");
		}
		final boolean startLessThanCurrent = compareTwoDates(startDate, DaoUtil.getSystemDate()).equals(CompareDates.DATE1_LESS_THAN_DATE2);
		final boolean endGreaterThanCurrent = compareTwoDates(endDate, DaoUtil.getSystemDate()).equals(CompareDates.DATE1_GREATER_THAN_DATE2);
		return startLessThanCurrent && endGreaterThanCurrent;
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public static boolean isTimeConflict(final Time timeFrom, final Time timeTo, final Time otherTimeFrom, final Time otherTimeTo) {
		if (DateTimeUtil.isTimesEqaualed(timeFrom, otherTimeFrom) || DateTimeUtil.isTimesEqaualed(timeTo, otherTimeTo)) {
			return true;
		}

		// other time start time is between start and end time for this time
		if (timeFrom.after(otherTimeFrom) && timeFrom.before(otherTimeTo)) {
			return true;
		}

		// other time e is between start and end time for this time
		if (timeTo.after(otherTimeFrom) && timeTo.before(otherTimeTo)) {
			return true;
		}

		// if time starting before this time and ends after this time
		if (timeFrom.before(otherTimeFrom) && timeTo.after(otherTimeTo)) {
			return true;
		}

		return false;
	}

	public static boolean isTimesEqaualed(final Date time1, final Date time2) {
		return formatTime(time1).equals(formatTime(time2));
	}

	/**
	 *
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isTimesEqaualed(final Time time1, final Time time2) {
		final String d1 = FormatUtil.formatTime(time1);
		final String d2 = FormatUtil.formatTime(time2);
		return d1.equalsIgnoreCase(d2);
	}

	public static void main(final String[] args) throws ParseException {
		final Date parseDate = DateTimeUtil.parseDate("01-12-2012", "dd-MM-yyyy");
		final Date parseDate2 = DateTimeUtil.parseDate("01-10-2012", "dd-MM-yyyy");
		final int numOfMonths = getNumOfMonths(parseDate, parseDate2);
		System.out.println(numOfMonths);
		System.out.println(getMonthFromDate(parseDate));
		System.out.println(getCurrentYear());

	}

	public static java.util.Date parseDate(final String strDate, final String pattern) throws ParseException {
		final SimpleDateFormat parser = new SimpleDateFormat(pattern, Locale.US);
		final Date date = parser.parse(strDate);
		return date;
	} // parseDate

	public static Date parseShortDate(final String strDate) throws ParseException {
		return parseDate(strDate, FormatUtil.PATTERN_SHORT_DATE);
	}

	public static void printCurrentTime(final Object label) {
		System.err.println(getCurrentTime(label + ""));
	}

}
