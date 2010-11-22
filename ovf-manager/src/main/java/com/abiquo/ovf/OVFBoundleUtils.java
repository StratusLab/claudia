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

package com.abiquo.ovf;

import org.dmtf.schemas.ovf.envelope._1.StringsType;

import com.abiquo.cim.CIMTypesUtils;
import com.abiquo.ovf.exceptions.RequiredAttributeException;

public class OVFBoundleUtils
{
    
    
    public static StringsType createString(String lang, String fileRef) throws RequiredAttributeException
    {
        StringsType strings = new StringsType();
     
        if(lang == null)
        {
            throw new RequiredAttributeException("Lang for Strings");
        }
        
        strings.setLang(lang);      // TODO from xml:lang namespace creates an enumeration ?¿?
        strings.setFileRef(fileRef); // TODO file exist ???
        
        return strings;
    }

    
    public static void addMessageToStrings(StringsType strings, String messageId, String value) throws RequiredAttributeException
    {
        // TODO id already exist 
        strings.getMsg().add(CIMTypesUtils.createMsgStrings(messageId, value));
    }
    
}
