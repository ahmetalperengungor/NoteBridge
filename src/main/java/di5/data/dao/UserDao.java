package di5.data.dao;

import di5.data.enums.Gender;
import di5.data.helpers.jdbcHelper;
import di5.data.model.User;
import di5.helpers.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao {

    private Connection connection;

    public UserDao() throws ClassNotFoundException, SQLException {
        connection = jdbcHelper.getConnection();
    }

    public void createUser(User user) throws SQLException {
        String uuid = UUID.randomUUID().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO appuser (id, createdAt, createdBy, updatedAt, updatedBy, isDeleted, deletedBy, deletedAt, firstName, lastName, birthDate, gender, email, phoneNumber, hashPassword, description, city, country) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        try {
            pst.setString(1, uuid);
            pst.setTimestamp(2, timestamp);
            pst.setString(3, user.getCreatedBy());
            pst.setTimestamp(4, timestamp);
            pst.setString(5, user.getUpdatedBy());
            pst.setBoolean(6, false);
            pst.setString(7, null);
            pst.setTimestamp(8, null);
            pst.setString(9, user.getFirstName());
            pst.setString(10, user.getLastName());
            pst.setDate(11, new java.sql.Date(user.getBirthDate().getTime()));
            pst.setInt(12, user.getGender().toInt());
            pst.setString(13, user.getEmail());
            pst.setString(14, user.getPhoneNumber());
            pst.setString(15, user.getHashPassword());
            pst.setString(16, user.getDescription());
            pst.setString(17, user.getCity());
            pst.setString(18, user.getCountry());

            pst.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM appuser");

        while (rs.next()) {
            Logger.log("USER DATA START");

            User user = new User();
            user.setId(rs.getString("id"));
            user.setCreatedAt(rs.getTimestamp("createdAt"));
            user.setCreatedBy(rs.getString("createdBy"));
            user.setUpdatedAt(rs.getTimestamp("updatedAt"));
            user.setUpdatedBy(rs.getString("updatedBy"));
            user.setDeleted(rs.getBoolean("isDeleted"));
            user.setDeletedBy(rs.getString("deletedBy"));
            user.setDeletedAt(rs.getTimestamp("deletedAt"));
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setBirthDate(rs.getDate("birthDate"));
            user.setGender(Gender.values()[rs.getInt("gender")]);
            user.setEmail(rs.getString("email"));
            user.setPhoneNumber(rs.getString("phoneNumber"));
            user.setHashPassword(rs.getString("hashPassword"));
            user.setDescription(rs.getString("description"));
            user.setCity(rs.getString("city"));
            user.setCountry(rs.getString("country"));

            users.add(user);
        }

        return users;
    }

    public User getUserById(String id) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM appuser WHERE id = ?");
        pst.setString(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setCreatedAt(rs.getTimestamp("createdAt"));
            user.setCreatedBy(rs.getString("createdBy"));
            user.setUpdatedAt(rs.getTimestamp("updatedAt"));
            user.setUpdatedBy(rs.getString("updatedBy"));
            user.setDeleted(rs.getBoolean("isDeleted"));
            user.setDeletedBy(rs.getString("deletedBy"));
            user.setDeletedAt(rs.getTimestamp("deletedAt"));
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setBirthDate(rs.getDate("birthDate"));
            user.setGender(Gender.values()[rs.getInt("gender")]);
            user.setEmail(rs.getString("email"));
            user.setPhoneNumber(rs.getString("phoneNumber"));
            user.setHashPassword(rs.getString("hashPassword"));
            user.setDescription(rs.getString("description"));
            user.setCity(rs.getString("city"));
            user.setCountry(rs.getString("country"));
            return user;
        } else {
            return null;
        }
    }

    public User getUserByEmail(String email) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM appuser WHERE email = ?");
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setCreatedAt(rs.getTimestamp("createdAt"));
            user.setCreatedBy(rs.getString("createdBy"));
            user.setUpdatedAt(rs.getTimestamp("updatedAt"));
            user.setUpdatedBy(rs.getString("updatedBy"));
            user.setDeleted(rs.getBoolean("isDeleted"));
            user.setDeletedBy(rs.getString("deletedBy"));
            user.setDeletedAt(rs.getTimestamp("deletedAt"));
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setBirthDate(rs.getDate("birthDate"));
            user.setGender(Gender.values()[rs.getInt("gender")]);
            user.setEmail(rs.getString("email"));
            user.setPhoneNumber(rs.getString("phoneNumber"));
            user.setHashPassword(rs.getString("hashPassword"));
            user.setDescription(rs.getString("description"));
            user.setCity(rs.getString("city"));
            user.setCountry(rs.getString("country"));
            return user;
        } else {
            return null;
        }
    }
    public void deleteUser(String id) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("DELETE FROM appuser WHERE id = ?");
        pst.setString(1, id);
        pst.executeUpdate();
    }

    public void updateUser(User user) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(
                "UPDATE appuser SET firstName = ?, lastName = ?, birthDate = ?, gender = ?, email = ?, phoneNumber = ?, hashPassword = ?, updatedAt = ?, updatedBy = ?, description = ?, city = ?, country = ? WHERE id = ?"
        );

        try {
            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setDate(3, new java.sql.Date(user.getBirthDate().getTime()));
            pst.setInt(4, user.getGender().toInt());
            pst.setString(5, user.getEmail());
            pst.setString(6, user.getPhoneNumber());
            pst.setString(7, user.getHashPassword());
            pst.setTimestamp(8, user.getUpdatedAt());
            pst.setString(9, user.getUpdatedBy());
            pst.setString(10, user.getDescription());
            pst.setString(11, user.getCity());
            pst.setString(12, user.getCountry());
            pst.setString(13, user.getId());



            pst.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
}
