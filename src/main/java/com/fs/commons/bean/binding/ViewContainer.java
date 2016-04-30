package com.fs.commons.bean.binding;

import java.util.Map;

public interface ViewContainer {
	
	/**
	 * 
	 * @param viewName
	 * @return
	 */
	public BindingComponent getViewComponent(String viewName);
	
	/**
	 * 
	 * @return
	 */
	public Map getViewComponents();
}
