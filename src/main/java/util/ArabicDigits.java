/**
 * Modification History
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      10/08/2009     Jamil Shreet    -Add this method : 
 */

package util;

import java.awt.font.NumericShaper;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 This class used by jasper reports to show the arabic digits instead of
 english digits
 */
public class ArabicDigits {

	/**
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String format(String str) {
		NumericShaper shaper = NumericShaper
				.getContextualShaper(NumericShaper.ARABIC);
		char[] c = str.toCharArray();
		shaper.shape(c, 0, c.length, NumericShaper.ARABIC);
		return new String(c);
	}

	/**
	 * 
	 * @param num
	 *            Integer
	 * @return String
	 */
	public static String format(long num) {
		return format(num + "");
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String str = format.format(date);
		return format(str);
	}

	/**
	 * 
	 * @return
	 */
	public static String getCurrentDateFormatted() {
		return formatDate(new Date(System.currentTimeMillis()));
	}

	/**
	 * @1.1
	 * @param num
	 * @return
	 */
	public static String format(double num) {
		NumberFormat dFormatter = new DecimalFormat("#0.00");
		String sFformatter = dFormatter.format(num);
		return format(sFformatter + "");
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getCurrentDateFormatted());
	}

}
