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

public class TemplateVariable {

	int tempVarId;
	Template temp;
	int varIndex;
	Variable var;

	// ///////////////////////////////////////////////////////////////
	public Template getTemp() {
		return this.temp;
	}

	// ///////////////////////////////////////////////////////////////
	public int getTempVarId() {
		return this.tempVarId;
	}

	// ///////////////////////////////////////////////////////////////
	public Variable getVar() {
		return this.var;
	}

	// ///////////////////////////////////////////////////////////////
	public int getVarIndex() {
		return this.varIndex;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTemp(final Template temp) {
		this.temp = temp;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTempVarId(final int tempVarId) {
		this.tempVarId = tempVarId;
	}

	// ///////////////////////////////////////////////////////////////
	public void setVar(final Variable var) {
		this.var = var;
	}

	// ///////////////////////////////////////////////////////////////
	public void setVarIndex(final int varIndex) {
		this.varIndex = varIndex;
	}

}
