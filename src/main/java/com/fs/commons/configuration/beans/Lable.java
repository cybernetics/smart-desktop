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

public class Lable {
	private int lableId;
	private String lableKey;
	private String lableValue;
	private int moduleId;
	private int languageId;

	public int getLableId() {
		return this.lableId;
	}

	public String getLableKey() {
		return this.lableKey;
	}

	public String getLableValue() {
		return this.lableValue;
	}

	public int getLanguageId() {
		return this.languageId;
	}

	public int getModuleId() {
		return this.moduleId;
	}

	public void setLableId(final int lableId) {
		this.lableId = lableId;
	}

	public void setLableKey(final String lableKey) {
		this.lableKey = lableKey;
	}

	public void setLableValue(final String lableValue) {
		this.lableValue = lableValue;
	}

	public void setLanguageId(final int languageId) {
		this.languageId = languageId;
	}

	public void setModuleId(final int moduleId) {
		this.moduleId = moduleId;
	}

}
