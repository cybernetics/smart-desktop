package com.fs.notification.bean;

import com.fs.commons.apps.templates.beans.Query;
import com.fs.commons.apps.templates.beans.Template;

public class EventGenerationTask {
	int id;
	NotificationType notType;
	Query templateValues;
	Query appliedAccountsQuery;
	private Template template;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public NotificationType getNotType() {
		return notType;
	}

	public void setNotType(NotificationType notType) {
		this.notType = notType;
	}

	public Query getTemplateValues() {
		return templateValues;
	}

	public void setTemplateValues(Query templateValues) {
		this.templateValues = templateValues;
	}

	public Query getAppliedAccountsQuery() {
		return appliedAccountsQuery;
	}

	public void setAppliedAccountsQuery(Query appliedAccountsQuery) {
		this.appliedAccountsQuery = appliedAccountsQuery;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Template getTemplate() {
		return template;
	}

	public void setName(String name) {
		this.name = name;

	}

	public String getName() {
		return name;
	}

}
