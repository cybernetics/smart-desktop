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
package com.fs.commons.desktop.swing.tree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class FSTreeModel extends DefaultTreeModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 2574127995630521662L;

	public FSTreeModel(final TreeNode root) {
		super(root);
	}

	public FSTreeModel(final TreeNode root, final boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
	}

	@Override
	protected void fireTreeNodesChanged(final Object source, final Object[] path, final int[] childIndices, final Object[] children) {
		super.fireTreeNodesChanged(source, path, childIndices, children);
	}

	public void reloadNode(final TreeNode node) {
		fireTreeNodesChanged(this, getPathToRoot(node), null, null);
	}

	/**
	 * Override this method because it has a bug , it assumes that the tree node
	 * is always from type MutableTreeNode in the super class
	 */
	@Override
	public void valueForPathChanged(final TreePath path, final Object newValue) {
		if (path.getLastPathComponent() instanceof MutableTreeNode) {
			final MutableTreeNode aNode = (MutableTreeNode) path.getLastPathComponent();
			aNode.setUserObject(newValue);
		}
		nodeChanged((TreeNode) path.getLastPathComponent());
	}
}
