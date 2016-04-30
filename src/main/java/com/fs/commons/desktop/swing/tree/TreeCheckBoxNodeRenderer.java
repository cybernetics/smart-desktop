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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

public class TreeCheckBoxNodeRenderer implements TreeCellRenderer {
	private final JCheckBox leafRenderer = new JCheckBox("");

	// private DefaultTreeCellRenderer nonLeafRenderer = new
	// DefaultTreeCellRenderer();

	Color selectionBorderColor, selectionForeground, selectionBackground, textForeground, textBackground;

	// ///////////////////////////////////////////////////////////////////////////////////////
	public TreeCheckBoxNodeRenderer() {
		Font fontValue;
		fontValue = UIManager.getFont("Tree.font");
		if (fontValue != null) {
			this.leafRenderer.setFont(fontValue);
		}
		final Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
		this.leafRenderer.setFocusPainted(booleanValue != null && booleanValue.booleanValue());

		this.selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
		this.selectionForeground = UIManager.getColor("Tree.selectionForeground");
		this.selectionBackground = UIManager.getColor("Tree.selectionBackground");
		this.textForeground = UIManager.getColor("Tree.textForeground");
		this.textBackground = UIManager.getColor("Tree.textBackground");
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	protected JCheckBox getLeafRenderer() {
		return this.leafRenderer;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded,
			final boolean leaf, final int row, final boolean hasFocus) {
		Component returnValue;

		final String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false);
		this.leafRenderer.setText(stringValue);
		this.leafRenderer.setSelected(false);

		this.leafRenderer.setEnabled(tree.isEnabled());

		// if (selected) {
		// leafRenderer.setForeground(selectionForeground);
		// leafRenderer.setBackground(selectionBackground);
		// } else {
		this.leafRenderer.setForeground(this.textForeground);
		this.leafRenderer.setBackground(this.textBackground);
		// }

		// System.out.println(value.getClass().getName());
		// if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
		// Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
		if (value instanceof TreeCheckBoxNode) {
			final TreeCheckBoxNode node = (TreeCheckBoxNode) value;
			this.leafRenderer.setText(node.getText());
			this.leafRenderer.setSelected(node.isSelected());
		}
		// }
		returnValue = this.leafRenderer;
		// } else {
		// returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree,
		// value, selected, expanded, leaf, row, hasFocus);
		// }
		return returnValue;
	}
	// ///////////////////////////////////////////////////////////////////////////////////////

}