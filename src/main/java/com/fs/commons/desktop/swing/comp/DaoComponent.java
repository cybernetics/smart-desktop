package com.fs.commons.desktop.swing.comp;

import com.fs.commons.dao.connection.DataSource;

public interface DaoComponent {
	public void setDataSource(DataSource manager);
	public DataSource getDataSource();
}
