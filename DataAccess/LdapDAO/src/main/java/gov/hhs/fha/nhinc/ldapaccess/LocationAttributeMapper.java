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

package gov.hhs.fha.nhinc.ldapaccess;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import org.springframework.ldap.core.AttributesMapper;

/**
 * Maps a set of attributes to a Location object.
 *
 * @author cmatser
 */
public class LocationAttributeMapper implements AttributesMapper {

    public Object mapFromAttributes(Attributes attributes) throws NamingException {
        LocationDTO locationDTO = new LocationDTO();
        Attribute commonName = attributes.get("cn");
        if(commonName != null)
            locationDTO.setCommonName((String) commonName.get());
        Attribute departmentNumber = attributes.get("departmentNumber");
        if(departmentNumber != null)
            locationDTO.setDepartmentNumber((String) departmentNumber.get());
        Attribute description = attributes.get("description");
        if(description != null)
            locationDTO.setDescription((String) description.get());
        Attribute destinationIndicator = attributes.get("destinationIndicator");
        if(destinationIndicator != null)
            locationDTO.setDestinationIndicator((String) destinationIndicator.get());
        Attribute displayName = attributes.get("displayName");
        if(displayName != null)
            locationDTO.setDisplayName((String) displayName.get());
        Attribute organizationalUnit = attributes.get("ou");
        if(organizationalUnit != null)
            locationDTO.setOrganizationalUnit((String) organizationalUnit.get());
        Attribute telephoneNumber = attributes.get("telephoneNumber");
        if(telephoneNumber != null)
            locationDTO.setTelephoneNumber((String) telephoneNumber.get());

        return locationDTO;
    }

}