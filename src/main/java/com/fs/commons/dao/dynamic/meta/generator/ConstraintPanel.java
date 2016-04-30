package com.fs.commons.dao.dynamic.meta.generator;

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

public class ConstraintPanel extends JKPanel<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	enum CONSTRAINT_TYPES {
		NO_DUPLICATE, DATA_RANGE, NO_IDENTICAL_FIELDS, LESS_THAN;
		@Override
		public String toString() {
			if (this == NO_DUPLICATE)
				return "No Duplicate";
			if (this == DATA_RANGE)
				return "Data Range";
			if (this == NO_IDENTICAL_FIELDS)
				return "No Identical Fields";
			if (this == LESS_THAN)
				return "Less Than";
			return "";
		}
	}

	private TableMeta tableMeta;

	private Constraint constraint;

	JKTextField txtName = new JKTextField(20);

	JKComboBox cmbType = new JKComboBox();

	JList<String> lstFields = new JList<String>();

	private DefaultListModel<String> fieldsModel;

	JKButton btnSave = new JKButton("Save");

	JKButton btnAdd = new JKButton("Add");

	JKButton btnDelete = new JKButton("Delete");

	// used for range constraint
	private JKTextField txtValueFrom = new JKTextField(new FloatDocument(5), 20);

	private JKTextField txtValueTo = new JKTextField(new FloatDocument(5), 20);

	public ConstraintPanel(TableMeta tableMeta) {
		init(tableMeta, null);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
	}

	/**
	 * 
	 * @param tableMeta
	 * @param constraint
	 */
	public ConstraintPanel(TableMeta tableMeta, Constraint constraint) {
		init(tableMeta, constraint);
		cmbType.setEnabled(false);
		btnAdd.setVisible(false);
		modelToView();
	}

	private void init(TableMeta tableMeta, Constraint constraint) {
		this.tableMeta = tableMeta;
		this.constraint = constraint;
		cmbType.addItem(CONSTRAINT_TYPES.NO_DUPLICATE);
		cmbType.addItem(CONSTRAINT_TYPES.NO_IDENTICAL_FIELDS);
		cmbType.addItem(CONSTRAINT_TYPES.DATA_RANGE);
		cmbType.addItem(CONSTRAINT_TYPES.LESS_THAN);
		fieldsModel = new DefaultListModel<String>();
		for (int i = 0; i < tableMeta.getFieldList().size(); i++) {
			fieldsModel.addElement(tableMeta.getFieldList().get(i).getName());
		}
		lstFields.setModel(fieldsModel);
		init();
	}

	private void modelToView() {
		if (constraint instanceof DuplicateDataConstraint) {
			cmbType.setSelectedItem(CONSTRAINT_TYPES.NO_DUPLICATE);
		} else if (constraint instanceof IdenticalFieldsContraint) {
			cmbType.setSelectedItem(CONSTRAINT_TYPES.NO_IDENTICAL_FIELDS);
		} else if (constraint instanceof LessThanContsraint) {
			cmbType.setSelectedItem(CONSTRAINT_TYPES.LESS_THAN);
		} else if (constraint instanceof DataRangeConstraint) {
			cmbType.setSelectedItem(CONSTRAINT_TYPES.DATA_RANGE);
			txtValueFrom.setText(((DataRangeConstraint) constraint).getValueFrom() + "");
			txtValueTo.setText(((DataRangeConstraint) constraint).getValueTo() + "");
		}
		txtName.setText(constraint.getName());
		for (int i = 0; i < constraint.getFields().size(); i++) {
			int index = fieldsModel.indexOf(constraint.getFields().get(i).getName());
			lstFields.addSelectionInterval(index, index);
		}
	}

	void viewToModel() {
		constraint.getFields().clear();
		int[] selecredFields = lstFields.getSelectedIndices();
		for (int i = 0; i < selecredFields.length; i++) {
			constraint.addField(tableMeta.getField((String) fieldsModel.get(selecredFields[i])));
		}
		constraint.setName(txtName.getText());
		if (constraint instanceof DataRangeConstraint) {
			((DataRangeConstraint) constraint).setValueFrom(Float.parseFloat(txtValueFrom.getText()));
			((DataRangeConstraint) constraint).setValueTo(Float.parseFloat(txtValueTo.getText()));
		}
	}

	private void init() {
		JKPanel<?> pnl = new JKPanel<Object>(new GridLayout(1, 2));
		JKPanel<?> pnlInfo = new JKPanel<Object>();
		pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
		pnlInfo.setBorder(BorderFactory.createTitledBorder("Constraint info"));
		pnlInfo.add(new JKLabledComponent("Name ", txtName));
		pnlInfo.add(new JKLabledComponent("Type ", cmbType));
		pnlInfo.add(new JKLabledComponent("Value from", txtValueFrom));
		pnlInfo.add(new JKLabledComponent("Value To", txtValueTo));

		JKPanel<?> pnlFields = new JKPanel<Object>(new BorderLayout());
		pnlFields.setBorder(BorderFactory.createTitledBorder("Fields list"));
		pnlFields.add(new JScrollPane(lstFields), BorderLayout.CENTER);
		JKPanel<?> pnlButtons = new JKPanel<Object>();
		pnlButtons.add(btnAdd);
		pnlButtons.add(btnSave);
		pnlButtons.add(btnDelete);
		pnlFields.add(pnlButtons, BorderLayout.SOUTH);

		pnl.add(pnlInfo);
		pnl.add(pnlFields);

		add(pnl);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewToModel();
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAdd();
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableMeta.getConstraints().remove(constraint);
				closePanel();
			}
		});
	}

	protected void closePanel() {
		Container cont=getParent();
		cont.remove(this);
		cont.validate();
		cont.repaint();
	}

	protected void handleAdd() {
		Constraint instance;
		CONSTRAINT_TYPES constType = (CONSTRAINT_TYPES) cmbType.getSelectedItem();
		try {
			if (constType.equals(CONSTRAINT_TYPES.NO_DUPLICATE)) {
				instance = DuplicateDataConstraint.class.newInstance();
			} else if (constType.equals(CONSTRAINT_TYPES.DATA_RANGE)) {
				DataRangeConstraint cons = DataRangeConstraint.class.newInstance();
				cons.setValueFrom(Float.parseFloat(txtValueFrom.getText()));
				cons.setValueTo(Float.parseFloat(txtValueTo.getText()));
				instance = cons;
			} else if (constType.equals(CONSTRAINT_TYPES.LESS_THAN)) {
				LessThanContsraint cons = LessThanContsraint.class.newInstance();
				instance = cons;
			} else if (constType.equals(CONSTRAINT_TYPES.NO_IDENTICAL_FIELDS)) {
				IdenticalFieldsContraint cons = IdenticalFieldsContraint.class.newInstance();
				instance = cons;
			} else {
				instance = Constraint.class.newInstance();
			}
			instance.setTableMeta(tableMeta);
			tableMeta.getConstraints().add(instance);
			constraint = instance;
			viewToModel();
			SwingUtility.closePanelDialog(this);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}
}
