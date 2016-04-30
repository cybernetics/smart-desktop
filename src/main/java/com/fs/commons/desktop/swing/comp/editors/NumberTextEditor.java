package com.fs.commons.desktop.swing.comp.editors;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

import com.fs.commons.desktop.swing.comp.documents.NumberDocument;

public class NumberTextEditor extends  DefaultCellEditor{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param textField
	 * @param maxLength
	 */
	public NumberTextEditor(JTextField textField,int maxLength) {
		super(textField);
		textField.setDocument(new NumberDocument(maxLength));
	}

}
