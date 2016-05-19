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
package com.fs.commons.desktop.dynform.ui;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.desktop.dynform.ui.masterdetail.AbstractMasterDetail;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailPanel;

public class MasterPanelFactory {
	/**
	 *
	 * @param panelClassName
	 * @return
	 * @throws UIOPanelCreationException
	 */
	private static Object createDynamicPanel(final String panelClassName) throws UIOPanelCreationException {
		try {
			return Class.forName(panelClassName).newInstance();
		} catch (final Exception e) {
			throw new UIOPanelCreationException(e);
		}
	}

	/**
	 *
	 * @param meta
	 * @throws JKDataAccessException
	 * @throws TableMetaNotFoundException
	 * @throws UIOPanelCreationException
	 */
	public static AbstractMasterDetail createMasterPanel(final TableMeta meta)
			throws TableMetaNotFoundException, JKDataAccessException, UIOPanelCreationException {
		if (meta.getPanelClassName() != null && !meta.getPanelClassName().equals("")) {
			final Object panel = createDynamicPanel(meta.getPanelClassName());
			if (panel instanceof AbstractMasterDetail) {
				return (AbstractMasterDetail) panel;
			}
		}
		final DynMasterDetailPanel masterDetailPanel = new DynMasterDetailPanel(meta);
		masterDetailPanel.init();
		return masterDetailPanel;
	}
}
