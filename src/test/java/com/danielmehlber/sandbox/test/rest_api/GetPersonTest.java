package com.danielmehlber.sandbox.test.rest_api;

import com.danielmehlber.sandbox.db.DataBaseAccess;
import com.danielmehlber.sandbox.entities.Person;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;
import com.danielmehlber.sandbox.logic.PersonLogic;
import com.danielmehlber.sandbox.rest_api.PersonAPI;
import com.danielmehlber.sandbox.rest_api.RestApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

public class GetPersonTest extends JerseyTest {

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
    public void testGet() throws InternalErrorException, DataBaseException, JsonProcessingException {
        // arrange
        Person person = new Person("Daniel", "Mehlber", 19, "I love programming");
        int id = PersonLogic.addPerson(person);

        // act
        Response response = target("/person/id/"+ id).request().get();

        // assert
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        String json = response.readEntity(String.class);
        Person result = new ObjectMapper().readValue(json, Person.class);

        Assert.assertTrue("both objects must be equal in content", result.equalsInContent(person));
    }

    @Test
    public void testNonExistent() throws InternalErrorException, DataBaseException {
        // arrange
        Person person = new Person("Daniel", "Mehlber", 19, "I love programming");
        int id = PersonLogic.addPerson(person);

        // act
        Response response = target("/person/id/"+ id * 2).request().get();

        // assert
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}
