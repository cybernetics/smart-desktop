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
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;
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
	public static final java.awt.Font DEFAULT_FONT = new java.awt.Font("TAHOMA", Font.BOLD, 11);

	private static final long serialVersionUID = 1L;

	public static enum FieldType {
		TEXT, NUMBER, AMOUNT, DATE
	}

	// static Border focusBorder =
	// BorderFactory.createLineBorder(SystemColor.infoText);
	// static Border lostFocusBorder =
	// BorderFactory.createLineBorder(SystemColor.activeCaptionBorder);
	static Dimension dim = new Dimension(200, 30);
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

	private boolean transfer=true;

	/**
	 * 
	 * @param width
	 *            int
	 */
	public JKTextField(int width) {
		this(width, true);
	}

	/**
	 * JKTextField
	 * 
	 * @param plainDocument
	 *            PlainDocument
	 * @param i
	 *            int
	 */
	public JKTextField(FieldType type, int width) {
		super(getDocument(type), "", width);
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
	public JKTextField(Document plainDocument, int width) {
		super(plainDocument, "", width);
		init();
	}

	public JKTextField() {
		init();
	}

	public JKTextField(int width, boolean editable) {
		this(new PlainDocument(), width, editable);
	}

	public JKTextField(Document doc, int width, boolean editable) {
		this(new PlainDocument(), width);
		setEditable(editable);
	}

	public JKTextField(Document document) {
		setDocument(document);
		init();
	}

	/**
	 * 
	 * @param maxLength
	 * @param width
	 * @param editable
	 */
	public JKTextField(int maxLength, int width, boolean editable) {
		this(new FSTextDocument(maxLength), width);
		setEditable(editable);
	}

	public JKTextField(int i, int j) {
		this(i, j, true);
	}

	public JKTextField(boolean editable) {
		init();
		setEditable(editable);
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
			public void focusGained(FocusEvent e) {
				if (autoSelectText) {
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
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					handleCopyText();
				}
			}
		});
	}

	/**
	 * 
	 */
	protected void checkValidValue() {
		if (getDocument() instanceof FloatDocument) {
			try {
				Float.parseFloat(getText().trim());
			} catch (NumberFormatException e) {
				setText("");
			}
		}

	}

	/**
	 * 
	 */
	protected void handleCopyText() {
		GeneralUtility.copyToClipboard(getText());
	}

	/**
	 * 
	 * @param type
	 *            FieldType
	 * @return Document
	 */
	static Document getDocument(FieldType type) {
		if (type == FieldType.NUMBER) {
			return new NumberDocument();
		}
		return new PlainDocument();
	}

	/**
	 * 
	 */
	public Object getValue() {
		return getText().trim().equals("") ? null : getText();
	}

	/**
	 * 
	 */
	public void setValue(Object value) {
		// Object old=getValue();
		if (value != null) {
			setText(ConversionUtil.toString(value));
		} else {
			setText("");
		}
		// fsWrapper.fireValueChangeListener(old, value);
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
	 * @throws ValidationException
	 * 
	 */
	public void checkEmpty() throws ValidationException {
		SwingValidator.checkEmpty(this);
	}

	/**
	 * 
	 * @return
	 */
	public String getDefaultValue() {
		return defaultValue;// ==null?null: calculateDefaultValue();
	}

	/**
	 * 
	 */
	public void reset() {
		setText(getDefaultValue() != null ? getDefaultValue() : "");
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		if (defaultValue != null && defaultValue.toString().toUpperCase().equals("NULL")) {
			this.defaultValue = null;
		} else {
			this.defaultValue = defaultValue.toString();
		}
	}

	@Override
	public void clear() {
		setText("");
	}

	public double getTextAsDouble() {
		return ConversionUtil.toDouble(getValue());
		// if (getText().equals("")) {
		// return 0;
		// }
		// return Double.parseDouble(getText());
	}

	@Override
	public void validateValue() throws ValidationException {
		try {
			fsWrapper.validateValue();
		} catch (ValidationException e) {
			selectAll();
			throw e;
		}
	}

	@Override
	public void addValidator(Validator validator) {
		fsWrapper.addValidator(validator);
	}

	public void setRequired(boolean required) {
		if (required) {
			fsWrapper.addValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		} else {
			fsWrapper.removeValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		}
		this.required = required;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
		addValidator(FSValidators.minLength(minLength));
	}

	public boolean isNumbersOnly() {
		return numbersOnly;
	}

	public boolean isRequired() {
		return required;
	}

	// /////////////////////////////////////////////////////////////////
	public int getMaxLength() {
		return maxLength;
	}

	// /////////////////////////////////////////////////////////////////
	public void setNumbersOnly(boolean numbersOnly) {
		this.numbersOnly = numbersOnly;
		setDocument(new NumberDocument(maxLength == 0 ? NumberDocument.DEFAULT_MAX_LENGTH : maxLength));
	}

	// /////////////////////////////////////////////////////////////////
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
		if (getDocument() instanceof FSTextDocument) {
			FSTextDocument doc = (FSTextDocument) getDocument();
			doc.setMaxLength(maxLength);
		} else {
			addValidator(FSValidators.maxLength(maxLength));
		}
	}

	// /////////////////////////////////////////////////////////////////
	public void setValueFrom(long valueFrom) {
		this.valueFrom = valueFrom;
		addValidator(FSValidators.numberRange(valueFrom, valueTo));
	}

	// /////////////////////////////////////////////////////////////////
	public long getValueTo() {
		return valueTo;
	}

	// /////////////////////////////////////////////////////////////////
	public void setValueTo(long valueTo) {
		this.valueTo = valueTo;
		addValidator(FSValidators.numberRange(valueFrom, valueTo));
	}

	// /////////////////////////////////////////////////////////////////
	public long getValueFrom() {
		return valueFrom;
	}

	// /////////////////////////////////////////////////////////////////
	public void selectAll() {
		setSelectionStart(0);
		setSelectionEnd(getText().length());
	}

	@Override
	public void setName(String name) {
		super.setName(Lables.get(name));
	}

	@Override
	public void setColumns(int columns) {
		// Ignore this call , and depend on setPreferedSize
		System.err.println("Invalid call to setColunms at FSTextField");
	}

	public boolean isAutoTrim() {
		return autoTrim;
	}

	public void setAutoTrim(boolean autoTrim) {
		this.autoTrim = autoTrim;
	}

	@Override
	public String getText() {
		if (isAutoTrim()) {
			return super.getText().trim();
		} else {
			return super.getText();
		}
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	@Override
	public void filterValues(BindingComponent comp1) throws DaoException {
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
		fsWrapper.addValueChangeListsner(listener);
	}

	@Override
	public void setText(String text) {
		// String oldValue2 = getText();
		super.setText(text);
		if (fsWrapper != null) {
			// Since this could be called by the super class during construction
			fsWrapper.fireValueChangeListener(oldValue, text);
		}

	}

	public double getValueAsDouble() {
		return ConversionUtil.toDouble(getValue());
	}

	public void setAutoSelectText(boolean autoSelectText) {
		this.autoSelectText = autoSelectText;
	}

	public void setWidth(int width) {
		setPreferredSize(new Dimension(width, 25));
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
