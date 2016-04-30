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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;

/**
 *
 * @author mkiswani
 *
 */
public class JKFilePanel extends JKPanel {
	// /////////////////////////////////////////////////////////////////////
	class ExtensionFileFilter extends FileFilter {
		String description;

		String extensions[];

		// /////////////////////////////////////////////////////////////////////
		public ExtensionFileFilter(final String extension) {
			this(null, extension.split(","));
		}

		// /////////////////////////////////////////////////////////////////////
		public ExtensionFileFilter(final String description, final String extension) {
			this(description, extension.split(","));
		}

		// /////////////////////////////////////////////////////////////////////
		public ExtensionFileFilter(final String description, final String extensions[]) {
			if (description == null) {
				this.description = "";
				for (final String extension : extensions) {
					this.description += extension + ",";
				}
				this.description = this.description.substring(0, this.description.length() - 1);
			} else {
				this.description = description;
			}
			this.extensions = extensions.clone();
			toLower(this.extensions);
		}

		// /////////////////////////////////////////////////////////////////////
		@Override
		public boolean accept(final File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				final String path = file.getAbsolutePath().toLowerCase();
				for (final String extension : this.extensions) {
					if (path.endsWith(extension) && path.charAt(path.length() - extension.length() - 1) == '.') {
						return true;
					}
				}
			}
			return false;
		}

		// /////////////////////////////////////////////////////////////////////
		@Override
		public String getDescription() {
			return this.description;
		}

		// /////////////////////////////////////////////////////////////////////
		private void toLower(final String array[]) {
			for (int i = 0, n = array.length; i < n; i++) {
				array[i] = array[i].toLowerCase();
			}
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -5117822786775377237L;
	private final JKTextField txtFilePath = new JKTextField(20);
	private final JFileChooser fileChooser = new JFileChooser(".");

	private final JKButton btnOpenFileChooser = new JKButton("BROWSE");

	// //////////////////////////////////////////////////////////////////////////
	public JKFilePanel() {
		init();
	}

	// /////////////////////////////////////////////////////////////////////
	public void checkFields() throws ValidationException {
		this.txtFilePath.checkEmpty();
	}

	// //
	// //////////////////////////////////////////////////////////////////////////
	// public void setExtensions(String extension) {
	// fileChooser.setFileFilter(new ExtensionFileFilter(extension));
	// }

	public File getSelectedFile() {
		return this.fileChooser.getSelectedFile();
	}

	// //////////////////////////////////////////////////////////////////////////
	private void handleOpenFileChooser() {
		final int showOpenDialog = this.fileChooser.showOpenDialog(JKFilePanel.this);
		if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
			this.txtFilePath.setText(this.fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	// //////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////
	protected void init() {
		this.txtFilePath.setEditable(false);
		setLayout(new GridLayout(1, 2));
		add(this.txtFilePath);
		add(this.btnOpenFileChooser);
		setPreferredSize(400, 35);
		this.btnOpenFileChooser.setIcon("browse_3.png");
		this.btnOpenFileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				handleOpenFileChooser();
			}

		});
	}

	// //////////////////////////////////////////////////////////////////////////
	public void setExtensions(final String... extensions) {
		this.fileChooser.setFileFilter(new ExtensionFileFilter(null, extensions));
	}
}
