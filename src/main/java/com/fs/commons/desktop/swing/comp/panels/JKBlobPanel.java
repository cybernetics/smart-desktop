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
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class JKBlobPanel extends JKPanel implements BindingComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int MAX_LENGTH = (1024 * 1024) / 4;

	JKButton btnShowPanel = new JKButton("ADD_IMAGE");

	JKButton btnRemoveImage = new JKButton("REMOVE_IMAGE");

	ImagePanel lblThumb = new ImagePanel();

	JKPanel pnlBlob = new JKPanel();

	JKLabel lblImage = new JKLabel();

	JKButton btnBrowse = new JKButton("BROWSE");

	JKButton btnScan = new JKButton("FROM_IMAGING_DEVICE");

	JKButton btnClose = new JKButton("CLOSE");

	static JFileChooser ch = SwingUtility.getFileChooser();// new
															// JFileChooser("c:\\");

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

	/**
	 * 
	 */
	private void init() {
		setLayout(new GridLayout(1, 3));
		btnShowPanel.setIcon("edit_picture.png");
		add(lblThumb);
		add(btnShowPanel);
		add(btnRemoveImage);
		btnRemoveImage.setVisible(false);
		lblThumb.setVisible(false);
		lblThumb.setPreferredSize(new Dimension(80, 80));
		lblThumb.setStyle(ImagePanel.SCALED);

		btnRemoveImage.setIcon("button_cancel_1.png");
		initBlobUploadPanel();
		btnShowPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtility.showPanelInDialog(pnlBlob, fieldName);
			}
		});
		btnRemoveImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setValue(null);
			}
		});
	}

	/**
	 * 
	 */
	private void initBlobUploadPanel() {
		pnlBlob.setPreferredSize(new Dimension(600,400));
		pnlBlob.setLayout(new BorderLayout());
		pnlBlob.setMaximumSize(new Dimension(300, 400));
		JKPanel pnlButton = new JKPanel();
		btnScan.setIcon(new ImageIcon(GeneralUtility.getIconURL("scanner.png")));
		btnBrowse.setIcon(new ImageIcon(GeneralUtility
				.getIconURL("fileopen.png")));
		btnClose.setIcon(new ImageIcon(GeneralUtility
				.getIconURL("fileclose.png")));
		pnlButton.add(btnScan);
		pnlButton.add(btnBrowse);
		pnlButton.add(btnClose);

		pnlBlob.add(new JScrollPane(lblImage), BorderLayout.CENTER);
		pnlBlob.add(pnlButton, BorderLayout.SOUTH);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleBrowse();
			}
		});
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getDialog().dispose();
			}
		});
		btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleScan();
			}
		});

	}

	/**
	 * @return
	 */
	private JKDialog getDialog() {
		if (pnlBlob.getRootPane() != null) {
			Container cont = pnlBlob.getRootPane().getParent();
			if (cont instanceof JKDialog) {
				return ((JKDialog) cont);// pack the dialog reference
			}
		}
		return null;
	}

	/**
	 * 
	 */
	private void handleBrowse() {
		if (ch.showOpenDialog(JKBlobPanel.this) == JFileChooser.APPROVE_OPTION) {
			File file = ch.getSelectedFile();
			if (file != null) {
				if (!file.exists()) {
					SwingUtility
							.showUserErrorDialog("PLEASE_SELECT_VALID_FILE");
				}
				try {
					object = GeneralUtility
							.readStream(new FileInputStream(file));
					setValue(object);
				} catch (IOException e1) {
					ExceptionUtil.handleException(e1);
				}
			}
		}
	}

	/**
	 * 
	 */
	public void setValue(Object value) {
		if (value != null && (value instanceof byte[])
				&& ((byte[]) value).length > 0) {
			byte[] val = (byte[]) value;
			if (val.length > MAX_LENGTH) {
				SwingUtility.showUserErrorDialog(Lables
						.get("ERROR_INVALID_IMAGE_SIZE") + " " + val.length);
				return;
			} else {
				this.object = val;
				ImageIcon image = new ImageIcon(object);
				lblImage.setIcon(image);
				// lblImage.setPreferredSize(new
				// Dimension(image.getIconWidth(),image.getIconHeight()));
//				JKDialog dlg = getDialog();// if set after the dialog is shown
//				if (dlg != null) {
//					dlg.pack();
//					dlg.setLocationRelativeTo(null);
//				}
				btnShowPanel.setText(Lables.get("SHOW"));
				btnRemoveImage.setVisible(isEnabled());
				lblThumb.setVisible(true);
				lblThumb.setImage(val);
				//SwingUtility.packWindow(this);
			}
		} else {
			this.object = null;

			lblImage.removeIcon();
			lblThumb.removeImage();

			invalidate();
			repaint();
			btnRemoveImage.setVisible(false);
			btnShowPanel.setText(Lables.get("ADD_IMAGE"));
			lblThumb.setVisible(false);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	public Object getValue() {
		return object;
	}

	@Override
	public void reset() {
		setValue(null);
	}

	@Override
	public void clear() {
		setValue(null);
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	private void handleScan() {
		try {
//			byte[] image = ImageUtil.readImage();
//			setValue(image);
		} catch (Exception e1) {
			ExceptionUtil.handleException(e1);
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		btnShowPanel.setVisible(enabled);
	}
}
