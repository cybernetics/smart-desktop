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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.desktop.swin.ConversionUtil;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.documents.FSTextDocument;
import com.fs.commons.desktop.swing.comp.documents.FloatDocument;
import com.fs.commons.desktop.swing.comp.documents.NumberDocument;
import com.fs.commons.desktop.swing.comp.listeners.TransferFocusOnEnterKeyListener;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.desktop.validation.builtin.FSValidators;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;
import com.lowagie.text.Font;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class JKTextField extends JTextField implements BindingComponent {
	public static enum FieldType {
		TEXT, NUMBER, AMOUNT, DATE
	}

	public static final java.awt.Font DEFAULT_FONT = new java.awt.Font("TAHOMA", Font.BOLD, 11);

	private static final long serialVersionUID = 1L;

	// static Border focusBorder =
	// BorderFactory.createLineBorder(SystemColor.infoText);
	// static Border lostFocusBorder =
	// BorderFactory.createLineBorder(SystemColor.activeCaptionBorder);
	static Dimension dim = new Dimension(200, 30);

	/**
	 *
	 * @param type
	 *            FieldType
	 * @return Document
	 */
	static Document getDocument(final FieldType type) {
		if (type == FieldType.NUMBER) {
			return new NumberDocument();
		}
		return new PlainDocument();
	}

	private String defaultValue;
	private Class type;
	protected FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private boolean numbersOnly;
	private boolean autoTrim = true;
	private boolean required;
	private int minLength;
	private int maxLength;
	private long valueFrom;
	private long valueTo;

	private String oldValue;

	private boolean autoSelectText = true;

	private boolean transfer = true;

	public JKTextField() {
		init();
	}

	public JKTextField(final boolean editable) {
		init();
		setEditable(editable);
	}

	public JKTextField(final Document document) {
		setDocument(document);
		init();
	}

	/**
	 * JKTextField
	 *
	 * @param plainDocument
	 *            PlainDocument
	 * @param i
	 *            int
	 */
	public JKTextField(final Document plainDocument, final int width) {
		super(plainDocument, "", width);
		init();
	}

	public JKTextField(final Document doc, final int width, final boolean editable) {
		this(new PlainDocument(), width);
		setEditable(editable);
	}

	/**
	 * JKTextField
	 *
	 * @param plainDocument
	 *            PlainDocument
	 * @param i
	 *            int
	 */
	public JKTextField(final FieldType type, final int width) {
		super(getDocument(type), "", width);
		init();
	}

	/**
	 *
	 * @param width
	 *            int
	 */
	public JKTextField(final int width) {
		this(width, true);
	}

	public JKTextField(final int width, final boolean editable) {
		this(new PlainDocument(), width, editable);
	}

	public JKTextField(final int i, final int j) {
		this(i, j, true);
	}

	/**
	 *
	 * @param maxLength
	 * @param width
	 * @param editable
	 */
	public JKTextField(final int maxLength, final int width, final boolean editable) {
		this(new FSTextDocument(maxLength), width);
		setEditable(editable);
	}

	@Override
	public void addValidator(final Validator validator) {
		this.fsWrapper.addValidator(validator);
	}

	@Override
	public void addValueChangeListener(final ValueChangeListener listener) {
		this.fsWrapper.addValueChangeListsner(listener);
	}

	/**
	 * @throws ValidationException
	 *
	 */
	public void checkEmpty() throws ValidationException {
		SwingValidator.checkEmpty(this);
	}

	/**
	 *
	 */
	protected void checkValidValue() {
		if (getDocument() instanceof FloatDocument) {
			try {
				Float.parseFloat(getText().trim());
			} catch (final NumberFormatException e) {
				setText("");
			}
		}

	}

	@Override
	public void clear() {
		setText("");
	}

	@Override
	public void filterValues(final BindingComponent comp1) throws JKDataAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public JKDataSource getDataSource() {
		return this.fsWrapper.getDataSource();
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String getDefaultValue() {
		return this.defaultValue;// ==null?null: calculateDefaultValue();
	}

	// /////////////////////////////////////////////////////////////////
	public int getMaxLength() {
		return this.maxLength;
	}

	public int getMinLength() {
		return this.minLength;
	}

	@Override
	public String getText() {
		if (isAutoTrim()) {
			return super.getText().trim();
		} else {
			return super.getText();
		}
	}

	public double getTextAsDouble() {
		return ConversionUtil.toDouble(getValue());
		// if (getText().equals("")) {
		// return 0;
		// }
		// return Double.parseDouble(getText());
	}

	/**
	 *
	 * @return
	 */
	public float getTextAsFloat() {
		if (getText().equals("")) {
			return 0;
		}
		return Float.parseFloat(getText());
	}

	/**
	 *
	 * @return
	 */
	public int getTextAsInteger() {
		return ConversionUtil.toInteger(getValue());
		// if (getText().equals("")) {
		// return 0;
		// }
		// return Integer.parseInt(getText());
	}

	/**
	 *
	 * @return
	 */
	public long getTextAsLong() {
		if (getText().equals("")) {
			return 0;
		}
		return Long.parseLong(getText());
	}

	public Class getType() {
		return this.type;
	}

	/**
	 *
	 */
	@Override
	public Object getValue() {
		return getText().trim().equals("") ? null : getText();
	}

	public double getValueAsDouble() {
		return ConversionUtil.toDouble(getValue());
	}

	// /////////////////////////////////////////////////////////////////
	public long getValueFrom() {
		return this.valueFrom;
	}

	// /////////////////////////////////////////////////////////////////
	public long getValueTo() {
		return this.valueTo;
	}

	/**
	 *
	 */
	protected void handleCopyText() {
		GeneralUtility.copyToClipboard(getText());
	}

	/**
	 * init
	 */
	private void init() {
		setFont(DEFAULT_FONT);
		setDisabledTextColor(Color.black);
		setLocale(SwingUtility.getDefaultLocale());
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		// if(getDocument() instanceof FloatDocument || getDocument() instanceof
		// NumberDocument){
		// setHorizontalAlignment(CENTER);
		// }else{
		setHorizontalAlignment(LEADING);
		// }
		setPreferredSize(dim);
		// SwingUtility.setFont(this);
		// setBorder(lostFocusBorder);
		addKeyListener(new TransferFocusOnEnterKeyListener(this));
		// addKeyListener(new KeyAdapter() {
		// @Override
		// public void keyPressed(KeyEvent e) {
		// if (e.getKeyChar() == KeyEvent.VK_ENTER) {
		// SwingUtility.pressKey(KeyEvent.VK_TAB);
		// e.consume();
		// }
		// }
		// });

		addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(final FocusEvent e) {
				if (JKTextField.this.autoSelectText) {
					selectAll();
				}
			}

			// @Override
			// public void focusLost(FocusEvent e) {
			// // checkValidValue();
			// fsWrapper.fireValueChangeListener(oldValue, getText());
			// }
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					handleCopyText();
				}
			}
		});
	}

	@Override
	public boolean isAutoTransferFocus() {
		return this.transfer;
	}

	public boolean isAutoTrim() {
		return this.autoTrim;
	}

	public boolean isNumbersOnly() {
		return this.numbersOnly;
	}

	public boolean isRequired() {
		return this.required;
	}

	/**
	 *
	 */
	@Override
	public void reset() {
		setText(getDefaultValue() != null ? getDefaultValue() : "");
	}

	// /////////////////////////////////////////////////////////////////
	@Override
	public void selectAll() {
		setSelectionStart(0);
		setSelectionEnd(getText().length());
	}

	public void setAutoSelectText(final boolean autoSelectText) {
		this.autoSelectText = autoSelectText;
	}

	@Override
	public void setAutoTransferFocus(final boolean transfer) {
		this.transfer = transfer;
	}

	public void setAutoTrim(final boolean autoTrim) {
		this.autoTrim = autoTrim;
	}

	@Override
	public void setColumns(final int columns) {
		// Ignore this call , and depend on setPreferedSize
		System.err.println("Invalid call to setColunms at FSTextField");
	}

	@Override
	public void setDataSource(final JKDataSource manager) {
		this.fsWrapper.setDataSource(manager);
	}

	@Override
	public void setDefaultValue(final Object defaultValue) {
		if (defaultValue != null && defaultValue.toString().toUpperCase().equals("NULL")) {
			this.defaultValue = null;
		} else {
			this.defaultValue = defaultValue.toString();
		}
	}

	// /////////////////////////////////////////////////////////////////
	public void setMaxLength(final int maxLength) {
		this.maxLength = maxLength;
		if (getDocument() instanceof FSTextDocument) {
			final FSTextDocument doc = (FSTextDocument) getDocument();
			doc.setMaxLength(maxLength);
		} else {
			addValidator(FSValidators.maxLength(maxLength));
		}
	}

	public void setMinLength(final int minLength) {
		this.minLength = minLength;
		addValidator(FSValidators.minLength(minLength));
	}

	@Override
	public void setName(final String name) {
		super.setName(Lables.get(name));
	}

	// /////////////////////////////////////////////////////////////////
	public void setNumbersOnly(final boolean numbersOnly) {
		this.numbersOnly = numbersOnly;
		setDocument(new NumberDocument(this.maxLength == 0 ? NumberDocument.DEFAULT_MAX_LENGTH : this.maxLength));
	}

	public void setRequired(final boolean required) {
		if (required) {
			this.fsWrapper.addValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		} else {
			this.fsWrapper.removeValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		}
		this.required = required;
	}

	@Override
	public void setText(final String text) {
		// String oldValue2 = getText();
		super.setText(text);
		if (this.fsWrapper != null) {
			// Since this could be called by the super class during construction
			this.fsWrapper.fireValueChangeListener(this.oldValue, text);
		}

	}

	public void setType(final Class type) {
		this.type = type;
	}

	/**
	 *
	 */
	@Override
	public void setValue(final Object value) {
		// Object old=getValue();
		if (value != null) {
			setText(ConversionUtil.toString(value));
		} else {
			setText("");
		}
		// fsWrapper.fireValueChangeListener(old, value);
	}

	// /////////////////////////////////////////////////////////////////
	public void setValueFrom(final long valueFrom) {
		this.valueFrom = valueFrom;
		addValidator(FSValidators.numberRange(valueFrom, this.valueTo));
	}

	// /////////////////////////////////////////////////////////////////
	public void setValueTo(final long valueTo) {
		this.valueTo = valueTo;
		addValidator(FSValidators.numberRange(this.valueFrom, valueTo));
	}

	public void setWidth(final int width) {
		setPreferredSize(new Dimension(width, 25));
	}

	@Override
	public void validateValue() throws ValidationException {
		try {
			this.fsWrapper.validateValue();
		} catch (final ValidationException e) {
			selectAll();
			throw e;
		}
	}
}
