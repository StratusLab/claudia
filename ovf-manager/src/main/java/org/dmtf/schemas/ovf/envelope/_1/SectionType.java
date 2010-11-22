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


package org.dmtf.schemas.ovf.envelope._1;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import com.telefonica.claudia.ovf.AffinitySectionType;
import com.telefonica.claudia.ovf.AspectsSectionType;
import com.telefonica.claudia.ovf.DeploymentSectionType;
import com.telefonica.claudia.ovf.DiskMappingSectionType;
import com.telefonica.claudia.ovf.ElasticArraySectionType;
import com.telefonica.claudia.ovf.FirewallSectionType;
import com.telefonica.claudia.ovf.KPISectionType;
import com.telefonica.claudia.ovf.LoadBalancerSectionType;


/**
 * <p>Java class for Section_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Section_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Info" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://schemas.dmtf.org/ovf/envelope/1}required"/>
 *       &lt;anyAttribute processContents='lax'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Section_Type", propOrder = {
    "info"
})
@XmlSeeAlso({
    ElasticArraySectionType.class,
    AspectsSectionType.class,
    FirewallSectionType.class,
    DiskMappingSectionType.class,
    AffinitySectionType.class,
    LoadBalancerSectionType.class,
    DeploymentSectionType.class,
    KPISectionType.class,
    DeploymentOptionSectionType.class,
    StartupSectionType.class,
    InstallSectionType.class,
    OperatingSystemSectionType.class,
    AnnotationSectionType.class,
    NetworkSectionType.class,
    EulaSectionType.class,
    ProductSectionType.class,
    VirtualHardwareSectionType.class,
    ResourceAllocationSectionType.class,
    DiskSectionType.class
})
public abstract class SectionType {

    @XmlElement(name = "Info", required = true)
    protected MsgType info;
    @XmlAttribute(name = "required", namespace = "http://schemas.dmtf.org/ovf/envelope/1")
    protected Boolean required;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the info property.
     * 
     * @return
     *     possible object is
     *     {@link MsgType }
     *     
     */
    public MsgType getInfo() {
        return info;
    }

    /**
     * Sets the value of the info property.
     * 
     * @param value
     *     allowed object is
     *     {@link MsgType }
     *     
     */
    public void setInfo(MsgType value) {
        this.info = value;
    }

    /**
     * Gets the value of the required property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isRequired() {
        if (required == null) {
            return true;
        } else {
            return required;
        }
    }

    /**
     * Sets the value of the required property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequired(Boolean value) {
        this.required = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
