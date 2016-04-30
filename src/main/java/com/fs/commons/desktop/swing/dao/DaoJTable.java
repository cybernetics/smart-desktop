// package com.fs.commons.desktop.swing.dao;
//
// import java.awt.BorderLayout;
// import java.awt.Dialog;
// import java.awt.Dimension;
// import java.awt.FlowLayout;
// import java.awt.GridLayout;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
//
// import javax.swing.BorderFactory;
// import javax.swing.ImageIcon;
// import javax.swing.JButton;
//
// import com.fs.commons.dao.event.RecordActionAdapter;
// import com.fs.commons.dao.exception.DaoException;
// import com.fs.commons.desktop.swing.PrintUtil;
// import com.fs.commons.desktop.swing.SwingUtility;
// import com.fs.commons.desktop.swing.comp.JKButton;
// import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
// import com.fs.commons.desktop.swing.comp.panels.JKPanel;
// import com.fs.commons.desktop.swing.dialogs.AddDataDialog;
// import com.fs.commons.desktop.swing.dialogs.EditDataDialog;
// import com.fs.commons.util.GeneralUtility;
//
/// **
// * @deprecated
// */
// public class DaoJTable extends JKMainPanel implements FilterListener {
// /**
// *
// */
// private static final long serialVersionUID = 1L;
//
// static Dimension size = new Dimension(790, 500);
//
// private QueryTableModel model;
//
// JButton btnAdd = new JKButton("ADD_RECORD","F1");
//
// JButton btnPrint = new JKButton("PRINT");
//
// JButton btnClose = new JKButton("CLOSE");
//
// Dialog parent;
//
// private DataPanel dataPanel;
//
// private boolean autoIncrement;
//
// // TableFilterPanel[] filters;
//
// private QueryJTable table;
//
// public DaoJTable(DataPanel dataPanel ,String sql,boolean showIdColunm,boolean
// autoIncrement) {
// this(new QueryTableModel(sql,showIdColunm),dataPanel,autoIncrement);
//
// }
// /**
// *
// * @param parent
// * @param model
// * @param dataPanel
// * @param autoIncrement
// */
// public DaoJTable(QueryTableModel model, DataPanel dataPanel, boolean
// autoIncrement) {
// this(null,model,dataPanel,autoIncrement);
// }
//
// /**
// *
// * @param parent
// * Dialog
// * @param model
// * QueryTableModel
// * @param dataPanel
// * DataPanel
// * @param autoIncrement
// * boolean
// */
// public DaoJTable(Dialog parent, QueryTableModel model, DataPanel dataPanel,
// boolean autoIncrement) {
// this.parent = parent;
// this.dataPanel = dataPanel;
// this.autoIncrement = autoIncrement;
// this.model = model;
// // buildFilterPanels();
// table = new QueryJTable(model, "", true);
// init();
// // reloadData();
// }
//
// /**
// *
// * @param sql
// * @param pnlEmployee
// * @param b
// */
// public DaoJTable(String sql, DataPanel dataPanel, boolean autoIncrement) {
// this(new QueryTableModel(sql),dataPanel,autoIncrement);
// }
// /**
// * init
// */
// private void init() {
// setLayout(new BorderLayout());
// add(createTablePanel(), BorderLayout.CENTER);
// add(createNorthButtonsPanel(), BorderLayout.NORTH);
//
// btnAdd.addActionListener(new ActionListener() {
// public void actionPerformed(ActionEvent e) {
// handleAdd();
// }
// });
// btnPrint.addActionListener(new ActionListener() {
// public void actionPerformed(ActionEvent e) {
// handlePrint();
// }
// });
// btnClose.addActionListener(new ActionListener() {
// public void actionPerformed(ActionEvent e) {
// if(parent!=null){
// parent.dispose();
// }else{
// SwingUtility.closePanel(DaoJTable.this);
// }
// }
// });
// table.addDaoRecordListener(new RecordActionAdapter() {
// public void recordSelected(String recordId) {
// handleEditRow();
// }
// });
//
// // table.getTable().addKeyListener(new KeyAdapter() {
// // public void keyPressed(KeyEvent e) {
// // if (e.getKeyChar() == e.VK_ENTER) {
// // handleEditRow();
// // }
// // }
// // });
// // JTableHeader header = table.getTable().getTableHeader();
// // // header.setReorderingAllowed(false);
// // header.addMouseListener(new MouseAdapter() {
// // public void mouseClicked(MouseEvent e) {
// // int colIndex =
// // table.getTable().getTableHeader().columnAtPoint(e.getPoint());
// // if (e.getButton() == e.BUTTON1) {
// // if (e.getClickCount() == 2) {
// // showFilterPanel(colIndex);
// // }
// // }
// // };
// // });
// //
// }
//
// /**
// *
// * @return JKPanel
// */
// private JKPanel createTablePanel() {
// JKPanel tablePanel = new JKPanel(new BorderLayout());
// tablePanel.setBorder(BorderFactory.createEtchedBorder());
// tablePanel.add(table, BorderLayout.CENTER);
// return tablePanel;
// }
//
// /**
// *
// * @return JKPanel
// */
// private JKPanel createNorthButtonsPanel() {
// JKPanel panel = new JKPanel(new GridLayout(1, 2));
// JKPanel pnl = new JKPanel(new FlowLayout(SwingUtility.isLeftOrientation()?
// FlowLayout.LEFT:FlowLayout.RIGHT));
// pnl.setOpaque(false);
//
// pnl.add(btnAdd);
// btnAdd.setIcon(new ImageIcon(GeneralUtility.getIconURL("db_add.png")));
// pnl.add(btnPrint);
// btnPrint.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileprint.png")));
//
// panel.add(pnl);
//
// JKPanel panel2 = new JKPanel(new FlowLayout(SwingUtility.isLeftOrientation()?
// FlowLayout.RIGHT:FlowLayout.LEFT));
// panel2.setOpaque(false);
//// panel2.add(btnClose);
// btnClose.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileclose.png")));
//
// panel.add(panel2);
// return panel;
// }
//
// /**
// *
// */
// private void handleEditRow() {
// int row = table.getTable().getSelectedRow();
// String id = (String) model.getRecordId(row);
// try {
// if (EditDataDialog.showEditDialog(null, dataPanel, id)) {
// reloadData();
// if (row < table.getTable().getRowCount()) {
// table.getTable().setRowSelectionInterval(row, row);
// }
// }
// } catch (DaoException e) {
// SwingUtility.showDatabaseErrorDialog(e.getMessage(), e);
// }
// }
//
// /**
// *
// */
// private void reloadData() {
// table.reloadData();
// // model.loadData();
// // calcColunmWidthes();
// }
//
// /**
// *
// * @param colunmIndex
// * int
// */
// // void showFilterPanel(int colunmIndex) {
// // filters[colunmIndex].setVisible(true);
// // }
// /**
// *
// */
// // private int calcColunmWidthes() {
// // /** @todo cache colunm widthes */
// // int sum=0;
// // table.getTable().setAutoResizeMode(table.getTable().AUTO_RESIZE_OFF);
// // for (int i = 0; i <
// // table.getTable().getTableHeader().getColumnModel().getColumnCount(); i++)
// // {
// // sum+=model.getColunmWidth(i) * 5;
// //
// table.getTable().getTableHeader().getColumnModel().getColumn(i).setPreferredWidth(model.getColunmWidth(i)
// // * 3);
// // }
// // return sum;
// // }
// /**
// *
// * @param panel
// * TableFilterPanel
// */
// public void filterUpdated(TableFilterPanel panel) {
// model.setExtraSQLCondition(new Integer(panel.getFilterColunmIndex()),
// panel.getConditionString());
// reloadData();
// }
//
// /**
// *
// */
// void handlePrint() {
// // String title=SwingUtil.showInputDialog("ENTER_REPORT_TITLE");
// PrintUtil.printQueryModel(table.getModel(), "");
// }
//
// /**
// * getLayout
// *
// * @param string
// * String
// * @return String
// */
// private String getLayout(String string) {
// return "";
// }
//
// public void showFilterPanel(int colunmIndex) {
// table.showFilterPanel(colunmIndex);
// }
//
// public void requestFocus() {
// table.requestFocus();
// }
//
// public QueryJTable getQueryTable() {
// return table;
// }
//
// private Dialog getParentWindow() {
// return parent==null?SwingUtility.getEmptyDialog():parent;
// }
//
// private void handleAdd() {
// try {
// String newId=AddDataDialog.showAddDialog(getParentWindow(), dataPanel,
// autoIncrement);
// reloadData();
// if(newId!=null){
// table.setSelectedRowByRecordId(new Integer(newId));
// }
// } catch (DaoException e1) {
// SwingUtility.showDatabaseErrorDialog(e1.getMessage(), e1);
// }
// }
//
// }
