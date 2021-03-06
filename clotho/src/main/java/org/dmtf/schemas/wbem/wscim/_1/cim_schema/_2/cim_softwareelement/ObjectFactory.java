//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.12 at 03:51:35 PM CET 
//


package org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_softwareelement;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.dmtf.schemas.wbem.wscim._1.common.CimDateTime;
import org.dmtf.schemas.wbem.wscim._1.common.CimString;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_softwareelement package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _StatusDescriptions_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "StatusDescriptions");
    private final static QName _IdentificationCode_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "IdentificationCode");
    private final static QName _PrimaryStatus_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "PrimaryStatus");
    private final static QName _Caption_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "Caption");
    private final static QName _CodeSet_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "CodeSet");
    private final static QName _LanguageEdition_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "LanguageEdition");
    private final static QName _HealthState_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "HealthState");
    private final static QName _OtherTargetOS_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "OtherTargetOS");
    private final static QName _InstallDate_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "InstallDate");
    private final static QName _OperationalStatus_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "OperationalStatus");
    private final static QName _BuildNumber_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "BuildNumber");
    private final static QName _OperatingStatus_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "OperatingStatus");
    private final static QName _ElementName_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "ElementName");
    private final static QName _Status_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "Status");
    private final static QName _Description_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "Description");
    private final static QName _CIMSoftwareElement_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "CIM_SoftwareElement");
    private final static QName _DetailedStatus_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "DetailedStatus");
    private final static QName _InstanceID_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "InstanceID");
    private final static QName _CommunicationStatus_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "CommunicationStatus");
    private final static QName _SerialNumber_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "SerialNumber");
    private final static QName _Manufacturer_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", "Manufacturer");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_softwareelement
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Name }
     * 
     */
    public Name createName() {
        return new Name();
    }

    /**
     * Create an instance of {@link Caption }
     * 
     */
    public Caption createCaption() {
        return new Caption();
    }

    /**
     * Create an instance of {@link DetailedStatus }
     * 
     */
    public DetailedStatus createDetailedStatus() {
        return new DetailedStatus();
    }

    /**
     * Create an instance of {@link PrimaryStatus }
     * 
     */
    public PrimaryStatus createPrimaryStatus() {
        return new PrimaryStatus();
    }

    /**
     * Create an instance of {@link SoftwareElementID }
     * 
     */
    public SoftwareElementID createSoftwareElementID() {
        return new SoftwareElementID();
    }

    /**
     * Create an instance of {@link BuildNumber }
     * 
     */
    public BuildNumber createBuildNumber() {
        return new BuildNumber();
    }

    /**
     * Create an instance of {@link LanguageEdition }
     * 
     */
    public LanguageEdition createLanguageEdition() {
        return new LanguageEdition();
    }

    /**
     * Create an instance of {@link CIMSoftwareElementType }
     * 
     */
    public CIMSoftwareElementType createCIMSoftwareElementType() {
        return new CIMSoftwareElementType();
    }

    /**
     * Create an instance of {@link HealthState }
     * 
     */
    public HealthState createHealthState() {
        return new HealthState();
    }

    /**
     * Create an instance of {@link OperationalStatus }
     * 
     */
    public OperationalStatus createOperationalStatus() {
        return new OperationalStatus();
    }

    /**
     * Create an instance of {@link Status }
     * 
     */
    public Status createStatus() {
        return new Status();
    }

    /**
     * Create an instance of {@link CodeSet }
     * 
     */
    public CodeSet createCodeSet() {
        return new CodeSet();
    }

    /**
     * Create an instance of {@link TargetOperatingSystem }
     * 
     */
    public TargetOperatingSystem createTargetOperatingSystem() {
        return new TargetOperatingSystem();
    }

    /**
     * Create an instance of {@link Manufacturer }
     * 
     */
    public Manufacturer createManufacturer() {
        return new Manufacturer();
    }

    /**
     * Create an instance of {@link SoftwareElementState }
     * 
     */
    public SoftwareElementState createSoftwareElementState() {
        return new SoftwareElementState();
    }

    /**
     * Create an instance of {@link SerialNumber }
     * 
     */
    public SerialNumber createSerialNumber() {
        return new SerialNumber();
    }

    /**
     * Create an instance of {@link Version }
     * 
     */
    public Version createVersion() {
        return new Version();
    }

    /**
     * Create an instance of {@link IdentificationCode }
     * 
     */
    public IdentificationCode createIdentificationCode() {
        return new IdentificationCode();
    }

    /**
     * Create an instance of {@link OtherTargetOS }
     * 
     */
    public OtherTargetOS createOtherTargetOS() {
        return new OtherTargetOS();
    }

    /**
     * Create an instance of {@link CommunicationStatus }
     * 
     */
    public CommunicationStatus createCommunicationStatus() {
        return new CommunicationStatus();
    }

    /**
     * Create an instance of {@link OperatingStatus }
     * 
     */
    public OperatingStatus createOperatingStatus() {
        return new OperatingStatus();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "StatusDescriptions")
    public JAXBElement<CimString> createStatusDescriptions(CimString value) {
        return new JAXBElement<CimString>(_StatusDescriptions_QNAME, CimString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdentificationCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "IdentificationCode")
    public JAXBElement<IdentificationCode> createIdentificationCode(IdentificationCode value) {
        return new JAXBElement<IdentificationCode>(_IdentificationCode_QNAME, IdentificationCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrimaryStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "PrimaryStatus")
    public JAXBElement<PrimaryStatus> createPrimaryStatus(PrimaryStatus value) {
        return new JAXBElement<PrimaryStatus>(_PrimaryStatus_QNAME, PrimaryStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Caption }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "Caption")
    public JAXBElement<Caption> createCaption(Caption value) {
        return new JAXBElement<Caption>(_Caption_QNAME, Caption.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CodeSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "CodeSet")
    public JAXBElement<CodeSet> createCodeSet(CodeSet value) {
        return new JAXBElement<CodeSet>(_CodeSet_QNAME, CodeSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LanguageEdition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "LanguageEdition")
    public JAXBElement<LanguageEdition> createLanguageEdition(LanguageEdition value) {
        return new JAXBElement<LanguageEdition>(_LanguageEdition_QNAME, LanguageEdition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HealthState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "HealthState")
    public JAXBElement<HealthState> createHealthState(HealthState value) {
        return new JAXBElement<HealthState>(_HealthState_QNAME, HealthState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OtherTargetOS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "OtherTargetOS")
    public JAXBElement<OtherTargetOS> createOtherTargetOS(OtherTargetOS value) {
        return new JAXBElement<OtherTargetOS>(_OtherTargetOS_QNAME, OtherTargetOS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimDateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "InstallDate")
    public JAXBElement<CimDateTime> createInstallDate(CimDateTime value) {
        return new JAXBElement<CimDateTime>(_InstallDate_QNAME, CimDateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperationalStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "OperationalStatus")
    public JAXBElement<OperationalStatus> createOperationalStatus(OperationalStatus value) {
        return new JAXBElement<OperationalStatus>(_OperationalStatus_QNAME, OperationalStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuildNumber }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "BuildNumber")
    public JAXBElement<BuildNumber> createBuildNumber(BuildNumber value) {
        return new JAXBElement<BuildNumber>(_BuildNumber_QNAME, BuildNumber.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperatingStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "OperatingStatus")
    public JAXBElement<OperatingStatus> createOperatingStatus(OperatingStatus value) {
        return new JAXBElement<OperatingStatus>(_OperatingStatus_QNAME, OperatingStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "ElementName")
    public JAXBElement<CimString> createElementName(CimString value) {
        return new JAXBElement<CimString>(_ElementName_QNAME, CimString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Status }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "Status")
    public JAXBElement<Status> createStatus(Status value) {
        return new JAXBElement<Status>(_Status_QNAME, Status.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "Description")
    public JAXBElement<CimString> createDescription(CimString value) {
        return new JAXBElement<CimString>(_Description_QNAME, CimString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CIMSoftwareElementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "CIM_SoftwareElement")
    public JAXBElement<CIMSoftwareElementType> createCIMSoftwareElement(CIMSoftwareElementType value) {
        return new JAXBElement<CIMSoftwareElementType>(_CIMSoftwareElement_QNAME, CIMSoftwareElementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DetailedStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "DetailedStatus")
    public JAXBElement<DetailedStatus> createDetailedStatus(DetailedStatus value) {
        return new JAXBElement<DetailedStatus>(_DetailedStatus_QNAME, DetailedStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "InstanceID")
    public JAXBElement<CimString> createInstanceID(CimString value) {
        return new JAXBElement<CimString>(_InstanceID_QNAME, CimString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommunicationStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "CommunicationStatus")
    public JAXBElement<CommunicationStatus> createCommunicationStatus(CommunicationStatus value) {
        return new JAXBElement<CommunicationStatus>(_CommunicationStatus_QNAME, CommunicationStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SerialNumber }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "SerialNumber")
    public JAXBElement<SerialNumber> createSerialNumber(SerialNumber value) {
        return new JAXBElement<SerialNumber>(_SerialNumber_QNAME, SerialNumber.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Manufacturer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_SoftwareElement", name = "Manufacturer")
    public JAXBElement<Manufacturer> createManufacturer(Manufacturer value) {
        return new JAXBElement<Manufacturer>(_Manufacturer_QNAME, Manufacturer.class, null, value);
    }

}
