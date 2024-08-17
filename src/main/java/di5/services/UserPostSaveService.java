package di5.services;

import di5.data.dao.UserPostSaveDao;
import di5.data.dto.GetPostDTO;
import di5.data.model.Post;
import di5.data.model.UserPostSave;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserPostSaveService {
    private final UserPostSaveDao userPostSaveDao;

    public UserPostSaveService(UserPostSaveDao userPostSaveDao) throws ClassNotFoundException, SQLException {
        this.userPostSaveDao = userPostSaveDao;
    }

    public void savePost(String postId, String userId) throws SQLException {
        UserPostSave save = new UserPostSave();
        save.setPostId(postId);
        save.setUserId(userId);
        save.setCreatedBy(userId);
        save.setUpdatedBy(userId);
        userPostSaveDao.savePost(save);
    }

    public void unsavePost(String postId, String userId) throws SQLException {
        userPostSaveDao.unsavePost(postId, userId);
    }

    public List<GetPostDTO> getSavedPostsByUserId(String userId) throws SQLException {
        List<Post> savedPosts = userPostSaveDao.getSavedPostsByUserId(userId);
        List<GetPostDTO> savedPostDTOs = new ArrayList<>();
        for (Post post : savedPosts) {
            GetPostDTO postDTO = convertToGetPostDTO(post);
            savedPostDTOs.add(postDTO);
        }
        return savedPostDTOs;
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
}
