/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.accumulo.monitor.view;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

/**
 * Basic unit test for parameter validation constraints
 */
public class WebViewsTest extends JerseyTest {

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(WebViews.class);
    }
    
    @Test
    public void getTables() throws Exception {
        // This should not pass the constraint pattern
        Response output = target("tables/f+oo").request().get();
        Assert.assertEquals("should return status 400", 400, output.getStatus());

        // This will pass the constraint pattern, but since we are not mocking, we expect 500 reponse.
        output = target("tables/foo").request().get();
        Assert.assertEquals("should return status 500", 500, output.getStatus());
    }

    /**
     * Test minutes parameter constraints.  When outside range we should get a 400 response.
     */
    @Test
    public void testGetTracesSummaryValidationConstraint() {
        // Test upper bounds of constraint
        Response output = target("trace/summary").queryParam("minutes", 5000000).request().get();
        Assert.assertEquals("should return status 400", 400, output.getStatus());

        // Test lower bounds of constraint
        output = target("trace/summary").queryParam("minutes", -27).request().get();
        Assert.assertEquals("should return status 400", 400, output.getStatus());
    }
}