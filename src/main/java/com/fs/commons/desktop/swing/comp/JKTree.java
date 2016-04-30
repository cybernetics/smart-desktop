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

	public JKTree() {
		super();
		init();
	}

	public JKTree(Hashtable<?, ?> value) {
		super(value);
		init();
	}

	public JKTree(Object[] value) {
		super(value);
		init();
	}

	public JKTree(TreeModel newModel) {
		super(newModel);
		init();
	}

	public JKTree(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
		init();
	}

	public JKTree(TreeNode root) {
		super(new FSTreeModel(root, false));
		init();
	}

	public JKTree(Vector<?> value) {
		super(value);
		init();
	}

	private void init() {
		// setUI(new WindowsTreeUI());
		// setBackground(Colors.MAIN_PANEL_BG);
		// JComponent renderer = (JComponent)getCellRenderer();
		// renderer.setOpaque(false);
		// renderer.setBackground(Colors.MAIN_PANEL_BG);
	}

	/**
	 * 
	 * @param listener
	 */
	public void addActionListener(final ActionListener listener) {
		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int selRow = getRowForLocation(e.getX(), e.getY());
				if (selRow != -1) {
					if (e.getClickCount() == 1) {
						// mySingleClick(selRow, selPath);
					} else if (e.getClickCount() == 2) {
						listener.actionPerformed(null);
					}
				}
			}
		};
		KeyListener kl = new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					listener.actionPerformed(null);
				}
			}
		};
		addMouseListener(ml);
		addKeyListener(kl);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	/**
	 * 
	 * @param nodeToFind
	 * @return
	 */
	public TreeNode searchNode(TreeNode nodeToFind, TreeNode searchFrom) {
		if (searchFrom == null) {
			searchFrom = (TreeNode) getModel().getRoot();
		}
		if (searchFrom.equals(nodeToFind)) {
			return searchFrom;
		}
		Enumeration children = searchFrom.children();
		while (children.hasMoreElements()) {
			TreeNode node = searchNode(nodeToFind, (TreeNode) children.nextElement());
			if (node != null) {
				return node;
			}
		}
		return null;
	}

	/**
	 * 
	 */
	public void refresh() {
		invalidate();
		repaint();
	}

	public TreeNode getRoot() {
		return (TreeNode) getModel().getRoot();
	}
	
	public ArrayList<TreeNode> getNodesAsArray(){
		return TreeUtil.toArray(getRoot());
	}
}
