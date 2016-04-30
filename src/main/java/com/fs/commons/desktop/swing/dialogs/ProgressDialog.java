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
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	public interface Task extends Runnable {
		String getMessage();

		int getPercentComplete();

		void addChangeListener(ChangeListener l);
	}

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

	private Vector<Runnable> tasks = new Vector<Runnable>();
	private JKLabel messageLabel = null;
	private JKButton closeButton = new JKButton("Cancel");
	private Component closePanel = null;
	private JProgressBar progressBar = null;
	private String completionMessage = null;
	private boolean cancelled = false;
	private String closeText = "Close";
	private boolean conteniueProcessing;

	/**
	 * 
	 * @param parent
	 * @param title
	 * @param message
	 */
	public ProgressDialog(Frame parent, String title, String message) {
		super(parent, title);
		init(parent, message, false);
	}

	/**
	 * 
	 * @param parent
	 * @param title
	 * @param message
	 */
	public ProgressDialog(Frame parent, String title, String message, boolean modal) {
		super(parent, title);
		init(parent, message, modal);
	}

	/**
	 * 
	 * @param parent
	 * @param title
	 * @param message
	 */
	public ProgressDialog(Dialog parent, String title, String message) {
		super(parent, title);
		init(parent, message, false);
	}

	/**
	 * 
	 * @param parent
	 * @param title
	 * @param message
	 */
	public ProgressDialog(Dialog parent, String title, String message, boolean modal) {
		super(parent, title);
		init(parent, message, modal);
	}

	/**
	 * 
	 * @param parent
	 * @param message
	 * @param modal
	 */
	private void init(Component parent, String message, boolean modal) {
		setModal(modal);
		setPreferredSize(new Dimension(300, 120));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		if (message == null) {
			message = Lables.get("PLEASE_WAIT");
		}
		messageLabel = new JKLabel(message);

		JKLabel iconLabel = new JKLabel("");
		iconLabel.setIcon("progress.gif");

		JKPanel pnl = new JKPanel(new GridLayout(2, 1));
		pnl.add(messageLabel);
		pnl.add(iconLabel);

		progressBar = new JProgressBar();
		getContentPane().add(pnl, BorderLayout.NORTH);
		getContentPane().add(progressBar, BorderLayout.CENTER);

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleClose();
			}
		});
		closePanel = borderComponent(closeButton);
		closePanel.setVisible(false);
		getContentPane().add(closePanel, BorderLayout.SOUTH);
		// getContentPane().add(new JKPanel( closeButton),BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(parent);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				conteniueProcessing = false;
			}
		});
	}

	/**
	 * 
	 * @param title
	 * @param message
	 * @return
	 */
	public static ProgressDialog create(String title, String message) {
		return create(SwingUtility.getDefaultMainFrame(), title, message, false);
	}

	/**
	 * 
	 * @param title
	 * @param message
	 * @return
	 */
	public static ProgressDialog create(String title, String message, boolean modal) {
		return create(SwingUtility.getDefaultMainFrame(), title, message, modal);
	}

	/**
	 * 
	 * @param parent
	 * @param title
	 * @param message
	 * @return
	 */
	public static ProgressDialog create(Object parent, String title, String message, boolean modal) {
		if (parent instanceof Frame)
			return new ProgressDialog((Frame) parent, title, message, modal);
		else if (parent instanceof Dialog)
			return new ProgressDialog((Dialog) parent, title, message, modal);
		else
			return null;
	}

	/**
	 * 
	 * @param comp
	 * @return
	 */
	private Component borderComponent(Component comp) {
		Box horizBox = Box.createHorizontalBox();
		horizBox.add(Box.createHorizontalGlue());
		horizBox.add(comp);
		horizBox.add(Box.createHorizontalGlue());

		Box vertBox = Box.createVerticalBox();
		vertBox.add(Box.createVerticalStrut(5));
		vertBox.add(horizBox);
		vertBox.add(Box.createVerticalStrut(5));

		return vertBox;
	}

	/**
	 * Add a task for this dialog to perform.
	 * 
	 * All tasks must be added <b>before</b> calling the <code>run()</code>
	 * method.
	 */
	public void addTask(Runnable r) {
		tasks.add(r);
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
	public void setCompletionMessage(String msg) {
		completionMessage = msg;
	}

	/**
	 * State whether the tasks run by this dialog can be cancelled.
	 * 
	 * If this parameter is set to true, the progess dialog will display a
	 * cancel button.
	 */
	public void setCancellable(boolean canCancel) {
		closePanel.setVisible(canCancel);
		pack();
	}

	/**
	 * Set the text that should be used for the cancel button.
	 */
	public void setCancelText(String text) {
		closeButton.setText(text);
	}

	/**
	 * Set the text that should be used for the close button.
	 */
	public void setCloseText(String text) {
		this.closeText = text;
	}

	/**
	 * Displays the dialog, and runs the tasks in the order they were added.
	 */
	public void run() {
		conteniueProcessing = true;
		progressBar.setMaximum(tasks.size() * 100);
		WorkThread w = new WorkThread();
		w.start();
		setVisible(true); // this will block until the work thread finishes
	}

	public void finished() {
		tasks.clear();

		if (completionMessage == null)
			ProgressDialog.this.dispose();

		else {
			messageLabel.setText(completionMessage);

			getContentPane().remove(progressBar);
			closeButton.setText(closeText);
			closePanel.setVisible(true);
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			invalidate();
			pack();
		}
	}

	private class WorkThread extends Thread implements ChangeListener {
		private Runnable task;
		private int i;

		public void run() {
			for (i = 0; i < tasks.size(); progressBar.setValue(++i * 100)) {
				if (cancelled)
					break;

				try {
					task = (Runnable) tasks.get(i);
					if (task instanceof Task) {
						String msg = ((Task) task).getMessage();
						progressBar.setString(msg);
						progressBar.setStringPainted(msg != null);

						((Task) task).addChangeListener(this);
					}
					task.run();
				} catch (Throwable t) {
				}
			}
			conteniueProcessing = false;
			finished();
		}

		public void stateChanged(ChangeEvent e) {
			if (cancelled && (task instanceof CancellableTask))
				throw new CancelledException();

			try {
				int percent = ((Task) task).getPercentComplete() % 100;
				progressBar.setValue(i * 100 + percent);

				String msg = ((Task) task).getMessage();
				progressBar.setStringPainted(msg != null);
				progressBar.setString(msg);
			} catch (Exception ex) {
			}
		}
	}

	public void setProgressCount(final String string) {
		messageLabel.setText(string);
		ProgressDialog.this.pack();
	}

	public boolean isConteniueProcessing() {
		return conteniueProcessing;
	}

	private void handleClose() {
		dispose();
		throw new RuntimeException("Progress bar has been interuupred by user");
		// if (running)
		// cancelled = true;
		// else
		// dispose();
	}
}
