package di5.services;

import di5.data.dao.FollowDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @Mock
    private FollowDao followDao;

    @InjectMocks
    private FollowService followService;

    @Test
    void testFollowUser() throws SQLException {
        doNothing().when(followDao).followUser("followerId", "followedId");

        followService.followUser("followerId", "followedId");

        verify(followDao, times(1)).followUser("followerId", "followedId");
    }

    @Test
    void testUnfollowUser() throws SQLException {
        doNothing().when(followDao).unfollowUser("followerId", "followedId");

        followService.unfollowUser("followerId", "followedId");

        verify(followDao, times(1)).unfollowUser("followerId", "followedId");
    }

    @Test
    void testGetFollowers() throws SQLException {
        List<String> followers = Arrays.asList("follower1", "follower2");
        when(followDao.getFollowers("userId")).thenReturn(followers);

        List<String> result = followService.getFollowers("userId");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("follower1"));
        assertTrue(result.contains("follower2"));

        verify(followDao, times(1)).getFollowers("userId");
    }

    @Test
    void testGetFollowing() throws SQLException {
        List<String> following = Arrays.asList("following1", "following2");
        when(followDao.getFollowing("userId")).thenReturn(following);

        List<String> result = followService.getFollowing("userId");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("following1"));
        assertTrue(result.contains("following2"));

        verify(followDao, times(1)).getFollowing("userId");
    }
}
