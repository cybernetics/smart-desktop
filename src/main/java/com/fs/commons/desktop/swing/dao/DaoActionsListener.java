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
package com.fs.commons.desktop.swing.dao;

import com.fs.commons.dao.JKDataAccessException;

public interface DaoActionsListener {
	/**
	 *
	 */
	public String handleAddEvent() throws JKDataAccessException;

	/**
	 *
	 */
	public void handleDeleteEvent() throws JKDataAccessException;

	/**
	 *
	 */
	public void handleFindEvent(Object id) throws JKDataAccessException;

	/**
	 *
	 */
	public void handleSaveEvent() throws JKDataAccessException;

}
