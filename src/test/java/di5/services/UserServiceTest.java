package di5.services;

import di5.data.dao.UserDao;
import di5.data.dto.CreateUserDTO;
import di5.data.dto.GetUserDTO;
import di5.data.enums.Gender;
import di5.data.model.User;
import di5.helpers.JwtUtil;
import di5.helpers.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private Logger logger;

    @InjectMocks
    private UserService userService;

    private User user;
    private CreateUserDTO createUserDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(new Timestamp(System.currentTimeMillis()));
        user.setGender(Gender.MALE);
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("1234567890");
        user.setHashPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setCreatedBy("tester");
        user.setUpdatedAt(user.getCreatedAt());
        user.setUpdatedBy("tester");
        user.setDeleted(false);

        createUserDTO = new CreateUserDTO();
        createUserDTO.setFirstName("John");
        createUserDTO.setLastName("Doe");
        createUserDTO.setBirthDate(new Timestamp(System.currentTimeMillis()));
        createUserDTO.setGender(Gender.MALE);
        createUserDTO.setEmail("john.doe@example.com");
        createUserDTO.setPhoneNumber("1234567890");
        createUserDTO.setPassword("password");
    }

    @Test
    void testCreateUser() throws SQLException {
        doNothing().when(userDao).createUser(any(User.class));

        userService.createUser(createUserDTO);

        verify(userDao, times(1)).createUser(any(User.class));
    }

    @Test
    void testGetAllUsers() throws SQLException {
        when(userDao.getAllUsers()).thenReturn(Arrays.asList(user));

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user.getId(), users.get(0).getId());

        verify(userDao, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() throws SQLException {
        when(userDao.getUserById("1")).thenReturn(user);

        GetUserDTO dto = userService.getUserById("1");
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());

        verify(userDao, times(1)).getUserById("1");
    }

    @Test
    void testGetUserToken() throws SQLException {
        try (MockedStatic<JwtUtil> mockedStatic = mockStatic(JwtUtil.class)) {
            when(userDao.getUserByEmail("john.doe@example.com")).thenReturn(user);
            mockedStatic.when(() -> JwtUtil.generateToken("john.doe@example.com")).thenReturn("mockToken");

            String token = userService.GetUserToken("john.doe@example.com", "password");

            assertNotNull(token);
            assertEquals("mockToken", token);

            verify(userDao, times(1)).getUserByEmail("john.doe@example.com");
            mockedStatic.verify(() -> JwtUtil.generateToken("john.doe@example.com"), times(1));
        }
    }

    @Test
    void testGetUserTokenInvalidPassword() throws SQLException {
        when(userDao.getUserByEmail("john.doe@example.com")).thenReturn(user);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.GetUserToken("john.doe@example.com", "wrongpassword");
        });

        assertEquals("UNAUTHORIZED", exception.getMessage());

        verify(userDao, times(1)).getUserByEmail("john.doe@example.com");
        try (MockedStatic<JwtUtil> mockedStatic = mockStatic(JwtUtil.class)) {
            mockedStatic.verify(() -> JwtUtil.generateToken(anyString()), never());
        }
    }
}
