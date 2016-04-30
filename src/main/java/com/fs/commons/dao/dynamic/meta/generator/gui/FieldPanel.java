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

	JKTextField txtDefaultValue=new JKTextField(20);
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
	public FieldPanel(FieldMeta fieldMeta, Hashtable<String, TableMeta> tablesHash) {
		this.fieldMeta = fieldMeta;
		this.tablesHash = tablesHash;
		cmbReleation.addItem(Relation.ONE_TO_ONE);
		cmbReleation.addItem(Relation.ONE_TO_MANY);
		cmbReleation.addItem(Relation.MANY_TO_ONE);
		cmbReleation.addItem(Relation.MANY_TO_MANY);
		cmbReleation.addItem(Relation.ONE_TO_ONE);

		cmbViewMode.addItem(ViewMode.COMBO);
		cmbViewMode.addItem(ViewMode.LIST);
		cmbViewMode.addItem(ViewMode.DIALOG);
		cmbViewMode.addItem(ViewMode.LOOKUP);

		Enumeration enu = tablesHash.keys();
		while (enu.hasMoreElements()) {
			TableMeta tableMeta = tablesHash.get(enu.nextElement());
			cmbReferenceTable.addItem(tableMeta.getTableId());
		}
		init();
		txtName.setEnabled(false);
		cmbReferenceField.setEnabled(false);
		cmbReferenceTable.setEnabled(false);

		modelToView();
	}

	/**
	 * 
	 */
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		if (fieldMeta instanceof IdFieldMeta) {
			add(getIdFieldPanel());
		}
		add(new JKLabledComponent("Name", txtName));
		add(new JKLabledComponent("Caption", txtCaption));
		add(new JKLabledComponent("Max length", txtMaxLength));
		add(new JKLabledComponent("Type", txtType));
		add(new JKLabledComponent("UI Width", txtWidth));
		add(new JKLabledComponent("Default Value", txtDefaultValue));
		add(new JKLabledComponent("Allow update", chkAllowUpdate));
		add(new JKLabledComponent("Confirm User Input", chkCofirmInput));

		add(new JKLabledComponent("Required", chkRequired));
		add(new JKLabledComponent("Visible", chkVisible));
		add(new JKLabledComponent("Enabled", chkEnabled));
		add(new JKLabledComponent("Summary Field", chkSummaryFild));
		if (fieldMeta instanceof ForiegnKeyFieldMeta) {
			add(getForiegnKeyPanel());
		}
		JKPanel pnlButtons = new JKPanel();
		pnlButtons.add(btnSave);
		pnlButtons.add(btnCancel);

		add(pnlButtons);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				handleSave();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleCancel();
			}
		});
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
	private void viewToModel() {
		if (fieldMeta instanceof IdFieldMeta) {
			((IdFieldMeta) fieldMeta).setAutoIncrement(chkAutoIncrement.isSelected());
		}
		fieldMeta.setMaxLength(Integer.parseInt(txtMaxLength.getText()));
		fieldMeta.setType(Integer.parseInt(txtType.getText()));
		fieldMeta.setWidth(Integer.parseInt(txtWidth.getText()));
		fieldMeta.setAllowUpdate(chkAllowUpdate.isSelected());
		fieldMeta.setConfirmInput(chkCofirmInput.isSelected());
		fieldMeta.setRequired(chkRequired.isSelected());
		fieldMeta.setVisible(chkVisible.isSelected());
		fieldMeta.setEnabled(chkEnabled.isSelected());
		fieldMeta.setDefaultValue(txtDefaultValue.getText().trim());
		if (fieldMeta instanceof ForiegnKeyFieldMeta) {
			ForiegnKeyFieldMeta fk = (ForiegnKeyFieldMeta) fieldMeta;
			fk.setRelation((Relation) cmbReleation.getSelectedItem());
			fk.setViewMode((ViewMode) cmbViewMode.getSelectedItem());
		}
		fieldMeta.setCaption(txtCaption.getText());
		fieldMeta.setSummaryField(chkSummaryFild.isSelected());
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel getIdFieldPanel() {
		JKPanel panel = new JKPanel();
		panel.add(new JKLabledComponent("Auto increment", chkAutoIncrement));
		return panel;
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel getForiegnKeyPanel() {
		JKPanel panel = new JKPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JKLabledComponent("Reference table", cmbReferenceTable));
		panel.add(new JKLabledComponent("Reference field", cmbReferenceField));
		panel.add(new JKLabledComponent("Releation", cmbReleation));
		panel.add(new JKLabledComponent("View Mode", cmbViewMode));
		return panel;
	}

	/**
	 * 
	 */
	private void modelToView() {
		if (fieldMeta instanceof IdFieldMeta) {
			chkAutoIncrement.setSelected(((IdFieldMeta) fieldMeta).isAutoIncrement());
		}
		txtMaxLength.setText(fieldMeta.getMaxLength() + "");
		txtName.setText(fieldMeta.getName());
		txtType.setText(fieldMeta.getType() + "");
		txtWidth.setText(fieldMeta.getWidth() + "");
		chkAllowUpdate.setSelected(fieldMeta.isAllowUpdate());
		chkCofirmInput.setSelected(fieldMeta.isConfirmInput());
		chkRequired.setSelected(fieldMeta.isRequired());
		chkVisible.setSelected(fieldMeta.isVisible());
		chkEnabled.setSelected(fieldMeta.isEnabled());
		chkSummaryFild.setSelected(fieldMeta.isSummaryField());
		txtDefaultValue.setText(fieldMeta.getDefaultValue());
		if (fieldMeta instanceof ForiegnKeyFieldMeta) {
			ForiegnKeyFieldMeta fk = (ForiegnKeyFieldMeta) fieldMeta;
			cmbReferenceTable.setSelectedItem(fk.getReferenceTable());
			loadTableFields(fk.getReferenceTable());
			cmbReferenceField.setSelectedItem(fk.getReferenceField());
			cmbReleation.setSelectedItem(fk.getRelation());
			cmbViewMode.setSelectedItem(fk.getViewMode());
		}
		if (fieldMeta.getCaption() != null) {
			txtCaption.setText(fieldMeta.getCaption());
		}

	}

	/**
	 * 
	 * @param tableName
	 */
	private void loadTableFields(String tableName) {
		cmbReferenceField.removeAllItems();
		TableMeta meta = tablesHash.get(tableName);
		for (int i = 0; i < meta.getFieldList().size(); i++) {
			cmbReferenceField.addItem(meta.getFieldList().get(i).getName());
		}
	}
}
