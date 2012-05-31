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
// Generated on: 2009.12.23 at 07:11:48 PM CET 
//


package org.dmtf.schemas.ovf.envelope._1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
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
 * Root OVF descriptor type
 * 
 * <p>Java class for EnvelopeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EnvelopeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="References" type="{http://schemas.dmtf.org/ovf/envelope/1}References_Type"/>
 *         &lt;element ref="{http://schemas.dmtf.org/ovf/envelope/1}Section" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/ovf/envelope/1}Content"/>
 *         &lt;element name="Strings" type="{http://schemas.dmtf.org/ovf/envelope/1}Strings_Type" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnvelopeType", propOrder = {
    "references",
    "section",
    "content",
    "strings"
})
public class EnvelopeType {

    @XmlElement(name = "References", required = true)
    protected ReferencesType references;
    @XmlElementRef(name = "Section", namespace = "http://schemas.dmtf.org/ovf/envelope/1", type = JAXBElement.class)
    protected List<JAXBElement<? extends SectionType>> section;
    @XmlElementRef(name = "Content", namespace = "http://schemas.dmtf.org/ovf/envelope/1", type = JAXBElement.class)
    protected JAXBElement<? extends ContentType> content;
    @XmlElement(name = "Strings")
    protected List<StringsType> strings;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the references property.
     * 
     * @return
     *     possible object is
     *     {@link ReferencesType }
     *     
     */
    public ReferencesType getReferences() {
        return references;
    }

    /**
     * Sets the value of the references property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferencesType }
     *     
     */
    public void setReferences(ReferencesType value) {
        this.references = value;
    }

    /**
     * Package level meta-data Gets the value of the section property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the section property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ResourceAllocationSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link ProductSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link DeploymentSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link DiskSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link DeploymentOptionSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link SectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link NetworkSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link KPISectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link FirewallSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link AffinitySectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link VirtualHardwareSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link OperatingSystemSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link LoadBalancerSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link InstallSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link AnnotationSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link AspectsSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link DiskMappingSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElasticArraySectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link EulaSectionType }{@code >}
     * {@link JAXBElement }{@code <}{@link StartupSectionType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends SectionType>> getSection() {
        if (section == null) {
            section = new ArrayList<JAXBElement<? extends SectionType>>();
        }
        return this.section;
    }

    /**
     * Content: A VirtualSystem or a
     *                         VirtualSystemCollection
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ContentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link VirtualSystemType }{@code >}
     *     {@link JAXBElement }{@code <}{@link VirtualSystemCollectionType }{@code >}
     *     
     */
    public JAXBElement<? extends ContentType> getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ContentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link VirtualSystemType }{@code >}
     *     {@link JAXBElement }{@code <}{@link VirtualSystemCollectionType }{@code >}
     *     
     */
    public void setContent(JAXBElement<? extends ContentType> value) {
        this.content = ((JAXBElement<? extends ContentType> ) value);
    }

    /**
     * Gets the value of the strings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the strings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStrings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StringsType }
     * 
     * 
     */
    public List<StringsType> getStrings() {
        if (strings == null) {
            strings = new ArrayList<StringsType>();
        }
        return this.strings;
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
