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
public class XmlValidator{
	 
	/**
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public void validate(File xmlfile,File xsdfile)throws SAXException,ParserConfigurationException,IOException{
		
	     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	     dbf.setNamespaceAware(true);
	     
	     
	     DocumentBuilder parser = dbf.newDocumentBuilder();
	     Document document = parser.parse(xmlfile);
	            

	     SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	            

	     Source schemaFile = new StreamSource(xsdfile);
	     Schema schema = factory.newSchema(schemaFile);
	        
	     Validator validator = schema.newValidator();
	     validator.validate(new DOMSource(document));
	     System.out.println("success");
		
	}
	 
//	 public static void main()throws SAXException,ParserConfigurationException,IOException{
//	 XmlValidator validator = new XmlValidator();
//     File xml = new File("E:/New Folder/meta-config.xml");
//     File xsd = new File("E:/New Folder/meta-config-do.xsd");
//	 validator.validate(xml, xsd);
//	 }
	
}