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

import org.dmtf.schemas.ovf.environment._1.PlatformSectionType;

import com.abiquo.cim.CIMTypesUtils;

public class OVFPlatformUtils {

	 /**
     * Creates a PlatformSection.
     * 
     * @param vendor, the optional Deployment platform vendor
     *@param version, the optional deployment platform version
     *@param kind, the optional deployment platform kind.
     *@param locale, the optional current locale
     *@param timeZone, the optional Current timezone offset in minutes from UTC. Time zones
     *            east of Greenwich are positive and time zones west of Greenwich are negative
     */
    public static PlatformSectionType createPlatformSection(String vendor, String version,
        String kind, String locale, Integer timeZone)
    {
        PlatformSectionType psection = new PlatformSectionType();

        psection.setKind(CIMTypesUtils.createString(kind));
        psection.setLocale(CIMTypesUtils.createString(locale));
        psection.setTimezone(timeZone);
        psection.setVendor(CIMTypesUtils.createString(vendor));
        psection.setVersion(CIMTypesUtils.createString(version));

        return null;
    }
}
