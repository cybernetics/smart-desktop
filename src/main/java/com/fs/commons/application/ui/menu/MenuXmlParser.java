//package com.fs.commons.application.ui.menu;
//
//import java.io.InputStream;
//import java.util.Properties;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
//import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;
//
//public class MenuXmlParser {
//
//	/**
//	 * 
//	 * @param in
//	 * @return
//	 * @throws JKXmlException
//	 */
//	public MainMenu parse(InputStream in) throws JKXmlException {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder;
//		try {
//			builder = factory.newDocumentBuilder();
//			Document doc = builder.parse(in);
//			MainMenu mainMenu = new MainMenu();
//			NodeList sectionNode = doc.getElementsByTagName("section");
//			for (int i = 0; i < sectionNode.getLength(); i++) {
//				Element sectionElement = (Element) sectionNode.item(i);
//				MenuSection instance = MenuSection.class.newInstance();
//				instance.setName(sectionElement.getAttribute("name"));
//				if (!sectionElement.getAttribute("icon-name").equals("")) {
//					instance.setIconName(sectionElement.getAttribute("icon-name"));
//				}
//				if (!sectionElement.getAttribute("privlige-id").equals("")) {
//					instance.setPrivligeId(Integer.parseInt(sectionElement.getAttribute("privlige-id")));
//				}
//				NodeList menusNodes = sectionElement.getChildNodes();
//				for (int j = 0; j < menusNodes.getLength(); j++) {
//					Node node = menusNodes.item(j);
//					if (node instanceof Element) {
//						Element e = (Element) node;
//						if (e.getNodeName().equals("menu")) {
//							Menu menu = parseMenu(e);
//							instance.add(menu);
//						}
//						if (e.getNodeName().equals("menu-item")) {
//							MenuItem item = parseMenuItem(e);
//							instance.getMenuItems().add(item);
//						}
//					}
//				}
//				mainMenu.add(instance);
//			}
//			return mainMenu;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new JKXmlException(e);
//		}
//
//	}
//
//	/**
//	 * 
//	 * @param e
//	 * @return
//	 * @throws IllegalAccessException
//	 * @throws InstantiationException
//	 * @throws TableMetaNotFoundException
//	 */
//	private Menu parseMenu(Element e1) throws InstantiationException, IllegalAccessException {
//		Menu menu = Menu.class.newInstance();
//		menu.setName(e1.getAttribute("name"));
//		if (!e1.getAttribute("icon-name").equals("")) {
//			menu.setIconName(e1.getAttribute("icon-name"));
//		}
//		if (!e1.getAttribute("privlige-id").equals("")) {
//			menu.setPriviligeId(Integer.parseInt(e1.getAttribute("privlige-id")));
//		}
//		NodeList list = e1.getChildNodes();
//		for (int i = 0; i < list.getLength(); i++) {
//			Node node = list.item(i);
//			if (node instanceof Element) {
//				Element element = (Element) node;
//				if (element.getNodeName().equals("menu-item")) {
//					MenuItem item = parseMenuItem(element);
//					menu.add(item);
//				}
//				if (element.getNodeName().equals("group")) {
//					Menu group=parseMenu(element);
//					menu.addGroup(group);
//				}
//				
//			}
//		}
//		return menu;
//	}
//
//	/**
//	 * 
//	 * @param e
//	 * @return
//	 * @throws InstantiationException
//	 * @throws IllegalAccessException
//	 * @throws TableMetaNotFoundException
//	 */
//	private MenuItem parseMenuItem(Element e) throws InstantiationException, IllegalAccessException {
//		MenuItem item = MenuItem.class.newInstance();
//		item.setName(e.getAttribute("name"));
//		if (!e.getAttribute("icon-name").equals("")) {
//			item.setIconName(e.getAttribute("icon-name"));
//		}
//		if (!e.getAttribute("privlige-id").equals("")) {
//			item.setPriviligeId(Integer.parseInt(e.getAttribute("privlige-id")));
//		}
//		if (!e.getAttribute("cache").equals("")) {
//			item.setCacheAtStartup(Boolean.parseBoolean(e.getAttribute("cache")));
//		}
//
//		NodeList nodeList = e.getChildNodes();
//		for (int i = 0; i < nodeList.getLength(); i++) {
//			Node node = nodeList.item(i);
//			if (node.getNodeName().equals("properties")) {
//				item.setProperties(parseProperties(node));
//			}
//		}
//		return item;
//	}
//
//	/**
//	 * @param node
//	 * @return
//	 */
//	private Properties parseProperties(Node node) {
//		Properties properties = new Properties();
//		NodeList propList = node.getChildNodes();
//		for (int i = 0; i < propList.getLength(); i++) {
//			if (propList.item(i).getNodeName().equals("property")) {
//				Element e = (Element) propList.item(i);
//				String propertyName = e.getAttribute("name");
//				if (!e.getAttribute("value").equals("")) {
//					// add simple record
//					properties.setProperty(propertyName, e.getAttribute("value"));
//				} else {
//					properties.put(propertyName, parseProperties(e));
//				}
//			}
//		}
//		return properties;
//	}
//}
