    -- private String id;
    -- private Date createdAt;
    -- private String createdBy;
    -- private Date updatedAt;
    -- private String updatedBy;
    -- private boolean isDeleted;
    -- private String deletedBy;
    -- private Date deletedAt;

    -- private String title;
    -- private String sponsorId;
    -- private String eventId;
    -- private String description;
    -- private PostType postType;
    -- private Number likeCount;
    -- private Number commentCount;

CREATE TABLE Post (
    id VARCHAR(36) PRIMARY KEY,
    createdAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(36) NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    updatedBy VARCHAR(36) NOT NULL,
    isDeleted BOOLEAN NOT NULL,
    deletedBy VARCHAR(36),
    deletedAt TIMESTAMP,
    title VARCHAR(255) NOT NULL,
    sponsorId VARCHAR(36) NOT NULL,
    eventId VARCHAR(36) NOT NULL,
    description TEXT NOT NULL,
    postType INT NOT NULL,
    likeCount INT NOT NULL,
    commentCount INT NOT NULL
);