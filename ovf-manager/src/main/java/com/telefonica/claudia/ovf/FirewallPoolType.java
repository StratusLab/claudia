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


package com.telefonica.claudia.ovf;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FirewallPool_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FirewallPool_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddressIP" type="{http://schemas.telefonica.com/claudia/ovf}IPAddress_Type"/>
 *         &lt;element name="Netmask" type="{http://schemas.telefonica.com/claudia/ovf}IPAddress_Type"/>
 *         &lt;element name="Protocol" type="{http://schemas.telefonica.com/claudia/ovf}Protocol_Type"/>
 *         &lt;element name="ExternalPortStart" type="{http://schemas.telefonica.com/claudia/ovf}Port_Type" minOccurs="0"/>
 *         &lt;element name="ExternalPortEnd" type="{http://schemas.telefonica.com/claudia/ovf}Port_Type" minOccurs="0"/>
 *         &lt;element name="InternalPortStart" type="{http://schemas.telefonica.com/claudia/ovf}Port_Type" minOccurs="0"/>
 *         &lt;element name="InternalPortEnd" type="{http://schemas.telefonica.com/claudia/ovf}Port_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="policy" use="required" type="{http://schemas.telefonica.com/claudia/ovf}FirewallPolicy_Type" />
 *       &lt;attribute name="timeout_dns" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="timeout_icpm" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="timeout_pptp" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="timeout_dinamic_nat" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="timeout_udp_flow" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="timeout_tcp_flow" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirewallPool_Type", propOrder = {
    "addressIP",
    "netmask",
    "protocol",
    "externalPortStart",
    "externalPortEnd",
    "internalPortStart",
    "internalPortEnd"
})
public class FirewallPoolType {

    @XmlElement(name = "AddressIP", required = true)
    protected String addressIP;
    @XmlElement(name = "Netmask", required = true)
    protected String netmask;
    @XmlElement(name = "Protocol", required = true)
    protected ProtocolType protocol;
    @XmlElement(name = "ExternalPortStart")
    protected Integer externalPortStart;
    @XmlElement(name = "ExternalPortEnd")
    protected Integer externalPortEnd;
    @XmlElement(name = "InternalPortStart")
    protected Integer internalPortStart;
    @XmlElement(name = "InternalPortEnd")
    protected Integer internalPortEnd;
    @XmlAttribute(name = "policy", required = true)
    protected FirewallPolicyType policy;
    @XmlAttribute(name = "timeout_dns")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger timeoutDns;
    @XmlAttribute(name = "timeout_icpm")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger timeoutIcpm;
    @XmlAttribute(name = "timeout_pptp")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger timeoutPptp;
    @XmlAttribute(name = "timeout_dinamic_nat")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger timeoutDinamicNat;
    @XmlAttribute(name = "timeout_udp_flow")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger timeoutUdpFlow;
    @XmlAttribute(name = "timeout_tcp_flow")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger timeoutTcpFlow;

    /**
     * Gets the value of the addressIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressIP() {
        return addressIP;
    }

    /**
     * Sets the value of the addressIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressIP(String value) {
        this.addressIP = value;
    }

    /**
     * Gets the value of the netmask property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetmask() {
        return netmask;
    }

    /**
     * Sets the value of the netmask property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetmask(String value) {
        this.netmask = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link ProtocolType }
     *     
     */
    public ProtocolType getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProtocolType }
     *     
     */
    public void setProtocol(ProtocolType value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the externalPortStart property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExternalPortStart() {
        return externalPortStart;
    }

    /**
     * Sets the value of the externalPortStart property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExternalPortStart(Integer value) {
        this.externalPortStart = value;
    }

    /**
     * Gets the value of the externalPortEnd property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExternalPortEnd() {
        return externalPortEnd;
    }

    /**
     * Sets the value of the externalPortEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExternalPortEnd(Integer value) {
        this.externalPortEnd = value;
    }

    /**
     * Gets the value of the internalPortStart property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInternalPortStart() {
        return internalPortStart;
    }

    /**
     * Sets the value of the internalPortStart property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInternalPortStart(Integer value) {
        this.internalPortStart = value;
    }

    /**
     * Gets the value of the internalPortEnd property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInternalPortEnd() {
        return internalPortEnd;
    }

    /**
     * Sets the value of the internalPortEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInternalPortEnd(Integer value) {
        this.internalPortEnd = value;
    }

    /**
     * Gets the value of the policy property.
     * 
     * @return
     *     possible object is
     *     {@link FirewallPolicyType }
     *     
     */
    public FirewallPolicyType getPolicy() {
        return policy;
    }

    /**
     * Sets the value of the policy property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirewallPolicyType }
     *     
     */
    public void setPolicy(FirewallPolicyType value) {
        this.policy = value;
    }

    /**
     * Gets the value of the timeoutDns property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTimeoutDns() {
        return timeoutDns;
    }

    /**
     * Sets the value of the timeoutDns property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTimeoutDns(BigInteger value) {
        this.timeoutDns = value;
    }

    /**
     * Gets the value of the timeoutIcpm property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTimeoutIcpm() {
        return timeoutIcpm;
    }

    /**
     * Sets the value of the timeoutIcpm property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTimeoutIcpm(BigInteger value) {
        this.timeoutIcpm = value;
    }

    /**
     * Gets the value of the timeoutPptp property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTimeoutPptp() {
        return timeoutPptp;
    }

    /**
     * Sets the value of the timeoutPptp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTimeoutPptp(BigInteger value) {
        this.timeoutPptp = value;
    }

    /**
     * Gets the value of the timeoutDinamicNat property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTimeoutDinamicNat() {
        return timeoutDinamicNat;
    }

    /**
     * Sets the value of the timeoutDinamicNat property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTimeoutDinamicNat(BigInteger value) {
        this.timeoutDinamicNat = value;
    }

    /**
     * Gets the value of the timeoutUdpFlow property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTimeoutUdpFlow() {
        return timeoutUdpFlow;
    }

    /**
     * Sets the value of the timeoutUdpFlow property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTimeoutUdpFlow(BigInteger value) {
        this.timeoutUdpFlow = value;
    }

    /**
     * Gets the value of the timeoutTcpFlow property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTimeoutTcpFlow() {
        return timeoutTcpFlow;
    }

    /**
     * Sets the value of the timeoutTcpFlow property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTimeoutTcpFlow(BigInteger value) {
        this.timeoutTcpFlow = value;
    }

}
