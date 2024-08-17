package di5.data.model;

public class UserPostComment extends BaseEntity {
    private String postId;
    private String userId;
    private String comment;

    public UserPostComment() {
        postId = null;
        userId = null;
        comment = null;
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
