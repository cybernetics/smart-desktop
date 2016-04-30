package com.fs.commons.dao.dynamic.meta;

import java.sql.Types;

public class IdFieldMeta extends FieldMeta {
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
		return autoIncrement;
	}

	/**
	 * 
	 * @param autoIncrement
	 */
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	@Override
	public boolean isEnabled() {
		if (isAutoIncrement()) {
			return false;
		}
		return super.isEnabled();
	}

	@Override
	public boolean isVisible() {
		return super.isVisible() && !isAutoIncrement();
	}

	public boolean isParticpateInInsert() {
		return isVisible() || !isAutoIncrement();
	}



}
