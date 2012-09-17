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
package gov.hhs.fha.nhinc.presentationservices;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author jharby
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({gov.hhs.fha.nhinc.presentationservices.SetMessagesTest.class,
    gov.hhs.fha.nhinc.presentationservices.ValidateAccountTest.class,
    gov.hhs.fha.nhinc.presentationservices.SetAccountTest.class,
    gov.hhs.fha.nhinc.presentationservices.GetMessagesTest.class,
    gov.hhs.fha.nhinc.presentationservices.GetMessagesEmailTest.class,
    gov.hhs.fha.nhinc.presentationservices.GetMessageDetailTest.class,
    gov.hhs.fha.nhinc.presentationservices.GetAddressBookTest.class,
    gov.hhs.fha.nhinc.presentationservices.GetPatientDataTest.class,
    gov.hhs.fha.nhinc.presentationservices.SetMessagesEmailTest.class,
    gov.hhs.fha.nhinc.presentationservices.DiagnosticsTests.class})
public class PresentationServicesSuite {

    public static final String defaultServer = "localhost";

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Test beginning: " + new Date());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("Test ending: " + new Date());
    }

    @Before
    public void setUp() throws Exception {
        
    }

    @After
    public void tearDown() throws Exception {
        
    }
}
