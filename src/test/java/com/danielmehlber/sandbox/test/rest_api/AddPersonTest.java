package com.danielmehlber.sandbox.test.rest_api;

import com.danielmehlber.sandbox.db.DataBaseAccess;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;
import com.danielmehlber.sandbox.rest_api.PersonAPI;
import com.danielmehlber.sandbox.rest_api.RestApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import jdk.net.SocketFlow;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class AddPersonTest extends JerseyTest {

    /**
     * Required by Jersey Test Framework
     * @return Application Configuration
     */
    @Override
    protected Application configure() {
        // mount PersonAPI
        return new ResourceConfig(RestApplication.class, PersonAPI.class);
    }

    @Before
    public void prepare() throws InternalErrorException, DataBaseException {
        DataBaseAccess.getConnection().openTestConnection();
        DataBaseAccess.getConnection().prepareForTest();
    }

    @After
    public void cleanUp() throws InternalErrorException, DataBaseException {
        DataBaseAccess.getConnection().cleanupAfterTest();
        DataBaseAccess.getConnection().closeTestConnection();
    }

    @Test
    public void testAdd() throws JsonProcessingException {
        Response response = target("/person/add").request().post(Entity.entity("", MediaType.TEXT_PLAIN));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

}
