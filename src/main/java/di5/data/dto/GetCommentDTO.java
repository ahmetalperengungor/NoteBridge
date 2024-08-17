package di5.data.dto;

import java.util.Date;

public class GetCommentDTO {
    private String id;
    private String postId;
    private String userId;
    private String comment;
    private Date createdAt;
    private String createdBy;
    private int commentCount;
    private String username;

    public GetCommentDTO() {
    }

    public GetCommentDTO(String id, String postId, String userId, String comment, Date createdAt, String createdBy, int commentCount, String username) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.comment = comment;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.commentCount = commentCount;
        this.username = username;
    }
    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
