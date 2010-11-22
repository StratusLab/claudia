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
 * The Initial Developer of the Original Code is Telefónica Investigación y Desarrollo S.A.U.,
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
// Generated on: 2010.01.26 at 07:51:55 PM CET 
//


package com.telefonica.claudia.ovf;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.dmtf.schemas.ovf.envelope._1.SectionType;


/**
 * <p>Java class for AvailabilitySection_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AvailabilitySection_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.dmtf.org/ovf/envelope/1}Section_Type">
 *       &lt;sequence>
 *         &lt;element name="Window" type="{http://schemas.telefonica.com/claudia/ovf}Window_Type"/>
 *         &lt;element name="Percentile" type="{http://schemas.telefonica.com/claudia/ovf}Percentage_Type"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AvailabilitySection_Type", propOrder = {
    "window",
    "percentile"
})
public class AvailabilitySectionType
    extends SectionType
{

    @XmlElement(name = "Window", required = true)
    protected WindowType window;
    @XmlElement(name = "Percentile", required = true)
    protected BigDecimal percentile;

    /**
     * Gets the value of the window property.
     * 
     * @return
     *     possible object is
     *     {@link WindowType }
     *     
     */
    public WindowType getWindow() {
        return window;
    }

    /**
     * Sets the value of the window property.
     * 
     * @param value
     *     allowed object is
     *     {@link WindowType }
     *     
     */
    public void setWindow(WindowType value) {
        this.window = value;
    }

    /**
     * Gets the value of the percentile property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPercentile() {
        return percentile;
    }

    /**
     * Sets the value of the percentile property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPercentile(BigDecimal value) {
        this.percentile = value;
    }

}
