CREATE TABLE USER_ROLE(
    ID number NOT NULL PRIMARY KEY,
    ROLE VARCHAR2(40) NOT NULL UNIQUE
);

CREATE TABLE SABE_USER(
    ID number NOT NULL PRIMARY KEY,
    NAME VARCHAR2(255) NOT NULL,
    SURNAME VARCHAR2(255) NOT NULL,
    MAIL VARCHAR2(255) UNIQUE NOT NULL,
    PASSWORD VARCHAR2(128) NOT NULL,
    RANDOM_IDENTIFIER VARCHAR2(128) NOT NULL,
    VERIFICATION_STATUS VARCHAR2(128) NOT NULL,
    AGE NUMBER
);

CREATE TABLE USER_ROLES(
    USER_ID number NOT NULL,
    ROLE_ID number NOT NULL,
    CONSTRAINT fk_role FOREIGN KEY (ROLE_ID) REFERENCES USER_ROLE(ID),
    CONSTRAINT fk_user FOREIGN KEY (USER_ID) REFERENCES SABE_USER(ID)
                ON DELETE CASCADE,  -- Consent USER_ROLES records to be deleted when the user is deleted
    PRIMARY KEY (USER_ID, ROLE_ID)
);