/**
 * 
 */
package com.fs.commons.reports;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;

/**
 * @author u087
 * 
 */
public class ReportXmlParser {
	String sourcePath;

	String outPath;

	ArrayList<Report> reports = new ArrayList<Report>();

	/**
	 * 
	 */
	public ReportXmlParser() {
	}

	/**
	 * @return the reports
	 */
	public ArrayList<Report> getReports() {
		return this.reports;
	}

	/**
	 * 
	 * @param in
	 * @throws JKXmlException
	 */
	public ArrayList<Report> parse(InputStream in) throws JKXmlException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);
			Node root = doc.getFirstChild();
			NodeList list = root.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeName().equals("source-path")) {
					Node node = list.item(i);
					sourcePath=(node.getTextContent().trim());
				} else if (list.item(i).getNodeName().equals("out-path")) {
					outPath=(list.item(i).getTextContent());
				} else if (list.item(i).getNodeName().equals("report")) {
					Report report = parseReport(list.item(i));
					reports.add(report);
				}
			}
			return reports;
		} catch (Exception e) {
			throw new JKXmlException(e);
		}
	}

	/**
	 * @param item
	 * @return
	 */
	private Report parseReport(Node item) {
		Report report = new Report();
//		report.setSourcePath(sourcePath);
		//report.setOutPath(outPath);
		Element e = (Element) item;
		report.setName(e.getAttribute("name"));
		report.setTitle(e.getAttribute("title"));
		if (!e.getAttribute("visible").equals("")) {
			report.setVisible(Boolean.parseBoolean(e.getAttribute("visible")));
		}
		NodeList list = item.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("paramters")) {
				report.setParamters(parseParamters(list.item(i)));
			}
		}
		return report;
	}

	/**
	 * @param item
	 * @return
	 */
	private ArrayList<Paramter> parseParamters(Node item) {
		ArrayList<Paramter> paramters = new ArrayList<Paramter>();
		NodeList list = item.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("paramter")) {
				Paramter paramter = new Paramter();
				Element e = (Element) list.item(i);
				paramter.setName(e.getAttribute("name"));
				paramter.setCaption(e.getAttribute("caption"));
				paramter.setType(e.getAttribute("type"));

				NodeList list2 = e.getChildNodes();
				for (int j = 0; j < list2.getLength(); j++) {
					if (list2.item(j).getNodeName().equals("property")) {
						Element e1 = (Element) list2.item(j);
						String name = e1.getAttribute("name");
						String value = e1.getAttribute("value");
						paramter.setProperty(name, value);
					}
				}
				paramters.add(paramter);
			}
		}
		return paramters;
	}
}
