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

package gov.hhs.fha.nhinc.presentationservices.resources.facts;

import gov.hhs.fha.nhinc.kmr.util.CommonUtil;
import gov.hhs.fha.nhinc.kmr.util.SessionUtilities;
import gov.hhs.fha.nhinc.presentationservices.helpers.ErrorResponse;
import gov.hhs.fha.nhinc.presentationservices.helpers.ParameterValidator;
import gov.hhs.fha.nhinc.presentationservices.resources.BaseResource;
import gov.hhs.fha.nhinc.presentationservices.utils.factQuery.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class GetPatientDataResource extends BaseResource {

    /* 
     * fieldMap expected incoming attributes:
     * 
     *  userId
     *  token
     *  domain
     *  responseType     list, detail, ecs
     *  code             optional
     *  codeSystemCode   optional
     *  sectionId        optional
     *  fromDate         optional
     *  toDate           optional
     *  returnCount      optional
     *
     * SAMPLE gui call:
     *   http://<host>:9763/PresentationServices/getPatientData?domain=medications&responseType=list&userId=99990070&token=
     *   http://<host>:9763/PresentationServices/getPatientData?itemId=10&responseType=detail&code=0093-4160-78&codeSystemCode=ndc&domain=medications&userId=99990070&token=6C
     *   http://<host>:9763/PresentationServices/getPatientData?itemId=10&responseType=ecs&code=0093-4160-78&codeSystemCode=ndc&sectionId=4&userId=99990070&token=6C
     */
    private Map fieldMap = new HashMap();
    boolean useStubbedEcs = false;
    static final Logger logger = Logger.getLogger(GetPatientDataResource.class.getName());

    public GetPatientDataResource(Context context, Request request, Response response) {
        
        super(context, request, response);

        String token = null;
        
        setModifiable(true);
        
        try {
            String query = request.getResourceRef().getQueryAsForm().getQueryString();
            GetPatientDataResource.logger.log(Level.INFO, "query: {0}", query);

            if (checkApiCaller(query) != true) {
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);

                return;
            }
        } catch (Exception e) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

            return;
        }
        try {

            System.out.println("\n===>REQ: " + request.getResourceRef().getQueryAsForm().toString());

            //creating a Map of all params.
            //Note: should not be used if an attrib is an array.
            fieldMap.putAll(request.getResourceRef().getQueryAsForm().getValuesMap());
            
            // When patientId is not given, use the userId as the patient Id.
            if (CommonUtil.strNullorEmpty( (String)fieldMap.get("patientId")) ) {
                fieldMap.put("patientId", fieldMap.get("userId"));
            }
            token = (String) fieldMap.get("token");
            
            System.out.println("\n===> userid= "+fieldMap.get("userId"));
            
            
            
        } catch (Exception e) {
            GetPatientDataResource.logger.log(Level.SEVERE, e.getMessage());
        }

        //VERIFY THAT TOKEN BEIGN USED IS STILL VALID.
        if ((token == null) || (!SessionUtilities.verifyToken(token))) {
            String errorMsg = "The token was not found, the session may have timed out.";
            SessionUtilities.generateErrorRepresentation(errorMsg, "400", response);
        }

        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        context.getClientDispatcher();
        SessionUtilities.setCORSHeaders(this);
        init(context, request, response);
    }

    @Override
    public void init(Context context, Request request, Response response) {
        super.init(context, request, response);
    }

    @Override
    public Representation represent(Variant variant) throws ResourceException {

        //GETTING PresentationServices.properties......
        String useStubbedData = getProperty("useStubbedEcsData");
        if (useStubbedData.equalsIgnoreCase("true"))
            this.useStubbedEcs = true;

        System.out.println("\nPROPERTY: useStubbedEcsData = "+ useStubbedData);


        //VALIDATING INPUT PARAMS............
        String ret = handleParamValidation();
        
        if (!ret.equals("")) {
            System.out.println("\n==>ERROR: "+ret);
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        }

        //PROCESSING.......................
        String resp = "";
        try {
            resp = getPatientData();
            
        } catch (Exception e) {
            e.printStackTrace();
            resp = e.getMessage();
        }
        
        System.out.println("===>GET PATIENT DATA (" + (String) fieldMap.get("domain") + "):" + resp);
        Representation representation = new StringRepresentation(resp, MediaType.APPLICATION_JSON);

        return representation;
    }

    /**
     * handleParamValidation - Check for required incoming parameters from request.
     * @return String, null if no errors.  Else, an relevant error messages will be returned.
     */
    private String handleParamValidation() {
        String ret = "";
        String responseType = (String)this.fieldMap.get("responseType");
        
        //PREP Map to check required detail attributes.
        Map requiredFieldMap = this.fieldMap;
        
        requiredFieldMap.put("userId", this.fieldMap.get("userId"));
        requiredFieldMap.put("responseType", this.fieldMap.get("responseType"));
        
        //INCLUDE required attributes as needed too.
        if (//responseType.equalsIgnoreCase("detail")||
            responseType.equalsIgnoreCase("ecs")) 
        {
            requiredFieldMap.put("code", (String)this.fieldMap.get("code"));
            requiredFieldMap.put("codeSystemCode", (String)this.fieldMap.get("codeSystemCode"));        

        } else {
            requiredFieldMap.put("domain", this.fieldMap.get("domain"));
        }

        ParameterValidator validator = new ParameterValidator(requiredFieldMap);
        String failures = validator.validateMissingOrEmpty();
        
        if (failures.length() > 1) {
            String errorMessage = "GetPatientData: " + failures + "is a missing required field";

            ErrorResponse err = new ErrorResponse(errorMessage, "GetPatientData");
            ret = err.generateError();
            return ret;
        }
        
        return ret;
    }

    private String getPatientData() throws Exception {
        String resp = "";

        JSON jsonOut = null;
        String jsonRsp = null;
        String xml = null;

        String domain = (String)this.fieldMap.get("domain");
        String responseType = (String)this.fieldMap.get("responseType");
        
        //GET data for detail, list, or ecs 
        if (responseType.equalsIgnoreCase("ecs"))
        {
            jsonRsp = EcsQuery.getInstance().createResponse(fieldMap, useStubbedEcs);
            
        } else {
            boolean isXML = false;
            
            if (domain.equalsIgnoreCase("Medications")) {
                xml = MedicationsQuery.getInstance().createResponse(fieldMap);

                isXML = true;

            } else if (domain.equalsIgnoreCase("Immunizations")) {
                xml = ImmunizationsQuery.getInstance().createResponse(fieldMap);
                

            } else if (domain.equalsIgnoreCase("Labs")) {
                xml = LabsQuery.getInstance().createResponse(fieldMap);
                
            
            } else if (domain.equalsIgnoreCase("Admissions")) {
                xml = AdmissionsQuery.getInstance().createResponse(fieldMap);

            } else if (domain.equalsIgnoreCase("Allergies")) {
                xml = AllergiesQuery.getInstance().createResponse(fieldMap);

            } else if (domain.equalsIgnoreCase("Demographics")) {
                xml = DemographicsQuery.getInstance().createResponse(fieldMap);

            } else if (domain.equalsIgnoreCase("Diagnoses")) {
                xml = DiagnosesQuery.getInstance().createResponse(fieldMap);

            } else if (domain.equalsIgnoreCase("DocInpatient")) {
                xml = DocInpatientQuery.getInstance().createResponse(fieldMap);

            } else if (domain.equalsIgnoreCase("DocOutpatient")) {
                xml = DocOutpatientQuery.getInstance().createResponse(fieldMap);

            } else if (domain.equalsIgnoreCase("Equipment")) {
                xml = EquipmentsQuery.getInstance().createResponse(fieldMap);

            } else if (domain.equalsIgnoreCase("Imaging")) {
                xml = ImagingQuery.getInstance().createResponse(fieldMap);

            } else if (domain.equalsIgnoreCase("Procedures")) {
                xml = ProceduresQuery.getInstance().createResponse(fieldMap);

            } else if (domain.equalsIgnoreCase("Vitals")) {
                xml = VitalsQuery.getInstance().createResponse(fieldMap);

            } else {
                System.out.println("\n===>ERROR: GetPatientDataResource: Domain " + domain + " not supported.\n");
                //TBD - Need better error msg return.
            }


            //TRANSFORM xml TO json for return.
            if (xml.isEmpty()) {
                System.out.println("\n===>ERROR: GetPatientData: No response for domain=" + domain
                        + " responseType=" + responseType
                        + "query.\n");

                //TBD - Need better error msg return.
            } else {
System.out.println("\n===>DATA: " + xml);
                jsonRsp = xml;
/*
                //OPEN and finish coding once DSA Fact services online.
                //ONLY transform List/Detail/ECS xml to json response if response is in xml.
                //else, it's already in json (only when running stubbed data).
                
                if (isXML) {
                    System.out.println("\nXML: " + xml);

                    XMLSerializer xmlser = new XMLSerializer();
                    xmlser.setSkipNamespaces(true);
                    xmlser.setRemoveNamespacePrefixFromElements(true);
                    jsonOut = xmlser.read(xml);
                    jsonRsp = jsonOut.toString(2);

                    // HAVE TO DO REPLACEMENTs because XMLSerializer defaults to String type.
                    jsonRsp = jsonRsp.replaceAll("\"true\"", "true");
                    jsonRsp = jsonRsp.replaceAll("\"false\"", "false");
                } else {

                    jsonRsp = xml;
                }
*/
                //DBG ONLY
                //System.out.println(jsonRsp);
            }
        }

        return jsonRsp;
    }
}