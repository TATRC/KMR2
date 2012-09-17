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
package gov.hhs.fha.nhinc.displaydataaggregator;

//import edu.emory.mathcs.backport.java.util.Arrays;
import java.util.Arrays;
import gov.hhs.fha.nhinc.addrbookmgr.AddressBookImpl;
import gov.hhs.fha.nhinc.common.dda.DetailData;
import gov.hhs.fha.nhinc.common.dda.GetAddressBookRequestType;
import gov.hhs.fha.nhinc.common.dda.GetAddressBookResponseType;
import gov.hhs.fha.nhinc.common.dda.GetAvailableSourcesResponseType;
import gov.hhs.fha.nhinc.common.dda.GetComponentDetailDataForUserRequestType;
import gov.hhs.fha.nhinc.common.dda.GetComponentDetailDataResponseType;
import gov.hhs.fha.nhinc.common.dda.GetComponentSummaryDataForUserRequestType;
import gov.hhs.fha.nhinc.common.dda.GetComponentSummaryDataResponseType;
import gov.hhs.fha.nhinc.common.dda.GetDetailDataForUserRequestType;
import gov.hhs.fha.nhinc.common.dda.GetDetailDataResponseType;
import gov.hhs.fha.nhinc.common.dda.GetDirectoryAttributeRequestType;
import gov.hhs.fha.nhinc.common.dda.GetDirectoryAttributeResponseType;
import gov.hhs.fha.nhinc.common.dda.GetMessageDetailRequestType;
import gov.hhs.fha.nhinc.common.dda.GetMessageDetailResponseType;
import gov.hhs.fha.nhinc.common.dda.GetMessagesRequestType;
import gov.hhs.fha.nhinc.common.dda.GetMessagesResponseType;
import gov.hhs.fha.nhinc.common.dda.GetSummaryDataForUserRequestType;
import gov.hhs.fha.nhinc.common.dda.GetSummaryDataResponseType;
import gov.hhs.fha.nhinc.common.dda.NameValuesPair;
import gov.hhs.fha.nhinc.common.dda.ServiceError;
import gov.hhs.fha.nhinc.common.dda.SetMessageDataRequestType;
import gov.hhs.fha.nhinc.common.dda.SetMessageDataResponseType;
import gov.hhs.fha.nhinc.common.dda.SetMessageRequestType;
import gov.hhs.fha.nhinc.common.dda.SetMessageResponseType;
import gov.hhs.fha.nhinc.common.dda.SummaryData;
import gov.hhs.fha.nhinc.common.dda.UpdateInboxStatusResponseType;
import gov.hhs.fha.nhinc.displayalert.DisplayAlertDataUtil;
import gov.hhs.fha.nhinc.displayalert.DisplayAlertMessages;
import gov.hhs.fha.nhinc.displaydataaggregator.util.GetMessagesHandler;
import gov.hhs.fha.nhinc.displaymaildata.DisplayMailDataHandler;
import gov.hhs.fha.nhinc.displaymanager.model.InboxStatus;
import gov.hhs.fha.nhinc.displaymanager.model.InboxStatusQueryParams;
import gov.hhs.fha.nhinc.displaymanager.service.DisplayStatusException;
import gov.hhs.fha.nhinc.displaymanager.service.DisplayStatusService;
import gov.hhs.fha.nhinc.docmgr.DocumentManagerImpl;
import gov.hhs.fha.nhinc.kmr.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.kmr.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.kmr.ura.IdAddressBean;
import gov.hhs.fha.nhinc.kmr.ura.UniversalResourceAddressBean;
import gov.hhs.fha.nhinc.kmr.ura.UniversalResourceAddressBeanFactory;
import gov.hhs.fha.nhinc.ldapaccess.ContactDAO;
import gov.hhs.fha.nhinc.ldapaccess.ContactDTO;
import gov.hhs.fha.nhinc.ldapaccess.LdapService;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author cmatser
 */
public class DisplayDataAggregatorImpl {

    /** Property constants. */
    public static final String PROPERTY_FILE = "displayAggregator";
    public static final String DATA_SOURCE_COUNT = "dataSource.count";
    public static final String DATA_SOURCE_PREFIX = "dataSource.";
    public static final String DATA_SOURCE_NAME = ".name";
    public static final String DATA_SOURCE_URL = ".url";
    /** Max data sources. */
    public static final Integer MAX_DATA_SOURCES = 100;
    /** Error code. */
    public static final Integer ERROR_CODE = -1;
    /** Sources where we fetch status values. */
    public static final String STATUS_NHIN_DOCS = "NHIN Documents";
    /** Item names for name value pairs. */
    public static final String ITEM_READ_STATUS = "Read";
    private static Log log = LogFactory.getLog(DisplayDataAggregatorImpl.class);

    /**
     * Update Inbox status with passed params.
     * 
     * @param request
     * @return
     */
    public gov.hhs.fha.nhinc.common.dda.UpdateInboxStatusResponseType updateInboxStatus(gov.hhs.fha.nhinc.common.dda.UpdateInboxStatusRequestType request) {
        UpdateInboxStatusResponseType response = new UpdateInboxStatusResponseType();

        if ((request.getUserId() == null) || request.getUserId().isEmpty()) {
            ServiceError error = new ServiceError();
            error.setCode(-1);
            error.setText("Missing user.");
            response.getErrorList().add(error);
        }

        if ((request.getDataSource() == null) || request.getDataSource().isEmpty()) {
            ServiceError error = new ServiceError();
            error.setCode(-1);
            error.setText("Missing data source.");
            response.getErrorList().add(error);
        }

        if ((request.getItemId() == null) || request.getItemId().isEmpty()) {
            ServiceError error = new ServiceError();
            error.setCode(-1);
            error.setText("Missing item.");
            response.getErrorList().add(error);
        }

        //Check for errors
        if (!response.getErrorList().isEmpty()) {
            return response;
        }

        DisplayStatusService service = new DisplayStatusService();

        //First find existing
        InboxStatusQueryParams params = new InboxStatusQueryParams();
        params.setUser(request.getUserId());
        params.setSource(request.getDataSource());
        params.setItem(request.getItemId());
        List<InboxStatus> list = service.inboxStatusQuery(params);

        //Pull out existing or create new
        InboxStatus status = null;
        if ((list == null) || (list.isEmpty())) {
            status = new InboxStatus();
            status.setUser(request.getUserId());
            status.setSource(request.getDataSource());
            status.setItem(request.getItemId());
        } else {
            status = list.get(0);
        }

        //Update status
        status.setRead(request.isRead());

        //Save or update in db
        service.saveInboxStatus(status);

        return response;
    }

    /**
     * Return data source list.
     * 
     * @param getAvailableSourcesRequest
     * @return
     */
    public gov.hhs.fha.nhinc.common.dda.GetAvailableSourcesResponseType getAvailableSources(gov.hhs.fha.nhinc.common.dda.GetAvailableSourcesRequestType getAvailableSourcesRequest) {
        GetAvailableSourcesResponseType response = new GetAvailableSourcesResponseType();
        Hashtable<String, String> sourceTable = new Hashtable<String, String>();

        try {
            ServiceError dataSourceError = getDataSources(sourceTable);
            if (dataSourceError != null) {
                throw new Exception("Error retrieving data sources.");
            }

            if (sourceTable.size() == 0) {
                throw new Exception("No data sources defined.");
            }

            for (String dataSource : sourceTable.keySet()) {
                response.getReturn().add(dataSource);
            }
        } catch (Exception e) {
            String errorMsg = "Error getting data sources.";
            log.error(errorMsg, e);
            response.getReturn().add(errorMsg + ". " + e.getMessage());
        }

        return response;
    }

    /**
     * Handle detail data sources.
     * 
     * @param request
     * @return
     */
    public gov.hhs.fha.nhinc.common.dda.GetDetailDataResponseType getDetailData(gov.hhs.fha.nhinc.common.dda.GetDetailDataRequestType request) {
        GetDetailDataForUserRequestType newRequest = new GetDetailDataForUserRequestType();
        newRequest.setDataSource(request.getDataSource());
        newRequest.setItemId(request.getItemId());
        newRequest.setUserId(null);
        return getDetailDataForUser(newRequest);
    }

    /**
     * Handle summary data sources.  This method may return partial results.  Always
     * check the error results.
     *
     * @param request
     * @return
     */
    public gov.hhs.fha.nhinc.common.dda.GetSummaryDataResponseType getSummaryData(gov.hhs.fha.nhinc.common.dda.GetSummaryDataRequestType request) {
        GetSummaryDataForUserRequestType newRequest = new GetSummaryDataForUserRequestType();
        newRequest.setGroupId(request.getGroupId());
        newRequest.setLocationId(request.getLocationId());
        newRequest.setPatientId(request.getPatientId());
        newRequest.setProviderId(request.getProviderId());
        newRequest.setUserId(null);
        newRequest.setOnlyNew(false);
        return getSummaryDataForUser(newRequest);
    }

    /**
     * Handle detail data sources requested by a specified user.
     *
     * @param request
     * @return
     */
    public gov.hhs.fha.nhinc.common.dda.GetDetailDataResponseType getDetailDataForUser(gov.hhs.fha.nhinc.common.dda.GetDetailDataForUserRequestType request) {
        GetDetailDataResponseType response = new GetDetailDataResponseType();
        Hashtable<String, String> sourceTable = new Hashtable<String, String>();

        try {
            ServiceError dataSourceError = getDataSources(sourceTable);
            if (dataSourceError != null) {
                throw new Exception("Error retrieving data sources.");
            }

            if (sourceTable.size() == 0) {
                throw new Exception("No data sources defined.");
            }

            //Get data source url
            String dataSourceUrl = sourceTable.get(request.getDataSource());
            if (dataSourceUrl == null) {
                throw new Exception("Requested data source is not configured: " + request.getDataSource());
            }

            //Convert userId
            String userId = request.getUserId();
            try {
                UniversalResourceAddressBean beanId = UniversalResourceAddressBeanFactory.getInstance().createUniversalResourceBean(request.getUserId());
                if (beanId instanceof IdAddressBean) {
                    userId = ((IdAddressBean) beanId).getId();
                }
            } catch (Exception e) {
                log.warn("Bad user id: " + userId + ", " + e.getMessage());
            }

            GetComponentDetailDataForUserRequestType componentRequest = new GetComponentDetailDataForUserRequestType();
            componentRequest.setUserId(userId);
            componentRequest.setItemId(request.getItemId());

            DisplayAlertDataUtil alert = new DisplayAlertDataUtil();

            GetComponentDetailDataResponseType componentResponse =
                    alert.getComponentDetailDataForUser(DisplayAlertDataUtil.DATA_SOURCE_ALERTS, componentRequest);
            response.setDetailObject(componentResponse.getDetailObject());
            response.getErrorList().addAll(componentResponse.getErrorList());
        } catch (Exception e) {
            String errorMsg = "Error getting detail data.";
            log.error(errorMsg, e);
            ServiceError error = new ServiceError();
            error.setCode(ERROR_CODE);
            error.setText(errorMsg + ". " + e.getMessage());
            response.getErrorList().add(error);
        }

        //Get "read" status as needed
        ServiceError error = addReadStatus(request.getUserId(), response.getDetailObject());
        if (error != null) {
            response.getErrorList().add(error);
        }

        return response;
    }

    public gov.hhs.fha.nhinc.common.dda.GetSummaryDataResponseType getSummaryDataForUser(gov.hhs.fha.nhinc.common.dda.GetSummaryDataForUserRequestType request) {
        GetSummaryDataResponseType response = new GetSummaryDataResponseType();
        Hashtable<String, String> sourceTable = new Hashtable<String, String>();

        try {
            ServiceError dataSourceError = getDataSources(sourceTable);
            if (dataSourceError != null) {
                throw new Exception("Error retrieving data sources.");
            }

            if (sourceTable.size() == 0) {
                throw new Exception("No data sources defined.");
            }

            //Convert userId
            String userId = request.getUserId();
            try {
                UniversalResourceAddressBean beanId = UniversalResourceAddressBeanFactory.getInstance().createUniversalResourceBean(request.getUserId());
                if (beanId instanceof IdAddressBean) {
                    userId = ((IdAddressBean) beanId).getId();
                }
            } catch (Exception e) {
                log.warn("Bad user id: " + userId + ", " + e.getMessage());
            }

            for (String dataSource : request.getDataSources()) {
                String dataSourceUrl = sourceTable.get(dataSource);
                if (dataSourceUrl == null) {
                    throw new Exception("Requested data source is not configured: " + dataSource);
                }

                try {
//                    gov.hhs.fha.nhinc.aggregator.DisplayDataComponent service = new gov.hhs.fha.nhinc.aggregator.DisplayDataComponent();
//                    gov.hhs.fha.nhinc.aggregator.DisplayDataComponentPortType port = service.getDisplayDataComponentPortSoap11();
//                    ((javax.xml.ws.BindingProvider) port).getRequestContext().put(
//                            javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
//                            dataSourceUrl);
                    DisplayAlertDataUtil util = new DisplayAlertDataUtil();

                    GetComponentSummaryDataForUserRequestType componentRequest = new GetComponentSummaryDataForUserRequestType();
                    componentRequest.setUserId(userId);
                    componentRequest.setGroupId(request.getGroupId());
                    componentRequest.setLocationId(request.getLocationId());
                    componentRequest.setPatientId(request.getPatientId());
                    componentRequest.setProviderId(request.getProviderId());
                    componentRequest.setOnlyNew(request.isOnlyNew());
                    componentRequest.setArchive(request.isArchive());
                    GetComponentSummaryDataResponseType componentResponse = util.getComponentSummaryDataForUser("Alert", componentRequest);
                    response.getSummaryObjects().addAll(componentResponse.getSummaryObjects());
                } catch (Exception e) {
                    String errorMsg = "Error getting component summary data from: " + dataSource;
                    log.error(errorMsg, e);
                    ServiceError error = new ServiceError();
                    error.setCode(ERROR_CODE);
                    error.setText(errorMsg + ". " + e.getMessage());
                    response.getErrorList().add(error);
                }

            } //for

        } catch (Exception e) {
            String errorMsg = "Error getting summary data.";
            log.error(errorMsg, e);
            ServiceError error = new ServiceError();
            error.setCode(ERROR_CODE);
            error.setText(errorMsg + ". " + e.getMessage());
            response.getErrorList().add(error);
        }

        //Get "read" status as needed
        ServiceError error = addReadStatus(request.getUserId(), response.getSummaryObjects());
        if (error != null) {
            response.getErrorList().add(error);
        }

        return response;
    }

    private String checkDataSource(String msgType) throws Exception {
        Hashtable<String, String> sourceTable = new Hashtable<String, String>();
        ServiceError dataSourceError = getDataSources(sourceTable);
        if (dataSourceError != null) {
            throw new Exception("Error retrieving data sources.");
        }
        if (sourceTable.size() == 0) {
            throw new Exception("No data sources defined.");
        }
        String dataSourceUrl = sourceTable.get(msgType);
        if (dataSourceUrl == null) {
            throw new Exception("Requested data source is not configured: " + msgType);
        }
        return dataSourceUrl;
    }

    /**
     * Read data sources from the property file.
     *
     * @param dataSources
     * @return
     */
    private ServiceError getDataSources(Map<String, String> dataSources) {
        ServiceError response = null;

        try {
            Long dataSourceCount = PropertyAccessor.getPropertyLong(PROPERTY_FILE, DATA_SOURCE_COUNT);

            if (dataSourceCount == null) {
                throw new PropertyAccessException("Property was null: " + DATA_SOURCE_COUNT);
            }

            for (int i = 1; dataSources.size() < dataSourceCount; i++) {
                String dataSourceName = PropertyAccessor.getProperty(PROPERTY_FILE, DATA_SOURCE_PREFIX + i + DATA_SOURCE_NAME);
                String dataSourceUrl = PropertyAccessor.getProperty(PROPERTY_FILE, DATA_SOURCE_PREFIX + i + DATA_SOURCE_URL);

                if (dataSourceName != null) {
                    dataSources.put(dataSourceName, dataSourceUrl);
                    log.info("PUT IN DATASOURCES: " + dataSourceName + " URL: " + dataSourceUrl);
                }

                if (i > MAX_DATA_SOURCES) {
                    break;
                }
            }

        } catch (PropertyAccessException e) {
            String errorMsg = "Error accessing properties in file:" + PROPERTY_FILE;
            log.error(errorMsg, e);
            response = new ServiceError();
            response.setCode(ERROR_CODE);
            response.setText(errorMsg + ". " + e.getMessage());
        }

        return response;
    }

    /**
     * Check InboxStatus and add to summary response if necessary.
     */
    private ServiceError addReadStatus(String user, List<SummaryData> summaryList) {
        ServiceError response = null;

        if (summaryList == null) {
            return null;
        }

        Iterator<SummaryData> iterator = summaryList.iterator();
        while (iterator.hasNext()) {
            SummaryData item = iterator.next();

            if (STATUS_NHIN_DOCS.equals(item.getDataSource())) {
                boolean readStatus = false;

                try {
                    readStatus = getReadStatusForItem(user, item.getDataSource(), item.getItemId());
                } catch (Exception e) {
                    String errorMsg = "Error accessing read status for user:" + user;
                    log.error(errorMsg, e);
                    response = new ServiceError();
                    response.setCode(ERROR_CODE);
                    response.setText(errorMsg + ". " + e.getMessage());
                }

                addNameValue(item.getItemValues(), ITEM_READ_STATUS, readStatus);
            }
        }

        return response;
    }

    /**
     * Check InboxStatus and add to detail response if necessary.
     */
    private ServiceError addReadStatus(String user, DetailData item) {
        ServiceError response = null;

        if (item == null) {
            return null;
        }

        if (STATUS_NHIN_DOCS.equals(item.getDataSource())) {
            boolean readStatus = false;

            try {
                readStatus = getReadStatusForItem(user, item.getDataSource(), item.getItemId());
            } catch (Exception e) {
                String errorMsg = "Error accessing read status for user:" + user;
                log.error(errorMsg, e);
                response = new ServiceError();
                response.setCode(ERROR_CODE);
                response.setText(errorMsg + ". " + e.getMessage());
            }

            addNameValue(item.getItemValues(), ITEM_READ_STATUS, readStatus);
        }

        return response;
    }

    /**
     * Retrieve InboxStatus
     */
    private boolean getReadStatusForItem(String user, String source, String item)
            throws DisplayStatusException {
        boolean response = false;

        DisplayStatusService service = new DisplayStatusService();

        InboxStatusQueryParams params = new InboxStatusQueryParams();
        params.setSource(source);
        params.setUser(user);
        params.setItem(item);

        List<InboxStatus> statusList = service.inboxStatusQuery(params);
        if ((statusList != null) && (statusList.size() > 0)) {
            response = statusList.get(0).isRead();
        }

        return response;
    }

    /**
     * Add name/value pair to response.
     *
     * @param pairList
     * @param name
     * @param value
     */
    private void addNameValue(List<NameValuesPair> pairList, String name, Object value) {
        NameValuesPair nameVal = new NameValuesPair();
        nameVal.setName(name);
        nameVal.getValues().add(String.valueOf(value));
        pairList.add(nameVal);

        return;
    }

    public GetMessageDetailResponseType getMessageDetail(GetMessageDetailRequestType request) {
        GetMessageDetailResponseType response = new GetMessageDetailResponseType();
        if (request.getMessageType().equals("Alert")) {
            DisplayAlertMessages alertMessages = new DisplayAlertMessages();
            response = alertMessages.getMessageDetail(request);
            if (response.getMessageDetail().isEmpty()) {
                response.setSuccessStatus(true);
                response.setStatusMessage("No detail found for message id " + request.getMessageId());
            } else {
                response.setSuccessStatus(true);
            }
        } else if (request.getMessageType().equals("Email") || request.getMessageType().equalsIgnoreCase("Archive")) {
            DisplayMailDataHandler handler = new DisplayMailDataHandler();
            try {
                response = handler.getMessageDetail(request);
                response.setSuccessStatus(true);
            } catch (Exception e) {
                response.setSuccessStatus(false);
                response.setStatusMessage("Error found in getting message detail: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (request.getMessageType().equals("Document")) {
            DocumentManagerImpl docMgr = new DocumentManagerImpl();
            response = docMgr.getMessageDetail(request);
            if (response.getMessageDetail().isEmpty()) {
                response.setSuccessStatus(true);
                response.setStatusMessage("No detail found for message id " + request.getMessageId());
            } else {
                response.setSuccessStatus(true);
            }
        }
        return response;
    }

    /**
     * getMessages()
     *
     * @param request
     * @return
     * @throws Exception
     */
    public GetMessagesResponseType getMessages(GetMessagesRequestType request) throws Exception {
        GetMessagesResponseType response = new GetMessagesResponseType();
        String messageType = request.getMessageType();

        GetMessagesHandler handler = new GetMessagesHandler();

        /* Where messageType is
         * "All"   - get all emails, alerts, and docs which are not Archived or Trashed.
         * "Email" - get all emails which are not Archived or Trashed.
         * "Alert" - get all alerts which are not Archived or Trashed.
         * "Document" - get all documents which are not Archived or Trashed.
         *
         */
        if (messageType == null || messageType.isEmpty() || messageType.equalsIgnoreCase("All"))
        {
            //GETTING all msg types (Alerts, Emails, Docs)
            response = handler.getAllMessages(request);

        } else if (messageType.equalsIgnoreCase("Email")) {
            response = handler.getEmailMessages(request);

        } else if (messageType.equalsIgnoreCase("Alert")) {
            response = handler.getAlertMessages(request);

        } else if (messageType.equalsIgnoreCase("Document")) {
            response = handler.getDocumentMessages(request);
        }

        if (response.getGetMessageResponse().isEmpty()) {
            GetMessagesResponseType.GetMessageResponse zeroResp =
                    new GetMessagesResponseType.GetMessageResponse();
            zeroResp.setSuccessStatus(true);
            response.getGetMessageResponse().add(zeroResp);
        }
        return response;
    }

    /**
     * setMessage()
     * 
     * @param request
     * @return
     */
    public SetMessageDataResponseType setMessage(SetMessageDataRequestType request) {
        SetMessageDataResponseType response = new SetMessageDataResponseType();

//        Map<String, String> sourceTable = new HashMap<String, String>();
//
//        ServiceError dataSourceError = getDataSources(sourceTable);
//        if (dataSourceError != null) {
//            response.setSuccessStatus(false);
//            response.setMessage("Error retriving data sources.");
//            return response;
//        }
//
//        if (sourceTable.isEmpty()) {
//            response.setSuccessStatus(false);
//            response.setMessage("No data sources defined.");
//            return response;
//        }
//
//        //Get data source url
//        String dataSourceUrl = sourceTable.get(request.getDataSource());
//        if (dataSourceUrl == null) {
//            response.setSuccessStatus(false);
//            response.setMessage("Requested data source is not configured: " + request.getDataSource());
//            return response;
//        }
//        log.info("Data source URL is " + dataSourceUrl);

        //Call destination
        if (request.getDataSource().equals("Alert")) {
            response = setAlertMessage(request, response);

        } else if (request.getDataSource().equals("Email")) {
            response = setEmailMessage(request, response);

        } else if (request.getDataSource().equals("Document")) {
            response = setDocumentMessage(request);
        }


        return response;
    }

    private SetMessageDataResponseType setDocumentMessage(SetMessageDataRequestType request) {
        DocumentManagerImpl docMgr = new DocumentManagerImpl();
        return docMgr.setMessage(request);
    }

    private SetMessageDataResponseType setAlertMessage(SetMessageDataRequestType request, SetMessageDataResponseType response) {
        SetMessageRequestType componentRequest = setMessageComponentRequest(request);
        DisplayAlertDataUtil util = new DisplayAlertDataUtil();
        SetMessageResponseType componentResponse = util.setMessage(componentRequest);
        
        response.setMessage(componentResponse.getMessage());
        response.setSuccessStatus(componentResponse.isSuccessStatus());
        return response;
    }

    /**
     *
     * @param request
     *          .action= Read, Send (reply/reply all/forwarding),
     *                   Delete, Archive, Update (labels only)
     *                   Save, Unread, Undelete, Unarchive, PermanentlyDelete
     *
     *          .types = "Email"
     *          .location = "Inbox", "Sent", "Draft", "Archived", or "UserTrash"
     *                      Not required for New messages.
     * @param response
     * @return
     */
    private SetMessageDataResponseType setEmailMessage(
                                            SetMessageDataRequestType request
                                           ,SetMessageDataResponseType response)
    {
        DisplayMailDataHandler handler = new DisplayMailDataHandler();
        SetMessageRequestType setRequest = setMessageComponentRequest(request);
        SetMessageResponseType mailResponse = new SetMessageResponseType();

        if (   request.getAction().equalsIgnoreCase("Send")
            || request.getAction().equalsIgnoreCase("Update")
            || request.getAction().equalsIgnoreCase("Read")
            || request.getAction().equalsIgnoreCase("Save"))
        {
            mailResponse = handler.setMessage(setRequest);

        } else if (request.getAction().equalsIgnoreCase("Archive")) {
            mailResponse = handler.archiveMessage(setRequest);

        } else if (request.getAction().equalsIgnoreCase("Delete")) {
            mailResponse = handler.deleteMessage(setRequest);
        }

        if (mailResponse.isSuccessStatus()) {
            response.setSuccessStatus(true);

        } else {
            response.setSuccessStatus(false);
            response.setMessage("Set Messages failed");
        }
        
        return response;
    }

    private SetMessageRequestType setMessageComponentRequest(SetMessageDataRequestType request) {
        SetMessageRequestType componentRequest = new SetMessageRequestType();
        componentRequest.setUserId(request.getUserId());
        componentRequest.setPatientId(request.getPatientId());
        componentRequest.setAction(request.getAction());
        componentRequest.setAttachment(request.getAttachment());
        componentRequest.getContactBCC().addAll(request.getContactBCC());
        componentRequest.getContactCC().addAll(request.getContactCC());
        componentRequest.getContactTo().addAll(request.getContactTo());
        componentRequest.setDocument(request.getAttachment());
        componentRequest.getLabels().addAll(request.getLabels());
        componentRequest.setMessageId(request.getMessageId());
        componentRequest.setTasks(request.getTasks());
        componentRequest.setSubject(request.getSubject());
        componentRequest.setBody(request.getBody());
        componentRequest.setLocation(request.getLocation());
        return componentRequest;
    }

    GetAddressBookResponseType getAddressBook(GetAddressBookRequestType request) {
        AddressBookImpl addrBook = new AddressBookImpl();
        return addrBook.getAddrBookForUserId(request.getUserId());
    }

    GetDirectoryAttributeResponseType getDirectoryAttribute(GetDirectoryAttributeRequestType request) {
        return getValueList(request);
    }
    
    // Can move to other project if desired
    private GetDirectoryAttributeResponseType getValueList(GetDirectoryAttributeRequestType request) {
        GetDirectoryAttributeResponseType response = new GetDirectoryAttributeResponseType();
        String uid = request.getUid();
        List<String> names = request.getNames();

        for (String name : names) {
            response.getValues().add(getLdapValue(uid, name));
        }

        return response;
    }
      
    private String getLdapValue(String uid, String name) {

        List<String> nameOptions =
                Arrays.asList(new String[]{"cn","mobile", "employeeNumber",
                    "displayName", "gender"});

        if (!nameOptions.contains(name)) {
            return (name + " is not currently supported.");
        }

        String value = "";

        ContactDAO contactDAO = LdapService.getContactDAO();
        List<ContactDTO> contacts = contactDAO.findContact("uid=" + uid);
        ContactDTO contact = contacts.get(0);
        if (name.equals("cn")) {
            value = contact.getCommonName();
        }
        if (name.equals("mobile")) {
            value = contact.getMobile();
        }
        if (name.equals("employeeNumber")) {
            value = contact.getEmployeeNumber();
        }
        if (name.equals("displayName")) {
            value = contact.getDisplayName();
        }
        if (name.equals("gender")) {
            value = contact.getGender();
        }
        return value;
    }
}
