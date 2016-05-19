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

import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.ImagePanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.locale.Lables;
import com.jk.exceptions.handler.JKExceptionUtil;

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

	private JKDataSource manager;

	// this flag used to avoid painting the home panel when
	// menu item clicked and another menu panel is shown

	public JKFrame() {
		super();
		initFrame();
	}

	public JKFrame(final DaoComponent parent) {
		setDataSource(parent.getDataSource());
	}

	/**
	 *
	 * @param gc
	 */
	public JKFrame(final GraphicsConfiguration gc) {
		super(gc);
		initFrame();
	}

	public JKFrame(final String title) throws HeadlessException {
		super(Lables.get(title));
		initFrame();
	}

	/**
	 *
	 * @param title
	 * @param gc
	 */
	public JKFrame(final String title, final GraphicsConfiguration gc) {
		super(title, gc);
		initFrame();
	}

	public void applyDataSource() {
		SwingUtility.applyDataSource(this, this.manager);
	}

	@Override
	public void dispose() {
		// if(getSize().getWidth()<=1){
		super.dispose();
		// }else{
		// AnimationUtil.close(this,true);
		// }
	}

	@Override
	public JKDataSource getDataSource() {
		if (this.manager != null) {
			return this.manager;
		}
		if (getParent() instanceof DaoComponent) {
			return ((DaoComponent) getParent()).getDataSource();
		}
		return JKDataSourceFactory.getDefaultDataSource();
	}

	/**
	 * @return the homePanel
	 */
	public JPanel getHomePanel() {
		return this.homePanel;
	}

	/**
	 *
	 * @param item
	 */
	public void handleShowPanel(final JPanel panel) {
		if (panel == null) {
			// do nothing
			return;
		}
		this.showAnotherPanel = true;
		if (this.centerPanel != null) {
			remove(this.centerPanel);
		}

		panel.applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());

		// panel.setMinimumSize(new Dimension(900,600));
		// JScrollPane pane=new JScrollPane(panel);
		add(panel, BorderLayout.CENTER);

		this.centerPanel = panel;
		this.centerPanel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, Color.DARK_GRAY, Color.black));

		validate();
		repaint();
		this.centerPanel.requestFocus();
		this.showAnotherPanel = false;
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
			public void componentRemoved(final ContainerEvent e) {
				if (e.getChild() != getHomePanel() && getHomePanel() != null && !JKFrame.this.showAnotherPanel) {
					showHomePanel();
				}
			}

		});
	}

	public void refreshComponents() {
		validate();
		repaint();
	}

	public void setBackgroundImage(final String image, final int type) {
		try {
			setHomePanel(new ImagePanel(SwingUtility.getImage(image), type));
			showHomePanel();
		} catch (final IOException e) {
			JKExceptionUtil.handle(e);
		}
	}

	@Override
	public void setDataSource(final JKDataSource manager) {
		this.manager = manager;
		applyDataSource();
	}

	/**
	 * @param homePanel
	 */
	public void setHomePanel(final JPanel homePanel) {
		this.homePanel = homePanel;
		handleShowPanel(homePanel);
	}

	public void setRightToLeft() {
		applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	}

	@Override
	public void setTitle(final String title) {
		super.setTitle(Lables.get(title));
	}

	@Override
	public void setVisible(final boolean show) {
		if (show) {
			// AnimationUtil.open(this);
			super.setVisible(true);
		} else {
			// if(getSize().getWidth()<=1){
			super.setVisible(false);
			// }else{
			// AnimationUtil.close(this,false);
			// }
		}
	}

	public void showHomePanel() {
		handleShowPanel(getHomePanel());
	}
}
