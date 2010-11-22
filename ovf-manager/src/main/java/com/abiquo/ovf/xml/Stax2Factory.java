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
 * The Original Code is available at https://abicloud.svn.sourceforge.net/svnroot/abicloud
 *
 * The Initial Developer of the Original Code is Soluciones Grid, S.L. (www.abiquo.com),
 * Consell de Cent 296 principal 2º, 08007 Barcelona, Spain.
 * No portions of the Code have been created by third parties.
 * All Rights Reserved.
 *
 * Contributor(s): ______________________________________.
 *
 * Graphical User Interface of this software may be used under the terms
 * of the Common Public Attribution License Version 1.0 (the  "CPAL License",
 * available at http://cpal.abiquo.com), in which case the provisions of CPAL
 * License are applicable instead of those above. In relation of this portions
 * of the Code, a Legal Notice according to Exhibits A and B of CPAL Licence
 * should be provided in any distribution of the corresponding Code to Graphical
 * User Interface.
 */

package com.abiquo.ovf.xml;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;

import com.ctc.wstx.api.WstxOutputProperties;
import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;

/**
 * Factories to configure the StAX implementation used on XML communication.
 * 
 * @see http://woodstox.codehaus.org/ (4.0 release notes) Default setting of IS_COALESCING *changed
 *      to false* * Default setting of WstxOutputProperties.P_OUTPUT_FIX_CONTENT *changed to false*
 *      * "no prefix" and "no namespace [URI]" have been changed from null value to empty String
 *      ("")
 */
public final class Stax2Factory
{
    /** The singleton instance. */
    private static Stax2Factory instance;

    /** Factory to obtain new StAX XML readers (implements XMLInputFactory). */
    private final WstxInputFactory inputFact;

    /** Factory to obtain new StAX XML writers (implements XMLOutputFactory). */
    private final WstxOutputFactory outputFact;

    /** Enable XMLStreamReader to uses name spaces. */
    private final boolean isNamespaceAware = true;

    /**
     * Property that determines whether Carriage Return (\r) characters are to be escaped when
     * output or not. If enabled, all instances of of character \r are escaped using a character
     * entity (where possible, that is, within CHARACTERS events, and attribute values). Otherwise
     * they are output as is. The main reason to enable this property is to ensure that carriage
     * returns are preserved as is through parsing, since otherwise they will be converted to
     * canonical xml linefeeds (\n), when occuring along or as part of \r\n pair.
     */
    private final boolean isEscapingOutputCR = false;

    /** Configure Woodstox as StAX XML implementation (actually stax2). */
    static
    {
        final String inputFactoryImpl = "com.ctc.wstx.stax.WstxInputFactory";
        final String outputFactoryImpl = "com.ctc.wstx.stax.WstxOutputFactory";
        final String eventFactoryImpl = "com.ctc.wstx.stax.WstxEventFactory";

        System.setProperty("javax.xml.stream.XMLInputFactory", inputFactoryImpl);
        System.setProperty("javax.xml.stream.XMLOutputFactory", outputFactoryImpl);
        System.setProperty("javax.xml.stream.XMLEventFactory", eventFactoryImpl);

    }

    /**
     * Configure and creates the factories instances.
     */
    private Stax2Factory()
    {
        inputFact = (WstxInputFactory) XMLInputFactory.newInstance();
        outputFact = (WstxOutputFactory) XMLOutputFactory.newInstance();

        inputFact.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, isNamespaceAware);
        inputFact.setProperty(XMLInputFactory2.P_REPORT_PROLOG_WHITESPACE, Boolean.FALSE);

        outputFact.setProperty(WstxOutputProperties.P_OUTPUT_ESCAPE_CR, isEscapingOutputCR);
    }

    /**
     * Return the singleton output factory to get XML writers configured to StAX.
     * 
     * @return The output factory.
     */
    public static XMLOutputFactory2 getStreamWriterFactory()
    {
        synchronized (Stax2Factory.class)
        {
            if (instance == null)
            {
                instance = new Stax2Factory();
            }
        }

        return instance.outputFact;
    }

    /**
     * Returns the singleton input factory to get XML readers configured to StAX.
     * 
     * @return The input factory.
     */
    public static XMLInputFactory2 getStreamReaderFactory()
    {
        synchronized (Stax2Factory.class)
        {
            if (instance == null)
            {
                instance = new Stax2Factory();
            }
        }

        return instance.inputFact;
    }

}
