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

public class Module {
	private int moduleId;
	private String moduleName;

	public int getModuleId() {
		return this.moduleId;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleId(final int moduleId) {
		this.moduleId = moduleId;
	}

	public void setModuleName(final String moduleName) {
		this.moduleName = moduleName;
	}

}
