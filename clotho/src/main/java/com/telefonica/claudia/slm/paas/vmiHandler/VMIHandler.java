/*
 * Claudia Project
 * http://claudia.morfeo-project.org
 *
 * (C) Copyright 2010 Telefonica Investigacion y Desarrollo
 * S.A.Unipersonal (Telefonica I+D)
 *
 * See CREDITS file for info about members and contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the Affero GNU General Public License (AGPL) as
 * published by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the Affero GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * If you want to use this software an plan to distribute a
 * proprietary application in any way, and you are not licensing and
 * distributing your source code under AGPL, you probably need to
 * purchase a commercial license of the product. Please contact
 * claudia-support@lists.morfeo-project.org for more information.
 */
package com.telefonica.claudia.slm.paas.vmiHandler;

import java.util.Map;
import java.util.Set;

import com.telefonica.claudia.slm.deployment.ServiceApplication;
import com.telefonica.claudia.slm.deployment.VEE;
import com.telefonica.claudia.slm.deployment.VEEReplica;
import com.telefonica.claudia.slm.deployment.hwItems.Network;
import com.telefonica.claudia.slm.deployment.hwItems.Product;
import com.telefonica.claudia.slm.vmiHandler.exceptions.AccessDeniedException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.CommunicationErrorException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.NotEnoughResourcesException;
import com.telefonica.claudia.slm.vmiHandler.exceptions.VEEReplicaDescriptionMalformedException;

public interface VMIHandler {

    public static enum ControlActionType {
        install, desinstall, start, stop, update
    };

    public static enum SubmissionModelType {
        TWOPHASE, BESTEFFORT
    };




    public void installProduct(Product product, String ip) throws AccessDeniedException,
            CommunicationErrorException;

    public void installApplication(Product product) throws AccessDeniedException,
            CommunicationErrorException;
}
