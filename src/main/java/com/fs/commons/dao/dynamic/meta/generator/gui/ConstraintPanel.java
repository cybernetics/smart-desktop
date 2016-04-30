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
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import com.fs.commons.dao.dynamic.constraints.Constraint;
import com.fs.commons.dao.dynamic.constraints.DataRangeConstraint;
import com.fs.commons.dao.dynamic.constraints.DuplicateDataConstraint;
import com.fs.commons.dao.dynamic.constraints.IdenticalFieldsContraint;
import com.fs.commons.dao.dynamic.constraints.LessThanContsraint;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKComboBox;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.documents.FloatDocument;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class ConstraintPanel extends JKPanel {
	enum CONSTRAINT_TYPES {
		NO_DUPLICATE, DATA_RANGE, NO_IDENTICAL_FIELDS, LESS_THAN;
		@Override
		public String toString() {
			if (this == NO_DUPLICATE) {
				return "No Duplicate";
			}
			if (this == DATA_RANGE) {
				return "Data Range";
			}
			if (this == NO_IDENTICAL_FIELDS) {
				return "No Identical Fields";
			}
			if (this == LESS_THAN) {
				return "Less Than";
			}
			return "";
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private TableMeta tableMeta;

	private Constraint constraint;

	JKTextField txtName = new JKTextField(20);

	JKComboBox cmbType = new JKComboBox();

	JList lstFields = new JList();

	private DefaultListModel fieldsModel;

	JKButton btnSave = new JKButton("Save");

	JKButton btnAdd = new JKButton("Add");

	JKButton btnDelete = new JKButton("Delete");

	// used for range constraint
	private final JKTextField txtValueFrom = new JKTextField(new FloatDocument(5), 20);

	private final JKTextField txtValueTo = new JKTextField(new FloatDocument(5), 20);

	public ConstraintPanel(final TableMeta tableMeta) {
		init(tableMeta, null);
		this.btnSave.setVisible(false);
		this.btnDelete.setVisible(false);
	}

	/**
	 *
	 * @param tableMeta
	 * @param constraint
	 */
	public ConstraintPanel(final TableMeta tableMeta, final Constraint constraint) {
		init(tableMeta, constraint);
		this.cmbType.setEnabled(false);
		this.btnAdd.setVisible(false);
		modelToView();
	}

	protected void closePanel() {
		final Container cont = getParent();
		cont.remove(this);
		cont.validate();
		cont.repaint();
	}

	protected void handleAdd() {
		Constraint instance;
		final CONSTRAINT_TYPES constType = (CONSTRAINT_TYPES) this.cmbType.getSelectedItem();
		try {
			if (constType.equals(CONSTRAINT_TYPES.NO_DUPLICATE)) {
				instance = DuplicateDataConstraint.class.newInstance();
			} else if (constType.equals(CONSTRAINT_TYPES.DATA_RANGE)) {
				final DataRangeConstraint cons = DataRangeConstraint.class.newInstance();
				cons.setValueFrom(Float.parseFloat(this.txtValueFrom.getText()));
				cons.setValueTo(Float.parseFloat(this.txtValueTo.getText()));
				instance = cons;
			} else if (constType.equals(CONSTRAINT_TYPES.LESS_THAN)) {
				final LessThanContsraint cons = LessThanContsraint.class.newInstance();
				instance = cons;
			} else if (constType.equals(CONSTRAINT_TYPES.NO_IDENTICAL_FIELDS)) {
				final IdenticalFieldsContraint cons = IdenticalFieldsContraint.class.newInstance();
				instance = cons;
			} else {
				instance = Constraint.class.newInstance();
			}
			instance.setTableMeta(this.tableMeta);
			this.tableMeta.getConstraints().add(instance);
			this.constraint = instance;
			viewToModel();
			SwingUtility.closePanelWindow(this);
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	private void init() {
		final JKPanel pnl = new JKPanel(new GridLayout(1, 2));
		final JKPanel pnlInfo = new JKPanel();
		pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
		pnlInfo.setBorder(BorderFactory.createTitledBorder("Constraint info"));
		pnlInfo.add(new JKLabledComponent("Name ", this.txtName));
		pnlInfo.add(new JKLabledComponent("Type ", this.cmbType));
		pnlInfo.add(new JKLabledComponent("Value from", this.txtValueFrom));
		pnlInfo.add(new JKLabledComponent("Value To", this.txtValueTo));

		final JKPanel pnlFields = new JKPanel(new BorderLayout());
		pnlFields.setBorder(BorderFactory.createTitledBorder("Fields list"));
		pnlFields.add(new JScrollPane(this.lstFields), BorderLayout.CENTER);
		final JKPanel pnlButtons = new JKPanel();
		pnlButtons.add(this.btnAdd);
		pnlButtons.add(this.btnSave);
		pnlButtons.add(this.btnDelete);
		pnlFields.add(pnlButtons, BorderLayout.SOUTH);

		pnl.add(pnlInfo);
		pnl.add(pnlFields);

		add(pnl);
		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				viewToModel();
			}
		});
		this.btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleAdd();
			}
		});
		this.btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				ConstraintPanel.this.tableMeta.getConstraints().remove(ConstraintPanel.this.constraint);
				closePanel();
			}
		});
	}

	private void init(final TableMeta tableMeta, final Constraint constraint) {
		this.tableMeta = tableMeta;
		this.constraint = constraint;
		this.cmbType.addItem(CONSTRAINT_TYPES.NO_DUPLICATE);
		this.cmbType.addItem(CONSTRAINT_TYPES.NO_IDENTICAL_FIELDS);
		this.cmbType.addItem(CONSTRAINT_TYPES.DATA_RANGE);
		this.cmbType.addItem(CONSTRAINT_TYPES.LESS_THAN);
		this.fieldsModel = new DefaultListModel();
		for (int i = 0; i < tableMeta.getFieldList().size(); i++) {
			this.fieldsModel.addElement(tableMeta.getFieldList().get(i).getName());
		}
		this.lstFields.setModel(this.fieldsModel);
		init();
	}

	private void modelToView() {
		if (this.constraint instanceof DuplicateDataConstraint) {
			this.cmbType.setSelectedItem(CONSTRAINT_TYPES.NO_DUPLICATE);
		} else if (this.constraint instanceof IdenticalFieldsContraint) {
			this.cmbType.setSelectedItem(CONSTRAINT_TYPES.NO_IDENTICAL_FIELDS);
		} else if (this.constraint instanceof LessThanContsraint) {
			this.cmbType.setSelectedItem(CONSTRAINT_TYPES.LESS_THAN);
		} else if (this.constraint instanceof DataRangeConstraint) {
			this.cmbType.setSelectedItem(CONSTRAINT_TYPES.DATA_RANGE);
			this.txtValueFrom.setText(((DataRangeConstraint) this.constraint).getValueFrom() + "");
			this.txtValueTo.setText(((DataRangeConstraint) this.constraint).getValueTo() + "");
		}
		this.txtName.setText(this.constraint.getName());
		for (int i = 0; i < this.constraint.getFields().size(); i++) {
			final int index = this.fieldsModel.indexOf(this.constraint.getFields().get(i).getName());
			this.lstFields.addSelectionInterval(index, index);
		}
	}

	void viewToModel() {
		this.constraint.getFields().clear();
		final int[] selecredFields = this.lstFields.getSelectedIndices();
		for (final int selecredField : selecredFields) {
			this.constraint.addField(this.tableMeta.getField((String) this.fieldsModel.get(selecredField)));
		}
		this.constraint.setName(this.txtName.getText());
		if (this.constraint instanceof DataRangeConstraint) {
			((DataRangeConstraint) this.constraint).setValueFrom(Float.parseFloat(this.txtValueFrom.getText()));
			((DataRangeConstraint) this.constraint).setValueTo(Float.parseFloat(this.txtValueTo.getText()));
		}
	}
}
