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
// Generated on: 2009.12.23 at 07:11:48 PM CET 
//


package com.telefonica.claudia.ovf;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Rule_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Rule_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="KPIName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Window" type="{http://schemas.telefonica.com/claudia/ovf}Window_Type"/>
 *         &lt;element name="Frequency" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="Quota" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="Tolerance" type="{http://schemas.telefonica.com/claudia/ovf}PositiveDecimal_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rule_Type", propOrder = {
    "kpiName",
    "window",
    "frequency",
    "quota",
    "tolerance",
    "kpitype"
})
public class RuleType {

    @XmlElement(name = "KPIName", required = true)
    protected String kpiName;
    @XmlElement(name = "Window", required = true)
    protected WindowType window;
    @XmlElement(name = "Frequency", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger frequency;
    @XmlElement(name = "Quota", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger quota;
    @XmlElement(name = "Tolerance")
    protected BigDecimal tolerance;
    @XmlElement(name = "KPIType", required = true)
    protected String kpiType;

    /**
     * Gets the value of the kpiName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKPIName() {
        return kpiName;
    }

    /**
     * Sets the value of the kpiName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKPIName(String value) {
        this.kpiName = value;
    }

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
     * Gets the value of the frequency property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFrequency() {
        return frequency;
    }

    /**
     * Sets the value of the frequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFrequency(BigInteger value) {
        this.frequency = value;
    }

    /**
     * Gets the value of the quota property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getQuota() {
        return quota;
    }

    /**
     * Sets the value of the quota property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setQuota(BigInteger value) {
        this.quota = value;
    }

    /**
     * Gets the value of the tolerance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTolerance() {
        return tolerance;
    }

    /**
     * Sets the value of the tolerance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTolerance(BigDecimal value) {
        this.tolerance = value;
    }
    
    public String getKPIType () {
        return kpiType;
    }


    public void setKPIType(String value) {
        this.kpiType = value;
    }

}
