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
package com.fs.commons.apps.templates.beans;

public class Query {
	int queryId;
	String desc;
	QueryType queryType;
	String queryText;

	public String getDesc() {
		return this.desc;
	}

	public int getQueryId() {
		return this.queryId;
	}

	public String getQueryText() {
		return this.queryText;
	}

	public QueryType getQueryType() {
		return this.queryType;
	}

	public void setDesc(final String desc) {
		this.desc = desc;
	}

	public void setQueryId(final int queryId) {
		this.queryId = queryId;
	}

	public void setQueryText(final String queryText) {
		this.queryText = queryText;
	}

	public void setQueryType(final QueryType queryType) {
		this.queryType = queryType;
	}

}
