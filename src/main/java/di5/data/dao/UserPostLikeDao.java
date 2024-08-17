package di5.data.dao;

import di5.data.enums.PostType;
import di5.data.helpers.jdbcHelper;
import di5.data.model.UserPostLike;
import di5.data.model.Post;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserPostLikeDao {

    Connection connection;

    public UserPostLikeDao() throws ClassNotFoundException, SQLException {
        connection = jdbcHelper.getConnection();
    }

    public void likePost(UserPostLike like) throws SQLException {
        String uuid = UUID.randomUUID().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO userpostlike (id, createdAt, createdBy, updatedAt, updatedBy, isDeleted, deletedBy, deletedAt, postId, userId) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        try {
            pst.setString(1, uuid);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, like.getCreatedBy());
            pst.setTimestamp(4, timestamp);
            pst.setString(5, like.getUpdatedBy());
            pst.setBoolean(6, false);
            pst.setString(7, null);
            pst.setTimestamp(8, null);
            pst.setString(9, like.getPostId());
            pst.setString(10, like.getUserId());

            pst.executeUpdate();

        } catch (SQLException e) {

            throw e;
        }
    }

    public void unlikePost(String postId, String userId) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "DELETE FROM userpostlike WHERE postId = ? AND userId = ?"
        );

        try {
            pst.setString(1, postId);
            pst.setString(2, userId);
            pst.executeUpdate();

        } catch (SQLException e) {

            throw e;
        }
    }

    public List<Post> getLikedPostsByUserId(String userId) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "SELECT p.* FROM userpostlike l JOIN post p ON l.postId = p.id WHERE l.userId = ? AND l.isDeleted = false"
        );
        pst.setString(1, userId);
        ResultSet rs = pst.executeQuery();

        List<Post> likedPosts = new ArrayList<>();
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
            likedPosts.add(post);
        }
        return likedPosts;
    }
}
