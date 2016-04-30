/**
 * 
 */
package com.fs.commons.desktop.swing.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.ImagePanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExceptionUtil;

/**
 * @author u087
 * 
 */
public class JKFrame extends JFrame implements DaoComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JComponent centerPanel;

	JPanel homePanel = new JKPanel();

	private boolean showAnotherPanel;

	private DataSource manager;

	// this flag used to avoid painting the home panel when
	// menu item clicked and another menu panel is shown

	public JKFrame() {
		super();
		initFrame();
	}

	public JKFrame(DaoComponent parent) {
		setDataSource(parent.getDataSource());
	}

	/**
	 * 
	 * @param gc
	 */
	public JKFrame(GraphicsConfiguration gc) {
		super(gc);
		initFrame();
	}

	/**
	 * 
	 * @param title
	 * @param gc
	 */
	public JKFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		initFrame();
	}

	public JKFrame(String title) throws HeadlessException {
		super(Lables.get(title));
		initFrame();
	}

	@Override
	public void setVisible(boolean show) {
		if (show) {
//			AnimationUtil.open(this);
			super.setVisible(true);
		} else {
			// if(getSize().getWidth()<=1){
			super.setVisible(false);
			// }else{
			// AnimationUtil.close(this,false);
			// }
		}
	}

	@Override
	public void dispose() {
		// if(getSize().getWidth()<=1){
		super.dispose();
		// }else{
		// AnimationUtil.close(this,true);
		// }
	}

	/**
	 * 
	 */
	private void initFrame() {
		setSize(1024, 700);
		setBackground(Colors.MAIN_PANEL_BG);
		setLocationRelativeTo(null);
		getInputContext().selectInputMethod(SwingUtility.getDefaultLocale());
		getContentPane().addContainerListener(new ContainerAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ContainerAdapter#componentRemoved(java.awt.event
			 * .ContainerEvent)
			 */
			@Override
			public void componentRemoved(ContainerEvent e) {
				if (e.getChild() != getHomePanel() && getHomePanel() != null && !showAnotherPanel) {
					showHomePanel();
				}
			}

		});
	}

	public void showHomePanel() {
		handleShowPanel(getHomePanel());
	}

	/**
	 * 
	 * @param item
	 */
	public void handleShowPanel(JPanel panel) {
		if (panel == null) {
			// do nothing
			return;
		}
		showAnotherPanel = true;
		if (centerPanel != null) {
			remove(centerPanel);
		}

		panel.applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());

//		panel.setMinimumSize(new Dimension(900,600));
//		JScrollPane pane=new JScrollPane(panel);		
		add(panel, BorderLayout.CENTER);

		centerPanel = panel;
		centerPanel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, Color.DARK_GRAY, Color.black));
		
		validate();
		repaint();
		centerPanel.requestFocus();
		showAnotherPanel = false;
	}

	/**
	 * @return the homePanel
	 */
	public JPanel getHomePanel() {
		return this.homePanel;
	}

	/**
	 * @param homePanel
	 */
	public void setHomePanel(JPanel homePanel) {
		this.homePanel = homePanel;
		handleShowPanel(homePanel);
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(Lables.get(title));
	}

	public void refreshComponents() {
		validate();
		repaint();
	}

	public void setRightToLeft() {
		applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	}

	public void setBackgroundImage(String image, int type) {
		try {
			setHomePanel(new ImagePanel(SwingUtility.getImage(image), type));
			showHomePanel();
		} catch (IOException e) {
			ExceptionUtil.handleException(e);
		}
	}

	@Override
	public void setDataSource(DataSource manager) {
		this.manager = manager;
		applyDataSource();
	}

	@Override
	public DataSource getDataSource() {
		if (manager != null) {
			return manager;
		}
		if (getParent() instanceof DaoComponent) {
			return ((DaoComponent) getParent()).getDataSource();
		}
		return DataSourceFactory.getDefaultDataSource();
	}

	public void applyDataSource() {
		SwingUtility.applyDataSource(this, manager);
	}
}
