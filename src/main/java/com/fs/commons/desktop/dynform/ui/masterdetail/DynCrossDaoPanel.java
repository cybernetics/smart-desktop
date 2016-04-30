/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

	private final ArrayList<DynDaoActionListener> listeners = new ArrayList<DynDaoActionListener>();

	/**
	 *
	 * @param metaTable
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 */
	public DynCrossDaoPanel(final TableMeta metaTable) throws DaoException, TableMetaNotFoundException {
		this.metaTable = metaTable;
		this.dao = DaoFactory.createDynamicDao(metaTable);
		this.field1Meta = metaTable.lstForiegnKeyFields().get(0);
		this.field2Meta = metaTable.lstForiegnKeyFields().get(1);

		this.table1Meta = AbstractTableMetaFactory.getTableMeta(metaTable.getDataSource(), this.field1Meta.getReferenceTable());
		this.table2Meta = AbstractTableMetaFactory.getTableMeta(metaTable.getDataSource(), this.field2Meta.getReferenceTable());

		// compMain = (BindingComponent)
		// ComponentFactory.buildForeignKeyComponent(field1Meta);
		this.compMain = new FieldPanelWithFilter(this.field1Meta.getReferenceTable());
		// if(compMain instanceof ManageSupport){
		// ((ManageSupport) compMain).setAllowManage(false);
		// }
		((JComponent) this.compMain).setEnabled(false);
		this.tblAll = new QueryJTable("", metaTable.getDataSource(), "", true);
		this.tblAll.setPagRowsCount(0);
		this.tblAll.setShowFilterButtons(false);
		this.tblAll.setShowSortingPanel(false);
		this.tblAll.setPreferredSize(new Dimension(300, 400));
		this.tblAll.setVisible(metaTable.isAllowAdd() || metaTable.isAllowDelete());

		this.tblRegistredValues = new QueryJTable("", metaTable.getDataSource(), "", true);
		this.tblRegistredValues.setPagRowsCount(0);
		this.tblRegistredValues.setShowFilterButtons(false);
		this.tblRegistredValues.setShowSortingPanel(false);
		this.tblRegistredValues.setPreferredSize(new Dimension(300, 400));
		init();
		setMasterIdValue(null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.fs.commons.desktop.dynform.ui.detail.DetailPanel#
	 * addDynDaoActionListener
	 * (com.fs.commons.desktop.dynform.ui.action.DynDaoPanelActionListener)
	 */
	@Override
	public void addDynDaoActionListener(final DynDaoActionListener listener) {
		this.listeners.add(listener);
	}

	/**
	 *
	 * @param record
	 * @throws DaoException
	 */
	private void callAfterAddEventOnTriggers(final Record record) throws DaoException {
		final ArrayList<Trigger> triggers = this.metaTable.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterAdd(record);
		}
	}

	/**
	 * @param record
	 * @throws DaoException
	 */
	private void callAfterDeleteEventOnTriggers(final Record record) throws DaoException {
		final ArrayList<Trigger> triggers = this.metaTable.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).afterDelete(record);
		}
	}

	/**
	 *
	 * @param record
	 * @throws DaoException
	 */
	private void callBeforeAddEventOnTriggers(final Record record) throws DaoException {
		final ArrayList<Trigger> triggers = this.metaTable.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).beforeAdd(record);
		}
	}

	private void callBeforeDeleteEventOnTriggers(final Record record) throws DaoException {
		final ArrayList<Trigger> triggers = this.metaTable.getTriggers();
		for (int i = 0; i < triggers.size(); i++) {
			triggers.get(i).beforeDelete(record);
		}
	}

	private void enableDisable() {
		this.btnAdd.setEnabled(this.tblAll.getModel().getRowCount() > 0);
		this.btnRemove.setEnabled(this.tblRegistredValues.getModel().getRowCount() > 0);
	}

	/**
	 *
	 * @param record
	 * @throws DaoException
	 */
	void fireAfterAddRecord(final Record record) throws DaoException {
		for (int i = 0; i < this.listeners.size(); i++) {
			final DynDaoActionListener listsner = this.listeners.get(i);
			listsner.afterAddRecord(record);
		}
	}

	void fireAfterDeleteRecord(final Record record) throws DaoException {
		for (int i = 0; i < this.listeners.size(); i++) {
			this.listeners.get(i).afterDeleteRecord(record);
		}
	}

	/**
	 *
	 */
	private void fireAfterResetComponents() {
		for (int i = 0; i < this.listeners.size(); i++) {
			this.listeners.get(i).afterResetComponents();
		}

	}

	/**
	 *
	 * @param record
	 * @throws DaoException
	 */
	void fireBeforeAddRecord(final Record record) throws DaoException {
		for (int i = 0; i < this.listeners.size(); i++) {
			this.listeners.get(i).beforeAddRecord(record);
		}
	}

	void fireBeforeDeleteRecord(final Record record) throws DaoException {
		for (int i = 0; i < this.listeners.size(); i++) {
			this.listeners.get(i).beforeDeleteRecord(record);

		}
	}

	/**
	 * @return
	 */
	private JKPanel getButtonsPanel() {
		final JKPanel pnl = new JKPanel();
		final JKPanel pnlButtons = new JKPanel(new GridLayout(2, 1, 5, 5));
		this.btnAdd.setIcon(SwingUtility.isLeftOrientation() ? "right.png" : "left.png");
		this.btnRemove.setIcon(SwingUtility.isLeftOrientation() ? "left.png" : "right.png");
		pnlButtons.add(this.btnAdd);
		pnlButtons.add(this.btnRemove);
		this.btnAdd.setVisible(this.metaTable.isAllowAdd());
		this.btnRemove.setVisible(this.metaTable.isAllowDelete());
		pnl.add(pnlButtons);
		return pnl;
	}

	/**
	 *
	 */
	protected void handleAdd() {
		try {
			final int ids[] = this.tblAll.getSelectedIdsAsInteger();
			for (final int id : ids) {
				final Record record = this.metaTable.createEmptyRecord();
				record.getField(0).setValue(this.compMain.getValue());
				record.getField(1).setValue(id);
				this.metaTable.validateData(record);
				fireBeforeAddRecord(record);
				callBeforeAddEventOnTriggers(record);
				this.dao.insertRecord(record);
				callAfterAddEventOnTriggers(record);
				fireAfterAddRecord(record);
			}
			reload();
		} catch (final DaoException e) {
			ExceptionUtil.handleException(e);
		} catch (final ValidationException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.fs.commons.desktop.dynform.ui.detail.DetailPanel#handleFind(java.lang
	 * .String)
	 */
	@Override
	public void handleFind(final Object idValud) throws DaoException {
		// TODO Auto-generated method stub
	}

	/**
	 *
	 */
	protected void handleRemove() {
		if (this.tblAll.isVisible()) {
			try {
				final int ids[] = this.tblRegistredValues.getSelectedIdsAsInteger();
				for (final int id : ids) {
					Record record = this.metaTable.createEmptyRecord();
					record.getField(0).setValue(this.compMain.getValue());
					record.getField(1).setValue(id);
					record = this.dao.lstRecords(record).get(0);
					callBeforeDeleteEventOnTriggers(record);
					fireBeforeDeleteRecord(record);
					this.dao.deleteRecord(record);
					callAfterDeleteEventOnTriggers(record);
					fireAfterDeleteRecord(record);
				}
				reload();
			} catch (final DaoException e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	/**
	 *
	 *
	 */
	private void init() {
		setLayout(new BorderLayout());
		final JKPanel pnlNorth = new JKPanel();
		if (this.field1Meta.isVisible()) {
			pnlNorth.add(new JKLabledComponent(this.field1Meta.getName(), this.compMain));
		}

		final JKPanel pnlCenter = new JKPanel();
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.LINE_AXIS));

		final JKPanel pnl = getButtonsPanel();

		if (this.metaTable.isAllowAdd() || this.metaTable.isAllowDelete()) {
			pnlCenter.add(this.tblAll);
			pnlCenter.add(pnl);
		}
		pnlCenter.add(this.tblRegistredValues);
		pnlCenter.setBorder(BorderFactory.createRaisedBevelBorder());

		pnlCenter.setBorder(BorderFactory.createLoweredBevelBorder());
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlCenter, BorderLayout.CENTER);

		this.tblAll.getTable().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					DynCrossDaoPanel.this.btnAdd.doClick();
				}
			}
		});
		this.tblRegistredValues.getTable().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					DynCrossDaoPanel.this.btnRemove.doClick();
				}
			}
		});

		this.btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleAdd();
			}
		});
		this.btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleRemove();

			}
		});
	}

	/**
	 *
	 * o
	 */
	void reload() {
		try {
			final Object value = this.compMain.getValue();
			if (value == null) {
				this.tblAll.setQuery("");
				this.tblRegistredValues.setQuery("");
			} else {
				final int parseInt = value == null ? -1 : Integer.parseInt(value.toString());
				final String notAssignedValuesQuery = this.dao.getNotAssignedValuesQuery(parseInt);
				this.tblAll.setQuery(notAssignedValuesQuery);
				this.tblRegistredValues.setQuery(this.dao.getAssignedValuesQuery(parseInt));
			}
			enableDisable();
		} catch (final TableMetaNotFoundException e) {
			ExceptionUtil.handleException(e);
		}

	}

	@Override
	public void resetComponents() throws DaoException {
		this.compMain.setValue(null);
		this.tblAll.setQuery("");
		this.tblRegistredValues.setQuery("");
		fireAfterResetComponents();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.fs.commons.desktop.dynform.ui.detail.DetailPanel#setMasterIdValue
	 * (java .lang.Object)
	 */
	@Override
	public void setMasterIdValue(final Object object) throws DaoException {
		// System.out.println("Hyane at object : "+object);
		((JComponent) this.compMain).setEnabled(false);

		// if(compMain instanceof DaoComboBox){
		// ((DaoComboBox)compMain).reloadData();
		// }else{
		// if(compMain instanceof DaoComboWithManagePanel){
		// ((DaoComboWithManagePanel)compMain).reloadData();
		// }
		// }
		//
		// the purpose of this reset is to feresh the combo box data
		// compMain.reset();
		this.compMain.setValue(object);
		reload();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.fs.commons.desktop.dynform.ui.detail.DetailPanel#setMode(com.jk.
	 * commons .dao.dynform.ui.DynDaoPanel.DynDaoMode)
	 */
	@Override
	public void setMode(final DynDaoMode mode) {
		// btnAdd.setEnabled(mode == DynDaoMode.VIEW);
		// btnRemove.setEnabled(mode == DynDaoMode.VIEW);
	}

}
