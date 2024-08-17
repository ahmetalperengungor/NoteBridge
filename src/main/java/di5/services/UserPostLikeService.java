package di5.services;

import di5.data.dao.PostDao;
import di5.data.dao.UserPostLikeDao;
import di5.data.dto.GetPostDTO;
import di5.data.model.Post;
import di5.data.model.UserPostLike;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserPostLikeService {
    private final UserPostLikeDao userPostLikeDao;
    private final PostDao postDao;

    public UserPostLikeService() throws ClassNotFoundException, SQLException {
        this.userPostLikeDao = new UserPostLikeDao();
        this.postDao = new PostDao();
    }

    public void likePost(String postId, String userId) throws SQLException {
        UserPostLike like = new UserPostLike();
        like.setPostId(postId);
        like.setUserId(userId);
        like.setCreatedBy(userId);
        like.setUpdatedBy(userId);
        userPostLikeDao.likePost(like);

        postDao.incrementLikeCount(postId);
    }

    public void unlikePost(String postId, String userId) throws SQLException {
        userPostLikeDao.unlikePost(postId, userId);
    }

    public List<GetPostDTO> getLikedPostsByUserId(String userId) throws SQLException {
        List<Post> likedPosts = userPostLikeDao.getLikedPostsByUserId(userId);
        List<GetPostDTO> likedPostDTOs = new ArrayList<>();
        for (Post post : likedPosts) {
            GetPostDTO postDTO = convertToGetPostDTO(post);
            likedPostDTOs.add(postDTO);
        }
        return likedPostDTOs;
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
        dto.setIsLiked(true);
        return dto;
    }
}
