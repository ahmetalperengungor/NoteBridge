package di5.services;

import di5.data.dao.UserInterestDao;
import di5.data.dto.CreateUserInterestDTO;
import di5.data.dto.GetUserInterestDTO;
import di5.data.model.UserInterest;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserInterestService {
    private UserInterestDao userInterestDao;

    public UserInterestService() throws Exception {
        this.userInterestDao = new UserInterestDao();
    }

    public void addInterest(CreateUserInterestDTO createUserInterestDTO, String userId) {
        UserInterest userInterest = new UserInterest();
        userInterest.setUserId(userId);
        userInterest.setInterest(createUserInterestDTO.getInterest());

        try {
            userInterestDao.addInterest(userInterest);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding user interest", e);
        }
    }

    public List<GetUserInterestDTO> getInterestsByUserId(String userId) {
        try {
            List<UserInterest> userInterests = userInterestDao.getInterestsByUserId(userId);
            return userInterests.stream()
                    .map(this::convertToGetUserInterestDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user interests", e);
        }
    }

    public void deleteInterest(String interest) {
        try {
            userInterestDao.deleteInterest(interest);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user interest", e);
        }
    }

    private GetUserInterestDTO convertToGetUserInterestDTO(UserInterest userInterest) {
        GetUserInterestDTO dto = new GetUserInterestDTO();
        dto.setUserId(userInterest.getUserId());
        dto.setInterest(userInterest.getInterest());
        return dto;
    }
}
