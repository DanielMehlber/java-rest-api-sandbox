package com.danielmehlber.sandbox.rest_api;

import com.danielmehlber.sandbox.db.DataBaseAccess;
import com.danielmehlber.sandbox.db.DataBaseConnection;
import com.danielmehlber.sandbox.entities.Person;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;
import com.danielmehlber.sandbox.exceptions.NoSuchPersonException;
import com.danielmehlber.sandbox.logic.PersonLogic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/person")
public class PersonAPI {

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonById(@PathParam("id") final int id) {

        String personJSON;
        Person person;
        try {
            person = PersonLogic.getPersonById(id);
            personJSON = new ObjectMapper().writeValueAsString(person);
        } catch (InternalErrorException | DataBaseException | JsonProcessingException e) {
            // ISSUE: Server-fault
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (NoSuchPersonException e) {
            // ISSUE: Client-fault. invalid id
            return Response.status(Response.Status.NOT_FOUND).entity("No such person").build();
        }

        return Response.status(Response.Status.OK).entity(personJSON).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response addPerson(final String json) {

        int id = 0;

        try {
            Person person = new ObjectMapper().readValue(json, Person.class);
            id = PersonLogic.addPerson(person);
        } catch (InternalErrorException | DataBaseException | RuntimeException e) {
            // ISSUE: Server-side
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (JsonProcessingException e) {
            // ISSUE: Client fault: invalid JSON
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        return Response.status(Response.Status.OK).entity(String.valueOf(id)).build();
    }

}
