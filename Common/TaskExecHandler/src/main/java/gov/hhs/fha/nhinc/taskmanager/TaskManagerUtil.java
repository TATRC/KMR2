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

package gov.hhs.fha.nhinc.taskmanager;

import gov.hhs.fha.nhinc.ldapaccess.ContactAcctDTO;
import gov.hhs.fha.nhinc.ldapaccess.ContactDAO;
import gov.hhs.fha.nhinc.ldapaccess.ContactDTO;
import gov.hhs.fha.nhinc.ldapaccess.LdapService;
import gov.hhs.fha.nhinc.ldapaccess.RoleDAO;
import gov.hhs.fha.nhinc.ldapaccess.RoleDTO;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author cmatser
 */
public class TaskManagerUtil {

    /**
     * Search the ldap roles to find the provider occupying that role.  The
     * return is a ldap query string that will find the provider object.
     *
     * @param role
     * @param location
     * @return
     */
    public static List<String> retrieveProviderLdaps(String role, String location) {
        List<String> providers = new LinkedList<String>();

        //Search for role occupant
        RoleDAO roleDAO = LdapService.getRoleDAO();
        List<RoleDTO> roles = roleDAO.findLocationRole(location, role);
        if (!roles.isEmpty()
                && !roles.get(0).getRoleOccupants().isEmpty()) {
            providers = roles.get(0).getRoleOccupants();
        }

        return providers;
    }

    /**
     * Find zimbra access.  For now, this is stored in the postal address of the
     * user ldap record.
     *
     * @param lookup
     * @return
     */
    public static String[] retrieveCalendarAccess(String lookup) {
        String access[] = new String[2];

        ContactDAO contactDAO = LdapService.getContactDAO();
        List<ContactDTO> contacts = contactDAO.findContact(lookup);
        if (contacts.size() > 0) {
            //Get mail login info
            List<ContactAcctDTO> accts = contactDAO.findContactAcct(
                contacts.get(0).getCommonName(), ContactAcctDTO.CN_CALENDAR);
            if (accts.size() > 0) {
                access[0] = accts.get(0).getUid();
                access[1] = accts.get(0).getClearPassword();
            }
        }

        return access;
    }

}
