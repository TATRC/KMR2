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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import gov.hhs.fha.nhinc.kmr.util.SessionUtilities;
import gov.hhs.fha.nhinc.presentationservices.helpers.ErrorResponse;
import gov.hhs.fha.nhinc.presentationservices.helpers.ParameterInitializer;
import gov.hhs.fha.nhinc.presentationservices.helpers.ParameterValidator;
import gov.hhs.fha.nhinc.presentationservices.helpers.SynchronousRequestHelperFactory;
import gov.hhs.fha.nhinc.presentationservices.resources.BaseResource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.drools.dssagentserver.helpers.SynchronousRequestHelper;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 *
 * @author nhin
 */
public class GetResultDetailsResource extends BaseResource {

    private String resultId;
    private String token;
    final Logger logger = Logger.getLogger(GetResultDetailsResource.class.getName());
    

    public GetResultDetailsResource(Context context, Request request, Response response) {
        super(context, request, response);

        setModifiable(true);

        try {
            String query = request.getResourceRef().getQueryAsForm().getQueryString();
            logger.log(Level.INFO, "query: " + query);

            if (checkApiCaller(query) != true) {
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
                return;
            }

        } catch (Exception e) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }
        try {
            ParameterInitializer.setParameters(this, request);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

        if (token == null || !SessionUtilities.verifyToken(token)) {
            String errorMsg = "";
            if (token == null) {
                errorMsg = "No token was provided in your request.";
            }
            else {
                errorMsg = "The token was not found, your session may have timed out.";
            }
            SessionUtilities.generateErrorRepresentation(errorMsg, "400", response);
        }

        // This representation has only one type of representation.
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
    public Representation represent(Variant variant) throws org.restlet.resource.ResourceException {
        String invalidFields = handleParamValidation();
        if (!invalidFields.equals("")) {
            return new StringRepresentation(invalidFields, MediaType.APPLICATION_JSON);
        }
        // Not sure on return, what is coming in XML from the DSA and what we
        // need to fill out here. If all is coming from the DSA then this should
        // be close.
        String jsonRsp = getResultDetails(resultId);
        jsonRsp = jsonRsp.replaceAll("\"true\"", "true");
        jsonRsp = jsonRsp.replaceAll("\"false\"", "false");
        System.out.println("GET RESULT DETAILS RETURNING: " + jsonRsp);
        Representation representation =
                new StringRepresentation(jsonRsp, MediaType.APPLICATION_JSON);
        return representation;
    }

    private String handleParamValidation() {
        String ret = "";
        Map<String, String> fieldMap = new HashMap<String, String>();
        fieldMap.put("resultId", resultId);
        ParameterValidator validator = new ParameterValidator(fieldMap);
        String failures = validator.validateMissingOrEmpty();
        if (failures.length() > 1) {
            String errorMessage = "getResultDetails: "
                    + failures + " are missing required field(s)";
            ErrorResponse err = new ErrorResponse(errorMessage, "getResultDetails");
            ret = err.generateError();
            return ret;
        }
        return ret;
    }

    private String getResultDetails(String resultId) {

        XStream serializer = new XStream(new JsonHierarchicalStreamDriver());
        SynchronousRequestHelper helper =
                SynchronousRequestHelperFactory.getInstance();
        LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>();
        args.put("resultId", resultId);

        helper.invokeRequest(
                "getResultDetails", args);
        String xml = (String) helper.getReturn(false);
        XMLSerializer xmlser = new XMLSerializer();
        JSON jsonOut = xmlser.read(xml);
        String jsonResp = jsonOut.toString(2);

        //return jsonResp;
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
