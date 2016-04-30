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
	private JKTextField txtFilePath = new JKTextField(20);
	private JFileChooser fileChooser = new JFileChooser(".");
	private JKButton btnOpenFileChooser = new JKButton("BROWSE");

	// //////////////////////////////////////////////////////////////////////////
	public JKFilePanel() {
		init();
	}

	// //////////////////////////////////////////////////////////////////////////
	protected void init() {
		txtFilePath.setEditable(false);
		setLayout(new GridLayout(1, 2));
		add(txtFilePath);
		add(btnOpenFileChooser);
		setPreferredSize(400, 35);
		btnOpenFileChooser.setIcon("browse_3.png");
		btnOpenFileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				handleOpenFileChooser();
			}

		});
	}

	// //////////////////////////////////////////////////////////////////////////
	public void setExtensions(String... extensions) {
		fileChooser.setFileFilter(new ExtensionFileFilter(null, extensions));
	}

	// //
	// //////////////////////////////////////////////////////////////////////////
	// public void setExtensions(String extension) {
	// fileChooser.setFileFilter(new ExtensionFileFilter(extension));
	// }

	// //////////////////////////////////////////////////////////////////////////
	private void handleOpenFileChooser() {
		int showOpenDialog = fileChooser.showOpenDialog(JKFilePanel.this);
		if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
			txtFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	// /////////////////////////////////////////////////////////////////////
	public void checkFields() throws ValidationException {
		txtFilePath.checkEmpty();
	}

	// //////////////////////////////////////////////////////////////////////////

	public File getSelectedFile() {
		return fileChooser.getSelectedFile();
	}

	// /////////////////////////////////////////////////////////////////////
	class ExtensionFileFilter extends FileFilter {
		String description;

		String extensions[];

		// /////////////////////////////////////////////////////////////////////
		public ExtensionFileFilter(String extension) {
			this(null, extension.split(","));
		}

		// /////////////////////////////////////////////////////////////////////
		public ExtensionFileFilter(String description, String extension) {
			this(description, extension.split(","));
		}

		// /////////////////////////////////////////////////////////////////////
		public ExtensionFileFilter(String description, String extensions[]) {
			if (description == null) {
				this.description="";
				for (String extension : extensions) {
					this.description+= extension +",";
				}
				this.description = this.description.substring(0,this.description.length()-1);
			} else {
				this.description = description;
			}
			this.extensions = (String[]) extensions.clone();
			toLower(this.extensions);
		}

		// /////////////////////////////////////////////////////////////////////
		private void toLower(String array[]) {
			for (int i = 0, n = array.length; i < n; i++) {
				array[i] = array[i].toLowerCase();
			}
		}

		// /////////////////////////////////////////////////////////////////////
		public String getDescription() {
			return description;
		}

		// /////////////////////////////////////////////////////////////////////
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				String path = file.getAbsolutePath().toLowerCase();
				for (int i = 0, n = extensions.length; i < n; i++) {
					String extension = extensions[i];
					if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
						return true;
					}
				}
			}
			return false;
		}
	}
}
