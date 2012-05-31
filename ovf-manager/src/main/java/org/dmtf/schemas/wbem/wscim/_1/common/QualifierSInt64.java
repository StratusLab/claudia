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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.12.23 at 02:26:18 PM CET 
//


package org.dmtf.schemas.wbem.wscim._1.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for qualifierSInt64 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="qualifierSInt64">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://schemas.dmtf.org/wbem/wscim/1/common>cimLong">
 *       &lt;attribute ref="{http://schemas.dmtf.org/wbem/wscim/1/common}qualifier use="required""/>
 *       &lt;anyAttribute processContents='lax'/>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "qualifierSInt64")
public class QualifierSInt64
    extends CimLong
{

    @XmlAttribute(name = "qualifier", namespace = "http://schemas.dmtf.org/wbem/wscim/1/common", required = true)
    protected boolean qualifier;

    /**
     * Gets the value of the qualifier property.
     * 
     */
    public boolean isQualifier() {
        return qualifier;
    }

    /**
     * Sets the value of the qualifier property.
     * 
     */
    public void setQualifier(boolean value) {
        this.qualifier = value;
    }

}
