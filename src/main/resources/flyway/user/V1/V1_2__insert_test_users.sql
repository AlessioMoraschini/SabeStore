set mode Oracle;
INSERT INTO USER_ROLE (id, role) VALUES (ROLE_SEQ.NEXTVAL, 'USER');
INSERT INTO USER_ROLE (id, role) VALUES (ROLE_SEQ.NEXTVAL, 'SUPERUSER');

INSERT INTO SABE_USER (id, name, surname, mail, ROLE_ID, password, age) VALUES (SABE_USER_SEQ.NEXTVAL, 'Alessio', 'Moraschini', 'alessio.moraschini@hotmail.it', 2, '$2a$10$vm8hJMZHV1q7RkGFxkNojeV9ILm3w1auHzMoNVtZqXTcQtg9Cibc2', 33);
INSERT INTO SABE_USER (id, name, surname, mail, ROLE_ID, password, age) VALUES (SABE_USER_SEQ.NEXTVAL, 'Nano', 'Moraschini', 'a.b@mail.it', 1, '$2a$10$A7.32vMAcLq81lEwaGEhAuCdUDWtfacvb4L3eHFNmz2lPWJIeA3v.', 6);
INSERT INTO SABE_USER (id, name, surname, mail, ROLE_ID, password, age) VALUES (SABE_USER_SEQ.NEXTVAL, 'Fefe', 'Moraschini', 'b.b@mail.it', 1, '$2a$10$A7.32vMAcLq81lEwaGEhAuCdUDWtfacvb4L3eHFNmz2lPWJIeA3v.', 5);
INSERT INTO SABE_USER (id, name, surname, mail, ROLE_ID, password, age) VALUES (SABE_USER_SEQ.NEXTVAL, 'Armando', 'Maradona', 'c.b@mail.it', 1, '$2a$10$A7.32vMAcLq81lEwaGEhAuCdUDWtfacvb4L3eHFNmz2lPWJIeA3v.', 98);
INSERT INTO SABE_USER (id, name, surname, mail, ROLE_ID, password, age) VALUES (SABE_USER_SEQ.NEXTVAL, 'Hail', 'ToTheKing', 'd.b@mail.it', 1, '$2a$10$A7.32vMAcLq81lEwaGEhAuCdUDWtfacvb4L3eHFNmz2lPWJIeA3v.', 100);
