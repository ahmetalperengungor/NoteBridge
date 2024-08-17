package di5.data.dao;

import di5.data.enums.PostType;
import di5.data.helpers.jdbcHelper;
import di5.data.model.Post;
import di5.helpers.Logger;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostDao {

    Connection connection;

    public PostDao() throws ClassNotFoundException, SQLException {
        connection = jdbcHelper.getConnection();
    }

    public String createPost(Post post) throws SQLException {
        String uuid = UUID.randomUUID().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO post (id, createdAt, createdBy, updatedAt, updatedBy, isDeleted, deletedBy, deletedAt, title, sponsorId, eventId, description, postType, likeCount, commentCount) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        try {
            pst.setString(1, uuid);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, post.getCreatedBy());
            pst.setTimestamp(4, timestamp);
            pst.setString(5, post.getUpdatedBy());
            pst.setBoolean(6, false);
            pst.setString(7, null);
            pst.setTimestamp(8, null);
            pst.setString(9, post.getTitle());
            pst.setString(10, post.getSponsorId());
            pst.setString(11, post.getEventId());
            pst.setString(12, post.getDescription());
            pst.setInt(13, post.getPostType().toInt());
            pst.setInt(14, 0);
            pst.setInt(15, 0);

            pst.executeUpdate();

            return uuid;

        } catch (SQLException e) {

            throw e;
        }
    }

    public List<Post> getAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM post");

        while (rs.next()) {
            Logger.log("POST DATA START ");

            String id = rs.getString("id");
            Timestamp createdAt = rs.getTimestamp("createdAt");
            String createdBy = rs.getString("createdBy");
            Timestamp updatedAt = rs.getTimestamp("updatedAt");
            String updatedBy = rs.getString("updatedBy");
            boolean isDeleted = rs.getBoolean("isDeleted");
            Timestamp deletedAt = rs.getTimestamp("deletedAt");
            String deletedBy = rs.getString("deletedBy");
            String title = rs.getString("title");
            String sponsorId = rs.getString("sponsorId");
            String eventId = rs.getString("eventId");
            String description = rs.getString("description");
            int postType = rs.getInt("postType");
            int likeCount = rs.getInt("likeCount");
            int commentCount = rs.getInt("commentCount");

            Logger.log("POST ID: ");
            Post post = new Post();
            post.setId(id);
            post.setCreatedAt(createdAt);
            post.setCreatedBy(createdBy);
            post.setUpdatedAt(updatedAt);
            post.setUpdatedBy(updatedBy);
            post.setDeleted(isDeleted);
            post.setDeletedBy(deletedBy);
            post.setDeletedAt(deletedAt);
            post.setTitle(title);
            post.setSponsorId(sponsorId);
            post.setEventId(eventId);
            post.setDescription(description);
            post.setPostType(PostType.values()[rs.getInt("postType")]);
            post.setLikeCount(likeCount);
            post.setCommentCount(commentCount);
            posts.add(post);
        }

        return posts;
    }

    public Post getPostById(String id) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM post WHERE id = ?");
        pst.setString(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            Post post = new Post();
            post.setId(rs.getString("id"));
            post.setCreatedAt(rs.getTimestamp("createdAt"));
            post.setCreatedBy(rs.getString("createdBy"));
            post.setUpdatedAt(rs.getTimestamp("updatedAt"));
            post.setUpdatedBy(rs.getString("updatedBy"));
            post.setDeleted(rs.getBoolean("isDeleted"));
            post.setDeletedBy(rs.getString("deletedBy"));
            post.setDeletedAt(rs.getTimestamp("deletedAt"));
            post.setTitle(rs.getString("title"));
            post.setSponsorId(rs.getString("sponsorId"));
            post.setEventId(rs.getString("eventId"));
            post.setDescription(rs.getString("description"));
            post.setPostType(PostType.values()[rs.getInt("postType")]);
            post.setLikeCount(rs.getInt("likeCount"));
            post.setCommentCount(rs.getInt("commentCount"));
            return post;
        } else {
            return null;
        }
    }

    public void deletePostById(String id) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("DELETE FROM post WHERE id = ?");
        pst.setString(1, id);
        pst.executeUpdate();
    }

    public void incrementCommentCount(String postId) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "UPDATE post SET commentCount = commentCount + 1 WHERE id = ?"
        );
        pst.setString(1, postId);
        pst.executeUpdate();
    }

    public void decrementCommentCount(String postId) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "UPDATE post SET commentCount = commentCount - 1 WHERE id = ?"
        );
        pst.setString(1, postId);
        pst.executeUpdate();
    }

    public void incrementLikeCount(String postId) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "UPDATE post SET likeCount = likeCount + 1 WHERE id = ?"
        );
        pst.setString(1, postId);
        pst.executeUpdate();
    }

    public void decrementLikeCount(String postId) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "UPDATE post SET likeCount = likeCount - 1 WHERE id = ?"
        );
        pst.setString(1, postId);
        pst.executeUpdate();
    }

    public List<Post> getPostsByUserId(String userId) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "SELECT * FROM post WHERE createdBy = ? AND isDeleted = false"
        );
        pst.setString(1, userId);
        ResultSet rs = pst.executeQuery();

        List<Post> posts = new ArrayList<>();
        while (rs.next()) {
            Post post = new Post();
            post.setId(rs.getString("id"));
            post.setTitle(rs.getString("title"));
            post.setSponsorId(rs.getString("sponsorId"));
            post.setEventId(rs.getString("eventId"));
            post.setDescription(rs.getString("description"));
            post.setPostType(PostType.values()[rs.getInt("postType")]);
            post.setLikeCount(rs.getInt("likeCount"));
            post.setCommentCount(rs.getInt("commentCount"));
            post.setCreatedAt(rs.getTimestamp("createdAt"));
            post.setCreatedBy(rs.getString("createdBy"));
            post.setUpdatedAt(rs.getTimestamp("updatedAt"));
            post.setUpdatedBy(rs.getString("updatedBy"));
            post.setDeleted(rs.getBoolean("isDeleted"));
            posts.add(post);
        }
        return posts;
    }

    public List<Post> searchAndFilterPosts(String searchText, String postType) throws SQLException {
        List<Post> posts = new ArrayList<>();

        String query = "SELECT * FROM post WHERE (LOWER(title) LIKE ? OR LOWER(description) LIKE ?)";
        if (postType != null && !postType.isEmpty()) {
            query += " AND postType = ?";
        }

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, "%" + searchText.toLowerCase() + "%");
        pst.setString(2, "%" + searchText.toLowerCase() + "%");
        if (postType != null && !postType.isEmpty()) {
            int temp = Integer.parseInt(postType);
            pst.setInt(3, temp);
        }

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            Post post = new Post();
            post.setId(rs.getString("id"));
            post.setTitle(rs.getString("title"));
            post.setDescription(rs.getString("description"));
            post.setPostType(PostType.values()[rs.getInt("postType")]);
            post.setCreatedAt(rs.getTimestamp("createdAt"));
            post.setCreatedBy(rs.getString("createdBy"));
            post.setUpdatedAt(rs.getTimestamp("updatedAt"));
            post.setUpdatedBy(rs.getString("updatedBy"));
            post.setDeleted(rs.getBoolean("isDeleted"));
            post.setDeletedBy(rs.getString("deletedBy"));
            post.setDeletedAt(rs.getTimestamp("deletedAt"));

            posts.add(post);
        }

        return posts;
    }

    public void updatePost(Post post) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "UPDATE post SET title = ?, sponsorId = ?, eventId = ?, description = ?, postType = ?, updatedAt = ?, updatedBy = ? WHERE id = ?"
        );

        try {
            pst.setString(1, post.getTitle());
            pst.setString(2, post.getSponsorId());
            pst.setString(3, post.getEventId());
            pst.setString(4, post.getDescription());
            pst.setInt(5, post.getPostType().toInt());
            pst.setTimestamp(6, post.getUpdatedAt());
            pst.setString(7, post.getUpdatedBy());
            pst.setString(8, post.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

}
