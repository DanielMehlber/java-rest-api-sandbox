package com.danielmehlber.sandbox.rest_api;

import com.danielmehlber.sandbox.db.DataBaseAccess;
import com.danielmehlber.sandbox.db.DataBaseConnection;
import com.danielmehlber.sandbox.entities.Person;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;
import com.danielmehlber.sandbox.exceptions.NoSuchPersonException;
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
            person = DataBaseAccess.getConnection().fetchById(id);
            personJSON = new ObjectMapper().writeValueAsString(person);
        } catch (InternalErrorException | DataBaseException | JsonProcessingException e) {
            // ISSUE: Server-fault
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (NoSuchPersonException e) {
            // ISSUE: Client-fault. invalid id
            return Response.status(Response.Status.NO_CONTENT).entity("No such person").build();
        }

        return Response.status(Response.Status.OK).entity(personJSON).build();
    }

    @GET
    @Path("/add")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response addPerson(final String json) {

        try {
            Person person = new ObjectMapper().readValue(json, Person.class);
            DataBaseAccess.getConnection().insertPerson(person);
        } catch (JsonProcessingException | InternalErrorException | DataBaseException e) {
            // ISSUE: Server-side
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

        return Response.status(Response.Status.OK).entity("person added to DB").build();
    }

}
