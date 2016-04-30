package com.fs.commons.desktop.dynform.ui.masterdetail;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTabbedPane;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class DynMasterDetailPanel extends AbstractMasterDetail {

	private JKTabbedPane pnlTabbedPane = new JKTabbedPane();

	private JKButton btnNext = new JKButton("Next");

	private JKButton btnPre = new JKButton("Previouse");

	private JKButton btnClose = new JKButton("Close");

	private JKPanel pnlButtons;

	// /////////////////////////////////////////////////////////////////
	/**
	 * This method with empty constructor for reflection purposes
	 */
	public DynMasterDetailPanel() {
		super();
	}

	// /////////////////////////////////////////////////////////////////
	public DynMasterDetailPanel(TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		super(tableMeta);
	}

	public DynMasterDetailPanel(String tableMetaName) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		this(AbstractTableMetaFactory.getTableMeta(tableMetaName));
	}

	/**
	 * I made public , to give the caller the ability to add extra components
	 * for this panel
	 * 
	 * @return
	 */
	protected JKPanel getButtonsPanel() {
		if (pnlButtons == null) {
			pnlButtons = new JKPanel();
			// if (pnlTabbedPane.getTabCount() > 1) {
//			SwingUtility.setHotKeyFoButton(SwingUtility.isLeftOrientation() ? btnNext : btnPre, "ctrl  RIGHT", "NEXT");
			btnNext.setShortcut(SwingUtility.isLeftOrientation()?"ctrl RIGHT":"ctrl LEFT", SwingUtility.isLeftOrientation()?"ctrl ->":"ctr <-");
			btnPre.setShortcut(SwingUtility.isLeftOrientation()?"ctrl LEFT":"ctrl RIGHT", SwingUtility.isLeftOrientation()?"ctrl <-":"ctr ->");
//			SwingUtility.setHotKeyFoButton(SwingUtility.isLeftOrientation() ? btnPre : btnNext, "ctrl  LEFT", "PRE");
			btnNext.setIcon(SwingUtility.isLeftOrientation() ? "next2.gif" : "previos2.gif");
			btnPre.setIcon(SwingUtility.isLeftOrientation() ? "previos2.gif" : "next2.gif");
			btnClose.setIcon("fileclose.png");
			pnlButtons.add(btnPre);
			pnlButtons.add(btnNext);
//			pnlButtons.add(btnClose);
			// }
		}
		return pnlButtons;
	}

	/**
	 * 
	 * @param title
	 * @param icon
	 * @param pnl
	 */
	@Override
	protected void addPanelToView(String title, String icon, JKPanel pnl) {
		pnlTabbedPane.addTab(title, icon, pnl);
	}

	/**
	 * 
	 * @return
	 */
	protected int getCurrentPanelIndex() {
		return pnlTabbedPane.getSelectedIndex();
	}

	@Override
	protected void initUI() {
		setLayout(new BorderLayout());
		JKPanel container = new JKPanel();
		container.setLayout(new BorderLayout());
		// addPanelsToContainer();

		container.add(pnlTabbedPane, BorderLayout.CENTER);
		container.add(getButtonsPanel(), BorderLayout.SOUTH);

		add(container);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
			}
		});

		pnlTabbedPane.addChangeListener(new ChangeListener() {
			// // This method is called whenever the selected tab changes
			public void stateChanged(ChangeEvent evt) {
				JTabbedPane pane = (JTabbedPane) evt.getSource();
				int sel = pane.getSelectedIndex();
				navigateToPanelAtIndex(sel);
			}
		});

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				navigateToPanelAtIndex(getCurrentPanelIndex() + 1);
			};
		});
		btnPre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				navigateToPanelAtIndex(getCurrentPanelIndex() - 1);
			}
		});
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleClosePanel();
			}
		});
	}

	@Override
	protected void navigateToPanelAtIndex(int panelIndex) {
		if (panelIndex < 0 || panelIndex == 0 || panelIndex >= pnlTabbedPane.getTabCount()) {
			panelIndex = 0;
		}
		pnlTabbedPane.setSelectedIndex(panelIndex);
		btnClose.setVisible(getCurrentPanelIndex() > 0);// since the
		// master
		btnPre.setEnabled(getCurrentPanelIndex() > 0);
		btnNext.setEnabled(getCurrentPanelIndex() + 1 != pnlTabbedPane.getTabCount());
	}

	@Override
	public void addComponent(JComponent comp) {
		getButtonsPanel().add(comp);
	}
}
