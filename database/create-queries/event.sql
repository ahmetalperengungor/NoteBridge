    -- private String id;
    -- private Date createdAt;
    -- private String createdBy;
    -- private Date updatedAt;
    -- private String updatedBy;
    -- private boolean isDeleted;
    -- private String deletedBy;
    -- private Date deletedAt;

    -- private String title;
    -- private String description;
    -- private Date eventDate;
    -- private Date eventTime;
    -- private String eventLocationLatitude;
    -- private String eventLocationLongitude;

CREATE TABLE Event (
    id VARCHAR(36) PRIMARY KEY,
    createdAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(36) NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    updatedBy VARCHAR(36) NOT NULL,
    isDeleted BOOLEAN NOT NULL,
    deletedBy VARCHAR(36),
    deletedAt TIMESTAMP,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    eventDate DATE NOT NULL,
    eventTime TIME NOT NULL,
    eventLocationLatitude VARCHAR(255) NOT NULL,
    eventLocationLongitude VARCHAR(255) NOT NULL
);