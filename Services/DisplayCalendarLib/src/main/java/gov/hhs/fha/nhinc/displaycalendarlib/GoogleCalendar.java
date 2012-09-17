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
package gov.hhs.fha.nhinc.displaycalendarlib;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.data.calendar.HiddenProperty;
import com.google.gdata.data.calendar.TimeZoneProperty;
import com.google.gdata.data.extensions.When;
import java.net.URL;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import com.google.gdata.data.extensions.Where;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.common.collect.Maps;
import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.data.Content;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Feed;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.TextContent;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.XmlBlob;
import com.google.gdata.util.common.xml.XmlWriter;
import gov.hhs.fha.nhinc.ldapaccess.ContactAcctDTO;
import gov.hhs.fha.nhinc.ldapaccess.ContactDAO;
import gov.hhs.fha.nhinc.ldapaccess.ContactDTO;
import gov.hhs.fha.nhinc.ldapaccess.LdapService;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

/**
 *
 * @author nhin
 */
public class GoogleCalendar {

    String LDAP_PATIENT_ID_ATT = "uid";
    /**
     * LDAP attribute for provider's user id.
     */
    String LDAP_PROVIDER_ID_ATT = "employeeNumber";
    private CalendarService myService;
    protected static final Log log = LogFactory.getLog(GoogleCalendar.class);
    private final String CALENDAR_FEED_URL =
            "https://www.google.com/calendar/feeds/default/owncalendars/full";
    private static final String CALENDAR_EVENT_URL =
            "https://www.google.com/calendar/feeds/default/private/full";

    public GoogleCalendar() {
    }

    public GoogleCalendar(String userId) {
        myService = new CalendarService("socraticgrid-inbox-1");
        try {
            myService.setUserCredentials(userId, "twtw1234");
        } catch (AuthenticationException ae) {
            log.error("Authentication failed");
            ae.printStackTrace();
        }
    }

    public CalendarService getCalendarService(String userId, String password) {
        CalendarService service = new CalendarService("socraticgrid-inbox-1");
        try {
            service.setUserCredentials(userId, password);
        } catch (AuthenticationException ae) {
            log.error("Authentication failed");
            ae.printStackTrace();
        }
        return service;
    }

    public List<CalResponse> getCalsForUser(String userId, String type) throws MalformedURLException, AuthenticationException,
            IOException, ServiceException {
        //Get email address and password from LDAP
        ContactDTO contact = null;
        String[] access = getLdapAccess(userId);
        // FOR TEST SET ACCESS
//        access[0] = "jhart92108";
//        access[1] = "twtw1234";
        CalendarService calService = getCalendarService(access[0], access[1]);
        List<CalResponse> returnList = new ArrayList<CalResponse>();
        // Send the request and print the response
        URL feedUrl = new URL(CALENDAR_FEED_URL);
        CalendarFeed resultFeed = calService.getFeed(feedUrl, CalendarFeed.class);
        StringBuilder sb = new StringBuilder();

        sb.append("BEGIN:VCALENDAR\n");
        sb.append("PRODID:-//Google Inc//Google Calendar 70.9054//EN\n");
        sb.append("VERSION:2.0\n");
        sb.append("CALSCALE:GREGORIAN\n");
        sb.append("METHOD:PUBLISH\n");

        log.info("Calendars for user: " + userId + "\n");
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            CalendarEntry entry = resultFeed.getEntries().get(i);
            sb.append("X-WR-CALNAME:").append(entry.getTitle().getPlainText()).append("\n");
            sb.append("X-WR-TIMEZONE:").append(entry.getTimeZone().getValue()).append("\n");
            CalResponse response = new CalResponse();
            
            response.setType("Private");
            response.setEvents(new ArrayList<Events>());
            response.setTitle(entry.getTitle().getPlainText());
            String color = entry.getColor().getValue();
            response.setBackgroundColor(color);
            response.setBorderColor(color);
            response.setTextColor("#FFF");
            response.setCalendarId(entry.getId());

            StringBuilder iCalSB = new StringBuilder();
            Writer w = new StringWriter();
            XmlWriter writer = new XmlWriter(w);

            URL eventFeedUrl = new URL(CALENDAR_EVENT_URL);
            CalendarQuery cq = new CalendarQuery(eventFeedUrl);
            cq.setMinimumStartTime(DateTime.parseDateTime("2011-01-01T00:00:00"));
            CalendarEventFeed eventFeed = calService.query(cq, CalendarEventFeed.class);
            eventFeed.generate(writer, calService.getExtensionProfile());
            List<Events> eventList = new ArrayList<Events>();

            for (CalendarEventEntry event : eventFeed.getEntries()) {
                Events calEvent = new Events();
                DateTime startTime = event.getTimes().get(0).getStartTime();
                DateTime endTime = event.getTimes().get(0).getEndTime();
                Double d1 = startTime.getValue() * .001;
                Double d2 = endTime.getValue() * .001;
                calEvent.setStart(Long.toString(d1.longValue()));
                calEvent.setEnd(Long.toString(d2.longValue()));
                calEvent.setTitle(event.getTitle().getPlainText());
                calEvent.setEventId(event.getId());
                calEvent.setEditable(event.getCanEdit());
                if (endTime.getValue() - startTime.getValue() >= (1000 * 60 * 60 * 24)) {
                    calEvent.setAllDay(true);
                } else {
                    calEvent.setAllDay(false);
                }
                response.getEvents().add(calEvent);
            }
            returnList.add(response);
            log.info("\t" + entry.getTitle().getPlainText());
        }
        return returnList;
    }

    public CalendarEntry createCalendarEntry() throws Exception {
        // Create the calendar
        CalendarEntry calendar = new CalendarEntry();
        calendar.setTitle(new PlainTextConstruct("Test Calendar"));
        calendar.setSummary(new PlainTextConstruct("This calendar contains a test calendar."));
        calendar.setTimeZone(new TimeZoneProperty("America/Los_Angeles"));
        calendar.setHidden(HiddenProperty.FALSE);
        calendar.setColor(new ColorProperty("#2952A3"));
        Where where = new Where();
        where.setLabel("");
        where.setRel("");
        where.setValueString("San Diego");
        calendar.addLocation(where);

        // Insert the calendar
        URL postUrl = new URL("https://www.google.com/calendar/feeds/default/owncalendars/full");
        CalendarEntry returnedCalendar = myService.insert(postUrl, calendar);
        return returnedCalendar;
    }

    public void createCalEvent(String title, String content) throws MalformedURLException, IOException,
            ServiceException {
        URL postUrl =
                new URL("https://www.google.com/calendar/feeds/jhart92108@gmail.com/private/full");
        CalendarEventEntry myEntry = new CalendarEventEntry();

        myEntry.setTitle(new PlainTextConstruct(title));
        myEntry.setContent(new PlainTextConstruct(content));

        DateTime startTime = DateTime.parseDateTime("2011-09-23T15:00:00-08:00");
        DateTime endTime = DateTime.parseDateTime("2011-09-23T17:00:00-08:00");
        When eventTimes = new When();
        eventTimes.setStartTime(startTime);
        eventTimes.setEndTime(endTime);
        myEntry.addTime(eventTimes);

        // Send the request and receive the response:
        CalendarEventEntry insertedEntry = myService.insert(postUrl, myEntry);
    }

    public void deleteCalendar(String calendarTitle) throws MalformedURLException, IOException, ServiceException {
        URL feedUrl = new URL("https://www.google.com/calendar/feeds/default/owncalendars/full");
        CalendarFeed resultFeed = myService.getFeed(feedUrl, CalendarFeed.class);
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            CalendarEntry entry = resultFeed.getEntries().get(i);
            if (entry.getTitle().getPlainText().equals(calendarTitle)) {
                System.out.println("Deleting calendar: " + entry.getTitle().getPlainText());
                try {
                    entry.delete();
                } catch (InvalidEntryException e) {
                    System.out.println("\tUnable to delete primary calendar");
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        GoogleCalendar gc = new GoogleCalendar("jhart92108@gmail.com");
        log.info("DELETECALENDARS");
        gc.deleteCalendar("Test Calendar");
        log.info("CREATECALENTRY");
        gc.createCalendarEntry();
        log.info("CREATECALEVENT");
        gc.createCalEvent("Test event title", "Test event content");
        log.info("GETCALSFORUSER");
        List<CalResponse> calList = gc.getCalsForUser("jhart92108@gmail.com", "Personal");
        for (CalResponse cr : calList) {
            System.out.println("Entry: \n" + cr.toString());
        }
    }

    private String[] retrieveMailAccess(String userType, String userId) {
        String access[] = new String[2];

        ContactDAO contactDAO = LdapService.getContactDAO();
        List<ContactDTO> contacts = contactDAO.findContact(
                ("pv".equals(userType)
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

    private String[] getLdapAccess(String userId) {
        ContactDTO contact = null, ptContact = null;
        ContactDAO contactDAO = LdapService.getContactDAO();
        contact = contactDAO.findContact("uid=" + userId).get(0);
        String userCn = contact.getCommonName();
        ContactAcctDTO acct =
                contactDAO.findContactAcct(contact.getCommonName(), "PCal.Account").get(0);
        
        String[] access = new String[2];
        access[0] = acct.getUid();
        access[1] = acct.getClearPassword();
//        if (contact.getEmployeeNumber() != null) {
//            userType = "pv";
//            access = retrieveMailAccess(userType, contact.getEmployeeNumber());
//        } else {
//            userType = "pt";
//            access = retrieveMailAccess(userType, contact.getUid());
//        }

        return access;
    }
    //   private String getIcalString() {
    //                sb.append("BEGIN:VEVENT" + "\n");
//                sb.append("DTSTART:").append(event.getTimes().get(0).getStartTime()).append("\n");
//                sb.append("DTEND:").append(event.getTimes().get(0).getEndTime()).append("\n");
//                sb.append("DTSTAMP:").append(new Date()).append("\n");
//                sb.append("UID:").append(event.getIcalUID()).append("\n");
//                sb.append("CREATED:").append(event.getPublished()).append("\n");
//                sb.append("DESCRIPTION:").append(event.getPlainTextContent()).append("\n");
//                sb.append("LAST-MODIFIED:").append(event.getEdited()).append("\n");
//                sb.append("LOCATION:").append(event.getLocations().get(0).getValueString()).append("\n");
//                sb.append("SEQUENCE:").append(event.getSequence()).append("\n");
//                sb.append("STATUS:").append(event.getStatus().getValue()).append("\n");
//                if (event.getSummary() != null)
//                    sb.append("SUMMARY:").append(event.getSummary().getPlainText()).append("\n");
//                sb.append("TRANSP:").append(event.getTransparency().getValue()).append("\n");
//                sb.append("END:VEVENT");
//            }
////            iCalSB.append(w.toString());
////            response.setiCalData((iCalSB.toString()));
//            sb.append("END:VCALENDAR");
//            response.setiCalData(sb.toString());
//    }
}
