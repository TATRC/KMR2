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
package gov.hhs.fha.nhinc.presentationservices.resources.simulator;

import gov.hhs.fha.nhinc.kmr.util.SessionUtilities;
import gov.hhs.fha.nhinc.presentationservices.helpers.ErrorResponse;
import gov.hhs.fha.nhinc.presentationservices.helpers.ParameterValidator;
import gov.hhs.fha.nhinc.presentationservices.helpers.SaveConfigurationParams;
import gov.hhs.fha.nhinc.presentationservices.resources.BaseResource;
import java.util.HashMap;
import java.util.Map;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;

/**
 * REQUIRED INPUT PARAMS:
 *      userId, token, modelId, configuration
 * OPTIONAL INPUT PARAMS:
 *      configId  --> if given then need to do a SAVE.
 *                    else do a SAVEAS, and this value will be returned by DSA
 *                          within the Response this saveConfiguration Request.
 *
 * SAMPLE INCOMING:

    saveConfiguration?
     userId=1
    &token=6C
    &modelId=m101
    &configId=c99
    &configuration[description]=This is a freeform description of the configuration
    &configuration[agents][0][type]=consumer
    &configuration[agents][0][population]=950
    &configuration[agents][0][subfilter]=Predicted
    &configuration[agents][1][type]=producer
    &configuration[agents][1][population]=8
    &configuration[agents][1][subfilter]=Military Only
    &userId=1
    &token=63

 * SAMPLE OUTGOING:
 *
 *
 * @author 
 */
public class SaveConfigurationResource extends BaseResource {

    public Form form;
    private String modelId;
    private String userId;
    private String configId;
    private SaveConfigurationParams configuration; //  TBD
    
    private String token;

    public SaveConfigurationResource(Context context, Request request, Response response) {
        super(context, request, response);
        init(context, request, response);
        setModifiable(true);
        setAvailable(true);
        Representation rep = request.getEntity();

    }

    @Override
    public void init(Context context, Request request, Response response) {
        SessionUtilities.setCORSHeaders(this);
        super.init(context, request, response);
    }

    @Override
    public boolean allowPut() {
        return true;
    }

    @Override
    public boolean allowPost() {
        return true;
    }

    /**
     * PUT handler
     * 
     * @param entity
     * @throws ResourceException 
     */
    @Override
    public void storeRepresentation(Representation entity)
            throws ResourceException {
        Form f = new Form(entity);
        setParameters(f);
        
        if (!SessionUtilities.verifyToken(token)) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, "Session token validation failed.");
            return;
        }

        String failures = validateFields();

        if (failures.length() > 1) {
            String errorMessage = "SaveConfiguration: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "saveConfigurationFact");
            String ret = err.generateError();
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, ret);
            return;
        }

	String response = "";
        String callResponse = saveConfiguration(configId, userId);
        if (callResponse.equals("success")) {
             response = "{\n"
			+ "    \"saveConfigurationFact\": {\n"
			+ "        \"successStatus\": true,\n"
			+ "    }\n" + "}";
        }
        else {
            response = "{\n"
			+ "    \"saveConfigurationFact\": {\n"
			+ "        \"successStatus\": false,\n"
			+ "        \"statusMessage\": \"Error in saveConfiguration\",\n"
			+ "    }\n" + "}";
        }

        System.out.println("SAVE CONFIGURATION RETURNING: " + callResponse);

        getResponse().setStatus(Status.SUCCESS_OK);
        getResponse().setEntity(response, MediaType.APPLICATION_JSON);
        // TODO handle error case returning json for successStatus
    }

    /**
     * REQUIRED PARAMS:  userId, token, modelId, configuration
     * @param form
     */
    private void setParameters(Form form) {

        if (getRequestParameter(form, "modelId")) {
            modelId = form.getFirst("modelId").getValue();
        }
        if (getRequestParameter(form, "configId")) {
            configId = form.getFirst("configId").getValue();
        }
        if (getRequestParameter(form, "configuration")) {
            //getting an array
            configuration.configId = this.configId;


        }

        if (getRequestParameter(form, "userId")) {
            userId = form.getFirst("userId").getValue();
        }

        if (getRequestParameter(form, "token")) {
            token = form.getFirst("token").getValue();
        }

//FIGURE OUT HOW to get arrays.
//        if (getRequestParameter(form, "configuration")) {
//            configuration = form.getFirst("configuration").getValue();
//        }

// Need to determine how these are coming in        
//        configuration.configId = configurationId;
//        configuration.stopImprovement =
//        configuration.stopIteration =
//        configuration.stopScore = 
//        configuration.stopTime = 
//        configuration.timeUnit = 
//        configuration.agents =
    }

    private boolean getRequestParameter(Form form, String param) {
        if (null != form.getFirst(param)) {
            return true;
        }
        return false;
    }

    private String validateFields() {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("modelId", modelId);
        fields.put("userId", userId);
        ParameterValidator validator = new ParameterValidator(fields);
        return validator.validateMissingOrEmpty();
    }


    private String saveConfiguration(String simulationId, String action) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
