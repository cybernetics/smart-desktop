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
package com.fs.commons.desktop.swing.comp.panels;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.desktop.swing.dialogs.JKDialog;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.ExceptionUtil;

public class JKBlobPanel extends JKPanel implements BindingComponent {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final int MAX_LENGTH = 1024 * 1024 / 4;

	static JFileChooser ch = SwingUtility.getFileChooser();// new
															// JFileChooser("c:\\");

	JKButton btnShowPanel = new JKButton("ADD_IMAGE");

	JKButton btnRemoveImage = new JKButton("REMOVE_IMAGE");

	ImagePanel lblThumb = new ImagePanel();

	JKPanel pnlBlob = new JKPanel();

	JKLabel lblImage = new JKLabel();

	JKButton btnBrowse = new JKButton("BROWSE");

	JKButton btnScan = new JKButton("FROM_IMAGING_DEVICE");

	JKButton btnClose = new JKButton("CLOSE");

	byte[] object;

	private String fieldName;

	private Object defaultValue;

	public JKBlobPanel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 */
	public JKBlobPanel(final String field) {
		this.fieldName = field;
		init();
	}

	@Override
	public void clear() {
		setValue(null);
	}

	@Override
	public Object getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * @return
	 */
	private JKDialog getDialog() {
		if (this.pnlBlob.getRootPane() != null) {
			final Container cont = this.pnlBlob.getRootPane().getParent();
			if (cont instanceof JKDialog) {
				return (JKDialog) cont;// pack the dialog reference
			}
		}
		return null;
	}

	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	// //////////////////////////////////////////////////////////////////////////////
	@Override
	public Object getValue() {
		return this.object;
	}

	/**
	 *
	 */
	private void handleBrowse() {
		if (ch.showOpenDialog(JKBlobPanel.this) == JFileChooser.APPROVE_OPTION) {
			final File file = ch.getSelectedFile();
			if (file != null) {
				if (!file.exists()) {
					SwingUtility.showUserErrorDialog("PLEASE_SELECT_VALID_FILE");
				}
				try {
					this.object = GeneralUtility.readStream(new FileInputStream(file));
					setValue(this.object);
				} catch (final IOException e1) {
					ExceptionUtil.handle(e1);
				}
			}
		}
	}

	private void handleScan() {
		try {
			// byte[] image = ImageUtil.readImage();
			// setValue(image);
		} catch (final Exception e1) {
			ExceptionUtil.handle(e1);
		}
	}

	/**
	 *
	 */
	private void init() {
		setLayout(new GridLayout(1, 3));
		this.btnShowPanel.setIcon("edit_picture.png");
		add(this.lblThumb);
		add(this.btnShowPanel);
		add(this.btnRemoveImage);
		this.btnRemoveImage.setVisible(false);
		this.lblThumb.setVisible(false);
		this.lblThumb.setPreferredSize(new Dimension(80, 80));
		this.lblThumb.setStyle(ImagePanel.SCALED);

		this.btnRemoveImage.setIcon("button_cancel_1.png");
		initBlobUploadPanel();
		this.btnShowPanel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				SwingUtility.showPanelInDialog(JKBlobPanel.this.pnlBlob, JKBlobPanel.this.fieldName);
			}
		});
		this.btnRemoveImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setValue(null);
			}
		});
	}

	/**
	 *
	 */
	private void initBlobUploadPanel() {
		this.pnlBlob.setPreferredSize(new Dimension(600, 400));
		this.pnlBlob.setLayout(new BorderLayout());
		this.pnlBlob.setMaximumSize(new Dimension(300, 400));
		final JKPanel pnlButton = new JKPanel();
		this.btnScan.setIcon(new ImageIcon(GeneralUtility.getIconURL("scanner.png")));
		this.btnBrowse.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileopen.png")));
		this.btnClose.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileclose.png")));
		pnlButton.add(this.btnScan);
		pnlButton.add(this.btnBrowse);
		pnlButton.add(this.btnClose);

		this.pnlBlob.add(new JScrollPane(this.lblImage), BorderLayout.CENTER);
		this.pnlBlob.add(pnlButton, BorderLayout.SOUTH);
		this.btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleBrowse();
			}
		});
		this.btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				getDialog().dispose();
			}
		});
		this.btnScan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleScan();
			}
		});

	}

	@Override
	public void reset() {
		setValue(null);
	}

	@Override
	public void setDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		this.btnShowPanel.setVisible(enabled);
	}

	/**
	 *
	 */
	@Override
	public void setValue(final Object value) {
		if (value != null && value instanceof byte[] && ((byte[]) value).length > 0) {
			final byte[] val = (byte[]) value;
			if (val.length > MAX_LENGTH) {
				SwingUtility.showUserErrorDialog(Lables.get("ERROR_INVALID_IMAGE_SIZE") + " " + val.length);
				return;
			} else {
				this.object = val;
				final ImageIcon image = new ImageIcon(this.object);
				this.lblImage.setIcon(image);
				// lblImage.setPreferredSize(new
				// Dimension(image.getIconWidth(),image.getIconHeight()));
				// JKDialog dlg = getDialog();// if set after the dialog is
				// shown
				// if (dlg != null) {
				// dlg.pack();
				// dlg.setLocationRelativeTo(null);
				// }
				this.btnShowPanel.setText(Lables.get("SHOW"));
				this.btnRemoveImage.setVisible(isEnabled());
				this.lblThumb.setVisible(true);
				this.lblThumb.setImage(val);
				// SwingUtility.packWindow(this);
			}
		} else {
			this.object = null;

			this.lblImage.removeIcon();
			this.lblThumb.removeImage();

			invalidate();
			repaint();
			this.btnRemoveImage.setVisible(false);
			this.btnShowPanel.setText(Lables.get("ADD_IMAGE"));
			this.lblThumb.setVisible(false);
		}
	}
}
