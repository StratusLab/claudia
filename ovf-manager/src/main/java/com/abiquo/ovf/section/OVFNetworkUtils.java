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

package com.abiquo.ovf.section;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.dmtf.schemas.ovf.envelope._1.MsgType;
import org.dmtf.schemas.ovf.envelope._1.NetworkSectionType;
import org.dmtf.schemas.ovf.envelope._1.NetworkSectionType.Network;

import com.abiquo.ovf.exceptions.IdAlreadyExistsException;
import com.abiquo.ovf.exceptions.IdNotFoundException;
import com.abiquo.ovf.exceptions.RequiredAttributeException;
import com.abiquo.ovf.exceptions.SectionNotPresentException;

/**
 * 
 * @author abiquo
 *
 */
public class OVFNetworkUtils
{

    /**
     * Adds a new Network to NetworkSection
     * 
     * @param netSection <NetworkSection> xml tag.
     * @param net new <Network> tag to add
     * @throws RequiredAttributeException if 
     * @throws IdAlreadyExistsException 
     */
    public static void addNetwork(NetworkSectionType netSection, Network net) throws RequiredAttributeException, IdAlreadyExistsException
    {
        if (net != null || netSection != null)
        {
            for (Network existingNetwork : netSection.getNetwork())
            {
                if (existingNetwork.getName().equalsIgnoreCase(net.getName()))
                {
                    throw new IdAlreadyExistsException("Id " + net.getName() + " already exists!");
                }
            }
            netSection.getNetwork().add(net);
        }
        else
        {
            throw new RequiredAttributeException("Required Network or NetworkSection cannot be null");
        }
    }
    
    /**
     * Create a <Network> tag with a given identifer string for ovf:name
     * 
     * @param identifier identifier of the network (required)
     * @param description description of the network (optional)
     * @return New Network instance;
     * @throws RequiredAttributeException thrown when identifier is null.
     */
    public static Network createNetwork(String identifier, String description) throws RequiredAttributeException
    {
        Network net = new Network();
        
        if (identifier != null)
        {
            net.setName(identifier);
            if(description != null)
            {
                MsgType infoNetwork = new MsgType();
                infoNetwork.setValue(description);
                
                net.setDescription(infoNetwork);
            }
        }
        else
        {
            throw new RequiredAttributeException("Required network ovf:name");
        }
        
        return net;
    }
    
    /**
     * Search inside the NetworkSection all the network interfaces.
     * 
     * @param netSection <NetworkSection> tag we look for all Networks
     * @return All Network interfaces defined in XML
     * @throws SectionNotPresentException
     * @throws RequiredAttributeException 
     */
    public static List<Network> getAllNetworks(NetworkSectionType netSection) throws RequiredAttributeException
    {
               
        List<Network> networks = new ArrayList<Network>();
        
        if( netSection != null)
        {
            networks = netSection.getNetwork();
        }
        else
        {
            throw new RequiredAttributeException("NetworkSectionType cannot be null");
        }
            
        return networks;
    }

    /**
     * Search a Network with a given Id
     * 
     * @param netSection <NetworkSection> where we look for the <Network> interfaces
     * @param networkId identifier of the <Network> (ovf:name)
     * @return Network founded <Network>
     * @throws IdNotFoundException 
     * @throws RequiredAttributeException
     */
    public static Network getNetwork(NetworkSectionType netSection, String networkId) throws IdNotFoundException, RequiredAttributeException
    {
        if (netSection == null || networkId == null)
        {
          throw new RequiredAttributeException("Some values are null!");    
        }
        
        for (Network net : netSection.getNetwork())
        {
            if (networkId.equals(net.getName()))
            {
                return net;
            }
        }

        throw new IdNotFoundException("Network name " + networkId);
    }
    
    /**
     * Set other attributes to NetworkSection. Due there is no xsd attributes for network features
     * such as Gateway, range, netmask.. It's mandatory to create a function that will insert this
     * values in an auxiliar OtherAttributes Map.
     * 
     * @param netSection the <NetworkSection> we work with
     * @param key Key of the Map
     * @param value value of the key
     * @throws RequiredAttributeException if key or netSection are null throws this method
     * @throws IdAlreadyExistsException if key already inserted
     */
    public static void addOtherAttributes(Network net, QName key, String value) throws RequiredAttributeException, IdAlreadyExistsException
    {
        if (net == null || key == null)
        {
            throw new RequiredAttributeException("Some values are null!");
        }
        if (net.getOtherAttributes().get(key) != null)
        {
            throw new IdAlreadyExistsException("Key already exists");
        }
        net.getOtherAttributes().put(key, value);

    }
    
    /**
     * Return an OtherAttributes value for a given key into the <NetworkSection>
     * @param netSection <Network
     * @param key
     * @return
     * @throws RequiredAttributeException
     * @throws IdNotFoundException
     */
    public static String getOtherAttribute(Network net, QName key) throws RequiredAttributeException, IdNotFoundException
    {
        if (net == null || key == null)
        {
            throw new RequiredAttributeException("Some values are null!");
        }
        
        String value = net.getOtherAttributes().get(key);
        
        if (value == null)
        {
            throw new IdNotFoundException("Key doesn't exist");
        }
     
        return value;
    }
}
