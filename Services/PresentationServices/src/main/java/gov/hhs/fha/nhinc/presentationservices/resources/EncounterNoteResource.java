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

package gov.hhs.fha.nhinc.presentationservices.resources;


import java.io.File;
import org.restlet.Context;

import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;


/**
 *
 * @author markpitman
 */
public class EncounterNoteResource extends BaseResource
{

    private static String prefix = "EN";
    private String patientId = "";
    private String typeOfEncounter = "";
    private String recordId = "";
    private String jsonRequestString = "";

    public EncounterNoteResource(Context context, Request request,
        Response response)
    {
        super(context, request, response);

        try
        {
            String query = request.getResourceRef().getQueryAsForm()
                .getQueryString();

            //      System.out.println("query: "+query);
            if (checkApiCaller(query) != true)
            {
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);

                return;
            }

        }
        catch (Exception e)
        {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

            return;
        }

        patientId = this.getParameter(request, "patientId", "");
        typeOfEncounter = this.getParameter(request, "typeOfEncounter", "");
        recordId = this.getParameter(request, "recordId", "");

        getVariants().clear();
        getVariants().add(new Variant(MediaType.TEXT_HTML));
    }

    /**
     * Returns a full representation for a given variant.
     */
    @Override public Representation represent(Variant variant)
        throws ResourceException
    {

        Representation representation;



        String demoDir = getProperty("staticResources")+"/demodata/";
        LocalReference lr = new LocalReference(demoDir+prefix+"_"+patientId+"_"+recordId+".html");
        File fi = lr.getFile();
        if (fi == null || !fi.exists())
        {
            lr = new LocalReference(demoDir+prefix+"_"+patientId+".html");
            fi = lr.getFile();
            if (fi == null || !fi.exists())
            {
                lr = new LocalReference(demoDir+"defaultencounternote.html");
                fi = lr.getFile();

            }
        }
        if (fi != null || !fi.exists())
        {
                representation = new FileRepresentation(fi
                        ,MediaType.TEXT_HTML);

        }
        else
        {
                throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    "Unable to find encounter note");
        }
        return representation;
    }
}
