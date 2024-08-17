package di5.services;

import di5.data.dao.ImageDao;
import di5.data.enums.MediaType;
import di5.data.model.Image;
import di5.helpers.FileWriter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageService {
    private final String UPLOAD_DIR = "/uploads/";

    private ImageDao imageDao;

    public ImageService() {
        imageDao = new ImageDao();
    }

    public boolean saveImage(String fileName, InputStream image, String username, MediaType mediaType) {
        try {

            // Ensure the directory exists
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File[] subdirectories = directory.listFiles(File::isDirectory);
            if (subdirectories != null) {
                for (File subdir : subdirectories) {
                    System.out.println("Directory name: " + subdir.getName());
                }
            }

            String uploadedFileLocation = UPLOAD_DIR + fileName;
            // save it
            FileWriter.writeToFile(image, uploadedFileLocation);

            Image imgObject = new Image();
            imgObject.setUrl(fileName);
            imgObject.setOwner(username);
            imgObject.setMediaType(mediaType);
            imgObject.setCreatedBy(username);
            imgObject.setUpdatedBy(username);

            imageDao.createImage(imgObject);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getImage(String owner, MediaType mediaType, String baseUrl) throws SQLException {
        try {
            Image image = imageDao.getImage(owner, mediaType);
            return baseUrl + image.getUrl();

        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getAllImages(String owner, MediaType mediaType, String baseUrl) throws SQLException {
        List<Image> images = imageDao.getMultipleImage(owner, mediaType);
        List<String> imageUrls = new ArrayList<>();
        for (Image image : images) {
            imageUrls.add(baseUrl + image.getUrl());
        }
        return imageUrls;
    }
}
