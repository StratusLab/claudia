package com.telefonica.claudia.smi;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telefonica.claudia.smi.TCloudConstants.ErrorType;

public class TCloudException extends Exception {

	private String uri;
	private String elementType;
	private ErrorType errorType;
	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5858849420125201599L;
	
	public TCloudException(String uri ,String elementType, ErrorType errorType){
		this.uri = uri;
		this.elementType = elementType;
		this.errorType = errorType;
	}

	public Document createErrorMessage() {
        try {
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder;

			docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
            Element r = doc.createElement("ErrorSet");
            doc.appendChild(r);
            
            if (errorType==ErrorType.UNKNOWN_ELEMENTS) {
                Element unknown= doc.createElement("UnknownElements");
                r.appendChild(unknown);
                
                Element element= doc.createElement("element");
                unknown.appendChild(element);
                
                element.setAttribute("type", elementType);
                element.setAttribute("ref", uri);
            }
            else if (errorType==ErrorType.ELEMENT_NOT_FOUND) {
                Element elementNotFound= doc.createElement("ElementNotFound");
                r.appendChild(elementNotFound);
                
                Element element= doc.createElement("element");
                elementNotFound.appendChild(element);
                
                element.setAttribute("type", elementType);
                element.setAttribute("ref", uri);
            } 
            
            return doc;
        } catch (IllegalArgumentException iae) {
        	return null;
        } catch (ParserConfigurationException pe) {
        	return null;
		}
	}
}
