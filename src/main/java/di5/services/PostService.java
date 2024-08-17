package di5.services;

import di5.data.dao.PostDao;
import di5.data.dto.CreatePostDTO;
import di5.data.dto.GetPostDTO;
import di5.data.dto.SearchDTO;
import di5.data.model.Post;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PostService {

    private PostDao postDao;

    public PostService() throws Exception{
        this.postDao = new PostDao();
    }

    public Post createPost(CreatePostDTO createPostDTO) {
        Post post = new Post();
        post.setTitle(createPostDTO.getTitle());
        post.setSponsorId(createPostDTO.getSponsorId());
        post.setEventId(createPostDTO.getEventId());
        post.setDescription(createPostDTO.getDescription());
        post.setPostType(createPostDTO.getPostType());
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCreatedBy(createPostDTO.getCreatedBy());
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setUpdatedAt(post.getCreatedAt());
        post.setUpdatedBy(createPostDTO.getCreatedBy());
        post.setDeleted(false);

        try {
            String addedPostId = postDao.createPost(post);
            post.setId(addedPostId);
            return post;
        } catch (SQLException e) {
            throw new RuntimeException("AERROR: Hata var yegen service create postta", e);
        }
    }

    public List<Post> getAllPosts() {
        try {
            List<Post> posts = postDao.getAllPosts();
            return posts;
        } catch (SQLException e) {
            throw new RuntimeException("AERROR: Hata var yegen service get all postta", e);
        }
    }

    public GetPostDTO getPostById(String id) {
        try {
            Post post = postDao.getPostById(id);
            if (post == null) {
                return null;
            }
            return convertToGetPostDTO(post);
        } catch (SQLException e) {
            throw new RuntimeException("AERROR: Hata var yegen service get specific postta", e);
        }
    }

    public void deletePostById(String id) {
        try {
            postDao.deletePostById(id);
        } catch (SQLException e) {
            throw new RuntimeException("AERROR: Hata var yegen service delete postta", e);
        }
    }

    public List<GetPostDTO> getPostsByUserId(String userId) {
        try {
            List<Post> posts = postDao.getPostsByUserId(userId);
            return posts.stream()
                    .map(this::convertToGetPostDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching posts by user ID", e);
        }
    }

    private GetPostDTO convertToGetPostDTO(Post post) {
        GetPostDTO dto = new GetPostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setSponsorId(post.getSponsorId());
        dto.setEventId(post.getEventId());
        dto.setDescription(post.getDescription());
        dto.setPostType(post.getPostType());
        dto.setLikeCount(post.getLikeCount());
        dto.setCommentCount(post.getCommentCount());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setCreatedBy(post.getCreatedBy());
        return dto;
    }

    public List<Post> searchAndFilterPosts(SearchDTO searchDTO) {
        try {
            return postDao.searchAndFilterPosts(searchDTO.getSearchText(), String.valueOf(searchDTO.getPostType()));
        } catch (SQLException e) {
            throw new RuntimeException("Error searching and filtering posts", e);
        }
    }

    public void updatePost(String id, CreatePostDTO createPostDTO) throws SQLException {
        Post existingPost = postDao.getPostById(id);
        if (existingPost == null) {
            throw new RuntimeException("Post not found");
        }

        if (createPostDTO.getTitle() != null) {
            existingPost.setTitle(createPostDTO.getTitle());
        }
        if (createPostDTO.getSponsorId() != null) {
            existingPost.setSponsorId(createPostDTO.getSponsorId());
        }
        if (createPostDTO.getEventId() != null) {
            existingPost.setEventId(createPostDTO.getEventId());
        }
        if (createPostDTO.getDescription() != null) {
            existingPost.setDescription(createPostDTO.getDescription());
        }
        if (createPostDTO.getPostType() != null) {
            existingPost.setPostType(createPostDTO.getPostType());
        }

        existingPost.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        existingPost.setUpdatedBy(createPostDTO.getCreatedBy());  // Ensure you set the correct user who updated the post

        postDao.updatePost(existingPost);
    }



}
