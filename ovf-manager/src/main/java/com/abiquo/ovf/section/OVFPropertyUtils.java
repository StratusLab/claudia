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

package com.abiquo.ovf.section;

import javax.xml.namespace.QName;

import org.dmtf.schemas.ovf.envelope._1.NetworkSectionType.Network;
import org.dmtf.schemas.ovf.environment._1.PropertySectionType;
import org.dmtf.schemas.ovf.environment._1.PropertySectionType.Property;

import com.abiquo.ovf.exceptions.IdAlreadyExistsException;
import com.abiquo.ovf.exceptions.RequiredAttributeException;

public class OVFPropertyUtils {
	
	 /**
     * Key/value pairs of assigned properties for an entity
     */
	
	 public static PropertySectionType createPropertySection()
     {
         PropertySectionType psection = new PropertySectionType();

         return psection;
     }

     public static void addProperty(PropertySectionType psection, String key, String value)
         throws RequiredAttributeException, IdAlreadyExistsException
     {
         checkPropertyKey(psection, key);

         if (key == null || value == null)
         {
             throw new RequiredAttributeException("Id or value for Environment.PropretySection.Property");
         }

         Property property = new Property();
         property.setKey(key);
         property.setValue(value);

         psection.getProperty().add(property);
     }

     public static void checkPropertyKey(PropertySectionType psection, String propertyKey)
         throws IdAlreadyExistsException
     {
         for (Property property : psection.getProperty())
         {
             if (propertyKey.equals(property.getKey()))
             {
                 throw new IdAlreadyExistsException("PropertyKeys " + propertyKey);
             }
         }
     }

     /**
      * Set other attributes to PropertySection. Due there is no xsd attributes for network features
      * such as Gateway, range, netmask.. It's mandatory to create a function that will insert this
      * values in an auxiliar OtherAttributes Map.
      * 
      * @param netSection the <NetworkSection> we work with
      * @param key Key of the Map
      * @param value value of the key
      * @throws RequiredAttributeException if key or netSection are null throws this method
      * @throws IdAlreadyExistsException if key already inserted
      */
     public static void addOtherAttributes(Network net, QName key, String value) throws RequiredAttributeException, IdAlreadyExistsException
     {
         if (net == null || key == null)
         {
             throw new RequiredAttributeException("Some values are null!");
         }
         if (net.getOtherAttributes().get(key) != null)
         {
             throw new IdAlreadyExistsException("Key already exists");
         }
         net.getOtherAttributes().put(key, value);

     }

}
