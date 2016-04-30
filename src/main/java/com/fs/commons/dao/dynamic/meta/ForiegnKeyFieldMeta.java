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
package com.fs.commons.dao.dynamic.meta;

import java.io.Serializable;

public class ForiegnKeyFieldMeta extends FieldMeta {
	public enum Relation implements Serializable {
		ONE_TO_ONE, ONE_TO_MANY, MANY_TO_ONE, MANY_TO_MANY
	}

	public enum ViewMode implements Serializable {
		COMBO, LIST, DIALOG, LOOKUP;
	};

	/**
	 *
	 */
	private static final long serialVersionUID = 2369064347086466554L;

	public static final Relation DEFAULT_RELATION = Relation.ONE_TO_ONE;

	public static final ViewMode DEFAULT_VIEW_MODE = ViewMode.COMBO;

	String referenceTable;

	String aliasNamePostFix = "";

	String referenceField;

	Relation relation = DEFAULT_RELATION;

	ViewMode viewMode = DEFAULT_VIEW_MODE;

	public String getAliasNamePostFix() {
		return this.aliasNamePostFix;
	}

	public String getLeftJoinStatement() {
		return "LEFT JOIN " + getReferenceTable() + " AS " + getReferenceTable() + getAliasNamePostFix() + " ON " + getRelationStatement();
	}

	/**
	 * @return
	 */
	public String getReferenceField() {
		return this.referenceField;
	}

	/**
	 * @return
	 */
	public String getReferenceTable() {
		return this.referenceTable;
	}

	public TableMeta getReferenceTableMeta() {
		return AbstractTableMetaFactory.getTableMeta(getParentTable().getDataSource(), getReferenceTable());
	}

	/**
	 * @return
	 */
	public Relation getRelation() {
		return this.relation;
	}

	private String getRelationStatement() {
		return getFullQualifiedName() + "=" + getReferenceTableMeta().getIdField().getFullQualifiedName(getAliasNamePostFix());
	}

	/**
	 * @return the viewMode
	 */
	public ViewMode getViewMode() {
		return !isEnabled() && this.viewMode == ViewMode.COMBO ? ViewMode.DIALOG : this.viewMode;
	}

	public void setAliasNamePostFix(final Object aliasNamePostFix) {
		this.aliasNamePostFix = aliasNamePostFix.toString();
	}

	/**
	 * @param referenceField
	 */
	public void setReferenceField(final String referenceField) {
		this.referenceField = referenceField;
	}

	/*
	 *
	 */
	public void setReferenceTable(final String referenceTable) {
		this.referenceTable = referenceTable;
	}

	/**
	 * @param relation
	 */
	public void setRelation(final Relation relation) {
		this.relation = relation;
	}

	/**
	 * @param viewMode
	 *            the viewMode to set
	 */
	public void setViewMode(final ViewMode viewMode) {
		if (viewMode == null) {
			this.viewMode = DEFAULT_VIEW_MODE;
		} else {
			this.viewMode = viewMode;
		}
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer(super.toString());
		buf.append("(" + getReferenceTable() + "." + getReferenceField() + ")");
		return buf.toString();
	}
}
