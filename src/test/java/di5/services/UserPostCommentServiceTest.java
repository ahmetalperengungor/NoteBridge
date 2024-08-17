package di5.services;

import di5.data.dao.UserPostCommentDao;
import di5.data.dao.PostDao;
import di5.data.dto.CreateCommentDTO;
import di5.data.dto.GetCommentDTO;
import di5.data.dto.GetPostDTO;
import di5.data.enums.PostType;
import di5.data.model.UserPostComment;
import di5.data.model.Post;
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
public class UserPostCommentServiceTest {

    @Mock
    private UserPostCommentDao userPostCommentDao;

    @Mock
    private PostDao postDao;

    @InjectMocks
    private UserPostCommentService userPostCommentService;

    private UserPostComment comment;
    private CreateCommentDTO createCommentDTO;
    private Post post;

    @BeforeEach
    void setUp() throws ClassNotFoundException, SQLException {
//        userPostCommentService = new UserPostCommentService(userPostCommentDao, postDao);

        comment = new UserPostComment();
        comment.setId("1");
        comment.setPostId("postId");
        comment.setUserId("userId");
        comment.setComment("This is a comment");

        createCommentDTO = new CreateCommentDTO();
        createCommentDTO.setPostId("postId");
        createCommentDTO.setUserId("userId");
        createCommentDTO.setComment("This is a comment");

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
    void testCreateComment() throws SQLException {
        doNothing().when(userPostCommentDao).createComment(any(UserPostComment.class));
        doNothing().when(postDao).incrementCommentCount("postId");

        userPostCommentService.createComment(createCommentDTO);

        verify(userPostCommentDao, times(1)).createComment(any(UserPostComment.class));
        verify(postDao, times(1)).incrementCommentCount("postId");
    }

    @Test
    void testDeleteComment() throws SQLException {
        when(userPostCommentDao.getCommentById("1")).thenReturn(comment);
        doNothing().when(userPostCommentDao).deleteComment("1");
        doNothing().when(postDao).decrementCommentCount("postId");

        userPostCommentService.deleteComment("1");

        verify(userPostCommentDao, times(1)).getCommentById("1");
        verify(userPostCommentDao, times(1)).deleteComment("1");
        verify(postDao, times(1)).decrementCommentCount("postId");
    }

    @Test
    void testGetCommentsByPostId() throws SQLException {
        when(userPostCommentDao.getCommentsByPostId("postId")).thenReturn(Arrays.asList(comment));

        List<GetCommentDTO> comments = userPostCommentService.getCommentsByPostId("postId");

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(comment.getComment(), comments.get(0).getComment());

        verify(userPostCommentDao, times(1)).getCommentsByPostId("postId");
    }

    @Test
    void testGetCommentedPostsByUserId() throws SQLException {
        when(userPostCommentDao.getCommentsByUserId("userId")).thenReturn(Arrays.asList(comment));
        when(postDao.getPostById("postId")).thenReturn(post);

        List<GetPostDTO> posts = userPostCommentService.getCommentedPostsByUserId("userId");

        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals(post.getId(), posts.get(0).getId());

        verify(userPostCommentDao, times(1)).getCommentsByUserId("userId");
        verify(postDao, times(1)).getPostById("postId");
    }
}
