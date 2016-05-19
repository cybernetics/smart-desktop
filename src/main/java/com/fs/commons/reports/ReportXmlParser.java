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

	ArrayList<JKReport> reports = new ArrayList<JKReport>();

	/**
	 *
	 */
	public ReportXmlParser() {
	}

	/**
	 * @return the reports
	 */
	public ArrayList<JKReport> getReports() {
		return this.reports;
	}

	/**
	 *
	 * @param in
	 * @throws JKXmlException
	 */
	public ArrayList<JKReport> parse(final InputStream in) throws JKXmlException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(in);
			final Node root = doc.getFirstChild();
			final NodeList list = root.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeName().equals("source-path")) {
					final Node node = list.item(i);
					this.sourcePath = node.getTextContent().trim();
				} else if (list.item(i).getNodeName().equals("out-path")) {
					this.outPath = list.item(i).getTextContent();
				} else if (list.item(i).getNodeName().equals("report")) {
					final JKReport report = parseReport(list.item(i));
					this.reports.add(report);
				}
			}
			return this.reports;
		} catch (final Exception e) {
			throw new JKXmlException(e);
		}
	}

	/**
	 * @param item
	 * @return
	 */
	private ArrayList<Paramter> parseParamters(final Node item) {
		final ArrayList<Paramter> paramters = new ArrayList<Paramter>();
		final NodeList list = item.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("paramter")) {
				final Paramter paramter = new Paramter();
				final Element e = (Element) list.item(i);
				paramter.setName(e.getAttribute("name"));
				paramter.setCaption(e.getAttribute("caption"));
				paramter.setType(e.getAttribute("type"));

				final NodeList list2 = e.getChildNodes();
				for (int j = 0; j < list2.getLength(); j++) {
					if (list2.item(j).getNodeName().equals("property")) {
						final Element e1 = (Element) list2.item(j);
						final String name = e1.getAttribute("name");
						final String value = e1.getAttribute("value");
						paramter.setProperty(name, value);
					}
				}
				paramters.add(paramter);
			}
		}
		return paramters;
	}

	/**
	 * @param item
	 * @return
	 */
	private JKReport parseReport(final Node item) {
		final JKReport report = new JKReport();
		// report.setSourcePath(sourcePath);
		// report.setOutPath(outPath);
		final Element e = (Element) item;
		report.setName(e.getAttribute("name"));
		report.setTitle(e.getAttribute("title"));
		if (!e.getAttribute("visible").equals("")) {
			report.setVisible(Boolean.parseBoolean(e.getAttribute("visible")));
		}
		final NodeList list = item.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("paramters")) {
				report.setParamters(parseParamters(list.item(i)));
			}
		}
		return report;
	}
}
