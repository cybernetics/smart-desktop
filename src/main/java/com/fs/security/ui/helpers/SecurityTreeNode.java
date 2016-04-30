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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.locale.Lables;
import com.fs.commons.security.Privilige;
import com.fs.security.facade.SecurityFacade;

public class SecurityTreeNode implements TreeNode {

	private final ArrayList<Privilige> privilige;

	public SecurityTreeNode() throws DaoException {
		final SecurityFacade facade = new SecurityFacade();
		this.privilige = facade.lstPrivilige();
	}

	@Override
	public Enumeration children() {
		return Collections.enumeration(this.privilige);
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public TreeNode getChildAt(final int childIndex) {
		return this.privilige.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return this.privilige.size();
	}

	@Override
	public int getIndex(final TreeNode node) {
		return this.privilige.indexOf(node);
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public String toString() {
		return Lables.get("privilige");
	}

}
