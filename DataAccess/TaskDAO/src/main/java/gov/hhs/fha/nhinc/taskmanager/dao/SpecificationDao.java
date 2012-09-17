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

package gov.hhs.fha.nhinc.taskmanager.dao;

import gov.hhs.fha.nhinc.taskmanager.model.Specification;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author cmatser
 */
public class SpecificationDao {

    ObjectDao<Specification> objectDao = new ObjectDao();

    Log log = LogFactory.getLog(SpecificationDao.class);

    /**
     * Save a record to the database.
     * Insert if id is null. Update otherwise.
     *
     * @param specification Specification object to save.
     */
    public void save(Specification specification) {
        log.debug("Performing specification save");

        try {
            objectDao.save(specification);
        }
        catch (Throwable t) {
            log.error("Failure during object save.", t);
        }

        log.debug("Completed specification save");
    }

    /**
     * Delete a specification
     *
     * @param specification Specification to delete
     */
    public void delete(Specification specification) {
        log.debug("Performing specification delete");

        try {
            objectDao.delete(specification);
        }
        catch (Throwable t) {
            log.error("Failure during object delete.", t);
        }

        log.debug("Completed specification delete");
    }

    /**
     * Retrieve a specification by identifier
     *
     * @param specificationId Specification identifier
     * @return Retrieved specification
     */
    public Specification findById(Long specificationId) {
        Specification specification = null;

        log.debug("Performing specification retrieve using id: " + specificationId);

        try {
            specification = objectDao.findById(specificationId, Specification.class);
        }
        catch (Throwable t) {
            log.error("Failure during object findById", t);
        }

        log.debug("Completed specification retrieve by id");

        return specification;
    }

    /**
     * Retrieves all specifications
     *
     * @return All specification records
     */
    @SuppressWarnings("unchecked")
    public List<Specification> findAll() {
        List<Specification> specifications = null;

        log.debug("Performing retrieve of all specifications");

        try {
            specifications = objectDao.findAll(Specification.class);
        }
        catch (Throwable t) {
            log.error("Failure during object findAll", t);
        }

        log.debug("Completed specification retrieve all");

        return specifications;
    }

}

