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
package gov.hhs.fha.nhinc.presentationservices.resources.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.hhs.fha.nhinc.aggregator.DisplayDataAggregator;
import gov.hhs.fha.nhinc.aggregator.DisplayDataAggregatorPortType;
import gov.hhs.fha.nhinc.common.dda.GetMessageDetailRequestType;
import gov.hhs.fha.nhinc.common.dda.GetMessageDetailResponseType;
import gov.hhs.fha.nhinc.kmr.util.SessionUtilities;
import gov.hhs.fha.nhinc.presentationservices.resources.BaseResource;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
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
 * @author jharby
 */
public class GetMessageDetailResource extends BaseResource {

    private String userId;
    private String messageId;
    private String type;
    private String token;
    private String location;
    final Logger logger = Logger.getLogger(GetMessageDetailResource.class.getName());

    public GetMessageDetailResource(Context context, Request request, Response response) {
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
            this.userId =
                    request.getResourceRef().getQueryAsForm().getFirstValue("userId");
            this.messageId =
                    request.getResourceRef().getQueryAsForm().getFirstValue("messageId");
            this.type =
                    request.getResourceRef().getQueryAsForm().getFirstValue("type");
            this.token =
                    request.getResourceRef().getQueryAsForm().getFirstValue("token");
            this.location =
                    request.getResourceRef().getQueryAsForm().getFirstValue("location");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        if (token == null || !SessionUtilities.verifyToken(token)) {
            String errorMsg = "The token was not found, the session may have timed out.";
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
    public boolean allowPost() {
        return true;
    }

    @Override
    public Representation represent(Variant variant) throws org.restlet.resource.ResourceException {
        String result = getMessageDetail();
        Representation representation =
                new StringRepresentation(result, MediaType.APPLICATION_JSON);
        return representation;
    }

    private String getMessageDetail() {
        DisplayDataAggregator service = new gov.hhs.fha.nhinc.aggregator.DisplayDataAggregator();
        DisplayDataAggregatorPortType port = service.getDisplayDataAggregatorPortSoap11();
        ((BindingProvider) port).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                getProperty("DisplayDataAggregatorService"));

        GetMessageDetailRequestType request = new GetMessageDetailRequestType();
        GetMessageDetailResponseType response = new GetMessageDetailResponseType();

        try {
            request.setMessageId(messageId);
            request.setMessageType(type);
            request.setUserId(userId);
            if (location.contains("Archive")) {
                request.setLocation("Archive");
            }
            else {
                request.setLocation(location);
            }
            response = port.getMessageDetail(request);
        } catch (Exception e) {
            logger.info("Exception in getMessageDetail()");
        }

        DetailFact fact = new DetailFact();
        if (response.getMessageDetail().size() > 0) {
            fact.setBody(response.getMessageDetail().get(0));
            fact.setPatientId(response.getPatientId());
            fact.successStatus = response.isSuccessStatus();
            fact.setCCTo(response.getCCTo());
            fact.setBCCTo(response.getBCCTo());
            fact.setSentTo(response.getSentTo());
        }
        StringBuilder sb = new StringBuilder("{\n\"messageDetailFact\": \n");
        if (!response.isSuccessStatus()) {
            sb.append("\"statusMessage\": " + response.getStatusMessage() + " ,\n");
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        sb.append(gson.toJson(fact) + "\n}");
        return sb.toString();
    }

    class DetailFact {
        
        boolean successStatus;
        String body;
        String patientId;
        List<String> sentTo;
        List<String> CCTo;
        List<String> BCCTo;
        
        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public List<String> getBCCTo() {
            return BCCTo;
        }

        public void setBCCTo(List<String> BCCTo) {
            this.BCCTo = BCCTo;
        }

        public List<String> getCCTo() {
            return CCTo;
        }

        public void setCCTo(List<String> CCTo) {
            this.CCTo = CCTo;
        }

        public List<String> getSentTo() {
            return sentTo;
        }

        public void setSentTo(List<String> sentTo) {
            this.sentTo = sentTo;
        }

        public boolean isSuccessStatus() {
            return successStatus;
        }

        public void setSuccessStatus(boolean successStatus) {
            this.successStatus = successStatus;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }
        
        
    }
}
