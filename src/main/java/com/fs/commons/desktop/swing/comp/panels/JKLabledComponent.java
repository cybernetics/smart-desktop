package com.fs.commons.desktop.swing.comp.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.desktop.swing.comp.JKTextField;

public class JKLabledComponent extends JKPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BindingComponent txt;

	private BindingComponent lbl;

	private int lableWidth=75;

	public JKLabledComponent(String labelKey, BindingComponent comp) {
		this(new JKLabel(labelKey), 120, comp);
	}

	/**
	 * 
	 * @param lbl
	 *            JKLabel
	 * @param txt
	 *            JTextComponent
	 */
	public JKLabledComponent(String labelKey, int labelWidth, BindingComponent comp) {
		this(labelKey, labelWidth, comp,true);
	}
	
	public JKLabledComponent(String labelKey, int labelWidth, BindingComponent comp,boolean visible) {
		this(new JKLabel(labelKey), labelWidth, comp);
		setVisible(visible);
	}

	/**
	 * 
	 * @param lbl
	 *            JKLabel
	 * @param txt
	 *            JTextComponent
	 */
	public JKLabledComponent(JKLabel lbl, int labelWidth, BindingComponent txt) {
		setLableWidth(labelWidth);
		init();
		add(lbl);
		add((Component) txt);
	}

	public int getLableWidth() {
		return lableWidth;
	}

	public void setLableWidth(int lableWidth) {
		this.lableWidth = lableWidth;
	}

	public JKLabledComponent(JKLabel lbl, JKTextField txt) {
		init();
		add(lbl);
		add(txt);
	}

	public JKLabledComponent() {
		init();
	}

	public JKLabledComponent(String label, int labelWidth, JKTextField txt, int txtWidth) {
		this(label,labelWidth,txt);
		txt.setWidth(txtWidth);
	}

	/**
	 * 
	 */
	protected void init() {
		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		setFocusable(false);
		setLayout(new BorderLayout());
		// ((FlowLayout)getLayout()).setAlignment(FlowLayout.LEADING);
		SwingUtility.setFont(this);
		// txt.getPreferredSize().setSize(1, 30);
		applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());
	}

	@Override
	public void setLayout(LayoutManager mgr) {
		if (mgr instanceof BorderLayout) {
			super.setLayout(mgr);
		}
	}
	
	@Override
	protected void addImpl(Component comp, Object constraints, int index) {
		if(comp==null){
			return;
		}
		if(lbl==null){
			lbl=(BindingComponent) comp;
			Dimension preferredSize = comp.getPreferredSize();
			preferredSize.width=getLableWidth();
			lbl.setPreferredSize(preferredSize);
			super.addImpl(comp,BorderLayout.LINE_START,0);
		}else if(txt==null){
			txt=(BindingComponent) comp;
			super.addImpl(comp,BorderLayout.CENTER,1);
		}else{
			//just ignore the call
		}
		
		//super.addImpl(comp, constraints, index);
	}

	
	
}
