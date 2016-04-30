package com.fs.security.ui.helpers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.locale.Lables;
import com.fs.commons.security.Privilige;
import com.fs.security.facade.SecurityFacade;

public class SecurityTreeNode implements TreeNode {

	private ArrayList<Privilige> privilige;

	public SecurityTreeNode() throws DaoException {
		SecurityFacade facade = new SecurityFacade();
		privilige = facade.lstPrivilige();
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return privilige.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return privilige.size();
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public int getIndex(TreeNode node) {
		return privilige.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public Enumeration children() {
		return Collections.enumeration(privilige);
	}

	@Override
	public String toString() {
		return Lables.get("privilige");
	}

}
