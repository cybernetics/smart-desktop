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
	public static void checkEmpty(final BindingComponent comp) throws ValidationException {
		if (comp instanceof JTextComponent) {
			final JTextComponent txt = (JTextComponent) comp;
			txt.setText(txt.getText().trim());
			if (txt.getText().equals("")) {
				comp.requestFocus();
				throw new ValidationException("ERROR_EMPTY_FIELD", comp);
			}
		} else if (comp instanceof JTextArea) {
			final JTextArea txt = (JTextArea) comp;
			if (txt.getText().trim().equals("")) {
				comp.requestFocus();
				throw new ValidationException("ERROR_SELECT_ENTRY_FROM_LIST", comp);
			}
		} else if (comp instanceof JComboBox) {
			final JComboBox<?> combo = (JComboBox<?>) comp;
			if (combo.getSelectedIndex() < 0) {
				comp.requestFocus();
				throw new ValidationException("ERROR_SELECT_ENTRY_FROM_LIST", comp);
			}
		} else if (comp instanceof DaoComboWithManagePanel) {
			final DaoComboWithManagePanel combo = (DaoComboWithManagePanel) comp;
			if (combo.getSelectedIndex() < 0) {
				comp.requestFocus();
				throw new ValidationException("ERROR_SELECT_ENTRY_FROM_LIST", comp);
			}
		} else if (comp instanceof JKBlobPanel) {
			final JKBlobPanel pnl = (JKBlobPanel) comp;
			if (pnl.getValue() == null) {
				pnl.requestFocus();
				throw new ValidationException("EMPTY_IMAGE_IS_NOT_ALLOWED", comp);
			}

		} else if (comp instanceof JList) {
			final JList<?> list = (JList<?>) comp;
			if (list.getSelectedIndex() < 0) {
				comp.requestFocus();
				throw new ValidationException("ERROR_SELECT_ENTRY_FROM_LIST", comp);
			}
		} else if (comp instanceof JKTime) {
			((JKTime) comp).checkValue();
		} else if (comp instanceof JKDate) {
			((JKDate) comp).checkValue();

			// @1.1 : by Jalal
		} else if (comp instanceof BindingComponent) {
			final Object value = ((BindingComponent<?>) comp).getValue();
			if (value == null || value.toString().equals("")) {
				throw new ValidationException("ERROR_EMPTY_FIELD", comp);
			}
		}
	}

	/**
	 * @1.1
	 * 
	 * @param fileName
	 * @throws ValidationException
	 */
	public static void checkValidFile(final String fileName) throws ValidationException {
		final File file = new File(fileName);

		if (file.exists()) {
			throw new ValidationException("FILE_ALREADY_EXISTS");
		}

		try {
			new FileOutputStream(file);
		} catch (final FileNotFoundException e) {
			throw new ValidationException("FILE_NAME_IS_NOT_CORRECT");
		}
	}

	/**
	 *
	 * @param txt
	 *            JComponent
	 * @throws ValidationException
	 */
	public static void checkValidInteger(final JKTextField txt) throws ValidationException {
		txt.setText(txt.getText().trim());
		try {
			Integer.parseInt(txt.getText());
		} catch (final NumberFormatException ex) {
			txt.requestFocus();
			throw new ValidationException("ERROR_MESSAGE_NUMERIC_EXPECTED", txt);
		}
	}

	/**
	 *
	 * @param txt
	 *            JTextComponent
	 */
	public static void checkValidRange(final JKTextField txt, final float rangeFrom, final float rangeTo) throws ValidationException {
		final float value = Float.parseFloat(txt.getText());
		if (!(value >= rangeFrom && value <= rangeTo)) {
			txt.requestFocus();
			throw new ValidationException(Lables.get("VALUE_OUT_OF_RANGE") + "\n" + (int) rangeFrom + "-" + (int) rangeTo, txt);
		}
	}
}
