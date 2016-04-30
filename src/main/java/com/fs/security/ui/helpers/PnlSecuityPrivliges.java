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
package com.fs.security.ui.helpers;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.tree.TreeNode;

import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTree;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.commons.desktop.swing.tree.TreeCheckBoxNodeEditor;
import com.fs.commons.desktop.swing.tree.TreeCheckBoxNodeRenderer;
import com.fs.commons.desktop.swing.tree.TreeUtil;
import com.fs.commons.security.Privilige;
import com.fs.commons.util.ExceptionUtil;

public class PnlSecuityPrivliges extends JKPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4577778132829126243L;
	private final SecurityTreeNode ROOT = new SecurityTreeNode();
	JKTree tree = new JKTree(this.ROOT);
	DaoComboBox cmbRoles = new DaoComboBox(AbstractTableMetaFactory.getTableMeta("sec_roles"));
	JKButton btnSave = new JKButton("SAVE");
	// JKButton btnReload=new JKButton("RELOAD");
	JKButton btnClose = new JKButton("CLOSE");

	/**
	 *
	 * @throws DaoException
	 */
	public PnlSecuityPrivliges() throws DaoException {
		init();
	}

	private DynamicDao getDao() {
		final DynamicDao dao = new DynamicDao(AbstractTableMetaFactory.getTableMeta("sec_role_privileges"));
		return dao;
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getNothPanel() {
		final JKPanel pnl = new JKPanel();
		pnl.add(new JKLabledComponent("ROLES", this.cmbRoles));
		return pnl;
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getSouthPanel() {
		final JKPanel pnl = new JKPanel();
		pnl.add(this.btnSave);
		pnl.add(this.btnClose);
		this.btnSave.setShortcut("F3", "F3");
		this.btnSave.setIcon("save_commons_model_icon.gif");

		this.btnClose.setShortcut("F6", "F6");
		this.btnClose.setIcon("close.png");

		this.btnSave.setShowProgress(true);
		return pnl;
	}

	/**
	 *
	 */
	protected void handleClose() {
		SwingUtility.closePanel(this);
	}

	/**
	 *
	 */
	protected void handleRoleChanged() {
		try {
			final int roldId = this.cmbRoles.getSelectedIdValueAsInteger();
			if (roldId != -1) {
				TreeUtil.setSelected(this.tree, this.tree.getRoot(), false, true);
				final DynamicDao dao = getDao();
				final ArrayList<Record> rolePrivliges = dao.findByFieldValue("role_id", roldId);
				for (final Record record : rolePrivliges) {
					final Privilige privilige = (Privilige) this.tree.searchNode(new Privilige(record.getFieldValueAsInteger("privilege_id")), null);
					privilige.setSelected(true);
				}
			}
			this.tree.refresh();
		} catch (final Exception e) {
			ExceptionUtil.handleException(e);
		}
		this.tree.setEnabled(this.cmbRoles.getSelectedIdValueAsInteger() != -1);
		this.btnSave.setEnabled(this.cmbRoles.getSelectedIdValueAsInteger() != -1);
	}

	/**
	 *
	 */
	protected void handleSave() {
		try {
			final ArrayList<TreeNode> array = this.tree.getNodesAsArray();
			final DynamicDao dao = getDao();
			final int roleId = this.cmbRoles.getSelectedIdValueAsInteger();
			try {
				dao.deleteByFieldValue("role_id", roleId);
			} catch (final RecordNotFoundException e) {
				// its safe to eat this exception
			}
			for (final TreeNode treeNode : array) {
				if (treeNode instanceof Privilige) {
					final Privilige p = (Privilige) treeNode;
					if (p.isSelected()) {
						final Record record = dao.createEmptyRecord(true);
						record.setFieldValue("role_id", roleId);
						record.setFieldValue("privilege_id", p.getPriviligeId());
						dao.insertRecord(record);
					}
				}
			}
			SwingUtility.showSuccessDialog("ROLE_UPDATED_SUCC");
			handleRoleChanged();
		} catch (final Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 *
	 */
	private void init() {
		final TreeCheckBoxNodeRenderer renderer = new TreeCheckBoxNodeRenderer();
		this.tree.setCellRenderer(renderer);

		this.tree.setCellEditor(new TreeCheckBoxNodeEditor(this.tree));
		this.tree.setEditable(true);
		// tree.setRootVisible(false);
		setLayout(new BorderLayout());
		add(getNothPanel(), BorderLayout.NORTH);
		add(new JScrollPane(this.tree), BorderLayout.CENTER);
		add(getSouthPanel(), BorderLayout.SOUTH);
		handleRoleChanged();
		this.cmbRoles.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleRoleChanged();
			}
		});
		this.btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleSave();
			}
		});
		this.btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent actionevent) {
				handleClose();
			}
		});
	}

	/**
	 * @param privlige
	 * @param selected
	 */
	private void setSelected(final Privilige privlige, final boolean selected) {

	}
}
