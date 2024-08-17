package di5.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/images")
public class ImageController {

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{imageName}")
    @Produces("image/png") // Adjust the media type according to your image type
    public Response getImage(@PathParam("imageName") String imageName) {
        final String UPLOAD_DIR = "/uploads/";
        File file = new File(UPLOAD_DIR + imageName);

        if (!file.exists() || !file.isFile()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        byte[] imageData;
        try {
            imageData = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(imageData).build();
    }
}
