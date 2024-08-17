    -- private String id;
    -- private Date createdAt;
    -- private String createdBy;
    -- private Date updatedAt;
    -- private String updatedBy;
    -- private boolean isDeleted;
    -- private String deletedBy;
    -- private Date deletedAt;

    -- private String commercialName;
    -- private String price;
    -- private String agreementDate;
    -- private String agreementPostcount;

CREATE TABLE Sponsor (
    id VARCHAR(36) PRIMARY KEY,
    createdAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(36) NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    updatedBy VARCHAR(36) NOT NULL,
    isDeleted BOOLEAN NOT NULL,
    deletedBy VARCHAR(36),
    deletedAt TIMESTAMP,
    commercialName VARCHAR(255) NOT NULL,
    price VARCHAR(255) NOT NULL,
    agreementDate VARCHAR(255) NOT NULL,
    agreementPostcount VARCHAR(255) NOT NULL
);