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
	private final SecurityTreeNode ROOT = new SecurityTreeNode();
	JKTree tree = new JKTree(ROOT);
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

	/**
	 * 
	 */
	private void init() {
		TreeCheckBoxNodeRenderer renderer = new TreeCheckBoxNodeRenderer();
		tree.setCellRenderer(renderer);

		tree.setCellEditor(new TreeCheckBoxNodeEditor(tree));
		tree.setEditable(true);
//		tree.setRootVisible(false);
		setLayout(new BorderLayout());
		add(getNothPanel(), BorderLayout.NORTH);
		add(new JScrollPane(tree), BorderLayout.CENTER);
		add(getSouthPanel(), BorderLayout.SOUTH);
		handleRoleChanged();
		cmbRoles.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleRoleChanged();
			}
		});
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSave();
			}
		});
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				handleClose();
			}
		});
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
	protected void handleSave() {
		try {
			ArrayList<TreeNode> array = tree.getNodesAsArray();
			DynamicDao dao = getDao();
			int roleId = cmbRoles.getSelectedIdValueAsInteger();
			try {
				dao.deleteByFieldValue("role_id", roleId);
			} catch (RecordNotFoundException e) {
				//its safe to eat this exception
			}
			for (TreeNode treeNode : array) {
				if (treeNode instanceof Privilige) {
					Privilige p = (Privilige) treeNode;
					if (p.isSelected()) {
						Record record = dao.createEmptyRecord(true);
						record.setFieldValue("role_id", roleId);
						record.setFieldValue("privilege_id", p.getPriviligeId());
						dao.insertRecord(record);
					}
				}
			}
			SwingUtility.showSuccessDialog("ROLE_UPDATED_SUCC");
			handleRoleChanged();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel getSouthPanel() {
		JKPanel pnl = new JKPanel();
		pnl.add(btnSave);
		pnl.add(btnClose);
		btnSave.setShortcut("F3", "F3");
		btnSave.setIcon("save_commons_model_icon.gif");

		btnClose.setShortcut("F6", "F6");
		btnClose.setIcon("close.png");
		
		btnSave.setShowProgress(true);
		return pnl;
	}

	/**
	 * 
	 */
	protected void handleRoleChanged() {
		try {
			int roldId = cmbRoles.getSelectedIdValueAsInteger();
			if (roldId != -1) {
				TreeUtil.setSelected(tree, tree.getRoot(), false, true);
				DynamicDao dao = getDao();
				ArrayList<Record> rolePrivliges = dao.findByFieldValue("role_id", roldId);
				for (Record record : rolePrivliges) {
					Privilige privilige = (Privilige) tree.searchNode(new Privilige(record.getFieldValueAsInteger("privilege_id")), null);
					privilige.setSelected(true);
				}
			}
			tree.refresh();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		tree.setEnabled(cmbRoles.getSelectedIdValueAsInteger() != -1);
		btnSave.setEnabled(cmbRoles.getSelectedIdValueAsInteger() != -1);
	}

	private DynamicDao getDao() {
		DynamicDao dao = new DynamicDao(AbstractTableMetaFactory.getTableMeta("sec_role_privileges"));
		return dao;
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel getNothPanel() {
		JKPanel pnl = new JKPanel();
		pnl.add(new JKLabledComponent("ROLES", cmbRoles));
		return pnl;
	}

	/**
	 * @param privlige
	 * @param selected
	 */
	private void setSelected(Privilige privlige, boolean selected) {

	}
}
