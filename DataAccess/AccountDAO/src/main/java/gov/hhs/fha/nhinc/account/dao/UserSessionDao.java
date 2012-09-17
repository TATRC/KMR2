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

package gov.hhs.fha.nhinc.account.dao;

import gov.hhs.fha.nhinc.account.model.UserSession;
import gov.hhs.fha.nhinc.account.util.HibernateUtil;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author nhin
 */
public class UserSessionDao {

    ObjectDao<UserSession> objectDao = new ObjectDao();
    Log log = LogFactory.getLog(UserSessionDao.class);

    public void save(UserSession session) {
        log.debug("Performing UserSession item save");

        try {
            //Update date
            session.setLoginTime(new Date());

            objectDao.save(session);
        } catch (Throwable t) {
            log.error("Failure during object save for user: " + session.getUserId(), t);
        }

        log.debug("Completed user session save");
    }

    public void delete(UserSession session) {
        log.debug("Performing user session delete");

        try {
            objectDao.delete(session);
        } catch (Throwable t) {
            log.error("Failure during user session delete.", t);
        }

        log.debug("Completed user session delete");
    }

    public UserSession findById(String userSessionId) {
        UserSession userSession = null;

        log.debug("Performing user session retrieve using id: " + userSessionId);

        try {
            userSession = objectDao.findById(userSessionId, UserSession.class);
        } catch (Throwable t) {
            log.error("Config file is " + HibernateUtil.getConfigFile().getAbsolutePath() +
                    " Failure during user session findById", t);
        }

        log.debug("Completed user session retrieve by id");

        return userSession;
    }

}
