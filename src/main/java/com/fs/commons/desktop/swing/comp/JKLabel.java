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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JLabel;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;

public class JKLabel extends JLabel implements BindingComponent<String> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	static Font font = new Font("Arial", Font.BOLD, 12);

	static Color BG_COLOR = Colors.JK_LABEL_BG;// new Color(191, 215, 255);

	static Color FG_COLOR = Colors.JK_LABEL_FG;// new Color(191, 215, 255);
	private final FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	private String defaultValue;
	private boolean captilize;
	private boolean transfer;

	/**
	 *
	 */
	public JKLabel() {
		init();
	}

	public JKLabel(final Icon image) {
		this();
		setIcon(image);
	}

	public JKLabel(final Icon image, final int horizontalAlignment) {
		this();
		setIcon(image);
		setHorizontalAlignment(horizontalAlignment);
	}

	/**
	 *
	 * @param lableKey
	 */
	public JKLabel(final String lableKey) {
		this(lableKey, true);
	}

	/**
	 *
	 * @param lableKey
	 * @param setSize
	 */
	public JKLabel(final String lableKey, final boolean setSize) {
		super(Lables.get(lableKey, true));
		if (setSize) {
			setPreferredSize(new Dimension(80, 30));
		}
		init();
	}

	public JKLabel(final String text, final Icon icon, final int horizontalAlignment) {
		this(text);
		setIcon(icon);
		setHorizontalAlignment(horizontalAlignment);
	}

	public JKLabel(final String text, final int horizontalAlignment) {
		this(text);
		setHorizontalAlignment(horizontalAlignment);
	}

	@Override
	public void addActionListener(final ActionListener actionListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addValidator(final Validator validator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addValueChangeListener(final ValueChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		setText("");
	}

	@Override
	public void filterValues(final BindingComponent component) {

	}

	@Override
	public JKDataSource getDataSource() {
		return this.fsWrapper.getDataSource();
	}

	@Override
	public String getDefaultValue() {
		return this.defaultValue;
	}

	@Override
	public String getValue() {
		return getText().trim();
	}

	void init() {
		setToolTipText(getText());
		setBackground(BG_COLOR);
		setForeground(FG_COLOR);
		setOpaque(true);
		setLocale(SwingUtility.getDefaultLocale());
		setFocusable(false);
		setHorizontalAlignment(JLabel.CENTER);
		// setFont(font);
	}

	@Override
	public boolean isAutoTransferFocus() {
		return this.transfer;
	}

	@Override
	public void paint(final Graphics g) {
		if (isOpaque()) {
			// setOpaque(false);
			// GraphicsFactory.makeGradient(this, g, getBackground());
			super.paint(g);
			// setOpaque(true);
		} else {
			super.paint(g);
		}

	}

	public void removeIcon() {
		super.setIcon(null);
	}

	@Override
	public void reset() {
		setText(this.defaultValue);
	}

	@Override
	public void setAutoTransferFocus(final boolean transfer) {
		this.transfer = transfer;
	}

	public void setCaptilize(final boolean captilize) {
		this.captilize = captilize;
	}

	@Override
	public void setDataSource(final JKDataSource manager) {
		this.fsWrapper.setDataSource(manager);
	}

	@Override
	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setIcon(final String iconName) {
		if (GeneralUtility.getIconURL(iconName) != null) {
			setIcon(GeneralUtility.getIcon(iconName));
		}
	}

	@Override
	public void setText(final String text) {
		final String txt = Lables.get(text, this.captilize);
		super.setText(txt);
		setToolTipText(text);
	}

	@Override
	public void setValue(final String value) {
		setText(value.trim());
	}

	@Override
	public void validateValue() throws ValidationException {
		// TODO Auto-generated method stub

	}
}
