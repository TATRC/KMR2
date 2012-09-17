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

package org.osaf.caldav4j.credential;

import java.net.URI;

public class CaldavCredential {
	static String CALDAV_YAHOO = "https://caldav.calendar.yahoo.com/dav/%s/Calendar/";
	static String CALDAV_GOOGLE = "https://www.google.com/calendar/dav/%s/";
        static String CALDAV_SOGO = "http://192.168.5.47/SOGo/dav/%s/Calendar/personal";


	// sample class for storing credentials 
    //public static final String CALDAV_SERVER_HOST = "10.0.8.205";
    public  String host = "192.168.5.47";
    public  int port = 443;
    public  String protocol = "https";
    public  String user = "sogo";
    public  String home = "/dav/" + user + "/";
    public  String password = "sogo1";
    public  String collection      = "collection/";

    public CaldavCredential() {
    	
    }
    
    public CaldavCredential(String uri) {
    	try {
			URI server = new URI(uri);
			protocol = server.getScheme().replaceAll("://", "");
			host = server.getHost();
			port = server.getPort() != -1 ? server.getPort() : (server.getScheme().endsWith("s") ? 443: 80);
			home = server.getPath().replace("\\w+/$", "");
		} catch (Exception e) {
			// noop
		}
    }
    public CaldavCredential(String proto, String server, int  port, String base, String collection, String user, String pass) {
    	this.host = server;
    	this.port = port;
    	this.protocol = proto;
    	this.home = base;
    	this.collection = collection;
    	this.user = user;
    	this.password = pass;
    }
    
}

