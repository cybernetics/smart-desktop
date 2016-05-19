// package com.fs.commons.desktop.dynform.ui;
//
// import java.awt.BorderLayout;
// import java.awt.FlowLayout;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
//
// import javax.swing.BoxLayout;
// import javax.swing.ImageIcon;
// import javax.swing.JDialog;
//
// import
// com.fs.commons.dao.dynamic.constraints.exceptions.DuplicateDataException;
// import com.fs.commons.dao.dynamic.meta.Record;
// import com.fs.commons.dao.dynamic.meta.TableMeta;
// import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
// import com.fs.commons.dao.event.RecordActionAdapter;
// import com.fs.commons.dao.exception.DaoException;
// import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
// import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
// import com.fs.commons.desktop.swing.SwingUtility;
// import com.fs.commons.desktop.swing.comp.JKButton;
// import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
// import com.fs.commons.desktop.swing.comp.panels.JKPanel;
// import com.fs.commons.desktop.swing.dao.QueryJTable;
// import com.fs.commons.util.ExceptionUtil;
// import com.fs.commons.util.GeneralUtility;
//
/// **
// * @deprecated Any call to this class should replaced to call
// DynMasterDetailLCRUD panel
// * @author jalal
// *
// */
// public class DynDaoLCRUD extends JKMainPanel implements DynDaoActionListener,
// MasterPanel {
//
// /**
// *
// */
// private static final long serialVersionUID = 1L;
//
// TableMeta tableMeta;
//
// QueryJTable queryTable;
//
// DynDaoPanel pnlDao;
//
// JKButton btnAdd = new JKButton("ADD_RECORD");
//
// // boolean singleRecordOnly;
//
// /**
// *
// * @param tableMeta
// * @throws TableMetaNotFoundException
// * @throws DaoException
// */
// public DynDaoLCRUD(TableMeta tableMeta) throws TableMetaNotFoundException,
// DaoException {
// this( new DynDaoPanel(tableMeta));
// }
//
// /**
// *
// * @param dynDaoPanel
// */
// public DynDaoLCRUD(DynDaoPanel dynDaoPanel){
// this.tableMeta=dynDaoPanel.getTableMeta();
// this.pnlDao=dynDaoPanel;
//
// String sql=tableMeta.getReportSql();
// String title=tableMeta.getTableName();
//
// queryTable = new QueryJTable(sql, title, true);
// queryTable.setShowIdColunm(!tableMeta.getIdField().isAutoIncrement());
// queryTable.setShowSortingPanel(true);
// // ArrayList<String> filters = tableMeta.getFilters();
// // for (int i = 0; i < filters.size(); i++) {
// // tbl.showFilterPanel(filters.get(i), true);
// // }
// // pnlDao.setVisible(false);
// pnlDao.addDynDaoActionListener(this);
// pnlDao.setAllowFind(false);
// init();
// reloadTable();
// }
//
//
//
// /**
// *
// *
// */
// private void init() {
// btnAdd.setIcon(new ImageIcon(GeneralUtility.getIconURL("db_add.png")));
// setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
// JKPanel pnlButtons = new JKPanel();
// pnlButtons.setLayout(new FlowLayout(FlowLayout.LEADING));
// pnlButtons.add(btnAdd);
//
// add(pnlButtons, BorderLayout.NORTH);
// add(queryTable, BorderLayout.CENTER);
// // add(pnlDao, BorderLayout.CENTER);
// queryTable.addDaoRecordListener(new RecordActionAdapter() {
// @Override
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
// }
//
// /**
// *
// * @param recordId
// */
// protected void handleEdit(String recordId) {
// try {
// pnlDao.handleFindRecord(recordId);
// showDynPanel(true);
// } catch (DaoException e1) {
// // this never shuold be thrown
// SwingUtility.showDatabaseErrorDialog(e1.getMessage(), e1);
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
// try {
// pnlDao.resetComponents();
// pnlDao.setMode(DynDaoMode.ADD);
// showDynPanel(true);
//
// } catch (DaoException e) {
// ExceptionUtil.handle(e);
// }
// }
//
// /**
// *
// */
// public void afterAddRecord(Record record) throws DaoException {
// // pnlDao.resetComponents();
// // reloadTable();
// if (isCountExceeded()) {
//
// showDynPanel(false);
// }
// queryTable.setSelectedRowByRecordId(record.getIdValue());
// }
//
// /**
// *
// */
// public void afterClosePanel() {
// handleClosePanel();
// reloadTable();
// // btnAdd.setVisible(true);
// }
//
// public void afterDeleteRecord(Record record) throws DaoException {
// showDynPanel(false);
// reloadTable();
//
// }
//
// public void afterUpdateRecord(Record record) throws DaoException {
// reloadTable();
// }
//
// public void beforeAddRecord(Record record) {
// // TODO Auto-generated method stub
//
// }
//
// public void beforeClosePanel() {
// // TODO Auto-generated method stub
//
// }
//
// public void beforeDeleteRecord(Record record) throws DaoException {
// // TODO Auto-generated method stub
//
// }
//
// /**
// *
// */
// public void beforeUpdateRecord(Record record) throws DaoException {
// // TODO Auto-generated method stub
// }
//
// /**
// *
// */
// public void onDaoException(Record recod, DaoException ex) {
// // check for record before use , becuas it maybe null
// if (ex instanceof DuplicateDataException) {
// String recordId = ((DuplicateDataException) ex).getIdValue();
// queryTable.setSelectedRowByRecordId(recordId);
// pnlDao.requestFocus();
// }
// }
//
// /**
// *
// * @param show
// */
// void showDynPanel(boolean show) {
// if (show) {
// pnlDao.setAllowClear(!isCountExceeded());
// pnlDao.setVisible(true);
// SwingUtility.showPanelInDialog(pnlDao, tableMeta.getTableName());
// pnlDao.requestFocus();
//
// } else {
// if (pnlDao.getRootPane().getParent() instanceof JDialog) {
// ((JDialog) pnlDao.getRootPane().getParent()).dispose();
// }
// }
// }
//
// public void addDynDaoActionListener(DynDaoActionListener listener) {
// pnlDao.addDynDaoActionListener(listener);
// }
//
// public void onRecordFound(Record record) {
// // TODO Auto-generated method stub
//
// }
//
// public void onRecordNotFound(Object recordId, DaoException e) {
// // TODO Auto-generated method stub
//
// }
//
// public void afterResetComponents() {
// // TODO Auto-generated method stub
//
// }
//
// public void beforeResetComponents(Record record) {
// // TODO Auto-generated method stub
//
// }
//
// /**
// *
// * @return
// */
// public DynDaoPanel getPnlDao() {
// return pnlDao;
// }
//
// /**
// *
// * @param pnlDao
// */
// public void setPnlDao(DynDaoPanel pnlDao) {
// this.pnlDao = pnlDao;
// }
//
// public void handleFindRecord(Object id) throws DaoException {
// pnlDao.handleFindRecord(id.toString());
//
// }
//
// public void addDynDaoPanelActionListener(DynDaoActionListener listener) {
// pnlDao.addDynDaoActionListener(listener);
// }
//
// public DynDaoMode getMode() {
// return pnlDao.getMode();
// }
//
// public Record getRecord() {
// return pnlDao.getRecord();
// }
//
// /**
// *
// *
// */
// void reloadTable() {
// boolean showAdd = !isCountExceeded();
// btnAdd.setVisible(showAdd);
// }
//
// private boolean isCountExceeded() {
// queryTable.reloadData();
// return tableMeta.getMaxRecordsCount() != 0 &&
// queryTable.getTable().getRowCount() >= tableMeta.getMaxRecordsCount();
// }
//
// public QueryJTable getQueryTable() {
// return queryTable;
// }
//
// public void setQueryTable(QueryJTable queryTable) {
// this.queryTable = queryTable;
// }
//
// public void afterSetMode(DynDaoMode mode) {
// // TODO Auto-generated method stub
//
// }
//
// public void beforeSetMode(DynDaoMode mode) {
// // TODO Auto-generated method stub
//
// }
// }
