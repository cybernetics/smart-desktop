package com.fs.commons.apps.templates.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fs.commons.apps.templates.beans.Query;
import com.fs.commons.apps.templates.beans.QueryType;
import com.fs.commons.apps.templates.beans.Template;
import com.fs.commons.apps.templates.beans.TemplateVariable;
import com.fs.commons.apps.templates.beans.Variable;
import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.DaoFinder;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;

public class TemplateDao extends AbstractDao{
	////////////////////////////////////////////////////////////////////////////////////
	public Variable findVariable(final int varId) throws RecordNotFoundException, DaoException{
		DaoFinder finder = new DaoFinder() {
			
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1,varId);
				
			}
			
			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				Variable variable = new Variable();
				variable.setVarId(rs.getInt("var_id"));
				variable.setVarName(rs.getString("var_name"));
				variable.setTableName(rs.getString("table_name"));
				variable.setFieldName(rs.getString("field_name"));
				if(rs.getInt("query_id")!=0){
					variable.setQuery(findQuery(rs.getInt("query_id")));
				}
				return variable;
			}
			
			@Override
			public String getFinderSql() {
				return "SELECT * FROM conf_vars WHERE var_id=?";
			}
		};
		  return (Variable) findRecord(finder);
	}

	////////////////////////////////////////////////////////////////////////////////////
	public Query findQuery(final int queryId) throws RecordNotFoundException, DaoException {
		DaoFinder finder=new DaoFinder() {
			
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, queryId);
			}
			
			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				Query query=new Query();
				query.setQueryId(rs.getInt("query_id"));
				query.setDesc(rs.getString("desc"));
				query.setQueryType(findQueryType(rs.getInt("query_type_id")));
				query.setQueryText(rs.getString("query_text"));
				return query;
			}
			
			@Override
			public String getFinderSql() {
				return "SELECT * FROM conf_queries WHERE query_id=?";
			}
		};
		return (Query) findRecord(finder);
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	protected QueryType findQueryType(final int typeId) throws RecordNotFoundException, DaoException {
		DaoFinder finder=new DaoFinder() {
			
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, typeId);
			}
			
			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				QueryType type=new QueryType();
				type.setId(rs.getInt(1));
				type.setName(rs.getString(2));
				return type;
			}
			
			@Override
			public String getFinderSql() {				
				return "SELECT  * FROM conf_query_types WHERE query_type_id=?";
			}
		};
		return (QueryType) findRecord(finder);
	}

	// ///////////////////////////////////////////////////////////////
	public Template findTemplate(final int tempId) throws RecordNotFoundException, DaoException{
		DaoFinder finder = new DaoFinder() {
			
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, tempId);			
			}
			
			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				Template template = new Template();
				template.setTempId(rs.getInt("template_id"));
				template.setTempName(rs.getString("template_name"));
				template.setTempTitle(rs.getString("template_title"));
				template.setTempText(rs.getString("template_text"));
				template.setVariables(lstTemplateVariables(tempId));
				return template;
			}
			
			@Override
			public String getFinderSql() {
				return "SELECT * FROM conf_templates WHERE template_id=? ";
			}
		};
		return (Template) findRecord(finder);
	}

	// ///////////////////////////////////////////////////////////////
	public ArrayList<TemplateVariable> lstTemplateVariables(final int tempId) throws RecordNotFoundException, DaoException{
		DaoFinder finder = new DaoFinder() {
			
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, tempId);
				
			}
			
			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				TemplateVariable var = new TemplateVariable();
				var.setTempVarId(rs.getInt("template_var_id"));
				//var.setTemp(findTemplate(rs.getInt("template_id")));
				var.setVarIndex(rs.getInt("var_index"));
				var.setVar(findVariable(rs.getInt("var_id")));
				return var;
			}
			
			@Override
			public String getFinderSql() {
				return "SELECT * FROM conf_template_vars WHERE template_id=? ORDER BY var_index";
			}
		};
		return lstRecords(finder);
	}
}

	