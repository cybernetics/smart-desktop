package com.fs.commons.desktop.swing.tree;

import javax.swing.tree.TreeNode;

public interface TreeCheckBoxNode extends TreeNode {
	public boolean isSelected();

	public void setSelected(boolean newValue);

	public String getText();

	public boolean isEditable();
}
