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

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author saeed
 *
 */
public class XmlValidator {

	/**
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public void validate(final File xmlfile, final File xsdfile) throws SAXException, ParserConfigurationException, IOException {

		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);

		final DocumentBuilder parser = dbf.newDocumentBuilder();
		final Document document = parser.parse(xmlfile);

		final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		final Source schemaFile = new StreamSource(xsdfile);
		final Schema schema = factory.newSchema(schemaFile);

		final Validator validator = schema.newValidator();
		validator.validate(new DOMSource(document));
		System.out.println("success");

	}

	// public static void main()throws
	// SAXException,ParserConfigurationException,IOException{
	// XmlValidator validator = new XmlValidator();
	// File xml = new File("E:/New Folder/meta-config.xml");
	// File xsd = new File("E:/New Folder/meta-config-do.xsd");
	// validator.validate(xml, xsd);
	// }

}