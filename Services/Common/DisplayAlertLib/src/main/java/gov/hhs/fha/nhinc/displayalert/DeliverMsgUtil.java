/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.displayalert;

import gov.hhs.fha.nhinc.alertmanager.model.AlertAction;
import gov.hhs.fha.nhinc.alertmanager.model.AlertContact;
import gov.hhs.fha.nhinc.alertmanager.model.AlertStatus;
import gov.hhs.fha.nhinc.alertmanager.model.AlertTicket;
import gov.hhs.fha.nhinc.alertmanager.service.AlertService;
import gov.hhs.fha.nhinc.dsa.DeliverMessageRequestType;
import gov.hhs.fha.nhinc.dsa.DeliverMessageResponseType;
import gov.hhs.fha.nhinc.kmr.util.CommonUtil;
import gov.hhs.fha.nhinc.ldapaccess.ContactDAO;
import gov.hhs.fha.nhinc.ldapaccess.ContactDTO;
import gov.hhs.fha.nhinc.ldapaccess.LdapService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nhin
 */
public class DeliverMsgUtil {

    public DeliverMessageResponseType deliverMsg(DeliverMessageRequestType request,
            String propDir) {
        boolean update = false;
        DeliverMessageResponseType response = new DeliverMessageResponseType();
        String patientId = request.getSubject().get(0);
        ContactDAO contactDAO = null;

        if (propDir != null) {
            contactDAO = LdapService.getContactDAO(propDir);
        }
        else {
            contactDAO = LdapService.getContactDAO();
        }

        List<ContactDTO> ldapList = contactDAO.findContact("uid=" + patientId);
        ContactDTO contact = ldapList.get(0);
        String name = ldapList.get(0).getCommonName();
        if (ldapList.size() < 1) {
            response.setStatus("PtUnitNumber not found in LDAP");
            return response;
        }

        AlertService service = new AlertService();

        List<AlertTicket> tickets = service.getAllTickets();
        AlertTicket ticket = new AlertTicket();
        for (AlertTicket tik : tickets) {
            if (tik.getTicketUniqueId().equals(request.getRefId())) {
                update = true;
                ticket = tik;
                break;
            }
        }
        
        ticket.setPatientName(contact.getDisplayName());
        ticket.setPatientSex(contact.getGender());
        
        if (!CommonUtil.strNullorEmpty(request.getStatus())) {
            ticket.setPtFMPSSN(request.getStatus());
        }

        if (!CommonUtil.strNullorEmpty(request.getBody())) {
            ticket.setPayload(request.getBody());
        }

        ticket.setTicketUniqueId(request.getRefId());

        if (!CommonUtil.strNullorEmpty(request.getHeader())) {
            ticket.setDescription(request.getHeader());
        }

        if (!CommonUtil.strNullorEmpty(request.getPriority())) {
            ticket.setPriority(request.getPriority());
        }
        else {
            ticket.setPriority("Low");
        }
        ticket.setAlertOriginator("Clinical Decision");

        ticket.setPatientUnitNumber(patientId);
//        Date date = null;
//        try {
//            date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss",
//                    Locale.ENGLISH).parse(request.getDeliveryDate());
//        } catch (ParseException ex) {
//            Logger.getLogger(DisplayAlertDataUtil.class.getName()).log(Level.SEVERE, null, ex);
//            response.setStatus("Error in date input: " + ex.toString());
//        }
        ticket.setAlertTimestamp(new Date());

        ticket.setEscalationPeriod(0);
        ticket.setType("MedAlerts");
        ticket.setAlertId(new Long(8));

        if (!update) {
            List<String> all = new ArrayList();
            all.addAll(request.getMainRecipients());
            all.addAll(request.getSecondaryRecipients());
            all.addAll(request.getHiddenRecipients());
            for (String recip : all) {
                List<ContactDTO> rList = contactDAO.findContact("uid=" + recip);
                if (CommonUtil.listNullorEmpty(rList)) {
                    response.setStatus("No contact found for uid=" + recip);
                    return response;
                }
                ContactDTO ctc = rList.get(0);
                insertContact(ctc.getUid(), ctc.getDisplayName(), ticket);
                insertStatus(ctc.getUid(), ticket);
            }
        }


        AlertAction action = new AlertAction();
        action.setActionName("Alert");
        if (!update) {
            action.setMessage("Notification generated");
        }
        else {
            action.setMessage("Update");
        }
        action.setUserId(patientId);
        action.setTicket(ticket);
        if (contact.getEmployeeNumber() != null) {
            action.setUserProvider(Boolean.TRUE);
        }
        else {
            action.setUserProvider(Boolean.FALSE);
        }
        action.setActionTimestamp(new Date());
        ticket.getActionHistory().add(action);

        service.saveTicket(ticket);
        response.setStatus("success");
        return response;

    }

    private void insertContact(String ptId, String name, AlertTicket ticket) {
        AlertContact alertContact = new AlertContact();
        alertContact.setUserId(ptId);
        alertContact.setMethod("Alert");
        alertContact.setUserName(name);
        alertContact.setTicket(ticket);
        ticket.getProviders().add(alertContact);
    }

    private void insertStatus(String ptId, AlertTicket ticket) {
        AlertStatus status = new AlertStatus();
        status.setUserId(ptId);
        status.setArchive(false);
        status.setFlagged(false);
        status.setCreateTimeStamp(new Date());
        status.setTicket(ticket);
        status.setDeleted(false);
        ticket.getStatus().add(status);
    }
}
