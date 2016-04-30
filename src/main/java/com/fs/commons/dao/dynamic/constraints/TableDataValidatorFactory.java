package com.fs.commons.dao.dynamic.constraints;

public class TableDataValidatorFactory {
	
	/**
	 * 
	 * @return
	 */
	public static TableDataValidator createValidator(){
		return new DefaultTableDataValidator();
	}
}
