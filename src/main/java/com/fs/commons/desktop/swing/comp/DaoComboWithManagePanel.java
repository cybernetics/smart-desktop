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
package com.fs.commons.desktop.swing.comp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailCRUDLPanel;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.ExceptionUtil;

public class DaoComboWithManagePanel extends JKPanel<Object> {

	private static final long serialVersionUID = 1L;

	private TableMeta tableMeta;

	private DaoComboBox combo;

	JKButton btnEdit = new JKButton("");

	public DaoComboWithManagePanel() {
	}

	public DaoComboWithManagePanel(final ForiegnKeyFieldMeta meta) throws JKDataAccessException {
		this(AbstractTableMetaFactory.getTableMeta(meta.getParentTable().getDataSource(), meta.getReferenceTable()));
		this.combo.setName(meta.getName());
	}

	/**
	 * @param tableMeta
	 * @throws JKDataAccessException
	 */
	public DaoComboWithManagePanel(final TableMeta tableMeta) throws JKDataAccessException {
		this.tableMeta = tableMeta;
		this.combo = new DaoComboBox(tableMeta);
		init();
	}

	@Override
	public void clear() {
		this.combo.clear();
	}

	/**
	 * @return
	 * @throws JKDataAccessException
	 * @throws UIOPanelCreationException
	 */
	private JKPanel createManagePanel() throws JKDataAccessException, UIOPanelCreationException {
		final DynMasterDetailCRUDLPanel pnl = new DynMasterDetailCRUDLPanel(this.tableMeta);
		return pnl;
	}

	/**
	 * @return the combo
	 */
	public DaoComboBox getCombo() {
		return this.combo;
	}

	@Override
	public Object getDefaultValue() {
		return this.combo.getDefaultValue();
	}

	public int getItemCount() {
		return this.combo.getItemCount();
	}

	/**
	 * @return
	 */
	public int getSelectedIndex() {
		return this.combo.getSelectedIndex();
	}

	/**
	 *
	 */
	@Override
	public String getValue() {
		if (this.combo.getValue() != null) {
			return this.combo.getValue().toString();
		}
		return null;
	}

	/**
	 *
	 */
	protected void handleEdit() {
		try {
			final JKPanel pnl = createManagePanel();
			SwingUtility.showPanelInDialog(pnl, this.tableMeta.getCaption());
			reloadData();
			this.combo.requestFocus();
			if (this.combo.getItemCount() > 0) {
				this.combo.setSelectedIndex(0);
			}
			SwingUtility.packWindow(this);
			this.btnEdit.transferFocus();
		} catch (final TableMetaNotFoundException e) {
			ExceptionUtil.handle(e);
		} catch (final JKDataAccessException e) {
			ExceptionUtil.handle(e);
		} catch (final UIOPanelCreationException e) {
			ExceptionUtil.handle(e);
		}

	}

	/**
	 *
	 */
	private void init() {
		setLayout(new BorderLayout());
		add(this.combo, BorderLayout.CENTER);
		if (this.tableMeta.isAllowManage()) {
			add(this.btnEdit, BorderLayout.LINE_END);
		}
		this.btnEdit.setIcon(new ImageIcon(GeneralUtility.getIconURL("edit.png")));
		this.btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleEdit();
			}
		});
		this.btnEdit.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					handleEdit();
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					DaoComboWithManagePanel.this.btnEdit.transferFocus();
				}
			}
		});
	}

	public void reloadData() throws JKDataAccessException {
		this.combo.forceReload();
	}

	@Override
	public void requestFocus() {
		this.combo.requestFocus();
	}

	@Override
	public void reset() {
		this.combo.reset();
	}

	/**
	 * @param combo
	 *            the combo to set
	 */
	public void setCombo(final DaoComboBox combo) {
		this.combo = combo;
	}

	@Override
	public void setDefaultValue(final Object defaultValue) {
		this.combo.setDefaultValue(defaultValue);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		this.combo.setEnabled(enabled);
		this.btnEdit.setVisible(enabled);
	}

	@Override
	public void setValue(final Object value) {
		if (value != null) {
			this.combo.setValue(value.toString());
		} else {
			this.combo.setSelectedIndex(-1);
		}
	}

}
