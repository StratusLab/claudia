//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.12 at 03:51:35 PM CET 
//


package org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_applicationsystem;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.dmtf.schemas.wbem.wscim._1.common.CimDateTime;
import org.dmtf.schemas.wbem.wscim._1.common.CimString;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_applicationsystem package. 
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

    private final static QName _TransitioningToState_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "TransitioningToState");
    private final static QName _Status_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "Status");
    private final static QName _EnabledDefault_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "EnabledDefault");
    private final static QName _Description_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "Description");
    private final static QName _ElementName_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "ElementName");
    private final static QName _EnabledState_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "EnabledState");
    private final static QName _StartupTime_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "StartupTime");
    private final static QName _OperatingStatus_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "OperatingStatus");
    private final static QName _Distribution_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "Distribution");
    private final static QName _RequestedState_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "RequestedState");
    private final static QName _TimeOfLastStateChange_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "TimeOfLastStateChange");
    private final static QName _PrimaryOwnerName_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "PrimaryOwnerName");
    private final static QName _IdentifyingDescriptions_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "IdentifyingDescriptions");
    private final static QName _LastServingStatusUpdate_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "LastServingStatusUpdate");
    private final static QName _Roles_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "Roles");
    private final static QName _CommunicationStatus_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "CommunicationStatus");
    private final static QName _InstanceID_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "InstanceID");
    private final static QName _OtherIdentifyingInfo_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "OtherIdentifyingInfo");
    private final static QName _DetailedStatus_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "DetailedStatus");
    private final static QName _NameFormat_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "NameFormat");
    private final static QName _HealthState_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "HealthState");
    private final static QName _PrimaryOwnerContact_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "PrimaryOwnerContact");
    private final static QName _ServingStatus_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "ServingStatus");
    private final static QName _StatusDescriptions_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "StatusDescriptions");
    private final static QName _PrimaryStatus_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "PrimaryStatus");
    private final static QName _Caption_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "Caption");
    private final static QName _CIMApplicationSystem_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "CIM_ApplicationSystem");
    private final static QName _OperationalStatus_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "OperationalStatus");
    private final static QName _InstallDate_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "InstallDate");
    private final static QName _AvailableRequestedStates_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "AvailableRequestedStates");
    private final static QName _OtherEnabledState_QNAME = new QName("http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", "OtherEnabledState");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_applicationsystem
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EnabledState }
     * 
     */
    public EnabledState createEnabledState() {
        return new EnabledState();
    }

    /**
     * Create an instance of {@link Name }
     * 
     */
    public Name createName() {
        return new Name();
    }

    /**
     * Create an instance of {@link RequestStateChangeOUTPUT.ReturnValue }
     * 
     */
    public RequestStateChangeOUTPUT.ReturnValue createRequestStateChangeOUTPUTReturnValue() {
        return new RequestStateChangeOUTPUT.ReturnValue();
    }

    /**
     * Create an instance of {@link EnabledDefault }
     * 
     */
    public EnabledDefault createEnabledDefault() {
        return new EnabledDefault();
    }

    /**
     * Create an instance of {@link PrimaryOwnerContact }
     * 
     */
    public PrimaryOwnerContact createPrimaryOwnerContact() {
        return new PrimaryOwnerContact();
    }

    /**
     * Create an instance of {@link CommunicationStatus }
     * 
     */
    public CommunicationStatus createCommunicationStatus() {
        return new CommunicationStatus();
    }

    /**
     * Create an instance of {@link StopApplicationINPUT }
     * 
     */
    public StopApplicationINPUT createStopApplicationINPUT() {
        return new StopApplicationINPUT();
    }

    /**
     * Create an instance of {@link PrimaryStatus }
     * 
     */
    public PrimaryStatus createPrimaryStatus() {
        return new PrimaryStatus();
    }

    /**
     * Create an instance of {@link RequestStateChangeINPUT.RequestedState }
     * 
     */
    public RequestStateChangeINPUT.RequestedState createRequestStateChangeINPUTRequestedState() {
        return new RequestStateChangeINPUT.RequestedState();
    }

    /**
     * Create an instance of {@link HealthState }
     * 
     */
    public HealthState createHealthState() {
        return new HealthState();
    }

    /**
     * Create an instance of {@link RequestStateChangeINPUT }
     * 
     */
    public RequestStateChangeINPUT createRequestStateChangeINPUT() {
        return new RequestStateChangeINPUT();
    }

    /**
     * Create an instance of {@link CIMApplicationSystemType }
     * 
     */
    public CIMApplicationSystemType createCIMApplicationSystemType() {
        return new CIMApplicationSystemType();
    }

    /**
     * Create an instance of {@link StopApplicationOUTPUT.ReturnValue }
     * 
     */
    public StopApplicationOUTPUT.ReturnValue createStopApplicationOUTPUTReturnValue() {
        return new StopApplicationOUTPUT.ReturnValue();
    }

    /**
     * Create an instance of {@link StartApplicationOUTPUT.ReturnValue }
     * 
     */
    public StartApplicationOUTPUT.ReturnValue createStartApplicationOUTPUTReturnValue() {
        return new StartApplicationOUTPUT.ReturnValue();
    }

    /**
     * Create an instance of {@link Distribution }
     * 
     */
    public Distribution createDistribution() {
        return new Distribution();
    }

    /**
     * Create an instance of {@link PrimaryOwnerName }
     * 
     */
    public PrimaryOwnerName createPrimaryOwnerName() {
        return new PrimaryOwnerName();
    }

    /**
     * Create an instance of {@link DetailedStatus }
     * 
     */
    public DetailedStatus createDetailedStatus() {
        return new DetailedStatus();
    }

    /**
     * Create an instance of {@link ServingStatus }
     * 
     */
    public ServingStatus createServingStatus() {
        return new ServingStatus();
    }

    /**
     * Create an instance of {@link CreationClassName }
     * 
     */
    public CreationClassName createCreationClassName() {
        return new CreationClassName();
    }

    /**
     * Create an instance of {@link StartApplicationINPUT }
     * 
     */
    public StartApplicationINPUT createStartApplicationINPUT() {
        return new StartApplicationINPUT();
    }

    /**
     * Create an instance of {@link StopApplicationOUTPUT }
     * 
     */
    public StopApplicationOUTPUT createStopApplicationOUTPUT() {
        return new StopApplicationOUTPUT();
    }

    /**
     * Create an instance of {@link Caption }
     * 
     */
    public Caption createCaption() {
        return new Caption();
    }

    /**
     * Create an instance of {@link Status }
     * 
     */
    public Status createStatus() {
        return new Status();
    }

    /**
     * Create an instance of {@link StartApplicationOUTPUT }
     * 
     */
    public StartApplicationOUTPUT createStartApplicationOUTPUT() {
        return new StartApplicationOUTPUT();
    }

    /**
     * Create an instance of {@link org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_applicationsystem.RequestedState }
     * 
     */
    public org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_applicationsystem.RequestedState createRequestedState() {
        return new org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_applicationsystem.RequestedState();
    }

    /**
     * Create an instance of {@link OtherIdentifyingInfo }
     * 
     */
    public OtherIdentifyingInfo createOtherIdentifyingInfo() {
        return new OtherIdentifyingInfo();
    }

    /**
     * Create an instance of {@link OperatingStatus }
     * 
     */
    public OperatingStatus createOperatingStatus() {
        return new OperatingStatus();
    }

    /**
     * Create an instance of {@link OperationalStatus }
     * 
     */
    public OperationalStatus createOperationalStatus() {
        return new OperationalStatus();
    }

    /**
     * Create an instance of {@link RequestStateChangeOUTPUT }
     * 
     */
    public RequestStateChangeOUTPUT createRequestStateChangeOUTPUT() {
        return new RequestStateChangeOUTPUT();
    }

    /**
     * Create an instance of {@link NameFormat }
     * 
     */
    public NameFormat createNameFormat() {
        return new NameFormat();
    }

    /**
     * Create an instance of {@link AvailableRequestedStates }
     * 
     */
    public AvailableRequestedStates createAvailableRequestedStates() {
        return new AvailableRequestedStates();
    }

    /**
     * Create an instance of {@link TransitioningToState }
     * 
     */
    public TransitioningToState createTransitioningToState() {
        return new TransitioningToState();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransitioningToState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "TransitioningToState")
    public JAXBElement<TransitioningToState> createTransitioningToState(TransitioningToState value) {
        return new JAXBElement<TransitioningToState>(_TransitioningToState_QNAME, TransitioningToState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Status }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "Status")
    public JAXBElement<Status> createStatus(Status value) {
        return new JAXBElement<Status>(_Status_QNAME, Status.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnabledDefault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "EnabledDefault")
    public JAXBElement<EnabledDefault> createEnabledDefault(EnabledDefault value) {
        return new JAXBElement<EnabledDefault>(_EnabledDefault_QNAME, EnabledDefault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "Description")
    public JAXBElement<CimString> createDescription(CimString value) {
        return new JAXBElement<CimString>(_Description_QNAME, CimString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "ElementName")
    public JAXBElement<CimString> createElementName(CimString value) {
        return new JAXBElement<CimString>(_ElementName_QNAME, CimString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnabledState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "EnabledState")
    public JAXBElement<EnabledState> createEnabledState(EnabledState value) {
        return new JAXBElement<EnabledState>(_EnabledState_QNAME, EnabledState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimDateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "StartupTime")
    public JAXBElement<CimDateTime> createStartupTime(CimDateTime value) {
        return new JAXBElement<CimDateTime>(_StartupTime_QNAME, CimDateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperatingStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "OperatingStatus")
    public JAXBElement<OperatingStatus> createOperatingStatus(OperatingStatus value) {
        return new JAXBElement<OperatingStatus>(_OperatingStatus_QNAME, OperatingStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Distribution }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "Distribution")
    public JAXBElement<Distribution> createDistribution(Distribution value) {
        return new JAXBElement<Distribution>(_Distribution_QNAME, Distribution.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_applicationsystem.RequestedState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "RequestedState")
    public JAXBElement<org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_applicationsystem.RequestedState> createRequestedState(org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_applicationsystem.RequestedState value) {
        return new JAXBElement<org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_applicationsystem.RequestedState>(_RequestedState_QNAME, org.dmtf.schemas.wbem.wscim._1.cim_schema._2.cim_applicationsystem.RequestedState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimDateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "TimeOfLastStateChange")
    public JAXBElement<CimDateTime> createTimeOfLastStateChange(CimDateTime value) {
        return new JAXBElement<CimDateTime>(_TimeOfLastStateChange_QNAME, CimDateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrimaryOwnerName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "PrimaryOwnerName")
    public JAXBElement<PrimaryOwnerName> createPrimaryOwnerName(PrimaryOwnerName value) {
        return new JAXBElement<PrimaryOwnerName>(_PrimaryOwnerName_QNAME, PrimaryOwnerName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "IdentifyingDescriptions")
    public JAXBElement<CimString> createIdentifyingDescriptions(CimString value) {
        return new JAXBElement<CimString>(_IdentifyingDescriptions_QNAME, CimString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimDateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "LastServingStatusUpdate")
    public JAXBElement<CimDateTime> createLastServingStatusUpdate(CimDateTime value) {
        return new JAXBElement<CimDateTime>(_LastServingStatusUpdate_QNAME, CimDateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "Roles")
    public JAXBElement<CimString> createRoles(CimString value) {
        return new JAXBElement<CimString>(_Roles_QNAME, CimString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommunicationStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "CommunicationStatus")
    public JAXBElement<CommunicationStatus> createCommunicationStatus(CommunicationStatus value) {
        return new JAXBElement<CommunicationStatus>(_CommunicationStatus_QNAME, CommunicationStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "InstanceID")
    public JAXBElement<CimString> createInstanceID(CimString value) {
        return new JAXBElement<CimString>(_InstanceID_QNAME, CimString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OtherIdentifyingInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "OtherIdentifyingInfo")
    public JAXBElement<OtherIdentifyingInfo> createOtherIdentifyingInfo(OtherIdentifyingInfo value) {
        return new JAXBElement<OtherIdentifyingInfo>(_OtherIdentifyingInfo_QNAME, OtherIdentifyingInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DetailedStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "DetailedStatus")
    public JAXBElement<DetailedStatus> createDetailedStatus(DetailedStatus value) {
        return new JAXBElement<DetailedStatus>(_DetailedStatus_QNAME, DetailedStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NameFormat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "NameFormat")
    public JAXBElement<NameFormat> createNameFormat(NameFormat value) {
        return new JAXBElement<NameFormat>(_NameFormat_QNAME, NameFormat.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HealthState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "HealthState")
    public JAXBElement<HealthState> createHealthState(HealthState value) {
        return new JAXBElement<HealthState>(_HealthState_QNAME, HealthState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrimaryOwnerContact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "PrimaryOwnerContact")
    public JAXBElement<PrimaryOwnerContact> createPrimaryOwnerContact(PrimaryOwnerContact value) {
        return new JAXBElement<PrimaryOwnerContact>(_PrimaryOwnerContact_QNAME, PrimaryOwnerContact.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServingStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "ServingStatus")
    public JAXBElement<ServingStatus> createServingStatus(ServingStatus value) {
        return new JAXBElement<ServingStatus>(_ServingStatus_QNAME, ServingStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "StatusDescriptions")
    public JAXBElement<CimString> createStatusDescriptions(CimString value) {
        return new JAXBElement<CimString>(_StatusDescriptions_QNAME, CimString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrimaryStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "PrimaryStatus")
    public JAXBElement<PrimaryStatus> createPrimaryStatus(PrimaryStatus value) {
        return new JAXBElement<PrimaryStatus>(_PrimaryStatus_QNAME, PrimaryStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Caption }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "Caption")
    public JAXBElement<Caption> createCaption(Caption value) {
        return new JAXBElement<Caption>(_Caption_QNAME, Caption.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CIMApplicationSystemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "CIM_ApplicationSystem")
    public JAXBElement<CIMApplicationSystemType> createCIMApplicationSystem(CIMApplicationSystemType value) {
        return new JAXBElement<CIMApplicationSystemType>(_CIMApplicationSystem_QNAME, CIMApplicationSystemType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperationalStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "OperationalStatus")
    public JAXBElement<OperationalStatus> createOperationalStatus(OperationalStatus value) {
        return new JAXBElement<OperationalStatus>(_OperationalStatus_QNAME, OperationalStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimDateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "InstallDate")
    public JAXBElement<CimDateTime> createInstallDate(CimDateTime value) {
        return new JAXBElement<CimDateTime>(_InstallDate_QNAME, CimDateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AvailableRequestedStates }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "AvailableRequestedStates")
    public JAXBElement<AvailableRequestedStates> createAvailableRequestedStates(AvailableRequestedStates value) {
        return new JAXBElement<AvailableRequestedStates>(_AvailableRequestedStates_QNAME, AvailableRequestedStates.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CimString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/wbem/wscim/1/cim-schema/2/CIM_ApplicationSystem", name = "OtherEnabledState")
    public JAXBElement<CimString> createOtherEnabledState(CimString value) {
        return new JAXBElement<CimString>(_OtherEnabledState_QNAME, CimString.class, null, value);
    }

}
