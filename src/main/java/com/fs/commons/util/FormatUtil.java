package com.fs.commons.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormatUtil {

	public static final String PATTERN_SHORT_DATE = "dd-MM-yyyy";
	public static final String PATTERN_DEFAULT = "dd/MM/yyyy";
	public static final String PATTERN_MYSQL = "yyyy-MM-dd";
	
	public static final String FULL_DB_PATTERN = "ddMMyyyy_kkmmss";
	private static DecimalFormat numberFormat = new DecimalFormat("###,###,##0.##");
	public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";

	/**
	 * 
	 * @param mark
	 * @return
	 */
	public static String formatAmount(double mark) {
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		format.setMaximumFractionDigits(2);
		// format.setDecimalSeparatorAlwaysShown(true);
		return format.format(mark);
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
	public static String formatShortDate(Date date) {
		return formatDate(date, PATTERN_SHORT_DATE);
	}

	/**
	 * 
	 * @param mark
	 * @return
	 */
	public static String formatMark(double mark) {
		DecimalFormat format = new DecimalFormat("#0.00");
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
	public static String formatDate(java.util.Date date) {
		return formatDate(date, PATTERN_DEFAULT);
	}

	public static String formatDate(java.util.Date date, String pattren) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattren, new Locale("en", "US"));
		if (date == null) {
			return "";
		}
		return formatter.format(date);
	} // formatDate

	/**
	 * 
	 * @param timeFrom
	 * @return
	 */
	public static String formatTime(Time time) {
		return formatDate(time, "hh:mm");
	}

	public static void main(String[] args) {
		// System.out.println(formatTime(new Time(System.currentTimeMillis())));
		System.out.println(formatAmount(516.238f));
	}

	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isEqualedDates(Date date1, Date date2) {
		return formatShortDate(date1).equals(formatShortDate(date2));
	}

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

	public static String formatMoney(double amount) {
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		format.setMaximumFractionDigits(3);
		// format.setDecimalSeparatorAlwaysShown(true);
		return format.format(amount);
	}

	public static String formatMysqlDate(Date date) {
		return formatDate(date, PATTERN_MYSQL);
	}

	public static String convertFromShortToMysqlDate(String date) {
		Date date2;
		try {
			date2 = DateTimeUtil.parseShortDate(date);
			return FormatUtil.formatMysqlDate(date2);
		} catch (ParseException e) {
			ExceptionUtil.handleException(e);
			return null;
		}
	}

	public static String formatDouble(Object value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getHour(Date timeFrom) throws ParseException {
		Calendar instance = Calendar.getInstance();
		instance.setTime(timeFrom);
		int hour = instance.get(Calendar.HOUR);
		return hour;

	}
	
	public static boolean equals(Date date1, Date date2) {
		if ((date1 == null && date2 != null) || (date1 != null && date2 == null)) {
			return false;
		}
		return formatDate(date1, DEFAULT_DATE_PATTERN).equals(formatDate(date2, DEFAULT_DATE_PATTERN));
	}
	
	public static long getDifferent(Date timeFrom, Date timeTo) throws ParseException {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date = format.parse(timeFrom.toString());
		Date date2 = format.parse(timeTo.toString());
		long difference = (date2.getTime() - date.getTime()) / 1000 / 60;
		return difference;
	}
	
	public static String formatNumber(Number number) {
		return numberFormat.format(number);
	}
	
	public static long getDayDifference(Date startDate, Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long diffTime = endTime - startTime;
		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		return diffDays;
	}
}
