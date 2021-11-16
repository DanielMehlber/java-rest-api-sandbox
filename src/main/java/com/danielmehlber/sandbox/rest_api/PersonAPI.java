package com.danielmehlber.sandbox.rest_api;

import com.danielmehlber.sandbox.entities.Person;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/person")
public class PersonAPI {

    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(final Person person) {

        return Response.ok().build();
    }

}
