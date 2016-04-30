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

	private final FSTableModel model;

	Vector<JKCheckBox> lstChk = new Vector<JKCheckBox>();

	JKCheckBox chkAll = new JKCheckBox("ALL");

	// ///////////////////////////////////////////////////////////////
	public PnlQueryFields(final FSTableModel model) {
		this.model = model;
		init();
		checkIfAllSelected();
	}

	// //////////////////////////////////////////////////////////////////
	private void checkIfAllSelected() {
		boolean allVisible = true;
		for (int i = 0; i < this.model.getActualColumnCount(); i++) {
			final FSTableColumn tableColumn = this.model.getTableColumn(i, false);
			if (!tableColumn.isVisible()) {
				allVisible = false;
			}
		}
		this.chkAll.setSelected(allVisible);
	}
	// ///////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////////
	private JKCheckBox createCheckBox(final int colunmIndex) {
		final FSTableColumn tableColumn = this.model.getTableColumn(colunmIndex, false);
		final JKCheckBox chk = new JKCheckBox(tableColumn.getName());
		chk.setSelected(tableColumn.isVisible());
		this.lstChk.add(chk);
		chk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleCheckBoxChecked(colunmIndex);
			}
		});
		return chk;
	}

	// ////////////////////////////////////////////////////////////////////////
	protected void handleCheckBoxChecked(final int colunmIndex) {
		final boolean value = this.lstChk.get(colunmIndex).isSelected();
		this.model.setVisibleByActualIndex(colunmIndex, value);
		checkIfAllSelected();
	}

	// //////////////////////////////////////////////////////////////////
	void handleToggleAll() {
		final boolean value = this.chkAll.isSelected();
		for (int i = 0; i < this.lstChk.size(); i++) {
			this.lstChk.get(i).setSelected(value);
			handleCheckBoxChecked(i);
		}
	}

	// ///////////////////////////////////////////////////////////////
	private void init() {
		// setPreferredSize(new Dimension(200,600));
		final JKPanel pnl = new JKPanel();
		// pnl.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		pnl.setLayout(new GridLayout(this.model.getActualColumnCount() + 1, 1));
		pnl.add(this.chkAll);
		for (int i = 0; i < this.model.getActualColumnCount(); i++) {
			final JKCheckBox chkBox = createCheckBox(i);
			if (i % 2 == 0) {
				chkBox.setBackground(Colors.JK_LABEL_BG);
			}
			pnl.add(new JKLabledComponent(i + 2 + " ", 25, chkBox));
		}
		final JScrollPane scroll = new JScrollPane(pnl);
		scroll.setPreferredSize(new Dimension(250, 600));
		add(scroll);
		this.chkAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleToggleAll();
				// System.out.println("Hayne");
			}
		});
	}
}
