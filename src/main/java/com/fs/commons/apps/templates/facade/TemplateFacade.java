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
package com.fs.commons.apps.templates.facade;

import java.util.ArrayList;

import com.fs.commons.apps.templates.beans.Template;
import com.fs.commons.apps.templates.beans.TemplateVariable;
import com.fs.commons.apps.templates.beans.Variable;
import com.fs.commons.apps.templates.dao.TemplateDao;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;

public class TemplateFacade {

	TemplateDao templateDao = new TemplateDao();

	// ///////////////////////////////////////////////////////////////
	public Template findTemplate(final int tempId) throws JKRecordNotFoundException, JKDataAccessException {
		return this.templateDao.findTemplate(tempId);
	}

	public Variable findVariable(final int varId) throws JKRecordNotFoundException, JKDataAccessException {
		return this.templateDao.findVariable(varId);
	}

	// ///////////////////////////////////////////////////////////////
	public ArrayList<TemplateVariable> lstTemplateVariables(final int tempId) throws JKRecordNotFoundException, JKDataAccessException {
		return this.templateDao.lstTemplateVariables(tempId);
	}

}
