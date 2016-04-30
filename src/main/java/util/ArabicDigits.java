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
	 * @1.1
	 * 
	 * @param num
	 * @return
	 */
	public static String format(final double num) {
		final NumberFormat dFormatter = new DecimalFormat("#0.00");
		final String sFformatter = dFormatter.format(num);
		return format(sFformatter + "");
	}

	/**
	 *
	 * @param num
	 *            Integer
	 * @return String
	 */
	public static String format(final long num) {
		return format(num + "");
	}

	/**
	 *
	 * @param str
	 *            String
	 * @return String
	 */
	public static String format(final String str) {
		final NumericShaper shaper = NumericShaper.getContextualShaper(NumericShaper.ARABIC);
		final char[] c = str.toCharArray();
		shaper.shape(c, 0, c.length, NumericShaper.ARABIC);
		return new String(c);
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static String formatDate(final Date date) {
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		final String str = format.format(date);
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
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		System.out.println(getCurrentDateFormatted());
	}

}
