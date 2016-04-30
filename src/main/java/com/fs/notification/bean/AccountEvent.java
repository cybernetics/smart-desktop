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

import com.fs.notification.NotificationConstants;

public class AccountEvent {
	int id;
	Account account;
	Event event;
	Status status;

	// //////////////////////////////////////////////////////
	public AccountEvent() {
	}

	// //////////////////////////////////////////////////////
	public AccountEvent(final Event event, final Account account) {
		this.event = event;
		this.account = account;
		this.status = new Status(NotificationConstants.EVENT_STATUS_NEW);
	}

	// //////////////////////////////////////////////////////
	@Override
	public boolean equals(final Object obj) {
		final AccountEvent that = (AccountEvent) obj;
		return getId() == that.getId();
	}

	// //////////////////////////////////////////////////////
	public Account getAccount() {
		return this.account;
	}

	// //////////////////////////////////////////////////////
	public Event getEvent() {
		return this.event;
	}

	// //////////////////////////////////////////////////////
	public int getId() {
		return this.id;
	}

	// //////////////////////////////////////////////////////
	public Status getStatus() {
		return this.status;
	}

	// //////////////////////////////////////////////////////
	public void setAccount(final Account account) {
		this.account = account;
	}

	// //////////////////////////////////////////////////////
	public void setEvent(final Event event) {
		this.event = event;
	}

	// //////////////////////////////////////////////////////
	public void setId(final int id) {
		this.id = id;
	}

	// //////////////////////////////////////////////////////
	public void setStatus(final Status status) {
		this.status = status;
	}
}
