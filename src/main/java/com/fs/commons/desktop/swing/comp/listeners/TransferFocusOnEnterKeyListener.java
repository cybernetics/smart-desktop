package com.fs.commons.desktop.swing.comp.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.SwingUtilities;

import com.fs.commons.bean.binding.BindingComponent;

public class TransferFocusOnEnterKeyListener extends KeyAdapter {

	private BindingComponent comp;

	public TransferFocusOnEnterKeyListener(BindingComponent comp) {
		this.comp = comp;
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() instanceof BindingComponent) {
				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						if (comp.isAutoTransferFocus()) {
							comp.transferFocus();
						}
					}
				};
				SwingUtilities.invokeLater(runnable);
			}
		}
	}
}
