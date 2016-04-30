package com.fs.commons.desktop.swing.comp.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;

import com.fs.commons.application.ui.UIPanel;
import com.fs.commons.desktop.graphics.GraphicsFactory.GradientType;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKFrame;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.locale.Lables;

public class TitledPanel extends JKMainPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String title;
	private final String icon;
	protected UIPanel panel;

	JKMainPanel pnlTitleBar = new JKMainPanel(new GridLayout(1, 2, 2, 2));
	JKPanel<?> pnlTitle = new JKPanel<Object>(new FlowLayout(FlowLayout.LEADING));
	JKPanel<?> pnlButtons = new JKPanel<Object>(new FlowLayout(FlowLayout.TRAILING));

	JKButton btnAddToFavorites = new JKTitleButton("");
	JKButton btnShowInFrame = new JKTitleButton("");
	JKButton btnReload = new JKTitleButton("");
	JKButton btnClose = new JKTitleButton("");

	JKButton btnNext = new JKTitleButton(">");
	JKButton btnPrevious = new JKTitleButton("<");

	JKLabel lbl;

	/**
	 * 
	 * @param title
	 * @param panelFactory
	 * @param panelProp
	 */
	public TitledPanel(String title, UIPanel panel) {
		this(title, panel, null);
	}

	/**
	 * 
	 * @param title
	 * @param panelFactory
	 * @param panelProp
	 * @param icon
	 * @param cached
	 */
	public TitledPanel(String title, UIPanel panel, String icon) {
		this.title = title;
		this.panel = panel;
		this.icon = icon;
		init();
		showPanel();
	}

	/**
	 * 
	 */
	protected void showPanel() {
		add((Container)panel,BorderLayout.CENTER);
		validate();
		repaint();
		if(panel instanceof JInternalFrame){
			JInternalFrame f=(JInternalFrame) panel;
			javax.swing.plaf.InternalFrameUI ifu= f.getUI();
			((javax.swing.plaf.basic.BasicInternalFrameUI)ifu).setNorthPane(null);
			f.setVisible(true);
		}

	}

	/**
	 * 
	 */
	private void init() {
		setLayout(new BorderLayout());
		JKPanel<?> titleBar = getTitleBar();

		add(titleBar, BorderLayout.NORTH);
		applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());

	}

	/**
	 * 
	 * @return
	 */
	private JKPanel<?> getTitleBar() {
		initComponenets();
		pnlTitleBar.setGradientType(GradientType.HORIZENTAL);
		btnClose.setShortcut("control X", "");
		btnReload.setShortcut("control R", "");
		pnlTitle.add(lbl);
		
		pnlButtons.add(btnAddToFavorites);
		pnlButtons.add(btnShowInFrame);
		pnlButtons.add(btnReload);
		pnlButtons.add(btnPrevious);
		pnlButtons.add(btnNext);
		pnlButtons.add(btnClose);

		pnlTitleBar.add(pnlTitle);
		pnlTitleBar.add(pnlButtons);

		btnAddToFavorites.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleAddToFavorites();
			}
		});
		btnShowInFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleShowInFrame();
			}
		});
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleReload();
			}
		});

		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtility.getDefaultMainFrame().showHomePanel();
			}
		});
		btnNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleNext();
			}
		});
		btnPrevious.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handlePreviouse();
			}
		});
		return pnlTitleBar;
	}

	protected void handlePreviouse() {
		// TODO Auto-generated method stub

	}

	protected void handleNext() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	protected void handleShowInFrame() {
		JKFrame frame = SwingUtility.showPanelFrame(this, title);
		frame.setExtendedState(JKFrame.NORMAL);
	}

	/**
	 * 
	 */
	private void initComponenets() {
		// pnlTitleBar.setBorder(BorderFactory.createLineBorder(new Color(200,
		// 200, 200)));
		pnlTitleBar.setBackground(Colors.JK_TITLE_BAR_BG);// new Color(191, 215,
															// 255));
		pnlTitleBar.setPreferredSize(new Dimension(800, 35));
		lbl = new JKLabel();
		lbl.setText(Lables.get(title, true));
		lbl.setOpaque(false);
		lbl.setForeground(Colors.JK_TITLE_BAR_FG);// new Color(22, 125, 219));
		lbl.setPreferredSize(null);
		lbl.setFont(new Font("Arial", Font.BOLD, 16));
		lbl.setVerticalAlignment(SwingConstants.TOP);
		if (icon != null) {
			lbl.setIcon(icon);
		}
		pnlTitle.setOpaque(false);

		// Border emptyBorder = BorderFactory.createEmptyBorder(2,5,2,5);
		// btnAddToFavorites.setBorder(emptyBorder );
		// btnClose.setBorder(emptyBorder );
		// btnNext.setBorder(emptyBorder );
		// btnPrevious.setBorder(emptyBorder );
		// btnReload.setBorder(emptyBorder );
		// btnShowInFrame.setBorder(emptyBorder );

		btnShowInFrame.setToolTipText("POP_OUT_WINDOW");
		// btnReload.setBorder(null);
		// btnClose.setBorder(null);

		btnReload.setIcon("small_reload_2.png");
		btnClose.setIcon("smal_close.png");
		btnAddToFavorites.setIcon("favorite.gif");
		btnShowInFrame.setIcon("favorite.gif");
		btnClose.setToolTipText("CLOSE_PANEL");
		btnReload.setToolTipText("RELOAD");
		btnClose.setToolTipText("CLOSE");
		btnAddToFavorites.setToolTipText("ADD_TO_FAVORITES");
		btnNext.setToolTipText("NEXT_WINDOW");
		btnPrevious.setToolTipText("PREVIOUSE_WINDOW");
	}

	/**
	 * 
	 */
	protected void handleReload() {
		// by default empty implementation
	}

	/**
	 * 
	 */
	protected void handleAddToFavorites() {
		// by default empty implementation
	}
}
class JKTitleButton extends JKButton{

	public JKTitleButton(String string) {
		super(string);
		setFocusable(false);
		setOpaque(false);
		setBorder(BorderFactory.createLineBorder(Color.gray));
		setPreferredSize(new Dimension(24,24));
	}
	
}
