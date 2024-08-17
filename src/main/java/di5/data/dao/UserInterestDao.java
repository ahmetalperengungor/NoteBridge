package di5.data.dao;

import di5.data.helpers.jdbcHelper;
import di5.data.model.UserInterest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserInterestDao {
    Connection connection;

    public UserInterestDao() throws ClassNotFoundException, SQLException {
        connection = jdbcHelper.getConnection();
    }

    public void addInterest(UserInterest userInterest) throws SQLException {
        String uuid = UUID.randomUUID().toString();

        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO userinterest (userid, interest) VALUES (?, ?)"
        );

        try {

            pst.setString(1, userInterest.getUserId());
            pst.setString(2, userInterest.getInterest());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<UserInterest> getInterestsByUserId(String userId) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "SELECT * FROM userinterest WHERE userid = ?"
        );
        pst.setString(1, userId);
        ResultSet rs = pst.executeQuery();

        List<UserInterest> interests = new ArrayList<>();
        while (rs.next()) {
            UserInterest userInterest = new UserInterest();
            userInterest.setUserId(rs.getString("userid"));
            userInterest.setInterest(rs.getString("interest"));
            interests.add(userInterest);
        }

        return interests;
    }

    public void deleteInterest(String interest) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("DELETE FROM userinterest WHERE interest = ?");
        pst.setString(1, interest);
        pst.executeUpdate();
    }
}
