/*
 * Copyright (c) 2008, Nationwide Health Information Network (NHIN) Connect. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the NHIN Connect Project nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

var sirona = {
    "localOnce":false,  // One time flag to execute a particular API locally
    "localAll":false,    // Global overwrite to test all APIs locally
    "version":"Version 0.93.1 (beta)",

// LOCAL
    "PS": "http://192.168.5.47:9763/PresentationServices/",
    "testLocal": "http://localhost:8080/UniversalPortal/data/",
    "views" : "http://localhost:8080/UniversalPortal/views/",
    "images": "http://localhost:8080/UniversalPortal/images/",
    "debugIt":true,     // Flag to show debug statements.
// WSO2
//    "PS": "http://192.168.5.47:9763/PresentationServices/",
//    "testLocal": "http://192.168.5.47:9763/portal/data/",
//    "views" : "http://192.168.5.47:9763/portal/views/",
//    "images": "http://192.168.5.47:9763/portal/images/",
//    "debugIt":true,     // Flag to show debug statements.
//QA
//    "PS": "http://192.168.5.62:9763/PresentationServices/",
//    "testLocal": "http://192.168.5.62:9763/portal/data/",
//    "views" : "http://192.168.5.62:9763/portal/views/",
//    "images": "http://192.168.5.62:9763/portal/images/",
//    "debugIt":false,     // Flag to show debug statements.
//
// LOCAL IP
//    "PS": "http://192.168.5.47:9763/PresentationServices/",
//    "testLocal": "http://192.168.1.4:8080/UniversalPortal/data/",
//    "views" : "http://192.168.1.4:8080/UniversalPortal/views/",
//    "images": "http://192.168.1.4:8080/UniversalPortal/images/",
//    "debugIt":true,     // Flag to show debug statements.

// DEV
//    "PS": "http://192.168.5.47:8080/PresentationServices/",
//    "testLocal": "http://192.168.5.47:8080/UniversalPortal/data/",
//    "views" : "http://192.168.5.47:8080/UniversalPortal/views/",
//    "images": "http://192.168.5.47:8080/UniversalPortal/images/"  ,
//    "debugIt":true,     // Flag to show debug statements.

// SHARED
//    "PS": "http://192.168.5.47:8080/PresentationServices/",
//    "testLocal": "http://192.168.5.155:8080/UniversalPortal/data/",
//    "views" : "http://192.168.5.155:8080/UniversalPortal/views/",
//    "images": "http://192.168.5.155:8080/UniversalPortal/images/",
//    "debugIt":true,     // Flag to show debug statements.
// LOCALHOST
//    "PS": "http://localhost:8080/PresentationServices/",
//    "testLocal": "http://localhost:8080/UniversalPortal/data/",
//    "views" : "http://localhost:8080/UniversalPortal/views/",
//    "images": "http://localhost:8080/UniversalPortal/images/",
//    "debugIt":true,     // Flag to show debug statements.

    "backupServers": [ "http://192.168.5.47:8080/UniversalPortal/data/", "http://nhinint01:8080/PresentationServices/" ],

    "ajaxError": "A critical error has occurred:",
    "ajaxViewError": "There was a problem loading the requested view.\nPlease contact the web administrator.",
    "confirmDelete":"Move the selection(s) to the Trash folder?",
    "confirmPermDelete":"Permanently remove the selection(s) from the Trash folder?",
    "confirmDiscard":"Are you sure you want to discard this message?",
    "confirmDraft":"Would you like to save this message as a draft?",
    "debugInfo":"",
    "securityData":{},
    "viewData":{},          // Stores data passed from widget to widget
    "ajaxMgr":{},           // Keeps track of the requests and responses, aborting duplicate requests and ones which belong to unloaded widgets
    "portal":"",            // Which portal is loaded
    "timers":[],            // Keeps track of timer handlers.  Only one timer per widget is allowed right now

    "inboxDefaultActions":{"label":"Inbox", "view":"inbox", "data":{"location":"INBOX"}, "actions":[{"label":"Delete"},{"label":"Archive"}] },
    "inboxFilters": [
        { "label":"All Messages", "data":{"location":"INBOX"} },
        { "label":"Mail", "data":{"location":"INBOX", "type":"Email"}  },
        { "label":"Alerts", "data":{"location":"INBOX", "type":"Alert"} },
        { "label":"Documents", "data":{"location":"INBOX", "type":"Document"} }
    ],
    "inboxEmailActions": [
        { "label":"Reply" },
        { "label":"Reply All" },
        { "label":"Forward" }
    ],
    "inboxSelections": [
        { "label":"All" },
        { "label":"None" },
        { "label":"Read" },
        { "label":"Unread" }
    ],
    "homeTemplate": {

    },
    "patientTemplate": {
        "leftNav": [
            { "label":"Messages", "children":[
                {"label":"Compose", "view":"inboxCompose", "data":{"location":"INBOX"} },
                {"label":"Inbox", "view":"inbox", "data":{"location":"INBOX"}, "actions":[{"label":"Delete"},{"label":"Archive"}] },
                {"label":"Sent", "view":"inbox", "data":{"location":"Sent"}, "actions":[{"label":"Delete"},{"label":"Archive"}] },
                {"label":"Drafts", "view":"inbox", "data":{"location":"Draft"}, "actions":[{"label":"Delete"},{"label":"Archive"}] },
                { "label":"Archive", "view":"inbox", "data":{"location":"Archived"}, "actions":[{"label":"Delete"},{"label":"Unarchive"}] },
                { "label":"Trash", "view":"inbox", "data":{"location":"UserTrash"}, "actions":[{"label":"Undelete"},{"label":"Archive"},{"label":"Delete Forever", "action":"delete"}] }
                ]
            },
            { "label":"Organizer", "children":[
                {"label":"Calendar", "view":"calendar"},
                {"label":"Contacts", "view":"addressBook" },
                {"label":"Personal Info", "view":"patientData", "domain":"demographics" }
                ]
            },
            { "label":"Medical Records", "children":[
                {"label":"Admissions", "view":"patientData", "domain":"admissions"},
                {"label":"Allergies", "view":"patientData", "domain":"allergies" },
                {"label":"Documentation", "view":"patientData", "domain":"documentation" },
                {"label":"Immunizations", "view":"patientData", "domain":"immunizations" },
                {"label":"Medications", "view":"patientData", "domain":"medications" },
                {"label":"Problems", "view":"patientData", "domain":"diagnoses" },
                {"label":"Results", "view":"patientData", "domain":"labs" },
                {"label":"Vital Signs", "view":"vitalSigns" }
                ]
            }
        ]
    },
    "providerTemplate": {
        "leftNav": [
            { "label":"Messages", "children":[
                {"label":"Compose", "view":"inboxCompose", "data":{"location":"INBOX"} },
                {"label":"Inbox", "view":"inbox", "data":{"location":"INBOX"}, "actions":[{"label":"Delete"},{"label":"Archive"}]  },
                {"label":"Sent", "view":"inbox", "cssClass":"providerContext", "data":{"location":"Sent"}, "actions":[{"label":"Delete"},{"label":"Archive"}] },
                {"label":"Drafts", "view":"inbox", "cssClass":"providerContext", "data":{"location":"Draft"}, "actions":[{"label":"Delete"},{"label":"Archive"}] },
                { "label":"Archive", "view":"inbox", "cssClass":"providerContext", "data":{"location":"Archived"}, "actions":[{"label":"Delete"},{"label":"Unarchive"}] },
                { "label":"Trash", "view":"inbox", "cssClass":"providerContext", "data":{"location":"UserTrash"}, "actions":[{"label":"Undelete"},{"label":"Archive"},{"label":"Delete Forever", "action":"delete"}] }
                ]
            },
            { "label":"Organizer", "cssClass":"providerContext", "children":[
                {"label":"Calendar", "cssClass":"providerContext", "view":"calendar"},
                {"label":"Contacts", "cssClass":"providerContext", "view":"addressBook" },
                {"label":"Personal Info", "cssClass":"providerContext", "view":"patientData", "domain":"demographics" }
                ]
            },
            { "label":"Medical Records", "cssClass":"providerPatientContext", "children":[
                {"label":"Admissions", "cssClass":"providerPatientContext", "view":"patientData", "domain":"admissions" },
                {"label":"Allergies", "cssClass":"providerPatientContext", "view":"patientData", "domain":"allergies"  },
                {"label":"Appointments", "cssClass":"providerPatientContext", "view":"calendar" },
                {"label":"Demographics", "cssClass":"providerPatientContext", "view":"patientData", "domain":"demographics"  },
                {"label":"Documentation", "cssClass":"providerPatientContext", "view":"patientData", "domain":"documentation"  },
                {"label":"Immunizations", "cssClass":"providerPatientContext", "view":"patientData", "domain":"immunizations" },
                {"label":"Medications", "cssClass":"providerPatientContext", "view":"patientData", "domain":"medications" },
                {"label":"Problems", "cssClass":"providerPatientContext", "view":"patientData", "domain":"diagnoses"  },
                {"label":"Results", "cssClass":"providerPatientContext", "view":"patientData", "domain":"labs" },
                {"label":"Vital Signs", "cssClass":"providerPatientContext", "view":"vitalSigns" }
                ]
            }
        ],
        "dxGuideStageSeverities":[ "N/A", "Low","Average","High","Very High" ],
        "predictiveSeverityColors":[ "#FFF", "#e8c811","#d7891c","#d5511e","#bf001e" ],
        "predictiveAnalysisPollingInterval":30000,
        "simulatorInProgressRefresh":[{label:'30 seconds', milliseconds:5000},{label:'1 minute', milliseconds:60000},{label:'5 minutes', milliseconds:300000},{label:'15 minutes', milliseconds:900000}]
    },
    "suiteFooterNav": [
        { "label":"Home/Login", "view":"login", "secondary": [
            { "label":"Log In", "view":"login" },
            { "label":"Account", "view":"newAccount" }
            ]
        },
        { "label":"About Sirona", "view":"about", "secondary": [
            { "label":"Patient Portal", "view":"aboutPatient" },
            { "label":"Provider Portal", "view":"aboutProvider" },
            { "label":"ResCap Simulator", "view":"aboutSimulator" }
            ]
        },
        { "label":"Medical News", "view":"medicalNews", "secondary": [
            { "label":"Wellness", "view":"wellness" },
            { "label":"Exercise & Fitness", "view":"exercise" },
            { "label":"Diet & Nutrition", "view":"diet" },
            { "label":"Your Family", "view":"yourFamily" },
            { "label":"Family", "view":"family" },
            { "label":"Veterans", "view":"veterans" }
            ]
        },
        { "label":"Support/Help", "view":"help", "secondary": [
            { "label":"Get Started", "view":"about" },
            { "label":"Preferences", "view":"preferences" },
            { "label":"Help", "view":"help" },
            { "label":"Feedback", "view":"feedback" }
            ]
        },
        { "label":"Legal/Security", "view":"legal", "secondary": [
            { "label":"Terms of Service", "view":"termsOfService" },
            { "label":"Privacy", "view":"privacyPolicy" },
            { "label":"Copyright Policy", "view":"copyrightPolicy" },
            { "label":"Security", "view":"security" }
            ]
        },
        { "label":"Contact Us", "view":"contactUs", "secondary": [
            { "label":"Contact Us", "view":"contactUs" }
            ]
        }
    ],
    // Main functions
    reqPS : function (domain, payload, jsonRoot, transType) {
        if (!payload.data) payload.data={};
        // Append the current userId and token which is required on all requests
        if (sirona.securityData.userId && sirona.securityData.token) {
            $.extend(payload.data, { userId:sirona.securityData.userId, token:sirona.securityData.token } );
        }

        // Pick the appropriate server to handle the calls
        var _server = sirona.PS;
        if (sirona.localOnce || sirona.localAll) {
            domain += '.json';  // Local files have a .json extension
            _server = sirona.testLocal;
        }
        sirona.localOnce=false;

        // Main Ajax function for all calls to Presentation Services (PS)
        if (sirona.ajaxMgr[domain]) { sirona.ajaxMgr[domain].abort(); }     // Abort duplicate API requests
        sirona.ajaxMgr[domain] = $.ajax({
            type: transType || 'GET',
            url: _server + domain,
            data: payload.data,
            dataType: 'json', mimeType:"application/json",
            cache:false,        // Appends a unique number to the URL
            timeout:30000,     // TODO change Ajax timeout

            // Global beforeSend wrapper with user defined function
            beforeSend: function () {
                // TODO look for the xhr for this domain and abort since a new request came in
//if (_server == sirona.testLocal && sirona.debugIt) $.publish( '/suite/debug', [ 'local:'+domain ] );
if ('getMessages.json'.indexOf(domain)>=0 && sirona.debugIt) $.publish( '/suite/debug', [ 'suiteUtil: ' + new Date() + ':' + this.url] );
//if ('setMessages.json'.indexOf(domain)>=0 && sirona.debugIt) $.publish( '/suite/debug', [ new Date() + ': ' + this.url + '?' + this.data ] );
                // Execute user defined method
                if (typeof payload.beforeSend === 'function'){
                    payload.beforeSend();
                }
            }
        })
        .done(function(data) {
            // Request is done and has returned something
            if (!data) { alert('No data returned for:' + this.url); return }
            if (jsonRoot && data[jsonRoot]) data=data[jsonRoot];  // Change the root of the JSON data

            // Make sure the return status can be found
            if (data.successStatus) {
                // Execute user defined function
                if (typeof payload.success === 'function') {
                    payload.success(data);
                }
            } else {
                // There is something wrong with the reply, so populate an error condition and return the error callback
                if (!data.statusMessage || data.statusMessage==null || data.statusMessage=='') data.statusMessage = 'An error occurred with this request, and the system did not return a description';
                else data.statusMessage = "An error occurred with the " + domain +" request.  System returned:\n" + data.statusMessage;
                if (typeof payload.error === 'function') {
                    payload.error(data);
                } else alert(data.statusMessage);
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            // Request failed
            if (textStatus == 'abort') return false;  // Aborted above, disregard this error
            if (typeof payload.error === 'function') {
                payload.error(null);
            } else {
                if (errorThrown.indexOf('timeout')<0) alert("An error occurred with the " + domain +" request.  System returned: " + errorThrown);
                else $.publish( '/suite/debug', [ 'config reqPS timeout:'+domain ] )
            }
        });
        // Keep track of the requests made by widget if the view is supplied in the payload
        if (payload.view) {
//            if (!sirona.ajaxMgr[payload.view]) sirona.ajaxMgr[payload.view] = {};
//            sirona.ajaxMgr[payload.view][domain] = sirona.ajaxMgr[domain];
            sirona.ajaxMgr[domain].viewName = payload.view;         // Store the view name in the jqXHR object
//            alert('config: '+ concatObject(sirona.ajaxMgr[payload.view][domain]));
        }
    },
    getView : function(viewName, dataObj) {
        // viewName is the name of the widget to load.
        // dataObj contains a payload of parameters including:
        //      container:  Optional. The JQuery element to contain the results.  data() parameter of the container holds the viewName for unsubscribing.
        //      data:       Optional data object to be stored in the widget's namespace.  Stored in sirona.viewData[viewName].
        //      [split]:    Optional boolean flag whether or not to split the suiteBody, looking for the top, splitter, and bottom
        if (!dataObj) dataObj={};
        if (!dataObj.container) dataObj.container = $('#suiteContent');  // Default container if none is specified

        // Unsubscribe all subscriptions created in this container
        var _currentView = dataObj.container.data('viewName');
        if (_currentView) {
            // The container element to be loaded holds the current viewName loaded.  Publish the unloadView for that widget first.
            $.publish( '/suite/unloadView',[ _currentView ] );      // Publish when the body will be replaced, signalling widgets to do something before
            // If the widget unloading has pending requests, then abort them
            $.each(sirona.ajaxMgr, function() { if (this.viewName==_currentView) this.abort() });
            $.unsubscribeView(_currentView);                        // Unsubscribe any subscriptions for this view
            abortTimer(_currentView);                               // Abort any timers active for this view
        }
        // Store the viewName in the container data().  It will be used to unsubscribe all subscriptions if this container is needed again.
        dataObj.container.data({ viewName:viewName });
        dataObj.viewName = viewName;

        // Each widget(view) has its own global area to pass data around
        sirona.viewData[viewName] = dataObj;         // Store the widget's "data" object that is passed around

        // Load the container passed in with the html from the viewName
        dataObj.container.html('&nbsp;');    // .empty(); - Causing container to collapse
        dataObj.container.load(sirona.views + viewName + '.html', function(response, status, xhr) {
            if (status=='error') {
                if (typeof dataObj.error === 'function') { dataObj.error() }
                else alert('getView: Error loading ' + viewName + " widget")
            } else {
                if (dataObj.split) {
                    // Look for the suiteSplitTop element to transform it into a splitter
                    // In order for the splitter to work, it MUST conform to a HTML structure containing:  suiteBody, suiteSplitTop, suiteSplitBottom
                    $('#suiteSplitTop').resizable({
                        minWidth:100, minHeight: 100, containment:$('#suiteBody'), handles: 's',
                        // Resize all elements if the splitter is resizing
                        resize:(function(e, ui) { resizeContent.updateHeight() }),
                        stop:(function() { resizeContent.updateAll() }) // Clean up display when resizing stops
                    });
                }
                resizeContent.init();
                $.publish('/suite/widgetLoaded',[dataObj]);

                // View was successfully loaded.  Call the success callback
                if (typeof dataObj.success === 'function') { dataObj.success() }
            }
        });
    }
};