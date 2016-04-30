package com.fs.commons.desktop.dynform.ui;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.masterdetail.AbstractMasterDetail;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailPanel;

public class MasterPanelFactory {
	/**
	 * 
	 * @param meta
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 * @throws UIOPanelCreationException 
	 */
	public static AbstractMasterDetail createMasterPanel(TableMeta meta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		if(meta.getPanelClassName() !=null && !meta.getPanelClassName().equals("") ){
			 Object panel = createDynamicPanel(meta.getPanelClassName());
			 if(panel instanceof AbstractMasterDetail){
				 return (AbstractMasterDetail) panel;
			 }
		}
		DynMasterDetailPanel masterDetailPanel = new DynMasterDetailPanel(meta);
		masterDetailPanel.init();
		return masterDetailPanel;
	}
	
	/**
	 * 
	 * @param panelClassName
	 * @return
	 * @throws UIOPanelCreationException 
	 */
	private static Object createDynamicPanel(String panelClassName) throws UIOPanelCreationException {
		try {
			return  Class.forName(panelClassName).newInstance();
		} catch (Exception e) {
			throw new UIOPanelCreationException(e);
		}
	}
}
