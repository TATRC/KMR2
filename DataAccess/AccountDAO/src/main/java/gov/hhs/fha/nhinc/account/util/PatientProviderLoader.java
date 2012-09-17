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
package gov.hhs.fha.nhinc.account.util;

import com.thoughtworks.xstream.XStream;
import gov.hhs.fha.nhinc.account.model.PatientProviderItem;
import gov.hhs.fha.nhinc.account.service.AccountService;
import java.io.FileReader;

/**
 * Simple loader for testing
 * @author Sushma
 */
public class PatientProviderLoader {

    public static void main(String args[]) {

        //Setup XStream
        XStream xstream = new XStream();
        xstream.alias("PatientProviderItem", PatientProviderItem.class);

        //Read xml file from command line
        if (args.length != 1) {
            System.out.println("Usage: java PatientProviderLoader <file-name>");

            PatientProviderItem ppi = new PatientProviderItem();
            ppi.setPatientProviderId(1L);
            ppi.setPatientId(2L);
            ppi.setProviderId("3");

            System.out.println("Eg:");
            System.out.println(xstream.toXML(ppi));

            System.exit(1);
        }

        try {
            //Get object out of file
            PatientProviderItem ppi = (PatientProviderItem) xstream.fromXML(new FileReader(args[0]));

            //Save object
            AccountService accountService = new AccountService();
            accountService.savePatientProvider(ppi);

            System.out.println("PatientProvider saved, id: " + ppi.getPatientProviderId());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
