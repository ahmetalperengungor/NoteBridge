package di5.data.dao;

import di5.data.helpers.jdbcHelper;
import di5.data.model.Follow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FollowDao {
    private final Connection connection;

    public FollowDao() throws ClassNotFoundException, SQLException {
        connection = jdbcHelper.getConnection();
    }

    public void followUser(String followerId, String followedId) throws SQLException {
        String uuid = UUID.randomUUID().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO follow (id, createdAt, createdBy, updatedAt, updatedBy, isDeleted, deletedBy, deletedAt, followerId, followedId) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        try {
            pst.setString(1, uuid);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, followerId);
            pst.setTimestamp(4, timestamp);
            pst.setString(5, followerId);
            pst.setBoolean(6, false);
            pst.setString(7, null);
            pst.setTimestamp(8, null);
            pst.setString(9, followerId);
            pst.setString(10, followedId);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void unfollowUser(String followerId, String followedId) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "DELETE FROM follow WHERE followerId = ? AND followedId = ?"
        );

        try {
            pst.setString(1, followerId);
            pst.setString(2, followedId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<String> getFollowers(String userId) throws SQLException {
        List<String> followers = new ArrayList<>();
        PreparedStatement pst = connection.prepareStatement(
                "SELECT followerId FROM follow WHERE followedId = ? AND isDeleted = false"
        );
        pst.setString(1, userId);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            followers.add(rs.getString("followerId"));
        }

        return followers;
    }

    public List<String> getFollowing(String userId) throws SQLException {
        List<String> following = new ArrayList<>();
        PreparedStatement pst = connection.prepareStatement(
                "SELECT followedId FROM follow WHERE followerId = ? AND isDeleted = false"
        );
        pst.setString(1, userId);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            following.add(rs.getString("followedId"));
        }

        return following;
    }
}
