package di5.controller;

import di5.data.dao.FollowDao;
import di5.data.dto.FollowDTO;
import di5.services.FollowService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/api/follows")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FollowController {

    private final FollowDao followDao;
    private final FollowService followService;

    public FollowController() throws ClassNotFoundException, SQLException {
        this.followDao = new FollowDao();
        this.followService = new FollowService(followDao);
    }

    @POST
    public Response followUser(FollowDTO followDTO) {
        try {
            followService.followUser(followDTO.getFollowerId(), followDTO.getFollowedId());
            return Response.status(Response.Status.CREATED).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("AERROR: follow user controllerda sorun var yegen").build();
        }
    }

    @DELETE
    @Path("/{followerId}/{followedId}")
    public Response unfollowUser(@PathParam("followerId") String followerId, @PathParam("followedId") String followedId) {
        try {
            followService.unfollowUser(followerId, followedId);
            return Response.noContent().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("AERROR: unfollow user controllerda sorun var yegen").build();
        }
    }

    @GET
    @Path("/followers/{userId}")
    public Response getFollowers(@PathParam("userId") String userId) {
        try {
            List<String> followers = followService.getFollowers(userId);
            return Response.ok(followers).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("AERROR: get follow user controllerda sorun var yegen").build();
        }
    }

    @GET
    @Path("/following/{userId}")
    public Response getFollowing(@PathParam("userId") String userId) {
        try {
            List<String> following = followService.getFollowing(userId);
            return Response.ok(following).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("AERROR: get following user controllerda sorun var yegen").build();
        }
    }
}
