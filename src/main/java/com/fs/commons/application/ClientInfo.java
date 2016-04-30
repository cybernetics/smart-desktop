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
package com.fs.commons.application;

public class ClientInfo {
	int clientId;
	String clientName;
	byte[] logo;
	ClientNls nls;

	/**
	 * @return the clientId
	 */
	public int getClientId() {
		return this.clientId;
	}

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return this.clientName;
	}

	/**
	 * @return the logo
	 */
	public byte[] getLogo() {
		return this.logo;
	}

	public ClientNls getNls() {
		return this.nls;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(final int clientId) {
		this.clientId = clientId;
	}

	/**
	 * @param clientName
	 *            the clientName to set
	 */
	public void setClientName(final String clientName) {
		this.clientName = clientName;
	}

	/**
	 * @param logo
	 *            the logo to set
	 */
	public void setLogo(final byte[] logo) {
		this.logo = logo;
	}

	/**
	 *
	 * @param fieldValue
	 */
	public void setLogo(final Object logo) {
		if (logo instanceof byte[]) {
			setLogo((byte[]) logo);
		}
	}

	public void setNls(final ClientNls nls) {
		this.nls = nls;
	}

}
