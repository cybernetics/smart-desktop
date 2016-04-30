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
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.graphics.GraphicsFactory;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.swing.dialogs.ProgressDialog;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.locale.Lables;
import com.fs.commons.security.Privilige;
import com.fs.commons.security.SecurityManager;
import com.fs.commons.security.exceptions.NotAllowedOperationException;
import com.fs.commons.security.exceptions.SecurityException;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.FormatUtil;
import com.fs.commons.util.GeneralUtility;

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
	private Privilige privlige;
	private boolean progressAsModal;
	// private Font font = new Font("arial", Font.BOLD, 10);
	private FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private boolean authorized = true;
	private boolean transfer;

	/**
	 * 
	 * @param caption
	 *            String
	 */

	public JKButton(ImageIcon imageIcon) {
		this("");
		setIcon(imageIcon);
	}

	public JKButton() {
		this("");
	}

	public JKButton(String caption) {
		this(caption, false, "");
	}

	/**
	 */
	public JKButton(String caption, boolean leadingAligned) {
		this(caption, leadingAligned, "");
	}

	public JKButton(String caption, String shortcut) {
		this(caption, false, shortcut);
	}

	public JKButton(String caption, String shortcut, Privilige privlige) {
		this(caption, false, shortcut);
		setPrivlige(privlige);
	}

	public void setPrivlige(Privilige privlige) {
		if (privlige != null) {
			this.privlige = privlige;
			try {
				SecurityManager.checkAllowedPrivilige(privlige);
				authorized = true;
			} catch (NotAllowedOperationException e) {
				authorized = false;
				setEnabled(false);
			} catch (SecurityException e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	public Privilige getPrivlige() {
		return privlige;
	}

	/**
	 * 
	 * @param caption
	 * @param leadingAligned
	 * @param string
	 */
	public JKButton(String caption, boolean leadingAligned, String shortcut) {
		super(caption);
		setShortcut(shortcut, shortcut);
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		init();
		if (leadingAligned) {
			setHorizontalTextPosition(LEADING);
		}
	}

	public JKButton(String caption, String shortcut, boolean progress) {
		this(caption, shortcut);
		setShowProgress(progress);
	}

	public JKButton(String caption, Privilige privlige) {
		this(caption, "", privlige);
	}

	/**
	 * 
	 * @param shortcut
	 * @param b
	 */
	public void setShortcutText(String shortcut, boolean pre) {
		if (shortcut != null && !shortcut.equals("")) {
			shortcut = FormatUtil.capitalizeFirstLetters(shortcut);
			String htmlText = "<html><div align='" + getLabelAlignment() + "' width='100%'>" + "<font color=\""
					+ SwingUtility.colorToHex(getForeground()) + "\" " + "size=\"" + getFontSize() + "\" face=\"" + getFontName() + "\">" + getText()
					+ "</font></div> " + "<div align='" + getShortCutAlignment() + "'><font size=\"1\" color=\"#AA0000\"><em>" + shortcut
					+ "</em></font></div></html>";
			setText(htmlText);
		}
	}

	private String getLabelAlignment() {
		return SwingUtility.isLeftOrientation() ? "left" : "right";
	}

	protected int getFontSize() {
		return SwingUtility.isLeftOrientation() ? 3 : 3;
	}

	/**
	 * 
	 * @return
	 */
	private String getShortCutAlignment() {
		return "center";// SwingUtility.isLeftOrientation() ? "left" : "right";
	}

	/**
	 * 
	 * @return
	 */
	protected String getFontName() {
		return SwingUtility.isLeftOrientation() ? "Calibri" : getFont().getFamily();
	}

	/**
	 * 
	 * @return
	 */
	public String getShortcut() {
		return shortcut;
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
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					// transferFocus();					
					doClick();
				}
			}
		});
	}

	@Override
	public void setText(String text) {
		super.setText(Lables.get(text, true));
		setToolTipText(text);
	}

	@Override
	public void setToolTipText(String text) {
		super.setToolTipText(Lables.get(text));
	}

	@Override
	public void setIcon(Icon defaultIcon) {
		if (defaultIcon != null) {
			super.setIcon(defaultIcon);
			setIconTextGap(2);
		}
	}

	/**
	 * 
	 * @param iconName
	 */
	public void setIcon(String iconName) {
		setIcon(GeneralUtility.createIcon(iconName));
	}

	@Override
	protected void fireActionPerformed(final ActionEvent event) {
		if (privlige != null) {
			try {
				SecurityManager.getAuthorizer().checkAllowed(privlige);
			} catch (SecurityException e) {
				ExceptionUtil.handleException(e);
				return;
			}
		}
		if (showProgress) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					JKButton.super.fireActionPerformed(event);
				}
			};
			this.progress = ProgressDialog.create("", null, progressAsModal);
			progress.setCancellable(true);
			progress.addTask(runnable);
			progress.run();
		} else {
			JKButton.super.fireActionPerformed(event);
		}
	}

	public void setProgressCount(int count, int max) {
		if (progress != null) {
			progress.setProgressCount(Lables.get("PROCESSING_RECORD") + "  :  " + count + "/" + max);
		}

	}

	/**
	 * @return the showProgress
	 */
	public boolean isShowProgress() {
		return showProgress;
	}

	/**
	 * @param showProgress
	 *            the showProgress to set
	 */
	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}

	/**
	 * 
	 * @param shortcut2
	 * @param string
	 */
	public void setShortcut(String shortcut, String text) {
		SwingUtility.setHotKeyFoButton(this, shortcut);
		setShortcutText(text, false);
	}

	@Override
	public void paint(Graphics g) {
		if (isOpaque()) {
//			setOpaque(false);
//			GraphicsFactory.makeGradient(this, g, getBackground());
			super.paint(g);
//			setOpaque(true);
		} else {
			super.paint(g);
		}

	}

	public void setProgressMessage(String name) {
		if (this.progress != null) {
			progress.setProgressCount(name);
		}
	}

	public boolean isConteniueProcessing() {
		if (this.progress != null) {
			progress.isConteniueProcessing();
		}
		return false;
	}

	@Override
	public void setEnabled(boolean enable) {
		setOpaque(enable && authorized);
		super.setEnabled(enable && authorized);
	}

	public boolean isProgressAsModal() {
		return progressAsModal;
	}

	public void setProgressAsModal(boolean progressAsModal) {
		this.progressAsModal = progressAsModal;
	}

	@Override
	public void setValue(Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultValue(Object t) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object getDefaultValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addValidator(Validator validator) {
		// TODO Auto-generated method stub
	}

	@Override
	public void validateValue() throws ValidationException {
	}

	@Override
	public void filterValues(BindingComponent comp1) throws DaoException {
		// TODO Auto-generated method stub
	}

	@Override
	public void setDataSource(DataSource manager) {
		fsWrapper.setDataSource(manager);
	}

	@Override
	public DataSource getDataSource() {
		return fsWrapper.getDataSource();
	}

	@Override
	public void addValueChangeListener(ValueChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAutoTransferFocus(boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public boolean isAutoTransferFocus() {
		return transfer;
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
