/*
 * TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
 * Copyright (c) 2008, Nationwide Health Information Network (NHIN) Connect. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the NHIN Connect Project nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *    END OF TERMS AND CONDITIONS
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.account;

import gov.hhs.fha.nhinc.common.account.CreateAccountRequestType;
import gov.hhs.fha.nhinc.common.account.Response;
import gov.hhs.fha.nhinc.common.account.UpdateAccountRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.ldapaccess.ContactDAO;
import gov.hhs.fha.nhinc.ldapaccess.ContactDTO;
import gov.hhs.fha.nhinc.ldapaccess.LdapService;

/**
 *
 * @author Sushma
 */
public class AccountMgrImpl {

    /** Logger. */
    private static Log log = LogFactory.getLog(AccountMgrImpl.class);
    private String ERROR_CREATE_ACCOUNT = "Error while creating user Account";

    public Response updateAccount(UpdateAccountRequestType updateAccountRequest) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Response createAccount(CreateAccountRequestType createAccountRequest) {
        log.debug("Starting create Account for createAccountRequest query.");
        Response response = new Response();
        
        try {
            ContactDAO contactDAO = LdapService.getContactDAO();
            ContactDTO contactDTO = new ContactDTO();
            contactDTO.setCommonName(createAccountRequest.getUserName());
            contactDTO.setUid(createAccountRequest.getSsn());
            contactDTO.setGivenName(createAccountRequest.getName().getFirstName());
            contactDTO.setInitials(createAccountRequest.getName().getMiddleName());
            contactDTO.setSurname(createAccountRequest.getName().getLastName());
            contactDTO.setHomePhone(createAccountRequest.getPhoneNumber().getHomeNumber());
            contactDTO.setMobile(createAccountRequest.getPhoneNumber().getMobileNumber());
            contactDTO.setTelephoneNumber(createAccountRequest.getPhoneNumber().getWorkNumber());
            contactDTO.setPostalAddress(createAccountRequest.getAddress().getAddress1());
            contactDTO.setStreet(createAccountRequest.getAddress().getAddress2());
            contactDTO.setCity(createAccountRequest.getAddress().getCity());
            contactDTO.setState(createAccountRequest.getAddress().getState());
            contactDTO.setPostalCode(createAccountRequest.getAddress().getZipCode());
            contactDTO.setMail(createAccountRequest.getEmailAddress());
            contactDTO.setUserPassword(createAccountRequest.getPassword().getBytes());
            contactDAO.insertContact(contactDTO);
            response.setText("Your information has been received, we will email "
                    + "you once your account has been created");
            log.info("User details successfully inserted into LDAP");
            response.setSuccess(true);

        } catch (Throwable t) {
            response.setSuccess(false);
            log.error("Error creating User Account", t);
            response.setText(ERROR_CREATE_ACCOUNT + t.getMessage() == null ? t.getClass().getName() : t.getMessage());
        }
        return response;
    }
}
