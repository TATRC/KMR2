package gov.hhs.fha.nhinc.kmr;

import gov.hhs.fha.nhinc.adapter.fact.AdmissionFactType;
import gov.hhs.fha.nhinc.adapter.fact.AllergyFactType;
import gov.hhs.fha.nhinc.adapter.fact.AppointmentFactType;
import gov.hhs.fha.nhinc.adapter.fact.DiagnosisFactType;
import gov.hhs.fha.nhinc.adapter.fact.ImmunizationFactType;
import gov.hhs.fha.nhinc.adapter.fact.PersonFactType;
import gov.hhs.fha.nhinc.adapter.fact.SupportSourceFactType;
import gov.hhs.fha.nhinc.adapter.fact.MedicationFactType;
import gov.hhs.fha.nhinc.adapter.fact.ProblemFactType;
import gov.hhs.fha.nhinc.adapter.fact.ProcedureFactType;
import gov.hhs.fha.nhinc.adapter.fact.ResultFactType;
import gov.hhs.fha.nhinc.adapter.fact.VitalFactType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Duane DeCouteau
 */
public class VirtualMedicalRecord {
    private String patientId;
    private XMLGregorianCalendar dateStoreCreated;
    private PersonFactType demographics;
    private List<SupportSourceFactType> supportSources;
    private List<AllergyFactType> allergies;
    private List<MedicationFactType> medications;
    private List<ProblemFactType> problems;
    private List<ResultFactType> results;
    private List<ProcedureFactType> procedures;
    private List<AppointmentFactType> appointments;
    private List<AdmissionFactType> admissions;
    private List<DiagnosisFactType> diagnoses;
    private List<ImmunizationFactType> immunizations;
    private List<VitalFactType> vitals;
    //add additional domains as they become available

    public VirtualMedicalRecord() {
    }

    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the dateStoreCreated
     */
    public XMLGregorianCalendar getDateStoreCreated() {
        return dateStoreCreated;
    }

    /**
     * @param dateStoreCreated the dateStoreCreated to set
     */
    public void setDateStoreCreated(XMLGregorianCalendar dateStoreCreated) {
        this.dateStoreCreated = dateStoreCreated;
    }

    /**
     * @return the demographics
     */
    public PersonFactType getDemographics() {
        return demographics;
    }

    /**
     * @param demographics the demographics to set
     */
    public void setDemographics(PersonFactType demographics) {
        this.demographics = demographics;
    }

    /**
     * @return the support Sources
     */
    public List<SupportSourceFactType> getSupportSources() {
        if (supportSources == null) {
            supportSources = new ArrayList<SupportSourceFactType>();
        }
        return supportSources;
    }

    /**
     * @return the allergies
     */
    public List<AllergyFactType> getAllergies() {
        if (allergies == null) {
            allergies = new ArrayList<AllergyFactType>();
        }
        return allergies;
    }

    /**
     * @return the medications
     */
    public List<MedicationFactType> getMedications() {
        if (medications == null) {
            medications = new ArrayList<MedicationFactType>();
        }
        return medications;
    }

    /**
     * @return the problems
     */
    public List<ProblemFactType> getProblems() {
        if (problems == null) {
            problems = new ArrayList<ProblemFactType>();
        }
        return problems;
    }

    /**
     * @return the results
     */
    public List<ResultFactType> getResults() {
        if (results == null) {
            results = new ArrayList<ResultFactType>();
        }
        return results;
    }

    /**
     * @return procedures from VMR
     */
    public List<ProcedureFactType> getProcedures() {
        if (procedures == null) {
            procedures = new ArrayList<ProcedureFactType>();
        }
        return procedures;
    }

    /**
     * @return appointments from VMR
     */
    public List<AppointmentFactType> getAppointments() {
        if (appointments == null) {
            appointments = new ArrayList<AppointmentFactType>();
        }
        return appointments;
    }

    /**
     * @return admissions from VMR
     */
    public List<AdmissionFactType> getAdmissions() {
        if (admissions == null) {
            admissions = new ArrayList<AdmissionFactType>();
        }
        return admissions;
    }

    /**
     * @return immunizations from VMR
     */
    public List<ImmunizationFactType> getImmunizations() {
        if (immunizations == null) {
            immunizations = new ArrayList<ImmunizationFactType>();
        }
        return immunizations;
    }

    /**
     * @return diagnoses from VMR
     */
    public List<DiagnosisFactType> getDiagnoses() {
        if (diagnoses == null) {
            diagnoses = new ArrayList<DiagnosisFactType>();
        }
        return diagnoses;
    }

    /**
     * @return vitals from VMR
     */
    public List<VitalFactType> getVitals() {
        if (vitals == null) {
            vitals = new ArrayList<VitalFactType>();
        }
        return vitals;
    }
}
