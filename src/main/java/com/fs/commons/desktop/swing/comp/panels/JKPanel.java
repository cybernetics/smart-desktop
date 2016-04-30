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
package com.fs.commons.desktop.swing.comp.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.application.ui.UIPanel;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.graphics.GraphicsFactory;
import com.fs.commons.desktop.graphics.GraphicsFactory.GradientType;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.FSAbstractComponent;
import com.fs.commons.desktop.swing.comp.JKErrorLabel;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.desktop.validation.exception.UIValidationException;
import com.fs.commons.locale.Lables;

/**
 */
public class JKPanel<T> extends JPanel implements UIPanel, BindingComponent<T> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// private DataSource manager;
	private GradientType gradientType;
	private Color gredientColor;
	private Image backGroundImage;
	protected FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	private boolean transfer;

	public JKPanel() {
		// super(new FlowLayout(FlowLayout.CENTER, 0, 0));
		initJKPanel();
	}

	public JKPanel(final GradientType gradientType) {
		this.gradientType = gradientType;
	}

	public JKPanel(final JComponent component) {
		this();
		add(component);
	}

	public JKPanel(final JComponent container, final String title) {
		this(container);
		setTitle(title);
	}

	public JKPanel(final LayoutManager layout) {
		super(layout);
		initJKPanel();
	}

	@Override
	public void addActionListener(final ActionListener actionListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addValidator(final Validator validator) {
		this.fsWrapper.addValidator(validator);
	}

	@Override
	public void addValueChangeListener(final ValueChangeListener listener) {
		this.fsWrapper.addValueChangeListsner(listener);
	}

	@Override
	public void clear() {
		final String methodName = "clear ";
		handleUnAllowedMethodCall(methodName);
	}

	@Override
	public void filterValues(final BindingComponent component) {

	}

	public Image getBackGroundImage() {
		return this.backGroundImage;
	}

	@Override
	public DataSource getDataSource() {
		return this.fsWrapper.getDataSource();
	};

	@Override
	public T getDefaultValue() {
		final String methodName = "getDefaultValue ";
		return handleUnAllowedMethodCall(methodName);
	}

	/**
	 * @return the gradientType
	 */
	public GradientType getGradientType() {
		return this.gradientType;
	}

	/**
	 *
	 * @return
	 */
	private Color getGredientColor(final boolean recalculate) {
		if (this.gredientColor == null || recalculate) {
			this.gredientColor = GraphicsFactory.createGradientColor(getBackground());
		}
		return this.gredientColor;
	}

	@Override
	public T getValue() {
		final String methodName = "getValue ";
		return handleUnAllowedMethodCall(methodName);
	}

	/**
	 * @param methodName
	 */
	private T handleUnAllowedMethodCall(final String methodName) {
		// System.err.println(this.getClass().getName());
		// System.err.println("calling " + methodName +
		// "on JKPanel is not allowed");
		return null;
	}

	private void initJKPanel() {
		setOpaque(false);
		// setDoubleBuffered(false);
		setLocale(SwingUtility.getDefaultLocale());
		setFocusable(false);
		setBackground(SwingUtility.getDefaultBackgroundColor());
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON3) {
					setVisible(false);
				}
			}
		});
	}

	@Override
	public boolean isAutoTransferFocus() {
		return this.transfer;
	}

	// ///////////////////////////////////////////////////////////
	@Override
	public void paint(final Graphics g) {
		if (this.gradientType != null) {
			final Color newColor = getGredientColor(false);
			GraphicsFactory.makeGradient(this, g, getBackground(), newColor, this.gradientType);
		}
		super.paint(g);
	}

	@Override
	protected void paintComponent(final Graphics g) {
		if (this.backGroundImage == null) {
			super.paintComponent(g);
		} else {
			final Graphics2D g2d = (Graphics2D) g;

			// scale the image to fit the size of the Panel
			final double mw = this.backGroundImage.getWidth(null);
			final double mh = this.backGroundImage.getHeight(null);

			final double sw = getWidth() / mw;
			final double sh = getHeight() / mh;

			g2d.scale(sw, sh);
			g2d.drawImage(this.backGroundImage, 0, 0, this);
		}
	}

	public void refreshComponents() {
		validate();
		repaint();
	}

	@Override
	public void requestFocus() {
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				transferFocus();
			}
		};
		SwingUtilities.invokeLater(runnable);
	}

	@Override
	public void reset() {
		final String methodName = "reset ";
		handleUnAllowedMethodCall(methodName);
	}

	/**
	 *
	 * @throws DaoException
	 */
	public void resetComponents() throws DaoException {
	}

	@Override
	public void setAutoTransferFocus(final boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public void setBackground(final Color bg) {
		super.setBackground(bg);
		getGredientColor(true);
	}

	public void setBackGroundImage(final Image backGroundImage) {
		this.backGroundImage = backGroundImage;
	}

	public void setBackGroundImage(final String imageName) {
		try {
			setBackGroundImage(SwingUtility.getImage(imageName));
			// add(new ImagePanel(SwingUtility.geti))
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setBorder(final Border border) {
		if (border instanceof TitledBorder) {
			final TitledBorder b = (TitledBorder) border;
			b.setTitle(Lables.get(b.getTitle(), true));
		}
		super.setBorder(border);
	}

	@Override
	public void setDataSource(final DataSource manager) {
		this.fsWrapper.setDataSource(manager);
	}

	@Override
	public void setDefaultValue(final T t) {
		final String methodName = "setDefaultValue ";
		handleUnAllowedMethodCall(methodName);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		SwingUtility.enableContainer(this, enabled);
	}

	/**
	 * @param gradientType
	 *            the gradientType to set
	 */
	public void setGradientType(final GradientType gradientType) {
		this.gradientType = gradientType;
	}

	public void setGredientColor(final Color gredientColor) {
		this.gredientColor = gredientColor;
	}

	@Override
	public void setLayout(final LayoutManager mgr) {
		if (mgr instanceof FlowLayout) {
			final FlowLayout flow = (FlowLayout) mgr;
			// flow.setHgap(0);
			// flow.setVgap(0);
		}
		super.setLayout(mgr);
	}

	// ///////////////////////////////////////////////////////////
	public void setPreferredSize(final int width, final int height) {
		super.setPreferredSize(new Dimension(width, height));
	}

	public void setTitle(final String title) {
		setBorder(SwingUtility.createTitledBorder(title));
	}

	@Override
	public void setValue(final T value) {
		final String methodName = "setValue ";
		handleUnAllowedMethodCall(methodName);
	}

	@Override
	public void validateValue() throws ValidationException {
		// validateValues();
		this.fsWrapper.validateValue();
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	public void validateValues() throws ValidationException {
		validateValues(null);
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	public void validateValues(final JKErrorLabel fslblErrorl) throws ValidationException {
		final StringBuffer buf = new StringBuffer();
		final Vector<BindingComponent> components = SwingUtility.findBindingComponents(this);
		for (final BindingComponent bindingComponent : components) {
			try {
				bindingComponent.validateValue();
			} catch (final UIValidationException e) {
				if (e.getProblems() != null && fslblErrorl != null) {
					fslblErrorl.setText(e.getProblems().getLeadProblem().getMessage());
				}
				throw e;
			}
		}
		if (fslblErrorl != null) {
			fslblErrorl.setText("");
		}
	}
}
