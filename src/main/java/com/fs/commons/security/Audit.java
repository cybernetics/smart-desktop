package com.fs.commons.security;

import java.util.Date;

import com.fs.commons.locale.Lables;
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
		return auditId;
	}

	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}

	public User getUser() {
		return user == null ? new User(1) : user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public AuditType getAuditType() {
		return auditType;
	}

	public void setAuditType(AuditType auditType) {
		this.auditType = auditType;
	}

	public Object getBusinessRecordId() {
		return businessRecordId;
	}

	public void setBusinessRecordId(Object businessRecordId) {
		this.businessRecordId = businessRecordId;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String recordName) {
		this.tableName = recordName;
	}

	public String getGui() {
		return gui;
	}

	public void setGui(String gui) {
		this.gui = gui;
	}

	public void setGui(Object gui) {
		setGui(GeneralUtility.toXml(gui));
	}

	public String getAuditText() {
		StringBuffer b=new StringBuffer();
		b.append(getOldValue().replaceAll(",", "\n"));
		b.append("-----------------------------------------\n");
		b.append(getNewValue().replaceAll(",", "\n"));
		b.append("-----------------------------------------\n");
		return b.toString();
	}

}
