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
import javax.swing.JComponent;
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
	public TableFilterPanel(QueryTableModel tableModel, String columnName, FilterListener listener) {
		this.filterListener = listener;
		this.tableModel = tableModel;
		setFilterColunmName(columnName);
		init();
		setVisible(false);
		applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());
	}

	/**
	 * 
	 */
	private void init() {
		SwingUtility.setHotKeyForFocus(field, "control F", "control F");
		setLayout(new GridLayout(1, 2));
		setPreferredSize(size);
		JKPanel pnl = new JKPanel(new GridLayout(2, 1));
		lbl.setPreferredSize(new Dimension(75, 20));
		lbl.setForeground(new Color(22, 125, 219));
		lbl.setHorizontalAlignment(SwingConstants.LEADING);
		lbl.setOpaque(false);

		field.setFont(new java.awt.Font("TAHOMA", Font.BOLD, 10));
		pnl.add(lbl);
		pnl.add((JComponent) field);

		group.add(rdStartsWith);
		group.add(rdContains);
		group.add(rdEndsWith);
		group.add(rdExclude);
		group.add(rdMoreThan);
		group.add(rdLessThan);

		rdContains.setSelected(true);
		JKPanel pnl2 = new JKPanel(new FlowLayout(FlowLayout.LEADING));
		pnl2.add(rdStartsWith);
		pnl2.add(rdContains);
		pnl2.add(rdEndsWith);
		pnl2.add(rdExclude);
		pnl2.add(rdMoreThan);
		pnl2.add(rdLessThan);

		JKPanel pnlButton = new JKPanel(new FlowLayout(FlowLayout.LEADING));
		// JKPanel pnlButton1=new JKPanel(new GridLayout(1,3,3,1));//to get
		// presfered sizes of buttons
		pnlButton.add(btnShow);
		pnlButton.add(btnClear);
		pnlButton.add(btnClose);
		btnClear.setIcon(CLEAR_ICON);
		btnShow.setIcon(OK_ICON);
		btnClose.setIcon(CANCEL_ICON);
		// pnlButton.add(pnlButton1);

		JKPanel pnl3 = new JKPanel(new GridLayout(2, 1));
		pnl3.add(pnl2);
		pnl3.add(pnlButton);

		add(pnl);
		add(pnl3);
		// add(pnl2);
		// add(pnlButton);

		field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					btnShow.doClick();
					e.consume();
				}
			}
		});
		btnShow.setShowProgress(true);
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filterListener.filterUpdated(TableFilterPanel.this);
			}
		});
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				field.setText("");
				btnShow.doClick();
			}
		});
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClear.doClick();
				setVisible(false);
			}
		});
	}

	/**
	 * 
	 * @return int
	 */
	public String getFilterColunmName() {
		return filterColunmName;
	}

	/**
	 * 
	 * @param filterColunmIndex
	 *            int
	 */
	public void setFilterColunmName(String name) {
		this.filterColunmName = name;
		// int maxWidth = tableModel.getColunmWidth(filterColunmIndex);
		lbl.setText(name);

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
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		field.requestFocus();
	}

	/**
	 * 
	 * @param sqlType
	 *            int
	 * @param maxWidth
	 *            int
	 * @return Document
	 */
	Document getDocument(int sqlType, int maxWidth) {
		switch (sqlType) {
		case Types.INTEGER:
			return new NumberDocument(maxWidth);
		default:
			return new FSTextDocument(maxWidth);
		}
	}

	/**
	 * 
	 * @return String sql condiotion string
	 */
	public String getConditionString() {
		try {
			SwingValidator.checkEmpty(field);
			if (rdStartsWith.isSelected()) {
				return filterColunmName + " like '" + field.getText() + "%'";
			} else if (rdContains.isSelected()) {
				return filterColunmName + " like '%" + field.getText() + "%'";
			} else if (rdEndsWith.isSelected()) {
				return filterColunmName + " like '%" + field.getText() + "'";
			} else if (rdExclude.isSelected()) {
				return filterColunmName + " not like '%" + field.getText() + "%'";
			} else if (rdMoreThan.isSelected()) {
				return filterColunmName + " > '" + field.getText().trim() + "'";
			} else if (rdLessThan.isSelected()) {
				return filterColunmName + " < '" + field.getText().trim() + "'";
			}
			return "";
		} catch (ValidationException ex) {
			return "";
		}
	}

	/**
	 * hideButtons
	 */
	public void showButtons(boolean show) {
		setPreferredSize(smallsize);
		btnClear.setVisible(show);
		btnClose.setVisible(show);
		btnShow.setVisible(show);
		rdContains.setVisible(show);
		rdEndsWith.setVisible(show);
		rdStartsWith.setVisible(show);
		rdExclude.setVisible(show);
		rdMoreThan.setVisible(show);
		rdLessThan.setVisible(show);
	}
}
