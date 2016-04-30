package com.fs.commons.desktop.swing.tree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class FSTreeModel extends DefaultTreeModel {

	public FSTreeModel(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
	}

	public FSTreeModel(TreeNode root) {
		super(root);
	}

	/**
	 * Override this method because it has a bug , it assumes that the tree node
	 * is always from type MutableTreeNode in the super class
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
		if (path.getLastPathComponent() instanceof MutableTreeNode) {
			MutableTreeNode aNode = (MutableTreeNode) path.getLastPathComponent();
			aNode.setUserObject(newValue);
		}
		nodeChanged((TreeNode) path.getLastPathComponent());
	}
	
	public void reloadNode(TreeNode node){
		fireTreeNodesChanged(this, getPathToRoot(node), null, null);
	}
	
	@Override
	protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
		super.fireTreeNodesChanged(source, path, childIndices, children);
	}
}
