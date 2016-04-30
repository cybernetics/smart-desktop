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
package com.fs.commons.desktop.dynform.ui.masterdetail;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTabbedPane;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class DynMasterDetailPanel extends AbstractMasterDetail {

	/**
	 *
	 */
	private static final long serialVersionUID = 5865900742114738634L;

	private final JKTabbedPane pnlTabbedPane = new JKTabbedPane();

	private final JKButton btnNext = new JKButton("Next");

	private final JKButton btnPre = new JKButton("Previouse");

	private final JKButton btnClose = new JKButton("Close");

	private JKPanel pnlButtons;

	// /////////////////////////////////////////////////////////////////
	/**
	 * This method with empty constructor for reflection purposes
	 */
	public DynMasterDetailPanel() {
		super();
	}

	public DynMasterDetailPanel(final String tableMetaName) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		this(AbstractTableMetaFactory.getTableMeta(tableMetaName));
	}

	// /////////////////////////////////////////////////////////////////
	public DynMasterDetailPanel(final TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		super(tableMeta);
	}

	@Override
	public void addComponent(final JComponent comp) {
		getButtonsPanel().add(comp);
	}

	/**
	 *
	 * @param title
	 * @param icon
	 * @param pnl
	 */
	@Override
	protected void addPanelToView(final String title, final String icon, final JKPanel pnl) {
		this.pnlTabbedPane.addTab(title, icon, pnl);
	}

	/**
	 * I made public , to give the caller the ability to add extra components
	 * for this panel
	 *
	 * @return
	 */
	protected JKPanel getButtonsPanel() {
		if (this.pnlButtons == null) {
			this.pnlButtons = new JKPanel();
			// if (pnlTabbedPane.getTabCount() > 1) {
			// SwingUtility.setHotKeyFoButton(SwingUtility.isLeftOrientation() ?
			// btnNext : btnPre, "ctrl RIGHT", "NEXT");
			this.btnNext.setShortcut(SwingUtility.isLeftOrientation() ? "ctrl RIGHT" : "ctrl LEFT",
					SwingUtility.isLeftOrientation() ? "ctrl ->" : "ctr <-");
			this.btnPre.setShortcut(SwingUtility.isLeftOrientation() ? "ctrl LEFT" : "ctrl RIGHT",
					SwingUtility.isLeftOrientation() ? "ctrl <-" : "ctr ->");
			// SwingUtility.setHotKeyFoButton(SwingUtility.isLeftOrientation() ?
			// btnPre : btnNext, "ctrl LEFT", "PRE");
			this.btnNext.setIcon(SwingUtility.isLeftOrientation() ? "next2.gif" : "previos2.gif");
			this.btnPre.setIcon(SwingUtility.isLeftOrientation() ? "previos2.gif" : "next2.gif");
			this.btnClose.setIcon("fileclose.png");
			this.pnlButtons.add(this.btnPre);
			this.pnlButtons.add(this.btnNext);
			// pnlButtons.add(btnClose);
			// }
		}
		return this.pnlButtons;
	}

	/**
	 *
	 * @return
	 */
	@Override
	protected int getCurrentPanelIndex() {
		return this.pnlTabbedPane.getSelectedIndex();
	}

	@Override
	protected void initUI() {
		setLayout(new BorderLayout());
		final JKPanel container = new JKPanel();
		container.setLayout(new BorderLayout());
		// addPanelsToContainer();

		container.add(this.pnlTabbedPane, BorderLayout.CENTER);
		container.add(getButtonsPanel(), BorderLayout.SOUTH);

		add(container);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(final ComponentEvent e) {
			}
		});

		this.pnlTabbedPane.addChangeListener(new ChangeListener() {
			// // This method is called whenever the selected tab changes
			@Override
			public void stateChanged(final ChangeEvent evt) {
				final JTabbedPane pane = (JTabbedPane) evt.getSource();
				final int sel = pane.getSelectedIndex();
				navigateToPanelAtIndex(sel);
			}
		});

		this.btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent e) {
				navigateToPanelAtIndex(getCurrentPanelIndex() + 1);
			};
		});
		this.btnPre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				navigateToPanelAtIndex(getCurrentPanelIndex() - 1);
			}
		});
		this.btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleClosePanel();
			}
		});
	}

	@Override
	protected void navigateToPanelAtIndex(int panelIndex) {
		if (panelIndex < 0 || panelIndex == 0 || panelIndex >= this.pnlTabbedPane.getTabCount()) {
			panelIndex = 0;
		}
		this.pnlTabbedPane.setSelectedIndex(panelIndex);
		this.btnClose.setVisible(getCurrentPanelIndex() > 0);// since the
		// master
		this.btnPre.setEnabled(getCurrentPanelIndex() > 0);
		this.btnNext.setEnabled(getCurrentPanelIndex() + 1 != this.pnlTabbedPane.getTabCount());
	}
}
