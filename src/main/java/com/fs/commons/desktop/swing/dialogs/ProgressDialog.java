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
package com.fs.commons.desktop.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.locale.Lables;
import com.jk.exceptions.handler.JKExceptionUtil;

/**
 * A useful class which displays a dialog box with a progress bar, then runs a
 * sequence of tasks, updating the progress bar as each task completes.
 *
 * The tasks in question can be any object which implements Runnable. If,
 * however, the task objects implement ProgressDialog.Task, they can provide a
 * string to be displayed in the progress bar, and they can provide
 * finer-grained progress completion data.
 */
public class ProgressDialog extends JKDialog {
	/**
	 *
	 * @author Administrator
	 *
	 */
	public interface CancellableTask extends Task {
	}

	/**
	 *
	 * @author Administrator
	 *
	 */
	public class CancelledException extends Error {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
	}

	/**
	 *
	 * @author Administrator
	 *
	 */
	public interface Task extends Runnable {
		void addChangeListener(ChangeListener l);

		String getMessage();

		int getPercentComplete();
	}

	private class WorkThread extends Thread implements ChangeListener {
		private Runnable task;
		private int i;

		@Override
		public void run() {
			for (this.i = 0; this.i < ProgressDialog.this.tasks.size(); ProgressDialog.this.progressBar.setValue(++this.i * 100)) {
				if (ProgressDialog.this.cancelled) {
					break;
				}

				try {
					this.task = ProgressDialog.this.tasks.get(this.i);
					if (this.task instanceof Task) {
						final String msg = ((Task) this.task).getMessage();
						ProgressDialog.this.progressBar.setString(msg);
						ProgressDialog.this.progressBar.setStringPainted(msg != null);

						((Task) this.task).addChangeListener(this);
					}
					this.task.run();
				} catch (final Throwable t) {
					JKExceptionUtil.handle(t);
				} finally {
					ProgressDialog.this.conteniueProcessing = false;
					finished();
				}
			}
		}

		@Override
		public void stateChanged(final ChangeEvent e) {
			if (ProgressDialog.this.cancelled && this.task instanceof CancellableTask) {
				throw new CancelledException();
			}

			try {
				final int percent = ((Task) this.task).getPercentComplete() % 100;
				ProgressDialog.this.progressBar.setValue(this.i * 100 + percent);

				final String msg = ((Task) this.task).getMessage();
				ProgressDialog.this.progressBar.setStringPainted(msg != null);
				ProgressDialog.this.progressBar.setString(msg);
			} catch (final Exception ex) {
			}
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param parent
	 * @param title
	 * @param message
	 * @return
	 */
	public static ProgressDialog create(final Object parent, final String title, final String message, final boolean modal) {
		if (parent instanceof Frame) {
			return new ProgressDialog((Frame) parent, title, message, modal);
		} else if (parent instanceof Dialog) {
			return new ProgressDialog((Dialog) parent, title, message, modal);
		} else {
			return null;
		}
	}

	/**
	 *
	 * @param title
	 * @param message
	 * @return
	 */
	public static ProgressDialog create(final String title, final String message) {
		return create(SwingUtility.getDefaultMainFrame(), title, message, false);
	}

	/**
	 *
	 * @param title
	 * @param message
	 * @return
	 */
	public static ProgressDialog create(final String title, final String message, final boolean modal) {
		return create(SwingUtility.getDefaultMainFrame(), title, message, modal);
	}

	private final Vector<Runnable> tasks = new Vector<Runnable>();
	private JKLabel messageLabel = null;
	private final JKButton closeButton = new JKButton("Cancel");
	private Component closePanel = null;
	private JProgressBar progressBar = null;

	private String completionMessage = null;

	private final boolean cancelled = false;

	private String closeText = "Close";

	private boolean conteniueProcessing;

	/**
	 *
	 * @param parent
	 * @param title
	 * @param message
	 */
	public ProgressDialog(final Dialog parent, final String title, final String message) {
		super(parent, title);
		init(parent, message, false);
	}

	/**
	 *
	 * @param parent
	 * @param title
	 * @param message
	 */
	public ProgressDialog(final Dialog parent, final String title, final String message, final boolean modal) {
		super(parent, title);
		init(parent, message, modal);
	}

	/**
	 *
	 * @param parent
	 * @param title
	 * @param message
	 */
	public ProgressDialog(final Frame parent, final String title, final String message) {
		super(parent, title);
		init(parent, message, false);
	}

	/**
	 *
	 * @param parent
	 * @param title
	 * @param message
	 */
	public ProgressDialog(final Frame parent, final String title, final String message, final boolean modal) {
		super(parent, title);
		init(parent, message, modal);
	}

	/**
	 * Add a task for this dialog to perform.
	 *
	 * All tasks must be added <b>before</b> calling the <code>run()</code>
	 * method.
	 */
	public void addTask(final Runnable r) {
		this.tasks.add(r);
	}

	/**
	 *
	 * @param comp
	 * @return
	 */
	private Component borderComponent(final Component comp) {
		final Box horizBox = Box.createHorizontalBox();
		horizBox.add(Box.createHorizontalGlue());
		horizBox.add(comp);
		horizBox.add(Box.createHorizontalGlue());

		final Box vertBox = Box.createVerticalBox();
		vertBox.add(Box.createVerticalStrut(5));
		vertBox.add(horizBox);
		vertBox.add(Box.createVerticalStrut(5));

		return vertBox;
	}

	public void finished() {
		this.tasks.clear();

		if (this.completionMessage == null) {
			ProgressDialog.this.dispose();
		} else {
			this.messageLabel.setText(this.completionMessage);

			getContentPane().remove(this.progressBar);
			this.closeButton.setText(this.closeText);
			this.closePanel.setVisible(true);
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			invalidate();
			pack();
		}
	}

	private void handleClose() {
		dispose();
		throw new RuntimeException("Progress bar has been interuupred by user");
		// if (running)
		// cancelled = true;
		// else
		// dispose();
	}

	/**
	 *
	 * @param parent
	 * @param message
	 * @param modal
	 */
	private void init(final Component parent, String message, final boolean modal) {
		setModal(modal);
		setPreferredSize(new Dimension(300, 120));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		if (message == null) {
			message = Lables.get("PLEASE_WAIT");
		}
		this.messageLabel = new JKLabel(message);

		final JKLabel iconLabel = new JKLabel("");
		iconLabel.setIcon("progress.gif");

		final JKPanel pnl = new JKPanel(new GridLayout(2, 1));
		pnl.add(this.messageLabel);
		pnl.add(iconLabel);

		this.progressBar = new JProgressBar();
		getContentPane().add(pnl, BorderLayout.NORTH);
		getContentPane().add(this.progressBar, BorderLayout.CENTER);

		this.closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleClose();
			}
		});
		this.closePanel = borderComponent(this.closeButton);
		this.closePanel.setVisible(false);
		getContentPane().add(this.closePanel, BorderLayout.SOUTH);
		// getContentPane().add(new JKPanel( closeButton),BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(parent);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				ProgressDialog.this.conteniueProcessing = false;
			}
		});
	}

	public boolean isConteniueProcessing() {
		return this.conteniueProcessing;
	}

	/**
	 * Displays the dialog, and runs the tasks in the order they were added.
	 */
	public void run() {
		this.conteniueProcessing = true;
		this.progressBar.setMaximum(this.tasks.size() * 100);
		final WorkThread w = new WorkThread();
		w.start();
		setVisible(true); // this will block until the work thread finishes
	}

	/**
	 * State whether the tasks run by this dialog can be cancelled.
	 *
	 * If this parameter is set to true, the progess dialog will display a
	 * cancel button.
	 */
	public void setCancellable(final boolean canCancel) {
		this.closePanel.setVisible(canCancel);
		pack();
	}

	/**
	 * Set the text that should be used for the cancel button.
	 */
	public void setCancelText(final String text) {
		this.closeButton.setText(text);
	}

	/**
	 * Set the text that should be used for the close button.
	 */
	public void setCloseText(final String text) {
		this.closeText = text;
	}

	/**
	 * Request that a message be displayed when the dialog completes.
	 *
	 * By default, the completion message is <code>null</code>, which tells the
	 * progress dialog to automatically close upon completion. If a non-null
	 * completion message is set, when the progress dialog finishes running it
	 * will not close automatically; instead, it will change the label to
	 * display the completion message, and replace the progress bar with a
	 * "Close" button.
	 */
	public void setCompletionMessage(final String msg) {
		this.completionMessage = msg;
	}

	public void setProgressCount(final String string) {
		this.messageLabel.setText(string);
		ProgressDialog.this.pack();
	}
}
