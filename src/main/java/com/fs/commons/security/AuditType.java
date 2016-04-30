package com.fs.commons.security;

import com.fs.commons.application.Module;

public class AuditType {
	public static AuditType AUDIT_ADD_RECORD=new  AuditType(1,"ADD");
	public static AuditType AUDIT_UPDATE_RECORD=new  AuditType(2,"UPDATE");
	public static AuditType AUDIT_DELETE_RECORD=new  AuditType(3,"DELETE");
	public static AuditType AUDIT_LOGIN=new  AuditType(4,"LOGIN");
	public static AuditType AUDIT_LOGOUT=new  AuditType(5,"LOGOUT");
	
	int auditTypeId;
	String auditTypeName;
	Module module;

	public AuditType(int id, String name) {
		auditTypeId = id;
		setAuditTypeName(name);
	}

	public AuditType(int id) {
		this.auditTypeId=id;
	}

	public int getAuditType() {
		return auditTypeId;
	}

	public void setAuditType(int auditType) {
		this.auditTypeId = auditType;
	}

	public String getAuditTypeName() {
		return auditTypeName;
	}

	public void setAuditTypeName(String auditTypeName) {
		this.auditTypeName = auditTypeName;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}
}
