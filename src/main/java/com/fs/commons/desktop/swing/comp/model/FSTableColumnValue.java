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
package com.fs.commons.desktop.swing.comp.model;

public class FSTableColumnValue<T> {
	FSTableColumn tableColumn;
	T value;
	private boolean enabled = true;

	public FSTableColumnValue() {
	}

	public FSTableColumnValue(final FSTableColumn tableColumn) {
		this.tableColumn = tableColumn;
	}

	public FSTableColumn getTableColumn() {
		return this.tableColumn;
	}

	public T getValue() {
		return this.value;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public void setTableColumn(final FSTableColumn tableColumn) {
		this.tableColumn = tableColumn;
	}

	public void setValue(final T value) {
		this.value = value;
	}

}
