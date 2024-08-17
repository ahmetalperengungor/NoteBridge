package di5.services;

import di5.data.dao.UserDao;
import di5.data.dto.CreateUserDTO;
import di5.data.dto.GetUserDTO;
import di5.data.enums.Gender;
import di5.data.model.User;
import di5.helpers.JwtUtil;
import di5.helpers.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class UserService {

    private UserDao userDao;

    public UserService() throws Exception {
        this.userDao = new UserDao();
    }

    public void createUser(CreateUserDTO createUserDTO) {
        String password = createUserDTO.getPassword();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setBirthDate(createUserDTO.getBirthDate());
        user.setGender(createUserDTO.getGender());
        user.setEmail(createUserDTO.getEmail());
        user.setPhoneNumber(createUserDTO.getPhoneNumber());
        user.setHashPassword(hashedPassword);
        user.setCreatedBy("IMPLEMENT THIS"); // TODO: Implement by Token
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(user.getCreatedAt());
        user.setUpdatedBy("IMPLEMENT THIS"); // TODO: Implement by token
        user.setDeleted(false);
        user.setDescription(createUserDTO.getDescription());

        try {
            userDao.createUser(user);
        } catch (SQLException e) {
            Logger.error("There is a problem while creating the user");
        }
    }

    public List<User> getAllUsers() {
        try {
            List<User> users = userDao.getAllUsers();
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("AERROR: Hata var yegen service get all usersta", e);
        }
    }

    public GetUserDTO getUserById(String id) {
        try {
            User user = userDao.getUserById(id);
            if (user == null) {
                return null;
            }
            return convertToGetUserDTO(user);
        } catch (SQLException e) {
            throw new RuntimeException("AERROR: Hata var yegen service get specific userda", e);
        }
    }

    public GetUserDTO getUserByEmail(String email) {
        try {
            User user = userDao.getUserByEmail(email);
            if (user == null) {
                return null;
            }
            return convertToGetUserDTO(user);
        } catch (SQLException e) {
            throw new RuntimeException("AERROR: Hata var yegen service get specific userda", e);
        }
    }

    public String GetUserToken(String email, String password) {
        try {

            User user = userDao.getUserByEmail(email);
            if (user == null) {
                return null;
            }
            if (BCrypt.checkpw(password, user.getHashPassword())) {
                String token = JwtUtil.generateToken(email);
                return token;
            } else {
                throw new RuntimeException("UNAUTHORIZED");
            }

        } catch (SQLException e) {
            throw new RuntimeException("AERROR: Hata var yegen service get specific userda", e);
        }
    }

    private GetUserDTO convertToGetUserDTO(User user) {
        GetUserDTO dto = new GetUserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setBirthDate(user.getBirthDate());
        dto.setGender(user.getGender());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setHashPassword(user.getHashPassword());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setDescription(user.getDescription());
        return dto;
    }

    public void deleteUser(String id) throws SQLException {
        userDao.deleteUser(id);
    }

    public void updateUser(String id, CreateUserDTO createUserDTO) throws SQLException {
        User existingUser = userDao.getUserById(id);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        if (createUserDTO.getFirstName() != null) {
            existingUser.setFirstName(createUserDTO.getFirstName());
        }
        if (createUserDTO.getLastName() != null) {
            existingUser.setLastName(createUserDTO.getLastName());
        }
        if (createUserDTO.getBirthDate() != null) {
            existingUser.setBirthDate(createUserDTO.getBirthDate());
        }
        if (createUserDTO.getGender() != null) {
            existingUser.setGender(createUserDTO.getGender());
        }
        if (createUserDTO.getEmail() != null) {
            existingUser.setEmail(createUserDTO.getEmail());
        }
        if (createUserDTO.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(createUserDTO.getPhoneNumber());
        }
        if (createUserDTO.getPassword() != null) {
            String hashedPassword = BCrypt.hashpw(createUserDTO.getPassword(), BCrypt.gensalt());
            existingUser.setHashPassword(hashedPassword);
        }
        if (createUserDTO.getDescription()!=null){
            existingUser.setDescription(createUserDTO.getDescription());
        }

        existingUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        existingUser.setUpdatedBy("IMPLEMENT THIS");

        userDao.updateUser(existingUser);
    }


}
