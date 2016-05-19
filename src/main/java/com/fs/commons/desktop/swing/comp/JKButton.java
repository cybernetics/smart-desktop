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
package com.fs.commons.desktop.swing.comp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.swing.dialogs.ProgressDialog;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.FormatUtil;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.JKNotAllowedOperationException;
import com.jk.exceptions.handler.ExceptionUtil;
import com.jk.security.JKSecurityManager;
import com.jk.security.JKPrivilige;

public class JKButton extends JButton implements BindingComponent {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	static Color BACKGROUND_COLOR = Colors.JK_BUTTON_BG;// getBackground();
	static Color FORGROUND_COLOR = Colors.JK_BUTTON_FG;

	private String shortcut;
	private boolean showProgress = false;
	private ProgressDialog progress;
	private JKPrivilige privlige;
	private boolean progressAsModal;
	// private Font font = new Font("arial", Font.BOLD, 10);
	private final FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private boolean authorized = true;
	private boolean transfer;

	public JKButton() {
		this("");
	}

	/**
	 *
	 * @param caption
	 *            String
	 */

	public JKButton(final ImageIcon imageIcon) {
		this("");
		setIcon(imageIcon);
	}

	public JKButton(final String caption) {
		this(caption, false, "");
	}

	/**
	 */
	public JKButton(final String caption, final boolean leadingAligned) {
		this(caption, leadingAligned, "");
	}

	/**
	 *
	 * @param caption
	 * @param leadingAligned
	 * @param string
	 */
	public JKButton(final String caption, final boolean leadingAligned, final String shortcut) {
		super(caption);
		setShortcut(shortcut, shortcut);
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		init();
		if (leadingAligned) {
			setHorizontalTextPosition(LEADING);
		}
	}

	public JKButton(final String caption, final JKPrivilige privlige) {
		this(caption, "", privlige);
	}

	public JKButton(final String caption, final String shortcut) {
		this(caption, false, shortcut);
	}

	public JKButton(final String caption, final String shortcut, final boolean progress) {
		this(caption, shortcut);
		setShowProgress(progress);
	}

	public JKButton(final String caption, final String shortcut, final JKPrivilige privlige) {
		this(caption, false, shortcut);
		setPrivlige(privlige);
	}

	@Override
	public void addValidator(final Validator validator) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addValueChangeListener(final ValueChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void filterValues(final BindingComponent comp1) throws JKDataAccessException {
		// TODO Auto-generated method stub
	}

	@Override
	protected void fireActionPerformed(final ActionEvent event) {
		if (this.privlige != null) {
			try {
				JKSecurityManager.getAuthorizer().checkAllowed(this.privlige);
			} catch (final SecurityException e) {
				ExceptionUtil.handle(e);
				return;
			}
		}
		if (this.showProgress) {
			final Runnable runnable = new Runnable() {
				@Override
				public void run() {
					JKButton.super.fireActionPerformed(event);
				}
			};
			this.progress = ProgressDialog.create("", null, this.progressAsModal);
			this.progress.setCancellable(true);
			this.progress.addTask(runnable);
			this.progress.run();
		} else {
			JKButton.super.fireActionPerformed(event);
		}
	}

	@Override
	public JKDataSource getDataSource() {
		return this.fsWrapper.getDataSource();
	}

	@Override
	public Object getDefaultValue() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 * @return
	 */
	protected String getFontName() {
		return SwingUtility.isLeftOrientation() ? "Calibri" : getFont().getFamily();
	}

	protected int getFontSize() {
		return SwingUtility.isLeftOrientation() ? 3 : 3;
	}

	private String getLabelAlignment() {
		return SwingUtility.isLeftOrientation() ? "left" : "right";
	}

	public JKPrivilige getPrivlige() {
		return this.privlige;
	}

	/**
	 *
	 * @return
	 */
	public String getShortcut() {
		return this.shortcut;
	}

	/**
	 *
	 * @return
	 */
	private String getShortCutAlignment() {
		return "center";// SwingUtility.isLeftOrientation() ? "left" : "right";
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 */
	void init() {
		// setFont(font);
		setBackground(BACKGROUND_COLOR);
		setSelected(false);// to set the right shape
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(final KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					// transferFocus();
					doClick();
				}
			}
		});
	}

	@Override
	public boolean isAutoTransferFocus() {
		return this.transfer;
	}

	public boolean isConteniueProcessing() {
		if (this.progress != null) {
			this.progress.isConteniueProcessing();
		}
		return false;
	}

	public boolean isProgressAsModal() {
		return this.progressAsModal;
	}

	/**
	 * @return the showProgress
	 */
	public boolean isShowProgress() {
		return this.showProgress;
	}

	@Override
	public void paint(final Graphics g) {
		if (isOpaque()) {
			// setOpaque(false);
			// GraphicsFactory.makeGradient(this, g, getBackground());
			super.paint(g);
			// setOpaque(true);
		} else {
			super.paint(g);
		}

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setAutoTransferFocus(final boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public void setDataSource(final JKDataSource manager) {
		this.fsWrapper.setDataSource(manager);
	}

	@Override
	public void setDefaultValue(final Object t) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setEnabled(final boolean enable) {
		setOpaque(enable && this.authorized);
		super.setEnabled(enable && this.authorized);
	}

	@Override
	public void setIcon(final Icon defaultIcon) {
		if (defaultIcon != null) {
			super.setIcon(defaultIcon);
			setIconTextGap(2);
		}
	}

	/**
	 *
	 * @param iconName
	 */
	public void setIcon(final String iconName) {
		setIcon(GeneralUtility.createIcon(iconName));
	}

	public void setPrivlige(final JKPrivilige privlige) {
		if (privlige != null) {
			this.privlige = privlige;
			try {
				JKSecurityManager.checkAllowedPrivilige(privlige);
				this.authorized = true;
			} catch (final JKNotAllowedOperationException e) {
				this.authorized = false;
				setEnabled(false);
			} catch (final SecurityException e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	public void setProgressAsModal(final boolean progressAsModal) {
		this.progressAsModal = progressAsModal;
	}

	public void setProgressCount(final int count, final int max) {
		if (this.progress != null) {
			this.progress.setProgressCount(Lables.get("PROCESSING_RECORD") + "  :  " + count + "/" + max);
		}

	}

	public void setProgressMessage(final String name) {
		if (this.progress != null) {
			this.progress.setProgressCount(name);
		}
	}

	/**
	 *
	 * @param shortcut2
	 * @param string
	 */
	public void setShortcut(final String shortcut, final String text) {
		SwingUtility.setHotKeyFoButton(this, shortcut);
		setShortcutText(text, false);
	}

	/**
	 *
	 * @param shortcut
	 * @param b
	 */
	public void setShortcutText(String shortcut, final boolean pre) {
		if (shortcut != null && !shortcut.equals("")) {
			shortcut = FormatUtil.capitalizeFirstLetters(shortcut);
			final String htmlText = "<html><div align='" + getLabelAlignment() + "' width='100%'>" + "<font color=\""
					+ SwingUtility.colorToHex(getForeground()) + "\" " + "size=\"" + getFontSize() + "\" face=\"" + getFontName() + "\">" + getText()
					+ "</font></div> " + "<div align='" + getShortCutAlignment() + "'><font size=\"1\" color=\"#AA0000\"><em>" + shortcut
					+ "</em></font></div></html>";
			setText(htmlText);
		}
	}

	/**
	 * @param showProgress
	 *            the showProgress to set
	 */
	public void setShowProgress(final boolean showProgress) {
		this.showProgress = showProgress;
	}

	@Override
	public void setText(final String text) {
		super.setText(Lables.get(text, true));
		setToolTipText(text);
	}

	@Override
	public void setToolTipText(final String text) {
		super.setToolTipText(Lables.get(text));
	}

	@Override
	public void setValue(final Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateValue() throws ValidationException {
	}

	// @Override
	// public Dimension getPreferredSize() {
	// // TODO Auto-generated method stub
	// Dimension preferredSize = super.getPreferredSize();
	// if (preferredSize.height<32) {
	// preferredSize.height = 35;
	// }
	// return preferredSize;
	// }
}
