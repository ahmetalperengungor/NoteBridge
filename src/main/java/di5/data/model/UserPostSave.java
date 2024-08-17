package di5.data.model;

import java.sql.Timestamp;

public class UserPostSave extends BaseEntity {
    private String postId;
    private String userId;

    public UserPostSave() {
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
