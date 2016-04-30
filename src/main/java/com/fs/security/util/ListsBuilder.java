package com.fs.security.util;

import com.fs.commons.desktop.swing.dao.DaoComboBox;

public class ListsBuilder {

	public static DaoComboBox buildUserComboBox() {
		return new DaoComboBox("SELECT " + "sec_users.user_record_id, " + "sec_users.user_id FROM sec_users");
	}

	public static DaoComboBox buildRoleComboBox() {
		return new DaoComboBox("SELECT " + "sec_roles.role_id, " + "sec_roles.role_name FROM sec_roles");
	}

}
