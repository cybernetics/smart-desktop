package com.fs.commons.desktop.swing.comp2;

import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.dao.DaoComboBox;

public class FSDataComboBox extends DaoComboBox {

	public FSDataComboBox() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FSDataComboBox(ForiegnKeyFieldMeta meta) {
		super(meta);
		// TODO Auto-generated constructor stub
	}

	public FSDataComboBox(String sql, boolean transferFocusOnEnter) {
		super(sql, transferFocusOnEnter);
		// TODO Auto-generated constructor stub
	}

	public FSDataComboBox(String sql, int defauleSelectedIndex)  {
		super(sql, defauleSelectedIndex);
		// TODO Auto-generated constructor stub
	}

	public FSDataComboBox(String sql)  {
		super(sql);
		// TODO Auto-generated constructor stub
	}

	public FSDataComboBox(TableMeta tableMeta)  {
		super(tableMeta);
		// TODO Auto-generated constructor stub
	}

	



}
