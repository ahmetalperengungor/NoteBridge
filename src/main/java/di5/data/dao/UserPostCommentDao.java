package di5.data.dao;

import di5.data.helpers.jdbcHelper;
import di5.data.model.UserPostComment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserPostCommentDao {

    Connection connection;

    public UserPostCommentDao() throws ClassNotFoundException, SQLException {
        connection = jdbcHelper.getConnection();
    }

    public void createComment(UserPostComment comment) throws SQLException {
        String uuid = UUID.randomUUID().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO userpostcomment (id, createdAt, createdBy, updatedAt, updatedBy, isDeleted, deletedBy, deletedAt, postId, userId, comment) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        try {
            pst.setString(1, uuid);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, comment.getCreatedBy());
            pst.setTimestamp(4, timestamp);
            pst.setString(5, comment.getUpdatedBy());
            pst.setBoolean(6, false);
            pst.setString(7, null);
            pst.setTimestamp(8, null);
            pst.setString(9, comment.getPostId());
            pst.setString(10, comment.getUserId());
            pst.setString(11, comment.getComment());

            pst.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    public List<UserPostComment> getCommentsByPostId(String postId) throws SQLException {
        List<UserPostComment> comments = new ArrayList<>();
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM userpostcomment WHERE postid = ?");
        pst.setString(1, postId);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            UserPostComment comment = new UserPostComment();
            comment.setId(rs.getString("id"));
            comment.setCreatedAt(rs.getTimestamp("createdAt"));
            comment.setCreatedBy(rs.getString("createdBy"));
            comment.setUpdatedAt(rs.getTimestamp("updatedAt"));
            comment.setUpdatedBy(rs.getString("updatedBy"));
            comment.setDeleted(rs.getBoolean("isDeleted"));
            comment.setDeletedBy(rs.getString("deletedBy"));
            comment.setDeletedAt(rs.getTimestamp("deletedAt"));
            comment.setPostId(rs.getString("postId"));
            comment.setUserId(rs.getString("userId"));
            comment.setComment(rs.getString("comment"));
            comments.add(comment);
        }

        return comments;
    }

    public UserPostComment getCommentById(String id) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM userpostcomment WHERE id = ?");
        pst.setString(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            UserPostComment comment = new UserPostComment();
            comment.setId(rs.getString("id"));
            comment.setCreatedAt(rs.getTimestamp("createdAt"));
            comment.setCreatedBy(rs.getString("createdBy"));
            comment.setUpdatedAt(rs.getTimestamp("updatedAt"));
            comment.setUpdatedBy(rs.getString("updatedBy"));
            comment.setDeleted(rs.getBoolean("isDeleted"));
            comment.setDeletedBy(rs.getString("deletedBy"));
            comment.setDeletedAt(rs.getTimestamp("deletedAt"));
            comment.setPostId(rs.getString("postId"));
            comment.setUserId(rs.getString("userId"));
            comment.setComment(rs.getString("comment"));
            return comment;
        } else {
            return null;
        }
    }

    public List<UserPostComment> getCommentsByUserId(String userId) throws SQLException {
        List<UserPostComment> comments = new ArrayList<>();
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM userpostcomment WHERE userId = ?");
        pst.setString(1, userId);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            UserPostComment comment = new UserPostComment();
            comment.setId(rs.getString("id"));
            comment.setCreatedAt(rs.getTimestamp("createdAt"));
            comment.setCreatedBy(rs.getString("createdBy"));
            comment.setUpdatedAt(rs.getTimestamp("updatedAt"));
            comment.setUpdatedBy(rs.getString("updatedBy"));
            comment.setDeleted(rs.getBoolean("isDeleted"));
            comment.setDeletedBy(rs.getString("deletedBy"));
            comment.setDeletedAt(rs.getTimestamp("deletedAt"));
            comment.setPostId(rs.getString("postId"));
            comment.setUserId(rs.getString("userId"));
            comment.setComment(rs.getString("comment"));
            comments.add(comment);
        }

        return comments;
    }

    public void deleteComment(String id) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("DELETE FROM userpostcomment WHERE id = ?");
        pst.setString(1, id);
        pst.executeUpdate();
    }
}
