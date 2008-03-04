/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved. 
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License. 
 * 
 * You can obtain a copy of the License at:
 *     https://jersey.dev.java.net/license.txt
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each
 * file and include the License file at:
 *     https://jersey.dev.java.net/license.txt
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 *     "Portions Copyrighted [year] [name of copyright owner]"
 */

package com.sun.ws.rest.impl.resource;

import com.sun.ws.rest.impl.AbstractResourceTester;
import com.sun.ws.rest.api.core.HttpRequestContext;
import com.sun.ws.rest.api.core.HttpResponseContext;
import com.sun.ws.rest.api.client.WebResource;
import javax.ws.rs.Path;
import javax.ws.rs.ConsumeMime;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.core.Response;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class ConsumeProduceSimpleTest extends AbstractResourceTester {
    
    public ConsumeProduceSimpleTest(String testName) {
        super(testName);
    }
    
    @Path("/{arg1}/{arg2}")
    @ConsumeMime("text/html")
    public static class ConsumeSimpleBean {
        @POST
        public void doPostHtml(HttpRequestContext request, HttpResponseContext response) {
            assertEquals("POST", request.getHttpMethod());
            assertEquals("text/html", request.getRequestHeaders().getFirst("Content-Type"));
            response.setResponse(Response.ok("HTML").build());
        }
        
        @POST
        @ConsumeMime("text/xhtml")
        public void doPostXHtml(HttpRequestContext request, HttpResponseContext response) {
            assertEquals("POST", request.getHttpMethod());            
            assertEquals("text/xhtml", request.getRequestHeaders().getFirst("Content-Type"));
            response.setResponse(Response.ok("XHTML").build());
        }
    }
        
    @Path("/{arg1}/{arg2}")
    @ProduceMime("text/html")
    public static class ProduceSimpleBean {
        @GET
        public void doGetHtml(HttpRequestContext request, HttpResponseContext response) {
            assertEquals("GET", request.getHttpMethod());
            assertEquals("text/html", request.getRequestHeaders().getFirst("Accept"));
            response.setResponse(Response.ok("HTML").build());
        }
        
        @GET
        @ProduceMime("text/xhtml")
        public void doGetXhtml(HttpRequestContext request, HttpResponseContext response) {
            assertEquals("GET", request.getHttpMethod());            
            assertEquals("text/xhtml", request.getRequestHeaders().getFirst("Accept"));
            response.setResponse(Response.ok("XHTML").build());
        }
    }
    
    @Path("/{arg1}/{arg2}")
    @ConsumeMime("text/html")
    @ProduceMime("text/html")
    public static class ConsumeProduceSimpleBean {
        @GET
        public void doGetHtml(HttpRequestContext request, HttpResponseContext response) {
            assertEquals("GET", request.getHttpMethod());
            assertEquals("text/html", request.getRequestHeaders().getFirst("Accept"));
            response.setResponse(Response.ok("HTML").build());
        }
        
        @GET
        @ProduceMime("text/xhtml")
        public void doGetXhtml(HttpRequestContext request, HttpResponseContext response) {
            assertEquals("GET", request.getHttpMethod());            
            assertEquals("text/xhtml", request.getRequestHeaders().getFirst("Accept"));
            response.setResponse(Response.ok("XHTML").build());
        }
        
        @POST
        public void doPostHtml(HttpRequestContext request, HttpResponseContext response) {
            assertEquals("POST", request.getHttpMethod());
            assertEquals("text/html", request.getRequestHeaders().getFirst("Content-Type"));
            assertEquals("text/html", request.getRequestHeaders().getFirst("Accept"));
            response.setResponse(Response.ok("HTML").build());
        }
        
        @POST
        @ConsumeMime("text/xhtml")
        @ProduceMime("text/xhtml")
        public void doPostXHtml(HttpRequestContext request, HttpResponseContext response) {
            assertEquals("POST", request.getHttpMethod());            
            assertEquals("text/xhtml", request.getRequestHeaders().getFirst("Content-Type"));
            assertEquals("text/xhtml", request.getRequestHeaders().getFirst("Accept"));
            response.setResponse(Response.ok("XHTML").build());
        }
    }
    
    public void testConsumeSimpleBean() {
        initiateWebApplication(ConsumeSimpleBean.class);
        WebResource r = resource("/a/b");
        
        assertEquals("HTML", r.entity("", "text/html").post(String.class));
        assertEquals("XHTML", r.entity("", "text/xhtml").post(String.class));
    }
    
    public void testProduceSimpleBean() {
        initiateWebApplication(ProduceSimpleBean.class);
        WebResource r = resource("/a/b");

        assertEquals("HTML", r.accept("text/html").get(String.class));
        assertEquals("XHTML", r.accept("text/xhtml").get(String.class));
    }
    
    public void testConsumeProduceSimpleBean() {
        initiateWebApplication(ConsumeProduceSimpleBean.class);
        WebResource r = resource("/a/b");
        
        assertEquals("HTML", r.entity("", "text/html").accept("text/html").post(String.class));
        assertEquals("XHTML", r.entity("", "text/xhtml").accept("text/xhtml").post(String.class));
        assertEquals("HTML", r.accept("text/html").get(String.class));
        assertEquals("XHTML", r.accept("text/xhtml").get(String.class));
    }
}
