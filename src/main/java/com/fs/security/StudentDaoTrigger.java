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
package com.fs.security;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.trigger.TriggerAdapter;
import com.fs.commons.util.GeneralUtility;

public class StudentDaoTrigger extends TriggerAdapter {
	@Override
	public void beforeAdd(final Record record) throws JKDataAccessException {
		String value = record.getField("password").getValue();
		value = GeneralUtility.encode(value);
		record.getField("password").setValue(value);
	}

	@Override
	public void beforeUpdate(final Record oldRecord, final Record newRecord) throws JKDataAccessException {
		beforeAdd(newRecord);
	}
}
