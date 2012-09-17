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

import org.osaf.caldav4j.dialect.SogoCalDavDialect;
import java.io.IOException;
import net.fortuna.ical4j.model.Calendar;
import org.osaf.caldav4j.CalDAVCollection;
import org.osaf.caldav4j.CalDAVConstants;
import org.osaf.caldav4j.exceptions.CalDAV4JException;
import org.osaf.caldav4j.util.GenerateQuery;
import net.fortuna.ical4j.model.component.CalendarComponent;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osaf.caldav4j.credential.CaldavCredential;
import org.osaf.caldav4j.dialect.CalDavDialect;
import org.osaf.caldav4j.dialect.GoogleCalDavDialect;

/**
 *
 * @author nhin
 */
public class CalendarAccessor {

    private CalendarInit init;
    protected static final Log log = LogFactory.getLog(CalendarInit.class);
    private CaldavCredential credential;
    private CalDavDialect dialect;
    
    public CalendarAccessor() throws Exception {
        credential = new CaldavCredential();
        dialect = new SogoCalDavDialect();
        try {
            init = new CalendarInit(credential, dialect);
        } catch (IOException ioe) {
            log.error("IO Exception creating CalendarAccessor");
            ioe.printStackTrace();
        }
    }

    public Calendar getCalendar(int userId, String type) {
        CalResponse response = new CalResponse();
        CalDAVCollection collection = createCalDAVCollection();
        Calendar calendar = null;
        try {
            GenerateQuery gq = new GenerateQuery(null,
                    CalendarComponent.VEVENT + "UID==" + userId);

            log.info(gq.prettyPrint());
            calendar = collection.queryCalendar(init.getHttpClient(),
                    CalendarComponent.VEVENT, Integer.toString(userId), "test");

        } catch (CalDAV4JException ce) {
            ce.printStackTrace();
        }
        return calendar;
    }

    private CalDAVCollection createCalDAVCollection() {
        CalDAVCollection calendarCollection = new CalDAVCollection(
                init.getCollectionPath(), createHostConfiguration(credential), init.getMethodFactory(),
                CalDAVConstants.PROC_ID_DEFAULT);
        return calendarCollection;
    }

    public static HostConfiguration createHostConfiguration(CaldavCredential caldavCredential) {
        HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost(caldavCredential.host, caldavCredential.port, caldavCredential.protocol);
        return hostConfig;
    }
}
