package di5.services;

import di5.data.dao.UserPostSaveDao;
import di5.data.dto.GetPostDTO;
import di5.data.enums.PostType;
import di5.data.model.Post;
import di5.data.model.UserPostSave;
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
public class UserPostSaveServiceTest {

    @Mock
    private UserPostSaveDao userPostSaveDao;

    @InjectMocks
    private UserPostSaveService userPostSaveService;

    private UserPostSave save;
    private Post post;

    @BeforeEach
    void setUp() {
        save = new UserPostSave();
        save.setPostId("postId");
        save.setUserId("userId");
        save.setCreatedBy("userId");
        save.setUpdatedBy("userId");

        post = new Post();
        post.setId("postId");
        post.setTitle("Test Post");
        post.setSponsorId("sponsorId");
        post.setEventId("eventId");
        post.setDescription("Test Description");
        post.setPostType(PostType.Question);
        post.setLikeCount(0);
        post.setCommentCount(0);
    }

    @Test
    void testSavePost() throws SQLException {
        doNothing().when(userPostSaveDao).savePost(any(UserPostSave.class));

        userPostSaveService.savePost("postId", "userId");

        verify(userPostSaveDao, times(1)).savePost(any(UserPostSave.class));
    }

    @Test
    void testUnsavePost() throws SQLException {
        doNothing().when(userPostSaveDao).unsavePost("postId", "userId");

        userPostSaveService.unsavePost("postId", "userId");

        verify(userPostSaveDao, times(1)).unsavePost("postId", "userId");
    }

    @Test
    void testGetSavedPostsByUserId() throws SQLException {
        when(userPostSaveDao.getSavedPostsByUserId("userId")).thenReturn(Arrays.asList(post));

        List<GetPostDTO> posts = userPostSaveService.getSavedPostsByUserId("userId");

        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals(post.getId(), posts.get(0).getId());

        verify(userPostSaveDao, times(1)).getSavedPostsByUserId("userId");
    }
}
