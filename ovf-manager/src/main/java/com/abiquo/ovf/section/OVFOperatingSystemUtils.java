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
 * Contributor(s): ______________________________________.
 *
 * Graphical User Interface of this software may be used under the terms
 * of the Common Public Attribution License Version 1.0 (the  "CPAL License",
 * available at http://cpal.abiquo.com), in which case the provisions of CPAL
 * License are applicable instead of those above. In relation of this portions
 * of the Code, a Legal Notice according to Exhibits A and B of CPAL Licence
 * should be provided in any distribution of the corresponding Code to Graphical
 * User Interface.
 */

package com.abiquo.ovf.section;

import org.dmtf.schemas.ovf.envelope._1.OperatingSystemSectionType;

import com.abiquo.cim.CIMTypesUtils;
import com.abiquo.cim.CIMTypesUtils.OperatingSystemTypeEnum;
import com.abiquo.ovf.exceptions.RequiredAttributeException;

public class OVFOperatingSystemUtils
{
    
    /**
     * 
     * VERSION: A string describing the Operating System's version number. The format of the version information is as follows: <Major Number>.<Minor Number>.<Revision> or <Major Number>.<Minor Number>.<Revision Letter>.
     * */
    public static OperatingSystemSectionType createOperatingSystemSection(OperatingSystemTypeEnum osType, String version, String description, String info) throws RequiredAttributeException
    {
        // TODO required
        
        OperatingSystemSectionType ossection = new OperatingSystemSectionType();
     
        
        if(osType == null)
        {
            throw new RequiredAttributeException("Operating System Id for OperatingSystemSection");
        }
        
        ossection.setInfo(CIMTypesUtils.createMsg(info, null)); // TODO info message id
        
        ossection.setId(osType.getNumericOSType());
        ossection.setDescription(CIMTypesUtils.createMsg(description, null)); // TODO message id
        // TODO if description == null set the enumeration name               
        ossection.setVersion(version); // TODO validate version format ??
        
        return ossection;
    }
    

}
