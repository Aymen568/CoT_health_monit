package tn.cot.healthmonitoring.boundaries;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/test")
public class UserResource {

    @GET
    public Response getTestMessage() {
        return Response.ok("Hello, this is a test endpoint!").build();
    }
}