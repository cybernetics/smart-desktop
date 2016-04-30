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
package com.fs.commons.desktop.swing.comp;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import com.fs.commons.desktop.swing.tree.FSTreeModel;
import com.fs.commons.desktop.swing.tree.TreeUtil;

public class JKTree extends JTree {

	/**
	 *
	 */
	private static final long serialVersionUID = 2354424326770047463L;

	public JKTree() {
		super();
		init();
	}

	public JKTree(final Hashtable<?, ?> value) {
		super(value);
		init();
	}

	public JKTree(final Object[] value) {
		super(value);
		init();
	}

	public JKTree(final TreeModel newModel) {
		super(newModel);
		init();
	}

	public JKTree(final TreeNode root) {
		super(new FSTreeModel(root, false));
		init();
	}

	public JKTree(final TreeNode root, final boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
		init();
	}

	public JKTree(final Vector<?> value) {
		super(value);
		init();
	}

	/**
	 *
	 * @param listener
	 */
	public void addActionListener(final ActionListener listener) {
		final MouseListener ml = new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				final int selRow = getRowForLocation(e.getX(), e.getY());
				if (selRow != -1) {
					if (e.getClickCount() == 1) {
						// mySingleClick(selRow, selPath);
					} else if (e.getClickCount() == 2) {
						listener.actionPerformed(null);
					}
				}
			}
		};
		final KeyListener kl = new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					listener.actionPerformed(null);
				}
			}
		};
		addMouseListener(ml);
		addKeyListener(kl);
	}

	public ArrayList<TreeNode> getNodesAsArray() {
		return TreeUtil.toArray(getRoot());
	}

	public TreeNode getRoot() {
		return (TreeNode) getModel().getRoot();
	}

	private void init() {
		// setUI(new WindowsTreeUI());
		// setBackground(Colors.MAIN_PANEL_BG);
		// JComponent renderer = (JComponent)getCellRenderer();
		// renderer.setOpaque(false);
		// renderer.setBackground(Colors.MAIN_PANEL_BG);
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);
	}

	/**
	 *
	 */
	public void refresh() {
		invalidate();
		repaint();
	}

	/**
	 *
	 * @param nodeToFind
	 * @return
	 */
	public TreeNode searchNode(final TreeNode nodeToFind, TreeNode searchFrom) {
		if (searchFrom == null) {
			searchFrom = (TreeNode) getModel().getRoot();
		}
		if (searchFrom.equals(nodeToFind)) {
			return searchFrom;
		}
		final Enumeration children = searchFrom.children();
		while (children.hasMoreElements()) {
			final TreeNode node = searchNode(nodeToFind, (TreeNode) children.nextElement());
			if (node != null) {
				return node;
			}
		}
		return null;
	}
}
