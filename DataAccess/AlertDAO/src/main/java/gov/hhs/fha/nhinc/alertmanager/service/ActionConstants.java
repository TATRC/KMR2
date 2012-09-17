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

package gov.hhs.fha.nhinc.alertmanager.service;

/**
 * Action names.  If you want to create a new alert:
 * 1) Add action here
 * 2) Add action to list of valid actions in AlertService (in this project)
 * 3) Update AlertUtil.isActionAllowed() to define the rules for when
 *    the action is allowed.
 * 4) To Test:
 *     a) add the action in the alert metadata
 *     b) perform action in inbox client
 *     c) verify that action occured on the alert
 *     d) verify that appropriate post-action effects are in place
 *
 * @author cmatser
 */
public interface ActionConstants {
    public static final String ACTION_ALERT = "Alert";
    public static final String ACTION_ACCEPT = "Accept";
    public static final String ACTION_ACKNOWLEDGE = "Acknowledge";
    public static final String ACTION_DISCARD = "Discard";
    public static final String ACTION_ESCALATE = "Escalate";
    public static final String ACTION_HOLD = "Hold";
    public static final String ACTION_MANUAL_ESCALATE = "Manual Escalate";
    public static final String ACTION_MODIFY = "Modify";
    public static final String ACTION_READ = "Read";
    public static final String ACTION_REJECT = "Reject";
}
