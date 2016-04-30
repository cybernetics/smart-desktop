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
package com.fs.commons.desktop.dynform.ui.masterdetail;

import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;

/**
 * @author u087
 *
 */
public interface DetailPanel {
	public void addDynDaoActionListener(DynDaoActionListener listener);

	public String getName();

	// find by primary key
	public void handleFind(Object idValud) throws DaoException;

	public void resetComponents() throws DaoException;

	// find by the foreign key: which could be ONE_TO_ONE , ONE_TO_MANY
	public void setMasterIdValue(Object object) throws DaoException;

	public void setMode(DynDaoMode mode);

	public void setName(String name);

}
