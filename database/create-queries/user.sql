    -- private String id;
    -- private Date createdAt;
    -- private String createdBy;
    -- private Date updatedAt;
    -- private String updatedBy;
    -- private boolean isDeleted;
    -- private String deletedBy;
    -- private Date deletedAt;

    -- private String firstName;
    -- private String lastName;
    -- private Date birthDate;
    -- private Gender gender;
    -- private String email;
    -- private String phoneNumber;
    -- private String hashPassword;

CREATE TABLE User (
    id VARCHAR(36) PRIMARY KEY,
    createdAt TIMESTAMP NOT NULL,
    createdBy VARCHAR(36) NOT NULL,
    updatedAt TIMESTAMP NOT NULL,
    updatedBy VARCHAR(36) NOT NULL,
    isDeleted BOOLEAN NOT NULL,
    deletedBy VARCHAR(36),
    deletedAt TIMESTAMP,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    birthDate DATE NOT NULL,
    gender INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255) NOT NULL,
    hashPassword VARCHAR(255) NOT NULL
);
