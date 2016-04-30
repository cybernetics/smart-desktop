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
package com.fs.commons.desktop.validation.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fs.commons.desktop.validation.Problem;
import com.fs.commons.desktop.validation.Severity;

/**
 * A simple panel which can display a problem, fire changes when the problem
 * changes. To use, create your own panel and call setInnerComponent() with it.
 * Call getValidationGroup() to add other components to the validation group.
 *
 * @author Tim Boudreau
 */
public final class ValidationPanel extends JPanel implements ValidationGroupProvider {

	private class VUI implements ValidationUI {

		@Override
		public final void clearProblem() {
			ValidationPanel.this.problemLabel.setText("  "); // NOI18N
			ValidationPanel.this.problemLabel.setIcon(null);
			final Problem old = ValidationPanel.this.problem;
			ValidationPanel.this.problem = null;
			if (old != null) {
				fireChange();
			}
		}

		@Override
		public void setProblem(final Problem problem) {
			if (problem == null) {
				throw new NullPointerException("Null problem");
			}
			final Problem old = ValidationPanel.this.problem;
			ValidationPanel.this.problem = problem;
			ValidationPanel.this.problemLabel.setIcon(problem.severity().icon());
			ValidationPanel.this.problemLabel.setText("<html>" + problem.getMessage());
			ValidationPanel.this.problemLabel.setToolTipText(problem.getMessage());
			ValidationPanel.this.problemLabel.setForeground(colorForSeverity(problem.severity()));
			if (!problem.equals(old)) {
				fireChange();
			}
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 3504749779289508040L;
	private final JLabel problemLabel;
	private final boolean initialized;
	private Problem problem;
	private final List<ChangeListener> listeners = Collections.synchronizedList(new LinkedList<ChangeListener>());
	private final ValidationUI vui = new VUI();

	protected final ValidationGroup group;

	public ValidationPanel() {
		this(null);
	}

	public ValidationPanel(ValidationGroup group) {
		super(new BorderLayout());
		if (group == null) {
			group = ValidationGroup.create(this.vui);
		} else {
			group.addUI(this.vui);
		}
		this.group = group;
		this.problemLabel = group.createProblemLabel();
		this.problemLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		add(this.problemLabel, BorderLayout.SOUTH);
		this.initialized = true;
	}

	/**
	 * Add a change listener which will be notified when the problem returned by
	 * getProblem changes
	 * 
	 * @param cl
	 *            a change listener
	 */
	public final void addChangeListener(final ChangeListener cl) {
		this.listeners.add(cl);
	}

	@Override
	protected void addImpl(final Component comp, final Object constraints, final int index) {
		super.addImpl(comp, constraints, index);
		if (comp instanceof ValidationGroupProvider) {
			final ValidationGroup g = ((ValidationGroupProvider) comp).getValidationGroup();
			this.group.addValidationGroup(this.group, true);
		}
		if (comp instanceof ValidationUI) {
			final ValidationUI theUI = (ValidationUI) comp;
			this.group.addUI(theUI);
		}
	}

	/**
	 * Overridden to call <code>getValidationGroup().validateAll(null);</code>
	 * to make sure any error messages are shown if the initial state of the UI
	 * is invalid.
	 */
	@Override
	public void addNotify() {
		super.addNotify();
		// Validate the initial state
		final Problem p = this.group.validateAll();
		if (p != null) {
			this.vui.setProblem(p);
		}
	}

	private Color colorForSeverity(final Severity s) {
		switch (s) {
		case FATAL: {
			Color c = UIManager.getColor("nb.errorForeground"); // NOI18N
			if (c == null) {
				c = Color.RED.darker();
			}
			return c;
		}
		case WARNING:
			return Color.BLUE.darker();
		case INFO:
			return UIManager.getColor("textText");
		default:
			throw new AssertionError();
		}
	}

	private JDialog createDialog() {
		Window w = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
		if (w == null) {
			final Frame[] f = Frame.getFrames();
			w = f == null || f.length == 0 ? null : f[0];
		}
		JDialog result;
		if (w instanceof Frame) {
			result = new JDialog((Frame) w);
		} else if (w instanceof Dialog) {
			result = new JDialog((Dialog) w);
		} else {
			result = new JDialog();
		}
		if (w != null) {
			result.setLocationRelativeTo(w);
		}
		return result;
	}

	private void fireChange() {
		final ChangeListener[] cl = this.listeners.toArray(new ChangeListener[0]);
		if (cl.length > 0) {
			final ChangeEvent e = new ChangeEvent(this);
			for (final ChangeListener l : cl) {
				l.stateChanged(e);
			}
		}
	}

	/**
	 * Get the last reported problem
	 * 
	 * @return the problem, or null
	 */
	public final Problem getProblem() {
		return this.problem;
	}

	/**
	 * Get this panel's built-in validation group, which drives its display of
	 * error messages. Add an inner panel by calling setInnerComponent(), then
	 * add your components to that, and add them to the validation group using
	 * whatever validators you want
	 * 
	 * @return The validation group
	 */
	@Override
	public final ValidationGroup getValidationGroup() {
		return this.group;
	}

	/**
	 * Determine if there currently is a problem
	 * 
	 * @return true if there is a problem
	 */
	public final boolean isProblem() {
		return this.problem != null;
	}

	/**
	 * Add a change listener which will be notified when the problem returned by
	 * getProblem changes
	 * 
	 * @param cl
	 *            a change listener
	 */
	public final void removeChangeListener(final ChangeListener cl) {
		this.listeners.remove(cl);
	}

	public void removeDelegateValidationUI(final ValidationUI ui) {
		this.group.removeUI(ui);
	}

	public void setDelegateValidationUI(final ValidationUI ui) {
		this.group.addUI(ui);
	}

	/**
	 * Set the inner component which will be displayed above the problem label
	 * 
	 * @param c
	 *            The component
	 */
	public final void setInnerComponent(final Component c) {
		removeAll();
		add(this.problemLabel, BorderLayout.SOUTH);
		add(c, BorderLayout.CENTER);
		if (isDisplayable()) {
			invalidate();
			revalidate();
			repaint();
		}
	}

	/**
	 * Overridden to disallow setting the layout manager. Use
	 * <code>setInnerComponent()</code>.
	 * 
	 * @param mgr
	 */
	@Override
	public final void setLayout(final LayoutManager mgr) {
		if (this.initialized) {
			throw new IllegalStateException("Use setInnerComponent, do not set" + // NOI18N
					" the layout"); // NOI18N
		}
		super.setLayout(mgr);
	}

	public boolean showOkCancelDialog(final String title) {
		final JDialog dlg = createDialog();
		dlg.setModal(true);
		dlg.setLayout(new BorderLayout());
		dlg.setTitle(title);
		final JPanel content = new JPanel();
		content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		content.add(this);
		dlg.add(content, BorderLayout.CENTER);
		final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		final JButton ok = new JButton("OK");
		final JButton cancel = new JButton("Cancel");
		buttons.add(ok);
		buttons.add(cancel);
		dlg.add(buttons, BorderLayout.SOUTH);

		dlg.getRootPane().getActionMap().put("esc", new AbstractAction() { // NOI18N

			@Override
			public void actionPerformed(final ActionEvent e) {
				cancel.doClick();
			}

		});

		dlg.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "esc"); // NOI18N

		final boolean[] result = new boolean[1];
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				result[0] = true;
				dlg.setVisible(false);
				dlg.dispose();
			}
		});
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				result[0] = false;
				dlg.setVisible(false);
				dlg.dispose();
			}
		});
		final ChangeListener cl = new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {
				ok.setEnabled(!isProblem());
			}
		};
		addChangeListener(cl);
		dlg.getRootPane().setDefaultButton(ok);
		dlg.pack();
		dlg.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent e) {
				cancel.doClick();
			}

			@Override
			public void windowOpened(final WindowEvent e) {
				final Problem p = ValidationPanel.this.group.validateAll();
				ok.setEnabled(p == null);
			}
		});
		dlg.setVisible(true);
		removeChangeListener(cl);
		return result[0];
	}
}
