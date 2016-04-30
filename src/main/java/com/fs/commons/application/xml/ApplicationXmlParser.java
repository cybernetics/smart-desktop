package com.fs.commons.application.xml;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fs.commons.application.Application;
import com.fs.commons.application.DefaultModule;
import com.fs.commons.application.Module;
import com.fs.commons.application.listener.ApplicationListener;
import com.fs.commons.dao.connection.PoolingDataSource;
import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;
import com.fs.commons.locale.LablesLoader;
import com.fs.commons.util.ReflicationUtil;

public class ApplicationXmlParser {
	
	/**
	 * 
	 * @param in
	 * @return
	 * @throws JKXmlException 
	 */
	public Application parseApplication(InputStream in) throws JKXmlException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);
			Node root = doc.getFirstChild();
			NodeList list = root.getChildNodes();
			Application application=new Application();
			ArrayList<Module> modules=new ArrayList<Module>();
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				String text = node.getTextContent().trim();
				if (node.getNodeName().equals("name")) {
					application.setApplicationName(text);
				}else if (node.getNodeName().equals("id")) {
					application.setApplicationId(Integer.parseInt(text));					
				}else if (node.getNodeName().equals("config-file")) {
					application.setConfigFileName(text);
				}else if (node.getNodeName().equals("splash-img")) {
					application.setSplashImage(text);
//				}else if (node.getNodeName().equals("date-format")) {
//					JKDate.setDefaultDateFormat(text);
				}else if (node.getNodeName().equals("view-modules")) {
					application.setViewModules(Boolean.parseBoolean(text));
				}else if (node.getNodeName().equals("home-img")) {					
					application.setHomeImage(text);
				}else if (node.getNodeName().equals("locale")) {
					application.setLocale(text,true);					
				}else if (node.getNodeName().equals("auto-logout-interval")) {
					application.setAutoLogoutInterval(text);
				} else if (node.getNodeName().equals("module")) {
					Module module = parseModule(list.item(i));
					module.setApplication(application);
					modules.add(module);
				} else if (node.getNodeName().equals("listener")) {
					ApplicationListener listener = parseListener(list.item(i));
					application.addListener(listener);
				}
			}
			application.setModules(modules);
			return application;
		} catch (Exception e) {
			throw new JKXmlException(e);
		}
		
	}


	/**
	 * 
	 * @param item
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private ApplicationListener parseListener(Node item) throws Exception {
		Element e = (Element) item;
		ApplicationListener listener=null;
		if (!e.getAttribute("class").equals("")) {
			String className=e.getAttribute("class");
			ReflicationUtil<ApplicationListener> reflicationUtil = new ReflicationUtil<ApplicationListener>();
			listener = reflicationUtil.getInstance(className, ApplicationListener.class);
		}
		return listener;
	}

	/**
	 * 
	 * @param item
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IOException 
	 */
	protected Module parseModule(Node item) throws Exception {
		Element e = (Element) item;
		Module module=null;
		if (!e.getAttribute("class").equals("")) {
			String className=e.getAttribute("class");
			module=(Module)Class.forName(className).newInstance();
		}else if(!e.getAttribute("config-path").equals("")){
			DefaultModule defaultModule = new DefaultModule();
			defaultModule.setConfigPath(e.getAttribute("config-path"));
			module=defaultModule;
		}
		if(!e.getAttribute("id").equals("")){
			module.setModuleId(Integer.parseInt(e.getAttribute("id")));
		}
		if(!e.getAttribute("default").trim().equals("")){
			module.setDefault(Boolean.parseBoolean(e.getAttribute("default")));
		}
		if(!e.getAttribute("datasource-config").trim().equals("")){
			module.setDataSource(new PoolingDataSource(e.getAttribute("datasource-config")));
		}
		if(!e.getAttribute("lables-loader").trim().equals("")){
			module.setLablesLoader(parseLablesLoader(e.getAttribute("lables-loader")));
		}				
		module.setModuleName(e.getAttribute("name"));
				
		NodeList list = item.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("paramters")) {
			}
		}
		
		return module;
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	private LablesLoader parseLablesLoader(String lablesLoaderClass) throws Exception {
		ReflicationUtil<LablesLoader> reflicationUtil = new ReflicationUtil<LablesLoader>();
		return reflicationUtil.getInstance(lablesLoaderClass, LablesLoader.class);
	}
}
