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

	public ModuleMenuXmlParser(Module parentModule) {
		this.parentModule = parentModule;
	}

	/**
	 * @param in
	 * @return
	 * @throws JKXmlException
	 */
	public ArrayList<Menu> parse(InputStream in) throws JKXmlException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);
			ArrayList<Menu> menus=new ArrayList<Menu>();
			NodeList menuNodes = doc.getElementsByTagName("menu");
			for (int i = 0; i < menuNodes.getLength(); i++) {
				Node node = menuNodes.item(i);					
				if (node instanceof Element) {
					Element e = (Element) node;
					Menu menu = parseMenu(e);					
					menu.setParentModule(parentModule);
					menu.init();
					menus.add(menu);
				}				
			}
			return menus;
		} catch (Exception e) {
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
	private Menu parseMenu(Element e1) throws InstantiationException, IllegalAccessException {
		Menu menu = Menu.class.newInstance();
		menu.setName(e1.getAttribute("name").trim());
		if (!e1.getAttribute("icon-name").trim().equals("")) {
			menu.setIconName(e1.getAttribute("icon-name").trim());
		}
//		if (!e1.getAttribute("privlige-id").trim().equals("")) {
//			menu.setPriviligeId(Integer.parseInt(e1.getAttribute("privlige-id").trim()));
//		}
		NodeList list = e1.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				if (element.getNodeName().trim().equals("menu-item")) {
					MenuItem item = parseMenuItem(element);
					item.setParentMenu(menu);
					menu.add(item);
				}
			}
		}
		return menu;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	private MenuItem parseMenuItem(Element e) throws InstantiationException, IllegalAccessException {
		MenuItem item = MenuItem.class.newInstance();
		item.setName(e.getAttribute("name").trim());
		if (!e.getAttribute("icon-name").trim().equals("")) {
			item.setIconName(e.getAttribute("icon-name"));
		}
//		if (!e.getAttribute("privlige-id").trim().equals("")) {
//			item.setPriviligeId(Integer.parseInt(e.getAttribute("privlige-id").trim()));
//		}
		if (!e.getAttribute("cache").trim().equals("")) {
			item.setCachePanel(Boolean.parseBoolean(e.getAttribute("cache").trim()));
		}

		NodeList nodeList = e.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
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
	private Properties parseProperties(Node node) {
		Properties properties = new Properties();
		NodeList propList = node.getChildNodes();
		for (int i = 0; i < propList.getLength(); i++) {
			if (propList.item(i).getNodeName().equals("property")) {
				Element e = (Element) propList.item(i);
				String propertyName = e.getAttribute("name").trim();
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
