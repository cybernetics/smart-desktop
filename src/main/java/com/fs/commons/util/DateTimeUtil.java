package com.fs.commons.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.exception.DaoException;

public class DateTimeUtil {
	public enum CompareDates {
		DATE1_LESS_THAN_DATE2, DATE1_GREATER_THAN_DATE2, DATE1_EQUAL_DATE2;
	}

	public static Date parseShortDate(String strDate) throws ParseException {
		return parseDate(strDate, FormatUtil.PATTERN_SHORT_DATE);
	}

	public static java.util.Date parseDate(String strDate, String pattern) throws ParseException {
		SimpleDateFormat parser = new SimpleDateFormat(pattern, Locale.US);
		Date date = parser.parse(strDate);
		return date;
	} // parseDate

	/**
	 * 
	 * @return int
	 */
	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public static int getCurrentMonth() {
		return getMonthFromDate(new Date());
	}

	/**
	 * 
	 * @return int
	 * @param date
	 *            Date
	 */
	public static int getYearFromData(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 
	 * @return int / //@v 1.1 start Update Bashar Nadir
	 * @param date
	 *            Date
	 */
	public static int getMonthFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	public static void printCurrentTime(Object label) {
		System.err.println(getCurrentTime(label + ""));
	}

	public static String getCurrentTime(String label) {
		Date date = new Date();
		return formatFullTime(date) + "  :" + label;
	}

	public static String formatFullTime(Date date) {
		return FormatUtil.formatDate(date, "hh:mm:ss SSS");
	}

	public static String formatTime(Date date) {
		return FormatUtil.formatDate(date, "hh:mm");
	}

	public static boolean isTimesEqaualed(Date time1, Date time2) {
		return formatTime(time1).equals(formatTime(time2));
	}

	public static void main(String[] args) throws ParseException {
		Date parseDate = DateTimeUtil.parseDate("01-12-2012", "dd-MM-yyyy");
		Date parseDate2 = DateTimeUtil.parseDate("01-10-2012", "dd-MM-yyyy");
		int numOfMonths = getNumOfMonths(parseDate, parseDate2);
		System.out.println(numOfMonths);
		System.out.println(getMonthFromDate(parseDate));
		System.out.println(getCurrentYear());

	}

	public static int getNumOfMonths(Date date1, Date date2) {
		Calendar firstDate = Calendar.getInstance();
		Date date = new Date(date1.getTime());
		firstDate.setTime(date);
		Calendar secondDate = Calendar.getInstance();
		Date date3 = new Date(date2.getTime());
		secondDate.setTime(date3);
		int months = firstDate.get(Calendar.MONTH) - secondDate.get(Calendar.MONTH);

		return months;
	}

	public static String formatCurrentDate() {
		return FormatUtil.formatDate(new Date(), "yyyy-MM-dd");
	}

	public static String formatCurrentDatabaseDate() {
		try {
			return FormatUtil.formatDate(DaoUtil.getSystemDate(), "yyyy-MM-dd");
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
			// unreachable
			return null;
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// Mohamed Kiswani
	//
	// /////////////////////////////////////////////////////////////////////////////////////
	public static String getFormatedDate(long lastModified) {
		return FormatUtil.formatDate(new Date(lastModified), FormatUtil.PATTERN_MYSQL);
	}

	public static String getCurrentDate() {
		return getFormatedDate(System.currentTimeMillis());
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// Mohamed Kiswani
	//
	// /////////////////////////////////////////////////////////////////////////////////////
	public static CompareDates compareTwoDates(Date date1, Date date2) {
		Date d1 = new Date(date1.getTime());// to unify the format of the dates
											// before the compare
		Date d2 = new Date(date2.getTime());
		if (d1.compareTo(d2) < 0)
			return CompareDates.DATE1_LESS_THAN_DATE2;
		else if (d1.compareTo(d2) > 0)
			return CompareDates.DATE1_GREATER_THAN_DATE2;
		else
			return CompareDates.DATE1_EQUAL_DATE2;
	}

	public static Date adddDaysToCurrentDate(int numberOfDays) {
		Date date = new Date();
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(Calendar.DATE, numberOfDays);
		return instance.getTime();
	}

	public static boolean isDate(String strDate, String pattern) {
		try {
			parseDate(strDate, pattern);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Date adddMonths(int numOfMonths) {
		return addMonths(new Date(), numOfMonths);
	}

	public static Date addMonths(Date date, int numOfMonths) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(Calendar.MONTH, numOfMonths);
		return instance.getTime();
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DaoException
	 */
	public static boolean isPeriodActive(Date startDate, Date endDate) throws DaoException {
		if (startDate == null && endDate == null) {
			return true;
		}
		if (startDate == null) {
			throw new DaoException("START_DATE_CAN_NOT_BE_NULL");
		}
		if (endDate == null) {
			throw new DaoException("END_DATE_CAN_NOT_BE_NULL");
		}
		if (compareTwoDates(startDate, endDate).equals(CompareDates.DATE1_GREATER_THAN_DATE2)) {
			throw new DaoException("START_DATE_MUST_BE_BEFORE_END_DATE");
		}
		boolean startLessThanCurrent = compareTwoDates(startDate, DaoUtil.getSystemDate()).equals(CompareDates.DATE1_LESS_THAN_DATE2);
		boolean endGreaterThanCurrent = compareTwoDates(endDate, DaoUtil.getSystemDate()).equals(CompareDates.DATE1_GREATER_THAN_DATE2);
		return startLessThanCurrent && endGreaterThanCurrent;
	}

	/**
	 * 
	 * @param timeFrom
	 * @param timeTo
	 * @return
	 * @throws DateParseException
	 */
	public static long getDifferent(Time timeFrom, Time timeTo) throws ParseException {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		// the a means am/pm marker
		Date date = format.parse(timeFrom.toString());
		Date date2 = format.parse(timeTo.toString());
		long difference = (date2.getTime() - date.getTime()) / 1000 / 60;
		return difference;
	}

	/**
	 * 
	 * @return
	 */
	public static int getCurrentDayInMonth() {
		Date date = new Date();
		return getDayOfMonth(date);
	}

	private static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static boolean isDateEqaualed(java.util.Date date1, java.util.Date date2) {
		String d1 = FormatUtil.formatDate(date1, FormatUtil.PATTERN_MYSQL);
		String d2 = FormatUtil.formatDate(date2, FormatUtil.PATTERN_MYSQL);
		return d1.equalsIgnoreCase(d2);
	}

	/**
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isTimesEqaualed(Time time1, Time time2) {
		String d1 = FormatUtil.formatTime(time1);
		String d2 = FormatUtil.formatTime(time2);
		return d1.equalsIgnoreCase(d2);
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public static boolean isTimeConflict(Time timeFrom, Time timeTo, Time otherTimeFrom, Time otherTimeTo) {
		if (DateTimeUtil.isTimesEqaualed(timeFrom,otherTimeFrom) || DateTimeUtil.isTimesEqaualed(timeTo,otherTimeTo)) {
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

	public static Calendar getCurrentTime(Time time) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
		calendar.set(Calendar.HOUR_OF_DAY, time.getMinutes());
		return calendar;
	}

	public static boolean isCurrentTimeBetweenTowTimes(Date fromDate, Date fromTime, Date toDate, Date timeTo) {
		FSTimeObject currntTime = getCurrntTime();
		FSTimeObject fromTimeObject = new FSTimeObject();
		FSTimeObject toTimeObject = new FSTimeObject();
		if(currntTime.after(fromTimeObject.toTimeObject(fromDate,fromTime)) && currntTime.before(toTimeObject.toTimeObject(toDate,timeTo))){
			return true;
		}
		return false;
	}
	
	public static FSTimeObject getCurrntTime() {
		FSTimeObject fsTimeObject = new FSTimeObject();
		Calendar instance = Calendar.getInstance();
		fsTimeObject.setYear(instance.get(Calendar.YEAR));
		fsTimeObject.setMonth(instance.get(Calendar.MONTH));
		fsTimeObject.setDay(instance.get(Calendar.DAY_OF_MONTH));
		fsTimeObject.setHour(instance.get(Calendar.HOUR_OF_DAY));
		fsTimeObject.setMunite(instance.get(Calendar.MINUTE));
		return fsTimeObject;
	}



}
