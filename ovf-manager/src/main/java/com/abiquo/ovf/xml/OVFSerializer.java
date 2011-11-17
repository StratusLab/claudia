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
 * The Original Code is available at https://abicloud.svn.sourceforge.net/svnroot/abicloud
 *
 * The Initial Developer of the Original Code is Soluciones Grid, S.L. (www.abiquo.com),
 * Consell de Cent 296 principal 2º, 08007 Barcelona, Spain.
 * No portions of the Code have been created by third parties.
 * All Rights Reserved.
 *
 * Contributor(s):
 *     Telefónica Investigación y Desarrollo S.A.U. (http://www.tid.es)
 *     Emilio Vargas 6, 28043 Madrid, Spain.
 *
 */

package com.abiquo.ovf.xml;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
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

import org.dmtf.schemas.ovf.envelope._1.AnnotationSectionType;
import org.dmtf.schemas.ovf.envelope._1.ContentType;
import org.dmtf.schemas.ovf.envelope._1.DeploymentOptionSectionType;
import org.dmtf.schemas.ovf.envelope._1.DiskSectionType;
import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.EulaSectionType;
import org.dmtf.schemas.ovf.envelope._1.InstallSectionType;
import org.dmtf.schemas.ovf.envelope._1.NetworkSectionType;
import org.dmtf.schemas.ovf.envelope._1.ObjectFactory;
import org.dmtf.schemas.ovf.envelope._1.OperatingSystemSectionType;
import org.dmtf.schemas.ovf.envelope._1.ProductSectionType;
import org.dmtf.schemas.ovf.envelope._1.ResourceAllocationSectionType;
import org.dmtf.schemas.ovf.envelope._1.SectionType;
import org.dmtf.schemas.ovf.envelope._1.StartupSectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualHardwareSectionType;
import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.abiquo.ovf.exceptions.XMLException;



/**
 * Use JAXB to bind standard OVF-envelope and OVFIndex objects into/from XML documents.<br/>
 * 
 * @see Stax2Factory, where Woodstox is used as StAX implementation of the underlying XML parser.
 * @see ANT build file "jaxb" target, where the binding classes are generated.
 * @TODO XMLStreamWriter can specify the encoding ... useful ?
 * @TODO close writers even when an exception occurs.
 */
public class OVFSerializer
{
    private final static Logger logger = LoggerFactory.getLogger(OVFSerializer.class);
    
    /** Define the allowed objects to be binded from/into the OVF-envelope schema definition. */
    private final JAXBContext contextEnvelope;
    
    /** Define the allowed objects to be binded from/into the OVF-envelope schema definition. */
    private final JAXBContext contextVirtualSystem;
    
    /** Generated factory to create XML elements in OVF-envelope name space. */
    private final ObjectFactory factoryEnvelop;

    /** Determines if the marshalling process to format the XML document.*/
    private boolean formatOutput = true;
    
    /** Determines if the unmarshalling process is to validate the XML document.*/  
    private boolean validateXML = true;
    
    /** Path to the OVF XSD schema document (in order to validate documents). */
    private final static String OVF_ENVELOPE_SCHEMA_LOCATION = "resources/schemas/TCloud_Reservoir.xsd";

    /** Used to validate XML documents. */
    private Schema envelopeSchema; 
    
    /** The singleton instance. */
    private static OVFSerializer instance;
        
    /** Used to bind an envelope into a DOM document. **/
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
    public static OVFSerializer getInstance()
    {
        if (instance == null)
        {
            try
            {
                instance = new OVFSerializer();
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
    	URL schemaURL = OVFSerializer.class.getClassLoader().getResource(OVF_ENVELOPE_SCHEMA_LOCATION);
    	
		try 
		{
			logger.debug("Using schema from [{}]", schemaURL.toExternalForm());
			envelopeSchema = sf.newSchema(schemaURL);
		}
		catch (Exception e) // SAXException  or Null pointer 
		{
			throw new JAXBException("Can not localize the OVFEnvelope schema in order to validate documents",e);
		}        		  
    }

    /**
     * Instantiate a new OVFSerializer.
     * 
     * @throws JAXBException, if some JAXB context can not be created.
     */
    private OVFSerializer() throws JAXBException
    {
        contextEnvelope = JAXBContext.newInstance(EnvelopeType.class);
        contextVirtualSystem = JAXBContext.newInstance(VirtualSystemType.class);
        factoryEnvelop = new ObjectFactory();
        //contextIndex = JAXBContext.newInstance(new Class[]{EnvelopeType.class});//RepositorySpace.class,OVFIndex.class});
        //factoryIndex = new com.abiquo.repositoryspace.ObjectFactory();      
    }
    
    
    

    /**
     * Creates an XML document representing the provided OVF-envelop and write it to output stream.
     * 
     * @param envelope, the object to be binded into an XML document.
     * @param os, the destination of the XML document.
     * @throws XMLException, any XML problem.
     */
    public void writeXML(EnvelopeType envelope, OutputStream os) throws XMLException
    {
        XMLStreamWriter writer;
        Marshaller marshall;

        try
        {
            writer = Stax2Factory.getStreamWriterFactory().createXMLStreamWriter(os);
            marshall = contextEnvelope.createMarshaller();

            if (formatOutput)
            {
                marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
            }

            marshall.marshal(factoryEnvelop.createEnvelope(envelope), writer);

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
     * @param envelope, the object to be binded into an XML document.
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
            marshall = contextEnvelope.createMarshaller();

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
     * @param envelope, the object to be binded into an XML document.
     * @throws XMLException, any XML problem.
     */
    public String writeXML(SectionType section) throws XMLException
    {
        Marshaller marshall;
        StringWriter writer = new StringWriter();
        
        try
        {
            marshall = contextEnvelope.createMarshaller();

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
     * Creates an XML document representing an OVF-envelop with the provided virtual system and
     * write it to output stream.
     * 
     * @param virutalSystem, the object inside the envelope to be binded into an XML document.
     * @param os, the destination of the XML document.
     * @throws XMLException, any XML problem.
     */
    public void writeXML(VirtualSystemType virtualSystem, OutputStream os) throws XMLException
    {
        EnvelopeType envelope;

        envelope = factoryEnvelop.createEnvelopeType();
        envelope.setContent(factoryEnvelop.createVirtualSystem(virtualSystem));

        writeXML(envelope, os);
    }
    
    public String writeXML(VirtualSystemType virtualSystem) throws XMLException
    {
        Marshaller marshall;
        StringWriter writer = new StringWriter();
        
        try
        {
            marshall = contextEnvelope.createMarshaller();

            if (formatOutput)
            {
                marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            }

            marshall.marshal(toJAXBElement(virtualSystem), writer);
            return writer.toString();
        }
        catch (JAXBException ea)
        {
            throw new XMLException(ea);
        }        
    }
    
    public String writeXML(EnvelopeType env) throws XMLException
    {
        Marshaller marshall;
        StringWriter writer = new StringWriter();
        
        try
        {
            marshall = contextEnvelope.createMarshaller();

            if (formatOutput)
            {
                marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            }

            marshall.marshal(toJAXBElement(env), writer);
            return writer.toString();
        }
        catch (JAXBException ea)
        {
            throw new XMLException(ea);
        }        
    }

  
    /**
     * Read an expected OVF-envelope form the provided source.
     * 
     * @param is, the input stream source where read XML documents.
     * @return the OVF-envelope read from source.
     * @throws XMLException, if it is not an envelope type or any XML problem.
     */
    public EnvelopeType readXMLEnvelope(InputStream is) throws XMLException
    {
        XMLStreamReader reader;
        Unmarshaller unmarshall;
        JAXBElement<EnvelopeType> jaxbEnvelope;

        try
        {
            reader = Stax2Factory.getStreamReaderFactory().createXMLStreamReader(is);            
            unmarshall = contextEnvelope.createUnmarshaller();
            
            if(validateXML)
            {            	
            	unmarshall.setSchema(envelopeSchema);
            }
           
            jaxbEnvelope = unmarshall.unmarshal(reader, EnvelopeType.class);

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

        return jaxbEnvelope.getValue();
    }
    
    
    /**
     * Read an expected OVF-Product Section form the provided source.
     * 
     * @param is, the input stream source where read XML documents.
     * @return the ProductSectionType read from source.
     * @throws XMLException, if it is not an envelope type or any XML problem.
     */
    public ProductSectionType readXMLProductSection(InputStream is) throws XMLException
    {
        XMLStreamReader reader;
        Unmarshaller unmarshall;
        JAXBElement<ProductSectionType> jaxbEnvelope;

        try
        {
            reader = Stax2Factory.getStreamReaderFactory().createXMLStreamReader(is);            
            unmarshall = contextEnvelope.createUnmarshaller();
            
            if(validateXML)
            {            	
            	unmarshall.setSchema(envelopeSchema);
            }
           
            jaxbEnvelope = unmarshall.unmarshal(reader, ProductSectionType.class);

            reader.close();
        }
        catch (JAXBException ea)
        {
        	ea.printStackTrace(); // TODO remove
        	throw new XMLException(ea.getLinkedException().getMessage());            
        }
        catch (XMLStreamException ex)
        {
        	ex.printStackTrace(); // TODO remove
            throw new XMLException(ex);
        }

        return jaxbEnvelope.getValue();
    }
    
    public VirtualSystemType readXMLVirtualSystem (InputStream is) throws XMLException
    {
        XMLStreamReader reader;
        Unmarshaller unmarshall;
        JAXBElement<VirtualSystemType> jaxbEnvelope;

        try
        {
            reader = Stax2Factory.getStreamReaderFactory().createXMLStreamReader(is);            
            unmarshall = contextVirtualSystem.createUnmarshaller();
            jaxbEnvelope = unmarshall.unmarshal(reader, VirtualSystemType.class);

            reader.close();
        }
        catch (JAXBException ea)
        {
        	ea.printStackTrace(); // TODO remove
        	throw new XMLException(ea.getLinkedException().getMessage());            
        }
        catch (XMLStreamException ex)
        {
        	ex.printStackTrace(); // TODO remove
            throw new XMLException(ex);
        }

        return jaxbEnvelope.getValue();
    }
    

    /**
     * Read an expected OVF-envelope form the provided source.
     * @param strEnvelope, an string representation of an XML OVF envelope file.
     * @param isNamespaceAware, determine if the created document is XML name space aware.
     * @return the OVF-envelope read from source.
     * @deprecated use readXMLEnvelope with InputStream argument
     */
    public EnvelopeType readXMLEnvelope(String strEnvelope, boolean isNamespaceAware) throws ParserConfigurationException, JAXBException, SAXException, IOException
    {
        // Now serialize the Java Content tree back to XML data
     
        StringReader stReader = new StringReader(strEnvelope);
        InputSource  source = new InputSource(stReader);
        
        docBuilderFact.setNamespaceAware(isNamespaceAware);        
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        
        Document doc = docBuilder.parse(source);
        
        Binder<Node> binder = contextEnvelope.createBinder();        
        
        JAXBElement<EnvelopeType> jaxb = binder.unmarshal(doc, EnvelopeType.class); 
        EnvelopeType envelope = jaxb.getValue();
        
        
        stReader.close();
        
        return envelope;               
    }

   
    /** Wrap into an JAXBElement the provided OVF envelope.**/
    public JAXBElement<EnvelopeType> toJAXBElement(EnvelopeType envelope)
    {
        return  factoryEnvelop.createEnvelope(envelope);      
        
       
    }
    
    public JAXBElement<VirtualSystemType> toJAXBElement(VirtualSystemType vs)
    {
        return  factoryEnvelop.createVirtualSystem(vs);      
        
        
    }
   
    

    /** Wrap into an JAXBElement the provided OVF envelope section.**/
    @SuppressWarnings("unchecked")
	public <T extends SectionType> JAXBElement<T> toJAXBElement(T section)
    {
    	JAXBElement<T> jaxB;
    	
    	if(section instanceof DeploymentOptionSectionType)
    	{
    		jaxB = (JAXBElement<T>) factoryEnvelop.createDeploymentOptionSection((DeploymentOptionSectionType) section);
    	}
    	else if(section instanceof StartupSectionType)
    	{
    		jaxB = (JAXBElement<T>) factoryEnvelop.createStartupSection((StartupSectionType) section);
    	}
    	else if(section instanceof InstallSectionType)
		{
    		jaxB = (JAXBElement<T>) factoryEnvelop.createInstallSection((InstallSectionType) section);
		}
    	else if(section instanceof OperatingSystemSectionType)
		{
    		jaxB = (JAXBElement<T>) factoryEnvelop.createOperatingSystemSection((OperatingSystemSectionType) section);
		}
    	else if(section instanceof AnnotationSectionType)
		{
    		jaxB = (JAXBElement<T>) factoryEnvelop.createAnnotationSection((AnnotationSectionType) section);
		}
    	else if(section instanceof NetworkSectionType)
		{
    		jaxB = (JAXBElement<T>) factoryEnvelop.createNetworkSection((NetworkSectionType) section);
		}
    	else if(section instanceof EulaSectionType)
		{
    		jaxB = (JAXBElement<T>) factoryEnvelop.createEulaSection((EulaSectionType) section);
		}
    	else if(section instanceof ProductSectionType)
		{
    		jaxB = (JAXBElement<T>) factoryEnvelop.createProductSection((ProductSectionType) section);
		}
    	else if(section instanceof VirtualHardwareSectionType)
		{
    		jaxB = (JAXBElement<T>) factoryEnvelop.createVirtualHardwareSection((VirtualHardwareSectionType) section);
		}
    	else if(section instanceof ResourceAllocationSectionType)
		{
    		jaxB = (JAXBElement<T>) factoryEnvelop.createResourceAllocationSection((ResourceAllocationSectionType) section);	
		}
    	else if(section instanceof DiskSectionType)
		{
    		jaxB = (JAXBElement<T>) factoryEnvelop.createDiskSection((DiskSectionType) section);
		}
    	else
    	{
    		// TODO throws an exception for invalid section 
    		jaxB = null;
    	}
		
		return jaxB;
    }
    
    
    /**
     * Wrap into an DOM document the provided OVF envelope.
     * @param envelope, the OVF envelope the be wrapped
     * @param isNamespaceAware, determine if the created document is XML name space aware. 
     * @return a DOM document containing the provided OVF envelope.
     * TODO rename to "toDocument"
     * */
    public Document bindToDocument(EnvelopeType envelope, boolean isNamespaceAware) throws ParserConfigurationException, JAXBException
    {
        // Now serialize the Java Content tree back to XML data        
        docBuilderFact.setNamespaceAware(isNamespaceAware);
        
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();        
        Document doc = docBuilder.newDocument();
        
        Binder<Node> binder = contextEnvelope.createBinder();
        
        binder.marshal(toJAXBElement(envelope), doc);

        return doc;               
    }
    
}
