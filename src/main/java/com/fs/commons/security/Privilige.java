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
package com.fs.commons.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import com.fs.commons.desktop.swing.tree.TreeCheckBoxNode;
import com.fs.commons.locale.Lables;

public class Privilige implements TreeCheckBoxNode {

	int priviligeId;
	String priviligeName;
	Privilige parentPrivlige;
	String desc;
	ArrayList<Privilige> childs = new ArrayList<Privilige>();
	boolean selected;// to be used on the GUI when selected in the security
	private boolean editable = true;
	private int number;

	// panel

	public Privilige() {
	}

	public Privilige(final int priviligeId) {
		this.priviligeId = priviligeId;
	}

	public Privilige(final int priviligeId, final String name, final Privilige parent) {
		this(priviligeId, name, parent, 0);
	}

	public Privilige(final int priviligeId, final String name, final Privilige parent, final int number) {
		this.priviligeId = priviligeId;
		this.priviligeName = name;
		this.parentPrivlige = parent;
		this.number = number;
	}

	@Override
	public Enumeration children() {
		return Collections.enumeration(this.childs);
	}

	@Override
	public boolean equals(final Object obj) {
		final Privilige that = (Privilige) obj;
		return this.priviligeId == that.priviligeId;
	}

	@Override
	public boolean getAllowsChildren() {
		return this.childs.size() > 0;
	}

	@Override
	public TreeNode getChildAt(final int childIndex) {
		return this.childs.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return this.childs.size();
	}

	public ArrayList<Privilige> getChilds() {
		return this.childs;
	}

	public String getDesc() {
		return Lables.get(getPriviligeName(), true);
	}

	@Override
	public int getIndex(final TreeNode node) {
		return this.childs.indexOf(node);
	}

	public int getNumber() {
		return this.number;
	}

	@Override
	public TreeNode getParent() {
		return this.parentPrivlige;
	}

	public Privilige getParentPrivlige() {
		return this.parentPrivlige;
	}

	/**
	 * @return the priviligeId
	 */
	public int getPriviligeId() {
		return this.priviligeId;
	}

	/**
	 * @return the priviligeName
	 */
	public String getPriviligeName() {
		return this.priviligeName;
	}

	@Override
	public String getText() {
		return toString(false);
	}

	@Override
	public boolean isEditable() {
		return this.editable;
	}

	@Override
	public boolean isLeaf() {
		return this.childs.size() == 0;
	}

	@Override
	public boolean isSelected() {
		return this.selected;
	}

	public void setChilds(final ArrayList<Privilige> childs) {
		this.childs = childs;
	}

	public void setDesc(final String desc) {
		this.desc = desc;
	}

	public void setEditable(final boolean editable) {
		this.editable = editable;
	};

	public void setNumber(final int number) {
		this.number = number;
	}

	public void setParentPrivlige(final Privilige parentPrivlige) {
		this.parentPrivlige = parentPrivlige;
	}

	/**
	 * @param priviligeId
	 *            the priviligeId to set
	 */
	public void setPriviligeId(final int priviligeId) {
		this.priviligeId = priviligeId;
	}

	/**
	 * @param priviligeName
	 *            the priviligeName to set
	 */
	public void setPriviligeName(final String priviligeName) {
		this.priviligeName = priviligeName;
	}

	@Override
	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return toString(false);
	}

	public String toString(final boolean deep) {
		final StringBuffer buf = new StringBuffer(this.number + " : ");
		if (deep) {
			buf.append(getPriviligeId());
			buf.append(",");
		}
		buf.append(getDesc());
		if (deep && this.childs.size() > 0) {
			buf.append(this.childs.toString());
		}
		return buf.toString();
	}
}
