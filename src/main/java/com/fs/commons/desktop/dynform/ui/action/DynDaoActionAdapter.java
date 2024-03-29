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
package com.fs.commons.desktop.dynform.ui.action;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;

public class DynDaoActionAdapter implements DynDaoActionListener {

	@Override
	public void afterAddRecord(final Record record) throws JKDataAccessException {

	}

	@Override
	public void afterClosePanel() {

	}

	@Override
	public void afterDeleteRecord(final Record record) throws JKDataAccessException {

	}

	@Override
	public void afterResetComponents() {

	}

	@Override
	public void afterSetMode(final DynDaoMode mode) {

	}

	@Override
	public void afterUpdateRecord(final Record record) throws JKDataAccessException {

	}

	@Override
	public void beforeAddRecord(final Record record) throws JKDataAccessException {

	}

	@Override
	public void beforeClosePanel() {

	}

	@Override
	public void beforeDeleteRecord(final Record record) throws JKDataAccessException {

	}

	@Override
	public void beforeResetComponents(final Record record) {

	}

	@Override
	public void beforeSetMode(final DynDaoMode mode) {

	}

	@Override
	public void beforeUpdateRecord(final Record record) throws JKDataAccessException {

	}

	@Override
	public void onDaoException(final Record recod, final JKDataAccessException ex) {

	}

	@Override
	public void onRecordFound(final Record record) {

	}

	@Override
	public void onRecordNotFound(final Object recordId, final JKDataAccessException e) {

	}
}
