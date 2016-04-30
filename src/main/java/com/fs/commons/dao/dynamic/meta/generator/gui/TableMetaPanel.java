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
package com.fs.commons.dao.dynamic.meta.generator.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKCheckBox;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.documents.NumberDocument;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class TableMetaPanel extends JKPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JTextArea txtReportSql = new JTextArea(5, 50);

	JTextArea txtListSql = new JTextArea(2, 50);

	JTextArea txtShortSql = new JTextArea(3, 50);
	JKTextField txtTableId = new JKTextField(20);
	JKTextField txtMaxRecordCount = new JKTextField(new NumberDocument(2), 20);

	private final TableMeta meta;

	ArrayList<ConstraintPanel> constraintsPanels = new ArrayList<ConstraintPanel>();

	private final JKPanel pnlConstraints = new JKPanel();

	JList lstFields = new JList();

	Hashtable<String, TableMeta> tablesHash;

	JKCheckBox chkAllowManage = new JKCheckBox("Allow Manage");

	JKTextField txtCaption = new JKTextField(20);

	JKTextField txtIconName = new JKTextField(20);

	JKTextField txtPageRowCount = new JKTextField(20);

	JKTextField txtFiltersPanel = new JKTextField(20);

	JKTextField txtPanelClass = new JKTextField(50);
	JKTextField txtUIColnmCount = new JKTextField(50);
	JKCheckBox rdAllowAdd = new JKCheckBox("Allow Add");
	JKCheckBox rdAllowUpdate = new JKCheckBox("Allow Update");
	JKCheckBox rdAllowDelete = new JKCheckBox("Allow Delete");

	JKButton btnSave = new JKButton("Save");
	JKButton btnCancel = new JKButton("Cancel");

	JKButton btnTriggers = new JKButton("triggers");
	JKButton btnAddConstraint = new JKButton("Add Constraint");

	/**
	 *
	 * @param meta
	 * @param tablesHash
	 */
	public TableMetaPanel(final TableMeta meta, final Hashtable<String, TableMeta> tablesHash) {
		this.meta = meta;
		this.tablesHash = tablesHash;
		modelToView();
		init();
	}

	private JKPanel getButtonsPanel() {
		final JKPanel pnlButtons = new JKPanel();
		pnlButtons.add(this.btnTriggers);
		pnlButtons.add(this.btnSave);
		pnlButtons.add(this.btnCancel);
		return pnlButtons;
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getConstraintsPanel() {
		for (int i = 0; i < this.meta.getConstraints().size(); i++) {
			this.constraintsPanels.add(new ConstraintPanel(this.meta, this.meta.getConstraints().get(i)));
		}
		this.pnlConstraints.setLayout(new BoxLayout(this.pnlConstraints, BoxLayout.Y_AXIS));
		this.pnlConstraints.add(this.btnAddConstraint);
		for (int i = 0; i < this.constraintsPanels.size(); i++) {
			this.pnlConstraints.add(this.constraintsPanels.get(i));
		}
		return this.pnlConstraints;
	}

	/**
	 *
	 */
	private void init() {
		setLayout(new BorderLayout());
		final JKPanel pnlMain = new JKPanel();
		pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
		final JScrollPane fieldsPane = new JScrollPane(this.lstFields, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		final JScrollPane reportPane = new JScrollPane(this.txtReportSql, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		final JScrollPane txtListPane = new JScrollPane(this.txtListSql, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		final JScrollPane txtShortPane = new JScrollPane(this.txtShortSql, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		fieldsPane.setBorder(BorderFactory.createTitledBorder("Fields list"));
		reportPane.setBorder(BorderFactory.createTitledBorder("Detailed Report SQL"));
		txtListPane.setBorder(BorderFactory.createTitledBorder("Combo Box SQL"));
		txtShortPane.setBorder(BorderFactory.createTitledBorder("Basic Info SQL"));

		final JKPanel pnlButtons = getButtonsPanel();

		pnlMain.add(new JKLabledComponent("Table Id", this.txtTableId));
		pnlMain.add(new JKLabledComponent("Filter fields", this.txtFiltersPanel));
		pnlMain.add(new JKLabledComponent("Caption", this.txtCaption));
		pnlMain.add(new JKLabledComponent("Max Record Count ", this.txtMaxRecordCount));
		pnlMain.add(new JKLabledComponent("Icon name", this.txtIconName));
		pnlMain.add(new JKLabledComponent("Page Count", this.txtPageRowCount));
		pnlMain.add(new JKLabledComponent("Panel class", this.txtPanelClass));
		pnlMain.add(new JKLabledComponent("UI-Column-Count", this.txtUIColnmCount));

		final JKPanel pnlCheckBoxes = new JKPanel();
		pnlCheckBoxes.add(this.chkAllowManage);
		pnlCheckBoxes.add(this.rdAllowAdd);
		pnlCheckBoxes.add(this.rdAllowUpdate);
		pnlCheckBoxes.add(this.rdAllowDelete);

		pnlMain.add(pnlCheckBoxes);

		pnlMain.add(fieldsPane);
		pnlMain.add(reportPane);
		pnlMain.add(txtListPane);
		pnlMain.add(txtShortPane);

		pnlMain.add(pnlButtons);

		add(pnlMain, BorderLayout.CENTER);
		add(new JScrollPane(getConstraintsPanel()), BorderLayout.EAST);

		this.btnTriggers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final PnlTriggers pnl = new PnlTriggers(TableMetaPanel.this.meta.getTriggerNames());
				SwingUtility.showPanelInDialog(pnl, "triggers");
			}
		});
		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				viewToModel();
				SwingUtility.closePanelWindow(TableMetaPanel.this);
			}
		});
		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				SwingUtility.closePanelWindow(TableMetaPanel.this);
			}
		});
		this.btnAddConstraint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final ConstraintPanel pnl = new ConstraintPanel(TableMetaPanel.this.meta);
				SwingUtility.showPanelInDialog(pnl, "Add Constraint");
				TableMetaPanel.this.pnlConstraints.removeAll();
				getConstraintsPanel();
				TableMetaPanel.this.pnlConstraints.validate();
				TableMetaPanel.this.pnlConstraints.repaint();
				SwingUtility.packWindow(TableMetaPanel.this);
			}
		});
		this.lstFields.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 1) {
					showSelectedField();
				}
			}
		});
		this.lstFields.addKeyListener(new KeyAdapter() {
			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					showSelectedField();
				}
			}
		});
	}

	/**
	 *
	 */
	private void modelToView() {
		this.txtTableId.setText(this.meta.getTableId());
		this.txtReportSql.setText(this.meta.getReportSql());
		this.txtListSql.setText(this.meta.getListSql());
		this.txtShortSql.setText(this.meta.getShortReportSql());
		this.txtMaxRecordCount.setText(this.meta.getMaxRecordsCount() + "");
		final DefaultListModel model = new DefaultListModel();
		this.lstFields.setModel(model);
		model.addElement(this.meta.getIdField().getName());
		for (int i = 0; i < this.meta.getFieldList().size(); i++) {
			model.addElement(this.meta.getFieldList().get(i).getName());
		}
		this.chkAllowManage.setSelected(this.meta.isAllowManage());
		if (this.meta.getIconName() != null) {
			this.txtIconName.setText(this.meta.getIconName());
		}
		this.txtCaption.setText(this.meta.getCaption());
		this.txtPageRowCount.setText(this.meta.getPageRowCount() + "");
		this.txtFiltersPanel.setText(this.meta.getFiltersAsString());
		this.txtPanelClass.setText(this.meta.getPanelClassName());
		this.txtUIColnmCount.setText(this.meta.getDefaultUIRowCount() + "");
		this.rdAllowAdd.setSelected(this.meta.isAllowAdd());
		this.rdAllowDelete.setSelected(this.meta.isAllowDelete());
		this.rdAllowUpdate.setSelected(this.meta.isAllowUpdate());
	}

	/**
	 *
	 */
	protected void showSelectedField() {
		final int index = this.lstFields.getSelectedIndex();
		if (index != -1) {
			final FieldMeta field = this.meta.getField((String) this.lstFields.getSelectedValue(), true);
			final FieldPanel panel = new FieldPanel(field, this.tablesHash);
			SwingUtility.showPanelInDialog(panel, "");
		}
	}

	/**
	 *
	 */
	private void viewToModel() {
		this.meta.setTableId(this.txtTableId.getText().trim());
		this.meta.setReportSql(this.txtReportSql.getText().trim());
		this.meta.setListSql(this.txtListSql.getText().trim());
		this.meta.setShortReportSql(this.txtShortSql.getText().trim());
		try {
			this.meta.setMaxRecordsCount(Integer.parseInt(this.txtMaxRecordCount.getText()));
		} catch (final NumberFormatException e) {
			// for empty string
		}
		this.meta.setAllowManage(this.chkAllowManage.isSelected());
		if (!this.txtIconName.getText().equals("")) {
			this.meta.setIconName(this.txtIconName.getText());
		} else {
			this.meta.setIconName(null);
		}
		this.meta.setCaption(this.txtCaption.getText());
		if (!this.txtPageRowCount.getText().equals("")) {
			this.meta.setPageRowCount(Integer.parseInt(this.txtPageRowCount.getText()));
		}
		if (!this.txtFiltersPanel.getText().equals("")) {
			this.meta.setFilters(this.txtFiltersPanel.getText().split(","));
		}
		if (!this.txtUIColnmCount.getText().trim().equals("")) {
			this.meta.setDefaultUIRowCount(this.txtUIColnmCount.getTextAsInteger());
		}
		this.meta.setPanelClassName(this.txtPanelClass.getText().trim());

		this.meta.setAllowAdd(this.rdAllowAdd.isSelected());
		this.meta.setAllowDelete(this.rdAllowDelete.isSelected());
		this.meta.setAllowUpdate(this.rdAllowUpdate.isSelected());
	}

}
