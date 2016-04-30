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
	 * @param path1
	 * @param path2
	 * @return
	 */
	public static boolean isDescendant(TreePath path1, TreePath path2) {
		int count1 = path1.getPathCount();
		int count2 = path2.getPathCount();
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
	 * @param row
	 * @return
	 */
	public static String getExpansionState(JTree tree, int row) {
		TreePath rowPath = tree.getPathForRow(row);
		StringBuffer buf = new StringBuffer();
		int rowCount = tree.getRowCount();
		for (int i = row; i < rowCount; i++) {
			TreePath path = tree.getPathForRow(i);
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
	 * 
	 * @param tree
	 * @param row
	 * @param expansionState
	 */
	public static void restoreExpanstionState(JTree tree, int row, String expansionState) {
		StringTokenizer stok = new StringTokenizer(expansionState, ",");
		while (stok.hasMoreTokens()) {
			int token = row + Integer.parseInt(stok.nextToken());
			tree.expandRow(token);
		}
	}

	/**
	 * @param tree
	 * @param checkBoxNode
	 * @param selected
	 * @param b
	 */
	public static void setSelected(JTree tree, TreeNode treeNode, boolean selected, boolean includeChilds) {
		if (treeNode instanceof TreeCheckBoxNode) {
			TreeCheckBoxNode checkBoxNode = (TreeCheckBoxNode) treeNode;
			checkBoxNode.setSelected(selected);
		}
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			TreeNode child = treeNode.getChildAt(i);
			setSelected(tree, (TreeCheckBoxNode) child, selected, true);
		}
	}

	/**
	 * @param tree
	 * @param node
	 */
	public static void refreshNode(final JTree tree, final TreeNode node) {
		((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
	}

	/**
	 * @param node
	 * @return
	 */
	public static ArrayList<TreeNode> toArray(TreeNode node) {
		ArrayList<TreeNode> list = new ArrayList<TreeNode>();
		list.add(node);
		Enumeration children = node.children();
		while (children.hasMoreElements()) {
			TreeNode nextNode = (TreeNode) children.nextElement();
			list.addAll(toArray(nextNode));
		}
		return list;
	}

	public static void setParentSelected(JKTree tree, TreeCheckBoxNode checkBoxNode, boolean selected) {
		if(checkBoxNode.getParent()!=null && checkBoxNode.getParent() instanceof TreeCheckBoxNode){
			TreeCheckBoxNode parent = (TreeCheckBoxNode)checkBoxNode.getParent();
			parent.setSelected(selected);
			setParentSelected(tree, parent, selected);
		}
	}
}
