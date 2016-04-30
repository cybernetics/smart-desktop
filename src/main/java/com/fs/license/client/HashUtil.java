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
package com.fs.license.client;

public class HashUtil {

	public static void main(final String[] args) {
		final HashUtil hash = new HashUtil();
		final String h = hash.hash("46-46-46-46-46-46-46-46-46-72-97-99-107-101-100-32-76-105-99-101-110-115-101-46-46-46-46-46-46-46");
		final String d = hash.deHash(h);

		System.out.println(h);
		System.out.println(d);
	}

	/**
	 *
	 */
	public HashUtil() {
	}

	/**
	 *
	 * @param src
	 * @return
	 */
	public String deHash(final String src) {
		String str = "";
		final String[] bytes = src.split("-");
		for (final String b : bytes) {
			final String digit = b;
			if (digit != null && !digit.equals("")) {
				str += "" + (char) Integer.parseInt(digit);
			}
		}
		return str;
	}

	/**
	 *
	 * @param src
	 * @return
	 */
	public String hash(final String src) {
		String str = "";
		final byte[] bytes = src.getBytes();
		for (final byte b : bytes) {
			str += "" + b + "-";
		}
		return str;
	}

}
