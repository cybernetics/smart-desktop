/*
 * 1.1    13-5-2008        Jalal     - add support for FieldPanelWithFilter
 * 1.1	  29-06-2008	   Jamil     - add the following method
 * 
 */
package com.fs.commons.desktop.swing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.comp.DaoComboWithManagePanel;
import com.fs.commons.desktop.swing.comp.JKDate;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.JKTime;
import com.fs.commons.desktop.swing.comp.panels.JKBlobPanel;
import com.fs.commons.locale.Lables;

public class SwingValidator {

	/**
	 * 
	 * @param comp
	 *            JComponent
	 * @throws ValidationException
	 */
	public static void checkEmpty(BindingComponent comp) throws ValidationException {
		if (comp instanceof JTextComponent) {
			JTextComponent txt = (JTextComponent) comp;
			txt.setText(txt.getText().trim());
			if (txt.getText().equals("")) {
				comp.requestFocus();
				throw new ValidationException("ERROR_EMPTY_FIELD", comp);
			}
		} else if (comp instanceof JTextArea) {
			JTextArea txt = (JTextArea) comp;
			if (txt.getText().trim().equals("")) {
				comp.requestFocus();
				throw new ValidationException("ERROR_SELECT_ENTRY_FROM_LIST", comp);
			}			
		} else if (comp instanceof JComboBox) {
			JComboBox<?> combo = (JComboBox<?>) comp;
			if (combo.getSelectedIndex() < 0) {
				comp.requestFocus();
				throw new ValidationException("ERROR_SELECT_ENTRY_FROM_LIST", comp);
			}
		} else if (comp instanceof DaoComboWithManagePanel) {
			DaoComboWithManagePanel combo = (DaoComboWithManagePanel) comp;
			if (combo.getSelectedIndex() < 0) {
				comp.requestFocus();
				throw new ValidationException("ERROR_SELECT_ENTRY_FROM_LIST", comp);
			}
		} else if (comp instanceof JKBlobPanel) {
			JKBlobPanel pnl = (JKBlobPanel) comp;
			if (pnl.getValue() == null) {
				pnl.requestFocus();
				throw new ValidationException("EMPTY_IMAGE_IS_NOT_ALLOWED", comp);
			}

		} else if (comp instanceof JList) {
			JList<?> list = (JList<?>) comp;
			if (list.getSelectedIndex() < 0) {
				comp.requestFocus();
				throw new ValidationException("ERROR_SELECT_ENTRY_FROM_LIST", comp);
			}
		}else if(comp instanceof JKTime){
			((JKTime)comp).checkValue();
		}else if(comp instanceof JKDate){
			((JKDate)comp).checkValue();
			
		//@1.1 : by Jalal
		}else if(comp instanceof BindingComponent){
			Object value=((BindingComponent<?>)comp).getValue();
			if(value==null || value.toString().equals("")){
				throw new ValidationException("ERROR_EMPTY_FIELD", comp);
			}
		}
	}

	/**
	 * 
	 * @param txt
	 *            JComponent
	 * @throws ValidationException
	 */
	public static void checkValidInteger(JKTextField txt) throws ValidationException {
		txt.setText(txt.getText().trim());
		try {
			Integer.parseInt(txt.getText());
		} catch (NumberFormatException ex) {
			txt.requestFocus();
			throw new ValidationException("ERROR_MESSAGE_NUMERIC_EXPECTED", txt);
		}
	}

	/**
	 * 
	 * @param txt
	 *            JTextComponent
	 */
	public static void checkValidRange(JKTextField txt, float rangeFrom, float rangeTo) throws ValidationException {
		float value = Float.parseFloat(txt.getText());
		if (!(value >= rangeFrom && value <= rangeTo)) {
			txt.requestFocus();
			throw new ValidationException(Lables.get("VALUE_OUT_OF_RANGE")+"\n"+(int)rangeFrom+"-"+(int)rangeTo, txt);
		}
	}
	
	/**
	 * @1.1
	 * @param fileName
	 * @throws ValidationException
	 */
	public static void checkValidFile(String fileName) throws ValidationException {
		File file = new File(fileName);
		
		if(file.exists()){
			throw new ValidationException("FILE_ALREADY_EXISTS");
		}
		
		try {
			new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			throw new ValidationException("FILE_NAME_IS_NOT_CORRECT");
		}
	}
}
