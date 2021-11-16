package com.danielmehlber.sandbox.rest_api;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/person")
public class PersonAPI {

    @Path("/add")
    public Response addPerson() {

        return Response.ok().build();
    }

}
