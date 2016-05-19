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
package com.fs.commons.desktop.dynform.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailCRUDLPanel;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.DaoComponent;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dialogs.QueryDialog;
import com.jk.exceptions.handler.ExceptionUtil;
import com.jk.logging.JKLogger;

/**
 * @author u087
 */
public class FieldPanelWithFilter extends JKPanel<Object> implements DaoComponent, ManageSupport {
	private static final long serialVersionUID = 1L;
	Record record;
	JKTextField txtView = new JKTextField(20);
	JKButton btnSelect = new JKButton("SELECT", "F12");
	JKButton btnAdd = new JKButton("ADD");
	private TableMeta tableMeta;
	private Object defaultValue;
	private JKDataSource manager;
	private boolean allowManage;

	public FieldPanelWithFilter() {
	}

	// ///////////////////////////////////////////////////////////////
	public FieldPanelWithFilter(final String tableMeta) {
		this(AbstractTableMetaFactory.getTableMeta(tableMeta));
	}

	// ///////////////////////////////////////////////////////////////
	public FieldPanelWithFilter(final TableMeta tableMeta) {
		this.tableMeta = tableMeta;
		init();
		setAllowManage(false);
	}

	/**
	 *
	 * @throws ValidationException
	 */
	public void checkEmpty() throws ValidationException {
		SwingValidator.checkEmpty(this);
	}

	@Override
	public void clear() {
		this.record = null;
		this.txtView.clear();
	}

	@Override
	public JKDataSource getDataSource() {
		return this.manager;
	}

	// ///////////////////////////////////////////////////////////////
	@Override
	public Object getValue() {
		if (this.record != null) {
			return this.record.getIdValue();
		}
		return null;
	}

	public int getValueAsInteger() {
		if (this.record == null) {
			return -1;
		}
		return this.record.getIdValueAsInteger();
	}

	// //////////////////////////////////////////////////////////////////////
	protected void handleEdit() {
		try {
			final DynMasterDetailCRUDLPanel pnl = new DynMasterDetailCRUDLPanel(this.tableMeta);
			pnl.addMasterDaoActionListener(new DynDaoActionAdapter() {
				@Override
				public void afterAddRecord(final Record record) throws JKDataAccessException {
					setValue(record.getIdValue());
				}
			});
			SwingUtility.showPanelInDialog(pnl, this.tableMeta.getTableName());
		} catch (final Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	// //////////////////////////////////////////////////////////////////////
	private void handleSelect() {
		final Object id = QueryDialog.showQueryDialog(this.tableMeta);
		if (id != null) {
			setValue(id);
		}
		SwingUtility.pressKey(KeyEvent.VK_TAB);
	}

	// ///////////////////////////////////////////////////////////////
	private void init() {
		setLayout(new BorderLayout());
		add(this.txtView, BorderLayout.CENTER);

		final JKPanel pnlButtons = new JKPanel();
		pnlButtons.add(this.btnAdd);
		pnlButtons.add(this.btnSelect);
		add(pnlButtons, BorderLayout.LINE_END);

		this.txtView.setEditable(false);
		this.btnSelect.setIcon("find_commons_mod_icon.gif");
		// btnSelect.setShortcut("F12", "f12");
		this.btnAdd.setIcon("edit.png");
		this.btnSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				handleSelect();
			}
		});
		this.btnSelect.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					FieldPanelWithFilter.this.btnSelect.transferFocus();
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					handleSelect();
				}
			}

		});
		this.txtView.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				// if(e.getKeyCode()==e.VK_ENTER){
				// btnSelect.doClick();
				// }
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					setValue(null);
				}
			}
		});
		this.btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleEdit();
			}
		});

	}

	@Override
	public boolean isAllowManage() {
		this.btnAdd.setVisible(this.allowManage);
		return this.allowManage;
	}

	@Override
	public void requestFocus() {
		if (this.btnSelect.isVisible() && this.btnSelect.isEnabled()) {
			this.btnSelect.requestFocus();
			// btnSelect.doClick();
		} else {
			super.requestFocus();
		}
	}

	@Override
	public void reset() {
		setValue(this.defaultValue);
	}

	// ///////////////////////////////////////////////////////////////
	@Override
	public void setAllowManage(final boolean allowManage) {
		this.allowManage = allowManage;
		this.btnAdd.setVisible(allowManage);
	}

	@Override
	public void setDataSource(final JKDataSource manager) {
		this.manager = manager;
	}

	// ///////////////////////////////////////////////////////////////
	@Override
	public void setDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		this.btnSelect.setVisible(enabled);
		this.btnSelect.setEnabled(enabled);
		this.btnAdd.setVisible(enabled && isAllowManage());
		this.txtView.setEnabled(enabled);

	}

	@Override
	public void setValue(final Object value) {
		final Object oldValue = getValue();
		if (value != null && !value.toString().trim().equals("") && !value.toString().toLowerCase().equals("null")) {
			try {
				final DynamicDao dao = new DynamicDao(this.tableMeta);
				this.record = dao.findRecord(value);
				this.txtView.setValue(this.record.getSummaryValue());
			} catch (final JKRecordNotFoundException e) {
				JKLogger.fatal("Record not found for field :" + this.tableMeta.getTableName() + " for id : " + value);
				setValue(null);// recursion
			} catch (final JKDataAccessException e) {
				ExceptionUtil.handle(e);
			}
		} else {
			clear();
		}
		final Object newValue = getValue();
		this.fsWrapper.fireValueChangeListener(oldValue, newValue);
	}
}
