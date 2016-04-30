//package com.fs.commons.desktop.dynform.ui.masterdetail;
//
//import javax.swing.JComponent;
//
//import com.fs.commons.application.ui.UIOPanelCreationException;
//import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
//import com.fs.commons.dao.dynamic.meta.TableMeta;
//import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
//import com.fs.commons.dao.exception.DaoException;
//import com.fs.commons.desktop.swing.comp.panels.JKPanel;
//
//public class DynEmbededMasterDetail extends AbstractMasterDetail{
//	JKPanel container=new JKPanel();
//	
//	
//	public DynEmbededMasterDetail() {	
//	}
//	
//	public DynEmbededMasterDetail(TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
//		super(tableMeta);
//	}
//
//	@Override
//	protected void initUI() {
//		add(container);
//		
//	}
//
//	@Override
//	protected void addPanelToView(String title, String icon, JKPanel pnl) {
//		container.add(pnl);
//	}
//
//	@Override
//	protected int getCurrentPanelIndex() {
//		return 0;
//	}
//
//	
//
//	@Override
//	protected void navigateToPanelAtIndex(int panelIndex) {		
//	}
//	
//	@Override
//	protected DetailPanel createDetailPanel(ForiegnKeyFieldMeta foriegnKeyFieldMeta) throws DaoException, UIOPanelCreationException {
//		return super.createDetailPanel(foriegnKeyFieldMeta);
//	}
//
//	@Override
//	public void addComponent(JComponent comp) {	
//	}
//
//}
