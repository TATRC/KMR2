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
package gov.hhs.fha.nhinc.displaymaildata;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPSSLStore;
import gov.hhs.fha.nhinc.common.dda.DetailData;
import gov.hhs.fha.nhinc.common.dda.GetComponentDetailDataForUserRequestType;
import gov.hhs.fha.nhinc.common.dda.GetComponentDetailDataResponseType;
import gov.hhs.fha.nhinc.common.dda.GetComponentSummaryDataForUserRequestType;
import gov.hhs.fha.nhinc.common.dda.GetComponentSummaryDataResponseType;
import gov.hhs.fha.nhinc.common.dda.GetDataSourceNameRequestType;
import gov.hhs.fha.nhinc.common.dda.GetDataSourceNameResponseType;
import gov.hhs.fha.nhinc.common.dda.GetMessageDetailRequestType;
import gov.hhs.fha.nhinc.common.dda.GetMessageDetailResponseType;
import gov.hhs.fha.nhinc.common.dda.GetMessagesRequestType;
import gov.hhs.fha.nhinc.common.dda.GetMessagesResponseType;
import gov.hhs.fha.nhinc.common.dda.NameValuesPair;
import gov.hhs.fha.nhinc.common.dda.ServiceError;
import gov.hhs.fha.nhinc.common.dda.SetMessageRequestType;
import gov.hhs.fha.nhinc.common.dda.SetMessageResponseType;
import gov.hhs.fha.nhinc.common.dda.SummaryData;
import gov.hhs.fha.nhinc.common.dda.UpdateComponentInboxStatusRequestType;
import gov.hhs.fha.nhinc.common.dda.UpdateComponentInboxStatusResponseType;
import gov.hhs.fha.nhinc.kmr.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.kmr.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.kmr.util.CommonUtil;
import gov.hhs.fha.nhinc.ldapaccess.ContactAcctDTO;
import gov.hhs.fha.nhinc.ldapaccess.ContactDAO;
import gov.hhs.fha.nhinc.ldapaccess.ContactDTO;
import gov.hhs.fha.nhinc.ldapaccess.LdapService;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Return a user's mail inbox data.
 *
 * @author cmatser
 */
public class DisplayMailDataHandler {

    /** Mail host. */
    private String host;
    /** Mail protocol. */
    private String protocol;
    /** Logging. */
    private static Log log = LogFactory.getLog(DisplayMailDataHandler.class);
    /**
     * Data source name.
     */
    String DATA_SOURCE = "Mail";
    /**
     * Standard error code.
     */
    Integer ERR_CODE = -1;
    /**
     * Service error message.
     */
    String ERR_MSG_ITEM_NOT_FOUND = "Item was not found.";
    /**
     * Item identifier for patient ids.
     */
    String ITEM_ID_PATIENT = "T";
    /**
     * Item identifier for provider ids.
     */
    String ITEM_ID_PROVIDER = "P";
    /**
     * Item id separater between userId and message number.
     */
    String ITEM_ID_SEPARATER = "?";
    /**
     * Item names for name value pairs.
     */
    String ITEM_READ = "Read";
    String ITEM_REPLIED = "Replied";
    String ITEM_STARRED = "Starred";
    /**
     * LDAP attribute for patient's user id.
     */
    String LDAP_PATIENT_ID_ATT = "uid";
    /**
     * LDAP attribute for provider's user id.
     */
    String LDAP_PROVIDER_ID_ATT = "employeeNumber";
    String MAIL_HOST = "mail.host";
    String MAIL_PROTOCOL = "mail.protocol";
    /**
     * Property constants.
     */
    String PROPERTY_FILE = "displayAggregator";
    String ZIMBRA_URL = "https://192.168.5.11/";
    // String ZIMBRA_URL = "https://192.168.5.47:993/";
    ContactDAO contactDAO = LdapService.getContactDAO();

    public DisplayMailDataHandler() {

        try {
            host = PropertyAccessor.getProperty(PROPERTY_FILE, MAIL_HOST);
            if (host == null) {
                throw new PropertyAccessException("Property was null: " + MAIL_HOST);
            }

            protocol = PropertyAccessor.getProperty(PROPERTY_FILE, MAIL_PROTOCOL);
            if (protocol == null) {
                throw new PropertyAccessException("Property was null: " + MAIL_PROTOCOL);
            }
        } catch (PropertyAccessException e) {
            log.error("Error accessing properties in file:" + PROPERTY_FILE, e);
        }

    }

    public GetDataSourceNameResponseType getDataSourceName(GetDataSourceNameRequestType getDataSourceNameRequest) {
        GetDataSourceNameResponseType response = new GetDataSourceNameResponseType();
        response.setReturn(DATA_SOURCE);

        return response;
    }

    /**
     * Update Inbox status.
     * 
     * @param updateComponentInboxStatusRequest
     * @return
     */
    public UpdateComponentInboxStatusResponseType updateComponentInboxStatus(UpdateComponentInboxStatusRequestType updateComponentInboxStatusRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Retrieves the message detail.
     *
     * @param request
     * @return
     */
    public GetComponentDetailDataResponseType getComponentDetailDataForUser(GetComponentDetailDataForUserRequestType request) {
        GetComponentDetailDataResponseType response = new GetComponentDetailDataResponseType();
        String userId = null;
        String userType;

        try {
            //Pull out userId
            userId = request.getItemId().substring(1, request.getItemId().indexOf(ITEM_ID_SEPARATER));
            userType = String.valueOf(request.getItemId().charAt(0));
            if ((userId == null) || userId.isEmpty()) {
                throw new Exception("Invalid item ID provided.");
            }

            //Get login info from ldap
            String access[] = retrieveMailAccess(userType, userId);
            if ((access[0] == null) || access[0].isEmpty()) {
                throw new Exception("Contact record not found for user: " + userId);
            }

            //Retrieve message
            DetailData detailData = retrieveMailDetail(access[0], access[1], request.getItemId(), "Email");

            response.setDetailObject(detailData);
        } catch (Throwable t) {
            log.error("Error getting mail summary data.", t);

            ServiceError serviceError = new ServiceError();
            serviceError.setCode(ERR_CODE);
            serviceError.setText(t.getMessage());
            response.getErrorList().add(serviceError);
        }

        return response;
    }

    /**
     * Retrieves the message summaries for a user's inbox.
     *
     * @param request
     * @return
     */
    public GetComponentSummaryDataResponseType getComponentSummaryDataForUser(GetComponentSummaryDataForUserRequestType request) {
        GetComponentSummaryDataResponseType response = new GetComponentSummaryDataResponseType();
        String userId;
        String userType;
        boolean isProvider = true;

        try {
            //Check for provider id
            userId = request.getProviderId();
            if ((userId == null) || userId.isEmpty()) {
                //Try patient id
                userId = request.getPatientId();
                isProvider = false;
            }

            //Check that we have an id
            if ((userId == null) || userId.isEmpty()) {
                throw new Exception("No provider/patient id provided.");
            }

            //Setup user type
            userType = isProvider ? ITEM_ID_PROVIDER : ITEM_ID_PATIENT;

            //Get login info from ldap
            String access[] = retrieveMailAccess(userType, userId);
            if ((access[0] == null) || access[0].isEmpty()) {
                throw new Exception("Contact record not found for user: " + userId);
            }



            //Retrieve messages
            List<SummaryData> summaryDataList =
                    retrieveMail(userType, userId, access[0], access[1], "", "", request.isOnlyNew());

            response.getSummaryObjects().addAll(summaryDataList);

        } catch (Throwable t) {
            log.error("Error getting mail summary data.", t);

            ServiceError serviceError = new ServiceError();
            serviceError.setCode(ERR_CODE);
            serviceError.setText(t.getMessage());
            response.getErrorList().add(serviceError);
        }

        return response;
    }

    /**
     * Creates the IMAP messages to be sent. Looks up access (email & pwd) from
     * LDAP, sets the values and returns the messages to be sent.
     * 
     * @param session Mail session 
     * @param emailAddr From email address
     * @param request Request from PS
     * @return Array of messages
     * @throws Exception 
     */
    private Message[] createMessage(Session session, String emailAddr,
            SetMessageRequestType request) throws Exception {

        String[] accessTo = new String[2];
        contactDAO = LdapService.getContactDAO();
        ContactDTO cntcTo = findContact(request.getUserId(), "");
        List<String> upass = getContactEmailAndPass(cntcTo);
        if (upass.isEmpty()) {
            throw new Exception("No contacts found for user: " + request.getUserId());
        }
        accessTo = upass.toArray(new String[0]);

        String userType = "";
        if (cntcTo.getEmployeeNumber() != null) {
            userType = ITEM_ID_PROVIDER;
        }
        else {
            userType = ITEM_ID_PATIENT;
        }

        MimeMessage message = new MimeMessage(session);

        message.setRecipients(Message.RecipientType.TO,
                getInetAddresses(request, "To"));
        message.setRecipients(Message.RecipientType.CC,
                getInetAddresses(request, "CC"));
        message.setRecipients(Message.RecipientType.BCC,
                getInetAddresses(request, "BCC"));



        message.setSubject(request.getSubject());

        // Adding headers doesn't seem to be supported currently in zimbra. 
        // Adding patientId to body instead temporarily 
        // message.addHeader("X-PATIENTID", request.getPatientId());
        StringBuilder sb = new StringBuilder();
        sb.append("PATIENTID=").append(request.getPatientId()).append("\n");
        sb.append(request.getBody());

        message.setContent(sb.toString(), "text/plain");
        message.setFrom(new InternetAddress(emailAddr));
        message.setSentDate(new Date());

        List<String> labelList = request.getLabels();
        if (labelList.size() > 0) {
            String label = labelList.get(0);
            if (label.equalsIgnoreCase("starred")) {
                message.setFlag(Flags.Flag.FLAGGED, true);
            }
        }
        Message[] msgArr = new Message[1];
        msgArr[0] = message;
        return msgArr;
    }


    private Address[] getInetAddresses(SetMessageRequestType request, String type) throws Exception
    {
        List<Address> toAddrList = new ArrayList<Address>();
        List<String> ctcList = new ArrayList<String>(); //List of all contacts' email access info to send to.

        // Get all TO email access info.
        if (type.equals("To")) {
            for (String ctcid : request.getContactTo()) {
                List<ContactDTO> contact = contactDAO.findContact("cn=" + ctcid);
                if (!CommonUtil.listNullorEmpty(contact)) {
                    ContactDTO ctc = contact.get(0);
                    ctcList.add(getContactEmailAndPass(ctc).get(0));
                }
            }
        }
        // Get all CC email access info.
        if (type.equals("CC")) {
            for (String ctcid : request.getContactCC()) {
                List<ContactDTO> contact = contactDAO.findContact("cn=" + ctcid);
                if (!CommonUtil.listNullorEmpty(contact)) {
                    ContactDTO ctc = contact.get(0);
                    ctcList.add(getContactEmailAndPass(ctc).get(0));
                }
            }
        }
        // Get all BCC email access info.
        if (type.equals("BCC")) {
            for (String ctcid : request.getContactBCC()) {
                List<ContactDTO> contact = contactDAO.findContact("cn=" + ctcid);
                if (!CommonUtil.listNullorEmpty(contact)) {
                    ContactDTO ctc = contact.get(0);
                    ctcList.add(getContactEmailAndPass(ctc).get(0));
                }
            }
        }

        for (String ctc : ctcList) {
            toAddrList.add(new InternetAddress(ctc));
        }
        return toAddrList.toArray(new InternetAddress[0]);
    }

    private Folder getImapFolder(Session session, IMAPSSLStore sslStore,
            String[] access, String folderName) throws MessagingException, NoSuchProviderException {
        Folder folder;
        Provider provider = session.getProvider("imap");
        session.setProvider(provider);
        URLName urlName = new URLName(ZIMBRA_URL);
        sslStore = new IMAPSSLStore(session, urlName);
        sslStore.connect(host, access[0], access[1]);
        folder = sslStore.getFolder(folderName);
        return folder;
    }

    private void performArchive(Folder folder, SetMessageRequestType request, Folder archiveFolder) throws MessagingException
    {
        System.out.println("===> performArchive: msg ID= "+request.getMessageId());

        IMAPMessage imapMessage = imapMessage =
                (IMAPMessage) folder.getMessage(Integer.parseInt(request.getMessageId()));
        Message[] messages = new Message[]{imapMessage};
        folder.copyMessages(messages, archiveFolder);
        imapMessage.setFlag(Flags.Flag.DELETED, true);
        folder.expunge();
    }

    private void performUnarchive(Folder folder, SetMessageRequestType request, Folder archiveFolder) throws MessagingException {
        IMAPMessage imapMessage = imapMessage =
                (IMAPMessage) archiveFolder.getMessage(Integer.parseInt(request.getMessageId()));
        Message[] messages = new Message[]{imapMessage};
        archiveFolder.copyMessages(messages, folder);
        imapMessage.setFlag(Flags.Flag.DELETED, true);
        archiveFolder.expunge();
    }

    /**
     * Find LDAP record from userId.
     *
     * @param userId
     * @return
     */
    private String[] retrieveMailAccess(String userType, String userId) {
        String access[] = new String[2];

        ContactDAO contactDAO = LdapService.getContactDAO();
        List<ContactDTO> contacts = contactDAO.findContact(
                (ITEM_ID_PROVIDER.equals(userType)
                ? LDAP_PROVIDER_ID_ATT
                : LDAP_PATIENT_ID_ATT) + "=" + userId);
        if (contacts.size() > 0) {
            //Get mail login info
            List<ContactAcctDTO> accts = contactDAO.findContactAcct(
                    contacts.get(0).getCommonName(), ContactAcctDTO.CN_MAIL);
            if (accts.size() > 0) {
                access[0] = accts.get(0).getUid();
                access[1] = accts.get(0).getClearPassword();
            }
        }

        return access;
    }

    private String getContactIdFromEmail(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }
        String notFound = "Not Found";
//        String emailAddr = 
//                email.substring(email.indexOf("<") + 1, email.indexOf(">"));

        List<ContactDTO> contacts = contactDAO.findContact("Mail.Account");
        for (ContactDTO contact : contacts) {
            String cn = contact.getCommonName();
            List<ContactAcctDTO> acctList =
                    contactDAO.findAllContactAccts(cn);
            for (ContactAcctDTO acct : acctList) {
                if (email.contains(acct.getUid())) {
                    return cn;
                }
            }
        }
        return notFound;
    }

    private List<String> getContactEmailAndPass(ContactDTO contact) {
        List<String> ret = new ArrayList();
        ContactAcctDTO acct =
                contactDAO.findContactAcct(contact.getCommonName(), "Mail.Account").get(0);
        ret.add(acct.getUid());
        ret.add(acct.getClearPassword());
        return ret;
    }

    private void updateImapSSLMail(SetMessageRequestType request, Folder folder,
            IMAPMessage message) throws MessagingException {
        // folder.open(Folder.READ_WRITE);

        List<String> labelList = request.getLabels();
        if (!labelList.isEmpty() && labelList.get(0).equalsIgnoreCase("starred")) {
            message.setFlag(Flags.Flag.FLAGGED, true);
        }
        else {
            message.setFlag(Flags.Flag.FLAGGED, false);
        }
        if (request.getAction().equalsIgnoreCase("Read")) {
            message.setFlag(Flags.Flag.SEEN, true);
        }
        //      message.saveChanges();
        folder.close(false);
    }

    // Handle Archive and Unarchive actions
    public SetMessageResponseType archiveMessage(SetMessageRequestType request) {
        SetMessageResponseType response = new SetMessageResponseType();

        IMAPSSLStore sslStore = null;
        Folder folder = null;
        Folder archiveFolder = null;
        Properties prop = initializeMailProperties();
        Session session = Session.getDefaultInstance(prop);

        //Get email address and password from LDAP
        String userId = request.getUserId();
        ContactDAO contactDAO = LdapService.getContactDAO();
        ContactDTO foundContact = new ContactDTO();
        List<ContactDTO> contacts = contactDAO.findAllContacts();
        for (ContactDTO contact : contacts) {
            if (contact.getUid() != null
                    && contact.getUid().equals(request.getUserId())) {
                foundContact = contact;
                break;
            }
        }

        String userType = "";
        String[] access = new String[2];
        String userCName = foundContact.getCommonName();
        if (contacts.isEmpty()) {
            log.error("Contact record not found for user: " + userCName);
            response.setMessage("Contact record not found for user: " + userCName);
            response.setSuccessStatus(false);
            return response;
        }

        if (foundContact.getEmployeeNumber() != null) {
            userType = ITEM_ID_PROVIDER;
            access = retrieveMailAccess(userType, foundContact.getEmployeeNumber());
        }
        else {
            userType = ITEM_ID_PATIENT;
            access = retrieveMailAccess(userType, foundContact.getUid());
        }

        if ((access[0] == null) || access[0].isEmpty()) {
            log.error("Contact record not found for user: " + userId);
            response.setMessage("Contact record not found for user: " + userId);
            response.setSuccessStatus(false);
            return response;
        }

        try {

            folder = getImapFolder(session, sslStore, access, "INBOX");
            folder.open(Folder.READ_WRITE);
            archiveFolder = getImapFolder(session, sslStore, access, "Archive");
            archiveFolder.open(Folder.READ_WRITE);

            int msgId = Integer.parseInt(request.getMessageId());
            if (msgId <= folder.getMessages().length) {
                if (request.getAction().equals("Archive")) {
                    performArchive(folder, request, archiveFolder);
                }
                if (request.getAction().equals("Unarchive")) {
                    performUnarchive(folder, request, archiveFolder);
                }
            }
            else {
                response.setSuccessStatus(false);
                response.setMessage("Message ID: " + request.getMessageId() + " was not found in the email server");
                return response;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage("Error archiving mail with Zimbra mail server: " + e.getMessage());
            response.setSuccessStatus(false);
            e.printStackTrace();
            return response;
        } finally {
            try {
                folder.close(false);
                archiveFolder.close(false);
            } catch (MessagingException me) {
                me.printStackTrace();
            }
        }
        response.setSuccessStatus(true);
        return response;
    }

    private SetMessageResponseType sendImapSSLMail(SetMessageRequestType request) {
        SetMessageResponseType response = new SetMessageResponseType();
        IMAPSSLStore sslStore = null;
        Folder folder = null;

        String userType = "";
        String[] access = new String[2];

        Properties prop = initializeMailProperties();
        Session session = Session.getDefaultInstance(prop);

        //Get email address and password from LDAP
        String userId = request.getUserId();
        ContactDTO foundContact = null;
        try {
            foundContact = findContact(userId, "");
        } catch (Exception e) {
            log.error("Contact record not found for userid: " + request.getUserId());
            response.setMessage("Contact record not found for userid: " + request.getUserId());
            response.setSuccessStatus(false);
            return response;
        }

        if (foundContact.getEmployeeNumber() != null) {
            userType = ITEM_ID_PROVIDER;
            access = retrieveMailAccess(userType, foundContact.getEmployeeNumber());
        }
        else {
            userType = ITEM_ID_PATIENT;
            access = retrieveMailAccess(userType, foundContact.getUid());
        }

        if ((access[0] == null) || access[0].isEmpty()) {
            log.error("Contact record not found for user: " + userId);
            response.setMessage("Contact record not found for user: " + userId);
            response.setSuccessStatus(false);
            return response;
        }

        // Use the sslStore to send/change the message
        try {
            if (request.getAction().equalsIgnoreCase("Save")) {
                response = saveDraft(request, access, sslStore, folder, session);
                return response;
            }

            // GETTING the correct Email folder this msg currently lives in.
            if (request.getLocation().equalsIgnoreCase("Draft")) {
                folder = getImapFolder(session, sslStore, access, "Drafts");
            }
            else if (request.getLocation().equalsIgnoreCase("Sent")) {
                folder = getImapFolder(session, sslStore, access, "Sent");
            }
            else if (request.getLocation().equalsIgnoreCase("Archive")) {
                folder = getImapFolder(session, sslStore, access, "Archive");
            }
            else if (request.getLocation().equalsIgnoreCase("UserTrash")) {
                folder = getImapFolder(session, sslStore, access, "UserTrash");
            }
            else {
                folder = getImapFolder(session, sslStore, access, "INBOX");
            }
            folder.open(Folder.READ_WRITE);
            IMAPMessage imapMessage = null;

            //Update existing message
            if (!request.getAction().equals("Send")) {

                int msgId = Integer.parseInt(request.getMessageId());

                //
                if (msgId <= folder.getMessageCount()) {
                    imapMessage = (IMAPMessage) folder.getMessage(msgId);
                }
                else {
                    response.setSuccessStatus(false);
                    response.setMessage("Invalid message id " + msgId + ", folder message count is "
                            + folder.getMessageCount());
                    log.error(response.getMessage());
                    log.error(Thread.currentThread().getStackTrace().toString());
                    return response;
                }

                //
                if (imapMessage != null) {
                    updateImapSSLMail(request, folder, imapMessage);
                    response.setSuccessStatus(true);
//                    return response;
                }
                else {
                    response.setSuccessStatus(false);
                    response.setMessage("No message found for message id = " + msgId);
                    return response;
                }
            }

            // Send new message

            Message[] msgArr = createMessage(session, access[0], request);
//            folder.appendMessages(msgArr);
            sendMessagesTOCCBCC(msgArr, request, session);

        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage("Error sending mail with Zimbra mail server: " + e.getMessage());
            response.setSuccessStatus(false);
            e.printStackTrace();
            return response;
        } finally {
            if (folder != null && folder.isOpen()) {
                try {
                    folder.close(false);
                } catch (MessagingException me) {
                    log.error("Error closing folder");
                    me.printStackTrace();
                }
            }
            if (sslStore != null) {
                try {
                    sslStore.close();
                } catch (MessagingException me) {
                    log.error("Error closing SSLStore");
                    me.printStackTrace();
                }
            }
        }

        response.setSuccessStatus(true);
        response.setMessage(" ");
        return response;

    }

    /**
     * Retrieve mail for user.
     *
     * @param login
     * @param pswd
     * @return
     * @throws java.lang.Exception
     */
    private List<SummaryData> retrieveMail(String userType, String userId,
            String login, String pswd, String folderName, String displayName, boolean onlyNew)
            throws MessagingException, DatatypeConfigurationException {
        List<SummaryData> dataList = new LinkedList<SummaryData>();
        IMAPSSLStore sslStore = null;
        IMAPFolder currentFolder = null;

        try {
            //Get session
            Session session = Session.getInstance(new Properties());
            URLName urlName = new URLName(ZIMBRA_URL);
            //Get the sslStore
            sslStore = new IMAPSSLStore(session, urlName);
            sslStore.connect(host, login, pswd);

            if (folderName == null || folderName.isEmpty()) {
                folderName = "INBOX";
            }

            if (folderName.equalsIgnoreCase("Draft")) {
                folderName = "Drafts";
            }

            if (folderName.contains("Archive")) {
                folderName = "Archive";
            }

            currentFolder = (IMAPFolder) sslStore.getFolder(folderName);
            currentFolder.open(Folder.READ_ONLY);
            Message[] allMessages = currentFolder.getMessages();
            GregorianCalendar cal = new GregorianCalendar();
            for (Message msg : allMessages) {
                if (msg == null) {
                    continue;
                }

// Keep this in case we want to search entire message
//
//                OutputStream os = new ByteArrayOutputStream();
//                msg.writeTo(os);
//                String msgContent = os.toString();

                SummaryData summaryData = new SummaryData();
                summaryData.setDataSource(DATA_SOURCE);
                String from = "";
                Address[] fromAddr = msg.getFrom();
                if (fromAddr != null && fromAddr.length > 0) {
                    String fromFull = fromAddr[0].toString();
                    if (fromFull.indexOf("<") > 0) {
                        from = fromFull.substring(0, fromFull.indexOf("<") - 1);
                    }
                    else {
                        from = fromFull;
                    }
                    summaryData.setFrom(from);
                }
                if (!displayName.equals("") && !from.contains(displayName)) {
                    continue;
                }

                summaryData.setAuthor(summaryData.getFrom());
                cal.setTime(msg.getReceivedDate());
                summaryData.setDateCreated(
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));

                summaryData.setDescription(msg.getSubject());
                summaryData.setItemId(
                        userType + userId + ITEM_ID_SEPARATER
                        + msg.getFolder().getName() + ITEM_ID_SEPARATER
                        + String.valueOf(msg.getMessageNumber()));

                boolean msgRead = msg.isSet(Flags.Flag.SEEN);
                addNameValue(summaryData.getItemValues(), ITEM_READ,
                        String.valueOf(msgRead));

                boolean msgStar = msg.isSet(Flags.Flag.FLAGGED);
                if (msgStar) {
                    addNameValue(summaryData.getItemValues(), ITEM_STARRED,
                            "Starred");
                }

                addNameValue(summaryData.getItemValues(),
                        ITEM_REPLIED, String.valueOf(msg.isSet(Flags.Flag.ANSWERED)));
                addNameValue(summaryData.getItemValues(),
                        "MESSAGE_TYPE", msg.getFolder().getName());
                if (onlyNew) {
                    if (!msg.isSet(Flags.Flag.SEEN)) {
                        dataList.add(summaryData);
                    }
                }
                else {
                    dataList.add(summaryData);
                }
            }

        } catch (MessagingException me) {
            log.error("Error in processing email");
            me.printStackTrace();
        } finally {
            // Close connections

            if (currentFolder != null) {
                try {
                    currentFolder.close(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (sslStore != null) {
                try {
                    sslStore.close();
                } catch (Exception e) {
                }
            }
        }

        return dataList;
    }

    private Message[] getFolderMessages(String folderName, IMAPSSLStore sslStore) throws MessagingException {
        if (folderName.equalsIgnoreCase("Draft")) {
            folderName = "Drafts";
        }
        IMAPFolder folder = (IMAPFolder) sslStore.getFolder(folderName);
        folder.open(Folder.READ_ONLY);
        Message[] messageArr = folder.getMessages();

        return messageArr;
    }

    private Message[] getAllFolderMessages(IMAPFolder[] folders, IMAPSSLStore sslStore) throws MessagingException {
        folders[0] = (IMAPFolder) sslStore.getFolder("INBOX");
        folders[0].open(Folder.READ_ONLY);
        Message[] messageArr = folders[0].getMessages();

        folders[1] = (IMAPFolder) sslStore.getFolder("archive");
        folders[1].open(Folder.READ_ONLY);
        Message[] archMsgArr = folders[1].getMessages();

        folders[2] = (IMAPFolder) sslStore.getFolder("Sent");
        folders[2].open(Folder.READ_ONLY);
        Message[] sentMsgArr = folders[2].getMessages();

        folders[3] = (IMAPFolder) sslStore.getFolder("Drafts");
        folders[3].open(Folder.READ_ONLY);
        Message[] draftMsgArr = folders[3].getMessages();

        Message[] allMessages =
                new Message[messageArr.length + archMsgArr.length
                + sentMsgArr.length + draftMsgArr.length];

        if (messageArr.length > 0) {
            System.arraycopy(messageArr, 0, allMessages, 0, messageArr.length);
        }
        if (archMsgArr.length > 0) {
            System.arraycopy(archMsgArr, 0, allMessages, messageArr.length, archMsgArr.length);
        }
        if (sentMsgArr.length > 0) {
            System.arraycopy(sentMsgArr, 0, allMessages, messageArr.length + archMsgArr.length,
                    sentMsgArr.length);
        }
        if (draftMsgArr.length > 0) {
            System.arraycopy(draftMsgArr, 0, allMessages, messageArr.length + archMsgArr.length
                    + sentMsgArr.length, draftMsgArr.length);
        }

        return allMessages;
    }

    /**
     * Retrieve mail message for user.
     *
     * @param login
     * @param pswd
     * @param msgnum
     * @return
     * @throws java.lang.Exception
     */
    private DetailData retrieveMailDetail(String login, String pswd, String itemId, String messageType)
            throws Exception {
        DetailData data = null;
        IMAPSSLStore sslStore = null;
        Folder folder = null;

        try {
            String msgId[] = parseMsgId(itemId);

            //Get session
            Session session = Session.getInstance(new Properties());
            URLName urlName = new URLName(ZIMBRA_URL);

            //Get the sslStore
            sslStore = new IMAPSSLStore(session, urlName);
            sslStore.connect(host, login, pswd);


            //Get folder
            if (messageType.equals("Email")) {
                folder = sslStore.getFolder(msgId[1]);
                folder.open(Folder.READ_ONLY);
            }

            if (messageType.equalsIgnoreCase("Archive")) {
                folder = sslStore.getFolder("Archive");
                folder.open(Folder.READ_ONLY);
            }

            if (messageType.equalsIgnoreCase("Sent")) {
                folder = sslStore.getFolder("Sent");
                folder.open(Folder.READ_ONLY);
            }

            //Get messages
            Message msgs[] = folder.getMessages(new int[]{Integer.parseInt(msgId[2])});
            if (msgs.length == 0) {
                return null;
            }

            //Add messages to response
            GregorianCalendar cal = new GregorianCalendar();
            data = new DetailData();
            data.setDataSource(DATA_SOURCE);
            if ((msgs[0].getFrom() != null) && (msgs[0].getFrom().length > 0)) {
                data.setFrom(msgs[0].getFrom()[0].toString());
                data.setAuthor(data.getFrom());
            }
            cal.setTime(msgs[0].getReceivedDate());
            data.setDateCreated(
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
            data.setDescription(msgs[0].getSubject());
            data.setItemId(msgId[0] + ITEM_ID_SEPARATER
                    + msgs[0].getFolder().getName() + ITEM_ID_SEPARATER
                    + String.valueOf(msgs[0].getMessageNumber()));
            data.setData(fetchMsgContent(msgs[0]));
            addNameValue(data.getItemValues(), ITEM_READ, String.valueOf(msgs[0].isSet(Flags.Flag.SEEN)));
            addNameValue(data.getItemValues(), ITEM_REPLIED, String.valueOf(msgs[0].isSet(Flags.Flag.ANSWERED)));

        } finally {
            // Close connection
            if (folder != null) {
                try {
                    folder.close(false);
                } catch (Exception e) {
                }
            }

            if (sslStore != null) {
                try {
                    sslStore.close();
                } catch (Exception e) {
                }
            }
        }

        return data;
    }

    /**
     * Extract folder and message number from item id.
     * 
     * @param itemId
     * @return
     */
    private String[] parseMsgId(String itemId) {
        String msgId[] = new String[3];
        StringTokenizer st = new StringTokenizer(itemId, ITEM_ID_SEPARATER);

        msgId[0] = st.nextToken();//userId
        msgId[1] = st.nextToken();//folder
        msgId[2] = st.nextToken();//msg num

        return msgId;
    }

    /**
     * Extract/format mail message content.
     * 
     * @param msg
     * @return
     */
    private String fetchMsgContent(Message msg)
            throws Exception {
        String content;

        //We understand plain text
        if (msg.isMimeType("text/plain")) {
            content = (String) msg.getContent();
        }
        else {
            content = "Unknown mail type: " + msg.getContentType();
        }

        return content;
    }

    private void addNameValue(List<NameValuesPair> pairList, String name, String value) {
        NameValuesPair nameVal = new NameValuesPair();
        nameVal.setName(name);
        nameVal.getValues().add(value);
        pairList.add(nameVal);

        return;
    }

    public SetMessageResponseType setMessage(SetMessageRequestType request) {

        SetMessageResponseType response = sendImapSSLMail(request);
        return response;
    }

    public GetMessagesResponseType getMessages(GetMessagesRequestType request) {
        GetMessagesResponseType toReturn = new GetMessagesResponseType();

        List<GetMessagesResponseType.GetMessageResponse> getResponse =
                toReturn.getGetMessageResponse();

        //Get email address and password from LDAP
        ContactDTO contact = null, ptContact = null;
        ContactDAO contactDAO = LdapService.getContactDAO();

        contact = contactDAO.findContact("uid=" + request.getUserId()).get(0);
        if (request.getPatientId() != null) {
            ptContact = contactDAO.findContact("uid=" + request.getPatientId()).get(0);
        }

        String userType = "";
        String[] access = new String[2];

        if (contact.getEmployeeNumber() != null) {
            userType = ITEM_ID_PROVIDER;
            access = retrieveMailAccess(userType, contact.getEmployeeNumber());
        }
        else {
            userType = ITEM_ID_PATIENT;
            access = retrieveMailAccess(userType, contact.getUid());
        }
        try {
            List<SummaryData> allMail = new ArrayList<SummaryData>();
            if (ptContact != null) {
                allMail = retrieveMail(userType, request.getUserId(),
                        access[0], access[1], request.getLocation(),
                        ptContact.getDisplayName(), false);
            }
            else {
                allMail = retrieveMail(userType, request.getUserId(),
                        access[0], access[1], request.getLocation(), "", false);
            }

            for (SummaryData summary : allMail) {
                GetMessagesResponseType.GetMessageResponse resp =
                        new GetMessagesResponseType.GetMessageResponse();
                resp.setMessageType("Email");
                String[] msgIds = parseMsgId(summary.getItemId());
                resp.setMessageId(msgIds[2]);
                //              resp.getLabels().add(summary.getFolder());
                resp.setTitle(summary.getDescription());
                resp.setDescription(summary.getDescription());
                resp.setFrom(summary.getFrom());
                resp.setSuccessStatus(true);
                resp.setMessageDate(summary.getDateCreated());

                List<NameValuesPair> itemValues = summary.getItemValues();
                NameValuesPair nvp = itemValues.get(0);
                List<String> values = nvp.getValues();
                String isRead = values.get(0);
                if (isRead.equals("true")) {
                    resp.setMessageStatus("Read");
                }
                else {
                    resp.setMessageStatus("Unread");
                }

                NameValuesPair nvp1 = itemValues.get(1);
                if (nvp1.getValues().get(0).equalsIgnoreCase("starred")) {
                    resp.getLabels().add("Starred");
                }

                NameValuesPair nvp2 = itemValues.get(2);
                values = nvp2.getValues();
                String msgType = values.get(0);
                if (msgType.equals("INBOX")) {
                    resp.setLocation("Inbox");
                    resp.setMessageType("Email");
                }
                if (msgType.equalsIgnoreCase("archive")) {
                    resp.setLocation("Archive");
                }
                if (msgType.equalsIgnoreCase("Sent")) {
                    resp.setMessageType("Email");
                    resp.setLocation("Sent");
                }
                if (msgType.contains("Draft")) {
                    resp.setMessageType("Email");
                    resp.setLocation("Draft");
                }
                toReturn.getGetMessageResponse().add(resp);
            }

        } catch (AuthenticationFailedException afe) {
            GetMessagesResponseType.GetMessageResponse resp =
                    new GetMessagesResponseType.GetMessageResponse();
            resp.setStatusMessage("Email authentication failed for userId: " + request.getUserId()
                    + " email: " + access[0]);
            resp.setSuccessStatus(false);
            toReturn.getGetMessageResponse().add(resp);
            return toReturn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public GetMessageDetailResponseType getMessageDetail(GetMessageDetailRequestType request) throws Exception {
        GetMessageDetailResponseType response = new GetMessageDetailResponseType();
        IMAPFolder msgFolder = null;
        IMAPSSLStore sslStore = null;
        Properties prop = initializeMailProperties();
        Session session = Session.getDefaultInstance(prop);

        //Get email address and password from LDAP
        ContactDAO contactDAO = LdapService.getContactDAO();
        String ldapQry = "uid=" + request.getUserId();
        List<ContactDTO> contacts = contactDAO.findContact(ldapQry);
        //     List<ContactDTO> contacts = contactDAO.findAllContacts();
        ContactDTO contact = new ContactDTO();
        for (ContactDTO userContact : contacts) {
            if (userContact.getUid() != null
                    && userContact.getUid().equals(request.getUserId())) {
                contact = userContact;
                break;
            }
        }

        if (contact == null || contact.getUid() == null) {
            response.setSuccessStatus(true);
            response.setStatusMessage("No contact was found for userId " + request.getUserId());
        }

        String userType = "";
        String[] access = new String[2];

        if (contact.getEmployeeNumber() != null) {
            userType = ITEM_ID_PROVIDER;
            access = retrieveMailAccess(userType, contact.getEmployeeNumber());
        }
        else {
            userType = ITEM_ID_PATIENT;
            access = retrieveMailAccess(userType, contact.getUid());
        }

        try {
            session = Session.getInstance(new Properties());
            URLName urlName = new URLName(ZIMBRA_URL);

            //Get the sslStore and connect
            sslStore = new IMAPSSLStore(session, urlName);
            sslStore.connect(host, access[0], access[1]);
            String folderName = request.getLocation();

            // Default the folderName to INBOX
            if (folderName == null || folderName.isEmpty()) {
                folderName = "INBOX";
            }

            if (folderName.equalsIgnoreCase("Draft")) {
                folderName = "Drafts";
            }

            // Get and open the IMAP folder
            msgFolder = (IMAPFolder) sslStore.getFolder(folderName);
            msgFolder.open(Folder.READ_ONLY);
            Message msg =
                    msgFolder.getMessage(Integer.parseInt(request.getMessageId()));
            String content = fetchMsgContent(msg);

            if (content.startsWith("PATIENTID=")) {

                // TEMPORARILY extract patientId from body if present
                Scanner scanner = new Scanner(content);
                boolean first = true;
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNextLine()) {
                    if (first) {
                        String[] parts = scanner.nextLine().split("=");
                        response.setPatientId(parts[1]);
                        first = false;
                    }
                    else {
                        sb.append(scanner.nextLine());
                    }

                }
                response.getMessageDetail().add(sb.toString());
            }
            else {
                response.getMessageDetail().add(content);
            }



// Adding patientId coming from the message header. 
//            if (msg.getHeader("X-PATIENTID") != null &&
//                msg.getHeader("X-PATIENTID").length > 0) {
//                
//                response.setPatientId(msg.getHeader("X-PATIENT_ID")[0]);
//            }

            if (msg.getRecipients(Message.RecipientType.TO) != null) {
                for (Address a : msg.getRecipients(Message.RecipientType.TO)) {
                    String contactId = getContactIdFromEmail(a.toString());
                    response.getSentTo().add(contactId);
                }
            }

            if (msg.getRecipients(Message.RecipientType.CC) != null) {
                for (Address a : msg.getRecipients(Message.RecipientType.CC)) {
                    String contactId = getContactIdFromEmail(a.toString());
                    response.getCCTo().add(contactId);
                }
            }

            if (msg.getRecipients(Message.RecipientType.BCC) != null) {
                for (Address a : msg.getRecipients(Message.RecipientType.BCC)) {
                    String contactId = getContactIdFromEmail(a.toString());
                    response.getBCCTo().add(contactId);
                }
            }

        } catch (Exception e) {
            response.setSuccessStatus(false);
            response.setStatusMessage("Error getting message detail for user: " + access[0]);
            e.printStackTrace();
        } finally {
            if (msgFolder != null) {
                try {
                    msgFolder.close(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (sslStore != null) {
                try {
                    sslStore.close();
                } catch (Exception e) {
                }
            }
        }

        response.setSuccessStatus(
                true);
        response.setStatusMessage(
                "");

        return response;
    }

    private Properties initializeMailProperties() {
        Properties prop = new Properties();
        prop.setProperty("mail.imap.starttls.enable", "false");
        prop.setProperty("mail.imap.ssl.snable", "true");
        // Use SSL
        prop.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.imap.socketFactory.fallback", "false");
        // Use port 993
        prop.setProperty("mail.imap.port", "993");
        prop.setProperty("mail.imap.socketFactory.port", "993");
        prop.setProperty("mail.imaps.class", "com.sun.mail.imap.IMAPSSLStore");
        prop.setProperty("mail.transport.protocol", "imap");
        prop.setProperty("mail.imap.host", host);
        return prop;
    }

    public SetMessageResponseType deleteMessage(SetMessageRequestType request) {
        SetMessageResponseType response = new SetMessageResponseType();
        IMAPSSLStore sslStore = null;
        Folder sourceFolder = null;
        Folder targetFolder = null;
        Properties prop = initializeMailProperties();
        Session session = Session.getDefaultInstance(prop);

        //Get email address and password from LDAP
        String userId = request.getUserId();
        ContactDAO contactDAO = LdapService.getContactDAO();
        ContactDTO foundContact = new ContactDTO();
        List<ContactDTO> contacts = contactDAO.findAllContacts();
        for (ContactDTO contact : contacts) {
            if (contact.getUid() != null
                    && contact.getUid().equals(request.getUserId())) {
                foundContact = contact;
                break;
            }
        }

        String userType = "";
        String[] access = new String[2];
        String userCName = foundContact.getCommonName();
        if (contacts.isEmpty()) {
            log.error("Contact record not found for user: " + userCName);
            response.setMessage("Contact record not found for user: " + userCName);
            response.setSuccessStatus(false);
            return response;
        }

        if (foundContact.getEmployeeNumber() != null) {
            userType = ITEM_ID_PROVIDER;
            access = retrieveMailAccess(userType, foundContact.getEmployeeNumber());
        }
        else {
            userType = ITEM_ID_PATIENT;
            access = retrieveMailAccess(userType, foundContact.getUid());
        }

        if ((access[0] == null) || access[0].isEmpty()) {
            log.error("Contact record not found for user: " + userId);
            response.setMessage("Contact record not found for user: " + userId);
            response.setSuccessStatus(false);
            return response;
        }

        try {
            if (request.getLocation() == null) {
                request.setLocation("INBOX");
            }
            if (request.getLocation().contains("Archive")) {
                sourceFolder = getImapFolder(session, sslStore, access, "Archive");
            }
            else if (request.getLocation().contains("Sent")) {
                sourceFolder = getImapFolder(session, sslStore, access, "Sent");
            }
            else if (request.getLocation().contains("raft")) {
                sourceFolder = getImapFolder(session, sslStore, access, "Drafts");
            }
            else if (request.getLocation().contains("UserTrash")) {
                sourceFolder = getImapFolder(session, sslStore, access, "UserTrash");
            }
            else {
                sourceFolder = getImapFolder(session, sslStore, access, "INBOX");
            }

            if (request.getAction().equals("Undelete")) {
                sourceFolder = getImapFolder(session, sslStore, access, "UserTrash");
            }

            sourceFolder.open(Folder.READ_WRITE);

            if (sourceFolder.getName().equals("UserTrash")) {
                targetFolder = getImapFolder(session, sslStore, access, "AdminTrash");
            }
            else {
                targetFolder = getImapFolder(session, sslStore, access, "UserTrash");
            }

            if (request.getAction().equals("Undelete")) {
                targetFolder = getImapFolder(session, sslStore, access, "INBOX");
            }

            targetFolder.open(Folder.READ_WRITE);

            int msgId = Integer.parseInt(request.getMessageId());
            if (msgId <= sourceFolder.getMessages().length) {
                IMAPMessage imapMessage = imapMessage = (IMAPMessage) sourceFolder.getMessage(msgId);
                Message[] messages = new Message[]{imapMessage};
                sourceFolder.copyMessages(messages, targetFolder);
                imapMessage.setFlag(Flags.Flag.DELETED, true);
                sourceFolder.expunge();
            }
            else {
                response.setSuccessStatus(false);
                response.setMessage("Message ID: " + request.getMessageId() + " was not found in the email server");
                return response;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage("Error archiving mail with Zimbra mail server: " + e.getMessage());
            response.setSuccessStatus(false);
            e.printStackTrace();
            return response;
        } finally {
            try {
                sourceFolder.close(false);
                targetFolder.close(false);
            } catch (MessagingException me) {
                me.printStackTrace();
            }
        }
        response.setSuccessStatus(true);
        return response;
    }

    private ContactDTO findContact(String userId, String cname) throws Exception {
        ContactDAO contactDAO = LdapService.getContactDAO();
        ContactDTO foundContact = new ContactDTO();
        //       List<ContactDTO> contacts = contactDAO.findAllContacts();

        if (cname.equals("")) {
            List<ContactDTO> contacts = contactDAO.findContact("uid=" + userId);
            if (contacts.isEmpty()) {
                throw new Exception("No contacts found");
            }
            foundContact = contacts.get(0);
        }
        else {
            List<ContactDTO> contacts = contactDAO.findContact("cn=" + cname);
            if (contacts.isEmpty()) {
                throw new Exception("No contacts found");
            }
            foundContact = contacts.get(0);
        }

        return foundContact;
    }

    private SetMessageResponseType saveDraft(SetMessageRequestType request, String[] access,
            IMAPSSLStore sslStore, Folder folder, Session session)
            throws MessagingException {
        SetMessageResponseType response = new SetMessageResponseType();
        folder = getImapFolder(session, sslStore, access, "Drafts");
        folder.open(Folder.READ_WRITE);
        IMAPMessage imapMessage = null;
        Message[] msgArr = null;
        try {
            msgArr = createMessage(session, access[0], request);
            folder.appendMessages(msgArr);
        } catch (Exception e) {
            log.error("Error creating draft meassage");
            response.setSuccessStatus(false);
            response.setMessage("Error saving draft message for user = "
                    + request.getUserId());
            e.printStackTrace();
            return response;
        }
        response.setSuccessStatus(true);
        response.setMessage(" ");
        return response;
    }

    /**
     * This methog 
     * @param message
     * @param request
     * @param session
     * @throws Exception 
     */
    private void sendMessagesTOCCBCC(Message[] message, SetMessageRequestType request,
            Session session) throws Exception
    {
        IMAPFolder folder = null;
        IMAPSSLStore sslStore = null;
        Set<String> allContacts = new HashSet<String>();
        allContacts.addAll(request.getContactTo());
        allContacts.addAll(request.getContactCC());
        allContacts.addAll(request.getContactBCC());
        try {

            for (String ctcName : allContacts) {
                ContactDTO dto = findContact(request.getUserId(), ctcName);
                List<String> acctPass = getContactEmailAndPass(dto);
                String[] access = acctPass.toArray(new String[0]);
                URLName urlName = new URLName(ZIMBRA_URL);
                sslStore = new IMAPSSLStore(session, urlName);
                sslStore.connect(host, access[0], access[1]);
                folder = (IMAPFolder) sslStore.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);
                folder.appendMessages(message);
                folder.close(false);
                sslStore.close();
            }

        } catch (Exception e) {
            log.error("Error sending TO, CC and BCC emails: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (folder != null && folder.isOpen()) {
                try {
                    folder.close(false);
                } catch (MessagingException me) {
                    log.error("Error closing folder");
                    me.printStackTrace();
                }
            }
            if (sslStore != null) {
                try {
                    sslStore.close();
                } catch (MessagingException me) {
                    log.error("Error closing SSLStore");
                    me.printStackTrace();
                }
            }
        }
    }
}
