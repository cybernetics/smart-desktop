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
package com.fs.commons.desktop.swing.comp.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.SwingUtilities;

import com.fs.commons.bean.binding.BindingComponent;

public class TransferFocusOnEnterKeyListener extends KeyAdapter {

	private final BindingComponent comp;

	public TransferFocusOnEnterKeyListener(final BindingComponent comp) {
		this.comp = comp;
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() instanceof BindingComponent) {
				final Runnable runnable = new Runnable() {

					@Override
					public void run() {
						if (TransferFocusOnEnterKeyListener.this.comp.isAutoTransferFocus()) {
							TransferFocusOnEnterKeyListener.this.comp.transferFocus();
						}
					}
				};
				SwingUtilities.invokeLater(runnable);
			}
		}
	}
}
