    -- private String id;
    -- private Date createdAt;
    -- private String createdBy;
    -- private Date updatedAt;
    -- private String updatedBy;
    -- private boolean isDeleted;
    -- private String deletedBy;
    -- private Date deletedAt;

    -- private String id;
    -- private String url;
    -- private MediaType mediaType;

CREATE TABLE Image (
    id VARCHAR(36) PRIMARY KEY,
    createdAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(36) NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    updatedBy VARCHAR(36) NOT NULL,
    isDeleted BOOLEAN NOT NULL,
    deletedBy VARCHAR(36),
    deletedAt TIMESTAMP,
    url VARCHAR(255) NOT NULL,
    mediaType INT NOT NULL
);
