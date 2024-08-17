package di5.services;

import di5.data.dao.PostDao;
import di5.data.dao.UserPostLikeDao;
import di5.data.dto.GetPostDTO;
import di5.data.enums.PostType;
import di5.data.model.Post;
import di5.data.model.UserPostLike;
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
public class UserPostLikeServiceTest {

    @Mock
    private UserPostLikeDao userPostLikeDao;

    @Mock
    private PostDao postDao;

    @InjectMocks
    private UserPostLikeService userPostLikeService;

    private UserPostLike like;
    private Post post;

    @BeforeEach
    void setUp() throws ClassNotFoundException, SQLException {
        like = new UserPostLike();
        like.setPostId("postId");
        like.setUserId("userId");
        like.setCreatedBy("userId");
        like.setUpdatedBy("userId");

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
    void testLikePost() throws SQLException {
        doNothing().when(userPostLikeDao).likePost(any(UserPostLike.class));
        doNothing().when(postDao).incrementLikeCount("postId");

        userPostLikeService.likePost("postId", "userId");

        verify(userPostLikeDao, times(1)).likePost(any(UserPostLike.class));
        verify(postDao, times(1)).incrementLikeCount("postId");
    }

    @Test
    void testUnlikePost() throws SQLException {
        doNothing().when(userPostLikeDao).unlikePost("postId", "userId");

        userPostLikeService.unlikePost("postId", "userId");

        verify(userPostLikeDao, times(1)).unlikePost("postId", "userId");
    }

    @Test
    void testGetLikedPostsByUserId() throws SQLException {
        when(userPostLikeDao.getLikedPostsByUserId("userId")).thenReturn(Arrays.asList(post));

        List<GetPostDTO> posts = userPostLikeService.getLikedPostsByUserId("userId");

        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals(post.getId(), posts.get(0).getId());

        verify(userPostLikeDao, times(1)).getLikedPostsByUserId("userId");
    }
}
