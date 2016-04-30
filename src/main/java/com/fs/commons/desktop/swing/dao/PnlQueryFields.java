package com.fs.commons.desktop.swing.dao;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JScrollPane;

import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.comp.JKCheckBox;
import com.fs.commons.desktop.swing.comp.model.FSTableColumn;
import com.fs.commons.desktop.swing.comp.model.FSTableModel;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class PnlQueryFields extends JKPanel {
	private static final long serialVersionUID = 1L;

	private FSTableModel model;

	Vector<JKCheckBox> lstChk = new Vector<JKCheckBox>();

	JKCheckBox chkAll = new JKCheckBox("ALL");

	// ///////////////////////////////////////////////////////////////
	public PnlQueryFields(FSTableModel model) {
		this.model = model;
		init();
		checkIfAllSelected();
	}

	// ///////////////////////////////////////////////////////////////
	private void init() {
//		setPreferredSize(new Dimension(200,600));
		JKPanel pnl=new JKPanel();
//		pnl.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		pnl.setLayout(new GridLayout(model.getActualColumnCount()+1,1));
		pnl.add(chkAll);
		for (int i = 0; i < model.getActualColumnCount(); i++) {
			JKCheckBox chkBox = createCheckBox(i);
			if (i % 2 == 0) {
				chkBox.setBackground(Colors.JK_LABEL_BG);
			}
			pnl.add(new JKLabledComponent((i+2)+" ",25, chkBox));
		}
		JScrollPane scroll = new JScrollPane( pnl);
		scroll.setPreferredSize(new Dimension(250,600));
		add(scroll);
		chkAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleToggleAll();
				// System.out.println("Hayne");
			}
		});
	}

	// ////////////////////////////////////////////////////////////////////////
	private JKCheckBox createCheckBox(final int colunmIndex) {
		final FSTableColumn tableColumn = model.getTableColumn(colunmIndex, false);
		final JKCheckBox chk = new JKCheckBox(tableColumn.getName());
		chk.setSelected(tableColumn.isVisible());
		this.lstChk.add(chk);
		chk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleCheckBoxChecked(colunmIndex);
			}
		});
		return chk;
	}

	// ////////////////////////////////////////////////////////////////////////
	protected void handleCheckBoxChecked(int colunmIndex) {
		boolean value = lstChk.get(colunmIndex).isSelected();
		model.setVisibleByActualIndex(colunmIndex, value);
		checkIfAllSelected();
	}

	// //////////////////////////////////////////////////////////////////
	void handleToggleAll() {
		boolean value = chkAll.isSelected();
		for (int i = 0; i < lstChk.size(); i++) {
			lstChk.get(i).setSelected(value);
			handleCheckBoxChecked(i);
		}
	}

	// //////////////////////////////////////////////////////////////////
	private void checkIfAllSelected() {
		boolean allVisible = true;
		for (int i = 0; i < model.getActualColumnCount(); i++) {
			FSTableColumn tableColumn = model.getTableColumn(i, false);
			if (!tableColumn.isVisible()) {
				allVisible = false;
			}
		}
		chkAll.setSelected(allVisible);
	}
	// ///////////////////////////////////////////////////////////////
}
