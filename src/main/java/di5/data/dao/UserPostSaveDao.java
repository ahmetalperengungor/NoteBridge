package di5.data.dao;

import di5.data.enums.PostType;
import di5.data.helpers.jdbcHelper;
import di5.data.model.UserPostSave;
import di5.data.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserPostSaveDao {

    Connection connection;

    public UserPostSaveDao() throws ClassNotFoundException, SQLException {
        connection = jdbcHelper.getConnection();
    }

    public void savePost(UserPostSave save) throws SQLException {
        String uuid = UUID.randomUUID().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO userpostsave (id, createdAt, createdBy, updatedAt, updatedBy, isDeleted, deletedBy, deletedAt, postId, userId) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        try {
            pst.setString(1, uuid);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, save.getCreatedBy());
            pst.setTimestamp(4, timestamp);
            pst.setString(5, save.getUpdatedBy());
            pst.setBoolean(6, false);
            pst.setString(7, null);
            pst.setTimestamp(8, null);
            pst.setString(9, save.getPostId());
            pst.setString(10, save.getUserId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void unsavePost(String postId, String userId) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "DELETE FROM userpostsave WHERE postId = ? AND userId = ?"
        );

        try {
            pst.setString(1, postId);
            pst.setString(2, userId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<Post> getSavedPostsByUserId(String userId) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "SELECT p.* FROM userpostsave s JOIN post p ON s.postId = p.id WHERE s.userId = ? AND s.isDeleted = false"
        );
        pst.setString(1, userId);
        ResultSet rs = pst.executeQuery();

        List<Post> savedPosts = new ArrayList<>();
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
            post.setDeletedBy(rs.getString("deletedBy"));
            post.setDeletedAt(rs.getTimestamp("deletedAt"));
            savedPosts.add(post);
        }
        return savedPosts;
    }
}
