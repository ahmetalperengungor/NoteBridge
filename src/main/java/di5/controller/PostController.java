package di5.controller;

import di5.data.dto.*;
import di5.data.model.Image;
import di5.data.model.Post;
import di5.data.model.User;
import di5.services.ImageService;
import di5.services.PostService;

import di5.services.UserPostLikeService;
import di5.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/api/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostController {

    private PostService postService;
    private ImageService imageService;
    private UserPostLikeService userPostLikeService;
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    public PostController() throws Exception {
        postService = new PostService();
        imageService = new ImageService();
        userPostLikeService = new UserPostLikeService();
        userService = new UserService();
    }

    @POST
    public Response createPost(CreatePostDTO createPostDTO, @Context HttpServletRequest httpRequest) {
        String username = httpRequest.getAttribute("username").toString();
        createPostDTO.setCreatedBy(username);
        Post post = postService.createPost(createPostDTO);
        CustomResponse<Post> response = new CustomResponse<Post>();
        response.setMessage("Post created successfully");
        response.setData(post);

        return Response.ok(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosts(@Context UriInfo uriInfo, @Context HttpServletRequest httpRequest) throws SQLException {
        String username = httpRequest.getAttribute("username").toString();
        GetUserDTO user = userService.getUserByEmail(username);

        List<Post> posts = postService.getAllPosts();
        List<GetPostDTO> likedPosts = userPostLikeService.getLikedPostsByUserId(user.getId());

        List<GetPostDTO> postDTOs = new ArrayList<>();
        String baseUrl = uriInfo.getBaseUri().toString() + "images/";
        for (Post post : posts) {
            List<String> images = imageService.getAllImages(post.getId(), di5.data.enums.MediaType.POST, baseUrl);
            Boolean isLiked = false;
            for (GetPostDTO likedPost : likedPosts) {
                if (likedPost.getId().equals(post.getId())) {
                    isLiked = true;
                    break;
                }
            }

            GetPostDTO postDTO = new GetPostDTO();
            postDTO.setId(post.getId());
            postDTO.setTitle(post.getTitle());
            postDTO.setDescription(post.getDescription());
            postDTO.setSponsorId(post.getSponsorId());
            postDTO.setEventId(post.getEventId());
            postDTO.setPostType(post.getPostType());
            postDTO.setLikeCount(post.getLikeCount());
            postDTO.setCommentCount(post.getCommentCount());
            postDTO.setCreatedBy(post.getCreatedBy());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setIsLiked(isLiked);
            postDTO.setImageUrls(images);
            postDTOs.add(postDTO);
        }

        CustomResponse<List<GetPostDTO>> response = new CustomResponse<List<GetPostDTO>>();
        response.setData(postDTOs);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/{id}")
    public Response getPostById(@PathParam("id") String id, @Context UriInfo uriInfo) throws SQLException {
        GetPostDTO postDTO = postService.getPostById(id);
        String baseUrl = uriInfo.getBaseUri().toString() + "images/";
        List<String> images = imageService.getAllImages(postDTO.getId(), di5.data.enums.MediaType.POST, baseUrl);
        postDTO.setImageUrls(images);
        if (postDTO == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(postDTO).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePostById(@PathParam("id") String id) {
        try {
            postService.deletePostById(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("AERROR: Hata var yegen controller delete postta")
                    .build();
        }
    }

    @GET
    @Path("/user/{userId}")
    public Response getPostsByUserId(@PathParam("userId") String userId, @Context UriInfo uriInfo) throws SQLException {
        try {

            List<GetPostDTO> likedPosts = userPostLikeService.getLikedPostsByUserId(userId);
            List<GetPostDTO> posts = postService.getPostsByUserId(userId);

            String baseUrl = uriInfo.getBaseUri().toString() + "images/";
            for (GetPostDTO getPostDTO : posts) {
                List<String> images = imageService.getAllImages(getPostDTO.getId(), di5.data.enums.MediaType.POST,
                        baseUrl);
                getPostDTO.setImageUrls(images);

                Boolean isLiked = false;
                for (GetPostDTO likedPost : likedPosts) {
                    if (likedPost.getId().equals(getPostDTO.getId())) {
                        isLiked = true;
                        break;
                    }
                }
                getPostDTO.setIsLiked(isLiked);
            }
            return Response.ok(posts).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("AERROR: Hata var yegen controller get userin all postunda")
                    .build();
        }
    }

    @GET
    @Path("/search")
    public Response searchAndFilterPosts(SearchDTO searchDTO) {
        List<Post> posts = postService.searchAndFilterPosts(searchDTO);
        return Response.ok(posts).build();
    }

    @PUT
    @Path("/{id}")
    public Response updatePost(@PathParam("id") String id, CreatePostDTO createPostDTO,
            @Context HttpServletRequest httpRequest) {
        try {
            String username = httpRequest.getAttribute("username").toString();
            createPostDTO.setCreatedBy(username);
            postService.updatePost(id, createPostDTO);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating post").build();
        }
    }

    @GET
    @Path("/myposts")
    public Response getMyPosts(@Context HttpServletRequest httpRequest, @Context UriInfo uriInfo) throws SQLException {
        String username = httpRequest.getAttribute("username").toString();
        GetUserDTO user = userService.getUserByEmail(username);
        List<GetPostDTO> likedPosts = userPostLikeService.getLikedPostsByUserId(user.getId());
        List<GetPostDTO> posts = postService.getPostsByUserId(username);
        String baseUrl = uriInfo.getBaseUri().toString() + "images/";
        for (GetPostDTO getPostDTO : posts) {
            List<String> images = imageService.getAllImages(getPostDTO.getId(), di5.data.enums.MediaType.POST, baseUrl);
            getPostDTO.setImageUrls(images);

            Boolean isLiked = false;
            for (GetPostDTO likedPost : likedPosts) {
                if (likedPost.getId().equals(getPostDTO.getId())) {
                    isLiked = true;
                    break;
                }
            }

            getPostDTO.setIsLiked(isLiked);
        }
        return Response.ok(posts).build();
    }

    @POST
    @Path("/uploadPostPicture/{postId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadImage(
            @PathParam("postId") String postId,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetails, @Context HttpServletRequest httpRequest) {

        String username = httpRequest.getAttribute("username").toString();
        String uuid = UUID.randomUUID().toString();

        // get the file extension
        String[] fileParts = fileDetails.getFileName().split("\\.");
        String fileExtension = fileParts[fileParts.length - 1];

        String finalName = uuid + "." + fileExtension;
        // Save the file to the specified directory

        imageService.saveImage(finalName, uploadedInputStream, postId, di5.data.enums.MediaType.POST);

        return Response.status(Response.Status.OK).build();
    }

}
