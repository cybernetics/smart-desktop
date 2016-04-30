//package com.fs.commons.desktop.dynform.ui.masterdetail;
//
//import java.awt.BorderLayout;
//import java.awt.event.ComponentAdapter;
//import java.awt.event.ComponentEvent;
//
//import javax.swing.Box;
//import javax.swing.JComponent;
//import com.fs.commons.application.ui.UIOPanelCreationException;
//import com.fs.commons.dao.dynamic.meta.TableMeta;
//import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
//import com.fs.commons.dao.exception.DaoException;
//import com.fs.commons.desktop.swing.comp.panels.JKPanel;
//
//public class DynUnifiedMasterDetailPanel extends AbstractMasterDetail {
//
//	private Box pnlTabbedPane = Box.createVerticalBox();
//	private JKPanel pnlButtons;
//
//	// /////////////////////////////////////////////////////////////////
//	/**
//	 * This method with empty constructor for reflection purposes
//	 */
//	public DynUnifiedMasterDetailPanel() {
//		super();
//	}
//
//	// /////////////////////////////////////////////////////////////////
//	public DynUnifiedMasterDetailPanel(TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
//		super(tableMeta);
//	}
//
//	/**
//	 * I made public , to give the caller the ability to add extra components
//	 * for this panel
//	 * 
//	 * @return
//	 */
//	protected JKPanel getButtonsPanel() {
//		if (pnlButtons == null) {
//			pnlButtons = new JKPanel();
//		}
//		return pnlButtons;
//	}
//
//	/**
//	 * 
//	 * @param title
//	 * @param icon
//	 * @param pnl
//	 */
//	@Override
//	protected void addPanelToView(String title, String icon, JKPanel pnl) {
//		pnlTabbedPane.add(pnl);
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	protected int getCurrentPanelIndex() {
//		return 0;
//	}
//
//	@Override
//	protected void initUI() {
//		JKPanel container = new JKPanel();
//		container.setLayout(new BorderLayout());
//		//addPanelsToContainer();
//
//		container.add(pnlTabbedPane, BorderLayout.CENTER);
//		container.add(getButtonsPanel(), BorderLayout.SOUTH);
//
//		add(container);
//
//		addComponentListener(new ComponentAdapter() {
//			@Override
//			public void componentShown(ComponentEvent e) {
//			}
//		});		
//	}
//
//	@Override
//	protected void navigateToPanelAtIndex(int panelIndex) {
//	}
//
//	@Override
//	public void addComponent(JComponent comp) {
//		getButtonsPanel().add(comp);
//	}
//}
