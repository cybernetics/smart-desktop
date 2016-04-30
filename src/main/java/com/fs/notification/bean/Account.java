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
package com.fs.notification.bean;

public class Account {
	int id;
	String number;
	String name;
	String mobile;
	String email;
	boolean active;

	// ////////////////////////////////////////////////////
	@Override
	public boolean equals(final Object obj) {
		final Account that = (Account) obj;
		return getId() == that.getId();
	}

	public String getEmail() {
		return this.email;
	}

	// ////////////////////////////////////////////////////////
	public int getId() {
		return this.id;
	}

	public String getMobile() {
		return this.mobile;
	}

	// //////////////////////////////////////////////////////
	public String getName() {
		return this.name;
	}

	public String getNumber() {
		return this.number;
	}

	// ////////////////////////////////////////////////////
	public boolean isActive() {
		return this.active;
	}

	// ////////////////////////////////////////////////////
	public void setActive(final boolean active) {
		this.active = active;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	// //////////////////////////////////////////////////////
	public void setId(final int id) {
		this.id = id;
	}

	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	// //////////////////////////////////////////////////////
	public void setName(final String name) {
		this.name = name;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

}
