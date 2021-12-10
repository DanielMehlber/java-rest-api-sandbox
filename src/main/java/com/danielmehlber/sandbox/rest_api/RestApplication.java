package com.danielmehlber.sandbox.rest_api;

import com.danielmehlber.sandbox.db.DataBaseAccess;
import com.danielmehlber.sandbox.exceptions.DataBaseException;
import com.danielmehlber.sandbox.exceptions.InternalErrorException;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

@ApplicationPath("/api")
@Path("/")
public class RestApplication extends Application {

    public RestApplication() throws InternalErrorException, DataBaseException {
        DataBaseAccess.init();
    }

    @GET
    public Response alive() {
        return Response.status(Response.Status.OK).entity("I am alive").build();
    }
}
