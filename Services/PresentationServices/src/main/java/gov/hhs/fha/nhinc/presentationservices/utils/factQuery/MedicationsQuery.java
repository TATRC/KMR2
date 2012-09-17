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
package gov.hhs.fha.nhinc.presentationservices.utils.factQuery;

import com.google.gson.Gson;
import gov.hhs.fha.nhinc.kmr.util.CommonUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
//import gov.hhs.fha.nhinc.presentationservices.facttypes.*;

/**
 *
 * @author nhin
 */
public class MedicationsQuery {
    
    private String classname = "\n"+this.getClass().getName()+":";
    private static MedicationsQuery instance = null;
    
    public static MedicationsQuery getInstance() {
        synchronized (MedicationsQuery.class) {
            if (instance == null) {
                instance = new MedicationsQuery();
            }
        }
        return instance;
    }
    
    public String createResponse(Map args) throws FileNotFoundException{
        
        // EXTRACT out args to filter with
        String domain =         (String) args.get("domain");
        String patientId =      (String) args.get("patientId");
        String userId =         (String) args.get("userId"); 
        String responseType =   (String) args.get("responseType");
        String itemId =         (String) args.get("itemId");
        String code =           (String) args.get("code");
        String codeSystemCode = (String) args.get("codeSystemCode");
        String fromDate =       (String) args.get("fromDate");


        
        // GET LIST or DETAIL DATA
        if (responseType.equalsIgnoreCase("detail")) {
            
            // TBD: Call Fact ws with given filter params.
            //  NOTE: Filtered aprams should be dynamic.  
            //  BUT:  First pass will be static filter params (code, codeSystemCode)
            
            return this.getStubbedXMLDetailData(itemId,  patientId,  domain,
                                                codeSystemCode,  code,
                                                fromDate);
        }
        else {
            // Filter should be by patientId
            return this.getStubbedXMLListData(patientId, domain);
            
            
        }
        
    }
    
    /*
    //TBD
    private PatientDataFact toKmrFacts() {
        
        PatientDataFact kmrFact = new PatientDataFact();
        //kmrFact.setFactType();
        
        return kmrFact;
    }
     * 
     */
    
    /**
     * This routine will request all Medications facts for given patientId.
     * 
     * @return 
     */
    public String getFactList() {
        
        String retStr = null;
        
        //CALL DSA level Fact ws or lib call.  Return is placed in onto-Fact objects.
        
        //MAP to gui java objects (for later json generation).
        //PatientDataFact kmrFact = new PatientDataFact();
        

        return "getFactDetailData: NOT YET SUPPORTED.";
    }
    /**
     * This routine will request specific Fact from DSA, per given filter params. 
     * 
     * @return 
     */
    public String getFactDetail() {
        
        //CALL DSA level Fact ws or lib call to get java object of Fact 
       
        

        return "getFactDetailData: NOT YET SUPPORTED.";
    }
    
    public String getStubbedXMLDetailData(String itemId, String patientId, String domain,
                                          String codeSystemCode, String code,
                                          String fromDate) 
            throws FileNotFoundException 
    {
        String id = itemId;
        if (CommonUtil.strNullorEmpty(itemId)) {
            System.out.println(classname+"No itemId given. DEFAULTING to 10");
            id = "10";
        }
        
        //String filename = "/home/nhin/Properties/facts/data/"+patientId+"/"+domain+"/med_"+codeSystemCode+"_"+code+"_"+fromDate;
        String filename = "/home/nhin/Properties/facts/data/getPatientData-Medications_Detail_"+id+".json";
        System.out.println("==>PULLING detail STATIC DATA:  "+filename);
        
        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
        return text;
    }
    
    public String getStubbedXMLListData(String patientId, String domain) throws FileNotFoundException {
        
        //String filename = "/home/nhin/Properties/facts/data/"+patientId+"/"+domain+"/getPatientData-Medications_List.xml";
        String filename = "/home/nhin/Properties/facts/data/getPatientData-Medications_List.json";
        System.out.println("PULLING list STATIC DATA:  "+filename);

        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
        return text;
    }
    
    
    
    
    
}
