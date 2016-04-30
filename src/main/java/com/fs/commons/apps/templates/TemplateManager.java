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

	public static String compile(int templateId, Object[] ids) throws RecordNotFoundException, DaoException {
		TemplateFacade facade = new TemplateFacade();
		Template template = facade.findTemplate(templateId);
		return (String) compile(template, ids);
	}

	/**
	 * 
	 * @param template
	 * @param queryIds
	 *            []
	 * @return
	 * @throws DaoException
	 */
	public static String compile(Template template, Object[] queryIds) throws DaoException {
		String output = template.getTempText();
		System.out.println("Compling Template : " + template.getTempName());
		System.out.println(template.getTempText());
		ArrayList<TemplateVariable> variables = template.getVariables();
		for (int i = 0; i < variables.size(); i++) {
			TemplateVariable templateVariable = variables.get(i);
			output = output.replaceFirst("\\?", fetchVariable(templateVariable.getVar(), queryIds[i]).toString());
		}
		return output;
	}

	/**
	 * 
	 * @param template
	 * @param entityParamaters
	 * @return
	 * @throws DaoException
	 */
	public static String[] compileTemplateGroup(Template template,String templateParamtersQuery) throws DaoException{
		System.out.println("Compling group by paramters query : \n"+templateParamtersQuery);
		return compileTemplateGroup(template, DaoUtil.executeQueryAsArray(templateParamtersQuery)) ;
		
	}
	
	/**
	 * @return
	 * @throws DaoException 
	 */
	public static String[] compileTemplateGroup(Template template,Object[] entityParamaters) throws DaoException{
		String[] compiledTemplates=new String[entityParamaters.length];
		for (int i=0;i<entityParamaters.length;i++) {
			Object[] queryIds=(Object[])entityParamaters[i];
		 	System.out.println(Arrays.toString(queryIds));
			compiledTemplates[i]=(compile(template, queryIds));
		}
		return compiledTemplates;
	}
	
	/**
	 * @param var
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	private static Object fetchVariable(Variable var, Object id) throws DaoException {
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
		Object outputQuery = DaoUtil.exeuteOutputQuery(queryText);
		System.out.println("Fetched value : " + outputQuery);
		return outputQuery;
	}

	/**
	 * 
	 * @param variable
	 * @param id
	 * @return
	 */
	private static String buildVariableFetchQuery(Variable variable, Object id) {
		Query query = new Query();

		TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta(variable.getTableName());
		FieldMeta variabeFieldMeta = variable.toFieldMeta();

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

}
