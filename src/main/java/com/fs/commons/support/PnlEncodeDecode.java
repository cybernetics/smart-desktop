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
package com.fs.commons.support;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.GeneralUtility;

public class PnlEncodeDecode extends JKPanel<Object> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	JKTextField txtSource = new JKTextField(20);
	JKTextField txtResult = new JKTextField(20);
	JKButton btnEncode = new JKButton("Encode");
	JKButton btnDecode = new JKButton("Decode");

	public PnlEncodeDecode() {
		init();
	}

	void handleDecode() {
		this.txtSource.setText(GeneralUtility.decode(this.txtResult.getText()));
	}

	void handleEncode() {
		this.txtResult.setText(GeneralUtility.encode(this.txtSource.getText()));
	}

	/**
	 *
	 */
	private void init() {
		add(this.txtSource);
		add(this.btnDecode);
		add(this.btnEncode);
		add(this.txtResult);
		this.btnDecode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleDecode();
			}
		});
		this.btnEncode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleEncode();
			}
		});
	}
}
