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
package gov.hhs.fha.nhinc.taskmanager;

import gov.hhs.fha.nhinc.displayalert.DeliverMsgUtil;
import gov.hhs.fha.nhinc.dsa.DeliverMessageRequestType;
import gov.hhs.fha.nhinc.dsa.DeliverMessageResponseType;
import gov.hhs.fha.nhinc.taskmanager.model.TaskServiceRef;
import gov.hhs.fha.nhinc.taskmanager.service.TaskService;
import java.util.List;

/**
 *
 * @author nhin
 */
public class DeliverMsgHandler {

    public static final Long SERVICE_TASK_ID = 3L;

    public DeliverMsgHandler() {
    }

    public gov.hhs.fha.nhinc.dsa.DeliverMessageResponseType deliverMessage(DeliverMessageRequestType request) {

        DeliverMessageResponseType response = new DeliverMessageResponseType();

        for (Object o : request.getType()) {
            String type = (String) o;
            
            if (type.contains("ALERT")) {
                response = new DeliverMsgUtil().deliverMsg(request, "/home/nhin/Properties");
            }
            
            if (type.contains("SMS")) {
                SwiftSMSHandler sms = new SwiftSMSHandler();

                String taskTicket = request.getRefId();
                List<String> mobiles = request.getMainRecipients();
                String msg = request.getBody();

                TaskService service = new TaskService();
                TaskServiceRef ref = service.getServiceRef(SERVICE_TASK_ID);

                try {
                    sms.sendSMSMessage(taskTicket, mobiles, msg, ref);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus("Failure: " + e.getMessage());
                    return response;
                }
                response.setStatus("Success");
            }
        }
        return response;
    }
}
