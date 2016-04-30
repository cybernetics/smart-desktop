/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
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
 * changes.  To use, create your own panel and call setInnerComponent() with it.
 * Call getValidationGroup() to add other components to the validation group.
 *
 * @author Tim Boudreau
 */
public final class ValidationPanel extends JPanel implements ValidationGroupProvider {

    private final JLabel problemLabel;
    private boolean initialized;
    private Problem problem;
    private final List<ChangeListener> listeners = Collections.synchronizedList(
            new LinkedList<ChangeListener>());
    private final ValidationUI vui = new VUI();
    protected final ValidationGroup group;

    public ValidationPanel (ValidationGroup group) {
        super(new BorderLayout());
        if (group == null) {
            group = ValidationGroup.create(vui);
        } else {
            group.addUI(vui);
        }
        this.group = group;
        problemLabel = group.createProblemLabel();
        problemLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        add(problemLabel, BorderLayout.SOUTH);
        initialized = true;
    }

    public ValidationPanel() {
        this(null);
    }

    public void setDelegateValidationUI(ValidationUI ui) {
        group.addUI(ui);
    }

    public void removeDelegateValidationUI (ValidationUI ui) {
        group.removeUI(ui);
    }

    private JDialog createDialog() {
        Window w = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        if (w == null) {
            Frame[] f = Frame.getFrames();
            w = f == null || f.length == 0 ? null : f[0];
        }
        JDialog result;
        if (w instanceof Frame) {
            result = new JDialog ((Frame) w);
        } else if (w instanceof Dialog) {
            result = new JDialog ((Dialog) w);
        } else {
            result = new JDialog();
        }
        if (w != null) {
            result.setLocationRelativeTo(w);
        }
        return result;
    }

    public boolean showOkCancelDialog(String title) {
        final JDialog dlg = createDialog();
        dlg.setModal(true);
        dlg.setLayout(new BorderLayout());
        dlg.setTitle (title);
        JPanel content = new JPanel();
        content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(this);
        dlg.add(content, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        final JButton ok = new JButton("OK");
        final JButton cancel = new JButton("Cancel");
        buttons.add(ok);
        buttons.add(cancel);
        dlg.add(buttons, BorderLayout.SOUTH);

        dlg.getRootPane().getActionMap().put("esc", new AbstractAction(){ //NOI18N

            public void actionPerformed(ActionEvent e) {
                cancel.doClick();
            }

        });

        dlg.getRootPane().getInputMap(
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).
                put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "esc"); //NOI18N

        final boolean[] result = new boolean[1];
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                result[0] = true;
                dlg.setVisible(false);
                dlg.dispose();
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                result[0] = false;
                dlg.setVisible(false);
                dlg.dispose();
            }
        });
        ChangeListener cl = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                ok.setEnabled(!isProblem());
            }
        };
        addChangeListener(cl);
        dlg.getRootPane().setDefaultButton(ok);
        dlg.pack();
        dlg.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                Problem p = group.validateAll();
                ok.setEnabled(p == null);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                cancel.doClick();
            }
        });
        dlg.setVisible(true);
        removeChangeListener(cl);
        return result[0];
    }

    /**
     * Overridden to call <code>getValidationGroup().validateAll(null);</code>
     * to make sure any error messages are shown if the initial state of the
     * UI is invalid.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        //Validate the initial state
        Problem p = group.validateAll();
        if (p != null) {
            vui.setProblem(p);
        }
    }

    private Color colorForSeverity(Severity s) {
        switch (s) {
            case FATAL: {
                Color c = UIManager.getColor("nb.errorForeground"); //NOI18N
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

    /**
     * Get this panel's built-in validation group, which drives its display
     * of error messages.  Add an inner panel by calling setInnerComponent(),
     * then add your components to that, and add them to the validation group
     * using whatever validators you want
     * @return The validation group
     */
    public final ValidationGroup getValidationGroup() {
        return group;
    }

    /**
     * Overridden to disallow setting the layout manager.  Use
     * <code>setInnerComponent()</code>.
     * @param mgr
     */
    @Override
    public final void setLayout(LayoutManager mgr) {
        if (initialized) {
            throw new IllegalStateException("Use setInnerComponent, do not set" + //NOI18N
                    " the layout"); //NOI18N
        }
        super.setLayout(mgr);
    }

    /**
     * Set the inner component which will be displayed above the
     * problem label
     * @param c The component
     */
    public final void setInnerComponent(Component c) {
        removeAll();
        add(problemLabel, BorderLayout.SOUTH);
        add(c, BorderLayout.CENTER);
        if (isDisplayable()) {
            invalidate();
            revalidate();
            repaint();
        }
    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);
        if (comp instanceof ValidationGroupProvider) {
            ValidationGroup g = ((ValidationGroupProvider) comp).getValidationGroup();
            group.addValidationGroup(group, true);
        }
        if (comp instanceof ValidationUI) {
            ValidationUI theUI = (ValidationUI) comp;
            group.addUI(theUI);
        }
    }

    private class VUI implements ValidationUI {

        public final void clearProblem() {
            problemLabel.setText("  "); //NOI18N
            problemLabel.setIcon(null);
            Problem old = ValidationPanel.this.problem;
            ValidationPanel.this.problem = null;
            if (old != null) {
                fireChange();
            }
        }

        public void setProblem(Problem problem) {
            if (problem == null) {
                throw new NullPointerException("Null problem");
            }
            Problem old = ValidationPanel.this.problem;
            ValidationPanel.this.problem = problem;
            problemLabel.setIcon(problem.severity().icon());
            problemLabel.setText("<html>" + problem.getMessage());
            problemLabel.setToolTipText(problem.getMessage());
            problemLabel.setForeground(colorForSeverity(problem.severity()));
            if (!problem.equals(old)) {
                fireChange();
            }
        }
    }

    /**
     * Get the last reported problem
     * @return the problem, or null
     */
    public final Problem getProblem() {
        return problem;
    }

    /**
     * Determine if there currently is a problem
     * @return true if there is a problem
     */
    public final boolean isProblem() {
        return problem != null;
    }

    /**
     * Add a change listener which will be notified when the problem
     * returned by getProblem changes
     * @param cl a change listener
     */
    public final void addChangeListener(ChangeListener cl) {
        listeners.add(cl);
    }

    /**
     * Add a change listener which will be notified when the problem
     * returned by getProblem changes
     * @param cl a change listener
     */
    public final void removeChangeListener(ChangeListener cl) {
        listeners.remove(cl);
    }

    private void fireChange() {
        ChangeListener[] cl = listeners.toArray(new ChangeListener[0]);
        if (cl.length > 0) {
            ChangeEvent e = new ChangeEvent(this);
            for (ChangeListener l : cl) {
                l.stateChanged(e);
            }
        }
    }
}
