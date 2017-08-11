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

import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.impl.Table;
import org.apache.accumulo.core.client.impl.Tables;
import org.apache.accumulo.monitor.Monitor;
import org.apache.accumulo.server.AccumuloServerContext;
import org.easymock.EasyMock;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;

/**
 * Basic unit test for parameter validation constraints
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Monitor.class, Tables.class, WebViews.class})
public class WebViewsTest extends JerseyTest {

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        ResourceConfig config = new ResourceConfig(WebViews.class);
        config.register(MediaType.TEXT_HTML);
        return new ResourceConfig(WebViews.class);
    }

    @Override
    protected void configureClient(ClientConfig config) {
        super.configureClient(config);
        config.register()
    }

    /**
     * Expect to fail the constraint validation on the REST endpoint.  The constraint is the pre-defined word character
     * class Pattern so passing a table name with punctuation will cause a 400 response code.
     */
    @Test
    public void testGetTablesConstraintViolations() {
        Response output = target("tables/f+o*o").request().get();
        Assert.assertEquals("should return status 400", 400, output.getStatus());
    }

    /**
     * Here we expect the constraints to pass, but underlying logic will be executed.  We mock a certain amount of the
     * parts as an example of how one can trace through the code; however it's difficult to mock the jersey MVC code
     * to get the proper MessageBodyWriter for the returned Model object.  Here we expect a 500 response code which
     * at least illustrates we made it past the constraints on the endpoint.
     * 
     * @throws Exception not expected
     */
    @Test
    public void testGetTablesConstraintPassing() throws Exception {
        Instance instanceMock = EasyMock.createMock(Instance.class);
        expect(instanceMock.getInstanceID()).andReturn("foo").anyTimes();
        AccumuloServerContext contextMock = EasyMock.createMock(AccumuloServerContext.class);
        expect(contextMock.getInstance()).andReturn(instanceMock).anyTimes();
        
        mockStatic(Monitor.class);
        expect(Monitor.getContext()).andReturn(contextMock).anyTimes();
        
        mockStatic(Tables.class);
        expect(Tables.getTableName(instanceMock, new Table.ID("foo"))).andReturn("bar");
        replayAll();
        org.easymock.EasyMock.replay(instanceMock, contextMock);
        
        // Using the mocks we can verify that the getModel method gets called via debugger
        // however it's difficult to continue to mock through the jersey MVC code for the properly built response.
        // Expect a 500 response code for this.
        Response output = target("tables/foo").request().get();
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