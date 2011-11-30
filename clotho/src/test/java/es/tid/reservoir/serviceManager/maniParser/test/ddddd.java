package es.tid.reservoir.serviceManager.maniParser.test;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.telefonica.claudia.slm.paas.PaasUtils;

public class ddddd {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String responseXml = "./src/test/resources/job-00009.xml";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = db.parse(responseXml);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	System.out.println (PaasUtils.tooString(doc));

    	 NodeList tasks = doc.getElementsByTagName("status");

         if (tasks.getLength() != 0 && ((Element)tasks.item(0)).getAttribute("status")!= null) {
        	 {
                System.out.println (((Element)tasks.item(0)).getNodeName()+ "  "+((Element)tasks.item(0)).getFirstChild().getNodeValue());
        	 }

         } 
       
	}

}
