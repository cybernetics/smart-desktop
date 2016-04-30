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
package com.fs.commons.application.util;

import java.text.DecimalFormat;

import com.fs.commons.locale.Lables;

public class NumberToWords {

	private static final String ZERO = Lables.get("zero");

	private static final String HUNDRED = Lables.get("hundred");

	private static final String THOUSAND = Lables.get("thousand");

	private static final String BILLION = Lables.get("billion");

	private static final String MILLION = Lables.get("million");

	private static final String[] numNames = { "", Lables.get("one"), Lables.get("two"), Lables.get("three"), Lables.get("four"), Lables.get("five"),
			Lables.get("six"), Lables.get("seven"), Lables.get("eight"), Lables.get("nine"), Lables.get("ten"), Lables.get("eleven"),
			Lables.get("twelve"), Lables.get("thirteen"), Lables.get("fourteen"), Lables.get("fifteen"), Lables.get("sixteen"),
			Lables.get("seventeen"), Lables.get("eighteen"), Lables.get("nineteen") };

	private static final String[] tensNames = { "", Lables.get("ten"), Lables.get("twenty"), Lables.get("thirty"), Lables.get("forty"),
			Lables.get("fifty"), Lables.get("sixty"), Lables.get("seventy"), Lables.get("eighty"), Lables.get("ninety") };

	/**
	 *
	 * @param number
	 * @return
	 */
	public static String convert(final long number) {
		// 0 to 999 999 999 999
		if (number == 0) {
			return ZERO;
		}

		String snumber = Long.toString(number);

		// pad with "0"
		final String mask = "000000000000";
		final DecimalFormat df = new DecimalFormat(mask);
		snumber = df.format(number);

		// XXXnnnnnnnnn
		final int billions = Integer.parseInt(snumber.substring(0, 3));
		// nnnXXXnnnnnn
		final int millions = Integer.parseInt(snumber.substring(3, 6));
		// nnnnnnXXXnnn
		final int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
		// nnnnnnnnnXXX
		final int thousands = Integer.parseInt(snumber.substring(9, 12));

		String tradBillions;
		switch (billions) {
		case 0:
			tradBillions = "";
			break;
		case 1:
			tradBillions = convertLessThanOneThousand(billions) + " " + BILLION;
			break;
		default:
			tradBillions = convertLessThanOneThousand(billions) + " " + BILLION;
		}
		String result = tradBillions;

		String tradMillions;
		switch (millions) {
		case 0:
			tradMillions = "";
			break;
		case 1:
			tradMillions = convertLessThanOneThousand(millions) + " " + MILLION;
			break;
		default:
			tradMillions = convertLessThanOneThousand(millions) + " " + MILLION;
		}
		result = result + tradMillions;

		String tradHundredThousands;
		switch (hundredThousands) {
		case 0:
			tradHundredThousands = "";
			break;
		case 1:
			tradHundredThousands = THOUSAND;
			break;
		default:
			tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " " + THOUSAND;
		}
		result = result + tradHundredThousands;

		String tradThousand;
		tradThousand = convertLessThanOneThousand(thousands);
		result = result + tradThousand;

		// remove extra spaces!
		return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	}

	/**
	 *
	 * @param number
	 * @return
	 */
	private static String convertLessThanOneThousand(int number) {
		String soFar;

		if (number % 100 < 20) {
			soFar = numNames[number % 100];
			number /= 100;
		} else {
			soFar = numNames[number % 10];
			number /= 10;

			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0) {
			return soFar;
		}
		return numNames[number] + HUNDRED + soFar;
	}

}
