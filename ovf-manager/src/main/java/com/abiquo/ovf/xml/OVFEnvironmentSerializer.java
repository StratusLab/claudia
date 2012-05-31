/**
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is available at https://svn.forge.morfeo-project.org/claudia
 *
 * The Initial Developer of the Original Code is Telefonica Investigacion y Desarrollo S.A.U.,
 * (http://www.tid.es), Emilio Vargas 6, 28043 Madrid, Spain.
.*
 * No portions of the Code have been created by third parties.
 * All Rights Reserved.
 *
 * Contributor(s): ______________________________________.
 *
 */

package com.abiquo.ovf.xml;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.dmtf.schemas.ovf.environment._1.EnvironmentType;
import org.dmtf.schemas.ovf.environment._1.ObjectFactory;
import org.dmtf.schemas.ovf.environment._1.PlatformSectionType;
import org.dmtf.schemas.ovf.environment._1.PropertySectionType;
import org.dmtf.schemas.ovf.environment._1.SectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.abiquo.ovf.exceptions.XMLException;

public class OVFEnvironmentSerializer {

	    private final static Logger logger = LoggerFactory.getLogger(OVFEnvironmentSerializer.class);
	    
	    /** Define the allowed objects to be binded from/into the OVF-environment schema definition. */
	    private final JAXBContext contextEnvironment;
	    
	    /** Generated factory to create XML elements in OVF-environment name space. */
	    private final ObjectFactory factoryEnvironment;

	    /** Determines if the marshalling process to format the XML document.*/
	    private boolean formatOutput = true;
	    
	    /** Determines if the unmarshalling process is to validate the XML document.*/  
	    private boolean validateXML = true;
	    
	    /** Path to the OVF XSD schema document (in order to validate documents). */
	    private final static String OVF_ENVIRONMENT_SCHEMA_LOCATION = "resources/schemas/dsp8027.xsd";

	    /** Used to validate XML documents. */
	    private Schema environmentSchema; 
	    
	    /** The singleton instance. */
	    private static OVFEnvironmentSerializer instance;
	        
	    /** Used to bind an environment into a DOM document. **/
	    private static DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();

	    //** Define the allowed objects to be binded form/into the OVFIndex schema definition. */
	    //private final JAXBContext contextIndex;

	    ///** Generated factory to create XML elements on OVFIndex name space. */
	    //private final com.abiquo.repositoryspace.ObjectFactory factoryIndex;


	    /**
	     * Get the OVFSerializer singelton instance.
	     * 
	     * @return the OVFSerializer instance or null if it can not be created.
	     */
	    public static OVFEnvironmentSerializer getInstance()
	    {
	        if (instance == null)
	        {
	            try
	            {
	                instance = new OVFEnvironmentSerializer();
	            }
	            catch (JAXBException e)
	            {
	                logger.error("OVFSerializer instance can not be created ", e);
	            }
	        }

	        return instance;
	    }
	    
	    
	    /**
	     * Configure the ''formatOutput'' property. 
	     * Determines if the marshalling process to format the XML document.
	     * */
	    public void setFormatOutput(boolean formatOutput)
	    {
	        this.formatOutput = formatOutput;
	    }
	    
	    
	    /**
	     * Configure the ''validateXML'' property. 
	     * Determines if the unmarshalling process is to validate the XML document.
	     * @throws JAXBException, if the schema can not be located.
	     * */
	    public void setValidateXML(boolean validateXML) throws JAXBException
	    {
	        this.validateXML = validateXML;
	        
	        if(validateXML)
	        {
	        	configureValidation();
	        }
	    }

	    /**
	     * Set up the schema to be used during validation.
	     * @throws JAXBException 
	     * */
	    private void configureValidation() throws JAXBException
	    {
	    	//SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);			      
	    	SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.DEFAULT_NS_PREFIX);
	    	URL schemaURL = OVFSerializer.class.getClassLoader().getResource(OVF_ENVIRONMENT_SCHEMA_LOCATION);
	    	
			try 
			{
				logger.debug("Using schema from [{}]", schemaURL.toExternalForm());
				environmentSchema = sf.newSchema(schemaURL);
			}
			catch (Exception e) // SAXException  or Null pointer 
			{
				throw new JAXBException("Can not localize the OVFEnvironment schema in order to validate documents",e);
			}        		  
	    }

	    /**
	     * Instantiate a new OVFSerializer.
	     * 
	     * @throws JAXBException, if some JAXB context can not be created.
	     */
	    private OVFEnvironmentSerializer() throws JAXBException
	    {
	    	contextEnvironment = JAXBContext.newInstance(EnvironmentType.class);
	        factoryEnvironment = new ObjectFactory();
	        //contextIndex = JAXBContext.newInstance(new Class[]{EnvironmentType.class});//RepositorySpace.class,OVFIndex.class});
	        //factoryIndex = new com.abiquo.repositoryspace.ObjectFactory();      
	    }
	    
	    
	    

	    /**
	     * Creates an XML document representing the provided OVF-envelop and write it to output stream.
	     * 
	     * @param environment, the object to be binded into an XML document.
	     * @param os, the destination of the XML document.
	     * @throws XMLException, any XML problem.
	     */
	    public void writeXML(EnvironmentType environment, OutputStream os) throws XMLException
	    {
	        XMLStreamWriter writer;
	        Marshaller marshall;

	        try
	        {
	            writer = Stax2Factory.getStreamWriterFactory().createXMLStreamWriter(os);
	            marshall = contextEnvironment.createMarshaller();

	            if (formatOutput)
	            {
	                marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	            }

	            marshall.marshal(factoryEnvironment.createEnvironment(environment), writer);

	            writer.close();
	        }
	        catch (JAXBException ea)
	        {
	            throw new XMLException(ea);
	        }
	        catch (XMLStreamException ex)
	        {
	            throw new XMLException(ex);
	        }
	    }
	    
	    /**
	     * Creates an XML document representing the provided OVF-section and write it to output stream.
	     * 
	     * @param section, the object to be binded into an XML document.
	     * @param os, the destination of the XML document.
	     * @throws XMLException, any XML problem.
	     */
	    public void writeXML(SectionType section, OutputStream os) throws XMLException
	    {
	        XMLStreamWriter writer;
	        Marshaller marshall;

	        try
	        {
	            writer = Stax2Factory.getStreamWriterFactory().createXMLStreamWriter(os);
	            marshall = contextEnvironment.createMarshaller();

	            if (formatOutput)
	            {
	                marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	            }

	            marshall.marshal(toJAXBElement(section), writer);
	            
	            writer.close();
	        }
	        catch (JAXBException ea)
	        {
	            throw new XMLException(ea);
	        }
	        catch (XMLStreamException ex)
	        {
	            throw new XMLException(ex);
	        }
	    }
	    
	    /**
	     * Creates an XML document representing the provided OVF-section and write it to formatter string.
	     * 
	     * @param section, the object to be binded into an XML document.
	     * @throws XMLException, any XML problem.
	     */
	    public String writeXML(SectionType section) throws XMLException
	    {
	        Marshaller marshall;
	        StringWriter writer = new StringWriter();
	        
	        try
	        {
	            marshall = contextEnvironment.createMarshaller();

	            if (formatOutput)
	            {
	                marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	            }

	            marshall.marshal(toJAXBElement(section), writer);
	            return writer.toString();
	        }
	        catch (JAXBException ea)
	        {
	            throw new XMLException(ea);
	        }        
	    }
	    
	   
	  
	    /**
	     * Read an expected OVF-environment form the provided source.
	     * 
	     * @param is, the input stream source where read XML documents.
	     * @return the OVF-environment read from source.
	     * @throws XMLException, if it is not an environment type or any XML problem.
	     */
	    public EnvironmentType readXMLEnvironment(InputStream is) throws XMLException
	    {
	        XMLStreamReader reader;
	        Unmarshaller unmarshall;
	        JAXBElement<EnvironmentType> jaxbEnvironment;

	        try
	        {
	            reader = Stax2Factory.getStreamReaderFactory().createXMLStreamReader(is);            
	            unmarshall = contextEnvironment.createUnmarshaller();
	            
	            if(validateXML)
	            {            	
	            	unmarshall.setSchema(environmentSchema);
	            }
	           
	            jaxbEnvironment = unmarshall.unmarshal(reader, EnvironmentType.class);

	            reader.close();
	        }
	        catch (JAXBException ea)
	        {
	        	ea.printStackTrace(); // TODO remove
	            throw new XMLException(ea);            
	        }
	        catch (XMLStreamException ex)
	        {
	        	ex.printStackTrace(); // TODO remove
	            throw new XMLException(ex);
	        }

	        return jaxbEnvironment.getValue();
	    }
	    
	   /** Wrap into an JAXBElement the provided OVF environment.**/
	    public JAXBElement<EnvironmentType> toJAXBElement(EnvironmentType environment)
	    {
	        return  factoryEnvironment.createEnvironment(environment);       
	    }
	   
	    

	    /** Wrap into an JAXBElement the provided OVF environment section.**/
	    @SuppressWarnings("unchecked")
		public <T extends SectionType> JAXBElement<T> toJAXBElement(T section)
	    {
	    	JAXBElement<T> jaxB;
	    	
	    	if(section instanceof PlatformSectionType)
	    	{
	    		jaxB = (JAXBElement<T>) factoryEnvironment.createPlatformSection((PlatformSectionType) section);
	    	}
	    	else if(section instanceof PropertySectionType)
	    	{
	    		jaxB = (JAXBElement<T>) factoryEnvironment.createPropertySection((PropertySectionType) section);
	    	}
	    	else
	    	{
	    		// TODO throws an exception for invalid section 
	    		jaxB = null;
	    	}
			
			return jaxB;
	    }
	    
	    
	    /**
	     * Wrap into an DOM document the provided OVF environment.
	     * @param environment, the OVF environment the be wrapped
	     * @param isNamespaceAware, determine if the created document is XML name space aware. 
	     * @return a DOM document containing the provided OVF environment.
	     * TODO rename to "toDocument"
	     * */
	    public Document bindToDocument(EnvironmentType environment, boolean isNamespaceAware) throws ParserConfigurationException, JAXBException
	    {
	        // Now serialize the Java Content tree back to XML data        
	        docBuilderFact.setNamespaceAware(isNamespaceAware);
	        
	        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();        
	        Document doc = docBuilder.newDocument();
	        
	        Binder<Node> binder = contextEnvironment.createBinder();
	        
	        binder.marshal(toJAXBElement(environment), doc);

	        return doc;               
	    }
	    
}
