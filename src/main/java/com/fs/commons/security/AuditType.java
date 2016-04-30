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

import com.fs.commons.application.Module;

public class AuditType {
	public static AuditType AUDIT_ADD_RECORD = new AuditType(1, "ADD");
	public static AuditType AUDIT_UPDATE_RECORD = new AuditType(2, "UPDATE");
	public static AuditType AUDIT_DELETE_RECORD = new AuditType(3, "DELETE");
	public static AuditType AUDIT_LOGIN = new AuditType(4, "LOGIN");
	public static AuditType AUDIT_LOGOUT = new AuditType(5, "LOGOUT");

	int auditTypeId;
	String auditTypeName;
	Module module;

	public AuditType(final int id) {
		this.auditTypeId = id;
	}

	public AuditType(final int id, final String name) {
		this.auditTypeId = id;
		setAuditTypeName(name);
	}

	public int getAuditType() {
		return this.auditTypeId;
	}

	public String getAuditTypeName() {
		return this.auditTypeName;
	}

	public Module getModule() {
		return this.module;
	}

	public void setAuditType(final int auditType) {
		this.auditTypeId = auditType;
	}

	public void setAuditTypeName(final String auditTypeName) {
		this.auditTypeName = auditTypeName;
	}

	public void setModule(final Module module) {
		this.module = module;
	}
}
