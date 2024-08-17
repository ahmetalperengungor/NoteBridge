package di5.services;

import di5.data.dao.UserDao;
import di5.data.dao.UserPostCommentDao;
import di5.data.dao.PostDao;  // Add this import
import di5.data.dto.CreateCommentDTO;
import di5.data.dto.GetCommentDTO;
import di5.data.dto.GetPostDTO;
import di5.data.model.User;
import di5.data.model.UserPostComment;
import di5.data.model.Post;  // Add this import

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserPostCommentService {
    private final UserPostCommentDao userPostCommentDao;
    private final PostDao postDao;
    private final UserDao userDao;

    public UserPostCommentService(UserPostCommentDao userPostCommentDao, PostDao postDao) throws ClassNotFoundException, SQLException {
        this.userPostCommentDao = userPostCommentDao;
        this.postDao = postDao;
        this.userDao = new UserDao();
    }

    public void createComment(CreateCommentDTO createCommentDTO) throws SQLException {
        UserPostComment comment = new UserPostComment();
        comment.setPostId(createCommentDTO.getPostId());
        comment.setUserId(createCommentDTO.getUserId());
        comment.setComment(createCommentDTO.getComment());
        comment.setCreatedBy(createCommentDTO.getUserId());
        comment.setUpdatedBy(createCommentDTO.getUserId());

        userPostCommentDao.createComment(comment);


        postDao.incrementCommentCount(createCommentDTO.getPostId());
    }

    public void deleteComment(String commentId) throws SQLException {
        UserPostComment comment = userPostCommentDao.getCommentById(commentId);
        if (comment != null) {
            userPostCommentDao.deleteComment(commentId);

            postDao.decrementCommentCount(comment.getPostId());
        }
    }

    public List<GetCommentDTO> getCommentsByPostId(String postId) throws SQLException {
        return userPostCommentDao.getCommentsByPostId(postId).stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private GetCommentDTO convertToDTO(UserPostComment comment)  {
        GetCommentDTO commentDTO = new GetCommentDTO();
        try {
            User user = userDao.getUserById(comment.getUserId());
            commentDTO.setUsername(user.getEmail());
        } catch (SQLException excp) {
        }
        commentDTO.setId(comment.getId());
        commentDTO.setPostId(comment.getPostId());
        commentDTO.setUserId(comment.getUserId());
        commentDTO.setComment(comment.getComment());
        return commentDTO;
    }

    public List<GetPostDTO> getCommentedPostsByUserId(String userId) throws SQLException {
        List<UserPostComment> userComments = userPostCommentDao.getCommentsByUserId(userId);
        List<GetPostDTO> commentedPosts = new ArrayList<>();

        for (UserPostComment comment : userComments) {
            Post post = postDao.getPostById(comment.getPostId());
            if (post != null) {
                GetPostDTO dto = convertToGetPostDTO(post);
                commentedPosts.add(dto);
            }
        }

        return commentedPosts;
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
