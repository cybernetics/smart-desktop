/**
 *
 */
package com.fs.commons.desktop.dynform.ui.masterdetail;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.DetailOneToManyPanel;

/**
 * @author u087
 * 
 */
public class DetailPanelFactory {
	
	/**
	 * 
	 * @param foriegnKeyFieldMeta
	 * @return
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 * @throws UIOPanelCreationException 
	 */
	public static DetailPanel createDetailPanel(ForiegnKeyFieldMeta foriegnKeyFieldMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		DetailPanel pnl;
		String panelClassName= foriegnKeyFieldMeta.getParentTable().getPanelClassName();
		if(panelClassName!=null && !panelClassName.equals("")){
			return createDynamicPanel(panelClassName);
		}
		if (foriegnKeyFieldMeta.getParentTable().isCrossTable()) {
			pnl = new DynCrossDaoPanel(foriegnKeyFieldMeta.getParentTable());
		} else if (foriegnKeyFieldMeta.getRelation() == ForiegnKeyFieldMeta.Relation.ONE_TO_ONE) {
			pnl = new DetailOneToOnePanel(foriegnKeyFieldMeta);
		} else {
			pnl = new DetailOneToManyPanel(foriegnKeyFieldMeta);
		}
		return pnl;
	}

	/**
	 * 
	 * @param panelClassName
	 * @return
	 * @throws UIOPanelCreationException 
	 */
	private static DetailPanel createDynamicPanel(String panelClassName) throws UIOPanelCreationException {
		try {
			return (DetailPanel) Class.forName(panelClassName).newInstance();
		} catch (Exception e) {
			throw new UIOPanelCreationException(e);
		}
	}
}
