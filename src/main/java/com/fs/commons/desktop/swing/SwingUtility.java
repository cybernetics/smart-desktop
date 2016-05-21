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
package com.fs.commons.desktop.swing;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.application.ui.menu.MenuItem;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.connection.JKDataSource;
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
import com.fs.commons.util.GeneralUtility;
import com.fs.commons.util.ImageUtil;
import com.fs.commons.util.ReflicationUtil;
import com.jgoodies.looks.windows.WindowsLookAndFeel;
import com.jk.exceptions.JKNotAllowedOperationException;
import com.jk.exceptions.handler.JKExceptionUtil;
import com.jk.security.JKUser;

public class SwingUtility {

	static {
		try {
			// PlasticXPLookAndFeel.set3DEnabled(true);
			// PlasticXPLookAndFeel.setHighContrastFocusColorsEnabled(true);
			// PlasticXPLookAndFeel.setPlasticTheme(new SkyBlue());

			// final ColorUIResource disabledBackground = new
			// ColorUIResource(Color.white);
			// UIManager.put("ComboBox.disabledBackground", disabledBackground);
			// final ColorUIResource disabledForeground = new
			// ColorUIResource(Color.black);
			// UIManager.put("ComboBox.disabledForeground", disabledForeground);
			UIManager.setLookAndFeel(new WindowsLookAndFeel());
			// NativeInterface.open();

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
		} catch (final Exception e) {
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

	private static JFileChooser chooser = new JFileChooser(".");

	private static Font DEFAULT_TITLE_FONT = new Font("Times", Font.BOLD, 14);
	private static final Font DEFAULT_TITLE_RIGHT_FONT = new Font("Arial", Font.BOLD, 14);

	private static final Border DEFAULT_EMPTY_BORDER = new EmptyBorder(5, 5, 5, 5);

	private static final Border DEFAULT_EMPTY_SELECTED_BORDER = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
			DEFAULT_EMPTY_BORDER);

	/**
	 *
	 * @param btn
	 * @param obj
	 * @param methodName
	 * @author mkiswani
	 */
	public static void addActionListener(final AbstractButton btn, final Object obj, final String methodName) {
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					ReflicationUtil.callMethod(obj, methodName);
				} catch (final InvocationTargetException e1) {
					JKExceptionUtil.handle(e1.getCause());
				}
			}
		});

	}

	public static void addFocusBackKey(final int button) {
		final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		final Set<?> oldBackKeys = manager.getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
		final Set<KeyStroke> backwordKeys = new HashSet(oldBackKeys);
		backwordKeys.add(KeyStroke.getKeyStroke(button, 0));
		manager.setDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwordKeys);
	}

	/**
	 * @param vkDown
	 *
	 */
	public static void addFocusForwardKey(final int button) {
		final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		final Set<?> forwardKeys = manager.getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		final Set<KeyStroke> newForwardKeys = new HashSet(forwardKeys);
		newForwardKeys.add(KeyStroke.getKeyStroke(button, 0));
		manager.setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
	}

	/**
	 *
	 * @param panel
	 * @param title
	 * @throws HeadlessException
	 */
	private static void addPanelToDialog(final JDialog dialog, final JPanel panel, final String title) throws HeadlessException {
		// panel.setBorder(createTitledBorder(title));
		dialog.add(new JKScrollPane(panel));
		panel.setOpaque(true);
		dialog.applyComponentOrientation(getDefaultComponentOrientation());
		panel.grabFocus();
	}

	/**
	 *
	 * @param panel
	 * @param title
	 * @throws HeadlessException
	 */
	public static void addPanelToFrame(final JFrame frame, final JPanel panel, final String title) throws HeadlessException {
		frame.add(panel);
		frame.setTitle(title);
		frame.applyComponentOrientation(getDefaultComponentOrientation());
	}

	public static void applyDataSource(final Container comp, final JKDataSource manager) {
		final Vector<BindingComponent> bindingComponents = SwingUtility.findBindingComponents(comp);
		for (final BindingComponent bindingComponent : bindingComponents) {
			bindingComponent.setDataSource(manager);
		}
	}

	/**
	 * @param image
	 * @param scaled
	 * @return
	 */
	public static JPanel buildImagePanel(final byte[] image, final int scaled) {
		return buildImagePanel(new ByteArrayInputStream(image), scaled);
	}

	public static JPanel buildImagePanel(final InputStream in, final int scaled) {
		BufferedImage image = null;
		try {
			image = javax.imageio.ImageIO.read(in);
			return new ImagePanel(image, scaled);
		} catch (final IOException ex) {
			return new JPanel();
		}
	}

	/**
	 * @param url
	 * @param scaled
	 * @return
	 */
	public static ImagePanel buildImagePanel(final URL url, final int scaled) {
		BufferedImage image = null;
		try {
			if (url != null) {
				image = getImage(url);
				return new ImagePanel(image, scaled);
			}
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		return new ImagePanel();
	}

	/**
	 *
	 * @param text
	 * @param fontSize
	 * @return
	 */
	public static int calculateTextSize(final String text, final int fontSize) {
		final JLabel lbl = new JLabel(text);
		return (int) lbl.getPreferredSize().getWidth();
		// java.awt.Font font=new java.awt.Font("Arial",Font.BOLD,fontSize);
		// FontMetrics metrics =
		// Toolkit.getDefaultToolkit().getFontMetrics(font);
		// return metrics.stringWidth(text);
	}

	/*
	 *
	 */
	public static void closePanel(final JPanel pnl) {
		// Container cont = pnl.getParent();
		// cont.remove(pnl);
		// cont.validate();
		// cont.repaint();
		getDefaultMainFrame().showHomePanel();
	}

	/**
	 *
	 */
	public static void closePanelDialog(final JComponent comp) {
		if (comp.getRootPane() != null) {
			final Container cont = comp.getRootPane().getParent();
			if (cont != null) {
				if (cont instanceof JDialog) {
					((JDialog) cont).dispose();
				}
			}
		}
	}

	public static void closePanelWindow(final JComponent comp) {
		if (comp.getRootPane() != null) {
			final Window window = getWindow(comp);
			if (window != null) {
				window.dispose();
			}
		}
	}

	/**
	 *
	 * @param color
	 * @return
	 */
	public static String colorToHex(final Color color) {
		return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
	}

	/**
	 *
	 * @param pnl
	 * @param fullColored
	 * @return
	 */
	public static BufferedImage convertPanelToImage(final JKPanel<?> pnl, final int width, final int height) {
		try {
			BufferedImage img = ImageUtil.getCompatibleImage(pnl.getWidth(), pnl.getHeight());// new
			// BufferedImage(width,height,
			// type);

			final Robot robot = new Robot();
			img = robot.createScreenCapture(getDefaultMainFrame().getBounds());
			img = ImageUtil.scaleNewerWay(img, false, width, height);
			return img;
		} catch (final AWTException e) {
		}
		return null;
	}

	/**
	 *
	 * @return
	 */
	public static void createLineBorder(final JKPanel<?> pnl) {
		pnl.setBorder(new LineBorder(Color.lightGray));

	}

	public static Border createTitledBorder(final String title) {
		final TitledBorder b = BorderFactory.createTitledBorder("");
		b.setTitle(Lables.get(title, true));
		b.setTitleJustification(TitledBorder.DEFAULT_JUSTIFICATION);
		b.setTitlePosition(TitledBorder.CENTER);
		b.setTitleColor(Colors.TITLE_BORDER_BG);
		return b;
	}

	/**
	 *
	 * @param cont
	 * @param enable
	 */
	public static void enableContainer(final Container cont, final boolean enable) {
		final int count = cont.getComponentCount();
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

	public static BindingComponent findBindingComponent(final Container cont, final String componentNam) {
		final Vector<BindingComponent> components = findBindingComponents(cont);
		for (final BindingComponent bindingComponent : components) {
			if (bindingComponent.getName() != null && bindingComponent.getName().equals(componentNam)) {
				return bindingComponent;
			}
		}
		return null;
	}

	public static Vector<BindingComponent> findBindingComponents(final Container cont) {
		final Vector<BindingComponent> c = new Vector<BindingComponent>();
		final Component[] components = cont.getComponents();
		for (final Component component : components) {
			if (component instanceof BindingComponent) {
				c.add((BindingComponent) component);
			}
			if (component instanceof Container) {
				c.addAll(findBindingComponents((Container) component));
			}
		}
		return c;
	}

	/**
	 *
	 * @param str
	 * @return
	 */
	public static String fixTwoLinesIssue(final String str) {
		final String arr[] = str.split(" ");
		String result = "<html><body>" + arr[0];
		for (int i = 1; i < arr.length; i++) {
			result += "<br>" + arr[i];
		}
		result += "</body></html>";
		return result;
	}

	private static Window getActiveWindow() {
		final KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		final Window window = keyboardFocusManager.getActiveWindow();
		if (window == null) {
			return getDefaultMainFrame();
		}
		return window;
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
	 * @return
	 */
	public static Color getDefaultBackgroundColor() {
		return defaultBackgroundColor;
	}

	/**
	 *
	 * @return
	 */

	public static ComponentOrientation getDefaultComponentOrientation() {
		return defaultComponentOrientation;
	}

	public static JFileChooser getDefaultFileChooser() {
		return chooser;
	}

	/**
	 * @return
	 */
	public static Locale getDefaultLocale() {
		return new Locale(defaultLocale, "JO");
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

	private static JScrollPane getDialogViewComponent(final String string) {
		return getDialogViewComponent(string, false);
	}

	// private static Font getDefaultFont() {
	// return defaultFont;
	// }

	private static JScrollPane getDialogViewComponent(final String string, final boolean color) {
		final JKTextArea txt = new JKTextArea();
		txt.setText(Lables.get(string, true));
		txt.setEditable(false);

		if (color) {
			txt.setForeground(Color.red);
			txt.setFont(new Font("Tahoma", Font.BOLD, 22));
		}
		final JScrollPane jScrollPane = new JScrollPane(txt);
		jScrollPane.getViewport().setPreferredSize(new Dimension(500, 200));
		return jScrollPane;
	}

	/**
	 *
	 * @return
	 */
	public static JDialog getEmptyDialog() {
		return emptyDialog;
	}

	public static JFileChooser getFileChooser() {
		return chooser;
	}

	/**
	 *
	 * @return
	 */
	public static JPanel getHomePanel() {
		return getDefaultMainFrame().getHomePanel();
	}

	public static BufferedImage getImage(final InputStream in) throws IOException {
		BufferedImage image;
		image = javax.imageio.ImageIO.read(in);
		return image;
	}

	public static BufferedImage getImage(final String imageName) throws IOException {
		return getImage(GeneralUtility.getFileURI(imageName).toURL());
	}

	public static BufferedImage getImage(final URL url) throws IOException {
		BufferedImage image;
		image = javax.imageio.ImageIO.read(url);
		return image;
	}

	public static Dimension getMaxWindowSize() {
		final Dimension d = getScreenDimesion();
		return new Dimension((int) d.getWidth() - 100, (int) d.getHeight() - 100);
	}

	public static Dimension getScreenDimesion() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static int getTabPaneLeadingPlacement() {
		return isLeftOrientation() ? JTabbedPane.LEFT : JTabbedPane.RIGHT;
	}

	/**
	 *
	 * @param text
	 * @param bold
	 * @return
	 */
	public static int getTextWidth(final String text, final boolean bold) {
		Font font = UIManager.getFont("Label.font");
		if (bold) {
			font = font.deriveFont(Font.BOLD);
		}
		return getTextWidth(text, font);
	}

	/**
	 *
	 * @param text
	 * @param font
	 * @return
	 */
	public static int getTextWidth(final String text, final Font font) {
//		final JLabel lbl = new JLabel(text);
//		lbl.setBorder(g);
//		lbl.setFont(font);
//		return (int) lbl.getPreferredSize().getWidth();
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
		int textheight = (int)(font.getStringBounds(text, frc).getHeight());
		return textwidth;
	}

	private static Window getWindow(final JComponent comp) {
		if (comp.getRootPane() != null) {
			final Container cont = comp.getRootPane().getParent();
			if (cont != null) {
				if (cont instanceof Window) {
					return (Window) cont;
				}
			}
		}
		return null;
	}

	public static Dimension getWindowActualSize(final Window window) {
		if (window.isVisible()) {
			return window.getSize();
		}
		if (window instanceof Frame) {
			final Frame frame = (Frame) window;
			if (frame.getExtendedState() == Frame.MAXIMIZED_BOTH) {
				return Toolkit.getDefaultToolkit().getScreenSize();
			}
		}
		return window.getSize();
	}

	/**
	 *
	 * @return
	 */
	public static boolean isLeftOrientation() {
		return defaultComponentOrientation == ComponentOrientation.LEFT_TO_RIGHT;
	}

	public static boolean isVisibleOnScreen(final JComponent component) {
		final Window window = getWindow(component);
		if (window != null) {
			return window.isVisible();
		}
		return false;
	}

	public static void main(final String[] args) {
		// System.out.println(colorToHex(Color.red));
		// System.out.println(showIntegerInput("test"));
		// System.out.println(showConfirmationDialog(new Frame(),
		// "line1\nline2"));
		System.out.println(getTextWidth("Jalal Kiswani", new Font("Arial", Font.BOLD, 12)));
	}

	public static void maximumizBoth(final Window window) {
		if (window instanceof Frame) {
			final Frame frm = (Frame) window;
			frm.setExtendedState(Frame.MAXIMIZED_BOTH);
		} else {
			window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		}

	}

	/**
	 * i seperated it in different method to avoid affecting other components
	 *
	 * @param comp
	 */
	public static void packJFrameWindow(final JComponent comp) {
		if (comp.getRootPane() != null) {
			final Container cont = comp.getRootPane().getParent();
			if (cont instanceof JFrame) {
				((JFrame) cont).pack();
				((JFrame) cont).setLocationRelativeTo(null);
			}
		}
	}

	public static void packWindow(final JComponent comp) {

		if (comp.getRootPane() != null) {
			final Container cont = comp.getRootPane().getParent();
			if (cont != null) {
				if (cont instanceof JDialog) {
					((JDialog) cont).pack();
					((JDialog) cont).setLocationRelativeTo(null);
				}
			}
		}
	}

	/**
	 *
	 * @param string
	 */
	public static void pressKey(final int key) {
		try {
			final Robot robot = new Robot();
			robot.keyPress(key);
		} catch (final AWTException e) {
			// ExceptionUtil.handle(e);
		}
	}

	/**
	 *
	 */
	public static void pressTab() {
		final Runnable r = new Runnable() {

			@Override
			public void run() {
				pressKey(KeyEvent.VK_TAB);
			}
		};
		new Thread(r).start();
	}

	public static void printInstalledLookAndFeel() {
		final LookAndFeelInfo[] look = UIManager.getInstalledLookAndFeels();
		for (final LookAndFeelInfo lookAndFeelInfo : look) {
			System.out.println(lookAndFeelInfo.getClassName());
		}
	}

	public static void reloadTable(final int nextRow, final QueryJTable tbl) {
		tbl.setSelectedRow(nextRow);
		tbl.reloadData();
	}

	/**
	 *
	 * @param component
	 * @throws JKDataAccessException
	 */
	public static void resetComponent(final Component component) throws JKDataAccessException {
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
	 * @param component
	 * @throws JKDataAccessException
	 */
	public static void resetComponent(final Object component) throws JKDataAccessException {
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

	public static void resetComponents() {
		// NativeInterface.close();
		// NativeInterface.open();
	}

	public static void setBoldFont(final Component comp) {
		comp.setFont(new Font("Tahoma", Font.BOLD, 10));
	}

	/**
	 *
	 * @param defaultBackgroundColor
	 */
	public static void setDefaultBackgroundColor(final Color defaultBackgroundColor) {
		SwingUtility.defaultBackgroundColor = defaultBackgroundColor;
	}

	/**
	 *
	 * @param orientation
	 */
	public static void setDefaultComponentOrientation(final ComponentOrientation orientation) {
		SwingUtility.defaultComponentOrientation = orientation;
		if (!isLeftOrientation()) {
			defaultLocale = "ar";
		} else {
			defaultLocale = "en";
		}
	}

	/**
	 * @param defaultLocale
	 *            the defaultLocale to set
	 */
	public static void setDefaultLocale(final String defaultLocale) {
		if (defaultLocale.equals("ar")) {
			setDefaultComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		} else {
			setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		}
		SwingUtility.defaultLocale = defaultLocale;
	}

	/**
	 *
	 * @param defaultMainFrame
	 */
	public static void setDefaultMainFrame(final JKFrame defaultMainFrame) {
		SwingUtility.defaultMainFrame = defaultMainFrame;
		defaultMainFrame.setLocale(getDefaultLocale());
	}

	/**
	 *
	 * @param comp
	 */
	public static void setFont(final Component comp) {
		// comp.setFont(defaultFont);
	}

	public static void setGeneralStatus(final String status) {
		final ApplicationFrame applicationFrame = ApplicationManager.getInstance().getApplication().getApplicationFrame();
		if (applicationFrame != null) {
			applicationFrame.setGeneralStatus(status);
		}
	}

	/**
	 *
	 * @param homePanel
	 */
	public static void setHomePanel(final JPanel homePanel) {
		getDefaultMainFrame().setHomePanel(homePanel);
	}

	/**
	 *
	 * @param btn
	 * @param keyStroke
	 * @param actionName
	 */
	public static void setHotKeyFoButton(final AbstractButton btn, final String keyStroke, final String actionName) {
		// get the button's Action map
		final ActionMap amap = btn.getActionMap();
		// add an action to the button's action map
		// and give it a name(it can be any object not just String)
		amap.put(actionName, new AbstractAction() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				// call your a method that contains your action code
				if (btn.isVisible() && btn.isEnabled()) {
					btn.doClick();
				}
			}
		});
		// get the input map for the button
		final InputMap imap = btn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		// add a key stroke associated with an action in the action map(action
		// name).
		// imap.put(KeyStroke.getKeyStroke("F1"),"ActionName");
		// you can do the same for more than one key.
		imap.put(KeyStroke.getKeyStroke(keyStroke), actionName);
	}

	/**
	 *
	 * @param btnAdd
	 * @param string
	 */
	public static void setHotKeyFoButton(final JKButton btn, final String shortcut) {
		setHotKeyFoButton(btn, shortcut, shortcut);
	}

	/**
	 *
	 * @param btn
	 * @param keyStroke
	 * @param actionName
	 */
	public static void setHotKeyForFocus(final JComponent comp, final String keyStroke, final String actionName) {
		// get the button's Action map
		final ActionMap amap = comp.getActionMap();
		// add an action to the button's action map
		// and give it a name(it can be any object not just String)
		amap.put(actionName, new AbstractAction() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				// call your a method that contains your action code
				comp.requestFocus();
			}
		});
		// get the input map for the button
		final InputMap imap = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		// add a key stroke associated with an action in the action map(action
		// name).
		// imap.put(KeyStroke.getKeyStroke("F1"),"ActionName");
		// you can do the same for more than one key.
		imap.put(KeyStroke.getKeyStroke(keyStroke), actionName);
	}

	public static void setSystemStatus(final String status) {
		ApplicationManager.getInstance().getApplication().getApplicationFrame().setSystemStatus(status);
	}

	public static void setUserStatus(final String status) {
		ApplicationManager.getInstance().getApplication().getApplicationFrame().setUserStatus(status);
	}

	public static boolean showConfirmationDialog(final JDialog dialog, final String message) {
		final int choice = JOptionPane.showConfirmDialog(dialog, Lables.get(message, true), Lables.get("WARNING"), JOptionPane.YES_NO_OPTION);
		return choice == JOptionPane.YES_OPTION;
	}

	public static boolean showConfirmationDialog(final String message) {
		return showConfirmationDialog(getDefaultMainFrame(), message);
	}

	/**
	 * @1.1
	 * 
	 * @param message
	 * @return
	 */
	public static boolean showConfirmationDialog(final String message[]) {
		return showConfirmationDialog(getDefaultMainFrame(), message);
	}

	public static boolean showConfirmationDialog(final String key, final String extraInfo) {
		final String str = Lables.get(key, true);
		return showConfirmationDialog(str + "\n" + extraInfo);
	}

	/**
	 *
	 * @param dialog
	 * @param message
	 * @return
	 */
	public static boolean showConfirmationDialog(final Window window, final String message) {
		// int choice = JOptionPane.showConfirmDialog(window,
		// (Lables.get(message)), Lables.get("WARNING"),
		// JOptionPane.YES_NO_OPTION);

		final String no = Lables.get("No");
		final String yes = Lables.get("Yes");
		final int selection = JOptionPane.showOptionDialog(window, Lables.get(message, true), Lables.get("WARNING"), JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, new String[] { yes, no }, no);
		return selection == JOptionPane.YES_OPTION;
	}

	/**
	 * @1.1
	 * 
	 * @param window
	 * @param messages
	 * @return
	 */
	public static boolean showConfirmationDialog(final Window window, final String[] messages) {
		final StringBuffer concateMessage = new StringBuffer();
		final String stringLabel = "";
		for (final String message : messages) {
			concateMessage.append(Lables.get(message, true));
			concateMessage.append("\n");
		}
		concateMessage.append(stringLabel);
		final int choice = JOptionPane.showConfirmDialog(window, Lables.get(concateMessage.toString(), true), Lables.get("WARNING"),
				JOptionPane.YES_NO_OPTION);
		return choice == JOptionPane.YES_OPTION;
	}

	public static void showDatabaseErrorDialog(final JDialog dialog, final String message, final Exception ex) {
		JOptionPane.showMessageDialog(dialog, getDialogViewComponent(message));
		throw new RuntimeException(ex);
	}

	public static void showDatabaseErrorDialog(final String message, final Exception ex) {
		showDatabaseErrorDialog(emptyDialog, message, ex);
	}

	/**
	 *
	 * @param xml
	 * @param string
	 */
	public static void showEncodedComponent(final String xml, final String title) {
		if (xml == null || xml.trim().equals("")) {
			return;
		}
		final Object object = GeneralUtility.toObject(xml);
		if (object instanceof Window) {
			final Window window = (Window) object;
			window.pack();
			enableContainer(window, false);
			window.setVisible(true);
		} else if (object instanceof JPanel) {
			final JPanel panel = (JPanel) object;
			enableContainer(panel, false);
			SwingUtility.showPanelInDialog(panel, title);
		} else if (object instanceof JComponent) {
			final FSPanel pnl = new FSPanel((JComponent) object);
			enableContainer(pnl, false);
			SwingUtility.showPanelInDialog(pnl, title);
		} else {
			System.err.println(object.getClass().getName() + " cannot be viewed");
		}
	}

	/**
	 * @param message
	 * @param ex
	 */
	public static void showErrorDialog(final String message, final Throwable ex) {
		showErrorDialog(message, ex, getDefaultMainFrame());

	}

	public static void showErrorDialog(final String message, final Throwable ex, final boolean color) {
		showErrorDialog(message, ex, getDefaultMainFrame(), color);

	}

	/**
	 *
	 * @param error
	 * @param parent
	 */
	public static void showErrorDialog(final String error, final Throwable e, final Window parent) {
		JOptionPane.showMessageDialog(parent, getDialogViewComponent(error));
		throw new RuntimeException(e);
	}

	public static void showErrorDialog(final String error, final Throwable e, final Window parent, final boolean color) {
		JOptionPane.showMessageDialog(parent, getDialogViewComponent(error, true));
		throw new RuntimeException(e);
	}

	// /////////////////////////////////////////////////////////////////////////
	public static void showFrame(final String frameClassName, final JDesktopPane pane)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, PropertyVetoException {
		if (frameClassName != null && !frameClassName.trim().equals("")) {
			final Object instance = Class.forName(frameClassName).newInstance();
			if (instance instanceof JFrame) {
				final JFrame frame = (JFrame) instance;
				if (!frame.isVisible()) {
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
				}
			} else if (instance instanceof JKInternalFrame) {
				final JKInternalFrame frm = (JKInternalFrame) instance;
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
	 * @param parentComponent
	 * @param message
	 * @return
	 * @throws HeadlessException
	 */
	public static String showInputDialog(final Component parentComponent, final String message) throws HeadlessException {
		return JOptionPane.showInputDialog(parentComponent, Lables.get(message, true));
	}

	/**
	 *
	 * @param parentComponent
	 * @param message
	 * @return
	 * @throws HeadlessException
	 */
	public static String showInputDialog(final String message) throws HeadlessException {
		return showInputDialog(defaultMainFrame, message);
	}

	/**
	 *
	 * @param string
	 * @return
	 */
	public static int showIntegerInput(final String msg) {
		String input;
		while ((input = showInputDialog(msg)) != null) {
			try {
				return Integer.parseInt(input);
			} catch (final NumberFormatException e) {
				SwingUtility.showUserErrorDialog("PLEASE_ENTER_NUMBERS_ONLY", false);
			}
		}
		return -1;
	}

	public static void showMessageDialog(final String message, final Throwable ex) {
		JOptionPane.showMessageDialog(getDefaultMainFrame(), Lables.get(message, true));
		throw new RuntimeException(message, ex);
	}

	/**
	 * @param browser
	 * @param string
	 * @return
	 */
	public static JKFrame showPanelFrame(final JKPanel<?> panel, final String title) {
		final JKFrame frame = new JKFrame(title);
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		return frame;
	}

	/**
	 *
	 * @param panel
	 * @param title
	 * @return
	 */
	public static JDialog showPanelInDialog(final JPanel panel, final String title) {
		return showPanelInDialog(panel, title, true);
	}

	/**
	 *
	 * @param pnl
	 * @param title
	 * @param modal
	 * @return
	 */
	public static JDialog showPanelInDialog(final JPanel pnl, final String title, final boolean modal) {
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
	public static JKDialog showPanelInDialog(final JPanel pnl, final String title, final boolean modal, final JKPanel<?> parent) {
		return showPanelInDialog(pnl, title, modal, parent, null);
	}

	public static JKDialog showPanelInDialog(final JPanel pnl, final String title, final boolean modal, final JKPanel<?> parent,
			final Dimension dimension) {
		JKDialog dialog = null;
		if (parent != null && parent.getRootPane() != null) {
			final Container cont = parent.getRootPane().getParent();
			if (cont != null) {
				if (cont instanceof JDialog) {
					dialog = new JKDialog((JDialog) cont, title);
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

	public static void showSuccessDialog(final JDialog parent, final String string) {
		JOptionPane.showMessageDialog(parent, Lables.get(string, true));
	}

	public static void showSuccessDialog(final String string) {
		JOptionPane.showMessageDialog(getDefaultMainFrame(), getDialogViewComponent(string));
	}

	public static void showUserErrorDialog(final JDialog parent, final String message, final Exception ex) {
		JOptionPane.showMessageDialog(parent, getDialogViewComponent(message));
	}

	public static void showUserErrorDialog(final JFrame parent, final String message, final Exception ex) {
		JOptionPane.showMessageDialog(parent, getDialogViewComponent(message));
		ex.printStackTrace();
	}

	public static void showUserErrorDialog(final String string) {
		showUserErrorDialog(string, true);
	}

	public static void showUserErrorDialog(final String string, final boolean throwRuntimeException) {
		final JScrollPane jScrollPane = getDialogViewComponent(string);

		JOptionPane.showMessageDialog(getDefaultMainFrame(), jScrollPane);
		if (throwRuntimeException) {
			throw new RuntimeException(string);
		}
	}

	/**
	 *
	 * @param message
	 * @param ex
	 */
	public static void showUserErrorDialog(final String message, final Exception ex) {
		showUserErrorDialog(SwingUtility.getDefaultMainFrame(), message, ex);
		throw new RuntimeException(message);
	}

	public static void showUserErrorDialog(final String messge, final String extraInfo) {
		final String str = Lables.get(messge, true);
		showUserErrorDialog(str + "\n" + extraInfo);

	}

	/**
	 *
	 * @param obj
	 */
	public static void testComponentSerialization(final Object obj) {
		final String xml = GeneralUtility.toXml(obj);
		// Object object = GeneralUtility.toObject(xml);
		showEncodedComponent(xml, "Test");
	}

	public static void testInternalFrame(final JKInternalFrame frame) {
		final JKFrame frm = new JKFrame();
		frm.setExtendedState(6);
		// JKDesktopPane pane = new JKDesktopPane();
		// pane.add(frame);
		frm.add(new JKPanel(frame));
		try {
			frame.initDefaults();
			frm.setVisible(true);
		} catch (final PropertyVetoException e) {
			JKExceptionUtil.handle(e);
		}
	}

	public static void testMenuItem(final String name) throws JKNotAllowedOperationException, SecurityException, UIOPanelCreationException {
		com.jk.security.JKSecurityManager.setCurrentUser(new JKUser(1));
		final MenuItem item = ApplicationManager.getInstance().getApplication().findMenuItem(name);
		// PnlStudentManagement pnl=new PnlStudentManagement();
		// pnl.setTableMeta(AbstractTableMetaFactory.getTableMeta("reg_active_students"));
		SwingUtility.testPanel((JPanel) item.createPanel());
	}

	/**
	 *
	 * @param panel
	 *            JPanel
	 */
	public static void testPanel(final JPanel panel) {
		final JKFrame frame = new JKFrame();
		// frame.setExtendedState(frame.MAXIMIZED_BOTH);
		final JKMainPanel mainPanel = new JKMainPanel(new BorderLayout());
		mainPanel.add(panel);
		frame.add(mainPanel);
		frame.applyComponentOrientation(getDefaultComponentOrientation());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static BufferedImage toBufferedImage(final byte[] imageBytes) throws IOException {
		return javax.imageio.ImageIO.read(new ByteArrayInputStream(imageBytes));
	}

	public static Color hexToColor(String colorHex) {
		final String replace = colorHex.replace("#", "0x");
		// System.out.println(replace);
		return Color.decode(replace);
	}

	public static Font getDefaultTitleFont() {
		if (SwingUtility.isLeftOrientation()) {
			return DEFAULT_TITLE_FONT;
		}
		return DEFAULT_TITLE_RIGHT_FONT;
	}

	public static Border getDefaultEmptyBorder() {
		return DEFAULT_EMPTY_BORDER;
	}

	public static Border getDefaultEmptySelectedBorder() {
		return DEFAULT_EMPTY_SELECTED_BORDER;
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
