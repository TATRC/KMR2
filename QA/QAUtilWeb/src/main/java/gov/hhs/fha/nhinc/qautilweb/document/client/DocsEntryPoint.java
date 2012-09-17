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
package gov.hhs.fha.nhinc.qautilweb.document.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.Date;
import java.util.List;

/**
 * Main entry point.
 *
 * @author cmatser
 */
public class DocsEntryPoint implements EntryPoint {

    private FlexTable docFlexTable = new FlexTable();
    private Button refreshDocTableButton = new Button("Refresh");
    private Label lastUpdatedLabel = new Label("Loading first time...");
    private Button insertButton = new Button("Insert New Doc");
    private TextBox insertDocUniqueIdTextBox = new TextBox();
    private TextBox insertPtIdTextBox = new TextBox();
    private TextBox insertPtFNameTextBox = new TextBox();
    private TextBox insertPtLNameTextBox = new TextBox();

    public static final String DEFAULT_PT_ID = "99990070";
    public static final String DEFAULT_PT_FNAME = "Jane";
    public static final String DEFAULT_PT_LNAME = "Doe";

    /**
     * Creates a new instance of DocsEntryPoint
     */
    public DocsEntryPoint() {
    }

    /** 
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point
     */
    public void onModuleLoad() {
        VerticalPanel mainPanel = new VerticalPanel();
        VerticalPanel docTablePanel = new VerticalPanel();
        Grid docInsertPanel = new Grid(5,2);

        // Create panel for docs
        docTablePanel.add(docFlexTable);
        docTablePanel.add(refreshDocTableButton);

        //Create panel for insert
        insertDocUniqueIdTextBox.setText("");
        insertPtIdTextBox.setText(DEFAULT_PT_ID);
        insertPtFNameTextBox.setText(DEFAULT_PT_FNAME);
        insertPtLNameTextBox.setText(DEFAULT_PT_LNAME);
        docInsertPanel.addStyleName("insertPanel");
        docInsertPanel.setWidget(0, 0, new Label("unique id:"));
        docInsertPanel.setWidget(0, 1, insertDocUniqueIdTextBox);
        docInsertPanel.setWidget(1, 0, new Label("pt id:"));
        docInsertPanel.setWidget(1, 1, insertPtIdTextBox);
        docInsertPanel.setWidget(2, 0, new Label("pt first name:"));
        docInsertPanel.setWidget(2, 1, insertPtFNameTextBox);
        docInsertPanel.setWidget(3, 0, new Label("pt last name:"));
        docInsertPanel.setWidget(3, 1, insertPtLNameTextBox);
        docInsertPanel.setWidget(4, 0, insertButton);

        // Wrap the content in a DecoratorPanel
        DecoratorPanel decTablePanel = new DecoratorPanel();
        decTablePanel.setWidget(docTablePanel);
        DecoratorPanel decInsertPanel = new DecoratorPanel();
        decInsertPanel.setWidget(docInsertPanel);

        // Assemble Main panel.
        mainPanel.setSpacing(5);
        mainPanel.add(lastUpdatedLabel);
        mainPanel.add(decTablePanel);
        mainPanel.add(decInsertPanel);

        RootPanel.get().add(mainPanel);

        // Listen for mouse events on the refresh button.
        refreshDocTableButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                showWorking();
                updateDocTable();
            }
        });

        // Listen for mouse events on the insert button.
        insertButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                showWorking();
                doInsert();
            }
        });

        //Get table contents for first display
        updateDocTable();
    }

    /**
     * Fetch all documents from the db.
     */
    private void updateDocTable() {

        //Clear out the table
        docFlexTable.removeAllRows();

        //Add the header info
        docFlexTable.setText(0, 0, "DocumentID");
        docFlexTable.setText(0, 1, "RepoID");
        docFlexTable.setText(0, 2, "PatientID");
        docFlexTable.setText(0, 3, "DocumentUniqueID");
        docFlexTable.setText(0, 4, "Remove");

        docFlexTable.getRowFormatter().addStyleName(0, "docListHeader");
        docFlexTable.addStyleName("docList");
        docFlexTable.getCellFormatter().addStyleName(0, 0, "docListNumericColumn");
        docFlexTable.getCellFormatter().addStyleName(0, 1, "docListNumericColumn");
        docFlexTable.getCellFormatter().addStyleName(0, 2, "docListNumericColumn");
        docFlexTable.getCellFormatter().addStyleName(0, 4, "docListRemoveColumn");

        // Create an asynchronous callback to handle the result data.
        final AsyncCallback<List<DocumentData>> getDocsCallback = new AsyncCallback<List<DocumentData>>() {

            public void onSuccess(List<DocumentData> docList) {
                int row = 1;

                //Fill table
                for (DocumentData doc : docList) {
                    int col = 0;
                    final Long docId = doc.getDocId();
                    String repoId = doc.getRepoId();
                    String ptId = doc.getPtId();
                    String docUniqueId = doc.getUniqueId();

                    docFlexTable.setText(row, col, docId.toString());
                    docFlexTable.getCellFormatter().addStyleName(row, col++, "docListNumericColumn");
                    docFlexTable.setText(row, col, repoId);
                    docFlexTable.getCellFormatter().addStyleName(row, col++, "docListNumericColumn");
                    docFlexTable.setText(row, col, ptId);
                    docFlexTable.getCellFormatter().addStyleName(row, col++, "docListNumericColumn");
                    docFlexTable.setText(row, col++, docUniqueId);


                    // Add a button to remove this doc from the table.
                    Button removeDocButton = new Button("x");
                    removeDocButton.addStyleDependentName("remove");
                    removeDocButton.addClickHandler(new ClickHandler() {

                        public void onClick(ClickEvent event) {

                            final AsyncCallback removeDocCallback = new AsyncCallback() {

                                public void onSuccess(Object result) {
                                    //Refresh table
                                    updateDocTable();
                                }

                                public void onFailure(Throwable caught) {
                                    ErrorHandler.showError("Removing doc failed.", caught);
                                }
                            };

                            boolean confirm = Window.confirm("Are you sure you want to delete document id: " + docId + "?");
                            if (confirm) {
                                showWorking();
                                getDocumentService().deleteDoc(docId, removeDocCallback);
                            }
                            else {
                                //do nothin
                            }
                        }
                    });
                    docFlexTable.setWidget(row, col, removeDocButton);
                    docFlexTable.getCellFormatter().addStyleName(row, col++, "docListRemoveColumn");

                    row++;
                }

                updatePageTimestamp();
            }

            public void onFailure(Throwable caught) {
                ErrorHandler.showError("Getting all docs failed. ", caught);
                updatePageTimestamp();
            }
        };

        getDocumentService().getAllDocs(getDocsCallback);
    }

    //Insert document
    private void doInsert() {
        String docUniqueId = insertDocUniqueIdTextBox.getText();
        String ptId = insertPtIdTextBox.getText();
        String ptFName = insertPtFNameTextBox.getText();
        String ptLName = insertPtLNameTextBox.getText();

        //Default pt id
        if (ptId.isEmpty()) {
            ptId = DEFAULT_PT_ID;
        }

        //Default pt first name
        if (ptFName.isEmpty()) {
            ptFName = DEFAULT_PT_FNAME;
        }

        //Default pt last name
        if (ptLName.isEmpty()) {
            ptLName = DEFAULT_PT_LNAME;
        }

        // Create an asynchronous callback to handle the result data.
        final AsyncCallback insertCallback = new AsyncCallback() {

            public void onSuccess(Object result) {
                insertDocUniqueIdTextBox.setText("");
                insertPtIdTextBox.setText(DEFAULT_PT_ID);
                insertPtFNameTextBox.setText(DEFAULT_PT_FNAME);
                insertPtLNameTextBox.setText(DEFAULT_PT_LNAME);
                updateDocTable();
            }

            public void onFailure(Throwable caught) {
                ErrorHandler.showError("Inserting doc failed. ", caught);
                insertDocUniqueIdTextBox.setText("");
                insertPtIdTextBox.setText(DEFAULT_PT_ID);
                insertPtFNameTextBox.setText(DEFAULT_PT_FNAME);
                insertPtLNameTextBox.setText(DEFAULT_PT_LNAME);
                updatePageTimestamp();
            }
        };

        boolean confirm = Window.confirm("About to insert a new document:"
            + "\n\tdocId : " + (docUniqueId.isEmpty() ? "<random>" : docUniqueId)
            + "\n\tptId  : " + ptId
            + "\n\tptName: " + ptFName + " " + ptLName
            + ".\n\nDo you wish to continue?");

        if (confirm) {
            getDocumentService().insertDoc(docUniqueId, ptId, ptFName, ptLName, insertCallback);
        }
        else {
            updatePageTimestamp();
        }
    }

    private void updatePageTimestamp() {
        lastUpdatedLabel.setText("Last updated: " + new Date()
                + ". Record count: " + (docFlexTable.getRowCount() - 1));
    }

    private void showWorking() {
        lastUpdatedLabel.setText("Working...");
    }

    private static DocumentServiceAsync getDocumentService() {
        // Create the client proxy. Note that although you are creating the
        // service interface proper, you cast the result to the asynchronous
        // version of the interface. The cast is always safe because the
        // generated proxy implements the asynchronous interface automatically.

        DocumentServiceAsync service = GWT.create(DocumentService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) service;
        endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL() + "documentservice");

        return service;
    }

}
