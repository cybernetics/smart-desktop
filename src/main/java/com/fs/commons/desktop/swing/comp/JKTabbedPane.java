/**
 * 
 */
package com.fs.commons.desktop.swing.comp;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;

/**
 * @author u087
 * 
 */
public class JKTabbedPane extends JTabbedPane {
	// private static final Color SELECTED_FOREGROUND_COLOR = new Color(22, 125,
	// 219);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static {
		UIManager.put("TabbedPane.selected", SwingUtility.getDefaultBackgroundColor());
	}

	/**
	 * 
	 */
	public JKTabbedPane() {
		init();

	}

	void init() {
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		setOpaque(false);
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				transferFocus();
			}
		});
	}

	/**
	 * 
	 */
	@Override
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
		getSelectedComponent().requestFocus();
	}

	/**
	 * 
	 * @param name
	 * @param icon
	 * @param panel
	 */
	@Override
	public void addTab(String title, Icon icon, Component component) {
		super.addTab(Lables.get(title,true), icon, component);
	}

	/**
	 * \
	 * 
	 * @param caption
	 * @param iconName
	 * @param pnlMaster
	 */
	public void addTab(String title, String iconName, Component component) {
		if (iconName != null && !iconName.equals("") && GeneralUtility.getIconURL(iconName) != null) {
			// ImageIcon icon=new
			// ImageIcon(GeneralUtility.getIconURL(iconName));
			this.addTab(title, GeneralUtility.getIcon(iconName), component);
		} else {
			this.addTab(title, component);
		}
	}

	/**
	 * 
	 * @param caption
	 * @param panel
	 */
	@Override
	public void addTab(String title, Component component) {
		super.addTab(Lables.get(title,true), component);
	}

	public JKTabbedPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
		// TODO Auto-generated constructor stub
	}

	public JKTabbedPane(int tabPlacement) {
		super(tabPlacement);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		// TODO Auto-generated method stub
		super.insertTab(Lables.get(title,true), icon, component, tip, index);
	}

}
