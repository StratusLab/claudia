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

package com.abiquo.ovf;

import java.io.File;

import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.environment._1.EnvironmentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * This class aims to assure the generated OVF documents are OVFv1 standard compliant. <b>
 * This methods take a full OVF package (envelope and environment) to check all the referenced properties are somewhere defined, so construction
 * process guarantees the OVF is an XML wellformed and schema compliant document TODO 
 * <b>
 * TODO : see clusule 10 Internationalization on the OVF specification document.
 * TODO: provide methods to check one specific configuration on the enviromnent.
 */
public class OVFValidation
{
    
 private final static Logger log = LoggerFactory.getLogger(OVFValidation.class);
    

    // TODO all checks
    public static boolean checkOVF(EnvelopeType envelope, EnvironmentType environment)
    {
        return false;
    }

    private static boolean checkBoundMessagesId()
    {
        // TODO fing on the Strings the id of all the
        return false;
    }

    private static boolean checkOVFProductPropertiesOnEnvironment(EnvelopeType envelope,
        EnvironmentType environment)
    {
        return false;
    }

    private static boolean checkFileReferences(EnvelopeType envelope, File ovfPackageDirectory)
    {
        //TODO ProductSectionIcon
        return false;
    }

   

}
