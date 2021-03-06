//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.12 at 03:51:35 PM CET 
//


package org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_softwarefeature;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.dmtf.schemas.wbem.wscim._1.common.CimDateTime;
import org.dmtf.schemas.wbem.wscim._1.common.CimString;


/**
 * <p>Java class for CIM_SoftwareFeature_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CIM_SoftwareFeature_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}Caption" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}CommunicationStatus" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}Description" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}DetailedStatus" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}ElementName" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}HealthState" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}IdentifyingNumber"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}InstallDate" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}InstanceID" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}Name"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}OperatingStatus" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}OperationalStatus" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}PrimaryStatus" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}ProductName"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}Status" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}StatusDescriptions" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}Vendor"/>
 *         &lt;element ref="{http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareFeature}Version"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CIM_SoftwareFeature_Type", propOrder = {
    "caption",
    "communicationStatus",
    "description",
    "detailedStatus",
    "elementName",
    "healthState",
    "identifyingNumber",
    "installDate",
    "instanceID",
    "name",
    "operatingStatus",
    "operationalStatus",
    "primaryStatus",
    "productName",
    "status",
    "statusDescriptions",
    "vendor",
    "version"
})
@XmlSeeAlso({
    com.telefonica.schemas.nuba_model.exp.CIMSoftwareFeatureType.class
})
public class CIMSoftwareFeatureType {

    @XmlElement(name = "Caption", nillable = true)
    protected Caption caption;
    @XmlElement(name = "CommunicationStatus", nillable = true)
    protected CommunicationStatus communicationStatus;
    @XmlElement(name = "Description", nillable = true)
    protected CimString description;
    @XmlElement(name = "DetailedStatus", nillable = true)
    protected DetailedStatus detailedStatus;
    @XmlElement(name = "ElementName", nillable = true)
    protected CimString elementName;
    @XmlElement(name = "HealthState", nillable = true)
    protected HealthState healthState;
    @XmlElement(name = "IdentifyingNumber", required = true)
    protected IdentifyingNumber identifyingNumber;
    @XmlElement(name = "InstallDate", nillable = true)
    protected CimDateTime installDate;
    @XmlElement(name = "InstanceID", nillable = true)
    protected CimString instanceID;
    @XmlElement(name = "Name", required = true)
    protected Name name;
    @XmlElement(name = "OperatingStatus", nillable = true)
    protected OperatingStatus operatingStatus;
    @XmlElement(name = "OperationalStatus", nillable = true)
    protected List<OperationalStatus> operationalStatus;
    @XmlElement(name = "PrimaryStatus", nillable = true)
    protected PrimaryStatus primaryStatus;
    @XmlElement(name = "ProductName", required = true)
    protected ProductName productName;
    @XmlElement(name = "Status", nillable = true)
    protected Status status;
    @XmlElement(name = "StatusDescriptions", nillable = true)
    protected List<CimString> statusDescriptions;
    @XmlElement(name = "Vendor", required = true)
    protected Vendor vendor;
    @XmlElement(name = "Version", required = true)
    protected Version version;

    /**
     * Gets the value of the caption property.
     * 
     * @return
     *     possible object is
     *     {@link Caption }
     *     
     */
    public Caption getCaption() {
        return caption;
    }

    /**
     * Sets the value of the caption property.
     * 
     * @param value
     *     allowed object is
     *     {@link Caption }
     *     
     */
    public void setCaption(Caption value) {
        this.caption = value;
    }

    /**
     * Gets the value of the communicationStatus property.
     * 
     * @return
     *     possible object is
     *     {@link CommunicationStatus }
     *     
     */
    public CommunicationStatus getCommunicationStatus() {
        return communicationStatus;
    }

    /**
     * Sets the value of the communicationStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommunicationStatus }
     *     
     */
    public void setCommunicationStatus(CommunicationStatus value) {
        this.communicationStatus = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link CimString }
     *     
     */
    public CimString getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link CimString }
     *     
     */
    public void setDescription(CimString value) {
        this.description = value;
    }

    /**
     * Gets the value of the detailedStatus property.
     * 
     * @return
     *     possible object is
     *     {@link DetailedStatus }
     *     
     */
    public DetailedStatus getDetailedStatus() {
        return detailedStatus;
    }

    /**
     * Sets the value of the detailedStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailedStatus }
     *     
     */
    public void setDetailedStatus(DetailedStatus value) {
        this.detailedStatus = value;
    }

    /**
     * Gets the value of the elementName property.
     * 
     * @return
     *     possible object is
     *     {@link CimString }
     *     
     */
    public CimString getElementName() {
        return elementName;
    }

    /**
     * Sets the value of the elementName property.
     * 
     * @param value
     *     allowed object is
     *     {@link CimString }
     *     
     */
    public void setElementName(CimString value) {
        this.elementName = value;
    }

    /**
     * Gets the value of the healthState property.
     * 
     * @return
     *     possible object is
     *     {@link HealthState }
     *     
     */
    public HealthState getHealthState() {
        return healthState;
    }

    /**
     * Sets the value of the healthState property.
     * 
     * @param value
     *     allowed object is
     *     {@link HealthState }
     *     
     */
    public void setHealthState(HealthState value) {
        this.healthState = value;
    }

    /**
     * Gets the value of the identifyingNumber property.
     * 
     * @return
     *     possible object is
     *     {@link IdentifyingNumber }
     *     
     */
    public IdentifyingNumber getIdentifyingNumber() {
        return identifyingNumber;
    }

    /**
     * Sets the value of the identifyingNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentifyingNumber }
     *     
     */
    public void setIdentifyingNumber(IdentifyingNumber value) {
        this.identifyingNumber = value;
    }

    /**
     * Gets the value of the installDate property.
     * 
     * @return
     *     possible object is
     *     {@link CimDateTime }
     *     
     */
    public CimDateTime getInstallDate() {
        return installDate;
    }

    /**
     * Sets the value of the installDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link CimDateTime }
     *     
     */
    public void setInstallDate(CimDateTime value) {
        this.installDate = value;
    }

    /**
     * Gets the value of the instanceID property.
     * 
     * @return
     *     possible object is
     *     {@link CimString }
     *     
     */
    public CimString getInstanceID() {
        return instanceID;
    }

    /**
     * Sets the value of the instanceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link CimString }
     *     
     */
    public void setInstanceID(CimString value) {
        this.instanceID = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link Name }
     *     
     */
    public Name getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link Name }
     *     
     */
    public void setName(Name value) {
        this.name = value;
    }

    /**
     * Gets the value of the operatingStatus property.
     * 
     * @return
     *     possible object is
     *     {@link OperatingStatus }
     *     
     */
    public OperatingStatus getOperatingStatus() {
        return operatingStatus;
    }

    /**
     * Sets the value of the operatingStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperatingStatus }
     *     
     */
    public void setOperatingStatus(OperatingStatus value) {
        this.operatingStatus = value;
    }

    /**
     * Gets the value of the operationalStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operationalStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperationalStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OperationalStatus }
     * 
     * 
     */
    public List<OperationalStatus> getOperationalStatus() {
        if (operationalStatus == null) {
            operationalStatus = new ArrayList<OperationalStatus>();
        }
        return this.operationalStatus;
    }

    /**
     * Gets the value of the primaryStatus property.
     * 
     * @return
     *     possible object is
     *     {@link PrimaryStatus }
     *     
     */
    public PrimaryStatus getPrimaryStatus() {
        return primaryStatus;
    }

    /**
     * Sets the value of the primaryStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimaryStatus }
     *     
     */
    public void setPrimaryStatus(PrimaryStatus value) {
        this.primaryStatus = value;
    }

    /**
     * Gets the value of the productName property.
     * 
     * @return
     *     possible object is
     *     {@link ProductName }
     *     
     */
    public ProductName getProductName() {
        return productName;
    }

    /**
     * Sets the value of the productName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductName }
     *     
     */
    public void setProductName(ProductName value) {
        this.productName = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

    /**
     * Gets the value of the statusDescriptions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statusDescriptions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatusDescriptions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CimString }
     * 
     * 
     */
    public List<CimString> getStatusDescriptions() {
        if (statusDescriptions == null) {
            statusDescriptions = new ArrayList<CimString>();
        }
        return this.statusDescriptions;
    }

    /**
     * Gets the value of the vendor property.
     * 
     * @return
     *     possible object is
     *     {@link Vendor }
     *     
     */
    public Vendor getVendor() {
        return vendor;
    }

    /**
     * Sets the value of the vendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vendor }
     *     
     */
    public void setVendor(Vendor value) {
        this.vendor = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link Version }
     *     
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link Version }
     *     
     */
    public void setVersion(Version value) {
        this.version = value;
    }

}
