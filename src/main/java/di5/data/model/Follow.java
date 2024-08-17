package di5.data.model;

import java.sql.Timestamp;

public class Follow extends BaseEntity {
    private String followerId;
    private String followedId;

    public Follow() {
        followerId = null;
        followedId = null;
    }

    public String getFollowerId() {
        return this.followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFollowedId() {
        return this.followedId;
    }

    public void setFollowedId(String followedId) {
        this.followedId = followedId;
    }
}