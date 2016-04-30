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
package com.fs.commons.application.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fs.commons.application.Module;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.application.ui.menu.MenuItem;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;

public class ModuleMenuXmlParser {

	private final Module parentModule;

	public ModuleMenuXmlParser(final Module parentModule) {
		this.parentModule = parentModule;
	}

	/**
	 * @param in
	 * @return
	 * @throws JKXmlException
	 */
	public ArrayList<Menu> parse(final InputStream in) throws JKXmlException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(in);
			final ArrayList<Menu> menus = new ArrayList<Menu>();
			final NodeList menuNodes = doc.getElementsByTagName("menu");
			for (int i = 0; i < menuNodes.getLength(); i++) {
				final Node node = menuNodes.item(i);
				if (node instanceof Element) {
					final Element e = (Element) node;
					final Menu menu = parseMenu(e);
					menu.setParentModule(this.parentModule);
					menu.init();
					menus.add(menu);
				}
			}
			return menus;
		} catch (final Exception e) {
			throw new JKXmlException(e);
		}
	}

	/**
	 * @param e
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws TableMetaNotFoundException
	 */
	private Menu parseMenu(final Element e1) throws InstantiationException, IllegalAccessException {
		final Menu menu = Menu.class.newInstance();
		menu.setName(e1.getAttribute("name").trim());
		if (!e1.getAttribute("icon-name").trim().equals("")) {
			menu.setIconName(e1.getAttribute("icon-name").trim());
		}
		// if (!e1.getAttribute("privlige-id").trim().equals("")) {
		// menu.setPriviligeId(Integer.parseInt(e1.getAttribute("privlige-id").trim()));
		// }
		final NodeList list = e1.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			final Node node = list.item(i);
			if (node instanceof Element) {
				final Element element = (Element) node;
				if (element.getNodeName().trim().equals("menu-item")) {
					final MenuItem item = parseMenuItem(element);
					item.setParentMenu(menu);
					menu.add(item);
				}
			}
		}
		return menu;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	private MenuItem parseMenuItem(final Element e) throws InstantiationException, IllegalAccessException {
		final MenuItem item = MenuItem.class.newInstance();
		item.setName(e.getAttribute("name").trim());
		if (!e.getAttribute("icon-name").trim().equals("")) {
			item.setIconName(e.getAttribute("icon-name"));
		}
		// if (!e.getAttribute("privlige-id").trim().equals("")) {
		// item.setPriviligeId(Integer.parseInt(e.getAttribute("privlige-id").trim()));
		// }
		if (!e.getAttribute("cache").trim().equals("")) {
			item.setCachePanel(Boolean.parseBoolean(e.getAttribute("cache").trim()));
		}

		final NodeList nodeList = e.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Node node = nodeList.item(i);
			if (node.getNodeName().equals("properties")) {
				item.setProperties(parseProperties(node));
			}
		}
		return item;
	}

	/**
	 * @param node
	 * @return
	 */
	private Properties parseProperties(final Node node) {
		final Properties properties = new Properties();
		final NodeList propList = node.getChildNodes();
		for (int i = 0; i < propList.getLength(); i++) {
			if (propList.item(i).getNodeName().equals("property")) {
				final Element e = (Element) propList.item(i);
				final String propertyName = e.getAttribute("name").trim();
				if (!e.getAttribute("value").trim().equals("")) {
					// add simple record
					properties.setProperty(propertyName, e.getAttribute("value").trim());
				} else {
					properties.put(propertyName, parseProperties(e));
				}
			}
		}
		return properties;
	}
}
