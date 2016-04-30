package com.fs.security.ui.privileges;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class PnlResetPrivilegs extends JKMainPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QueryJTable tblPrivileges = new QueryJTable(AbstractTableMetaFactory.getTableMeta("sec_privileges"));

	public PnlResetPrivilegs() {
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		add(tblPrivileges, BorderLayout.CENTER);
		add(getButtonsPnl(), BorderLayout.SOUTH);
	}

	private JKPanel<?> getButtonsPnl() {
		JKPanel<?> panel = new JKPanel<Object>();
		JKButton clearBtn = new JKButton("CLEAR");
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleClear();
			}

		});
		clearBtn.setIcon(new ImageIcon(GeneralUtility.getIconURL("agt_action_fail.png")));

		JKButton resetBtn = new JKButton("RESET");
		resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// handleReset();
			}
		});
		resetBtn.setIcon("std_trf_courses.png");
		panel.add(clearBtn);
		panel.add(resetBtn);
		return panel;
	}

	// private void handleReset() {
	// ArrayList<Module> modules =
	// ApplicationManager.getInstance().getApplication().getModules();
	// try {
	// for (Module module : modules) {
	// //String sql = buildInsert(module.getPrivilige(), module.getModuleName(),
	// Lables.get(module.getModuleName()), "NULL");
	// try{
	// //DaoUtil.executeUpdate(sql);
	// }catch (Exception e) {
	// System.out.println("Duplicated "+sql);
	// }
	// ArrayList<Menu> menus = module.getMenu();
	// for (int i = 0; i < menus.size(); i++) {
	// Menu menu = menus.get(i);
	// //sql = buildInsert(menu.getPrivilige(), menu.getName(),
	// Lables.get(menu.getName()), module.getPrivilige() + " ");
	// try{
	// DaoUtil.executeUpdate(sql);
	// }catch (Exception e) {
	// System.out.println("Duplicated "+sql);
	// }
	// ArrayList<MenuItem> items = menu.getItems();
	// for (int j = 0; j < items.size(); j++) {
	// //MenuItem item = items.get(j);
	// //sql = buildInsert(item.getPrivilige(), item.getName(),
	// Lables.get(item.getName()), menu.getPrivilige() + " ");
	// try{
	// DaoUtil.executeUpdate(sql);
	// }catch (Exception e) {
	// //System.out.println("Duplicated "+sql);
	// }
	// }
	// }
	// }
	// } finally {
	// tblPrivileges.reloadData();
	// }
	// }

	// /**
	// *
	// * @param priviligeId
	// * @param priviligeName
	// * @param label
	// * @throws SecurityException
	// */
	// private String buildInsert(int priviligeId, String priviligeName, String
	// label, String parentPriilige) {
	// StringBuffer buf = new StringBuffer();
	// buf.append("INSERT INTO sec_privileges values (");
	// buf.append(priviligeId);
	// buf.append(",'");
	// buf.append(priviligeName);
	// buf.append("','");
	// buf.append(label);
	// buf.append("',");
	// buf.append(parentPriilige);
	// buf.append(");\n");
	// return buf.toString();
	// }

	private void handleClear() {
		if (SwingUtility.showConfirmationDialog("THIS_WILL_DELETE_ALL_ROLES_AND_ROLE_PRIVLIGES_ARE_YOU_SURE?")) {
			DynamicDao dao = new DynamicDao("sec_privileges");
			DynamicDao rolesDao = new DynamicDao("sec_role_privileges");
			try {
				try {
					rolesDao.deleteAllRecords();
				} catch (RecordNotFoundException e) {
				}
				try {
					dao.deleteAllRecords();
				} catch (RecordNotFoundException e) {
				}
			} catch (DaoException e) {
				ExceptionUtil.handleException(e);
			} finally {
				tblPrivileges.reloadData();
			}
		}
	}

}
