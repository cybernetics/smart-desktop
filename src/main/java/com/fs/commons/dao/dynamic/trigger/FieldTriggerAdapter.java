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
package com.fs.commons.dao.dynamic.trigger;

import java.awt.Container;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.dynform.ui.DynPanel;
import com.fs.commons.desktop.swing.SwingUtility;

public class FieldTriggerAdapter implements FieldTrigger {

	protected BindingComponent findNeighber(final BindingComponent comp, final String neighberName) {
		return SwingUtility.findBindingComponent(getMainContainer(comp), neighberName);
	}

	@Override
	public void fireFocusGained(final BindingComponent comp) {
	}

	@Override
	public void fireFocusLost(final BindingComponent comp) {
	}

	protected Container getMainContainer(final BindingComponent field) {
		Container parent = field.getParent();
		while (!(parent instanceof DynPanel)) {
			parent = parent.getParent();
		}
		return parent;
	}

	// protected Component findFieldInPnl(JKPanel pnl, String fieldName) {
	// Component[] components = pnl.getComponents();
	// for (Component component : components) {
	// if(component instanceof JKPanel){
	// Component component2 = findFieldInPnl((JKPanel)component, fieldName);
	// if(component2 != null){
	// return component2 ;
	// }
	// }else{
	// if(component.getName() != null &&
	// component.getName().equalsIgnoreCase(fieldName)){
	// return component;
	// }
	// }
	// }
	// return null;
	// }

	@Override
	public void onSelected(final BindingComponent comp) {

	}

	@Override
	public void onUnSelected(final BindingComponent comp) {

	}

}
