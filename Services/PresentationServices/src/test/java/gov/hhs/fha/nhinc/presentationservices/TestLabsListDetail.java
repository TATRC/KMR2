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


import java.util.Iterator;
import gov.hhs.fha.nhinc.presentationsservices.facttypes.labslist.*;

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
public class TestLabsListDetail {

    public TestLabsListDetail() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testToJson() throws Exception {
        
        String filename = "/Users/tnguyen/DSS/trunk/projects/KMR/data/FactSamples/getPatientData-Results-Labs_Detail_10.xml";
        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
        
        XMLSerializer xmlser = new XMLSerializer();
        JSON json = xmlser.read(text);
        System.out.println(json.toString(2));
        assertTrue(true);
        
        
        PatientDataFact gui = new PatientDataFact();
        
        //PREP globals
        gui.setFactType("Medications");
        gui.setItemId(10);
        gui.setMaxColumns(8);
        gui.setStatus("Complete");
        gui.setSuccessStatus(true);
        gui.setTrxnType("list");
        gui.setVisibleGridHeaders(false);
        
        

        //PREP detailtabs
        List<String> dtabFilters = new ArrayList<String>();
        dtabFilters.add("code");
        dtabFilters.add("codeSystemCode");
    
        gui.getDetailTabs().add( 
                createDetailTabs("Results",
                                 "grid",
                                 "detail",
                                 "labTests",
                                 dtabFilters));
       
        
        dtabFilters.add("sectionId"); //ADDING sectionId to filter list 
        gui.getDetailTabs().add(
                createDetailTabs("Description",
                                 "text",
                                 "ecs",
                                 "4",
                                 dtabFilters));
                                 
        gui.getDetailTabs().add( 
                createDetailTabs("Images",
                                 "text",
                                 "ecs",
                                 "20",
                                 dtabFilters));
        
        
        Facts aFact = new Facts();
        aFact.setAccessionId("23");
        
        gui.getFacts().add(aFact);
        
        
    }
    
    private DetailTabs createDetailTabs(String label,
                                        String type,
                                        String responseType,
                                        String sectionId,
                                        List<String> filters)
    {
        DetailTabs dtab = new DetailTabs();
        dtab.setLabel(label);
        dtab.setResponseType(responseType);
        dtab.setSectionId(sectionId);
        dtab.setType(type);
        
        List<String> dtabFilter = new ArrayList();
        Iterator<String> iter = dtabFilter.iterator();
        while (iter.hasNext()) {
            dtabFilter.add(iter.next());
        }
        
        return dtab;
    }

    //@Test
    public void testXmlToJsonFile() throws Exception {
        String fileLocation = "/Users/nhin/vitalsSOAPResponse.xml";
        System.out.println("Expected location for vitalsSOAPResponse.xml is: " + fileLocation);
        String xml = readFileAsString(fileLocation);
        XMLSerializer xmlser = new XMLSerializer();
        JSON json = xmlser.read(xml);
        System.out.println(json.toString(2));
        assertTrue(true);

    }

    private String readFileAsString(String filePath)
            throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
