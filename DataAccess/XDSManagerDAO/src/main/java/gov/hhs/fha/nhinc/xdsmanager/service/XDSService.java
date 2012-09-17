/*
 *   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
 * Copyright (c) 2008, Nationwide Health Information Network (NHIN) Connect. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the NHIN Connect Project nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *   END OF TERMS AND CONDITIONS
 */
package gov.hhs.fha.nhinc.xdsmanager.service;

import gov.hhs.fha.nhinc.xdsmanager.dao.XDSProcessDao;
import gov.hhs.fha.nhinc.xdsmanager.model.ProcessQueryParams;
import gov.hhs.fha.nhinc.xdsmanager.model.XDSProcess;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author cmatser
 */
public class XDSService {

    private static Log log = LogFactory.getLog(XDSService.class);

    /**
     * Save a process record.
     *
     * @param process Process object to save.
     */
    public void saveProcess(XDSProcess process)
    {
        log.debug("Saving a process");

        if (process != null)
        {
            if (process.getProcessId() != null)
            {
                log.debug("Performing an update for process: " + process.getProcessId());
            }
            else
            {
                log.debug("Performing an insert");
            }

        }

        XDSProcessDao dao = new XDSProcessDao();
        dao.save(process);
    }

    /**
     * Delete a process
     *
     * @param process XDSProcess to delete
     */
    public void deleteProcess(XDSProcess process)
    {
        log.debug("Deleting a process");
        XDSProcessDao dao = new XDSProcessDao();
        dao.delete(process);
    }

    /**
     * Retrieve a process by identifier
     *
     * @param processId XDSProcess identifier
     * @return Retrieved process
     */
    public XDSProcess getProcess(Long processId)
    {
        XDSProcessDao dao = new XDSProcessDao();
        return dao.findById(processId);
    }

    /**
     * Retrieves all processes
     *
     * @return All process records
     */
    public List<XDSProcess> getProcesses()
    {
        XDSProcessDao dao = new XDSProcessDao();
        return dao.findAll();
    }

    public List<XDSProcess> getProcessesByParams(ProcessQueryParams params) {
        XDSProcessDao dao = new XDSProcessDao();
        return dao.findProcesses(params);
    }

}
