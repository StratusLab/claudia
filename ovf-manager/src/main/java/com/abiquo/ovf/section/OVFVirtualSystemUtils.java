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

import org.dmtf.schemas.ovf.envelope._1.VirtualSystemType;

import com.abiquo.ovf.exceptions.IdAlreadyExistsException;
import com.abiquo.ovf.exceptions.IdNotFoundException;
import com.abiquo.ovf.exceptions.RequiredAttributeException;

public class OVFVirtualSystemUtils
{

   
    /**
     * @param vs the <VirtualSystem> we work with
     * @param key Key of the Map
     * @param value value of the key
     * @throws RequiredAttributeException if key or vs are null throws this method
     * @throws IdAlreadyExistsException if key already inserted
     */
    public static void addOtherAttributes(VirtualSystemType vs, QName key, String value) throws RequiredAttributeException, IdAlreadyExistsException
    {
        if (vs == null || key == null)
        {
            throw new RequiredAttributeException("Some values are null!");
        }
        if (vs.getOtherAttributes().get(key) != null)
        {
            throw new IdAlreadyExistsException("Key already exists");
        }
        vs.getOtherAttributes().put(key, value);

    }
    
    /**
     * Return an OtherAttributes value for a given key into the <VirtualSystem>
     * @param vs
     * @param key
     * @return
     * @throws RequiredAttributeException
     * @throws IdNotFoundException
     */
    public static String getOtherAttribute(VirtualSystemType vs, QName key) throws RequiredAttributeException, IdNotFoundException
    {
        if (vs == null || key == null)
        {
            throw new RequiredAttributeException("Some values are null!");
        }
        
        String value = vs.getOtherAttributes().get(key);
        
        if (value == null)
        {
            throw new IdNotFoundException("Key doesn't exist");
        }
     
        return value;
    }
}
