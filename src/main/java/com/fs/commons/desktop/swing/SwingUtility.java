/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      31/8/2008     Jamil Shreet    -Add method showConfirmationDialog(String messages[]) that takes array as String.
 */
package com.fs.commons.desktop.swing;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;

import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.application.ui.menu.MenuItem;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKFrame;
import com.fs.commons.desktop.swing.comp.JKInternalFrame;
import com.fs.commons.desktop.swing.comp.JKScrollPane;
import com.fs.commons.desktop.swing.comp.JKTextArea;
import com.fs.commons.desktop.swing.comp.panels.ImagePanel;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.comp2.FSPanel;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.desktop.swing.dialogs.JKDialog;
import com.fs.commons.desktop.swing.frames.ApplicationFrame;
import com.fs.commons.locale.Lables;
import com.fs.commons.security.User;
import com.fs.commons.security.exceptions.NotAllowedOperationException;
import com.fs.commons.security.exceptions.SecurityException;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;
import com.fs.commons.util.ImageUtil;
import com.fs.commons.util.ReflicationUtil;

public class SwingUtility {
	static {
		try {
			// PlasticXPLookAndFeel.set3DEnabled(true);
			// PlasticXPLookAndFeel.setHighContrastFocusColorsEnabled(true);
			// PlasticXPLookAndFeel.setPlasticTheme(new SkyBlue());


			ColorUIResource disabledBackground = new ColorUIResource(Color.white);
			UIManager.put("ComboBox.disabledBackground", disabledBackground);
			ColorUIResource disabledForeground = new ColorUIResource(Color.black);
			UIManager.put("ComboBox.disabledForeground", disabledForeground);
			UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.PlasticXPLookAndFeel());			
//		    NativeInterface.open();

			// addFocusForwardKey(KeyEvent.VK_DOWN);
			// addFocusBackKey(KeyEvent.VK_UP);

			// MetalLookAndFeel feel = new MetalLookAndFeel();
			// feel.setCurrentTheme(new OceanTheme());
			// UIManager.setLookAndFeel(feel);
			//
			// UIManager.put("TextField.font", new FontUIResource( new
			// Font("Tahoma",Font.PLAIN,12)));
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			// NativeInterface.open();
			// Runtime.getRuntime().addShutdownHook(new Thread(){
			// @Override
			// public void run() {
			// NativeInterface.close();
			// }
			// });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static Color defaultBackgroundColor = Colors.MAIN_PANEL_BG;// Colors.SKY_BLUE;

	static JKFrame defaultMainFrame;// = new JKFrame();

	static JDialog emptyDialog = new JDialog();

	// static JPanel homePanel = new JKPanel();

	// static Font defaultFont =new Font("Tahoma",Font.PLAIN,12);

	static ComponentOrientation defaultComponentOrientation = ComponentOrientation.LEFT_TO_RIGHT;

	static String defaultLocale = "en";

	/**
	 * @param defaultLocale
	 *            the defaultLocale to set
	 */
	public static void setDefaultLocale(String defaultLocale) {
		if (defaultLocale.equals("ar")) {
			setDefaultComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		} else {
			setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		}
		SwingUtility.defaultLocale = defaultLocale;
	}

	private static JFileChooser chooser = new JFileChooser(".");

	/**
	 * 
	 * @return
	 */
	public static void createLineBorder(JKPanel<?> pnl) {
		pnl.setBorder(new LineBorder(Color.lightGray));

	}

	/**
	 * 
	 * @return
	 */

	public static ComponentOrientation getDefaultComponentOrientation() {
		return defaultComponentOrientation;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isLeftOrientation() {
		return defaultComponentOrientation == ComponentOrientation.LEFT_TO_RIGHT;
	}

	/**
	 * 
	 * @param orientation
	 */
	public static void setDefaultComponentOrientation(ComponentOrientation orientation) {
		SwingUtility.defaultComponentOrientation = orientation;
		if (!isLeftOrientation()) {
			defaultLocale = "ar";
		} else {
			defaultLocale = "en";
		}
	}

	/**
	 * 
	 * @param panel
	 *            JPanel
	 */
	public static void testPanel(JPanel panel) {
		JKFrame frame = new JKFrame();
		// frame.setExtendedState(frame.MAXIMIZED_BOTH);
		JKMainPanel mainPanel = new JKMainPanel(new BorderLayout());
		mainPanel.add(panel);
		frame.add(mainPanel);
		frame.applyComponentOrientation(getDefaultComponentOrientation());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * 
	 * @param panel
	 * @param title
	 * @throws HeadlessException
	 */
	public static void addPanelToFrame(JFrame frame, JPanel panel, String title) throws HeadlessException {
		frame.add(panel);
		frame.setTitle(title);
		frame.applyComponentOrientation(getDefaultComponentOrientation());
	}

	/**
	 * 
	 * @param panel
	 * @param title
	 * @throws HeadlessException
	 */
	private static void addPanelToDialog(JDialog dialog, JPanel panel, String title) throws HeadlessException {
		// panel.setBorder(createTitledBorder(title));
		dialog.add(new JKScrollPane(panel));
		panel.setOpaque(true);
		dialog.applyComponentOrientation(getDefaultComponentOrientation());
		panel.grabFocus();
	}

	/**
	 * 
	 * @param cont
	 * @param enable
	 */
	public static void enableContainer(Container cont, boolean enable) {
		int count = cont.getComponentCount();
		Component comp;
		for (int i = 0; i < count; i++) {
			comp = cont.getComponent(i);
			if (comp instanceof JPanel || comp instanceof Box) {
				enableContainer((Container) comp, enable);
			} else {
				if (comp instanceof JTextField) {
					((JTextField) comp).setEnabled(enable);
				} else if (!(comp instanceof JLabel)) {
					comp.setEnabled(enable);
				}
			}
		}
	}

	/**
	 * 
	 * @param parentComponent
	 * @param message
	 * @return
	 * @throws HeadlessException
	 */
	public static String showInputDialog(String message) throws HeadlessException {
		return showInputDialog(defaultMainFrame, message);
	}

	/**
	 * 
	 * @param parentComponent
	 * @param message
	 * @return
	 * @throws HeadlessException
	 */
	public static String showInputDialog(Component parentComponent, String message) throws HeadlessException {
		return JOptionPane.showInputDialog(parentComponent, Lables.get(message, true));
	}

	/**
	 * 
	 * @param error
	 * @param parent
	 */
	public static void showErrorDialog(String error, Throwable e, Window parent) {
		JOptionPane.showMessageDialog(parent, getDialogViewComponent(error));
		throw new RuntimeException(e);
	}
	
	public static void showErrorDialog(String error, Throwable e, Window parent,boolean color) {
		JOptionPane.showMessageDialog(parent, getDialogViewComponent(error,true));
		throw new RuntimeException(e);
	}

	public static void showSuccessDialog(JDialog parent, String string) {
		JOptionPane.showMessageDialog(parent, Lables.get(string, true));
	}

	public static void showSuccessDialog(final String string) {
		JOptionPane.showMessageDialog(getDefaultMainFrame(), getDialogViewComponent(string));
	}

	public static void showUserErrorDialog(JDialog parent, String message, Exception ex) {
		JOptionPane.showMessageDialog(parent, getDialogViewComponent(message));
	}

	public static void showUserErrorDialog(JFrame parent, String message, Exception ex) {
		JOptionPane.showMessageDialog(parent, getDialogViewComponent(message));
		ex.printStackTrace();
	}

	public static void showDatabaseErrorDialog(String message, Exception ex) {
		showDatabaseErrorDialog(emptyDialog, message, ex);
	}

	public static void showDatabaseErrorDialog(JDialog dialog, String message, Exception ex) {
		JOptionPane.showMessageDialog(dialog, getDialogViewComponent(message));
		throw new RuntimeException(ex);
	}

	public static boolean showConfirmationDialog(String message) {
		return showConfirmationDialog(getDefaultMainFrame(), message);
	}

	/**
	 * @1.1
	 * @param message
	 * @return
	 */
	public static boolean showConfirmationDialog(String message[]) {
		return showConfirmationDialog(getDefaultMainFrame(), message);
	}

	/**
	 * @1.1
	 * @param window
	 * @param messages
	 * @return
	 */
	public static boolean showConfirmationDialog(Window window, String[] messages) {
		StringBuffer concateMessage = new StringBuffer();
		String stringLabel = "";
		for (int i = 0; i < messages.length; i++) {
			concateMessage.append(Lables.get(messages[i], true));
			concateMessage.append("\n");
		}
		concateMessage.append(stringLabel);
		int choice = JOptionPane.showConfirmDialog(window, (Lables.get(concateMessage.toString(), true)), Lables.get("WARNING"),
				JOptionPane.YES_NO_OPTION);
		return choice == JOptionPane.YES_OPTION;
	}

	public static boolean showConfirmationDialog(JDialog dialog, String message) {
		int choice = JOptionPane.showConfirmDialog(dialog, (Lables.get(message, true)), Lables.get("WARNING"), JOptionPane.YES_NO_OPTION);
		return choice == JOptionPane.YES_OPTION;
	}

	/**
	 * 
	 * @param dialog
	 * @param message
	 * @return
	 */
	public static boolean showConfirmationDialog(Window window, String message) {
		// int choice = JOptionPane.showConfirmDialog(window,
		// (Lables.get(message)), Lables.get("WARNING"),
		// JOptionPane.YES_NO_OPTION);

		String no = Lables.get("No");
		String yes = Lables.get("Yes");
		int selection = JOptionPane.showOptionDialog(window, Lables.get(message, true), Lables.get("WARNING"), JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, new String[] { yes, no }, no);
		return selection == JOptionPane.YES_OPTION;
	}

	/**
	 * 
	 * @return
	 */
	public static JKFrame getDefaultMainFrame() {
		if (defaultMainFrame == null) {
			return new JKFrame();
		}
		return defaultMainFrame;
	}

	/**
	 * 
	 * @param defaultMainFrame
	 */
	public static void setDefaultMainFrame(JKFrame defaultMainFrame) {
		SwingUtility.defaultMainFrame = defaultMainFrame;
		defaultMainFrame.setLocale(getDefaultLocale());
	}

	/**
	 * 
	 * @param comp
	 */
	public static void setFont(Component comp) {
		// comp.setFont(defaultFont);
	}

	public static void setBoldFont(Component comp) {
		comp.setFont(new Font("Tahoma", Font.BOLD, 10));
	}

	public static Border createTitledBorder(String title) {
		TitledBorder b = BorderFactory.createTitledBorder("");
		b.setTitle(Lables.get(title, true));
		b.setTitleJustification(TitledBorder.DEFAULT_JUSTIFICATION);
		b.setTitlePosition(TitledBorder.CENTER);
		b.setTitleColor(Colors.TITLE_BORDER_BG);
		return b;
	}

	// private static Font getDefaultFont() {
	// return defaultFont;
	// }

	/**
	 * 
	 * @return
	 */
	public static Color getDefaultBackgroundColor() {
		return defaultBackgroundColor;
	}

	/**
	 * 
	 * @param defaultBackgroundColor
	 */
	public static void setDefaultBackgroundColor(Color defaultBackgroundColor) {
		SwingUtility.defaultBackgroundColor = defaultBackgroundColor;
	}

	/*
	 * 
	 */
	public static void closePanel(JPanel pnl) {
		// Container cont = pnl.getParent();
		// cont.remove(pnl);
		// cont.validate();
		// cont.repaint();
		getDefaultMainFrame().showHomePanel();
	}

	/**
	 * 
	 * @return
	 */
	public static JDialog getEmptyDialog() {
		return emptyDialog;
	}

	/**
	 * 
	 * @return
	 */
	public static JPanel getHomePanel() {
		return getDefaultMainFrame().getHomePanel();
	}

	/**
	 * 
	 * @param homePanel
	 */
	public static void setHomePanel(JPanel homePanel) {
		getDefaultMainFrame().setHomePanel(homePanel);
	}

	/**
	 * 
	 * @return
	 */
	public static String getDatePattern() {
		return isLeftOrientation() ? "yyyy/MM/dd" : "dd/MM/yyyy";
	}

	/**
	 * 
	 * @param pnl
	 * @param title
	 * @param modal
	 * @return
	 */
	public static JDialog showPanelInDialog(JPanel pnl, String title, boolean modal) {
		return showPanelInDialog(pnl, title, modal, null);
	}

	/**
	 * 
	 * @param pnl
	 * @param title
	 * @param modal
	 * @param parent
	 * @return
	 */
	public static JKDialog showPanelInDialog(JPanel pnl, String title, boolean modal, JKPanel<?> parent) {
		return showPanelInDialog(pnl, title, modal, parent, null);
	}

	public static JKDialog showPanelInDialog(JPanel pnl, String title, boolean modal, JKPanel<?> parent, Dimension dimension) {
		JKDialog dialog = null;
		if (parent != null && parent.getRootPane() != null) {
			Container cont = parent.getRootPane().getParent();
			if (cont != null) {
				if (cont instanceof JDialog) {
					dialog = new JKDialog(((JDialog) cont), title);
				}
			}
		}
		if (dialog == null) {
			dialog = new JKDialog(getActiveWindow(), title);
		}
		dialog.setResizable(true);

		addPanelToDialog(dialog, pnl, title);
		if (dimension != null) {
			dialog.setSize(dimension);
		} else {
			dialog.pack();
		}

		dialog.setLocationRelativeTo(dialog.getParent());
		dialog.setModal(modal);

		dialog.setVisible(true);
		return dialog;
	}

	private static Window getActiveWindow() {
		KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		Window window = keyboardFocusManager.getActiveWindow();
		if (window == null) {
			return getDefaultMainFrame();
		}
		return window;
	}

	/**
	 * 
	 * @param panel
	 * @param title
	 * @return
	 */
	public static JDialog showPanelInDialog(JPanel panel, String title) {
		return showPanelInDialog(panel, title, true);
	}

	/**
	 * 
	 */
	public static void closePanelDialog(JComponent comp) {
		if (comp.getRootPane() != null) {
			Container cont = comp.getRootPane().getParent();
			if (cont != null) {
				if (cont instanceof JDialog) {
					((JDialog) cont).dispose();
				}
			}
		}
	}

	/**
	 * 
	 * @param btn
	 * @param keyStroke
	 * @param actionName
	 */
	public static void setHotKeyFoButton(final JButton btn, String keyStroke, String actionName) {
		// get the button's Action map
		ActionMap amap = btn.getActionMap();
		// add an action to the button's action map
		// and give it a name(it can be any object not just String)
		amap.put(actionName, new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				// call your a method that contains your action code
				if (btn.isVisible() && btn.isEnabled()) {
					btn.doClick();
				}
			}
		});
		// get the input map for the button
		InputMap imap = btn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		// add a key stroke associated with an action in the action map(action
		// name).
		// imap.put(KeyStroke.getKeyStroke("F1"),"ActionName");
		// you can do the same for more than one key.
		imap.put(KeyStroke.getKeyStroke(keyStroke), actionName);
	}

	/**
	 * 
	 * @param btn
	 * @param keyStroke
	 * @param actionName
	 */
	public static void setHotKeyForFocus(final JComponent comp, String keyStroke, String actionName) {
		// get the button's Action map
		ActionMap amap = comp.getActionMap();
		// add an action to the button's action map
		// and give it a name(it can be any object not just String)
		amap.put(actionName, new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				// call your a method that contains your action code
				comp.requestFocus();
			}
		});
		// get the input map for the button
		InputMap imap = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		// add a key stroke associated with an action in the action map(action
		// name).
		// imap.put(KeyStroke.getKeyStroke("F1"),"ActionName");
		// you can do the same for more than one key.
		imap.put(KeyStroke.getKeyStroke(keyStroke), actionName);
	}

	/**
	 * 
	 * @param message
	 * @param ex
	 */
	public static void showUserErrorDialog(String message, Exception ex) {
		showUserErrorDialog(SwingUtility.getDefaultMainFrame(), message, ex);
		throw new RuntimeException(message);
	}

	public static void showMessageDialog(String message, Throwable ex) {
		JOptionPane.showMessageDialog(getDefaultMainFrame(), Lables.get(message, true));
		throw new RuntimeException(message);
	}

	public static void showUserErrorDialog(String string) {
		showUserErrorDialog(string, true);
	}

	public static void showUserErrorDialog(String string, boolean throwRuntimeException) {
		JScrollPane jScrollPane = getDialogViewComponent(string);

		JOptionPane.showMessageDialog(getDefaultMainFrame(), jScrollPane);
		if (throwRuntimeException) {
			throw new RuntimeException(string);
		}
	}
	
	private static JScrollPane getDialogViewComponent(String string) {
		return getDialogViewComponent(string, false);
	}
	private static JScrollPane getDialogViewComponent(String string, boolean color) {
		JKTextArea txt = new JKTextArea();
		txt.setText(Lables.get(string, true));
		txt.setEditable(false);
		
		if(color){
		txt.setForeground(Color.red);
		txt.setFont(new Font("Tahoma", Font.BOLD, 22));
		}
		JScrollPane jScrollPane = new JScrollPane(txt);
		jScrollPane.getViewport().setPreferredSize(new Dimension(500, 200));
		return jScrollPane;
	}

	public static void packWindow(JComponent comp) {

		if (comp.getRootPane() != null) {
			Container cont = comp.getRootPane().getParent();
			if (cont != null) {
				if (cont instanceof JDialog) {
					((JDialog) cont).pack();
					((JDialog) cont).setLocationRelativeTo(null);
				}
			}
		}
	}

	/**
	 * i seperated it in different method to avoid affecting other components
	 * 
	 * @param comp
	 */
	public static void packJFrameWindow(JComponent comp) {
		if (comp.getRootPane() != null) {
			Container cont = comp.getRootPane().getParent();
			if (cont instanceof JFrame) {
				((JFrame) cont).pack();
				((JFrame) cont).setLocationRelativeTo(null);
			}
		}
	}

	/**
	 * @param url
	 * @param scaled
	 * @return
	 */
	public static ImagePanel buildImagePanel(URL url, int scaled) {
		BufferedImage image = null;
		try {
			if (url != null) {
				image = getImage(url);
				return new ImagePanel(image, scaled);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return new ImagePanel();
	}

	public static BufferedImage getImage(URL url) throws IOException {
		BufferedImage image;
		image = javax.imageio.ImageIO.read(url);
		return image;
	}

	public static BufferedImage getImage(InputStream in) throws IOException {
		BufferedImage image;
		image = javax.imageio.ImageIO.read(in);
		return image;
	}

	public static BufferedImage getImage(String imageName) throws IOException {
		return getImage(GeneralUtility.getFileURI(imageName).toURL());
	}

	public static JPanel buildImagePanel(InputStream in, int scaled) {
		BufferedImage image = null;
		try {
			image = javax.imageio.ImageIO.read(in);
			return new ImagePanel(image, scaled);
		} catch (IOException ex) {
			return new JPanel();
		}
	}

	/**
	 * @param image
	 * @param scaled
	 * @return
	 */
	public static JPanel buildImagePanel(byte[] image, int scaled) {
		return buildImagePanel(new ByteArrayInputStream(image), scaled);
	}

	public static BufferedImage toBufferedImage(byte[] imageBytes) throws IOException {
		return javax.imageio.ImageIO.read(new ByteArrayInputStream(imageBytes));
	}

	/**
	 * 
	 * @param pnl
	 * @param fullColored
	 * @return
	 */
	public static BufferedImage convertPanelToImage(JKPanel<?> pnl, int width, int height) {
		try {
			BufferedImage img = ImageUtil.getCompatibleImage(pnl.getWidth(), pnl.getHeight());// new
			// BufferedImage(width,height,
			// type);

			Robot robot = new Robot();
			img = robot.createScreenCapture(getDefaultMainFrame().getBounds());
			img = ImageUtil.scaleNewerWay(img, false, width, height);
			return img;
		} catch (AWTException e) {
		}
		return null;
	}

	/**
	 * @param message
	 * @param ex
	 */
	public static void showErrorDialog(String message, Throwable ex) {
		showErrorDialog(message, ex, getDefaultMainFrame());

	}
	
	public static void showErrorDialog(String message, Throwable ex,boolean color) {
		showErrorDialog(message, ex, getDefaultMainFrame(),color);

	}

	/**
	 * @return
	 */
	public static Locale getDefaultLocale() {
		return new Locale(defaultLocale, "JO");
	}

	public static JFileChooser getDefaultFileChooser() {
		return chooser;
	}

	public static boolean showConfirmationDialog(String key, String extraInfo) {
		String str = Lables.get(key, true);
		return showConfirmationDialog(str + "\n" + extraInfo);
	}

	public static void showUserErrorDialog(String messge, String extraInfo) {
		String str = Lables.get(messge, true);
		showUserErrorDialog(str + "\n" + extraInfo);

	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String fixTwoLinesIssue(String str) {
		String arr[] = str.split(" ");
		String result = "<html><body>" + arr[0];
		for (int i = 1; i < arr.length; i++) {
			result += "<br>" + arr[i];
		}
		result += "</body></html>";
		return result;
	}

	/**
	 * @param browser
	 * @param string
	 * @return
	 */
	public static JKFrame showPanelFrame(JKPanel<?> panel, String title) {
		JKFrame frame = new JKFrame(title);
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		return frame;
	}

	/**
	 * 
	 * @param text
	 * @param fontSize
	 * @return
	 */
	public static int calculateTextSize(String text, int fontSize) {
		JLabel lbl = new JLabel(text);
		return (int) lbl.getPreferredSize().getWidth();
		// java.awt.Font font=new java.awt.Font("Arial",Font.BOLD,fontSize);
		// FontMetrics metrics =
		// Toolkit.getDefaultToolkit().getFontMetrics(font);
		// return metrics.stringWidth(text);
	}

	public static void testInternalFrame(JKInternalFrame frame) {
		JKFrame frm = new JKFrame();
		frm.setExtendedState(6);
		// JKDesktopPane pane = new JKDesktopPane();
		// pane.add(frame);
		frm.add(new JKPanel(frame));
		try {
			frame.initDefaults();
			frm.setVisible(true);
		} catch (PropertyVetoException e) {
			ExceptionUtil.handleException(e);
		}
	}

	public static void main(String[] args) {
		// System.out.println(colorToHex(Color.red));
		// System.out.println(showIntegerInput("test"));
		// System.out.println(showConfirmationDialog(new Frame(),
		// "line1\nline2"));
		System.out.println(getTextWidth("Jalal Kiswani", new Font("Arial", Font.BOLD, 12)));
	}

	/**
	 * 
	 * @param component
	 * @throws DaoException
	 */
	public static void resetComponent(Component component) throws DaoException {
		if (component instanceof BindingComponent) {
			((BindingComponent<?>) component).reset();
		} else if (component instanceof JComboBox) {
			((JComboBox<?>) component).setSelectedIndex(-1);
		} else if (component instanceof JList) {
			((JList<?>) component).setSelectedIndex(-1);
		} else if (component instanceof JRadioButton) {
			((JRadioButton) component).setSelected(false);
		}
	}

	/**
	 * 
	 * @param color
	 * @return
	 */
	public static String colorToHex(Color color) {
		return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
	}

	/**
	 * 
	 * @param btnAdd
	 * @param string
	 */
	public static void setHotKeyFoButton(JKButton btn, String shortcut) {
		setHotKeyFoButton(btn, shortcut, shortcut);
	}

	/**
	 * 
	 * @param string
	 */
	public static void pressKey(int key) {
		try {
			Robot robot = new Robot();
			robot.keyPress(key);
		} catch (AWTException e) {
			// ExceptionUtil.handleException(e);
		}
	}

	/**
	 * @param vkDown
	 * 
	 */
	public static void addFocusForwardKey(int button) {
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		Set<?> forwardKeys = manager.getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		Set<KeyStroke> newForwardKeys = new HashSet(forwardKeys);
		newForwardKeys.add(KeyStroke.getKeyStroke(button, 0));
		manager.setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
	}

	public static void addFocusBackKey(int button) {
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		Set<?> oldBackKeys = manager.getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
		Set<KeyStroke> backwordKeys = new HashSet(oldBackKeys);
		backwordKeys.add(KeyStroke.getKeyStroke(button, 0));
		manager.setDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwordKeys);
	}

	/**
	 * 
	 */
	public static void pressTab() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				pressKey(KeyEvent.VK_TAB);
			}
		};
		new Thread(r).start();
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static int showIntegerInput(String msg) {
		String input;
		while ((input = showInputDialog(msg)) != null) {
			try {
				return Integer.parseInt(input);
			} catch (NumberFormatException e) {
				SwingUtility.showUserErrorDialog("PLEASE_ENTER_NUMBERS_ONLY", false);
			}
		}
		return -1;
	}

	public static Dimension getScreenDimesion() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static Dimension getMaxWindowSize() {
		Dimension d = getScreenDimesion();
		return new Dimension((int) d.getWidth() - 100, (int) d.getHeight() - 100);
	}

	public static JFileChooser getFileChooser() {
		return chooser;
	}

	public static void printInstalledLookAndFeel() {
		LookAndFeelInfo[] look = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo lookAndFeelInfo : look) {
			System.out.println(lookAndFeelInfo.getClassName());
		}
	}

	public static int getTabPaneLeadingPlacement() {
		return isLeftOrientation() ? JTabbedPane.LEFT : JTabbedPane.RIGHT;
	}

	public static void reloadTable(int nextRow, QueryJTable tbl) {
		tbl.setSelectedRow(nextRow);
		tbl.reloadData();
	}

	/**
	 * 
	 * @param btn
	 * @param obj
	 * @param methodName
	 * @author mkiswani
	 */
	public static void addActionListener(AbstractButton btn, final Object obj, final String methodName) {
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ReflicationUtil.callMethod(obj, methodName);
				} catch (InvocationTargetException e1) {
					ExceptionUtil.handleException(e1.getCause());
				}
			}
		});

	}

	public static void closePanelWindow(JComponent comp) {
		if (comp.getRootPane() != null) {
			Window window = getWindow(comp);
			if (window != null) {
				window.dispose();
			}
		}
	}

	private static Window getWindow(JComponent comp) {
		if (comp.getRootPane() != null) {
			Container cont = comp.getRootPane().getParent();
			if (cont != null) {
				if (cont instanceof Window) {
					return ((Window) cont);
				}
			}
		}
		return null;
	}

	public static void applyDataSource(Container comp, DataSource manager) {
		Vector<BindingComponent> bindingComponents = SwingUtility.findBindingComponents(comp);
		for (BindingComponent bindingComponent : bindingComponents) {
			bindingComponent.setDataSource(manager);
		}
	}

	public static Vector<BindingComponent> findBindingComponents(Container cont) {
		Vector<BindingComponent> c = new Vector<BindingComponent>();
		Component[] components = cont.getComponents();
		for (Component component : components) {
			if (component instanceof BindingComponent) {
				c.add((BindingComponent) component);
			}
			if (component instanceof Container) {
				c.addAll(findBindingComponents((Container) component));
			}
		}
		return c;
	}

	public static BindingComponent findBindingComponent(Container cont, String componentNam) {
		Vector<BindingComponent> components = findBindingComponents(cont);
		for (BindingComponent bindingComponent : components) {
			if (bindingComponent.getName() != null && bindingComponent.getName().equals(componentNam)) {
				return bindingComponent;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param component
	 * @throws DaoException
	 */
	public static void resetComponent(Object component) throws DaoException {
		if (component instanceof BindingComponent) {
			((BindingComponent) component).reset();
		} else if (component instanceof JComboBox) {
			((JComboBox) component).setSelectedIndex(-1);
		} else if (component instanceof JList) {
			((JList) component).setSelectedIndex(-1);
		} else if (component instanceof JRadioButton) {
			((JRadioButton) component).setSelected(false);
		}
	}

	/**
	 * 
	 * @param xml
	 * @param string
	 */
	public static void showEncodedComponent(String xml, String title) {
		if (xml == null || xml.trim().equals("")) {
			return;
		}
		Object object = GeneralUtility.toObject(xml);
		if (object instanceof Window) {
			Window window = (Window) object;
			window.pack();
			enableContainer(window, false);
			window.setVisible(true);
		} else if (object instanceof JPanel) {
			JPanel panel = (JPanel) object;
			enableContainer(panel, false);
			SwingUtility.showPanelInDialog(panel, title);
		} else if (object instanceof JComponent) {
			FSPanel pnl = new FSPanel((JComponent) object);
			enableContainer(pnl, false);
			SwingUtility.showPanelInDialog(pnl, title);
		} else {
			System.err.println(object.getClass().getName() + " cannot be viewed");
		}
	}

	/**
	 * 
	 * @param text
	 * @param bold
	 * @return
	 */
	public static int getTextWidth(String text, boolean bold) {
		Font font = UIManager.getFont("Label.font");
		if (bold) {
			font = font.deriveFont(Font.BOLD);
		}
		return getTextWidth(text, font);
	}

	// /////////////////////////////////////////////////////////////////////////
	public static void showFrame(String frameClassName, JDesktopPane pane) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, PropertyVetoException {
		if (frameClassName != null && !frameClassName.trim().equals("")) {
			Object instance = Class.forName(frameClassName).newInstance();
			if (instance instanceof JFrame) {
				JFrame frame = (JFrame) instance;
				if (!frame.isVisible()) {
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
				}
			} else if (instance instanceof JKInternalFrame) {
				JKInternalFrame frm = (JKInternalFrame) instance;
				pane.add(frm);
				if (!frm.isVisible()) {
					frm.initDefaults();
				}
			} else {
				System.err.println(frameClassName + " is not instanceof JFrame");
			}
		}
	}

	/**
	 * 
	 * @param text
	 * @param font
	 * @return
	 */
	public static int getTextWidth(String text, Font font) {
		JLabel lbl = new JLabel(text);
		lbl.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		lbl.setFont(font);
		return (int) lbl.getPreferredSize().getWidth();
		// get metrics from the graphics
		// FontMetrics metrics = new FontMetrics(font){};
		// get the height of a line of text in this
		// font and render context
		// int hgt = metrics.getHeight();
		// get the advance of my text in this font
		// and render context
		// int adv = metrics.stringWidth(text);
		// calculate the size of a box to hold the
		// text with some padding.
		// Dimension size = new Dimension(adv+2, hgt+2);
		// return size;
	}

	/**
	 * 
	 * @param obj
	 */
	public static void testComponentSerialization(Object obj) {
		String xml = GeneralUtility.toXml(obj);
		// Object object = GeneralUtility.toObject(xml);
		showEncodedComponent(xml, "Test");
	}

	public static void setGeneralStatus(String status) {
		ApplicationFrame applicationFrame = ApplicationManager.getInstance().getApplication().getApplicationFrame();
		if (applicationFrame != null) {
			applicationFrame.setGeneralStatus(status);
		}
	}

	public static void setUserStatus(String status) {
		ApplicationManager.getInstance().getApplication().getApplicationFrame().setUserStatus(status);
	}

	public static void setSystemStatus(String status) {
		ApplicationManager.getInstance().getApplication().getApplicationFrame().setSystemStatus(status);
	}

	public static Dimension getWindowActualSize(Window window) {
		if (window.isVisible()) {
			return window.getSize();
		}
		if (window instanceof Frame) {
			Frame frame = (Frame) window;
			if (frame.getExtendedState() == Frame.MAXIMIZED_BOTH) {
				return Toolkit.getDefaultToolkit().getScreenSize();
			}
		}
		return window.getSize();
	}

	public static void maximumizBoth(Window window) {
		if (window instanceof Frame) {
			Frame frm = (Frame) window;
			frm.setExtendedState(Frame.MAXIMIZED_BOTH);
		} else {
			window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		}

	}

	public static void testMenuItem(String name) throws NotAllowedOperationException, SecurityException, UIOPanelCreationException {
		com.fs.commons.security.SecurityManager.setCurrentUser(new User(1));
		MenuItem item = ApplicationManager.getInstance().getApplication().findMenuItem(name);
		// PnlStudentManagement pnl=new PnlStudentManagement();
		// pnl.setTableMeta(AbstractTableMetaFactory.getTableMeta("reg_active_students"));
		SwingUtility.testPanel((JPanel) item.createPanel());
	}

	public static boolean isVisibleOnScreen(JComponent component) {
		Window window = getWindow(component);
		if (window != null) {
			return window.isVisible();
		}
		return false;
	}

	public static void resetComponents() {
//		NativeInterface.close();
//		NativeInterface.open();
	}

	// public static void setLookAndFeel() {
	// try {
	// UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
	// //
	// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	// // ColorUIResource disabledBackground = new ColorUIResource(Color.white);
	// // UIManager.put("ComboBox.disabledBackground", disabledBackground);
	// // ColorUIResource disabledForeground = new ColorUIResource(Color.black);
	// // UIManager.put("ComboBox.disabledForeground", disabledForeground);
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
}
