package di5.services;

import di5.data.dao.SponsorDao;
import di5.data.dto.CreateSponsorDTO;
import di5.data.dto.GetSponsorDTO;
import di5.data.model.Sponsor;
import di5.helpers.Logger;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SponsorService {

    private SponsorDao sponsorDao;

    public SponsorService() throws Exception {
        this.sponsorDao = new SponsorDao();
    }

    public void createSponsor(CreateSponsorDTO createSponsorDTO) {
        Sponsor sponsor = new Sponsor();
        sponsor.setId(UUID.randomUUID().toString());
        sponsor.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        sponsor.setCreatedBy("IMPLEMENT_THIS"); // TODO: Implement this with actual user ID
        sponsor.setUpdatedAt(sponsor.getCreatedAt());
        sponsor.setUpdatedBy("IMPLEMENT_THIS"); // TODO: Implement this with actual user ID
        sponsor.setDeleted(false);
        sponsor.setCommercialName(createSponsorDTO.getCommercialName());
        sponsor.setPrice(createSponsorDTO.getPrice());
        sponsor.setAgreementDate(createSponsorDTO.getAgreementDate());
        sponsor.setAgreementPostCount(createSponsorDTO.getAgreementPostCount());

        try {
            sponsorDao.createSponsor(sponsor);
        } catch (SQLException e) {
            Logger.error("There is a problem while creating the sponsor");
        }
    }

    public GetSponsorDTO getSponsorById(String id) {
        try {
            Sponsor sponsor = sponsorDao.getSponsorById(id);
            if (sponsor == null) {
                return null;
            }
            return convertToGetSponsorDTO(sponsor);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching sponsor by ID", e);
        }
    }

    public List<GetSponsorDTO> getAllSponsors() {
        try {
            List<Sponsor> sponsors = sponsorDao.getAllSponsors();
            List<GetSponsorDTO> sponsorDTOs = new ArrayList<>();
            for (Sponsor sponsor : sponsors) {
                sponsorDTOs.add(convertToGetSponsorDTO(sponsor));
            }
            return sponsorDTOs;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all sponsors", e);
        }
    }

    public void deleteSponsor(String id) {
        try {
            sponsorDao.deleteSponsor(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting sponsor", e);
        }
    }

    private GetSponsorDTO convertToGetSponsorDTO(Sponsor sponsor) {
        GetSponsorDTO dto = new GetSponsorDTO();
        dto.setId(sponsor.getId());
        dto.setCommercialName(sponsor.getCommercialName());
        dto.setPrice(sponsor.getPrice());
        dto.setAgreementDate(sponsor.getAgreementDate());
        dto.setAgreementPostCount(sponsor.getAgreementPostCount());
        return dto;
    }
}
