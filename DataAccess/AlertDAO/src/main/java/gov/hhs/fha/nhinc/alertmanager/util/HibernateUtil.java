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
package gov.hhs.fha.nhinc.alertmanager.util;

import gov.hhs.fha.nhinc.kmr.properties.HibernateAccessor;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 *
 * @author cmatser
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static Log log = LogFactory.getLog(HibernateUtil.class);
    private static final String HIBERNATE_TASK_REPOSITORY = "alertmanager.hibernate.cfg.xml";

    static {
        try {
            // Create the SessionFactory from supplied config file.
            log.info("CONFIG FILE = " + getConfigFile());
           // Configuration config = new Configuration().configure(getConfigFile());

            sessionFactory = new Configuration().configure(getConfigFile()).buildSessionFactory();

        } catch (Throwable ex) {
            // Log the exception.
            System.err.println("Initial SessionFactory creation failed." + ex);
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static File getConfigFile() {
        File result = null;

        try {
            result = HibernateAccessor.getHibernateFile(HIBERNATE_TASK_REPOSITORY);
        } catch (Exception ex) {
            log.error("Unable to load " + HIBERNATE_TASK_REPOSITORY + " " + ex.getMessage(), ex);
        }

        //      result = new File("/Users/jharby/src/NHINC/Current/Product/Production/Common/Properties/hibernate/alertmanager.hibernate.cfg.xml");

        return result;
    }
}
