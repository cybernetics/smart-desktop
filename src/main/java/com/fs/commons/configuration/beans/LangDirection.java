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

public class LangDirection {
	private int langDirId;
	private String langDirIdStr;
	private String dirName;

	public String getDirName() {
		return this.dirName;
	}

	public int getLangDirId() {
		return this.langDirId;
	}

	public String getLangDirIdStr() {
		return this.langDirIdStr;
	}

	public void setDirName(final String dirName) {
		this.dirName = dirName;
	}

	public void setLangDirId(final int langDirId) {
		this.langDirId = langDirId;
	}

	public void setLangDirIdStr(final String langDirIdStr) {
		this.langDirIdStr = langDirIdStr;
	}

}
