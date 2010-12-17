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
// Generated on: 2009.12.23 at 02:26:18 PM CET 
//


package org.dmtf.schemas.ovf.envelope._1;
//TODO Review element name="Value" of Property... not value1 !!!!

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.dmtf.schemas.wbem.wscim._1.common.CimString;
import org.w3c.dom.Element;


/**
 * Product information for a virtual
 *             appliance
 * 
 * <p>Java class for ProductSection_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProductSection_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.dmtf.org/ovf/envelope/1}Section_Type">
 *       &lt;sequence>
 *         &lt;element name="Product" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type" minOccurs="0"/>
 *         &lt;element name="Vendor" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type" minOccurs="0"/>
 *         &lt;element name="Version" type="{http://schemas.dmtf.org/wbem/wscim/1/common}cimString" minOccurs="0"/>
 *         &lt;element name="FullVersion" type="{http://schemas.dmtf.org/wbem/wscim/1/common}cimString" minOccurs="0"/>
 *         &lt;element name="ProductUrl" type="{http://schemas.dmtf.org/wbem/wscim/1/common}cimString" minOccurs="0"/>
 *         &lt;element name="VendorUrl" type="{http://schemas.dmtf.org/wbem/wscim/1/common}cimString" minOccurs="0"/>
 *         &lt;element name="AppUrl" type="{http://schemas.dmtf.org/wbem/wscim/1/common}cimString" minOccurs="0"/>
 *         &lt;element name="Icon" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="height" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
 *                 &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
 *                 &lt;attribute name="mimeType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="fileRef" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;anyAttribute processContents='lax'/>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="Category" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type"/>
 *           &lt;element name="Property">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="Label" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type" minOccurs="0"/>
 *                     &lt;element name="Description" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type" minOccurs="0"/>
 *                     &lt;element name="Value" type="{http://schemas.dmtf.org/ovf/envelope/1}PropertyConfigurationValue_Type" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;/sequence>
 *                   &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="qualifiers" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="userConfigurable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                   &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" default="" />
 *                   &lt;anyAttribute processContents='lax'/>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" default="" />
 *       &lt;attribute name="instance" type="{http://www.w3.org/2001/XMLSchema}string" default="" />
 *       &lt;anyAttribute processContents='lax'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductSection_Type", propOrder = {
    "product",
    "vendor",
    "version",
    "fullVersion",
    "productUrl",
    "vendorUrl",
    "appUrl",
    "icon",
    "categoryOrProperty",
    "any"
})
public class ProductSectionType
    extends SectionType
{

    @XmlElement(name = "Product")
    protected MsgType product;
    @XmlElement(name = "Vendor")
    protected MsgType vendor;
    @XmlElement(name = "Version")
    protected CimString version;
    @XmlElement(name = "FullVersion")
    protected CimString fullVersion;
    @XmlElement(name = "ProductUrl")
    protected CimString productUrl;
    @XmlElement(name = "VendorUrl")
    protected CimString vendorUrl;
    @XmlElement(name = "AppUrl")
    protected CimString appUrl;
    @XmlElement(name = "Icon")
    protected List<ProductSectionType.Icon> icon;
    @XmlElements({
        @XmlElement(name = "Category", type = MsgType.class),
        @XmlElement(name = "Property", type = ProductSectionType.Property.class)
    })
    protected List<Object> categoryOrProperty;
    @XmlAnyElement(lax = true)
    protected List<Object> any;
    @XmlAttribute(name = "class", namespace = "http://schemas.dmtf.org/ovf/envelope/1")
    protected String clazz;
    @XmlAttribute(name = "instance", namespace = "http://schemas.dmtf.org/ovf/envelope/1")
    protected String instance;

    /**
     * Gets the value of the product property.
     * 
     * @return
     *     possible object is
     *     {@link MsgType }
     *     
     */
    public MsgType getProduct() {
        return product;
    }

    /**
     * Sets the value of the product property.
     * 
     * @param value
     *     allowed object is
     *     {@link MsgType }
     *     
     */
    public void setProduct(MsgType value) {
        this.product = value;
    }

    /**
     * Gets the value of the vendor property.
     * 
     * @return
     *     possible object is
     *     {@link MsgType }
     *     
     */
    public MsgType getVendor() {
        return vendor;
    }

    /**
     * Sets the value of the vendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link MsgType }
     *     
     */
    public void setVendor(MsgType value) {
        this.vendor = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link CimString }
     *     
     */
    public CimString getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link CimString }
     *     
     */
    public void setVersion(CimString value) {
        this.version = value;
    }

    /**
     * Gets the value of the fullVersion property.
     * 
     * @return
     *     possible object is
     *     {@link CimString }
     *     
     */
    public CimString getFullVersion() {
        return fullVersion;
    }

    /**
     * Sets the value of the fullVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link CimString }
     *     
     */
    public void setFullVersion(CimString value) {
        this.fullVersion = value;
    }

    /**
     * Gets the value of the productUrl property.
     * 
     * @return
     *     possible object is
     *     {@link CimString }
     *     
     */
    public CimString getProductUrl() {
        return productUrl;
    }

    /**
     * Sets the value of the productUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link CimString }
     *     
     */
    public void setProductUrl(CimString value) {
        this.productUrl = value;
    }

    /**
     * Gets the value of the vendorUrl property.
     * 
     * @return
     *     possible object is
     *     {@link CimString }
     *     
     */
    public CimString getVendorUrl() {
        return vendorUrl;
    }

    /**
     * Sets the value of the vendorUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link CimString }
     *     
     */
    public void setVendorUrl(CimString value) {
        this.vendorUrl = value;
    }

    /**
     * Gets the value of the appUrl property.
     * 
     * @return
     *     possible object is
     *     {@link CimString }
     *     
     */
    public CimString getAppUrl() {
        return appUrl;
    }

    /**
     * Sets the value of the appUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link CimString }
     *     
     */
    public void setAppUrl(CimString value) {
        this.appUrl = value;
    }

    /**
     * Gets the value of the icon property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the icon property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIcon().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProductSectionType.Icon }
     * 
     * 
     */
    public List<ProductSectionType.Icon> getIcon() {
        if (icon == null) {
            icon = new ArrayList<ProductSectionType.Icon>();
        }
        return this.icon;
    }

    /**
     * Gets the value of the categoryOrProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the categoryOrProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategoryOrProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MsgType }
     * {@link ProductSectionType.Property }
     * 
     * 
     */
    public List<Object> getCategoryOrProperty() {
        if (categoryOrProperty == null) {
            categoryOrProperty = new ArrayList<Object>();
        }
        return this.categoryOrProperty;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClazz() {
        if (clazz == null) {
            return "";
        } else {
            return clazz;
        }
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClazz(String value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the instance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstance() {
        if (instance == null) {
            return "";
        } else {
            return instance;
        }
    }

    /**
     * Sets the value of the instance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstance(String value) {
        this.instance = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="height" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
     *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
     *       &lt;attribute name="mimeType" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="fileRef" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;anyAttribute processContents='lax'/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Icon {

        @XmlAttribute(name = "height", namespace = "http://schemas.dmtf.org/ovf/envelope/1")
        @XmlSchemaType(name = "unsignedShort")
        protected Integer height;
        @XmlAttribute(name = "width", namespace = "http://schemas.dmtf.org/ovf/envelope/1")
        @XmlSchemaType(name = "unsignedShort")
        protected Integer width;
        @XmlAttribute(name = "mimeType", namespace = "http://schemas.dmtf.org/ovf/envelope/1")
        protected String mimeType;
        @XmlAttribute(name = "fileRef", namespace = "http://schemas.dmtf.org/ovf/envelope/1", required = true)
        protected String fileRef;
        @XmlAnyAttribute
        private Map<QName, String> otherAttributes = new HashMap<QName, String>();

        /**
         * Gets the value of the height property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getHeight() {
            return height;
        }

        /**
         * Sets the value of the height property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setHeight(Integer value) {
            this.height = value;
        }

        /**
         * Gets the value of the width property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getWidth() {
            return width;
        }

        /**
         * Sets the value of the width property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setWidth(Integer value) {
            this.width = value;
        }

        /**
         * Gets the value of the mimeType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMimeType() {
            return mimeType;
        }

        /**
         * Sets the value of the mimeType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMimeType(String value) {
            this.mimeType = value;
        }

        /**
         * Gets the value of the fileRef property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFileRef() {
            return fileRef;
        }

        /**
         * Sets the value of the fileRef property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFileRef(String value) {
            this.fileRef = value;
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Label" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type" minOccurs="0"/>
     *         &lt;element name="Description" type="{http://schemas.dmtf.org/ovf/envelope/1}Msg_Type" minOccurs="0"/>
     *         &lt;element name="Value" type="{http://schemas.dmtf.org/ovf/envelope/1}PropertyConfigurationValue_Type" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="qualifiers" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="userConfigurable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" default="" />
     *       &lt;anyAttribute processContents='lax'/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "label",
        "description",
        "value1"
    })
    public static class Property {

        @XmlElement(name = "Label")
        protected MsgType label;
        @XmlElement(name = "Description")
        protected MsgType description;
        @XmlElement(name = "Value")
        protected List<PropertyConfigurationValueType> value1;
        @XmlAttribute(name = "key", namespace = "http://schemas.dmtf.org/ovf/envelope/1", required = true)
        protected String key;
        @XmlAttribute(name = "type", namespace = "http://schemas.dmtf.org/ovf/envelope/1", required = true)
        protected String type;
        @XmlAttribute(name = "qualifiers", namespace = "http://schemas.dmtf.org/ovf/envelope/1")
        protected String qualifiers;
        @XmlAttribute(name = "userConfigurable", namespace = "http://schemas.dmtf.org/ovf/envelope/1")
        protected Boolean userConfigurable;
        @XmlAttribute(name = "value", namespace = "http://schemas.dmtf.org/ovf/envelope/1")
        protected String value;
        @XmlAnyAttribute
        private Map<QName, String> otherAttributes = new HashMap<QName, String>();
        
         /**
         * Gets the value of the label property.
         * 
         * @return
         *     possible object is
         *     {@link MsgType }
         *     
         */
        public MsgType getLabel() {
            return label;
        }

        /**
         * Sets the value of the label property.
         * 
         * @param value
         *     allowed object is
         *     {@link MsgType }
         *     
         */
        public void setLabel(MsgType value) {
            this.label = value;
        }

        /**
         * Gets the value of the description property.
         * 
         * @return
         *     possible object is
         *     {@link MsgType }
         *     
         */
        public MsgType getDescription() {
            return description;
        }

        /**
         * Sets the value of the description property.
         * 
         * @param value
         *     allowed object is
         *     {@link MsgType }
         *     
         */
        public void setDescription(MsgType value) {
            this.description = value;
        }

        /**
         * Gets the value of the value property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the value property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getValue().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PropertyConfigurationValueType }
         * 
         * 
         */
        public List<PropertyConfigurationValueType> getValue1() {
            if (value1 == null) {
                value1 = new ArrayList<PropertyConfigurationValueType>();
            }
            return this.value1;
        }

        /**
         * Gets the value of the key property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKey() {
            return key;
        }

        /**
         * Sets the value of the key property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKey(String value) {
            this.key = value;
        }

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Gets the value of the qualifiers property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getQualifiers() {
            return qualifiers;
        }

        /**
         * Sets the value of the qualifiers property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setQualifiers(String value) {
            this.qualifiers = value;
        }

        /**
         * Gets the value of the userConfigurable property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isUserConfigurable() {
            if (userConfigurable == null) {
                return false;
            } else {
                return userConfigurable;
            }
        }

        /**
         * Sets the value of the userConfigurable property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setUserConfigurable(Boolean value) {
            this.userConfigurable = value;
        }

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            if (value == null) {
                return "";
            } else {
                return value;
            }
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
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

}