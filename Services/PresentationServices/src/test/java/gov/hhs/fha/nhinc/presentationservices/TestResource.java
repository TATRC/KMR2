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


import java.util.Map;
import java.util.HashMap;
import gov.hhs.fha.nhinc.presentationservices.utils.factQuery.EcsQuery;
import org.junit.After;
import org.junit.Before;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import java.io.IOException;
import java.util.Iterator;

import gov.hhs.fha.nhinc.presentationsservices.facttypes.ecsDetail.PatientDataFact;
import gov.hhs.fha.nhinc.presentationsservices.facttypes.ecsDetail.*;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author nhin
 */
public class TestResource {

    public TestResource() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    
    @Test
    private String testPOSTWithArray() throws IOException {
        String ret = null;
        String request = "saveConfiguration?"
                +"name=configinfo name 1&description=configinfo desc 1:  Phasellus vestibulum ipsum augue, id molestie purus. Donec viverra orci ac sem tincidunt facilisis. Morbi eget rhoncus nulla. Aliquam porttitor odio vel metus viverra lacinia sit amet quis erat. Nulla fringilla malesuada ante eu dignissim&startDate=2011-11-07 10:00&duration=20&agents[agentId]=&agents[populationRange]=&agents[agentId][1]=1&agents[population][1]=50&agents[filtered][1]=on&agents[subfilter][1]=filter 1&agents[populationRange][1]=40&agents[agentId][2]=2&agents[population][2]=5&agents[subfilter][2]=filter 2&agents[populationRange][2]=80&agents[agentId][3]=3&agents[population][3]=1000&agents[filtered][3]=on&agents[subfilter][3]=filter 3&agents[populationRange][3]=10&constraints[constraintId]=&constraints[constraintId][1]=1&constraints[required][1]=on&constraints[importance][1]=Low&constraints[constraintId][2]=2&constraints[required][2]=on&constraints[importance][2]=Medium&constraints[constraintId][3]=3&constraints[importance][3]=High&constraints[constraintId][4]=4&constraints[required][4]=on&constraints[importance][4]=Medium&constraints[constraintId][5]=5&constraints[importance][5]=Low&constraints[constraintId][6]=6&constraints[required][6]=on&constraints[importance][6]=High&stopTime=3&stopScore=40&stopIteration=1,000&stopImprovement=5";
        
        return ret;
        
    }
    

}
