set mode Oracle;
INSERT INTO USER_ROLE (id, role) VALUES (ROLE_SEQ.NEXTVAL, 'USER');
INSERT INTO USER_ROLE (id, role) VALUES (ROLE_SEQ.NEXTVAL, 'SUPERUSER');

INSERT INTO SABE_USER (id, RANDOM_IDENTIFIER, VERIFICATION_STATUS, name, surname, mail, password, age) VALUES (SABE_USER_SEQ.NEXTVAL, 'Aaisdhauibeifb', 'VERIFIED', 'Alessio', 'Moraschini', 'alessio.moraschini@hotmail.it', '$2a$10$vm8hJMZHV1q7RkGFxkNojeV9ILm3w1auHzMoNVtZqXTcQtg9Cibc2', 33);
INSERT INTO SABE_USER (id, RANDOM_IDENTIFIER, VERIFICATION_STATUS, name, surname, mail, password, age) VALUES (SABE_USER_SEQ.NEXTVAL, 'AaisdhauiXxdfb', 'VERIFIED', 'Nano', 'Moraschini', 'a.b@mail.it', '$2a$10$A7.32vMAcLq81lEwaGEhAuCdUDWtfacvb4L3eHFNmz2lPWJIeA3v.', 6);
INSERT INTO SABE_USER (id, RANDOM_IDENTIFIER, VERIFICATION_STATUS, name, surname, mail, password, age) VALUES (SABE_USER_SEQ.NEXTVAL, 'Aazsdhauibeifb', 'VERIFIED', 'Fefe', 'Moraschini', 'b.b@mail.it', '$2a$10$A7.32vMAcLq81lEwaGEhAuCdUDWtfacvb4L3eHFNmz2lPWJIeA3v.', 5);
INSERT INTO SABE_USER (id, RANDOM_IDENTIFIER, VERIFICATION_STATUS, name, surname, mail, password, age) VALUES (SABE_USER_SEQ.NEXTVAL, 'AaisdhrfbeOifb', 'PENDING', 'Armando', 'Maradona', 'c.b@mail.it', '$2a$10$A7.32vMAcLq81lEwaGEhAuCdUDWtfacvb4L3eHFNmz2lPWJIeA3v.', 98);
INSERT INTO SABE_USER (id, RANDOM_IDENTIFIER, VERIFICATION_STATUS, name, surname, mail, password, age) VALUES (SABE_USER_SEQ.NEXTVAL, 'AaisdAjoMmeifb', 'VERIFIED', 'Hail', 'ToTheKing', 'd.b@mail.it', '$2a$10$A7.32vMAcLq81lEwaGEhAuCdUDWtfacvb4L3eHFNmz2lPWJIeA3v.', 100);

INSERT INTO USER_ROLES (user_id, role_id) VALUES (1, 1);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (1, 2);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (2, 1);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (3, 1);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (4, 1);
INSERT INTO USER_ROLES (user_id, role_id) VALUES (5, 1);