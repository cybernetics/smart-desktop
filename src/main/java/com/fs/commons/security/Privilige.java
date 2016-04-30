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

	public Privilige(int priviligeId, String name, Privilige parent) {
		this(priviligeId, name, parent, 0);
	}

	public Privilige(int priviligeId, String name, Privilige parent, int number) {
		this.priviligeId = priviligeId;
		this.priviligeName = name;
		parentPrivlige = parent;
		this.number = number;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the priviligeId
	 */
	public int getPriviligeId() {
		return priviligeId;
	}

	/**
	 * @param priviligeId
	 *            the priviligeId to set
	 */
	public void setPriviligeId(int priviligeId) {
		this.priviligeId = priviligeId;
	}

	/**
	 * @return the priviligeName
	 */
	public String getPriviligeName() {
		return priviligeName;
	}

	/**
	 * @param priviligeName
	 *            the priviligeName to set
	 */
	public void setPriviligeName(String priviligeName) {
		this.priviligeName = priviligeName;
	}

	public Privilige() {
	}

	public Privilige(int priviligeId) {
		this.priviligeId = priviligeId;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return childs.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return childs.size();
	}

	@Override
	public TreeNode getParent() {
		return parentPrivlige;
	}

	@Override
	public int getIndex(TreeNode node) {
		return childs.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return childs.size() > 0;
	}

	@Override
	public boolean isLeaf() {
		return childs.size() == 0;
	}

	@Override
	public Enumeration children() {
		return Collections.enumeration(childs);
	}

	public Privilige getParentPrivlige() {
		return parentPrivlige;
	}

	public void setParentPrivlige(Privilige parentPrivlige) {
		this.parentPrivlige = parentPrivlige;
	}

	public ArrayList<Privilige> getChilds() {
		return childs;
	}

	public void setChilds(ArrayList<Privilige> childs) {
		this.childs = childs;
	}

	public String getDesc() {
		return Lables.get(getPriviligeName(), true);
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return toString(false);
	};

	public String toString(boolean deep) {
		StringBuffer buf = new StringBuffer(number + " : ");
		if (deep) {
			buf.append(getPriviligeId());
			buf.append(",");
		}
		buf.append(getDesc());
		if (deep && childs.size() > 0) {
			buf.append(childs.toString());
		}
		return buf.toString();
	}

	@Override
	public String getText() {
		return toString(false);
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public boolean equals(Object obj) {
		Privilige that = (Privilige) obj;
		return this.priviligeId == that.priviligeId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
