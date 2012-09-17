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

import gov.hhs.fha.nhinc.common.dda.GetMessagesResponseType;
import javax.jws.WebService;

/**
 *
 * @author nhin
 */
@WebService(serviceName = "DisplayDataAggregator", portName = "DisplayDataAggregatorPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.aggregator.DisplayDataAggregatorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:aggregator", wsdlLocation = "WEB-INF/wsdl/DisplayDataAggregator.wsdl")
public class DisplayDataAggregator {

    public gov.hhs.fha.nhinc.common.dda.UpdateInboxStatusResponseType updateInboxStatus(gov.hhs.fha.nhinc.common.dda.UpdateInboxStatusRequestType updateInboxStatusRequest) {
        return new DisplayDataAggregatorImpl().updateInboxStatus(updateInboxStatusRequest);
    }

    public gov.hhs.fha.nhinc.common.dda.GetAvailableSourcesResponseType getAvailableSources(gov.hhs.fha.nhinc.common.dda.GetAvailableSourcesRequestType getAvailableSourcesRequest) {
        return new DisplayDataAggregatorImpl().getAvailableSources(getAvailableSourcesRequest);
    }

    public gov.hhs.fha.nhinc.common.dda.GetDetailDataResponseType getDetailData(gov.hhs.fha.nhinc.common.dda.GetDetailDataRequestType getDetailDataRequest) {
        return new DisplayDataAggregatorImpl().getDetailData(getDetailDataRequest);
    }

    public gov.hhs.fha.nhinc.common.dda.GetSummaryDataResponseType getSummaryData(gov.hhs.fha.nhinc.common.dda.GetSummaryDataRequestType getSummaryDataRequest) {
        return new DisplayDataAggregatorImpl().getSummaryData(getSummaryDataRequest);
    }

    public gov.hhs.fha.nhinc.common.dda.GetDetailDataResponseType getDetailDataForUser(gov.hhs.fha.nhinc.common.dda.GetDetailDataForUserRequestType getDetailDataForUserRequest) {
        return new DisplayDataAggregatorImpl().getDetailDataForUser(getDetailDataForUserRequest);
    }

    public gov.hhs.fha.nhinc.common.dda.GetSummaryDataResponseType getSummaryDataForUser(gov.hhs.fha.nhinc.common.dda.GetSummaryDataForUserRequestType getSummaryDataForUserRequest) {
        return new DisplayDataAggregatorImpl().getSummaryDataForUser(getSummaryDataForUserRequest);
    }

    public gov.hhs.fha.nhinc.common.dda.SetMessageDataResponseType setMessage(gov.hhs.fha.nhinc.common.dda.SetMessageDataRequestType setMessageDataRequestType) {
        return new DisplayDataAggregatorImpl().setMessage(setMessageDataRequestType);
    }

    public gov.hhs.fha.nhinc.common.dda.GetMessageDetailResponseType getMessageDetail(gov.hhs.fha.nhinc.common.dda.GetMessageDetailRequestType getMessageDetailRequest) {
        return new DisplayDataAggregatorImpl().getMessageDetail(getMessageDetailRequest);
    }

    public gov.hhs.fha.nhinc.common.dda.GetMessagesResponseType getMessages(gov.hhs.fha.nhinc.common.dda.GetMessagesRequestType getMessagesRequest) {
        GetMessagesResponseType responseType = new GetMessagesResponseType();
        try {
            responseType = new DisplayDataAggregatorImpl().getMessages(getMessagesRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseType;
    }

    public gov.hhs.fha.nhinc.common.dda.GetAddressBookResponseType getAddressBook(gov.hhs.fha.nhinc.common.dda.GetAddressBookRequestType request) {
        return new DisplayDataAggregatorImpl().getAddressBook(request);
    }

    @Deprecated
    public gov.hhs.fha.nhinc.common.dda.GetDirectoryAttributeResponseType getDirectoryAttribute(gov.hhs.fha.nhinc.common.dda.GetDirectoryAttributeRequestType request) {
        return new DisplayDataAggregatorImpl().getDirectoryAttribute(request);
    }
}
