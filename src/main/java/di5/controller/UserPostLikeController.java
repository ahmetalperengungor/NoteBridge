package di5.controller;

import di5.data.dao.PostDao;
import di5.data.dao.UserPostLikeDao;
import di5.data.dto.GetPostDTO;
import di5.data.dto.LikeDTO;
import di5.data.model.UserPostLike;
import di5.services.ImageService;
import di5.services.UserPostLikeService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.sql.SQLException;
import java.util.List;

@Path("/api/likes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserPostLikeController {
    private final UserPostLikeService userPostLikeService;
    private final UserPostLikeDao userPostLikeDao;
    private final PostDao postDao;
    private final ImageService imageService;

    public UserPostLikeController() throws ClassNotFoundException, SQLException {
        this.userPostLikeDao = new UserPostLikeDao();
        this.postDao = new PostDao();
        this.userPostLikeService = new UserPostLikeService();
        this.imageService = new ImageService();
    }

    @POST
    public Response likePost(LikeDTO likeDTO) {
        try {
            userPostLikeService.likePost(likeDTO.getPostId(), likeDTO.getUserId());
            return Response.status(Response.Status.CREATED).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error liking post").build();
        }
    }

    @DELETE
    @Path("/{postId}/{userId}")
    public Response unlikePost(@PathParam("postId") String postId, @PathParam("userId") String userId) {
        try {
            userPostLikeService.unlikePost(postId, userId);
            return Response.noContent().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error unliking post").build();
        }
    }

    @GET
    @Path("/{userId}")
    public Response getLikedPostsByUserId(@PathParam("userId") String userId, @Context UriInfo uriInfo) {
        try {
            List<GetPostDTO> likedPosts = userPostLikeService.getLikedPostsByUserId(userId);
            String baseUrl = uriInfo.getBaseUri().toString() + "images/";
            for (GetPostDTO getPostDTO : likedPosts) {
                List<String> images = imageService.getAllImages(getPostDTO.getId(), di5.data.enums.MediaType.POST, baseUrl);
                getPostDTO.setImageUrls(images);
            }
            return Response.ok(likedPosts).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving liked posts").build();
        }
    }
}
