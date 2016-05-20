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
package com.fs.commons.desktop.swing;

import java.awt.Color;
import java.io.InputStream;
import java.util.Properties;

import com.fs.commons.util.CollectionUtil;
import com.fs.commons.util.GeneralUtility;

public class Colors {
	static Properties prop = new Properties();
	static {
		InputStream inputStream = GeneralUtility.getFileInputStream("/color.properties");
		if (inputStream != null) {
			prop = GeneralUtility.readPropertyStream(inputStream);
		} else {
			System.err.println("No theme found , default theme will be loaded");
		}
	}
	public static final Color DARK_BLUE = new Color(22, 125, 219);
	public static final Color NICE_BLUE = new Color(191, 215, 255);
	public static final Color SKY_BLUE = new Color(219, 229, 241);

	public static final Color MAIN_PANEL_BG = getColor("main_panel_bg");

	public static final Color MODULE_PANEL_BG = getColor("module_panel_bg");
	public static final Color MODULE_BUTTON_BG = getColor("module_button_bg");
	public static final Color MODULE_BUTTON_FC = getColor("module_button_fc");;
	public static final Color MODULE_SELECTED_FC = getColor("module_selected_fc");;
	public static final Color MODULE_SELECTED_BG = getColor("module_selected_bg");;
	// public static final String
	// MODULE_PANEL_BG_IMAGE=getImage("module_panel_bg_image");

	public static final Color MENU_PANEL_BG = getColor("menu_panel_bg");
	public static final Color MENU_BUTTON_BG = getColor("menu_button_bg");
	public static final Color MENU_BUTTON_FC = getColor("menu_button_fc");;

	public static final Color MI_PANEL_BG = getColor("mi_panel_bg");
	public static final Color MI_BUTTON_BG = getColor("mi_button_bg");
	public static final Color MI_BUTTON_FC = getColor("mi_button_fc");;

	public static final Color JK_TITLE_BAR_BG = getColor("title_bar_bg");
	public static final Color JK_TITLE_BAR_FG = getColor("title_bar_fg");

	public static final Color JK_LABEL_BG = getColor("label_bg");
	public static final Color JK_LABEL_FG = getColor("label_fg");

	public static final Color JK_BUTTON_BG = getColor("button_bg");
	public static final Color JK_BUTTON_FG = getColor("button_fg");

	public static final Color JK_TITLE_BG = getColor("title_bg");
	public static final Color JK_TITLE_FG = getColor("title_fg");
	public static final Color TITLE_BORDER_BG = getColor("title_border_bg");

	public static final Color TABLE_EVEN_ROW = getColor("table_even_row");
	public static final Color TABLE_ODD_ROW = getColor("table_odd_row");
	public static final Color FAVORITE_ITEM_BORDER = new Color(173, 20, 0);
	public static final Color CELL_EDITOR_BG = MAIN_PANEL_BG;
	public static final Color CELL_EDTIOR_FG = Color.black;
	public static final Color JK_STATUS_BAR_BG = getColor("status_bg");
	public static final Color FAVORITE_BG = getColor("favorite_bg");
	public static final Color JK_STATUS_BAR_FG = getColor("status_bg");
	public static final Color MI_PANEL_TITLE_BG = getColor("mi-panel-title-bg");
	public static final Color MI_PANEL_TITLE_FG = getColor("mi-panel-title-fg");

	// public static final Color MENU_PANEL= SKY_BLUE;
	// public static final Color MENU_ITEMS_PANEL= NICE_BLUE;

	/**
	 *
	 * @return
	 */
	protected static Color getColor(final String colorStr) {
		return getColor(colorStr, null);
	}

	private static Color getColor(String colorStr, final Color defaultColor) {
		colorStr = CollectionUtil.fixPropertyKey(colorStr);
		if (prop.getProperty(colorStr) == null) {
			System.out.println("Color  " + colorStr + " Is not avaiable ");
			return defaultColor;
		}
		final String[] color = prop.getProperty(colorStr).split(",");
		String colorHex = color[0];
		if (color.length != 3) {
			return SwingUtility.hexToColor(colorHex);
		}
		return new Color(Integer.parseInt(colorHex), Integer.parseInt(color[1]), Integer.parseInt(color[2]));

	}


	public static void main(final String[] args) {
		System.out.println();
	}

}
