package di5.data.dao;

import di5.data.enums.MediaType;
import di5.data.enums.PostType;
import di5.data.helpers.jdbcHelper;
import di5.data.model.Image;
import di5.data.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageDao {

    Connection connection;

    public ImageDao() {
        connection = jdbcHelper.getConnection();
    }

    public void createImage(Image image) throws SQLException {
        String uuid = UUID.randomUUID().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO image (id, createdAt, createdBy, updatedAt, updatedBy, isDeleted, deletedBy, deletedAt, url, mediaType, owner) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        try {
            pst.setString(1, uuid);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, image.getCreatedBy());
            pst.setTimestamp(4, timestamp);
            pst.setString(5, image.getUpdatedBy());
            pst.setBoolean(6, false);
            pst.setString(7, null);
            pst.setTimestamp(8, null);
            pst.setString(9, image.getUrl());
            pst.setInt(10, image.getMediaType().toInt());
            pst.setString(11, image.getOwner());

            pst.executeUpdate();

        } catch (SQLException e) {

            throw e;
        }
    }

    public Image getImage(String owner, MediaType mediaType) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM image WHERE owner = ? AND mediaType = ? ORDER BY createdAt DESC LIMIT 1");
        pst.setString(1, owner);
        pst.setInt(2, mediaType.toInt());
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            Image image = new Image();
            image.setId(rs.getString("id"));
            image.setCreatedAt(rs.getTimestamp("createdAt"));
            image.setCreatedBy(rs.getString("createdBy"));
            image.setUpdatedAt(rs.getTimestamp("updatedAt"));
            image.setUpdatedBy(rs.getString("updatedBy"));
            image.setDeleted(rs.getBoolean("isDeleted"));
            image.setDeletedBy(rs.getString("deletedBy"));
            image.setDeletedAt(rs.getTimestamp("deletedAt"));
            image.setUrl(rs.getString("url"));
            image.setMediaType(mediaType);
            image.setOwner(owner);
            return image;
        } else {
            return null;
        }
    }

    public List<Image> getMultipleImage(String owner, MediaType mediaType) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM image WHERE owner = ? AND mediaType = ?");
        pst.setString(1, owner);
        pst.setInt(2, mediaType.toInt());
        ResultSet rs = pst.executeQuery();
        List<Image> images = new ArrayList<>();

        while (rs.next()) {
            Image image = new Image();
            image.setId(rs.getString("id"));
            image.setCreatedAt(rs.getTimestamp("createdAt"));
            image.setCreatedBy(rs.getString("createdBy"));
            image.setUpdatedAt(rs.getTimestamp("updatedAt"));
            image.setUpdatedBy(rs.getString("updatedBy"));
            image.setDeleted(rs.getBoolean("isDeleted"));
            image.setDeletedBy(rs.getString("deletedBy"));
            image.setDeletedAt(rs.getTimestamp("deletedAt"));
            image.setUrl(rs.getString("url"));
            image.setMediaType(mediaType);
            image.setOwner(owner);
            images.add(image);
        }

        return images;
    }
}
