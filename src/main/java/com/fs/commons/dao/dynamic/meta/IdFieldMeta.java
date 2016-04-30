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
package com.fs.commons.dao.dynamic.meta;

import java.sql.Types;

public class IdFieldMeta extends FieldMeta {
	/**
	 *
	 */
	private static final long serialVersionUID = -1507816846870278953L;
	public static final boolean DEFAULT_AUTO_INCREMENT = true;
	boolean autoIncrement = DEFAULT_AUTO_INCREMENT;

	/**
	 *
	 */
	public IdFieldMeta() {
		setRequired(true);
		setType(Types.NUMERIC);
	}

	/**
	 *
	 * @return
	 */
	public boolean isAutoIncrement() {
		return this.autoIncrement;
	}

	@Override
	public boolean isEnabled() {
		if (isAutoIncrement()) {
			return false;
		}
		return super.isEnabled();
	}

	public boolean isParticpateInInsert() {
		return isVisible() || !isAutoIncrement();
	}

	@Override
	public boolean isVisible() {
		return super.isVisible() && !isAutoIncrement();
	}

	/**
	 *
	 * @param autoIncrement
	 */
	public void setAutoIncrement(final boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

}
