package com.fs.commons.dao.sql.query;

import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;

public class LeftJoin implements Join {
	ForiegnKeyFieldMeta field;

	public LeftJoin(ForiegnKeyFieldMeta field) {
		this.field = field;
	}

	@Override
	public Object toQueryElement() {
		return field.getLeftJoinStatement();
	}

	@Override
	public boolean isInline() {
		return true;
	}

}
