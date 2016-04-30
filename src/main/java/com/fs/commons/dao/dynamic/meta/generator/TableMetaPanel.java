package com.fs.commons.dao.dynamic.meta.generator;

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

public class TableMetaPanel extends JKPanel<Object> {

	
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

	private final JKPanel<?> pnlConstraints = new JKPanel<Object>();

	JList<String> lstFields = new JList<String>();

	Hashtable<String, TableMeta> tablesHash;

	JKCheckBox chkAllowManage = new JKCheckBox("Allow Manage");

	JKTextField txtCaption = new JKTextField(20);

	JKTextField txtIconName = new JKTextField(20);

	JKTextField txtPageRowCount = new JKTextField(20);

	JKTextField txtFiltersPanel = new JKTextField(20);

	JKTextField txtPanelClass = new JKTextField(50);
	JKTextField txtUIColnmCount = new JKTextField(50);
	JKCheckBox rdAllowAdd=new JKCheckBox("Allow Add");
	JKCheckBox rdAllowUpdate=new JKCheckBox("Allow Update");
	JKCheckBox rdAllowDelete=new JKCheckBox("Allow Delete");
	

	JKButton btnSave = new JKButton("Save");
	JKButton btnCancel = new JKButton("Cancel");
	
	JKButton btnTriggers = new JKButton("triggers");
	JKButton btnAddConstraint = new JKButton("Add Constraint");

	/**
	 * 
	 * @param meta
	 * @param tablesHash
	 */
	public TableMetaPanel(TableMeta meta, Hashtable<String, TableMeta> tablesHash) {
		this.meta = meta;
		this.tablesHash = tablesHash;
		modelToView();
		init();
	}

	/**
	 * 
	 */
	private void modelToView() {
		txtTableId.setText(meta.getTableId());
		txtReportSql.setText(meta.getReportSql());
		txtListSql.setText(meta.getListSql());
		txtShortSql.setText(meta.getShortReportSql());
		txtMaxRecordCount.setText(meta.getMaxRecordsCount() + "");
		DefaultListModel<String> model = new DefaultListModel<String>();
		lstFields.setModel(model);
		model.addElement(meta.getIdField().getName());
		for (int i = 0; i < meta.getFieldList().size(); i++) {
			model.addElement(meta.getFieldList().get(i).getName());
		}
		chkAllowManage.setSelected(meta.isAllowManage());
		if (meta.getIconName() != null) {
			txtIconName.setText(meta.getIconName());
		}
		txtCaption.setText(meta.getCaption());
		txtPageRowCount.setText(meta.getPageRowCount() + "");
		txtFiltersPanel.setText(meta.getFiltersAsString());
		txtPanelClass.setText(meta.getPanelClassName());
		txtUIColnmCount.setText(meta.getDefaultUIRowCount()+"");
		rdAllowAdd.setSelected(meta.isAllowAdd());
		rdAllowDelete.setSelected(meta.isAllowDelete());
		rdAllowUpdate.setSelected(meta.isAllowUpdate());
	}

	/**
	 * 
	 */
	private void viewToModel() {
		meta.setTableId(txtTableId.getText().trim());
		meta.setReportSql(txtReportSql.getText().trim());
		meta.setListSql(txtListSql.getText().trim());
		meta.setShortReportSql(txtShortSql.getText().trim());
		try {
			meta.setMaxRecordsCount(Integer.parseInt(txtMaxRecordCount.getText()));
		} catch (NumberFormatException e) {
			// for empty string
		}
		meta.setAllowManage(chkAllowManage.isSelected());
		if (!txtIconName.getText().equals("")) {
			meta.setIconName(txtIconName.getText());
		} else {
			meta.setIconName(null);
		}
		meta.setCaption(txtCaption.getText());
		if (!txtPageRowCount.getText().equals("")) {
			meta.setPageRowCount(Integer.parseInt(txtPageRowCount.getText()));
		}
		if (!txtFiltersPanel.getText().equals("")) {
			meta.setFilters(txtFiltersPanel.getText().split(","));
		}
		if(!txtUIColnmCount.getText().trim().equals("")){
			meta.setDefaultUIRowCount(txtUIColnmCount.getTextAsInteger());
		}
		meta.setPanelClassName(txtPanelClass.getText().trim());
		
		meta.setAllowAdd(rdAllowAdd.isSelected());
		meta.setAllowDelete(rdAllowDelete.isSelected());
		meta.setAllowUpdate(rdAllowUpdate.isSelected());
	}

	/**
	 * 
	 */
	private void init() {
		setLayout(new BorderLayout());
		JKPanel<?> pnlMain = new JKPanel<Object>();
		pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
		JScrollPane fieldsPane = new JScrollPane(lstFields, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane reportPane = new JScrollPane(txtReportSql, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane txtListPane = new JScrollPane(txtListSql, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane txtShortPane = new JScrollPane(txtShortSql, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		fieldsPane.setBorder(BorderFactory.createTitledBorder("Fields list"));
		reportPane.setBorder(BorderFactory.createTitledBorder("Detailed Report SQL"));
		txtListPane.setBorder(BorderFactory.createTitledBorder("Combo Box SQL"));
		txtShortPane.setBorder(BorderFactory.createTitledBorder("Basic Info SQL"));

		JKPanel<?> pnlButtons = getButtonsPanel();
		
		pnlMain.add(new JKLabledComponent("Table Id", txtTableId));
		pnlMain.add(new JKLabledComponent("Filter fields", txtFiltersPanel));
		pnlMain.add(new JKLabledComponent("Caption", txtCaption));
		pnlMain.add(new JKLabledComponent("Max Record Count ", txtMaxRecordCount));
		pnlMain.add(new JKLabledComponent("Icon name", txtIconName));
		pnlMain.add(new JKLabledComponent("Page Count", txtPageRowCount));
		pnlMain.add(new JKLabledComponent("Panel class", txtPanelClass));
		pnlMain.add(new JKLabledComponent("UI-Column-Count",txtUIColnmCount));
		
		JKPanel<?> pnlCheckBoxes=new JKPanel<Object>();
		pnlCheckBoxes.add(chkAllowManage);
		pnlCheckBoxes.add(rdAllowAdd);
		pnlCheckBoxes.add(rdAllowUpdate);
		pnlCheckBoxes.add(rdAllowDelete);

		pnlMain.add(pnlCheckBoxes);
		
		pnlMain.add(fieldsPane);
		pnlMain.add(reportPane);
		pnlMain.add(txtListPane);
		pnlMain.add(txtShortPane);

		pnlMain.add(pnlButtons);

		add(pnlMain, BorderLayout.CENTER);
		add(new JScrollPane(getConstraintsPanel()), BorderLayout.EAST);

		btnTriggers.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			   PnlTriggers pnl=new PnlTriggers(meta.getTriggerNames());
			   SwingUtility.showPanelInDialog(pnl, "triggers");
			}
		});
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewToModel();
				SwingUtility.closePanelDialog(TableMetaPanel.this);
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtility.closePanelDialog(TableMetaPanel.this);
			}
		});
		btnAddConstraint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConstraintPanel pnl = new ConstraintPanel(meta);
				SwingUtility.showPanelInDialog(pnl, "Add Constraint");
				pnlConstraints.removeAll();
				getConstraintsPanel();
				pnlConstraints.validate();
				pnlConstraints.repaint();
				SwingUtility.packWindow(TableMetaPanel.this);
			}
		});
		lstFields.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 1) {
					showSelectedField();
				}
			}
		});
		lstFields.addKeyListener(new KeyAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					showSelectedField();
				}
			}
		});
	}

	private JKPanel<?> getButtonsPanel() {
		JKPanel<?> pnlButtons = new JKPanel<Object>();
		pnlButtons.add(btnTriggers);
		pnlButtons.add(btnSave);
		pnlButtons.add(btnCancel);
		return pnlButtons;
	}

	/**
	 * 
	 */
	protected void showSelectedField() {
		int index = lstFields.getSelectedIndex();
		if (index != -1) {
			FieldMeta field = meta.getField((String) lstFields.getSelectedValue(), true);
			FieldPanel panel = new FieldPanel(field, tablesHash);
			SwingUtility.showPanelInDialog(panel, "");
		}
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel<?> getConstraintsPanel() {
		for (int i = 0; i < meta.getConstraints().size(); i++) {
			constraintsPanels.add(new ConstraintPanel(meta, meta.getConstraints().get(i)));
		}
		pnlConstraints.setLayout(new BoxLayout(pnlConstraints, BoxLayout.Y_AXIS));
		pnlConstraints.add(btnAddConstraint);
		for (int i = 0; i < constraintsPanels.size(); i++) {
			pnlConstraints.add(constraintsPanels.get(i));
		}
		return pnlConstraints;
	}

}
