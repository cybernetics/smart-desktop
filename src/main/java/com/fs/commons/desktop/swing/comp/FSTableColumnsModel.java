package com.fs.commons.desktop.swing.comp;

import java.util.Vector;

import com.fs.commons.desktop.swing.comp.model.FSTableColumn;

public interface FSTableColumnsModel {
	public Vector<FSTableColumn> getFsTableColunms() ;
	public Vector<FSTableColumn> getVisibleColumns();
	public FSTableColumn getVisibleTableColumn(int column);
}
