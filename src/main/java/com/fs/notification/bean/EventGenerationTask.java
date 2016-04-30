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

import com.fs.commons.apps.templates.beans.Query;
import com.fs.commons.apps.templates.beans.Template;

public class EventGenerationTask {
	int id;
	NotificationType notType;
	Query templateValues;
	Query appliedAccountsQuery;
	private Template template;
	private String name;

	public Query getAppliedAccountsQuery() {
		return this.appliedAccountsQuery;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public NotificationType getNotType() {
		return this.notType;
	}

	public Template getTemplate() {
		return this.template;
	}

	public Query getTemplateValues() {
		return this.templateValues;
	}

	public void setAppliedAccountsQuery(final Query appliedAccountsQuery) {
		this.appliedAccountsQuery = appliedAccountsQuery;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;

	}

	public void setNotType(final NotificationType notType) {
		this.notType = notType;
	}

	public void setTemplate(final Template template) {
		this.template = template;
	}

	public void setTemplateValues(final Query templateValues) {
		this.templateValues = templateValues;
	}

}
