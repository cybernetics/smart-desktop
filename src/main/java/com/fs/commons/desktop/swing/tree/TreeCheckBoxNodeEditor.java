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
	Vector<CellEditorListener> listeners = new Vector<CellEditorListener>();

	TreeCheckBoxNodeRenderer renderer = new TreeCheckBoxNodeRenderer();
	JKTree tree;
	private TreeCheckBoxNode checkBoxNode;

	private JCheckBox checkBox;
	boolean editing;

	/**
	 * @param tree
	 */
	public TreeCheckBoxNodeEditor(JKTree tree) {
		this.tree = tree;
		checkBox = renderer.getLeafRenderer();
		checkBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				handleCheckBoxValueChanged(checkBox);
			}
		});
	}

	@Override
	public Object getCellEditorValue() {
		if (checkBoxNode != null) {
			checkBoxNode.setSelected(checkBox.isSelected());
			TreeUtil.setSelected(tree, checkBoxNode, checkBox.isSelected(), true);
			TreeUtil.setParentSelected(tree,checkBoxNode,checkBox.isSelected());
			tree.refresh();
		}
		return checkBoxNode;
	}

	@Override
	public boolean isCellEditable(EventObject event) {
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

	@Override
	public Component getTreeCellEditorComponent(final JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row) {
		final Component editor = renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
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

}
