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
package com.fs.commons.desktop.validation.ui;

/**
 * Optional interface which can be implemented on a component to indicate that
 * it supplies validation.
 * <p/>
 * Useful for composing together panels that have validation groups - for
 * example, if a panel implementing this interface is added to a
 * ValidationPanel, then its ValidationGroup will tranparently be merged into
 * that of the ValidationPanel.
 *
 * @author Tim Boudreau
 */
public interface ValidationGroupProvider {
	public static final String CLIENT_PROP_VALIDATION_GROUP = "_vg"; // NOI18N

	public ValidationGroup getValidationGroup();
}
