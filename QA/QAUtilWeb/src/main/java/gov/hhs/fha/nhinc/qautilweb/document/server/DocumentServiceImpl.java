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
package gov.hhs.fha.nhinc.qautilweb.document.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.hhs.fha.nhinc.docmgr.repository.model.Document;
import gov.hhs.fha.nhinc.qautilweb.document.client.DocumentData;

import gov.hhs.fha.nhinc.qautilweb.document.client.DocumentService;
import ihe.iti.xds_b._2007.DocumentManagerService;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

/**
 *
 * @author cmatser
 */
public class DocumentServiceImpl extends RemoteServiceServlet implements DocumentService {

    /** Document Manager web service. */
    private DocumentManagerService docMgrSvc;

    public static final String PT_HOME = "2.16.840.1.113883.3.198";
    public static final String PT_GENDER = "F";
    public static final String PT_ADDR = "4358 Abigail St^^Guthrie^HI^76278^USA";
    public static final String TITLE = "Summarization of Episode Note";
    public static final String TYPE = TITLE;
    public static final String AUTHOR = "NHIN Gateway";
    public static final String AUTHOR_INSTITUTION = "Naval Health Research Center";
    public static final String AUTHOR_ROLE = "Attending";
    public static final String AUTHOR_SPEC = "Orthopedic";

    public List<DocumentData> getAllDocs() {
        ArrayList<DocumentData> docList = new ArrayList<DocumentData>();

        gov.hhs.fha.nhinc.docmgr.repository.service.DocumentService docService = new gov.hhs.fha.nhinc.docmgr.repository.service.DocumentService();
        List<Document> docs = docService.getAllDocuments();

        for (Document doc : docs) {
            DocumentData docData = new DocumentData();
            docData.setDocId(doc.getDocumentid());
            docData.setRepoId(doc.getRepositoryId());
            docData.setPtId(doc.getPatientId());
            docData.setUniqueId(doc.getDocumentUniqueId());
            docList.add(docData);
        }

        return docList;
    }

    public void deleteDoc(Long docId) {
        gov.hhs.fha.nhinc.docmgr.repository.service.DocumentService docService = new gov.hhs.fha.nhinc.docmgr.repository.service.DocumentService();
        Document doc = docService.getDocument(docId);
        try {
            docService.deleteDocument(doc);
        }
        catch (Exception e) {
            //ignore
        }
        return;
    }

    public void insertDoc(String uniqueId, String ptId, String fName, String lName) {

        try { // Call Web Service Operation
            docMgrSvc = new DocumentManagerService();
            ihe.iti.xds_b._2007.DocumentManagerPortType port = docMgrSvc.getDocumentManagerPortSoap();
            ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body = new ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(
                javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                "http://localhost:9763/XDSManagerWeb/DocumentManager_Service?wsdl");

            //Build document metadata
            ExtrinsicObjectType extrinsic = new ExtrinsicObjectType();
            extrinsic.setId("Document1");
            extrinsic.setMimeType("text/plain");
            extrinsic.setObjectType("urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1");

            //RespositoryId *** NEW FIELD ***
            addSlot(extrinsic, "repositoryUniqueId", new String[]{"2"});

            //Creation time
            addSlot(extrinsic, "creationTime", new String[]{"20090921"});

            //Language code
            addSlot(extrinsic, "languageCode", new String[]{"en-us"});

            //Service start time
            addSlot(extrinsic, "serviceStartTime", new String[]{"201102140800"});

            //Service stop time
            addSlot(extrinsic, "serviceStopTime", new String[]{"201102141100"});

            //Source patient id
            addSlot(extrinsic, "sourcePatientId", new String[]{ptId + "^^^" + PT_HOME});

            //Source patient info
            addSlot(extrinsic, "sourcePatientInfo", new String[]{
                        "PID-3|" + ptId + "^^^" + PT_HOME,
                        "PID-5|" + fName + " " + lName,
                        "PID-7|" + ptId,
                        "PID-8|" + PT_GENDER,
                        "PID-11|" + PT_ADDR});

            //Clinical unique hash
            addSlot(extrinsic, "urn:gov:hhs:fha:nhinc:xds:clinicalUniqueHash",
                    new String[]{"myhash2c0e10501972de13a5bfcbe826c49feb75"});

            //Has been accessed
            addSlot(extrinsic, "urn:gov:hhs:fha:nhinc:xds:hasBeenAccessed",
                    new String[]{"false"});

            //Document name & description
            LocalizedStringType localString = new LocalizedStringType();
            localString.setValue(TITLE);
            InternationalStringType intlString = new InternationalStringType();
            intlString.getLocalizedString().add(localString);
            extrinsic.setName(intlString);
            extrinsic.setDescription(intlString);

            //Create Author classification
            addClassification(extrinsic,
                    "Document1", //classifiedObject
                    "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d", //scheme
                    "", //node representation
                    "id_1", //id
                    null, //name
                    new String[]{
                        "authorPerson",
                        "authorInstitution",
                        "authorRole",
                        "authorSpecialty",}, //slot names
                    new String[][]{
                        new String[]{AUTHOR},
                        new String[]{AUTHOR_INSTITUTION},
                        new String[]{AUTHOR_ROLE},
                        new String[]{AUTHOR_SPEC},} //slot values
                    );

            //Create education classification
            addClassification(extrinsic,
                    "Document1", //classifiedObject
                    "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a", //scheme
                    "Communication", //node representation
                    "id_3", //id
                    TYPE, //name
                    new String[]{
                        "codingScheme",}, //slot names
                    new String[][]{
                        new String[]{"Connect-a-thon classCodes"},} //slot values
                    );

            //Create clinical-staff classification
            addClassification(extrinsic,
                    "Document1", //classifiedObject
                    "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f", //scheme
                    "1.3.6.1.4.1.21367.2006.7.103", //node representation
                    "id_4", //id
                    "Clinical-Staff", //name
                    new String[]{
                        "codingScheme",}, //slot names
                    new String[][]{
                        new String[]{"Connect-a-thon confidentialityCodes"},} //slot values
                    );

            //Create CDAR/IHE classification
            addClassification(extrinsic,
                    "Document1", //classifiedObject
                    "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d", //scheme
                    "CDAR2/IHE 1.0", //node representation
                    "id_5", //id
                    "CDAR2/IHE 1.0", //name
                    new String[]{
                        "codingScheme",}, //slot names
                    new String[][]{
                        new String[]{"Connect-a-thon formatCodes"},} //slot values
                    );

            //Create hospital setting classification
            addClassification(extrinsic,
                    "Document1", //classifiedObject
                    "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1", //scheme
                    "Hospital Setting", //node representation
                    "id_6", //id
                    "Hospital Setting", //name
                    new String[]{
                        "codingScheme",}, //slot names
                    new String[][]{
                        new String[]{"Connect-a-thon healthcareFacilityTypeCodes"},} //slot values
                    );

            //Create general medicine classification
            addClassification(extrinsic,
                    "Document1", //classifiedObject
                    "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead", //scheme
                    "General Medicine", //node representation
                    "id_7", //id
                    "Hospital Setting", //name
                    new String[]{
                        "codingScheme",}, //slot names
                    new String[][]{
                        new String[]{"Connect-a-thon practiceSettingCodes"},} //slot values
                    );

            //Create outpatient evaluation and management classification
            addClassification(extrinsic,
                    "Document1", //classifiedObject
                    "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983", //scheme
                    "34108-1", //node representation
                    "id_8", //id
                    "Outpatient Evaluation And Management", //name
                    new String[]{
                        "codingScheme",}, //slot names
                    new String[][]{
                        new String[]{"LOINC"},} //slot values
                    );

            //Add patientId identifier
            addExternalIdentifier(extrinsic,
                    "Document1", //registryObject
                    "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427", //identificationScheme
                    "id_9", //id
                    "XDSDocumentEntry.patientId", //name
                    ptId + "^^^&" + PT_HOME + "&ISO" //value
                    );

            //Add uniqueId identifier
            addExternalIdentifier(extrinsic,
                    "Document1", //registryObject
                    "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab", //identificationScheme
                    "id_10", //id
                    "XDSDocumentEntry.uniqueId", //name
                    uniqueId //value
                    );

            //Create submission set
            RegistryPackageType registryPackage = new RegistryPackageType();
            registryPackage.setId("SubmissionSet01");
            registryPackage.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");

            //Submission time
            addSlot(registryPackage, "submissionTime", new String[]{"20110214235050"});

            //Submission name
            localString = new LocalizedStringType();
            localString.setValue(TITLE);
            intlString = new InternationalStringType();
            intlString.getLocalizedString().add(localString);
            registryPackage.setName(intlString);

            //Submission description
            localString = new LocalizedStringType();
            localString.setValue(TITLE);
            intlString = new InternationalStringType();
            intlString.getLocalizedString().add(localString);
            registryPackage.setDescription(intlString);

            //Add submission author classification
            addClassification(registryPackage,
                    "SubmissionSet01", //classifiedObject
                    "urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d", //scheme
                    "", //node representation
                    "id_20", //id
                    null, //name
                    new String[]{
                        "authorPerson",
                        "authorInstitution",
                        "authorRole",
                        "authorSpecialty",}, //slot names
                    new String[][]{
                        new String[]{AUTHOR},
                        new String[]{AUTHOR_INSTITUTION},
                        new String[]{AUTHOR_ROLE},
                        new String[]{AUTHOR_SPEC},} //slot values
                    );

            //Add submission history and physical classification
            addClassification(registryPackage,
                    "SubmissionSet01", //classifiedObject
                    "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500", //scheme
                    "History and Physical", //node representation
                    "id_21", //id
                    "History and Physical", //name
                    new String[]{
                        "codingScheme",}, //slot names
                    new String[][]{
                        new String[]{"Connect-a-thon contentTypeCodes"},} //slot values
                    );

            //Add submission uniqueId identifier
            addExternalIdentifier(registryPackage,
                    "SubmissionSet01", //registryObject
                    "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8", //identificationScheme
                    "id_22", //id
                    "XDSSubmissionSet.uniqueId", //name
                    "192.168.25.94.114" //value
                    );

            //Add submission sourceId identifier
            addExternalIdentifier(registryPackage,
                    "SubmissionSet01", //registryObject
                    "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832", //identificationScheme
                    "id_23", //id
                    "XDSSubmissionSet.sourceId", //name
                    "129.6.58.92.1.1" //value
                    );

            //Add submission patientId identifier
            addExternalIdentifier(registryPackage,
                    "SubmissionSet01", //registryObject
                    "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446", //identificationScheme
                    "id_24", //id
                    "XDSSubmissionSet.patientId", //name
                    ptId + "^^^&" + PT_HOME + "&ISO" //value
                    );

            //Build association
            AssociationType1 association = new AssociationType1();
            association.setAssociationType("urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember");
            association.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association");
            association.setId("ID_25276323_1");
            association.setSourceObject("SubmissionSet01");
            association.setTargetObject("Document1");

            //Add submission status to assocation
            addSlot(association, "SubmissionSetStatus", new String[]{"Original"});

            //Build classification
            ClassificationType classification = new ClassificationType();
            classification.setClassificationNode("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd");
            classification.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
            classification.setClassifiedObject("SubmissionSet01");
            classification.setId("ID_25276323_3");

            //Build registry object
            ObjectFactory rimObjectFactory = new ObjectFactory();
            JAXBElement<ExtrinsicObjectType> extrinsicMetadata = rimObjectFactory.createExtrinsicObject(extrinsic);
            JAXBElement<RegistryPackageType> submission = rimObjectFactory.createRegistryPackage(registryPackage);
            JAXBElement<AssociationType1> associationObject = rimObjectFactory.createAssociation(association);
            JAXBElement<ClassificationType> classificationObject = rimObjectFactory.createClassification(classification);
            RegistryObjectListType registryList = new RegistryObjectListType();
            registryList.getIdentifiable().add(extrinsicMetadata);
            registryList.getIdentifiable().add(submission);
            registryList.getIdentifiable().add(associationObject);
            registryList.getIdentifiable().add(classificationObject);

            //Build document object
            ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document document =
                    new ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document();
            document.setId("Document1");
            InputStream istream = this.getClass().getResourceAsStream("/dod_pt1.xml");
            BufferedInputStream reader = new BufferedInputStream(istream);
            StringBuilder draftDoc = new StringBuilder();
            for (int c = reader.read(); c != -1; c = reader.read()) {
                draftDoc.append((char) c);
            }

            //Replace document with local values
            String doc = MessageFormat.format(draftDoc.toString(),
                fName, lName, new Date());

            document.setValue(doc.toString().getBytes("US-ASCII"));

            //Add request to body for submission
            SubmitObjectsRequest submitObjects = new SubmitObjectsRequest();
            submitObjects.setRegistryObjectList(registryList);
            body.setSubmitObjectsRequest(submitObjects);
            body.getDocument().add(document);

            // TODO process result here
            oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType result = port.documentManagerStoreDocument(body);
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static void addExternalIdentifier(
            RegistryObjectType registry,
            String registryObject,
            String identificationScheme,
            String id,
            String name,
            String value) {

        ExternalIdentifierType externalId = new ExternalIdentifierType();
        externalId.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier");
        externalId.setRegistryObject(registryObject);
        externalId.setIdentificationScheme(identificationScheme);
        externalId.setId(id);
        externalId.setValue(value);

        //Identifier name
        if (name != null) {
            LocalizedStringType localString = new LocalizedStringType();
            localString.setValue(name);
            InternationalStringType intlName = new InternationalStringType();
            intlName.getLocalizedString().add(localString);
            externalId.setName(intlName);
        }

        //Add classification
        registry.getExternalIdentifier().add(externalId);
    }

    private static void addSlot(RegistryObjectType registry,
            String name, String[] values) {

        SlotType1 slot = new SlotType1();
        slot.setName(name);

        ValueListType valList = new ValueListType();
        for (String value : values) {
            valList.getValue().add(value);
        }

        slot.setValueList(valList);
        registry.getSlot().add(slot);
    }

    private static void addClassification(
            RegistryObjectType registry,
            String classifiedObject,
            String classificationScheme,
            String nodeRepresentation,
            String id,
            String name,
            String[] slotNames,
            String[][] slotValues) {

        //Create classification
        ClassificationType classification = new ClassificationType();
        classification.setClassificationScheme(classificationScheme);
        classification.setNodeRepresentation(nodeRepresentation);
        classification.setClassifiedObject(classifiedObject);
        classification.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
        classification.setId(id);

        //Classification name
        if (name != null) {
            LocalizedStringType localString = new LocalizedStringType();
            localString.setValue(name);
            InternationalStringType intlName = new InternationalStringType();
            intlName.getLocalizedString().add(localString);
            classification.setName(intlName);
        }

        //Slots
        for (int i = 0; i < slotNames.length; i++) {
            addSlot(classification, slotNames[i], slotValues[i]);
        }

        //Add classification
        registry.getClassification().add(classification);
    }

}
