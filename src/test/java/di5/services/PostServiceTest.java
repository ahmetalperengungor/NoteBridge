package di5.services;

import di5.data.dao.PostDao;
import di5.data.dto.CreatePostDTO;
import di5.data.dto.GetPostDTO;
import di5.data.enums.PostType;
import di5.data.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostDao postDao;

    @InjectMocks
    private PostService postService;

    private Post post;
    private CreatePostDTO createPostDTO;

    @BeforeEach
    void setUp() throws Exception {
        post = new Post();
        post.setId("1");
        post.setTitle("Test Title");
        post.setSponsorId("sponsorId");
        post.setEventId("eventId");
        post.setDescription("Test Description");
        post.setPostType(PostType.Question);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCreatedBy("tester");
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setUpdatedAt(post.getCreatedAt());
        post.setUpdatedBy("tester");
        post.setDeleted(false);

        createPostDTO = new CreatePostDTO();
        createPostDTO.setTitle("Test Title");
        createPostDTO.setSponsorId("sponsorId");
        createPostDTO.setEventId("eventId");
        createPostDTO.setDescription("Test Description");
        createPostDTO.setPostType(PostType.Question);
        createPostDTO.setCreatedBy("tester");
    }

    @Test
    void testCreatePost() throws SQLException {
        doNothing().when(postDao).createPost(any(Post.class));

        postService.createPost(createPostDTO);

        verify(postDao, times(1)).createPost(any(Post.class));
    }

    @Test
    void testGetAllPosts() throws SQLException {
        when(postDao.getAllPosts()).thenReturn(Arrays.asList(post));

        List<Post> posts = postService.getAllPosts();

        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals(post.getId(), posts.get(0).getId());

        verify(postDao, times(1)).getAllPosts();
    }

    @Test
    void testGetPostById() throws SQLException {
        when(postDao.getPostById("1")).thenReturn(post);

        GetPostDTO dto = postService.getPostById("1");

        assertNotNull(dto);
        assertEquals(post.getId(), dto.getId());

        verify(postDao, times(1)).getPostById("1");
    }

    @Test
    void testDeletePostById() throws SQLException {
        doNothing().when(postDao).deletePostById("1");

        postService.deletePostById("1");

        verify(postDao, times(1)).deletePostById("1");
    }

    @Test
    void testGetPostsByUserId() throws SQLException {
        when(postDao.getPostsByUserId("userId")).thenReturn(Arrays.asList(post));

        List<GetPostDTO> dtos = postService.getPostsByUserId("userId");

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(post.getId(), dtos.get(0).getId());

        verify(postDao, times(1)).getPostsByUserId("userId");
    }
}