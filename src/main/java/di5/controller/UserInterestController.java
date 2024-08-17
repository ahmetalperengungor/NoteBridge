package di5.controller;

import di5.data.dto.CreateUserInterestDTO;
import di5.data.dto.GetUserInterestDTO;
import di5.services.UserInterestService;
import di5.helpers.JwtUtil;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/userinterests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserInterestController {
    private UserInterestService userInterestService;

    public UserInterestController() throws Exception {
        userInterestService = new UserInterestService();
    }

    @POST
    public Response addInterest(CreateUserInterestDTO createUserInterestDTO, @Context HttpHeaders headers) {
        String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION).substring("Bearer ".length());
        String userId = JwtUtil.getUsernameFromToken(token);
        userInterestService.addInterest(createUserInterestDTO, userId);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/user")
    public Response getInterests(@Context HttpHeaders headers) {
        String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION).substring("Bearer ".length());
        String userId = JwtUtil.getUsernameFromToken(token);
        List<GetUserInterestDTO> userInterests = userInterestService.getInterestsByUserId(userId);
        return Response.ok(userInterests).build();
    }

    @GET
    @Path("/user/{username}")
    public Response getInterestsByUserId(@PathParam("username") String username, @Context HttpHeaders headers) {
        List<GetUserInterestDTO> userInterests = userInterestService.getInterestsByUserId(username);
        return Response.ok(userInterests).build();
    }

    @DELETE
    @Path("/{interest}")
    public Response deleteInterest(@PathParam("interest") String interest) {
        userInterestService.deleteInterest(interest);
        return Response.noContent().build();
    }
}
