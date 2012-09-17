/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.displayalert;

import gov.hhs.fha.nhinc.common.dda.GetMessagesResponseType.GetMessageResponse;
import java.util.Iterator;
import java.util.List;
import gov.hhs.fha.nhinc.common.dda.GetMessageDetailRequestType;
import gov.hhs.fha.nhinc.common.dda.GetMessageDetailResponseType;
import gov.hhs.fha.nhinc.common.dda.GetMessagesRequestType;
import gov.hhs.fha.nhinc.common.dda.GetMessagesResponseType;
import gov.hhs.fha.nhinc.common.dda.SetMessageRequestType;
import gov.hhs.fha.nhinc.common.dda.SetMessageResponseType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tnguyen
 */
public class DisplayAlertMessagesTest {

    public DisplayAlertMessagesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of setMessage method, of class DisplayAlertMessages.
     */
    //@Test
    public void testSetMessage() {
        System.out.println("setMessage");

        SetMessageRequestType request = null;
        DisplayAlertMessages instance = new DisplayAlertMessages();
        SetMessageResponseType expResult = null;
        SetMessageResponseType result = instance.setMessage(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMessages method, of class DisplayAlertMessages.
     *
        DisplayAlertDataUtil util = new DisplayAlertDataUtil();
        GetMessagesRequestType componentRequest = new GetMessagesRequestType();
        componentRequest.setMessageType(request.getMessageType());
        componentRequest.setPatientId(request.getPatientId());
        componentRequest.setUserId(request.getUserId());
        componentRequest.setLocation(request.getLocation());
        // Fix this - do we need to pass MedAlerts?
        return util.getMessages("MedAlerts", componentRequest);
     *
     */
    //@Test
    public void testGetMessages_EMRinbox() {
        System.out.println("getMessages");

        System.out.println("GET EMR INBOX Alerts");
        GetMessagesRequestType request = new GetMessagesRequestType();
        request.setMessageType("Alert");
        request.setPatientId("99990070");
        request.setUserId("1");
        //request.setLocation("Archive");
        
        DisplayAlertMessages instance = new DisplayAlertMessages();
        GetMessagesResponseType result = instance.getMessages(request);

        List<GetMessageResponse> alist = result.getGetMessageResponse();
        Iterator<GetMessageResponse> iter = alist.iterator();
        while (iter.hasNext()) {
            GetMessageResponse msg = iter.next();
            System.out.println("ID/DESCR: "+msg.getMessageId()+" , "+msg.getDescription());

        }

        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    @Test
    public void testGetMessages_USERinbox() {
        System.out.println("getMessages");

        System.out.println("GET EMR INBOX Alerts");
        GetMessagesRequestType request = new GetMessagesRequestType();
        request.setMessageType("Alert");
        //request.setPatientId("99990070");
        request.setUserId("1");
        //request.setLocation("Archive");

        DisplayAlertMessages instance = new DisplayAlertMessages();
        GetMessagesResponseType result = instance.getMessages(request);

        List<GetMessageResponse> alist = result.getGetMessageResponse();
        Iterator<GetMessageResponse> iter = alist.iterator();
        while (iter.hasNext()) {
            GetMessageResponse msg = iter.next();
            System.out.println("ID/DESCR: "+msg.getMessageId()+" , "+msg.getDescription());

        }

    }

    /**
     * Test of getMessageDetail method, of class DisplayAlertMessages.
     */
    //@Test
    public void testGetMessageDetail() {
        System.out.println("getMessageDetail");
        GetMessageDetailRequestType request = null;
        DisplayAlertMessages instance = new DisplayAlertMessages();
        GetMessageDetailResponseType expResult = null;
        GetMessageDetailResponseType result = instance.getMessageDetail(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
