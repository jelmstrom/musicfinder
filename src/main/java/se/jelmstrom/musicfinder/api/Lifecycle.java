package se.jelmstrom.musicfinder.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
@Path("/")
public class Lifecycle {
    @GET
    @Path("health")
    public Response findNthLargestNumber() {
        return Response.ok().build();
    }

    @GET
    @Path("status")
    public Response status() {
        return Response.ok().build();
    }
}
