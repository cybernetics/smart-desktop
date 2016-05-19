// package com.fs.commons.desktop.dynform.ui;
//
// import java.awt.BorderLayout;
// import java.awt.FlowLayout;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import javax.swing.BoxLayout;
// import javax.swing.JDialog;
//
// import com.fs.commons.application.ui.UIOPanelCreationException;
// import
// com.fs.commons.dao.dynamic.constraints.exceptions.DuplicateDataException;
// import com.fs.commons.dao.dynamic.meta.Record;
// import com.fs.commons.dao.dynamic.meta.TableMeta;
// import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
// import com.fs.commons.dao.event.RecordActionAdapter;
// import com.fs.commons.dao.exception.DaoException;
// import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
// import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
// import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
// import com.fs.commons.desktop.swing.SwingUtility;
// import com.fs.commons.desktop.swing.comp.JKButton;
// import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
// import com.fs.commons.desktop.swing.comp.panels.JKPanel;
// import com.fs.commons.desktop.swing.dao.QueryJTable;
// import com.fs.commons.locale.Lables;
// import com.fs.commons.util.ExceptionUtil;
//
/// **
// * TODO : refactor this class , because its duplicate from
// * DynMasterDetailCRUDLPanel ,
// *
// * @author Administrator
// *
// */
// public class DynCRUDL extends JKMainPanel {
// /**
// *
// */
// private static final long serialVersionUID = 1L;
//
// // private static final String CONTROL_A = "control A";
//
// TableMeta tableMeta;
//
// QueryJTable queryTable;
//
// DynDaoPanel pnl;
//
// JKButton btnAdd = new JKButton("ADD_RECORD");
//
// JKButton btnClose = new JKButton("CLOSE");
//
// private String title;
//
// /**
// *
// * @param tableMeta
// * @throws TableMetaNotFoundException
// * @throws DaoException
// * @throws UIOPanelCreationException
// */
// public DynCRUDL(TableMeta tableMeta) throws TableMetaNotFoundException,
// DaoException, UIOPanelCreationException {
// this(new DynDaoPanel(tableMeta));
// }
//
// /**
// *
// * @param dynDaoPanel
// */
// public DynCRUDL(DynDaoPanel dynDaoPanel) {
// this.pnl = dynDaoPanel;
// this.tableMeta = dynDaoPanel.getTableMeta();
// queryTable = new QueryJTable(tableMeta);
// pnl.addDynDaoActionListener(new MasterPanelListener());
// init();
// setTitle();
// checkCountExceeded(false);
// }
//
// /**
// *
// * @param pnlDao
// * @throws DaoException
// * @throws TableMetaNotFoundException
// */
// public DynCRUDL(DynPanel pnlDao) throws TableMetaNotFoundException,
// DaoException {
// this(new DynDaoPanel(pnlDao));
// }
//
// /**
// *
// *
// */
// protected void init() {
// // SwingUtility.setHotKeyFoButton(btnAdd, CONTROL_A, CONTROL_A);
// btnAdd.setShortcut("F1", "F1");
// // btnAdd.setShortcut("Ctrl A", false);
//
// btnAdd.setIcon("db_add.png");
// btnClose.setIcon("fileclose.png");
// setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
// add(getNorthButtonsPanel(), BorderLayout.NORTH);
// add(queryTable, BorderLayout.CENTER);
//
// queryTable.addDaoRecordListener(new RecordActionAdapter() {
// public void recordSelected(String recordId) {
// handleEdit(recordId);
// }
// });
//
// btnAdd.addActionListener(new ActionListener() {
// public void actionPerformed(ActionEvent e) {
// handleAdd();
// }
// });
// btnClose.addActionListener(new ActionListener() {
// public void actionPerformed(ActionEvent e) {
// SwingUtility.closePanel(DynCRUDL.this);
// }
// });
// }
//
// /**
// *
// * @return
// */
// private JKPanel getNorthButtonsPanel() {
// JKPanel pnlButtons = new JKPanel();
// if (tableMeta.isAllowAdd()) {
// pnlButtons.setLayout(new FlowLayout(FlowLayout.LEADING));
// pnlButtons.add(btnAdd);
// }
// return pnlButtons;
// }
//
// /**
// *
// * @param recordId
// */
// protected void handleEdit(String recordId) {
// try {
// pnl.handleFind(recordId, true);
// showDynPanel(true);
// } catch (DaoException e) {
// ExceptionUtil.handle(e);
// }
//
// }
//
// /**
// *
// *
// */
// private void handleClosePanel() {
// showDynPanel(false);
// }
//
// /**
// *
// *
// */
// private void handleAdd() {
// pnl.setMode(DynDaoMode.ADD);
// showDynPanel(true);
// }
//
// /**
// *
// * @param show
// */
// void showDynPanel(boolean show) {
// if (show) {
// SwingUtility.showPanelInDialog(pnl, title);
// } else {
// if (pnl.getRootPane().getParent() instanceof JDialog) {
// ((JDialog) pnl.getRootPane().getParent()).dispose();
// }
// }
// }
//
// /**
// *
// * @param listener
// */
// public void addMasterDaoActionListener(DynDaoActionListener listener) {
// pnl.addDynDaoActionListener(listener);
// }
//
// /**
// *
// * @return
// */
// public Record getRecord() {
// return pnl.getRecord();
// }
//
// /**
// *
// *
// */
// void checkCountExceeded(boolean reloadData) {
// boolean showAdd = !isCountExceeded(reloadData);
// btnAdd.setVisible(showAdd);
// pnl.setAllowAdd(showAdd);
// }
//
// /**
// *
// * @return
// */
// private boolean isCountExceeded(boolean reloadData) {
// if (reloadData) {
// queryTable.reloadData();
// }
// return tableMeta.getMaxRecordsCount() != 0 &&
// queryTable.getTable().getRowCount() >= tableMeta.getMaxRecordsCount();
// }
//
// /**
// * @return the tableMeta
// */
// public TableMeta getTableMeta() {
// return this.tableMeta;
// }
//
// /**
// *
// */
// private void setTitle() {
// if (SwingUtility.isLeftOrientation()) {
// title = Lables.get(this.tableMeta.getCaption()) + " " +
// Lables.get("MANAGEMENT");
// } else {
// title = Lables.get("MANAGEMENT") + " " +
// Lables.get(this.tableMeta.getCaption());
// }
// }
//
// /*
// *
// */
// public QueryJTable getQueryTable() {
// return queryTable;
// }
//
// @Override
// public void resetComponents() throws DaoException {
// pnl.resetComponents();
// }
//
// /**
// *
// * @return
// */
// public DynDaoMode getMode() {
// return pnl.getMode();
// }
//
// /**
// *
// * @return
// */
// public int getIdFieldValueAsInteger() {
// return pnl.getRecord().getIdField().getValueAsInteger();
// }
//
// // ///////////////////////////////////////////////////////////////
// // ///////////////////////////////////////////////////////////////
// // ///////////////////////////////////////////////////////////////
// // ///////////////////////////////////////////////////////////////
// // ///////////////////////////////////////////////////////////////
// // ///////////////////////////////////////////////////////////////
// public class MasterPanelListener extends DynDaoActionAdapter {
//
// @Override
// public void afterAddRecord(Record record) {
// if (isCountExceeded(true)) {
// showDynPanel(false);
// }
// // isCountExceeded method will reload the data , so no need to
// // reload again
// queryTable.setSelectedRowByRecordId(record.getIdValue());
// }
//
// @Override
// public void afterClosePanel() {
// handleClosePanel();
// }
//
// @Override
// public void afterDeleteRecord(Record record) {
// showDynPanel(false);
// // this to make the add visible again if was hidden because if max
// // count exceeded rule
// checkCountExceeded(true);
// }
//
// @Override
// public void onDaoException(Record recod, DaoException ex) {
// // check for record before use , because it maybe null
// if (ex instanceof DuplicateDataException) {
// String recordId = ((DuplicateDataException) ex).getIdValue();
// queryTable.setSelectedRowByRecordId(recordId);
// pnl.requestFocus();
// }
// }
//
// @Override
// public void afterUpdateRecord(Record record) {
// queryTable.reloadData();
// }
// }
//
// /**
// *
// * @param dynDaoActionAdapter
// */
// public void addDynDaoActionListener(DynDaoActionListener listener) {
// pnl.addDynDaoActionListener(listener);
// }
// }
