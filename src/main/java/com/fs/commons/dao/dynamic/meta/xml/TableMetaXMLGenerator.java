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
package com.fs.commons.dao.dynamic.meta.xml;

import java.util.List;

import com.fs.commons.dao.dynamic.constraints.Constraint;
import com.fs.commons.dao.dynamic.constraints.DataRangeConstraint;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.IdFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.jk.logging.JKLogger;

public class TableMetaXMLGenerator {
	/**
	 *
	 */
	public TableMetaXMLGenerator() {
	}

	/**
	 *
	 * @param constraint
	 * @return
	 */
	private Object buildConstraint(final Constraint constraint) {
		final StringBuffer buf = new StringBuffer();
		buf.append("<constraint ");
		buf.append("name='" + constraint.getName() + "' type='" + constraint.getTypeString() + "' ");
		if (constraint instanceof DataRangeConstraint) {
			buf.append(" value-from='" + ((DataRangeConstraint) constraint).getValueFrom() + "' ");
			buf.append(" value-to='" + ((DataRangeConstraint) constraint).getValueTo() + "' ");
		}
		buf.append(" >");
		for (int i = 0; i < constraint.getFields().size(); i++) {
			// TODO : check the following condition
			if (constraint.getFields().get(i) != null) {
				buf.append("<field name='" + constraint.getFields().get(i).getName() + "' />");
			}
		}
		buf.append("</constraint>");
		return buf;
	}

	/**
	 *
	 * @param fieldMeta
	 * @param buf
	 */
	private void buildFieldsAttributes(final FieldMeta fieldMeta, final StringBuffer buf) {
		buf.append(" name='" + fieldMeta.getName() + "' ");
		if (fieldMeta.getType() != FieldMeta.FIELD_TYPE) {
			buf.append(" type='" + fieldMeta.getType() + "' ");
		}
		if (fieldMeta.getMaxLength() != FieldMeta.MAX_LENGHT) {
			buf.append(" max-length='" + fieldMeta.getMaxLength() + "' ");
		}
		if (fieldMeta.isRequired() != FieldMeta.REQUIRED) {
			buf.append(" required='" + fieldMeta.isRequired() + "' ");
		}
		if (fieldMeta.isAllowUpdate() != FieldMeta.ALLOW_UPDATE) {
			buf.append(" allow-update='" + fieldMeta.isAllowUpdate() + "' ");
		}
		if (fieldMeta.isVisible() != FieldMeta.VISIBLE) {
			buf.append(" visible='" + fieldMeta.isVisible() + "' ");
		}
		if (fieldMeta.isEnabled() != FieldMeta.ENABLED) {
			buf.append(" enabled='" + fieldMeta.isEnabled() + "'");
		}
		if (fieldMeta.getDefaultValue() != null && !fieldMeta.getDefaultValue().equals("")) {
			buf.append(" default-value='" + fieldMeta.getDefaultValue() + "'");
		}
		if (fieldMeta.isSummaryField()) {
			buf.append(" summary-field='" + fieldMeta.isSummaryField() + "'");
		}
	}

	/**
	 * @param fieldMeta
	 * @return
	 */
	private String buildFieldXml(final FieldMeta fieldMeta) {
		if (fieldMeta instanceof ForiegnKeyFieldMeta) {
			return buildForiegnKeyFieldXML((ForiegnKeyFieldMeta) fieldMeta);
		}
		final StringBuffer buf = new StringBuffer();
		buf.append("<field ");
		buildFieldsAttributes(fieldMeta, buf);
		buf.append("/>\n");
		return buf.toString();
	}

	/**
	 *
	 * @param fieldMeta
	 * @return
	 */
	private String buildForiegnKeyFieldXML(final ForiegnKeyFieldMeta fieldMeta) {
		final StringBuffer buf = new StringBuffer();
		buf.append("<field ");
		buildFieldsAttributes(fieldMeta, buf);
		buf.append(" reference_table='" + fieldMeta.getReferenceTable() + "' ");
		buf.append(" reference_field='" + fieldMeta.getReferenceField() + "' ");
		if (fieldMeta.getViewMode() != ForiegnKeyFieldMeta.DEFAULT_VIEW_MODE) {
			buf.append(" view-mode='" + fieldMeta.getViewMode() + "' ");
		}
		if (fieldMeta.getRelation() != ForiegnKeyFieldMeta.DEFAULT_RELATION) {
			buf.append(" relation='" + fieldMeta.getRelation() + "' ");
		}
		buf.append("/>\n");
		return buf.toString();
	}

	/**
	 *
	 * @param tableMeta
	 * @return
	 */
	private String buildIdFieldXML(final TableMeta tableMeta) {
		final StringBuffer buf = new StringBuffer();
		final IdFieldMeta id = tableMeta.getIdField();
		if (id != null) {
			buf.append("<id-field ");
			if (id.isAutoIncrement() != IdFieldMeta.DEFAULT_AUTO_INCREMENT) {
				buf.append(" auto-increment='" + id.isAutoIncrement() + "' ");
			}
			buildFieldsAttributes(id, buf);
			buf.append("/>\n");
		}
		return buf.toString();
	}

	/**
	 *
	 * @param trigger
	 * @return
	 */
	private String buildTrigger(final String triggerName) {
		final StringBuffer buf = new StringBuffer();
		buf.append("<trigger>");
		buf.append(triggerName);
		buf.append("</trigger>");
		return buf.toString();
	}

	/**
	 *
	 * @param tableMeta
	 * @return
	 */
	private String generateTableMetaXML(final TableMeta tableMeta) {
		JKLogger.info("Generating tableMeta for table : " + tableMeta.getTableName());
		final StringBuffer buf = new StringBuffer();
		buf.append("<table ");
		buf.append("name='" + tableMeta.getTableName() + "' ");
		buf.append("icon-image='" + tableMeta.getTableName() + ".png' ");
		if (!tableMeta.isTableIdNull()) {
			buf.append(" id='" + tableMeta.getTableId() + "' ");
		}
		if (tableMeta.getMaxRecordsCount() != 0) {
			buf.append(" max-records_count='" + tableMeta.getMaxRecordsCount() + "' ");
		}
		if (tableMeta.isAllowManage() != TableMeta.ALLOW_MANAGE) {
			buf.append(" allow-manage='" + tableMeta.isAllowManage() + "' ");
		}
		if (tableMeta.getIconName() != null) {
			buf.append(" icon-image='" + tableMeta.getIconName() + "'");
		}
		if (tableMeta.getPageRowCount() != TableMeta.PAGE_ROW_COUNT) {
			buf.append(" page-row-count='" + tableMeta.getPageRowCount() + "' ");
		}
		if (!tableMeta.isCaptionNull()) {
			buf.append(" caption='" + tableMeta.getCaption() + "'");
		}
		if (tableMeta.getFilters().length > 0) {
			buf.append(" filter-indices='" + tableMeta.getFiltersAsString() + "'");
		}
		if (tableMeta.getPanelClassName() != null && !tableMeta.getPanelClassName().equals("")) {
			buf.append(" panel-class='" + tableMeta.getPanelClassName() + "'");
		}
		if (!tableMeta.isAllowAdd()) {
			buf.append(" allow-add='" + tableMeta.isAllowAdd() + "' ");
		}
		if (!tableMeta.isAllowDelete()) {
			buf.append(" allow-delete='" + tableMeta.isAllowDelete() + "' ");
		}
		if (!tableMeta.isAllowUpdate()) {
			buf.append(" allow-update='" + tableMeta.isAllowUpdate() + "' ");
		}
		if (tableMeta.getDefaultUIRowCount() != TableMeta.UI_COLUMN_COUNT) {
			buf.append(" ui-colunm-count='" + tableMeta.getDefaultUIRowCount() + "' ");
		}

		buf.append(">\n");
		buf.append(buildIdFieldXML(tableMeta));
		for (int i = 0; i < tableMeta.getFieldList().size(); i++) {
			buf.append(buildFieldXml(tableMeta.getFieldList().get(i)));
		}
		if (tableMeta.getConstraints().size() > 0) {
			buf.append("<constraints>\n");
			for (int i = 0; i < tableMeta.getConstraints().size(); i++) {
				buf.append(buildConstraint(tableMeta.getConstraints().get(i)));
			}
			buf.append("</constraints>\n");
		}
		if (tableMeta.getTriggerNames().size() > 0) {
			buf.append("<triggers>\n");
			for (final String triggerName : tableMeta.getTriggerNames()) {
				buf.append(buildTrigger(triggerName) + "\n");
			}
			buf.append("</triggers>\n");
		}
		if (!tableMeta.isReportSqlNull()) {
			buf.append("<report-sql>\n");
			buf.append(tableMeta.getReportSql() + "\n");
			buf.append("</report-sql>\n");
		}
		if (!tableMeta.isListSqlNull()) {
			buf.append("<list-sql>\n");
			buf.append(tableMeta.getListSql() + "\n");
			buf.append("</list-sql>\n");
		}
		if (!tableMeta.isShortSqlNull()) {
			buf.append("<short-sql>\n");
			buf.append(tableMeta.getShortReportSql() + "\n");
			buf.append("</short-sql>\n");
		}
		buf.append("</table>\n");
		return buf.toString();
	}

	/**
	 *
	 * @param tables
	 * @return
	 */
	public String generateTablesMetaXml(final List<TableMeta> tables) {
		final StringBuffer buf = new StringBuffer();
		buf.append("<tables>\n");
		for (int i = 0; i < tables.size(); i++) {
			final TableMeta tableMeta = tables.get(i);
			buf.append("<!-- ///////////////////////////////////////////////////////////////////////////// -->\n");
			buf.append("<!-- Table Meta for : -" + tableMeta.getTableName() + "-with id : -" + tableMeta.getTableId() + "- -->\n");
			buf.append("<!-- ///////////////////////////////////////////////////////////////////////////// -->\n");
			buf.append(generateTableMetaXML(tableMeta));
		}
		buf.append("</tables>");
		return buf.toString();
	}
}
