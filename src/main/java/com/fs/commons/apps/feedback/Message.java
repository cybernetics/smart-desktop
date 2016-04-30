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
package com.fs.commons.apps.feedback;

/**
 * @1.1
 * 
 * @author ASUS
 *
 */
public class Message {

	private String panelName;
	private String errorDesc;
	private String errorScenario;

	/**
	 * @return the errorDesc
	 */
	public String getErrorDesc() {
		return this.errorDesc;
	}

	/**
	 * @return the errorScenario
	 */
	public String getErrorScenario() {
		return this.errorScenario;
	}

	/**
	 * @return the panelName
	 */
	public String getPanelName() {
		return this.panelName;
	}

	/**
	 * @param errorDesc
	 *            the errorDesc to set
	 */
	public void setErrorDesc(final String errorDesc) {
		this.errorDesc = errorDesc;
	}

	/**
	 * @param errorScenario
	 *            the errorScenario to set
	 */
	public void setErrorScenario(final String errorScenario) {
		this.errorScenario = errorScenario;
	}

	/**
	 * @param panelName
	 *            the panelName to set
	 */
	public void setPanelName(final String panelName) {
		this.panelName = panelName;
	}

}
