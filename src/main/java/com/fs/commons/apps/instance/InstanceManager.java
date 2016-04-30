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
package com.fs.commons.apps.instance;

import java.io.IOException;
import java.net.ServerSocket;

import com.fs.commons.util.GeneralUtility;

public class InstanceManager {

	/**
	 *
	 * @param id
	 * @throws InstanceException
	 */
	public static void registerInstance(final int id) throws InstanceException {
		if (Boolean.valueOf(System.getProperty("fs.commons.singleInstance", "false"))) {
			try {
				final ServerSocket server = new ServerSocket(id);
				GeneralUtility.startFakeThread(server);
				// server.accept();
			} catch (final IOException e) {
				throw new InstanceException(e);
			}
		}
	}

	/**
	 *
	 */
	public InstanceManager() {
	}
}
