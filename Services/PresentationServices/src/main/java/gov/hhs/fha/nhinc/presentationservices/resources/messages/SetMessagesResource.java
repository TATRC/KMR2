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

import gov.hhs.fha.nhinc.aggregator.DisplayDataAggregator;
import gov.hhs.fha.nhinc.aggregator.DisplayDataAggregatorPortType;
import gov.hhs.fha.nhinc.common.dda.SetMessageDataRequestType;
import gov.hhs.fha.nhinc.common.dda.SetMessageDataResponseType;
import gov.hhs.fha.nhinc.kmr.util.CommonUtil;
import gov.hhs.fha.nhinc.kmr.util.SessionUtilities;
import gov.hhs.fha.nhinc.presentationservices.helpers.ErrorResponse;
import gov.hhs.fha.nhinc.presentationservices.helpers.JSONHelper;
import gov.hhs.fha.nhinc.presentationservices.helpers.ParameterValidator;
import gov.hhs.fha.nhinc.presentationservices.resources.BaseResource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

/**
 *
 * @author Sushma
 */
public class SetMessagesResource extends BaseResource {

    public Form form;
    private String userId = "";
    private String token = "";
    private String patientId;
    private List messageIds = new ArrayList();
    /** action can be update, new, delete etc */
    private String action = "";
    private String subject;
    private List contactTo = new ArrayList();
    private List contactCC = new ArrayList();
    private List contactBCC = new ArrayList();
    private String body;
    private String document = "";
    /** Folders which represent flagged or not */
    private List labels = new ArrayList();

//    private List<String> locations = new ArrayList();
    private String location = "";

    /** Status which represents archived, unread etc */
    private String attachment = "";
    private List messageTypes = new ArrayList();


    //final Logger logger = Logger.getLogger(SetMessagesResource.class.getName());
    private static Log logger = LogFactory.getLog(SetMessagesResource.class);

    SetMessageDataResponseType output = new SetMessageDataResponseType();
    private String LABEL = "Label";
    private String ALERT = "Alert";
    private String EMAIL = "EMail";
    private String ARCHIVE = "Archive";
    private String DOCUMENT = "Document";

    /**
     * Creates a new SetMessagesResource object.
     *
     * @param  context
     * @param  request
     * @param  response
     */
    public SetMessagesResource(Context context, Request request, Response response) {
        super(context, request, response);
        init(context, request, response);
        setModifiable(true);
        Representation rep = request.getEntity();
        form = new Form(rep);
        setParameters(form);   //GET ALL INPUT PARAMS
    }
    
    @Override
    public void init(Context context, Request request, Response response) {
        SessionUtilities.setCORSHeaders(this);
        super.init(context, request, response);
    }

    /**
     * Handle PUT requests. replace or update resource
     */
    @Override
    public void storeRepresentation(Representation entity)
            throws ResourceException {
        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    /**
     * @see  org.restlet.resource.Resource#allowPut()
     */
    @Override
    public boolean allowPut() {
        return false;
    }

    @Override
    public boolean allowPost() {
        return true;
    }

    /**
     * Handle DELETE requests. remove/delete resource
     */
    @Override
    public void removeRepresentations() throws ResourceException {
        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    /**
     * @see  org.restlet.resource.Resource #acceptRepresentation(org.restlet.resource.Representation)
     */
    @Override
    public void acceptRepresentation(Representation entity)
            throws ResourceException {

        if (!SessionUtilities.verifyToken(token)) {
            return;
        }
        String failures = validateFields();

        if (failures.length() > 1) {
            String errorMessage = "setMessages: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "getMessagesFact");
            String ret = err.generateError();
            return;
        }

        //PROCESSING....
        output = this.setMessages(userId, messageIds, messageTypes, labels);

        if (!output.isSuccessStatus()) {
            generateError("SetMessages is not successful");
            return;
        }

        output.setSuccessStatus(true);
        output.setMessage("Set Messages successful");
        getResponse().setStatus(Status.SUCCESS_OK);
        getResponse().setEntity(JSONHelper.getResponse(output).toString(),
                MediaType.APPLICATION_JSON);
    }

    /**
     * 
     * @param setMessageRequest
     * @param messageId
     * @param userId
     * @param messageIdWithType
     * @param j
     * @param labels
     */
    private void createMsgRequest( SetMessageDataRequestType setMessageRequest
                                  ,String messageId
                                  ,String userId
                                  ,Map messageIdWithType
                                  ,int j
                                  ,List labels)
    {
        if (!CommonUtil.strNullorEmpty(messageId)) {
            setMessageRequest.setMessageId(messageId.toString());
        }
        setMessageRequest.setUserId(userId);
        
        if (!CommonUtil.strNullorEmpty(patientId)) {
            setMessageRequest.setPatientId(patientId);
        }
        setMessageRequest.setAction(action);

        setMessageRequest.setLocation(this.location);
//        if (   (action.equalsIgnoreCase("Send") || messageIdWithType.get(messageId).equals("Email"))
//            && locations.size() >= j)
//        {
//            setMessageRequest.setLocation(locations.get(j));
//        }

        //
        if (!CommonUtil.mapNullorEmpty(messageIdWithType)) {
            setMessageRequest.setDataSource(messageIdWithType.get(messageId).toString());
        }
        else {
            setMessageRequest.setDataSource("Email");
        }

        setMessageRequest.setSubject(subject);
        setMessageRequest.setBody(body);

        if (CommonUtil.listNullorEmpty(setMessageRequest.getContactTo())) {
            setMessageRequest.getContactTo().addAll(contactTo);
        }

        setMessageRequest.getContactCC().addAll(contactCC);
        setMessageRequest.getContactBCC().addAll(contactBCC);

        //
        if (labels != null && !labels.isEmpty()) {
            setMessageRequest.getLabels().addAll(labels);
        }
    }

    private void createSaveRequest(SetMessageDataRequestType setMessageRequest,
            String userId, List labels) {
        setMessageRequest.setUserId(userId);
        setMessageRequest.setAction(action);
        setMessageRequest.setLocation("Draft");
        setMessageRequest.setDataSource("Email");
        setMessageRequest.setSubject(subject);
        if (!CommonUtil.strNullorEmpty(body)) {
            setMessageRequest.setBody(body);
        }
        else {
            setMessageRequest.setBody(" ");
        }
        setMessageRequest.getContactTo().addAll(contactTo);
        setMessageRequest.getContactCC().addAll(contactCC);
        setMessageRequest.getContactBCC().addAll(contactBCC);
        if (labels != null && !labels.isEmpty()) {
            setMessageRequest.getLabels().addAll(labels);
        }
    }

    private String validateFields() {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("action", action);
        fields.put("userId", userId);
        if (!action.equals("Send")) {
            fields.put("messageIds", messageIds);
        }
        //fields.put("messageTypes", messageTypes);
        ParameterValidator validator = new ParameterValidator(fields);
        String failures = validator.validateMissingOrEmpty();
        return failures;
    }

    private void generateError(String errorMsg) {
        output.setSuccessStatus(false);
        output.setMessage(errorMsg);
        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        getResponse().setEntity(JSONHelper.getResponse(output).toString(), MediaType.APPLICATION_JSON);
        logger.error(errorMsg);
    }


    /**
     *
     * @param userId
     * @param messageIds
     * @param messageTypes
     * @param labels
     * @return
     */
    private SetMessageDataResponseType setMessages( String userId
                                                   ,List<String> messageIds
                                                   ,List<String> messageTypes
                                                   ,List<String> labels) 
    {
        DisplayDataAggregator service = new gov.hhs.fha.nhinc.aggregator.DisplayDataAggregator();
        DisplayDataAggregatorPortType port = service.getDisplayDataAggregatorPortSoap11();
        
        ((BindingProvider) port).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                getProperty("DisplayDataAggregatorService"));

        SetMessageDataRequestType setMessageRequest = new SetMessageDataRequestType();
        SetMessageDataResponseType setMessageResponse = new SetMessageDataResponseType();

        try {
            int j = 0; //indicates how many msgs needs to be process.
            Map messageIdWithType = new HashMap();


            //------------------------------------------------
            // WHEN msg already exists
            //------------------------------------------------
            if (!CommonUtil.listNullorEmpty(messageIds)) {
                for (int i = 0; i < messageIds.size(); i++) {
                    messageIdWithType.put(messageIds.get(i), messageTypes.get(i));
                }
                for (String messageId : messageIds) {
                    createMsgRequest(setMessageRequest, messageId, userId, messageIdWithType, j, labels);
                    setMessageResponse = port.setMessage(setMessageRequest);
                    ++j;
                }
            }
            //------------------------------------------------
            // WHEN msg is new and user wants to do a SAVE
            //------------------------------------------------
            else if (action.equalsIgnoreCase("Save")) {
                createSaveRequest(setMessageRequest, userId, labels);
                setMessageResponse = port.setMessage(setMessageRequest);
            }
            //------------------------------------------------
            // WHEN msg is new and user wants to do a SEND
            //------------------------------------------------
            else if (action.equalsIgnoreCase("Send")) {
                createMsgRequest(setMessageRequest, null, userId, null, 0, labels);
                setMessageResponse = port.setMessage(setMessageRequest);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            setMessageResponse.setSuccessStatus(false);
            setMessageResponse.setMessage(e.toString());
            getResponse().setStatus(Status.SUCCESS_OK);
            getResponse().setEntity(JSONHelper.getResponse(setMessageResponse).toString(),
                    MediaType.APPLICATION_JSON);
            e.printStackTrace();
        }

        return setMessageResponse;
    }

    private boolean getRequestParameter(Form form, String param) {
        if (null != form.getFirst(param)) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param form
     */
    private void setParameters(Form form) {

        if (getRequestParameter(form, "body")) {
            body = form.getFirst("body").getValue();
            logger.info("body: " + body);
        }
        if (getRequestParameter(form, "action")) {
            action = form.getFirst("action").getValue();
            logger.info("action: " + action);
        }
        if (getRequestParameter(form, "userId")) {
            userId = form.getFirst("userId").getValue();
            logger.info("userID:" +userId);
        }
        if (getRequestParameter(form, "patientId")) {
            patientId = form.getFirst("patientId").getValue();
            logger.info("patientId:"+ patientId);
        }
        if (getRequestParameter(form, "token")) {
            token = form.getFirst("token").getValue();
            logger.info("token:" + token);
        }
        if (getRequestParameter(form, "subject")) {
            subject = form.getFirst("subject").getValue();
            logger.info("subject: " + subject);
        }
        if (getRequestParameter(form, "messageIds")) {         
            messageIds = SessionUtilities.getParameterValues(form, "messageIds");
            logger.info("messageIDs:" + messageIds);
        }

        if (getRequestParameter(form, "location")) {
            location = form.getFirst("location").getValue();
            logger.info("location:" + location);
        }
//        if (getRequestParameter(form, "locations")) {
//            locations = SessionUtilities.getParameterValues(form, "locations");
//            logger.info("locations:" + locations);
//        }

        if (getRequestParameter(form, "labels")) {
            labels = SessionUtilities.getParameterValues(form, "labels");
            logger.info("labels:" + labels);
        }
        if (getRequestParameter(form, "types")) {
            messageTypes = SessionUtilities.getParameterValues(form, "types");
            logger.info("Types:" + messageTypes);
        }
        if (getRequestParameter(form, "sendTo")) {
            contactTo = SessionUtilities.getParameterValues(form, "sendTo");
        }
        if (getRequestParameter(form, "CCTo")) {
            contactCC = SessionUtilities.getParameterValues(form, "CCTo");
        }
        if (getRequestParameter(form, "BCCTo")) {
            contactBCC = SessionUtilities.getParameterValues(form, "BCCTo");
        }
    }

    private boolean isvalidMessageType(String messageType) {
        return (messageType.equalsIgnoreCase(ALERT) || messageType.equalsIgnoreCase(EMAIL)
                || messageType.equalsIgnoreCase(ARCHIVE) || messageType.equalsIgnoreCase(DOCUMENT));
    }

}
