package di5.controller;

import di5.data.dto.CreateUserDTO;
import di5.data.dto.CustomResponse;
import di5.data.dto.GetUserDTO;
import di5.data.dto.LoginUserDTO;
import di5.data.model.Image;
import di5.data.model.User;
import di5.helpers.FileWriter;
import di5.services.ImageService;
import di5.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private UserService userService;
    private ImageService imageService;

    public UserController() throws Exception {
        userService = new UserService();
        imageService = new ImageService();
    }

    @POST
    @Path("/signup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(CreateUserDTO createUserDTO) {
        userService.createUser(createUserDTO);

        return Response.ok().build();
    }

    @GET
    public List<GetUserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<GetUserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            GetUserDTO userDTO = new GetUserDTO();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setBirthDate(user.getBirthDate());
            userDTO.setGender(user.getGender());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setHashPassword(user.getHashPassword());
            userDTO.setDescription(user.getDescription());
            userDTOs.add(userDTO);
        }

        return userDTOs;
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") String id, @Context UriInfo uriInfo) throws SQLException {
        GetUserDTO userDTO = userService.getUserById(id);
        String baseUrl = uriInfo.getBaseUri().toString() + "images/";
        String profilePicture = imageService.getImage(userDTO.getEmail(), di5.data.enums.MediaType.USER, baseUrl);
        userDTO.setImageUrl(profilePicture);
        if (userDTO == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(userDTO).build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginByEmailAndPassword(LoginUserDTO loginUserDTO) {
        try {
            String token = userService.GetUserToken(loginUserDTO.getEmail(), loginUserDTO.getPassword());
            if (token == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            CustomResponse customResponse = new CustomResponse();
            customResponse.setData(token);
            customResponse.setMessage("Login successful");
            return Response.ok(customResponse).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") String id) {
        try {
            userService.deleteUser(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("AError eleman silmede sorun var yegen").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") String id, CreateUserDTO createUserDTO) {
        try {
            userService.updateUser(id, createUserDTO);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating user").build();
        }
    }

    @GET
    @Path("/me")
    public Response checkUserToken(@Context HttpServletRequest httpRequest, @Context UriInfo uriInfo)
            throws SQLException {
        // get username from request
        String username = httpRequest.getAttribute("username").toString();
        String baseUrl = uriInfo.getBaseUri().toString() + "images/";
        GetUserDTO getUserDto = userService.getUserByEmail(username);
        String profilePicture = imageService.getImage(getUserDto.getEmail(), di5.data.enums.MediaType.USER, baseUrl);
        getUserDto.setImageUrl(profilePicture);

        if (getUserDto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(getUserDto).build();
    }

    @POST
    @Path("/updateProfilePicture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadImage(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetails, @Context HttpServletRequest httpRequest) {

        String username = httpRequest.getAttribute("username").toString();
        String uuid = UUID.randomUUID().toString();

        // get the file extension
        String[] fileParts = fileDetails.getFileName().split("\\.");
        String fileExtension = fileParts[fileParts.length - 1];

        String finalName = uuid + "." + fileExtension;
        // Save the file to the specified directory

        imageService.saveImage(finalName, uploadedInputStream, username, di5.data.enums.MediaType.USER);

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/getUserByMail/{username}")
    public Response getUserByEmail(@PathParam("username") String username, @Context UriInfo uriInfo)
            throws SQLException {
        // get username from request
        GetUserDTO getUserDto = userService.getUserByEmail(username);
        String baseUrl = uriInfo.getBaseUri().toString() + "images/";
        String profilePicture = imageService.getImage(getUserDto.getEmail(), di5.data.enums.MediaType.USER, baseUrl);
        getUserDto.setImageUrl(profilePicture);
        if (getUserDto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(getUserDto).build();
    }

}
