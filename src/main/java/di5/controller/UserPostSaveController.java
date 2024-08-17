package di5.controller;

import di5.data.dao.UserPostSaveDao;
import di5.data.dto.GetPostDTO;
import di5.data.dto.SaveDTO;
import di5.data.model.UserPostSave;
import di5.services.UserPostSaveService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/api/saves")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserPostSaveController {
    private final UserPostSaveService userPostSaveService;
    private final UserPostSaveDao userPostSaveDao;

    public UserPostSaveController() throws ClassNotFoundException, SQLException {
        this.userPostSaveDao = new UserPostSaveDao();
        this.userPostSaveService = new UserPostSaveService(this.userPostSaveDao);
    }

    @POST
    public Response savePost(SaveDTO saveDTO) {
        try {
            userPostSaveService.savePost(saveDTO.getPostId(), saveDTO.getUserId());
            return Response.status(Response.Status.CREATED).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("AERROR: save post controllerda sorun var yegen").build();
        }
    }

    @DELETE
    @Path("/{postId}/{userId}")
    public Response unsavePost(@PathParam("postId") String postId, @PathParam("userId") String userId) {
        try {
            userPostSaveService.unsavePost(postId, userId);
            return Response.noContent().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("AERROR: save post controller deleteda sorun var yegen").build();
        }
    }

    @GET
    @Path("/{userId}")
    public Response getSavedPostsByUserId(@PathParam("userId") String userId) {
        try {
            List<GetPostDTO> savedPosts = userPostSaveService.getSavedPostsByUserId(userId);
            return Response.ok(savedPosts).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving saved posts").build();
        }
    }
}
