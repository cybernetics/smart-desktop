// package com.fs.commons.desktop.dynform.ui.masterdetail;
//
// import java.awt.BorderLayout;
// import java.util.ArrayList;
//
// import javax.swing.BoxLayout;
//
// import com.fs.commons.application.ui.UIOPanelCreationException;
// import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
// import com.fs.commons.dao.dynamic.meta.Record;
// import com.fs.commons.dao.dynamic.meta.TableMeta;
// import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
// import com.fs.commons.dao.exception.DaoException;
// import com.fs.commons.desktop.dynform.ui.DynDaoPanel;
// import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
// import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
// import com.fs.commons.desktop.swing.comp.panels.JKPanel;
// import com.fs.commons.util.ExceptionUtil;
//
// public class DynCompoundMasterDetail extends JKMainPanel{
// private DynDaoPanel pnlMaster;
// ArrayList<DetailOneToOnePanel> detailPanels=new
// ArrayList<DetailOneToOnePanel>();
//
// /**
// * @param masterMeta
// * @throws TableMetaNotFoundException
// * @throws DaoException
// * @throws UIOPanelCreationException
// */
// public DynCompoundMasterDetail(TableMeta masterMeta) throws
// TableMetaNotFoundException, DaoException, UIOPanelCreationException {
// pnlMaster = new DynDaoPanel(masterMeta);
// ArrayList<ForiegnKeyFieldMeta> detailFields = masterMeta.getDetailFields();
// for (ForiegnKeyFieldMeta foriegnKeyFieldMeta : detailFields) {
// DetailOneToOnePanel pnl=new DetailOneToOnePanel(foriegnKeyFieldMeta);
// pnl.setShowButtons(false);
// detailPanels.add(pnl);
// }
// init();
// }
//
// private void init() {
// setLayout(new BorderLayout());
// JKPanel pnlDetailsContainer = pnlMaster.getEmptyPanel();
// pnlDetailsContainer.setLayout(new BoxLayout(pnlDetailsContainer,
// BoxLayout.Y_AXIS));
// for(DetailOneToOnePanel detail:detailPanels){
// pnlDetailsContainer.add(detail);
// }
// add(pnlMaster);
// pnlMaster.addDynDaoActionListener(new MasterDynActionListener());
// }
//
//
// // //////////////////////////////////////////////////////////////////////////
// class MasterDynActionListener extends DynDaoActionAdapter {
//
// @Override
// public void afterAddRecord(Record record) {
// try {
// pnlMaster.handleFindRecord(record.getIdValue());
// } catch (DaoException e) {
// ExceptionUtil.handleException(e);
// }
// }
//
// @Override
// public void afterResetComponents() {
// resetDetailPanels();
// }
//
// @Override
// public void onRecordFound(final Record masterRecord) {
// handleFindInDetail(masterRecord);
// }
//
// @Override
// public void onRecordNotFound(Object recordId, DaoException e) {
// resetDetailPanels();
// }
//
// //
// ///////////////////////////////////////////////////////////////////////////////
// private void handleFindInDetail(Record masterRecord) {
// try {
// for (DetailOneToOnePanel detail: detailPanels) {
// detail.setMasterIdValue(masterRecord.getIdValue());
// }
// } catch (DaoException e) {
// ExceptionUtil.handleException(e);
// }
// }
//
// //
// ///////////////////////////////////////////////////////////////////////////////
// private void resetDetailPanels() {
// for (int i = 0; i < detailPanels.size(); i++) {
// DetailPanel pnl = (DetailPanel) detailPanels.get(i);
// try {
// pnl.setMasterIdValue(null);
// } catch (DaoException e) {
// ExceptionUtil.handleException(e);
// }
// }
// }
// }
//
// }
