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

package gov.hhs.fha.nhinc.presentationservices;

import gov.hhs.fha.nhinc.presentationservices.helpers.PropertyHelper;
import gov.hhs.fha.nhinc.presentationservices.resources.AdmissionsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.ProblemsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.MedicationsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.TestResource;
import gov.hhs.fha.nhinc.presentationservices.resources.ApiDocResource;
import gov.hhs.fha.nhinc.presentationservices.resources.AllergiesResource;
import gov.hhs.fha.nhinc.presentationservices.resources.AppointmentsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.DiagnosticImagingResource;
import gov.hhs.fha.nhinc.presentationservices.resources.DocumentsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.EmergencyContactResource;
import gov.hhs.fha.nhinc.presentationservices.resources.EncounterDetailResource;
import gov.hhs.fha.nhinc.presentationservices.resources.EncounterNoteResource;
import gov.hhs.fha.nhinc.presentationservices.resources.LabsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.LoginResource;
import gov.hhs.fha.nhinc.presentationservices.resources.ExternalCalendarResource;
import gov.hhs.fha.nhinc.presentationservices.resources.GetAddressBookResource;
import gov.hhs.fha.nhinc.presentationservices.resources.GetCalendarResource;
import gov.hhs.fha.nhinc.presentationservices.resources.diagnostics.GetDGProcessStatusResource;
import gov.hhs.fha.nhinc.presentationservices.resources.GetECSResource;
import gov.hhs.fha.nhinc.presentationservices.resources.messages.GetMessagesResource;

import gov.hhs.fha.nhinc.presentationservices.resources.facts.GetPatientDataResource;

import gov.hhs.fha.nhinc.presentationservices.resources.GetRequiredsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.ImmunizationsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.LocationClinicsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.LocationScheduleResource;
import gov.hhs.fha.nhinc.presentationservices.resources.LocationWardsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.PatientDemographicsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.MailResource;
import gov.hhs.fha.nhinc.presentationservices.resources.MailActionResource;
import gov.hhs.fha.nhinc.presentationservices.resources.MailStatusResource;
import gov.hhs.fha.nhinc.presentationservices.resources.MaintainAccountResource;
import gov.hhs.fha.nhinc.presentationservices.resources.messages.GetMessageDetailResource;
import gov.hhs.fha.nhinc.presentationservices.resources.riskmodel.GetRiskModelsDetailResource;
import gov.hhs.fha.nhinc.presentationservices.resources.riskmodel.GetRiskModelsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.VitalsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.SchedulingResource;
import gov.hhs.fha.nhinc.presentationservices.resources.ToDoResource;
import gov.hhs.fha.nhinc.presentationservices.resources.PmrPreferencesResource;
import gov.hhs.fha.nhinc.presentationservices.resources.MobileResource;


import gov.hhs.fha.nhinc.presentationservices.resources.OrdersResource;
import gov.hhs.fha.nhinc.presentationservices.resources.PatientCensusResource;
import gov.hhs.fha.nhinc.presentationservices.resources.PatientsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.ProcedureResource;
import gov.hhs.fha.nhinc.presentationservices.resources.ProvidersImageResource;
import gov.hhs.fha.nhinc.presentationservices.resources.ProvidersResource;
import gov.hhs.fha.nhinc.presentationservices.resources.RegistryResource;
import gov.hhs.fha.nhinc.presentationservices.resources.SMSResource;
import gov.hhs.fha.nhinc.presentationservices.resources.diagnostics.SetDiagnosticActionStatusResource;
import gov.hhs.fha.nhinc.presentationservices.resources.messages.SetMessagesResource;
import gov.hhs.fha.nhinc.presentationservices.resources.riskmodel.SetRiskModelFavoritesResource;
import gov.hhs.fha.nhinc.presentationservices.resources.diagnostics.StartDGProcessResource;
import gov.hhs.fha.nhinc.presentationservices.resources.SurveyResource;
import gov.hhs.fha.nhinc.presentationservices.resources.ValidateAccountResource;
import gov.hhs.fha.nhinc.presentationservices.resources.diagnostics.AdvanceDGProcessResource;
import gov.hhs.fha.nhinc.presentationservices.resources.diagnostics.CompleteDGProcessResource;
import gov.hhs.fha.nhinc.presentationservices.resources.messages.GetDocumentsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.simulator.CommandSimulationResource;
import gov.hhs.fha.nhinc.presentationservices.resources.simulator.GetConfigurationDetailResource;
import gov.hhs.fha.nhinc.presentationservices.resources.simulator.GetPlanningModelsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.simulator.GetResultDetailsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.simulator.GetResultsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.simulator.GetSimulationDetailResource;
import gov.hhs.fha.nhinc.presentationservices.resources.simulator.GetSimulationsResource;
import gov.hhs.fha.nhinc.presentationservices.resources.simulator.RequestResultOperationResource;
import gov.hhs.fha.nhinc.presentationservices.resources.simulator.SaveConfigurationResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Application;
//import org.restlet.Context;
import org.restlet.Directory;
import org.restlet.Router;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.Redirector;

/**
 *
 * @author mep
 */
public class PresentationServicesApplication extends Application {

    private static Log log = LogFactory.getLog(PresentationServicesApplication.class);
    private Router router;
    
    //create root
    @Override
    public Restlet createRoot() {

        //create router that routes each call to a new instance of PatientInfoResources
        router = new Router(getContext());

        router.attachDefault(ApiDocResource.class);

        log.info("STATIC RESOURCE DIR IS: ");
        log.info(PropertyHelper.getPropertyHelper().getProperty("staticResources"));

        router.attach("/", new Directory(
                getContext(),
                new LocalReference(PropertyHelper.getPropertyHelper().getProperty("staticResources"))));

        router.attach("/",
                new Redirector(router.getContext().createChildContext(),
                "/Resources",
                Redirector.MODE_CLIENT_SEE_OTHER));



        //PORTAL LANDING PAGE APIs
        router.attach("/getRequiredFields", GetRequiredsResource.class);
        //setFeedBack
        //getHelp

        //LOGIN/AUTHENTICATE APIs
        router.attach("/validateAccount", ValidateAccountResource.class);
        //getMicellaneous
        //getAccountInfo
        //getAccount
        router.attach("/setAccount", MaintainAccountResource.class);
        //getPatient
        //getProvider


        //INBOX MSGS APIs
        router.attach("/getMessages", GetMessagesResource.class);
        router.attach("/getMessageDetail", GetMessageDetailResource.class);
        router.attach("/setMessages", SetMessagesResource.class);
        router.attach("/getDocuments", GetDocumentsResource.class);
        router.attach("/getAddressBook", GetAddressBookResource.class);
        //searchMessages
        
        router.attach("/getCalendar", GetCalendarResource.class);
        //setCalendar

        router.attach("/getSurvey", SurveyResource.class);
        router.attach("/setSurvey", SurveyResource.class);



        //RISK MODEL PREDICTION APIs
        router.attach("/getRiskModels", GetRiskModelsResource.class);
        router.attach("/getRiskModelsDetail", GetRiskModelsDetailResource.class);
        router.attach("/setRiskModelFavorites", SetRiskModelFavoritesResource.class);


        //DX GUIDE APIs
        router.attach("/startDiagnosticGuideProcess", StartDGProcessResource.class);
        router.attach("/getDiagnosticGuideProcessStatus", GetDGProcessStatusResource.class);
        router.attach("/completeDiagnosticGuideProcess", CompleteDGProcessResource.class);
        router.attach("/advanceDiagnosticGuideProcess", AdvanceDGProcessResource.class);
        router.attach("/setDiagnosticActionStatus", SetDiagnosticActionStatusResource.class);


        //SIMULATION APIs
        router.attach("/getPlanningModels", GetPlanningModelsResource.class);
        //router.attach("/searchConfigurationsResource", SearchConfigurationsResource.class);
        router.attach("/getConfigurationDetail", GetConfigurationDetailResource.class);
        router.attach("/saveConfiguration", SaveConfigurationResource.class);
        router.attach("/getSimulations", GetSimulationsResource.class);
        router.attach("/getSimulationDetail", GetSimulationDetailResource.class);
        router.attach("/commandSimulation", CommandSimulationResource.class);
        //router.attach("/launchSimulation", LaunchSimulationResource.class);

        router.attach("/getResults", GetResultsResource.class);
        router.attach("/getResultDetails", GetResultDetailsResource.class);
        router.attach("/requestResultOperation", RequestResultOperationResource.class);



        //FACTS-ECS APIs
        router.attach("/getPatientData", GetPatientDataResource.class);



        //FACTS - KMR1
        /*
        router.attach("/Allergies", AllergiesResource.class);
        router.attach("/Admissions", AdmissionsResource.class);
        router.attach("/Appointments", AppointmentsResource.class);
        router.attach("/Demographics", PatientDemographicsResource.class);
        router.attach("/DiagnosticImaging", DiagnosticImagingResource.class);
        router.attach("/Documents", DocumentsResource.class);
        router.attach("/EmergencyContact", EmergencyContactResource.class);
        router.attach("/EncounterDetail", EncounterDetailResource.class);
        router.attach("/Encounter/Detail", EncounterDetailResource.class);
        router.attach("/Encounter/Note", EncounterNoteResource.class);
        router.attach("/Immunizations", ImmunizationsResource.class);
        router.attach("/Labs", LabsResource.class);
        router.attach("/LocationSchedule", LocationScheduleResource.class);
        router.attach("/Locations/Clinics", LocationClinicsResource.class);
        router.attach("/Locations/Wards", LocationWardsResource.class);
        router.attach("/Orders", OrdersResource.class);
        router.attach("/PatientCensus", PatientCensusResource.class);
        router.attach("/Patients", PatientsResource.class);
        router.attach("/Medications", MedicationsResource.class);
        router.attach("/Problems", ProblemsResource.class);
        router.attach("/Procedures", ProcedureResource.class);
        router.attach("/Provider", ProvidersResource.class);
        router.attach("/Providers", ProvidersResource.class);
        router.attach("/Providers/Image", ProvidersImageResource.class);
        router.attach("/Todos", ToDoResource.class);
        router.attach("/Vitals", VitalsResource.class);
         * 
         */

        //router.attach("/getECS", GetECSResource.class);
        //router.attach("/MessageDetail", GetMessageDetailResource.class);
        //router.attach("/Calendar/External", ExternalCalendarResource.class);
        //router.attach("/Login", LoginResource.class);
        //router.attach("/Mail", MailResource.class);
        //router.attach("/Mail/Status", MailStatusResource.class);
        //router.attach("/MailAction", MailActionResource.class);
        //router.attach("/Mail/Action", MailActionResource.class);
        //router.attach("/Mobile", MobileResource.class);
        //router.attach("/PmrPreferences", PmrPreferencesResource.class);
        //router.attach("/Schedules", SchedulingResource.class);


        //TEST ONLY - this one is for testing - sandbox related
        //router.attach("/test", TestResource.class);
        //router.attach("/SMSResource", SMSResource.class);
        //router.attach("/RegistryResource", RegistryResource.class);
        
        return router;
    }
   
    
}
