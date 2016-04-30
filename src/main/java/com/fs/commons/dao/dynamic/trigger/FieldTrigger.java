package com.fs.commons.dao.dynamic.trigger;

import com.fs.commons.bean.binding.BindingComponent;

public interface FieldTrigger {
	public void fireFocusLost(BindingComponent comp);

	public void fireFocusGained(BindingComponent comp);

	public void onSelected(BindingComponent comp);

	public void onUnSelected(BindingComponent comp);
}
