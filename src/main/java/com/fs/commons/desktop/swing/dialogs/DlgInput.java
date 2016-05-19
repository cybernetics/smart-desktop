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
package com.fs.commons.desktop.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.documents.FloatDocument;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.jk.exceptions.handler.JKExceptionUtil;

public class DlgInput extends JKPanel<Object> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		showInputDialog("Fees");
	}

	/**
	 *
	 * @return
	 */
	public static Float showInputDialog(final String title) {
		return showInputDialog(title, "");
	}

	/**
	 *
	 * @param string
	 * @param string2
	 */
	public static Float showInputDialog(final String title, final String defaultValue) {
		return showInputDialog(title, defaultValue, null, null);
	}

	/*
	 * p
	 *
	 */
	public static float showInputDialog(final String title, final String defaultValue, final Float rangeFrom, final Float rangeTo) {
		final DlgInput pnl = new DlgInput(defaultValue, title, rangeFrom, rangeTo);
		SwingUtility.showPanelInDialog(pnl, "INPUT_DIALOG");
		return pnl.amount;
	}

	final JKTextField txtValue = new JKTextField(new FloatDocument(5), 20);
	JKButton btnOk = new JKButton("OK");
	JKButton btnCancel = new JKButton("CANCEL");

	private Float amount;

	private final String title;

	private final Float rangeTo;

	private final Float rangeFrom;

	/**
	 * @param defaultValue
	 * @param title
	 *
	 */
	private DlgInput(final String defaultValue, final String title, final Float rangeFrom, final Float rangeTo) {
		this.title = title;
		this.rangeFrom = rangeFrom;
		this.rangeTo = rangeTo;
		init();
		this.txtValue.setText(defaultValue);
	}

	/**
	 *
	 */
	protected void handleOk() {
		try {
			SwingValidator.checkEmpty(this.txtValue);
			if (this.rangeFrom != null && this.rangeTo != null) {
				SwingValidator.checkValidRange(this.txtValue, this.rangeFrom, this.rangeTo);
			}
			this.amount = this.txtValue.getTextAsFloat();
			SwingUtility.closePanelDialog(this);
		} catch (final ValidationException e) {
			JKExceptionUtil.handle(e);
		}
	}

	/**
	 *
	 */
	void init() {
		setLayout(new BorderLayout());
		final JKPanel<?> pnlInfo = new JKPanel<Object>();
		pnlInfo.setBorder(SwingUtility.createTitledBorder("SET_VALUE"));
		pnlInfo.add(new JKLabledComponent(this.title, this.txtValue));

		final JKPanel<?> pnlButtons = new JKPanel<Object>();
		pnlButtons.add(this.btnOk);
		pnlButtons.add(this.btnCancel);

		add(pnlInfo, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);

		this.btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleOk();
			}
		});
		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				DlgInput.this.amount = null;
				SwingUtility.closePanelDialog(DlgInput.this);
			}
		});
		this.txtValue.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					DlgInput.this.btnOk.doClick();
				}
			}
		});
	}

}