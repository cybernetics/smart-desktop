/**
 * 
 */
package com.fs.commons.desktop.dynform.ui.masterdetail;

import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;

/**
 * @author u087
 * 
 */
public interface DetailPanel {
	//find by primary key
	public void handleFind(Object idValud) throws DaoException;
	//find by the foreign key: which could be ONE_TO_ONE , ONE_TO_MANY
	public void setMasterIdValue(Object object) throws DaoException;

	public void addDynDaoActionListener(DynDaoActionListener listener);

	public void setMode(DynDaoMode mode);

	public void resetComponents() throws DaoException;
	
	public String getName();
	public void setName(String name);

}
