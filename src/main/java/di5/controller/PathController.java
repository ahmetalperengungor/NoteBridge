package di5.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.io.InputStream;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class PathController {

    @GET
    public Response index(@Context UriInfo uriInfo) {
        String baseUrl = uriInfo.getBaseUri().toString();
        InputStream pageStream = getClass().getResourceAsStream("/webapp/index.html");
        if (pageStream == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Page not found").build();
        }
        return Response.ok(pageStream).build();
    }

    @GET
    @Path("/login")
    public Response login() {
        InputStream pageStream = getClass().getResourceAsStream("/webapp/login_try.html");
        if (pageStream == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Page not found").build();
        }
        return Response.ok(pageStream).build();
    }


    @GET
    @Path("/profile/{username}")
    public Response otherprofile() {
        InputStream pageStream = getClass().getResourceAsStream("/webapp/profilepageother.html");
        if (pageStream == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Page not found").build();
        }
        return Response.ok(pageStream).build();
    }

    @GET
    @Path("/profile")
    public Response profile() {
        InputStream pageStream = getClass().getResourceAsStream("/webapp/profilepage.html");
        if (pageStream == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Page not found").build();
        }
        return Response.ok(pageStream).build();
    }

    @GET
    @Path("/myposts")
    public Response myposts() {
        InputStream pageStream = getClass().getResourceAsStream("/webapp/myposts.html");
        if (pageStream == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Page not found").build();
        }
        return Response.ok(pageStream).build();
    }

    @GET
    @Path("/myliked")
    public Response myliked() {
        InputStream pageStream = getClass().getResourceAsStream("/webapp/myliked.html");
        if (pageStream == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Page not found").build();
        }
        return Response.ok(pageStream).build();
    }
}
