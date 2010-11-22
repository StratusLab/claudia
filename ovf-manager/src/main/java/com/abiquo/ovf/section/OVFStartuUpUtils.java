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

import org.dmtf.schemas.ovf.envelope._1.StartupSectionType;
import org.dmtf.schemas.ovf.envelope._1.StartupSectionType.Item;

import com.abiquo.ovf.exceptions.RequiredAttributeException;

public class OVFStartuUpUtils
{

    public static Item createStartUpItem(String virtualSystemId, Integer order,
        Boolean waitForHost, String startAction, String stopAction, Integer startDelay,
        Integer stopDelay) throws RequiredAttributeException
    {
        Item ite = new Item();

        if (virtualSystemId == null || order == null)
        {
            throw new RequiredAttributeException("Id or oreder on StartUpSection.Item");

        }

        ite.setId(virtualSystemId);
        ite.setOrder(order);

        ite.setWaitingForGuest(waitForHost);
        ite.setStartAction(startAction);
        ite.setStopAction(stopAction);
        ite.setStartDelay(startDelay);
        ite.setStopDelay(stopDelay);

        return ite;
    }

    public static void addStartUpItem(StartupSectionType susection, Item startItem)
    {
        // TODO check id already exist exception
        // TODO check order is valid

        susection.getItem().add(startItem);
    }

}
