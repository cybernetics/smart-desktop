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

import java.util.Random;

/////////////////////////////////////////////////////////////////////////////////////////
//Author : Mohamed Kiswani
//Since  : 5-2-2010
///////////////////////////////////////////////////////////////////////////////////////

public final class PasswordGenerator {
	//////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////
	public static String generateMixPassword(final int n) {
		final char[] pw = new char[n];
		int c = 'A';
		int r1 = 0;
		for (int i = 0; i < n; i++) {
			r1 = (int) (Math.random() * 3);
			switch (r1) {
			case 0:
				c = '0' + (int) (Math.random() * 10);
				break;
			case 1:
				c = 'a' + (int) (Math.random() * 26);
				break;
			case 2:
				c = 'A' + (int) (Math.random() * 26);
				break;
			}
			pw[i] = (char) c;
		}
		return new String(pw);
	}

	//////////////////////////////////////////////////////////////
	public static String generateNumricPassword(final int numberOfChar) {
		final Random randomGenerator = new Random();
		String password = "";
		for (int i = 1; i <= numberOfChar; ++i) {
			password += randomGenerator.nextInt(10);
		}
		return password;
	}

	////////////////////////////////////////////////////////////
	public static String getAlphapticPassowrds(final int numberOfChar, final boolean islowerCase) {
		double randomNumber;
		double randomNumberSetup;
		char randomCharacter;
		String password = "";
		for (int i = 0; i < numberOfChar; i++) {
			randomNumber = Math.random();
			randomNumberSetup = randomNumber * 26 + 'a';
			randomCharacter = (char) randomNumberSetup;
			password += randomCharacter;

		}
		return islowerCase ? password : password.toUpperCase();
	}

	//////////////////////////////////////////////////////////////
	public static void main(final String[] args) {
		System.err.println(PasswordGenerator.generateMixPassword(8));
	}
}