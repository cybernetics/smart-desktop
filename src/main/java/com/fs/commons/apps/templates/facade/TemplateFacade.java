package com.fs.commons.apps.templates.facade;

import java.util.ArrayList;

import com.fs.commons.apps.templates.beans.Template;
import com.fs.commons.apps.templates.beans.TemplateVariable;
import com.fs.commons.apps.templates.beans.Variable;
import com.fs.commons.apps.templates.dao.TemplateDao;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;

public class TemplateFacade {
	
	TemplateDao templateDao = new TemplateDao();
	
	public Variable findVariable(final int varId) throws RecordNotFoundException, DaoException{
		return templateDao.findVariable(varId);
	}
	// ///////////////////////////////////////////////////////////////
	public Template findTemplate(final int tempId) throws RecordNotFoundException, DaoException{
		return templateDao.findTemplate(tempId);
	}
	// ///////////////////////////////////////////////////////////////
	public ArrayList<TemplateVariable> lstTemplateVariables(final int tempId) throws RecordNotFoundException, DaoException{
		return templateDao.lstTemplateVariables(tempId);
	}

}
