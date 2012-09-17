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
import org.junit.Ignore;
import org.apache.commons.logging.LogFactory;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.net.URL;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jharby
 */
public class SetMessagesTest {

    private String server = PresentationServicesSuite.defaultServer;

    public SetMessagesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        System.out.println("SETMESSAGES - HOST IS: " + server);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSetMessageAlerts() throws Exception {
        System.out.println("TEST CASE: testSetMessageAlerts");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);
        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Update"));
        request.getRequestParameters().add(new NameValuePair("userId", "99990070"));
        request.getRequestParameters().add(new NameValuePair("messageIds", "3"));
        request.getRequestParameters().add(new NameValuePair("types", "Alert"));
        request.getRequestParameters().add(new NameValuePair("labels", "Starred"));
//        request.getRequestParameters().add(new NameValuePair("status", "Archive"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));

        System.out.println("\nREQUEST URL IS: " + request.toString());
        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nALERT RESPONSE IS: \n" + stringResponse + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    @Test
    public void testSetMessageAlertsArchive() throws Exception {
        System.out.println("TEST CASE: testSetMessageAlerts");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);
        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Archive"));
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("messageIds", "3"));
        request.getRequestParameters().add(new NameValuePair("types", "Alert"));
//        request.getRequestParameters().add(new NameValuePair("labels", "Starred"));
//        request.getRequestParameters().add(new NameValuePair("status", "Archive"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));

        System.out.println("\nREQUEST URL IS: " + request.toString());
        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nALERT RESPONSE IS: \n" + stringResponse + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    @Test
    public void testSetMessageAlertDelete() throws Exception {
        System.out.println("TEST CASE: testSetMessageAlertDelete");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);
        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Delete"));
        request.getRequestParameters().add(new NameValuePair("userId", "99990070"));
        request.getRequestParameters().add(new NameValuePair("messageIds", "1"));
        request.getRequestParameters().add(new NameValuePair("types", "Alert"));
        request.getRequestParameters().add(new NameValuePair("labels", "starred,starred"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));

        System.out.println("\nREQUEST URL IS: " + request.toString());
        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nALERT RESPONSE IS: \n" + stringResponse + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    @Test
    public void testSetMessageAlertRead() throws Exception {
        System.out.println("TEST CASE: testSetMessageAlertRead");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);
        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Read"));
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("messageIds", "1"));
        request.getRequestParameters().add(new NameValuePair("types", "Alert"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));

        System.out.println("\nREQUEST URL IS: " + request.toString());
        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nALERT RESPONSE IS: \n" + stringResponse + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    @Test
    public void testSetMessageAlertUnread() throws Exception {
        System.out.println("TEST CASE: testSetMessageAlertRead");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);
        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Unread"));
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("messageIds", "1"));
        request.getRequestParameters().add(new NameValuePair("types", "Alert"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));

        System.out.println("\nREQUEST URL IS: " + request.toString());
        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nALERT RESPONSE IS: \n" + stringResponse + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    @Test
    @Ignore
    public void testSetMessagesDocument() throws Exception {
        System.out.println("TEST CASE: testSetMessages - Document");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);

        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Update"));
        request.getRequestParameters().add(new NameValuePair("userId", "99990070"));
        request.getRequestParameters().add(new NameValuePair("messageIds", "14"));
        request.getRequestParameters().add(new NameValuePair("types", "Document"));
        request.getRequestParameters().add(new NameValuePair("labels", "starred"));
        request.getRequestParameters().add(new NameValuePair("status", "Unread"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));

        System.out.println("\nREQUEST URL IS: " + request.toString());

        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nEMAIL RESPONSE IS: \n" + stringResponse + "\n");
        System.out.println("EXPECTED FOR BOTH is: \n" + getExpectedResponse() + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    private String getExpectedResponse() {
        return "{\"setMessagesFact\":{\"successStatus\":true,\"statusMessage\":\"Set Messages successful\"}}";
    }
}