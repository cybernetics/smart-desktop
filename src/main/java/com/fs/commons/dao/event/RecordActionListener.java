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
package com.fs.commons.dao.event;

public interface RecordActionListener<ID> {

	/**
	 *
	 * @param e
	 */
	public void onException(Exception e);

	/**
	 *
	 * @param recordId
	 */
	public void recordAdded(ID recordId);

	/**
	 *
	 * @param recordId
	 */
	public void recordDeleted(ID recordId);

	/**
	 *
	 * @param recordId
	 */
	public void recordSelected(ID recordId);

	/**
	 *
	 * @param recordId
	 */
	public void recordUpdated(ID recordId);
}
