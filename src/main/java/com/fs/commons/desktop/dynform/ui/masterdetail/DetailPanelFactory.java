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
package com.fs.commons.desktop.dynform.ui.masterdetail;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.DetailOneToManyPanel;

/**
 * @author u087
 *
 */
public class DetailPanelFactory {

	/**
	 *
	 * @param foriegnKeyFieldMeta
	 * @return
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 * @throws UIOPanelCreationException
	 */
	public static DetailPanel createDetailPanel(final ForiegnKeyFieldMeta foriegnKeyFieldMeta)
			throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		DetailPanel pnl;
		final String panelClassName = foriegnKeyFieldMeta.getParentTable().getPanelClassName();
		if (panelClassName != null && !panelClassName.equals("")) {
			return createDynamicPanel(panelClassName);
		}
		if (foriegnKeyFieldMeta.getParentTable().isCrossTable()) {
			pnl = new DynCrossDaoPanel(foriegnKeyFieldMeta.getParentTable());
		} else if (foriegnKeyFieldMeta.getRelation() == ForiegnKeyFieldMeta.Relation.ONE_TO_ONE) {
			pnl = new DetailOneToOnePanel(foriegnKeyFieldMeta);
		} else {
			pnl = new DetailOneToManyPanel(foriegnKeyFieldMeta);
		}
		return pnl;
	}

	/**
	 *
	 * @param panelClassName
	 * @return
	 * @throws UIOPanelCreationException
	 */
	private static DetailPanel createDynamicPanel(final String panelClassName) throws UIOPanelCreationException {
		try {
			return (DetailPanel) Class.forName(panelClassName).newInstance();
		} catch (final Exception e) {
			throw new UIOPanelCreationException(e);
		}
	}
}
