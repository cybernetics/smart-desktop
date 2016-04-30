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

public class RecordActionAdapter implements RecordActionListener<String> {

	@Override
	public void onException(final Exception e) {
	}

	@Override
	public void recordAdded(final String recordId) {
	}

	@Override
	public void recordDeleted(final String recordId) {
	}

	@Override
	public void recordSelected(final String recordId) {
	}

	@Override
	public void recordUpdated(final String recordId) {
	}

}
