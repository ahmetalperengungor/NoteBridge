package di5.controller;

import di5.data.dao.PostDao;
import di5.data.dao.UserPostCommentDao;
import di5.data.dto.CreateCommentDTO;
import di5.data.dto.GetCommentDTO;
import di5.data.dto.GetPostDTO;
import di5.data.dto.GetUserDTO;
import di5.data.model.User;
import di5.services.UserPostCommentService;

import di5.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/api/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserPostCommentController {
    private final UserPostCommentService userPostCommentService;
    private final UserPostCommentDao userPostCommentDao;
    private  final PostDao postDao;
    private UserService userService;

    public UserPostCommentController() throws ClassNotFoundException, SQLException, Exception {
        this.userPostCommentDao = new UserPostCommentDao();
        this.postDao = new PostDao();
        this.userPostCommentService = new UserPostCommentService(this.userPostCommentDao, this.postDao);
        this.userService = new UserService();
    }

    @POST
    public Response createComment(CreateCommentDTO createCommentDTO) {
        try {
            userPostCommentService.createComment(createCommentDTO);
            return Response.status(Response.Status.CREATED).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("AERROR comment create controllerda sorun var yegen").build();
        }
    }

    @GET
    @Path("/post/{postId}")
    public Response getCommentsByPostId(@PathParam("postId") String postId) {
        try {
            List<GetCommentDTO> comments = userPostCommentService.getCommentsByPostId(postId);
            return Response.ok(comments).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("AERROR get comments by post controllerda sorun var yegen").build();
        }
    }

    @DELETE
    @Path("/post/{commentId}")
    public Response deleteComment(@PathParam("commentId") String commentId) {
        try {
            userPostCommentService.deleteComment(commentId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("AERROR delete comment controllerda sorun var yegen").build();
        }
    }

    @GET
    @Path("/user/{userId}")
    public Response getCommentedPostsByUserId(@PathParam("userId") String userId) {
        try {
            List<GetPostDTO> commentedPosts = userPostCommentService.getCommentedPostsByUserId(userId);
            return Response.ok(commentedPosts).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("AERROR get all comment controllerda sorun var yegen").build();
        }
    }


}
