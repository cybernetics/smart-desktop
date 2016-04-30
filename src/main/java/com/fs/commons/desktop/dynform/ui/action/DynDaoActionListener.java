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

import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;

public interface DynDaoActionListener {

	public void afterAddRecord(Record record) throws DaoException;

	public void afterClosePanel();

	public void afterDeleteRecord(Record record) throws DaoException;

	public void afterResetComponents();

	public void afterSetMode(DynDaoMode mode);

	public void afterUpdateRecord(Record record) throws DaoException;

	public void beforeAddRecord(Record record) throws DaoException;

	public void beforeClosePanel();

	public void beforeDeleteRecord(Record record) throws DaoException;

	public void beforeResetComponents(Record record);

	public void beforeSetMode(DynDaoMode mode);

	public void beforeUpdateRecord(Record record) throws DaoException;

	public void onDaoException(Record recod, DaoException ex);

	public void onRecordFound(Record record);

	public void onRecordNotFound(Object recordId, DaoException e);
}
