package com.fs.commons.dao.dynamic.trigger;


import java.awt.Container;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.dynform.ui.DynPanel;
import com.fs.commons.desktop.swing.SwingUtility;

public class FieldTriggerAdapter implements FieldTrigger{
	
	public void fireFocusLost(BindingComponent comp){
	}
	
	public void fireFocusGained(BindingComponent comp){
	}

	@Override
	public void onSelected(BindingComponent comp) {
		
	}

	@Override
	public void onUnSelected(BindingComponent comp) {
		
	}

//	protected Component findFieldInPnl(JKPanel pnl, String fieldName) {
//		Component[] components = pnl.getComponents();
//		for (Component component : components) {
//			if(component instanceof JKPanel){
//				Component component2 = findFieldInPnl((JKPanel)component, fieldName);
//				if(component2 != null){
//					return component2 ;
//				}
//			}else{
//				if(component.getName() != null && component.getName().equalsIgnoreCase(fieldName)){
//					return component;
//				}
//			}
//		}
//		return null;
//	}

	protected Container getMainContainer(BindingComponent field){
		Container parent = field.getParent();
		while( !(parent instanceof DynPanel)){
			parent = parent.getParent();
		}
		return parent;
	}
	
	
	protected BindingComponent findNeighber(BindingComponent comp, String neighberName) {
		return SwingUtility.findBindingComponent(getMainContainer(comp),neighberName);
	}

}
