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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BoxLayout;

import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta.Relation;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta.ViewMode;
import com.fs.commons.dao.dynamic.meta.IdFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKCheckBox;
import com.fs.commons.desktop.swing.comp.JKComboBox;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.documents.NumberDocument;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class FieldPanel extends JKPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final FieldMeta fieldMeta;

	private final JKTextField txtName = new JKTextField(15);

	private final JKTextField txtCaption = new JKTextField(15);

	private final JKTextField txtType = new JKTextField(new NumberDocument(3), 15);

	private final JKTextField txtWidth = new JKTextField(new NumberDocument(3), 15);

	JKTextField txtDefaultValue = new JKTextField(20);
	private final JKCheckBox chkAllowUpdate = new JKCheckBox("Allow update");

	private final JKTextField txtMaxLength = new JKTextField(new NumberDocument(3), 15);

	private final JKCheckBox chkCofirmInput = new JKCheckBox("");

	private final JKCheckBox chkRequired = new JKCheckBox("");

	private final JKCheckBox chkVisible = new JKCheckBox("");

	private final JKComboBox cmbReferenceTable = new JKComboBox();

	private final JKComboBox cmbReferenceField = new JKComboBox();

	private final JKComboBox cmbReleation = new JKComboBox();

	private final JKCheckBox chkAutoIncrement = new JKCheckBox("");

	private final JKCheckBox chkEnabled = new JKCheckBox("");

	private final JKCheckBox chkSummaryFild = new JKCheckBox("");

	JKButton btnSave = new JKButton("Save");

	JKButton btnCancel = new JKButton("Cancel");

	private final Hashtable<String, TableMeta> tablesHash;

	JKComboBox cmbViewMode = new JKComboBox();

	/**
	 *
	 * @param fieldMeta
	 * @param tablesHash
	 */
	public FieldPanel(final FieldMeta fieldMeta, final Hashtable<String, TableMeta> tablesHash) {
		this.fieldMeta = fieldMeta;
		this.tablesHash = tablesHash;
		this.cmbReleation.addItem(Relation.ONE_TO_ONE);
		this.cmbReleation.addItem(Relation.ONE_TO_MANY);
		this.cmbReleation.addItem(Relation.MANY_TO_ONE);
		this.cmbReleation.addItem(Relation.MANY_TO_MANY);
		this.cmbReleation.addItem(Relation.ONE_TO_ONE);

		this.cmbViewMode.addItem(ViewMode.COMBO);
		this.cmbViewMode.addItem(ViewMode.LIST);
		this.cmbViewMode.addItem(ViewMode.DIALOG);
		this.cmbViewMode.addItem(ViewMode.LOOKUP);

		final Enumeration enu = tablesHash.keys();
		while (enu.hasMoreElements()) {
			final TableMeta tableMeta = tablesHash.get(enu.nextElement());
			this.cmbReferenceTable.addItem(tableMeta.getTableId());
		}
		init();
		this.txtName.setEnabled(false);
		this.cmbReferenceField.setEnabled(false);
		this.cmbReferenceTable.setEnabled(false);

		modelToView();
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getForiegnKeyPanel() {
		final JKPanel panel = new JKPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JKLabledComponent("Reference table", this.cmbReferenceTable));
		panel.add(new JKLabledComponent("Reference field", this.cmbReferenceField));
		panel.add(new JKLabledComponent("Releation", this.cmbReleation));
		panel.add(new JKLabledComponent("View Mode", this.cmbViewMode));
		return panel;
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getIdFieldPanel() {
		final JKPanel panel = new JKPanel();
		panel.add(new JKLabledComponent("Auto increment", this.chkAutoIncrement));
		return panel;
	}

	/**
	 *
	 */
	protected void handleCancel() {
		SwingUtility.closePanelWindow(this);
	}

	/**
	 *
	 */
	protected void handleSave() {
		viewToModel();
		SwingUtility.closePanelWindow(this);
	}

	/**
	 *
	 */
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		if (this.fieldMeta instanceof IdFieldMeta) {
			add(getIdFieldPanel());
		}
		add(new JKLabledComponent("Name", this.txtName));
		add(new JKLabledComponent("Caption", this.txtCaption));
		add(new JKLabledComponent("Max length", this.txtMaxLength));
		add(new JKLabledComponent("Type", this.txtType));
		add(new JKLabledComponent("UI Width", this.txtWidth));
		add(new JKLabledComponent("Default Value", this.txtDefaultValue));
		add(new JKLabledComponent("Allow update", this.chkAllowUpdate));
		add(new JKLabledComponent("Confirm User Input", this.chkCofirmInput));

		add(new JKLabledComponent("Required", this.chkRequired));
		add(new JKLabledComponent("Visible", this.chkVisible));
		add(new JKLabledComponent("Enabled", this.chkEnabled));
		add(new JKLabledComponent("Summary Field", this.chkSummaryFild));
		if (this.fieldMeta instanceof ForiegnKeyFieldMeta) {
			add(getForiegnKeyPanel());
		}
		final JKPanel pnlButtons = new JKPanel();
		pnlButtons.add(this.btnSave);
		pnlButtons.add(this.btnCancel);

		add(pnlButtons);
		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				// TODO Auto-generated method stub
				handleSave();
			}
		});
		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleCancel();
			}
		});
	}

	/**
	 *
	 * @param tableName
	 */
	private void loadTableFields(final String tableName) {
		this.cmbReferenceField.removeAllItems();
		final TableMeta meta = this.tablesHash.get(tableName);
		for (int i = 0; i < meta.getFieldList().size(); i++) {
			this.cmbReferenceField.addItem(meta.getFieldList().get(i).getName());
		}
	}

	/**
	 *
	 */
	private void modelToView() {
		if (this.fieldMeta instanceof IdFieldMeta) {
			this.chkAutoIncrement.setSelected(((IdFieldMeta) this.fieldMeta).isAutoIncrement());
		}
		this.txtMaxLength.setText(this.fieldMeta.getMaxLength() + "");
		this.txtName.setText(this.fieldMeta.getName());
		this.txtType.setText(this.fieldMeta.getType() + "");
		this.txtWidth.setText(this.fieldMeta.getWidth() + "");
		this.chkAllowUpdate.setSelected(this.fieldMeta.isAllowUpdate());
		this.chkCofirmInput.setSelected(this.fieldMeta.isConfirmInput());
		this.chkRequired.setSelected(this.fieldMeta.isRequired());
		this.chkVisible.setSelected(this.fieldMeta.isVisible());
		this.chkEnabled.setSelected(this.fieldMeta.isEnabled());
		this.chkSummaryFild.setSelected(this.fieldMeta.isSummaryField());
		this.txtDefaultValue.setText(this.fieldMeta.getDefaultValue());
		if (this.fieldMeta instanceof ForiegnKeyFieldMeta) {
			final ForiegnKeyFieldMeta fk = (ForiegnKeyFieldMeta) this.fieldMeta;
			this.cmbReferenceTable.setSelectedItem(fk.getReferenceTable());
			loadTableFields(fk.getReferenceTable());
			this.cmbReferenceField.setSelectedItem(fk.getReferenceField());
			this.cmbReleation.setSelectedItem(fk.getRelation());
			this.cmbViewMode.setSelectedItem(fk.getViewMode());
		}
		if (this.fieldMeta.getCaption() != null) {
			this.txtCaption.setText(this.fieldMeta.getCaption());
		}

	}

	/**
	 *
	 */
	private void viewToModel() {
		if (this.fieldMeta instanceof IdFieldMeta) {
			((IdFieldMeta) this.fieldMeta).setAutoIncrement(this.chkAutoIncrement.isSelected());
		}
		this.fieldMeta.setMaxLength(Integer.parseInt(this.txtMaxLength.getText()));
		this.fieldMeta.setType(Integer.parseInt(this.txtType.getText()));
		this.fieldMeta.setWidth(Integer.parseInt(this.txtWidth.getText()));
		this.fieldMeta.setAllowUpdate(this.chkAllowUpdate.isSelected());
		this.fieldMeta.setConfirmInput(this.chkCofirmInput.isSelected());
		this.fieldMeta.setRequired(this.chkRequired.isSelected());
		this.fieldMeta.setVisible(this.chkVisible.isSelected());
		this.fieldMeta.setEnabled(this.chkEnabled.isSelected());
		this.fieldMeta.setDefaultValue(this.txtDefaultValue.getText().trim());
		if (this.fieldMeta instanceof ForiegnKeyFieldMeta) {
			final ForiegnKeyFieldMeta fk = (ForiegnKeyFieldMeta) this.fieldMeta;
			fk.setRelation((Relation) this.cmbReleation.getSelectedItem());
			fk.setViewMode((ViewMode) this.cmbViewMode.getSelectedItem());
		}
		this.fieldMeta.setCaption(this.txtCaption.getText());
		this.fieldMeta.setSummaryField(this.chkSummaryFild.isSelected());
	}
}
