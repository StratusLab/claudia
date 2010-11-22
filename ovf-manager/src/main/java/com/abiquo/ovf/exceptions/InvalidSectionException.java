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

package com.abiquo.ovf.exceptions;

import org.dmtf.schemas.ovf.envelope._1.SectionType;

public class InvalidSectionException extends SectionException
{
	private static final long serialVersionUID = 5006493911366453340L;

	public InvalidSectionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidSectionException(String message)
    {
        super(message);
    }
    
    

    public InvalidSectionException(String artifact, Class<? extends SectionType> section)
    {
        super("Section "+section.getCanonicalName()+" not allowed on "+artifact);
    }
    
}