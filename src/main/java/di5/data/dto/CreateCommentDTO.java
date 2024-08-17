package di5.data.dto;

public class CreateCommentDTO {
    private String postId;
    private String userId;
    private String comment;

    public CreateCommentDTO() {
    }

    public CreateCommentDTO(String postId, String userId, String comment) {
        this.postId = postId;
        this.userId = userId;
        this.comment = comment;
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
}
