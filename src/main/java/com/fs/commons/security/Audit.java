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

import java.util.Date;

import com.fs.commons.util.GeneralUtility;

public class Audit {
	int auditId;
	User user = SecurityManager.isUserLoggedIn() ? SecurityManager.getCurrentUser() : null;
	Date date;
	AuditType auditType;
	Object businessRecordId;
	String oldValue;
	String newValue;
	String description;
	private String tableName;
	private String gui;

	public int getAuditId() {
		return this.auditId;
	}

	public String getAuditText() {
		final StringBuffer b = new StringBuffer();
		b.append(getOldValue().replaceAll(",", "\n"));
		b.append("-----------------------------------------\n");
		b.append(getNewValue().replaceAll(",", "\n"));
		b.append("-----------------------------------------\n");
		return b.toString();
	}

	public AuditType getAuditType() {
		return this.auditType;
	}

	public Object getBusinessRecordId() {
		return this.businessRecordId;
	}

	public Date getDate() {
		return this.date;
	}

	public String getDescription() {
		return this.description;
	}

	public String getGui() {
		return this.gui;
	}

	public String getNewValue() {
		return this.newValue;
	}

	public String getOldValue() {
		return this.oldValue;
	}

	public String getTableName() {
		return this.tableName;
	}

	public User getUser() {
		return this.user == null ? new User(1) : this.user;
	}

	public void setAuditId(final int auditId) {
		this.auditId = auditId;
	}

	public void setAuditType(final AuditType auditType) {
		this.auditType = auditType;
	}

	public void setBusinessRecordId(final Object businessRecordId) {
		this.businessRecordId = businessRecordId;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setGui(final Object gui) {
		setGui(GeneralUtility.toXml(gui));
	}

	public void setGui(final String gui) {
		this.gui = gui;
	}

	public void setNewValue(final String newValue) {
		this.newValue = newValue;
	}

	public void setOldValue(final String oldValue) {
		this.oldValue = oldValue;
	}

	public void setTableName(final String recordName) {
		this.tableName = recordName;
	}

	public void setUser(final User user) {
		this.user = user;
	}

}
