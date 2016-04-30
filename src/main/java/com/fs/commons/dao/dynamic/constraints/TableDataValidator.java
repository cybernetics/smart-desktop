package com.fs.commons.dao.dynamic.constraints;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;

public interface TableDataValidator {
	public void validate(TableMeta table,Record record)throws ValidationException;
}
