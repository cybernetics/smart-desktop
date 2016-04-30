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

public class Status {
	int id;
	String name;
	boolean active;

	public Status() {
		// TODO Auto-generated constructor stub
	}

	public Status(final int id) {
		this.id = id;
	}

	// //////////////////////////////////////////////////////
	@Override
	public boolean equals(final Object obj) {
		final Status that = (Status) obj;
		return getId() == that.getId();
	}

	// //////////////////////////////////////////////////////
	public int getId() {
		return this.id;
	}

	// //////////////////////////////////////////////////////
	public String getName() {
		return this.name;
	}

	// //////////////////////////////////////////////////////
	public boolean isActive() {
		return this.active;
	}

	// //////////////////////////////////////////////////////
	public void setActive(final boolean active) {
		this.active = active;
	}

	// //////////////////////////////////////////////////////
	public void setId(final int id) {
		this.id = id;
	}

	// //////////////////////////////////////////////////////
	public void setName(final String name) {
		this.name = name;
	}
}
