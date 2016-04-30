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

public class Event {

	int id;
	NotificationType notificationType;
	String title;
	String text;
	Status status;

	// //////////////////////////////////////////////////////
	@Override
	public boolean equals(final Object obj) {
		final Event that = (Event) obj;
		return getId() == that.getId();
	}

	// //////////////////////////////////////////////////////
	public int getId() {
		return this.id;
	}

	public NotificationType getNotificationType() {
		return this.notificationType;
	}

	// //////////////////////////////////////////////////////
	public Status getStatus() {
		return this.status;
	}

	// //////////////////////////////////////////////////////
	public String getText() {
		return this.text;
	}

	// //////////////////////////////////////////////////////
	public String getTitle() {
		return this.title;
	}

	// //////////////////////////////////////////////////////
	public void setId(final int id) {
		this.id = id;
	}

	public void setNotificationType(final NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	// //////////////////////////////////////////////////////
	public void setStatus(final Status status) {
		this.status = status;
	}

	// //////////////////////////////////////////////////////
	public void setText(final String text) {
		this.text = text;
	}

	// //////////////////////////////////////////////////////
	public void setTitle(final String title) {
		this.title = title;
	}

}
