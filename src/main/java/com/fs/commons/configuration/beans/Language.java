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
package com.fs.commons.configuration.beans;

public class Language {
	private int langId;
	private String langName;
	private String langShortName;
	private LangDirection LangDirection;

	public LangDirection getLangDirection() {
		return this.LangDirection;
	}

	public int getLangId() {
		return this.langId;
	}

	public String getLangName() {
		return this.langName;
	}

	public String getLangShortName() {
		return this.langShortName;
	}

	public void setLangDirection(final LangDirection langDirection) {
		this.LangDirection = langDirection;
	}

	public void setLangId(final int langId) {
		this.langId = langId;
	}

	public void setLangName(final String langName) {
		this.langName = langName;
	}

	public void setLangShortName(final String langShortName) {
		this.langShortName = langShortName;
	}

}
