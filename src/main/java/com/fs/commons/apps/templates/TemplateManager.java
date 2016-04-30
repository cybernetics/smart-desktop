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
package com.fs.commons.apps.templates;

import java.util.ArrayList;
import java.util.Arrays;

import com.fs.commons.apps.templates.beans.Template;
import com.fs.commons.apps.templates.beans.TemplateVariable;
import com.fs.commons.apps.templates.beans.Variable;
import com.fs.commons.apps.templates.facade.TemplateFacade;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.dao.sql.query.FieldCondition;
import com.fs.commons.dao.sql.query.Keyword;
import com.fs.commons.dao.sql.query.Query;

public class TemplateManager {

	/**
	 *
	 * @param variable
	 * @param id
	 * @return
	 */
	private static String buildVariableFetchQuery(final Variable variable, final Object id) {
		final Query query = new Query();

		final TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta(variable.getTableName());
		final FieldMeta variabeFieldMeta = variable.toFieldMeta();

		query.addComponent(Keyword.SELECT);
		query.addComponent(variabeFieldMeta);
		query.addComponent(Keyword.FROM);
		query.addComponent(tableMeta);
		if (id != null && !id.toString().trim().equals("")) {
			query.addComponent(Keyword.WHERE);
			query.addComponent(new FieldCondition(tableMeta.getIdField(), id));
		}

		return query.compile();
	}

	public static String compile(final int templateId, final Object[] ids) throws RecordNotFoundException, DaoException {
		final TemplateFacade facade = new TemplateFacade();
		final Template template = facade.findTemplate(templateId);
		return compile(template, ids);
	}

	/**
	 *
	 * @param template
	 * @param queryIds
	 *            []
	 * @return
	 * @throws DaoException
	 */
	public static String compile(final Template template, final Object[] queryIds) throws DaoException {
		String output = template.getTempText();
		System.out.println("Compling Template : " + template.getTempName());
		System.out.println(template.getTempText());
		final ArrayList<TemplateVariable> variables = template.getVariables();
		for (int i = 0; i < variables.size(); i++) {
			final TemplateVariable templateVariable = variables.get(i);
			output = output.replaceFirst("\\?", fetchVariable(templateVariable.getVar(), queryIds[i]).toString());
		}
		return output;
	}

	/**
	 * @return
	 * @throws DaoException
	 */
	public static String[] compileTemplateGroup(final Template template, final Object[] entityParamaters) throws DaoException {
		final String[] compiledTemplates = new String[entityParamaters.length];
		for (int i = 0; i < entityParamaters.length; i++) {
			final Object[] queryIds = (Object[]) entityParamaters[i];
			System.out.println(Arrays.toString(queryIds));
			compiledTemplates[i] = compile(template, queryIds);
		}
		return compiledTemplates;
	}

	/**
	 *
	 * @param template
	 * @param entityParamaters
	 * @return
	 * @throws DaoException
	 */
	public static String[] compileTemplateGroup(final Template template, final String templateParamtersQuery) throws DaoException {
		System.out.println("Compling group by paramters query : \n" + templateParamtersQuery);
		return compileTemplateGroup(template, DaoUtil.executeQueryAsArray(templateParamtersQuery));

	}

	/**
	 * @param var
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	private static Object fetchVariable(final Variable var, final Object id) throws DaoException {
		String queryText;
		if (var.getQuery() == null) {
			queryText = buildVariableFetchQuery(var, id);
		} else {
			queryText = var.getQuery().getQueryText();
			if (id != null && !id.toString().trim().equals("")) {
				queryText = queryText.replaceFirst("\\?", id.toString());
			}
		}
		System.out.println("Fetching variable : " + var.getVarName());
		System.out.println(queryText);
		final Object outputQuery = DaoUtil.exeuteOutputQuery(queryText);
		System.out.println("Fetched value : " + outputQuery);
		return outputQuery;
	}

}
