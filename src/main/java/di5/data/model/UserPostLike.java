package di5.data.model;

public class UserPostLike extends BaseEntity {
    private String postId;
    private String userId;

    public UserPostLike() {
        postId = null;
        userId = null;
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

}
