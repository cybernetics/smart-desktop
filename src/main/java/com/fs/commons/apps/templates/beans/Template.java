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
package com.fs.commons.apps.templates.beans;

import java.util.ArrayList;

public class Template {

	int tempId;
	String tempName;
	String tempTitle;
	String tempText;
	private ArrayList<TemplateVariable> variables;

	// ///////////////////////////////////////////////////////////////
	public int getTempId() {
		return this.tempId;
	}

	// ///////////////////////////////////////////////////////////////
	public String getTempName() {
		return this.tempName;
	}

	// ///////////////////////////////////////////////////////////////
	public String getTempText() {
		return this.tempText;
	}

	// ///////////////////////////////////////////////////////////////
	public String getTempTitle() {
		return this.tempTitle;
	}

	public ArrayList<TemplateVariable> getVariables() {
		return this.variables;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTempId(final int tempId) {
		this.tempId = tempId;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTempName(final String tempName) {
		this.tempName = tempName;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTempText(final String tempText) {
		this.tempText = tempText;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTempTitle(final String tempTitle) {
		this.tempTitle = tempTitle;
	}

	public void setVariables(final ArrayList<TemplateVariable> variables) {
		this.variables = variables;
	}

}
