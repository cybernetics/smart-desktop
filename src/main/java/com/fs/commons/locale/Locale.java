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
package com.fs.commons.locale;

import java.io.Serializable;

public class Locale implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4392783699434888201L;
	public static final Locale ARABIC = new Locale(1, "ar");
	public static final Locale ENGLISH = new Locale(2, "en");

	public static Locale valueOf(final int languageId) {
		return languageId == 1 ? ARABIC : ENGLISH;
	}

	public static Locale valueOf(final String localeString) {
		return localeString.equals("ar") ? ARABIC : ENGLISH;
	}

	int languageId;
	String languageName;

	/**
	 *
	 * @param langId
	 * @param languageName
	 */
	public Locale(final int langId, final String languageName) {
		this.languageId = langId;
		this.languageName = languageName;
	}

	/**
	 * @return the languageId
	 */
	public int getLanguageId() {
		return this.languageId;
	}

	/**
	 * @return the languageName
	 */
	public String getLanguageName() {
		return this.languageName;
	}

	/**
	 * @param languageId
	 *            the languageId to set
	 */
	public void setLanguageId(final int languageId) {
		this.languageId = languageId;
	}

	/**
	 * @param languageName
	 *            the languageName to set
	 */
	public void setLanguageName(final String languageName) {
		this.languageName = languageName;
	}

}
