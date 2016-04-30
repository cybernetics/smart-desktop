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
package com.fs.commons.application.listener;

import com.fs.commons.application.Application;

public interface ApplicationListener {

	/**
	 *
	 * @param application
	 */
	public void afterInit(Application application);

	/**
	 *
	 * @param application
	 */
	public void afterStart(Application application);

	/**
	 *
	 * @param application
	 */
	public void applicationLoaded(Application application);

	/**
	 *
	 * @param application
	 */
	public void beforeInit(Application application);

	/**
	 *
	 * @param application
	 */
	public void beforeStart(Application application);

	/**
	 *
	 * @param application
	 * @param e
	 */
	public void onException(Application application, Exception e);
}
