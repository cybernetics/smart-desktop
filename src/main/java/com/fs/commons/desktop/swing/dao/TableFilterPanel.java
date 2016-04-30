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
package com.fs.commons.desktop.swing.dao;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Types;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.text.Document;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.desktop.swing.comp.JKRadioButton;
import com.fs.commons.desktop.swing.comp.JKSmallButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.documents.FSTextDocument;
import com.fs.commons.desktop.swing.comp.documents.NumberDocument;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.GeneralUtility;
import com.lowagie.text.Font;

public class TableFilterPanel extends JKPanel {
	private static final ImageIcon CANCEL_ICON = new ImageIcon(GeneralUtility.getIconURL("button_cancel_small.png"));

	private static final ImageIcon OK_ICON = new ImageIcon(GeneralUtility.getIconURL("button_ok_small.png"));

	private static final ImageIcon CLEAR_ICON = new ImageIcon(GeneralUtility.getIconURL("clear_left.png"));

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	static Dimension size = new Dimension(650, 45);

	static Dimension smallsize = new Dimension(300, 35);

	QueryTableModel tableModel;

	JKLabel lbl = new JKLabel();

	JKTextField field = new JKTextField(25);

	JKButton btnShow = new JKSmallButton("SHOW");

	JButton btnClear = new JKSmallButton("CLEAR");

	JButton btnClose = new JKSmallButton("HIDE");

	ButtonGroup group = new ButtonGroup();

	JKRadioButton rdStartsWith = new JKRadioButton("SEARCH_STARTS_WITH", false);

	JKRadioButton rdContains = new JKRadioButton("SEARCH_CONTAINS", false);

	JKRadioButton rdEndsWith = new JKRadioButton("SEARCH_ENDS_WITH", false);

	JKRadioButton rdExclude = new JKRadioButton("EXCLUDE", false);

	JKRadioButton rdMoreThan = new JKRadioButton("MORE_THAN", false);
	JKRadioButton rdLessThan = new JKRadioButton("LESS_THAN", false);
	private final FilterListener filterListener;

	private String filterColunmName;

	/**
	 *
	 * @param tableModel
	 *            QueryTableModel
	 * @param colunmIndex
	 *            int
	 */
	public TableFilterPanel(final QueryTableModel tableModel, final String columnName, final FilterListener listener) {
		this.filterListener = listener;
		this.tableModel = tableModel;
		setFilterColunmName(columnName);
		init();
		setVisible(false);
		applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());
	}

	/**
	 *
	 * @return String sql condiotion string
	 */
	public String getConditionString() {
		try {
			SwingValidator.checkEmpty(this.field);
			if (this.rdStartsWith.isSelected()) {
				return this.filterColunmName + " like '" + this.field.getText() + "%'";
			} else if (this.rdContains.isSelected()) {
				return this.filterColunmName + " like '%" + this.field.getText() + "%'";
			} else if (this.rdEndsWith.isSelected()) {
				return this.filterColunmName + " like '%" + this.field.getText() + "'";
			} else if (this.rdExclude.isSelected()) {
				return this.filterColunmName + " not like '%" + this.field.getText() + "%'";
			} else if (this.rdMoreThan.isSelected()) {
				return this.filterColunmName + " > '" + this.field.getText().trim() + "'";
			} else if (this.rdLessThan.isSelected()) {
				return this.filterColunmName + " < '" + this.field.getText().trim() + "'";
			}
			return "";
		} catch (final ValidationException ex) {
			return "";
		}
	}

	/**
	 *
	 * @param sqlType
	 *            int
	 * @param maxWidth
	 *            int
	 * @return Document
	 */
	Document getDocument(final int sqlType, final int maxWidth) {
		switch (sqlType) {
		case Types.INTEGER:
			return new NumberDocument(maxWidth);
		default:
			return new FSTextDocument(maxWidth);
		}
	}

	/**
	 *
	 * @return int
	 */
	public String getFilterColunmName() {
		return this.filterColunmName;
	}

	/**
	 *
	 */
	private void init() {
		SwingUtility.setHotKeyForFocus(this.field, "control F", "control F");
		setLayout(new GridLayout(1, 2));
		setPreferredSize(size);
		final JKPanel pnl = new JKPanel(new GridLayout(2, 1));
		this.lbl.setPreferredSize(new Dimension(75, 20));
		this.lbl.setForeground(new Color(22, 125, 219));
		this.lbl.setHorizontalAlignment(SwingConstants.LEADING);
		this.lbl.setOpaque(false);

		this.field.setFont(new java.awt.Font("TAHOMA", Font.BOLD, 10));
		pnl.add(this.lbl);
		pnl.add(this.field);

		this.group.add(this.rdStartsWith);
		this.group.add(this.rdContains);
		this.group.add(this.rdEndsWith);
		this.group.add(this.rdExclude);
		this.group.add(this.rdMoreThan);
		this.group.add(this.rdLessThan);

		this.rdContains.setSelected(true);
		final JKPanel pnl2 = new JKPanel(new FlowLayout(FlowLayout.LEADING));
		pnl2.add(this.rdStartsWith);
		pnl2.add(this.rdContains);
		pnl2.add(this.rdEndsWith);
		pnl2.add(this.rdExclude);
		pnl2.add(this.rdMoreThan);
		pnl2.add(this.rdLessThan);

		final JKPanel pnlButton = new JKPanel(new FlowLayout(FlowLayout.LEADING));
		// JKPanel pnlButton1=new JKPanel(new GridLayout(1,3,3,1));//to get
		// presfered sizes of buttons
		pnlButton.add(this.btnShow);
		pnlButton.add(this.btnClear);
		pnlButton.add(this.btnClose);
		this.btnClear.setIcon(CLEAR_ICON);
		this.btnShow.setIcon(OK_ICON);
		this.btnClose.setIcon(CANCEL_ICON);
		// pnlButton.add(pnlButton1);

		final JKPanel pnl3 = new JKPanel(new GridLayout(2, 1));
		pnl3.add(pnl2);
		pnl3.add(pnlButton);

		add(pnl);
		add(pnl3);
		// add(pnl2);
		// add(pnlButton);

		this.field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(final KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					TableFilterPanel.this.btnShow.doClick();
					e.consume();
				}
			}
		});
		this.btnShow.setShowProgress(true);
		this.btnShow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				TableFilterPanel.this.filterListener.filterUpdated(TableFilterPanel.this);
			}
		});
		this.btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				TableFilterPanel.this.field.setText("");
				TableFilterPanel.this.btnShow.doClick();
			}
		});
		this.btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				TableFilterPanel.this.btnClear.doClick();
				setVisible(false);
			}
		});
	}

	/**
	 *
	 * @param filterColunmIndex
	 *            int
	 */
	public void setFilterColunmName(final String name) {
		this.filterColunmName = name;
		// int maxWidth = tableModel.getColunmWidth(filterColunmIndex);
		this.lbl.setText(name);

		// txt.setColumns(maxWidth > 25 ? 25 : maxWidth);
		// int colunmType = tableModel.getColunmType(filterColunmIndex);
		// txt.setDocument(getDocument(colunmType, maxWidth));
		// txt.invalidate();
	}

	/**
	 *
	 * @param visible
	 *            boolean
	 */
	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		this.field.requestFocus();
	}

	/**
	 * hideButtons
	 */
	public void showButtons(final boolean show) {
		setPreferredSize(smallsize);
		this.btnClear.setVisible(show);
		this.btnClose.setVisible(show);
		this.btnShow.setVisible(show);
		this.rdContains.setVisible(show);
		this.rdEndsWith.setVisible(show);
		this.rdStartsWith.setVisible(show);
		this.rdExclude.setVisible(show);
		this.rdMoreThan.setVisible(show);
		this.rdLessThan.setVisible(show);
	}
}
