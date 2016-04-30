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

public class ApplicationListenerAdapter implements ApplicationListener {

	/**
	 *
	 */
	@Override
	public void afterInit(final Application application) {
	}

	/**
	 *
	 */
	@Override
	public void afterStart(final Application application) {
	}

	/**
	 *
	 */
	@Override
	public void applicationLoaded(final Application application) {
	}

	/**
	 *
	 */
	@Override
	public void beforeInit(final Application application) {
	}

	/**
	 *
	 */
	@Override
	public void beforeStart(final Application application) {
	}

	/**
	 *
	 */
	@Override
	public void onException(final Application application, final Exception e) {
	}

}
