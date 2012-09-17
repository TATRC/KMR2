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
 *
 * @author nhin
 */
public class CommandSimulationResource extends BaseResource {

    public Form form;
    private String simulationId;
    private String action;
    private String token;

    public CommandSimulationResource(Context context, Request request, Response response) {
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
            String errorMessage = "commandSimulation: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "commandSimulationFact");
            String ret = err.generateError();
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, ret);
            return;
        }

	String response = "";
        String callResponse = commandSimulation(simulationId, action);
        if (callResponse.equals("success")) {
             response = "{\n"
			+ "    \"commandSimulationFact\": {\n"
			+ "        \"successStatus\": true,\n"
			+ "    }\n" + "}";
        }
        else {
            response = "{\n"
			+ "    \"commandSimulationFact\": {\n"
			+ "        \"successStatus\": false,\n"
			+ "        \"statusMessage\": \"Error in commandSimulation\",\n"
			+ "    }\n" + "}";
        }

        System.out.println("COMMAND CONFIGURATION RETURNING: " + callResponse);

        getResponse().setStatus(Status.SUCCESS_OK);
        getResponse().setEntity(response, MediaType.APPLICATION_JSON);
        // TODO handle error case returning json for successStatus
    }

    private void setParameters(Form form) {

        if (getRequestParameter(form, "simulationId")) {
            simulationId = form.getFirst("simulationId").getValue();
        }

        if (getRequestParameter(form, "action")) {
            action = form.getFirst("action").getValue();
        }

        if (getRequestParameter(form, "token")) {
            token = form.getFirst("token").getValue();
        }
    }

    private boolean getRequestParameter(Form form, String param) {
        if (null != form.getFirst(param)) {
            return true;
        }
        return false;
    }

    private String validateFields() {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("simulationId", simulationId);
        fields.put("action", action);
        ParameterValidator validator = new ParameterValidator(fields);
        return validator.validateMissingOrEmpty();
    }


    private String commandSimulation(String simulationId, String action) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
