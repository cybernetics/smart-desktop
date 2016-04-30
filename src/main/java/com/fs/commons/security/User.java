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
package com.fs.commons.security;

import com.fs.commons.util.TextUtil;

public class User {
	private int userRecordId;
	private String userId;
	private String fullName;
	private String password;
	private int status;

	private boolean disabled;

	public User() {
	}

	/**
	 *
	 * @param userRecordId
	 */
	public User(final int userRecordId) {
		this.userRecordId = userRecordId;
	}

	public User(final int id, final String name, final String fullName) {
		this.userRecordId = id;
		this.userId = name;
		this.fullName = fullName;
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 * @return
	 */
	public String getFullName() {
		if (TextUtil.isEmpty(this.fullName)) {
			return getUserId();
		}
		return this.fullName;
	}

	public int getId() {
		return getUserRecordId();
	}

	/**
	 *
	 * @return
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 *
	 * @return
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 *
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}

	public int getUserRecordId() {
		return this.userRecordId;
	}

	/**
	 *
	 * @return
	 */
	public boolean isDisabled() {
		return this.disabled;
	}

	/**
	 *
	 * @param disabled
	 */
	public void setDisabled(final boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 *
	 * @param name
	 */
	public void setFullName(final String name) {
		this.fullName = name;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 *
	 * @param status
	 */
	public void setStatus(final int status) {
		this.status = status;
	}

	/**
	 *
	 * @param userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public void setUserRecordId(final int userRecordId) {
		this.userRecordId = userRecordId;
	}

}
