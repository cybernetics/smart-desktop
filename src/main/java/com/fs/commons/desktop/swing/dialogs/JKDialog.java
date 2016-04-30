package com.fs.commons.desktop.swing.dialogs;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.locale.Lables;

public class JKDialog extends JDialog {
	public static final ImageIcon shadowImage = new ImageIcon(JKDialog.class.getResource("dialogShadow.png"));
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JKDialog() {
		this(SwingUtility.getDefaultMainFrame());
	}

	/**
	 * 
	 * @param title
	 */
	public JKDialog(String title) {
		this(SwingUtility.getDefaultMainFrame(), title);
	}

	// /**
	// *
	// * @param parent
	// */
	// public JKDialog(Frame parent) {
	// super(parent);
	// initDialog();
	// }
	//
	// /**
	// *
	// * @param parent
	// */
	// public JKDialog(Dialog parent) {
	// super(parent);
	// initDialog();
	// }

	/**
	 * 
	 * @param parent
	 * @param title
	 */
	public JKDialog(Window parent, String title) {
		super(parent, title);
		initDialog();
	}

	public JKDialog(Window window) {
		super(window);
		initDialog();
	}

	/**
	 * 
	 * @param parent
	 * @param title
	 */
	public JKDialog(Frame parent, String title) {
		super(parent, title);
		initDialog();
	}

	/**
	 * should be used after all UI components inistalization in subclass
	 */
	protected void initDialog() {
		// fix title lable
		// since it seem the Dialog constructor doesnot call the setTitle method
		// which avoid to call the lables
		setTitle(getTitle());
		if (!isDisplayable()) {
//			setUndecorated(true);
//			AWTUtilities.setWindowOpaque(this, false);
		}
		setLocale(SwingUtility.getDefaultLocale());
		setModal(true);
		applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		pack();
		setLocationRelativeTo(getParent());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				Runnable runnable = new Runnable() {
					public void run() {
						transferFocus();
					}
				};
				SwingUtilities.invokeLater(runnable);
			}
		});
	}

	/**
	 * 
	 * @return JRootPane
	 */
	protected JRootPane createRootPane() {
		JRootPane rootPane = new JKRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		Action actionListener = new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent actionEvent) {
				dispose();
			}
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", actionListener);
		return rootPane;
	}

//	@Override
//	public void setVisible(boolean show) {
//		if (show) {
//			AnimationUtil.fadeIn(this);
//			super.setVisible(true);
//		} else {
//			if (getOpacity() > 0) {
//				AnimationUtil.fadeOut(this, false);
//			} else {
//				super.setVisible(false);
//			}
//			// super.setVisible(show);
//		}
//	}

//	@Override
//	public void dispose() {
//		if (getOpacity() > 0) {
//			AnimationUtil.fadeOut(this, false);
//		} else {
//			super.dispose();
//		}
//	}

	@Override
	public void setTitle(String title) {
		super.setTitle(Lables.get(title, true));
	}

	// @Override
	// public void paintComponents(Graphics g) {
	// g.drawImage(shadowImage.getImage(), 0, 0,getWidth(),getHeight(),
	// getBackground(), this);
	// }
}

class JKRootPane extends JRootPane {
	private static final long serialVersionUID = 1L;

	public JKRootPane() {
		setBorder(BorderFactory.createLineBorder(Colors.MENU_PANEL_BG, 5));
//		Border border2=BorderFactory.createRaisedBevelBorder();
//		setBorder(BorderFactory.createCompoundBorder(border1,border2));
//		 setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		 setDoubleBuffered(true);
	}

	// @Override
	public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		// // GraphicsFactory.makeGradient(this, g, Colors.MAIN_PANEL_BG);
//		g.drawImage(JKDialog.shadowImage.getImage(), 0, 0, getWidth(), getHeight(), Colors.MAIN_PANEL_BG, this);
	}
}