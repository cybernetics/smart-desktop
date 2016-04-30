package com.fs.commons.desktop.dynform.ui.masterdetail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.dynamic.trigger.Trigger;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.FieldPanelWithFilter;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.util.ExceptionUtil;

public class DynCrossDaoPanel extends JKMainPanel implements DetailPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final TableMeta metaTable;

	private final ForiegnKeyFieldMeta field2Meta;

	private final ForiegnKeyFieldMeta field1Meta;

	BindingComponent compMain;

	private final TableMeta table1Meta;

	private final TableMeta table2Meta;

	private final DynamicDao dao;

	private final QueryJTable tblAll;

	private final QueryJTable tblRegistredValues;

	JKButton btnAdd = new JKButton("ADD");

	JKButton btnRemove = new JKButton("REMOVE");

	private ArrayList<DynDaoActionListener> listeners = new ArrayList<DynDaoActionListener>();

	/**
	 * 
	 * @param metaTable
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 */
	public DynCrossDaoPanel(TableMeta metaTable) throws DaoException, TableMetaNotFoundException {
		this.metaTable = metaTable;
		dao = DaoFactory.createDynamicDao(metaTable);
		field1Meta = metaTable.lstForiegnKeyFields().get(0);
		field2Meta = metaTable.lstForiegnKeyFields().get(1);

		table1Meta = AbstractTableMetaFactory.getTableMeta(metaTable.getDataSource(), field1Meta.getReferenceTable());
		table2Meta = AbstractTableMetaFactory.getTableMeta(metaTable.getDataSource(),field2Meta.getReferenceTable());
		
		//compMain = (BindingComponent) ComponentFactory.buildForeignKeyComponent(field1Meta);
		compMain=new FieldPanelWithFilter(field1Meta.getReferenceTable());
//		if(compMain instanceof ManageSupport){
//			((ManageSupport) compMain).setAllowManage(false);
//		}
		((JComponent) compMain).setEnabled(false);
		tblAll = new QueryJTable("", metaTable.getDataSource(), "", true);
		tblAll.setPagRowsCount(0);
		tblAll.setShowFilterButtons(false);
		tblAll.setShowSortingPanel(false);
		tblAll.setPreferredSize(new Dimension(300, 400));
		tblAll.setVisible(metaTable.isAllowAdd() || metaTable.isAllowDelete());

		tblRegistredValues = new QueryJTable("", metaTable.getDataSource(), "", true);
		tblRegistredValues.setPagRowsCount(0);
		tblRegistredValues.setShowFilterButtons(false);
		tblRegistredValues.setShowSortingPanel(false);
		tblRegistredValues.setPreferredSize(new Dimension(300, 400));
		init();
		setMasterIdValue(null);
	}

	/**
	 * 
	 * 
	 */
	private void init() {
		setLayout(new BorderLayout());
		JKPanel pnlNorth = new JKPanel();
		if (field1Meta.isVisible()) {
			pnlNorth.add(new JKLabledComponent(field1Meta.getName(), compMain));
		}

		JKPanel pnlCenter = new JKPanel();
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.LINE_AXIS));

		JKPanel pnl = getButtonsPanel();

		if (metaTable.isAllowAdd() || metaTable.isAllowDelete()) {
			pnlCenter.add(tblAll);
			pnlCenter.add(pnl);
		}
		pnlCenter.add(tblRegistredValues);
		pnlCenter.setBorder(BorderFactory.createRaisedBevelBorder());

		pnlCenter.setBorder(BorderFactory.createLoweredBevelBorder());
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlCenter, BorderLayout.CENTER);

		tblAll.getTable().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					btnAdd.doClick();
				}
			}
		});
		tblRegistredValues.getTable().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					btnRemove.doClick();
				}
			}
		});

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAdd();
			}
		});
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleRemove();

			}
		});
	}

	/**
	 * @return
	 */
	private JKPanel getButtonsPanel() {
		JKPanel pnl = new JKPanel();
		JKPanel pnlButtons = new JKPanel(new GridLayout(2, 1, 5, 5));
		btnAdd.setIcon(SwingUtility.isLeftOrientation() ? "right.png" : "left.png");
		btnRemove.setIcon(SwingUtility.isLeftOrientation() ? "left.png" : "right.png");
		pnlButtons.add(btnAdd);
		pnlButtons.add(btnRemove);
		btnAdd.setVisible(metaTable.isAllowAdd());
		btnRemove.setVisible(metaTable.isAllowDelete());
		pnl.add(pnlButtons);
		return pnl;
	}

	/**
	 * 
	 */
	protected void handleAdd() {
		try {
			int ids[] = tblAll.getSelectedIdsAsInteger();
			for (int i = 0; i < ids.length; i++) {
				Record record = metaTable.createEmptyRecord();
				record.getField(0).setValue(compMain.getValue());
				record.getField(1).setValue(ids[i]);
				metaTable.validateData(record);
				fireBeforeAddRecord(record);
				callBeforeAddEventOnTriggers(record);
				dao.insertRecord(record);
				callAfterAddEventOnTriggers(record);
				fireAfterAddRecord(record);
			}
			reload();
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		} catch (ValidationException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 * @param record
	 * @throws DaoException
	 */
	private void callBeforeAddEventOnTriggers(Record record) throws DaoException {
		ArrayList<Trigger> triggers = metaTable.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).beforeAdd(record);
		}
	}

	/**
	 * 
	 * @param record
	 * @throws DaoException
	 */
	private void callAfterAddEventOnTriggers(Record record) throws DaoException {
		ArrayList<Trigger> triggers = metaTable.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterAdd(record);
		}
	}

	/**
	 * 
	 */
	protected void handleRemove() {
		if (tblAll.isVisible()) {
			try {
				int ids[] = tblRegistredValues.getSelectedIdsAsInteger();
				for (int i = 0; i < ids.length; i++) {
					Record record = metaTable.createEmptyRecord();
					record.getField(0).setValue(compMain.getValue());
					record.getField(1).setValue(ids[i]);
					record = dao.lstRecords(record).get(0);
					callBeforeDeleteEventOnTriggers(record);
					fireBeforeDeleteRecord(record);
					dao.deleteRecord(record);
					callAfterDeleteEventOnTriggers(record);
					fireAfterDeleteRecord(record);
				}
				reload();
			} catch (DaoException e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	/**
	 * @param record
	 * @throws DaoException
	 */
	private void callAfterDeleteEventOnTriggers(Record record) throws DaoException {
		ArrayList<Trigger> triggers = metaTable.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterDelete(record);
		}
	}

	private void callBeforeDeleteEventOnTriggers(Record record) throws DaoException {
		ArrayList<Trigger> triggers = metaTable.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).beforeDelete(record);
		}
	}

	/**
	 * 
	 * o
	 */
	void reload() {
		try {
			Object value = compMain.getValue();
			if (value == null) {
				tblAll.setQuery("");
				tblRegistredValues.setQuery("");
			} else {
				int parseInt = value == null ? -1 : Integer.parseInt(value.toString());
				String notAssignedValuesQuery = dao.getNotAssignedValuesQuery(parseInt);
				tblAll.setQuery(notAssignedValuesQuery);
				tblRegistredValues.setQuery(dao.getAssignedValuesQuery(parseInt));
			}
			enableDisable();
		} catch (TableMetaNotFoundException e) {
			ExceptionUtil.handleException(e);
		}

	}

	private void enableDisable() {
		btnAdd.setEnabled(tblAll.getModel().getRowCount() > 0);
		btnRemove.setEnabled(tblRegistredValues.getModel().getRowCount() > 0);
	}

	@Override
	public void resetComponents() throws DaoException {
		compMain.setValue(null);
		tblAll.setQuery("");
		tblRegistredValues.setQuery("");
		fireAfterResetComponents();
	}

	/**
	 * 
	 */
	private void fireAfterResetComponents() {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).afterResetComponents();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fs.commons.desktop.dynform.ui.detail.DetailPanel#addDynDaoActionListener
	 * (com.fs.commons.desktop.dynform.ui.action.DynDaoPanelActionListener)
	 */
	public void addDynDaoActionListener(DynDaoActionListener listener) {
		this.listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fs.commons.desktop.dynform.ui.detail.DetailPanel#setMasterIdValue
	 * (java .lang.Object)
	 */
	public void setMasterIdValue(Object object) throws DaoException {
//		System.out.println("Hyane at object : "+object);
		((JComponent) compMain).setEnabled(false);

		// if(compMain instanceof DaoComboBox){
		// ((DaoComboBox)compMain).reloadData();
		// }else{
		// if(compMain instanceof DaoComboWithManagePanel){
		// ((DaoComboWithManagePanel)compMain).reloadData();
		// }
		// }
		//		
		// the purpose of this reset is to feresh the combo box data
	//	compMain.reset();
		compMain.setValue(object);
		reload();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fs.commons.desktop.dynform.ui.detail.DetailPanel#handleFind(java.lang
	 * .String)
	 */
	public void handleFind(Object idValud) throws DaoException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fs.commons.desktop.dynform.ui.detail.DetailPanel#setMode(com.jk.commons
	 * .dao.dynform.ui.DynDaoPanel.DynDaoMode)
	 */
	public void setMode(DynDaoMode mode) {
		// btnAdd.setEnabled(mode == DynDaoMode.VIEW);
		// btnRemove.setEnabled(mode == DynDaoMode.VIEW);
	}

	/**
	 * 
	 * @param record
	 * @throws DaoException 
	 */
	void fireBeforeAddRecord(Record record) throws DaoException {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).beforeAddRecord(record);
		}
	}

	void fireBeforeDeleteRecord(Record record) throws DaoException {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).beforeDeleteRecord(record);

		}
	}

	/**
	 * 
	 * @param record
	 * @throws DaoException
	 */
	void fireAfterAddRecord(Record record) throws DaoException {
		for (int i = 0; i < listeners.size(); i++) {
			DynDaoActionListener listsner = listeners.get(i);
			listsner.afterAddRecord(record);
		}
	}

	void fireAfterDeleteRecord(Record record) throws DaoException {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).afterDeleteRecord(record);
		}
	}

}
