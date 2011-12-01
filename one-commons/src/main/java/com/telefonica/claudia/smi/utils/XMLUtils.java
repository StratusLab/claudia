/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

  */
package com.telefonica.claudia.smi.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.telefonica.claudia.smi.exception.ParserException;

/**
 * Utilities to manage XML data
 * 
 * @author luismarcos.ayllon
  */
public class XMLUtils {

	private static Logger log = Logger.getLogger(XMLUtils.class);
	
	/**
	 * Parses a XML document encoded by a string
	 * 
	 * @param sequence sequence of characters that encodes the XML document
	 * @param namespaceAware to be aware of name space
	 * @return the XML document 
	 * @throws ParserException if the parser can not be configured, or sequence can not
	 * 					       be parsed
	 */
	public static Document parse (String sequence, boolean namespaceAware) throws ParserException {
		try {
			
			DocumentBuilder builder = getDocumentBuilder(namespaceAware);
			Document doc = builder.parse(new ByteArrayInputStream(sequence.getBytes()));
			return doc;
			
		} catch (SAXException e) {
			log.error("Parse error obtaining info: " + e.getMessage());
			throw new ParserException ("XML Parse error", e);
		} catch (IOException e) {
			log.error("Parse error creating input: " + e.getMessage());
			throw new ParserException ("XML Parse error", e);
		}
	}
	
	/**
	 * Parses a XML document encoded by a string
	 * 
	 * @param sequence sequence of characters that encodes the XML document
	 * @return the XML document 
	 * @throws ParserException if the parser can not be configured, or sequence can not
	 * 					       be parsed
	 */
	public static Document parse (String sequence) throws ParserException {
		return parse (sequence, false);
	}
	
	/**
	 * Obtains a document builder
	 * 
	 * @param namespaceAware to be aware of name space
	 * @return the builder
	 * @throws ParserException if the builder can not be created
	 */
	public static DocumentBuilder getDocumentBuilder (boolean namespaceAware) throws ParserException{
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			if (namespaceAware){
				factory.setNamespaceAware(true);	
			}
			DocumentBuilder builder = factory.newDocumentBuilder(); 
			return builder;
	
		} catch (ParserConfigurationException e) {
			log.error("Error configuring parser: " + e.getMessage());
			throw new ParserException ("Parser Configuration Error", e);
		}
	}
	
	
	/**
	 * Creates a XML document
	 * 
	 * @param namespaceAware to be aware of name space
	 * @return the document
	 * @throws ParserException
	 */
	public static Document createDocument (boolean namespaceAware) throws ParserException {
		DocumentBuilder builder = getDocumentBuilder(namespaceAware);
		Document doc = builder.newDocument();
		return doc;
	}
	
	/**
	 * Creates a XMK document with a given root node
	 * 
	 * @param root the root node for the new document
	 * @param namespaceAware to be aware of name space
	 * @return the new document with the given node as root node 
	 * @throws ParserException
	 */
	public static Document createDocument (Node root, boolean namespaceAware) throws ParserException {
		Document doc = createDocument(namespaceAware);
		doc.appendChild(doc.importNode(root, true));
		return doc;
	}

}
