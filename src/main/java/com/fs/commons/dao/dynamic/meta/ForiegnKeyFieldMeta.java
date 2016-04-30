package com.fs.commons.dao.dynamic.meta;

import java.io.Serializable;

public class ForiegnKeyFieldMeta extends FieldMeta {
	public enum Relation implements Serializable {
		ONE_TO_ONE, ONE_TO_MANY, MANY_TO_ONE, MANY_TO_MANY
	};

	public enum ViewMode implements Serializable {
		COMBO, LIST, DIALOG, LOOKUP;
	}

	public static final Relation DEFAULT_RELATION = Relation.ONE_TO_ONE;

	public static final ViewMode DEFAULT_VIEW_MODE = ViewMode.COMBO;

	String referenceTable;

	String aliasNamePostFix = "";

	String referenceField;

	Relation relation = DEFAULT_RELATION;

	ViewMode viewMode = DEFAULT_VIEW_MODE;

	/**
	 * @return
	 */
	public Relation getRelation() {
		return relation;
	}

	/**
	 * @param relation
	 */
	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	/**
	 * @return
	 */
	public String getReferenceField() {
		return referenceField;
	}

	/**
	 * @param referenceField
	 */
	public void setReferenceField(String referenceField) {
		this.referenceField = referenceField;
	}

	/**
	 * @return
	 */
	public String getReferenceTable() {
		return referenceTable;
	}

	/*
	 * 
	 */
	public void setReferenceTable(String referenceTable) {
		this.referenceTable = referenceTable;
	}

	/**
	 * @return the viewMode
	 */
	public ViewMode getViewMode() {
		return !isEnabled() && this.viewMode == ViewMode.COMBO ? ViewMode.DIALOG : this.viewMode;
	}

	/**
	 * @param viewMode
	 *            the viewMode to set
	 */
	public void setViewMode(ViewMode viewMode) {
		if (viewMode == null) {
			this.viewMode = DEFAULT_VIEW_MODE;
		} else {
			this.viewMode = viewMode;
		}
	}

	public TableMeta getReferenceTableMeta() {
		return AbstractTableMetaFactory.getTableMeta(getParentTable().getDataSource(), getReferenceTable());
	}

	public String getLeftJoinStatement() {
		return "LEFT JOIN " + getReferenceTable() + " AS " + getReferenceTable() + getAliasNamePostFix() + " ON " + getRelationStatement();
	}

	private String getRelationStatement() {
		return getFullQualifiedName() + "=" + getReferenceTableMeta().getIdField().getFullQualifiedName(getAliasNamePostFix());
	}

	public String getAliasNamePostFix() {
		return aliasNamePostFix;
	}

	public void setAliasNamePostFix(Object aliasNamePostFix) {
		this.aliasNamePostFix = aliasNamePostFix.toString();
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(super.toString());
		buf.append("(" + getReferenceTable() + "." + getReferenceField() + ")");
		return buf.toString();
	}
}
