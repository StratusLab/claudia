/*
* Claudia Project
* http://claudia.morfeo-project.org
*
* (C) Copyright 2010 Telefonica Investigacion y Desarrollo
* S.A.Unipersonal (Telefonica I+D)
*
* See CREDITS file for info about members and contributors.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the Affero GNU General Public License (AGPL) as 
* published by the Free Software Foundation; either version 3 of the License, 
* or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the Affero GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
* If you want to use this software an plan to distribute a
* proprietary application in any way, and you are not licensing and
* distributing your source code under AGPL, you probably need to
* purchase a commercial license of the product. Please contact
* claudia-support@lists.morfeo-project.org for more information.
*/
package com.telefonica.claudia.slm.monitoring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Client;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class WasupClient {

	private static Logger log = Logger.getLogger("Monitoring");
	
    private static final String DATA_PATH = "/data";
    private static final String ELEMENTS = "/elements";
    private static final String ELEMENT_TYPES = "/elementTypes";
    private static final String MEASUREMENT_TYPES = "/measurement_types";
    private static final String MEASURED_VALUES = "/measured_values";
    private static final String MEASUREMENT_RANGES = "/registerRanges";

    public static final int NULL_ELEMENT_TYPE = 0;
    public static final int VDC_ELEMENT_TYPE = 1;
    public static final int SERVICE_ELEMENT_TYPE = 2;
    public static final int VEE_ELEMENT_TYPE = 3;
    public static final int VEEREPLICA_ELEMENT_TYPE = 4;
    public static final int NET_ELEMENT_TYPE = 9;

    public static final int NO_ELEMENT_FOUND = -9999;

    private String server = null;
    private String wasupSesionId = "invalid";

    private Client client = new Client(Protocol.HTTP);

    private static WasupClient thisInstance=null;
    
    public static WasupClient getWasupClient() {
    	if (thisInstance==null) {
    		thisInstance = new WasupClient();
    	} 
    	
    	return thisInstance;
    }
    
	private WasupClient() {
    	
	}
	
	public void login(String server, String login, String passw) throws Exception {
         if (client.isStopped()) 
        	 client.start();

         this.server = server;

         log.info("WasupClient.login: server=" + server + "; user=" + login);
         Reference HWref = new Reference(server + "/login?format=HTML");
         Request request = new Request(Method.POST, HWref);

         List<Parameter> parameterList = new ArrayList<Parameter>();

         parameterList.add(new Parameter("username", login));
         parameterList.add(new Parameter("password", passw));

         Form form = new Form(parameterList);
         Representation webRepresentation = form.getWebRepresentation(CharacterSet.UTF_8);
         request.setEntity(webRepresentation);

         Response response = client.handle(request);

         wasupSesionId = response.getCookieSettings().getFirstValue("wasup-session-id");
         log.info("wasupSesionId:" + wasupSesionId);
	 }
	
	 private Response get(String path) throws IOException {
	         Reference ref = new Reference(server + path);
	         log.info("WasupClient.get: uri=" + server + path);
	         Request request = new Request(Method.GET, ref);
	         request.getCookies().add("wasup-session-id", wasupSesionId);
	         return client.handle(request);
	 }
	
	 private Response post(String path, Representation rep) throws IOException {
         Reference ref = new Reference(server + path);
         log.info("WasupClient.post: uri=" + server + path);
         Request request = new Request(Method.POST, ref);
         request.getCookies().add("wasup-session-id", wasupSesionId);
         request.setEntity(rep);
         return client.handle(request);
	 }
	 
	 private Response delete(String path) throws IOException {
         Reference ref = new Reference(server + path);
         log.info("WasupClient.delete: uri=" + server + path);
         Request request = new Request(Method.DELETE, ref);
         request.getCookies().add("wasup-session-id", wasupSesionId);
         return client.handle(request);
	 }
	 
	 public String getWasupSesionId() {
	         return wasupSesionId;
	 }
	
	 public Representation getElementMeasurementTypesRepresentation(String elementId) {
	         Representation rep = null;
	         try {
	                 Response res = get(DATA_PATH + ELEMENTS + "/" + elementId + MEASUREMENT_TYPES+"/");
	                 rep = res.getEntity();
	         } catch (IOException e) {
	                 log.error("I/O error retrieving measurement type: " + e.getMessage());
	         }
	         return rep;
	 }
	
	 public Representation getMeasuredValuesRepresentation(String measureDescriptorId, int samples) {
	         Representation rep = null;
	         String uri = DATA_PATH + MEASUREMENT_TYPES + "/" + measureDescriptorId  + MEASURED_VALUES+"/";
	         if (samples > 0) uri += "?limit=" + samples;
	         try {
	                 Response res = get(uri);
	                 rep = res.getEntity();
	         } catch (IOException e) {
	        	 	log.error("I/O error retrieving measured values: " + e.getMessage());	
	         }
	         return rep;
	 }
	
	 public int getElementId(String name, int elementTypeId) {
	         String url = DATA_PATH + ELEMENTS + "/" + "?name=" + name;
	         if (elementTypeId > NULL_ELEMENT_TYPE)
	                 url += "&elementType.elementTypeId=" + elementTypeId;
	
	         int ret = NO_ELEMENT_FOUND;
	         try {
	                 Response res = get(url);
	                 JsonRepresentation jsonRep = new JsonRepresentation(res.getEntity());
	                 JSONObject jo = jsonRep.toJsonArray().getJSONObject(0);
	                 int tableSize = jo.getInt("tableSize");
	                 if (tableSize >= 1){
	                         //Element name found
	                         JSONObject joElement = jo.getJSONArray("rows").getJSONObject(0);
	                         ret = joElement.getInt("elementId");
	                 }
	                 log.debug(jsonRep.getText());
	         } catch (IOException e) {
	        	 	log.error("I/O error retrieving element Id for element["+ name +"] of type [" + elementTypeId + "]: " + e.getMessage());
	         } catch (JSONException e) {
	        	 	log.error("Error parsing JSON representatino of element["+ name +"] of type [" + elementTypeId + "]: " + e.getMessage());
	         }
	         return ret;
	 }
	 
	 public int getMeasurementTypeId(String measurementName, int elementId) {
	         
		 	try {
	                 return Integer.parseInt(measurementName);
		 	} catch (NumberFormatException nfe) {
		 		
		 	}
	
	        String url = DATA_PATH + MEASUREMENT_TYPES + "/?name=" + measurementName
	                                 + "&element.elementId=" + elementId;
	
	        int ret = -1;
	        try {
	                Response res = get(url);
	                JsonRepresentation jsonRep = new JsonRepresentation(res.getEntity());
	                JSONObject jo = jsonRep.toJsonArray().getJSONObject(0);
	                int tableSize = jo.getInt("tableSize");
	                if (tableSize >= 1){
	                        //Measurement Type found
	                        JSONObject joElement = jo.getJSONArray("rows").getJSONObject(0);
	                        ret = joElement.getInt("measurementTypeId");
	                }
	                log.debug(jsonRep.getText());
	         } catch (IOException e) {
	        	 	log.error("I/O error retrieving measurement type Id for measurement ["+ measurementName +"] of type [" + elementId + "]: " + e.getMessage());
	         } catch (JSONException e) {
	        	 	log.error("Error parsing JSON representatino of measurement ["+ measurementName +"] of type [" + elementId + "]: " + e.getMessage());
	         }
	        return ret;
	 }

	 /**
	  * Calls the wasup to obtain the id of a type through its name.
	  * 
	  * @param typeName
	  * 	Name of the type whose id is wanted.
	  * 
	  * @return
	  * 	The id of the element type if there is one or -1, otherwise 
	  * 
	 * @throws IOException 
	 * @throws JSONException 
	  */
	 public int getElementTypeId(String typeName) throws IOException, JSONException {
	
	        String url = DATA_PATH + ELEMENT_TYPES + "/?name=" + typeName;
	
	        int ret = -1;
	        
            Response res = get(url);
            JsonRepresentation jsonRep = new JsonRepresentation(res.getEntity());
            JSONObject jo = jsonRep.toJsonArray().getJSONObject(0);
            int tableSize = jo.getInt("tableSize");
            if (tableSize >= 1){
                    //Measurement Type found
                    JSONObject joElement = jo.getJSONArray("rows").getJSONObject(0);
                    ret = joElement.getInt("elementTypeId");
            }
            log.debug(jsonRep.getText());

	        return ret;
	 }
	 
	 /**
	  * Creates a new element in the wasup, under the element <i>parent</i>.
	  * 
	  * The folloging information is needed for the element creation:
	  * 
	  * 	- parentElement.elementId
	  * 	- elementType.elementTypeId
	  * 	- name
	  * 	- description
	  * 	- location
	  * 
	  * @param parent
	  * 	The element resource that will hold the target element.
	  * 
	  * @param fqn
	  * 	FQN of the target element to be created in the wasup under the parent.
	  * 
	  * @return
	  * 	The Wasup ID of the newly created element.
	 * @throws IOException 
	 * @throws JSONException 
	  */
	 public int createElement(String parent, String fqn, String typeId) throws IOException, JSONException {
		
		log.info("Creating element: " + fqn + " under element " + parent);
		
        Form form = new Form();
        form.add("parentElement.elementId", parent);   
        form.add("elementType.elementTypeId", typeId);   
        form.add("name", fqn);   
        form.add("description", "Reservoir element " + fqn);   
        form.add("location", "Local");   
        form.add("responseType", "XML"); 
        
        Representation rep = form.getWebRepresentation();

        String uri = DATA_PATH + ELEMENTS + "/";
        
        Response response = post(uri, rep);
        
        if (response.getStatus().isSuccess()) {

			int ret=-1;
			if (response.getEntity().getMediaType().equals(MediaType.APPLICATION_JSON)) {
				JsonRepresentation jsonRep = new JsonRepresentation(response.getEntity());
				
				JSONObject jo = jsonRep.toJsonArray().getJSONObject(0);
	            int tableSize = jo.getInt("tableSize");
	            if (tableSize >= 1){
	                    JSONObject joElement = jo.getJSONArray("rows").getJSONObject(0);
	                    ret = joElement.getInt("elementId");
	            }
            } else if (response.getEntity().getMediaType().equals(MediaType.TEXT_XML)) {
            	DomRepresentation domRep = new DomRepresentation(response.getEntity());
            	
            	Document doc = domRep.getDocument();
            	NodeList nodeId = doc.getElementsByTagName("elementId");
            	
            	try {
            		if (nodeId.getLength() <= 0) {
            			throw new IOException("Wasup request succeed, but the response was empty");
            		}
            		
            		ret = Integer.parseInt(nodeId.item(0).getTextContent());
            	} catch (NumberFormatException nfe) {
            		throw new IOException("Wasup request succeed, but the response could not be parsed: " + nfe.getMessage());
            	}
            }
            return ret;
        } else {
        	throw new IOException("The wasup request didn't succeed; wrong server response.");
        }
	}

	/**
	 * Removes the selected element.
	 * 
	 * @param id
	 * 		The element id.
	 * 
	 * @throws IOException
	 */
	public void removeElement(String id) throws IOException {
		log.info("Removing element: " + id + "from wasup");
		
		String uri = DATA_PATH + ELEMENTS + "/" + id;
		
		Response response = delete(uri);
		
		if (response.getStatus().isSuccess()) {
			log.info("Removed: " + id);
		} else {
			log.info("Element couldn't be erased: " + response.getStatus());
		}
	}
	
    /**
     * Static counter to create the ids for the element Types.
     */
    public static int idCounter=1;
	
	 /**
	  * Creates a new element type in wasup, under the element <i>parent</i>.
	  * 
	  * The folloging information is needed for the element creation:
	  * 
	  * 	- elementType.elementTypeId
	  * 	- name
	  * 	- description
	  * 
	  * @param name
	  * 	Name of the type to be created.
	  * 
	  * @param description
	  * 	Description of the created type.
	  * 
	  * @return
	  * 	The Wasup ID of the newly created element.
	  * 
	 * @throws IOException 
	 * @throws JSONException 
	 * 
	  */
	 public int createElementType(String name, String description) throws IOException, JSONException {
		
		log.info("Creating element type: " + name);
		Form form = new Form();  
		form.add("elementTypeId", String.valueOf(idCounter++));   
		form.add("name", name);   
		form.add("description", description);
		       
		Representation rep = form.getWebRepresentation();
		
		String uri = DATA_PATH + ELEMENT_TYPES + "/";
		       
		Response response = post(uri, rep);
		       
		if (response.getStatus().isSuccess()) {
			
			int ret=-1;
			if (response.getEntity().getMediaType().equals(MediaType.APPLICATION_JSON)) {
				JsonRepresentation jsonRep = new JsonRepresentation(response.getEntity());
				
				JSONObject jo = jsonRep.toJsonArray().getJSONObject(0);
	            int tableSize = jo.getInt("tableSize");
	            if (tableSize >= 1){
	                    JSONObject joElement = jo.getJSONArray("rows").getJSONObject(0);
	                    ret = joElement.getInt("elementTypeId");
	            }
            } else if (response.getEntity().getMediaType().equals(MediaType.TEXT_XML)) {
            	DomRepresentation domRep = new DomRepresentation(response.getEntity());
            	
            	Document doc = domRep.getDocument();
            	NodeList nodeId = doc.getElementsByTagName("elementTypeId");
            	
            	try {
            		if (nodeId.getLength() <= 0) {
            			throw new IOException("Wasup request succeed, but the response was empty");
            		}
            		
            		ret = Integer.parseInt(nodeId.item(0).getTextContent());
            	} catch (NumberFormatException nfe) {
            		throw new IOException("Wasup request succeed, but the response could not be parsed: " + nfe.getMessage());
            	}
            }
			return ret;
		} else {
			throw new IOException("The wasup request didn't succeed; wrong server response: " + response.getStatus() );
		}
	 }
	 
	 /**
	  * Create a measurement type associated to the provided element. For each measurement type
	  * a range for this type must also be created, or no measure will be accepted.
	  * 
	  * @param name
	  * 	Human readable name of the measurement type.
	  * 
	  * @param itemName
	  * 	Fully quallyfied name or identifier of the measurement type.
	  * 
	  * @param elementId
	  * 	Wasup id of the element associated to the measurement type.
	  * 
	  * @param unit
	  * 	Engeenering unit associated to this measure type (e.g. GB, MHz, etc)
	  * 
	  * @param description
	  * 	Brief description of the measure.
	  * 
	  * @return
	  * 	The Wasup Id of the newly created measure type.
	  * 
	  * @throws IOException
	  * @throws JSONException
	  */
	 public int createMeasurementType(String name, String itemName, String elementId, String unit, String description, 
			 						  String lowerBound, String upperBound) throws IOException, JSONException {
		 
		 	log.info("Creating measure type: " + name);
			Form form = new Form();  
  
			form.add("element.elementId", elementId);   
			form.add("name", name);   
			form.add("itemName", itemName);   
			form.add("valueType", "xs:decimal");   
			form.add("engineeringUnit", unit);   
			form.add("analogDigital", "D");   
			form.add("description", description); 
			form.add("minValue", lowerBound);   
			form.add("maxValue", upperBound);
			       
			Representation rep = form.getWebRepresentation();
			
			String uri = DATA_PATH + MEASUREMENT_TYPES + "/";
			       
			Response response = post(uri, rep);
			       
			if (response.getStatus().isSuccess()) {
				
				int ret=-1;
				if (response.getEntity().getMediaType().equals(MediaType.APPLICATION_JSON)) {
					JsonRepresentation jsonRep = new JsonRepresentation(response.getEntity());
					
					JSONObject jo = jsonRep.toJsonArray().getJSONObject(0);
		            int tableSize = jo.getInt("tableSize");
		            if (tableSize >= 1){
		                    JSONObject joElement = jo.getJSONArray("rows").getJSONObject(0);
		                    ret = joElement.getInt("measurementTypeId");
		            }
	            } else if (response.getEntity().getMediaType().equals(MediaType.TEXT_XML)) {
	            	DomRepresentation domRep = new DomRepresentation(response.getEntity());
	            	
	            	Document doc = domRep.getDocument();
	            	NodeList nodeId = doc.getElementsByTagName("measurementTypeId");
	            	
	            	try {
	            		if (nodeId.getLength() <= 0) {
	            			throw new IOException("Wasup request succeed, but the response was empty");
	            		}
	            		
	            		ret = Integer.parseInt(nodeId.item(0).getTextContent());
	            	} catch (NumberFormatException nfe) {
	            		throw new IOException("Wasup request succeed, but the response could not be parsed: " + nfe.getMessage());
	            	}
	            }
				return ret;
			} else {
				throw new IOException("The wasup request didn't succeed; wrong server response: " + response.getStatus() );
			}
	 }
	 
	 /**
	  * Create a new measurement range associated to the given measureTypeId. The range is
	  * defined with its lower and upper bounds. 
	  * 
	  * @param measureTypeId
	  * 	Wasup id of the measure type associated to this range.
	  * 
	  * @param lowerBound
	  * 	String representation of the double value acting as lower bound.
	  * 
	  * @param upperBound
	  * 	String representation of the double value acting as the upper bound.
	  * 
	  * @throws IOException
	  * @throws JSONException
	  */
	 public void createMeasurementRange(String measureTypeId, String lowerBound, String upperBound)  throws IOException, JSONException {
		 
		 	log.info("Creating range for measurement type: " + measureTypeId);
			Form form = new Form();  

			form.add("registerRangeId", measureTypeId);   
			form.add("measurementType.measurementTypeId", measureTypeId);   
			form.add("lowerLimit", lowerBound);   
			form.add("upperLimit", upperBound);   
			       
			Representation rep = form.getWebRepresentation();
			
			String uri = DATA_PATH + MEASUREMENT_RANGES + "/";
			       
			Response response = post(uri, rep);
			       
			if (response.getStatus().isSuccess()) {
				log.info("Measurement range successfully created.");
			} else {
				throw new IOException("The wasup request didn't succeed; wrong server response: " + response.getStatus() );
			}
	 }
	 
	 /**
	  * Post a measure of the selected measure type to Wasup.
	  * 
	  * @param name
	  * 	Human readable name of the measurement type.
	  * 
	  * @param itemName
	  * 	Fully quallyfied name or identifier of the measurement type.
	  * 
	  * @throws IOException
	  * @throws JSONException
	  */
	 public void postMeasure(String measureType, String value) throws IOException, JSONException {
		 
		 	log.info("Posting measure for type: " + measureType + " value: " + value);
			Form form = new Form();  

			form.add("measurementType.itemName", measureType);   
			form.add("qualityType.qualityTypeId", "192");   
			form.add("value", value);   
			       
			Representation rep = form.getWebRepresentation();
			
			String uri = DATA_PATH + MEASURED_VALUES + "/";
			       
			Response response = post(uri, rep);
			       
			if (response.getStatus().isSuccess()) {
				log.info("Measure posted to Wasup");
			} else {
				log.warn("Wasup rejected the measure. Reason: " + response.getStatus());
			}
	 }
} 
