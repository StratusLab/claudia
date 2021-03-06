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
// Generated on: 2009.12.23 at 05:08:29 PM CET 
//


package org.dmtf.schemas.ovf.environment._1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.dmtf.schemas.ovf.environment._1 package. 
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

    private final static QName _Section_QNAME = new QName("http://schemas.dmtf.org/ovf/environment/1", "Section");
    private final static QName _Environment_QNAME = new QName("http://schemas.dmtf.org/ovf/environment/1", "Environment");
    private final static QName _PlatformSection_QNAME = new QName("http://schemas.dmtf.org/ovf/environment/1", "PlatformSection");
    private final static QName _PropertySection_QNAME = new QName("http://schemas.dmtf.org/ovf/environment/1", "PropertySection");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.dmtf.schemas.ovf.environment._1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EntityType }
     * 
     */
    public EntityType createEntityType() {
        return new EntityType();
    }

    /**
     * Create an instance of {@link PropertySectionType }
     * 
     */
    public PropertySectionType createPropertySectionType() {
        return new PropertySectionType();
    }

    /**
     * Create an instance of {@link PlatformSectionType }
     * 
     */
    public PlatformSectionType createPlatformSectionType() {
        return new PlatformSectionType();
    }

    /**
     * Create an instance of {@link PropertySectionType.Property }
     * 
     */
    public PropertySectionType.Property createPropertySectionTypeProperty() {
        return new PropertySectionType.Property();
    }

    /**
     * Create an instance of {@link EnvironmentType }
     * 
     */
    public EnvironmentType createEnvironmentType() {
        return new EnvironmentType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/ovf/environment/1", name = "Section")
    public JAXBElement<SectionType> createSection(SectionType value) {
        return new JAXBElement<SectionType>(_Section_QNAME, SectionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnvironmentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/ovf/environment/1", name = "Environment")
    public JAXBElement<EnvironmentType> createEnvironment(EnvironmentType value) {
        return new JAXBElement<EnvironmentType>(_Environment_QNAME, EnvironmentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PlatformSectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/ovf/environment/1", name = "PlatformSection", substitutionHeadNamespace = "http://schemas.dmtf.org/ovf/environment/1", substitutionHeadName = "Section")
    public JAXBElement<PlatformSectionType> createPlatformSection(PlatformSectionType value) {
        return new JAXBElement<PlatformSectionType>(_PlatformSection_QNAME, PlatformSectionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PropertySectionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.dmtf.org/ovf/environment/1", name = "PropertySection", substitutionHeadNamespace = "http://schemas.dmtf.org/ovf/environment/1", substitutionHeadName = "Section")
    public JAXBElement<PropertySectionType> createPropertySection(PropertySectionType value) {
        return new JAXBElement<PropertySectionType>(_PropertySection_QNAME, PropertySectionType.class, null, value);
    }

}
