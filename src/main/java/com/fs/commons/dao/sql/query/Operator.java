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
package com.fs.commons.dao.sql.query;

public class Operator implements QueryComponent {
	public static final Operator EQUAL = new Operator("=");
	public static final Operator COMMA = new Operator(",");
	public static final Operator MORE_THAN = new Operator(">");
	public static final Operator LESS_THAN = new Operator("<");

	String name;

	public Operator(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public boolean isInline() {
		return true;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Object toQueryElement() {
		return getName();
	}

	@Override
	public String toString() {
		return toQueryElement().toString();
	}
}
