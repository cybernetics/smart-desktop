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

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.TreeCellEditor;

import com.fs.commons.desktop.swing.comp.JKTree;

public class TreeCheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {
	/**
	 *
	 */
	private static final long serialVersionUID = 5144761768239460062L;

	Vector<CellEditorListener> listeners = new Vector<CellEditorListener>();

	TreeCheckBoxNodeRenderer renderer = new TreeCheckBoxNodeRenderer();
	JKTree tree;
	private TreeCheckBoxNode checkBoxNode;

	private final JCheckBox checkBox;
	boolean editing;

	/**
	 * @param tree
	 */
	public TreeCheckBoxNodeEditor(final JKTree tree) {
		this.tree = tree;
		this.checkBox = this.renderer.getLeafRenderer();
		this.checkBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {
				handleCheckBoxValueChanged(TreeCheckBoxNodeEditor.this.checkBox);
			}
		});
	}

	@Override
	public Object getCellEditorValue() {
		if (this.checkBoxNode != null) {
			this.checkBoxNode.setSelected(this.checkBox.isSelected());
			TreeUtil.setSelected(this.tree, this.checkBoxNode, this.checkBox.isSelected(), true);
			TreeUtil.setParentSelected(this.tree, this.checkBoxNode, this.checkBox.isSelected());
			this.tree.refresh();
		}
		return this.checkBoxNode;
	}

	@Override
	public Component getTreeCellEditorComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded,
			final boolean leaf, final int row) {
		final Component editor = this.renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
		if (value instanceof TreeCheckBoxNode) {
			this.checkBoxNode = (TreeCheckBoxNode) value;
		} else {
			this.checkBoxNode = null;
		}
		return editor;
	}

	/**
	 *
	 */
	private void handleCheckBoxValueChanged(final JCheckBox checkBox) {
		stopCellEditing();
	}

	@Override
	public boolean isCellEditable(final EventObject event) {
		// // DateTimeUtil.printCurrentTime("isCellEditable()");
		// boolean returnValue = false;
		// if (event instanceof MouseEvent) {
		// MouseEvent mouseEvent = (MouseEvent) event;
		// TreePath path = tree.getPathForLocation(mouseEvent.getX(),
		// mouseEvent.getY());
		// if (path != null) {
		// Object node = path.getLastPathComponent();
		// if ((node != null) && (node instanceof TreeCheckBoxNode)) {
		// TreeCheckBoxNode treeNode = (TreeCheckBoxNode) node;
		// returnValue = treeNode.isEditable();
		// }
		// }
		// }
		// return returnValue;
		return true;
	}

}
