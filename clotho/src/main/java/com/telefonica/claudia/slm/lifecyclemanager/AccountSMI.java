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

package com.telefonica.claudia.slm.lifecyclemanager;

/**
 *
 * Interface for Accounting the Service LifeCycle
 */
public interface AccountSMI {
    //loginAuthentication(userId, credentials) To access the web based GUI interface
    
    //logout(userId) To logout from the web based GUI
            
    //newReservoirAccount(userId, profile, grants) To create a new account associated to the logged Service Provider
            
    //updateServiceProviderProfile(userId, profile) To update the profile associated to the Service Provider
            
    //requestServiceAccount(userData) So new SP can request accounts in the RESERVOIR site
            
    //renewCredentials(userId) To renew the credentials of a given user
            
    //listHistoricalInvoicesIssuedByService(userId, serviceID, dateStart, dateEnd) To list the invoices associate with a given service of a given SP
            
    //listHistoricalInvoicesIssuedBySP(userId, dateStart,dateEnd) To list the invoices associate with a given  SP, for all her services  viewInvoicesIssued(invoiceId) To get a particular invoice.
            
    //rechargePrepaidAccount(userId, accountId, credits). Recharge actions to deposit new credits to prepaid accounts.
            
    //checkBalance(accountId) The user invokes this method in order to check the balance by providing her  account ID
            
    //changeAccountOfService(serviceId,newAccountId) To change the account to which a service is charging. Note that this can be use to change between pay-per-use and prepaid (or viceversa) if the old and  new accounts belong to different types.
    
            //changeBankAccount(accountId, newBankData) Change the bank data associated of a pay-per-use  account (e.g. bank name, bank account number, etc.).
            
    //moveCredits(accountIdA, accountIdB, credits) Transfer credits between two prepaid accounts
            
    //LowCredit() The Service Provider must be warned if he/she runs out of credit.
}
