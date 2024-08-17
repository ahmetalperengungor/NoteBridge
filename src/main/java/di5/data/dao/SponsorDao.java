package di5.data.dao;

import di5.data.helpers.jdbcHelper;
import di5.data.model.Sponsor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SponsorDao {
    private Connection connection;

    public SponsorDao() throws ClassNotFoundException, SQLException {
        connection = jdbcHelper.getConnection();
    }

    public void createSponsor(Sponsor sponsor) throws SQLException {
        String query = "INSERT INTO sponsor (id, createdat, createdby, updatedat, updatedby, isdeleted, commercialname, price, agreementdate, agreementpostcount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, sponsor.getId());
            pst.setTimestamp(2, sponsor.getCreatedAt());
            pst.setString(3, sponsor.getCreatedBy());
            pst.setTimestamp(4, sponsor.getUpdatedAt());
            pst.setString(5, sponsor.getUpdatedBy());
            pst.setBoolean(6, sponsor.isDeleted());
            pst.setString(7, sponsor.getCommercialName());
            pst.setString(8, sponsor.getPrice());
            pst.setString(9, sponsor.getAgreementDate());
            pst.setString(10, sponsor.getAgreementPostCount());
            pst.executeUpdate();
        }
    }

    public Sponsor getSponsorById(String id) throws SQLException {
        String query = "SELECT * FROM sponsor WHERE id = ? AND isdeleted = false";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapRowToSponsor(rs);
            } else {
                return null;
            }
        }
    }

    public List<Sponsor> getAllSponsors() throws SQLException {
        String query = "SELECT * FROM sponsor WHERE isdeleted = false";
        List<Sponsor> sponsors = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                sponsors.add(mapRowToSponsor(rs));
            }
        }
        return sponsors;
    }

    public void deleteSponsor(String id) throws SQLException {
        String query = "UPDATE sponsor SET isdeleted = true, deletedby = ?, deletedat = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, "IMPLEMENT_THIS"); // Implement with the correct user ID
            pst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pst.setString(3, id);
            pst.executeUpdate();
        }
    }

    private Sponsor mapRowToSponsor(ResultSet rs) throws SQLException {
        Sponsor sponsor = new Sponsor();
        sponsor.setId(rs.getString("id"));
        sponsor.setCreatedAt(rs.getTimestamp("createdat"));
        sponsor.setCreatedBy(rs.getString("createdby"));
        sponsor.setUpdatedAt(rs.getTimestamp("updatedat"));
        sponsor.setUpdatedBy(rs.getString("updatedby"));
        sponsor.setDeleted(rs.getBoolean("isdeleted"));
        sponsor.setCommercialName(rs.getString("commercialname"));
        sponsor.setPrice(rs.getString("price"));
        sponsor.setAgreementDate(rs.getString("agreementdate"));
        sponsor.setAgreementPostCount(rs.getString("agreementpostcount"));
        return sponsor;
    }
}
