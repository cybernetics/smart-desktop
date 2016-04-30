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
package com.fs.commons.dao;

import java.io.Serializable;
import java.util.Vector;

public class IdValueRecord implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 216808898783739866L;

	public static Vector<IdValueRecord> createList(final int[] ids, final Object[] values) {
		final Vector<IdValueRecord> records = new Vector<IdValueRecord>();
		for (int i = 0; i < ids.length; i++) {
			final IdValueRecord rec = new IdValueRecord();
			rec.setId(ids[i]);
			rec.setValue(values[i]);
			records.add(rec);
		}
		return records;
	}

	public static Vector<IdValueRecord> createList(final Object[] values) {
		final int ids[] = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			ids[i] = i;
		}
		return createList(ids, values);
	}

	Object id;

	Object value;

	public IdValueRecord() {
	}

	public Object getId() {
		return this.id;
	}

	public int getIdAsInteger() {
		final String id = getId().toString();
		if (id == null || id.trim().equals("")) {
			return -1;
		}
		return Integer.parseInt(id);
	}

	public Object getValue() {
		return this.value;
	}

	public void setId(final Object id) {
		this.id = id;
	}

	public void setValue(final Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		if (this.value != null) {
			return this.value.toString();
		}
		return this.id.toString();
	}
}
