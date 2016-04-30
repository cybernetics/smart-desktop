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
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BoxLayout;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class PnlTriggers extends JKPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		final ArrayList<String> triggers = new ArrayList<String>();
		final PnlTriggers pnl = new PnlTriggers(triggers);
		SwingUtility.showPanelInDialog(pnl, "");
		System.out.println(triggers);
	}

	private final Collection<String> triggersNames;
	ArrayList<JKTextField> cmbTriggers = new ArrayList<JKTextField>();

	private JKPanel pnlTriggers;
	JKButton btnAdd = new JKButton("add");
	JKButton btnSave = new JKButton("save");

	JKButton btnClose = new JKButton("close");

	/**
	 *
	 * @param triggers
	 */
	public PnlTriggers(final Collection<String> triggers) {
		this.triggersNames = triggers;
		init();
	}

	/**
	 *
	 * @param name
	 */
	private void addTrigger(final String name) {
		final JKTextField txt = new JKTextField();
		txt.setText(name);
		this.cmbTriggers.add(txt);
		this.pnlTriggers.add(txt);
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getButtonsPanel() {
		final JKPanel pnlButtons = new JKPanel();
		pnlButtons.add(this.btnAdd);
		pnlButtons.add(this.btnSave);
		pnlButtons.add(this.btnClose);
		return pnlButtons;
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getTriggersPanel() {
		if (this.pnlTriggers == null) {
			this.pnlTriggers = new JKPanel();
			this.pnlTriggers.setLayout(new BoxLayout(this.pnlTriggers, BoxLayout.Y_AXIS));
			for (final String triggersName : this.triggersNames) {
				addTrigger(triggersName);
			}
		}
		return this.pnlTriggers;
	}

	/**
	 *
	 */
	protected void handleAdd() {
		final String triggerName = SwingUtility.showInputDialog("Enter trigger Name");
		addTrigger(triggerName);
		this.pnlTriggers.validate();
		this.pnlTriggers.repaint();
		SwingUtility.packWindow(this);
	}

	/**
	 *
	 */
	protected void handleClose() {
		SwingUtility.closePanelWindow(this);
	}

	/**
	 *
	 */
	protected void handleSave() {
		this.triggersNames.clear();
		for (int i = 0; i < this.cmbTriggers.size(); i++) {
			final String triggerName = this.cmbTriggers.get(i).getText().trim();
			if (!triggerName.equals("")) {
				this.triggersNames.add(triggerName);
			}
		}
		handleClose();
	}

	/**
	 *
	 */
	private void init() {
		setLayout(new BorderLayout());
		add(getTriggersPanel(), BorderLayout.CENTER);
		add(getButtonsPanel(), BorderLayout.SOUTH);
		this.btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleAdd();
			}
		});
		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleSave();
			}
		});
		this.btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleClose();
			}
		});
	}
}
