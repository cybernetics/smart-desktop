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
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.desktop.graphics.GraphicsFactory;
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
	private FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	static Font font = new Font("Arial", Font.BOLD, 12);

	static Color BG_COLOR = Colors.JK_LABEL_BG;// new Color(191, 215, 255);
	static Color FG_COLOR = Colors.JK_LABEL_FG;// new Color(191, 215, 255);

	private String defaultValue;
	private boolean captilize;
	private boolean transfer;

	/**
	 * 
	 * @param lableKey
	 */
	public JKLabel(String lableKey) {
		this(lableKey, true);
	}

	/**
	 * 
	 * @param lableKey
	 * @param setSize
	 */
	public JKLabel(String lableKey, boolean setSize) {
		super(Lables.get(lableKey, true));
		if (setSize) {
			setPreferredSize(new Dimension(80, 30));
		}
		init();
	}

	/**
	 * 
	 */
	public JKLabel() {
		init();
	}

	public JKLabel(Icon image, int horizontalAlignment) {
		this();
		setIcon(image);
		setHorizontalAlignment(horizontalAlignment);
	}

	public JKLabel(Icon image) {
		this();
		setIcon(image);
	}

	public JKLabel(String text, Icon icon, int horizontalAlignment) {
		this(text);
		setIcon(icon);
		setHorizontalAlignment(horizontalAlignment);
	}

	public JKLabel(String text, int horizontalAlignment) {
		this(text);
		setHorizontalAlignment(horizontalAlignment);
	}

	void init() {
		setToolTipText(getText());
		setBackground(BG_COLOR);
		setForeground(FG_COLOR);
		setOpaque(true);
		setLocale(SwingUtility.getDefaultLocale());
		setFocusable(false);
		setHorizontalAlignment(JLabel.CENTER);
//		setFont(font);
	}

	public String getValue() {
		return getText().trim();
	}

	public void setValue(String value) {
		setText(value.trim());
	}

	@Override
	public void setText(String text) {
		String txt = Lables.get(text,captilize);
		super.setText(txt);
		setToolTipText(text);
	}

	public void setIcon(String iconName) {
		if (GeneralUtility.getIconURL(iconName) != null) {
			setIcon(GeneralUtility.getIcon(iconName));
		}
	}

	public void removeIcon() {
		super.setIcon(null);
	}

	@Override
	public void paint(Graphics g) {
		if (isOpaque()) {
//			setOpaque(false);
//			GraphicsFactory.makeGradient(this, g, getBackground());
			super.paint(g);
//			setOpaque(true);
		} else {
			super.paint(g);
		}

	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public void reset() {
		setText(defaultValue);
	}

	@Override
	public void clear() {
		setText("");
	}

	@Override
	public void filterValues(BindingComponent component) {

	}

	@Override
	public void addValidator(Validator validator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateValue() throws ValidationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDataSource(DataSource manager) {
		fsWrapper.setDataSource(manager);
	}

	@Override
	public DataSource getDataSource() {
		return fsWrapper.getDataSource();
	}

	@Override
	public void addValueChangeListener(ValueChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addActionListener(ActionListener actionListener) {
		// TODO Auto-generated method stub
		
	}

	public void setCaptilize(boolean captilize) {
		this.captilize = captilize;		
	}

	@Override
	public void setAutoTransferFocus(boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public boolean isAutoTransferFocus() {
		return transfer;
	}
}
