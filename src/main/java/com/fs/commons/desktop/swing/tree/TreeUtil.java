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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.fs.commons.desktop.swing.comp.JKTree;

public class TreeUtil {

	/**
	 * @param tree
	 * @param row
	 * @return
	 */
	public static String getExpansionState(final JTree tree, final int row) {
		final TreePath rowPath = tree.getPathForRow(row);
		final StringBuffer buf = new StringBuffer();
		final int rowCount = tree.getRowCount();
		for (int i = row; i < rowCount; i++) {
			final TreePath path = tree.getPathForRow(i);
			if (i == row || isDescendant(path, rowPath)) {
				if (tree.isExpanded(path)) {
					buf.append("," + String.valueOf(i - row));
				}
			} else {
				break;
			}
		}
		return buf.toString();
	}

	/**
	 * @param path1
	 * @param path2
	 * @return
	 */
	public static boolean isDescendant(TreePath path1, final TreePath path2) {
		int count1 = path1.getPathCount();
		final int count2 = path2.getPathCount();
		if (count1 <= count2) {
			return false;
		}
		while (count1 != count2) {
			path1 = path1.getParentPath();
			count1--;
		}
		return path1.equals(path2);
	}

	/**
	 * @param tree
	 * @param node
	 */
	public static void refreshNode(final JTree tree, final TreeNode node) {
		((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
	}

	/**
	 *
	 * @param tree
	 * @param row
	 * @param expansionState
	 */
	public static void restoreExpanstionState(final JTree tree, final int row, final String expansionState) {
		final StringTokenizer stok = new StringTokenizer(expansionState, ",");
		while (stok.hasMoreTokens()) {
			final int token = row + Integer.parseInt(stok.nextToken());
			tree.expandRow(token);
		}
	}

	public static void setParentSelected(final JKTree tree, final TreeCheckBoxNode checkBoxNode, final boolean selected) {
		if (checkBoxNode.getParent() != null && checkBoxNode.getParent() instanceof TreeCheckBoxNode) {
			final TreeCheckBoxNode parent = (TreeCheckBoxNode) checkBoxNode.getParent();
			parent.setSelected(selected);
			setParentSelected(tree, parent, selected);
		}
	}

	/**
	 * @param tree
	 * @param checkBoxNode
	 * @param selected
	 * @param b
	 */
	public static void setSelected(final JTree tree, final TreeNode treeNode, final boolean selected, final boolean includeChilds) {
		if (treeNode instanceof TreeCheckBoxNode) {
			final TreeCheckBoxNode checkBoxNode = (TreeCheckBoxNode) treeNode;
			checkBoxNode.setSelected(selected);
		}
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			final TreeNode child = treeNode.getChildAt(i);
			setSelected(tree, child, selected, true);
		}
	}

	/**
	 * @param node
	 * @return
	 */
	public static ArrayList<TreeNode> toArray(final TreeNode node) {
		final ArrayList<TreeNode> list = new ArrayList<TreeNode>();
		list.add(node);
		final Enumeration children = node.children();
		while (children.hasMoreElements()) {
			final TreeNode nextNode = (TreeNode) children.nextElement();
			list.addAll(toArray(nextNode));
		}
		return list;
	}
}
