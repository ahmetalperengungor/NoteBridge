package di5.data.model;

import di5.data.enums.PostType;

public class Post extends BaseEntity {
    private String title;
    private String sponsorId;
    private String eventId;
    private String description;
    private PostType postType;
    private int likeCount;
    private int commentCount;

    public Post() {
        title = null;
        sponsorId = null;
        eventId = "0";
        description = null;
        postType = null;
        likeCount = 0;
        commentCount = 0;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSponsorId() {
        return this.sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PostType getPostType() {
        return this.postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
