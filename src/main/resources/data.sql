INSERT INTO users (username, password, email, enabled) VALUES ('user', '$2a$12$CBfXWsWXAVbH3BLFOiZNpuOTGzGdkR.0YnUaHdW2cu93fKkZ9FW3y','user@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('admin', '$2a$12$CBfXWsWXAVbH3BLFOiZNpuOTGzGdkR.0YnUaHdW2cu93fKkZ9FW3y', 'admin@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('test', '$2a$12$CBfXWsWXAVbH3BLFOiZNpuOTGzGdkR.0YnUaHdW2cu93fKkZ9FW3y', 'test@test.nl', TRUE);
INSERT INTO authorities(username,authority) VALUES ('admin','ROLE_ADMIN');
INSERT INTO authorities(username,authority) VALUES ('user','ROLE_USER');
INSERT INTO authorities(username,authority) VALUES ('test','ROLE_USER');


INSERT INTO persons (given_names, surname, sex)
VALUES ('Grandfather', 'Doe', 'M'),     -- 1
       ('Grandmother', 'Noe', 'F'),     -- 2
       ('Uncle1', 'Doe', 'M'),          -- 3
       ('Aunt1', 'Foe', 'F'),           -- 4
       ('Cousin1A', 'Doe', 'M'),        -- 5
       ('Cousin1B', 'Doe', 'F'),        -- 6
       ('Father', 'Doe', 'M'),          -- 7
       ('Mother', 'Moe', 'F'),          -- 8
       ('Son', 'Doe', 'M'),             -- 9
       ('Daughter-in-law', 'Goe', 'F'), --10
       ('Child', 'Doe', 'M'),           --11
       ('Daugter', 'Doe', 'F'),         --12
       ('Son-in-law', 'Toe', 'M'),      --13
       ('Nephew', 'Toe', 'M'),          --14
       ('Niece', 'Toe', 'F'),           --15
       ('Aunt2', 'Doe', 'F'),           --16
       ('Uncle2', 'Zoe', 'M'),          --17
       ('CousinTwinB', 'Zoe', 'M'),     --18
       ('CousinTwinA', 'Zoe', 'F'),     --19
       ('GreatGrandFather', 'Doe', 'M'),                --20
       ('GreatGrandMother', 'Roe', 'F'),                --21
       ('GrandUncle', 'Doe', 'M'),      --22
       ('GrandAunt', 'Roe', 'F'),                       --23
       ('Aunt3', 'Doe', 'F'),           --24
       ('Uncle3A', 'Hoe', 'M'),         --25
       ('Cousin3A1', 'Hoe', 'M'),       --26
       ('Cousin3A2', 'Hoe', 'F'),       --27
       ('Uncle3B', 'Koe', 'M'),         --28
       ('Cousin3B1', 'Koe', 'F'),       --29
       ('Cousin3B2', 'Koe', 'M'),       --30
       ('Father-in-Law', 'Goe', 'M'),   --31
       ('Mother-in-Law', 'Noe', 'F'),   --32
       ('Sister-in-Law', 'Goe', 'F'),   --33
       ('Co-Sister-in-Law', 'Poe', 'M'),--34
       ('NephewPoe', 'Poe', 'M');

INSERT INTO events(event_type, person_id, relation_id, description, text, begin_date, end_date)
VALUES ('BIRTH',1, null, 'Date of birth', '', '1898-01-01', '1898-12-31'),
       ('BIRTH',2, null, 'Date of birth', '', '1899-01-01', '1900-12-31'),
       ('BIRTH',3, null, 'Date of birth', '', '1920-01-01', '1920-01-01'),
       ('BIRTH',4, null, 'Date of birth', '', '1922-01-01', '1922-01-01'),
       ('BIRTH',5, null, 'Date of birth', '', '2011-12-01', '2011-12-01'),
       ('BIRTH',6, null, 'Date of birth', '', '2011-12-02', '2011-12-02'),
       ('BIRTH',7, null, 'Date of birth', '', '1922-02-02', '1922-02-02'),
       ('BIRTH',8, null, 'Date of birth', '', '1922-02-11', '1922-02-11'),
       ('BIRTH',9, null, 'Date of birth', '', '1950-11-01', '1950-11-01'),
       ('BIRTH',10, null, 'Date of birth', '', '1955-11-11', '1955-11-11'),
       ('BIRTH',11, null, 'Date of birth', '', '1985-11-11', '1985-11-11'),
       ('BIRTH',12, null, 'Date of birth', '', '1955-11-02', '1955-11-02'),
       ('BIRTH',13, null, 'Date of birth', '', '1954-11-11', '1954-11-11'),
       ('BIRTH',14, null, 'Date of birth', '', '1980-12-01', '1980-12-01'),
       ('BIRTH',15, null, 'Date of birth', '', '1982-12-01', '1982-12-01'),
       ('BIRTH',16, null, 'Date of birth', '', '1925-12-01', '1925-12-31'),
       ('BIRTH',17, null, 'Date of birth', '', '1918-02-01', '1918-02-01'),
       ('BIRTH',18, null, 'Date of birth', '', '1940-01-01', '1940-12-31'),
       ('BIRTH',19, null, 'Date of birth', '', '1940-01-01', '1940-12-31'),
--       ('BIRTH',20, NULL, 'Date of birth', '', NULL, NULL),
--       ('BIRTH',21, NULL, 'Date of birth', '', NULL, NULL),
       ('BIRTH',22, NULL, 'Date of birth', '', '1899-01-01', '1899-12-31'),
--       ('BIRTH',23, NULL, 'Date of birth', '', NULL, NULL),
       ('BIRTH',24, NULL, 'Date of birth', '', '1923-02-01', '1923-02-01'),
       ('BIRTH',25, NULL, 'Date of birth', '', '1922-02-01', '1922-02-01'),
       ('BIRTH',26, NULL, 'Date of birth', '', '1945-02-01', '1945-02-01'),
       ('BIRTH',27, NULL, 'Date of birth', '', '1946-02-02', '1946-02-02'),
       ('BIRTH',28, NULL, 'Date of birth', '', '1918-02-01', '1918-02-01'),
       ('BIRTH',29, NULL, 'Date of birth', '', '1951-03-01', '1951-03-01'),
       ('BIRTH',30, NULL, 'Date of birth', '', '1952-03-02', '1952-03-02'),
       ('BIRTH',31, NULL, 'Date of birth', '', '1923-02-02', '1923-02-02'),
       ('BIRTH',32, NULL, 'Date of birth', '', '1924-02-11', '1924-02-11'),
       ('BIRTH',33, NULL, 'Date of birth', '', '1955-02-11', '1955-02-11'),
       ('BIRTH',34, NULL, 'Date of birth', '', '1954-02-11', '1954-02-11'),
       ('BIRTH',35, NULL, 'Date of birth', '', '1974-02-11', '1974-02-11');


INSERT INTO relations(person_id, spouse_id)
VALUES (1, 2),                   --1
       (3, 4),                   --2
       (7, 8),                   --3
       (9, 10),                  --4
       (12, 13),                 --5
       (16, 17),                 --6
       (20, 21),                 --7
       (22, 23),                 --8
       (24, 25), --9	<= Cousin3A1 (26), Cousin3A2 (27)
       (24, 28),                 --10			<= Cousin3AB (29), Cousin3B2 (30)
       (31, 32),                 --11
       (33, 34);


INSERT INTO events(event_type, person_id, relation_id, description, text, begin_date, end_date)
VALUES ('MARRIAGE', NULL, 1, 'Marriage', '', '1910-01-01', '1910-01-01'),                   --1
       ('MARRIAGE', NULL, 2, 'Marriage', '', '1940-01-01', '1940-01-01'),                   --2
       ('MARRIAGE', NULL, 3, 'Marriage', '', '1945-01-01', '1945-01-01'),                   --3
       ('MARRIAGE', NULL, 4, 'Marriage', '', '1970-01-01', '1970-01-01'),                  --4
       ('MARRIAGE', NULL, 5, 'Marriage', '', '1985-01-01', '1985-01-01'),                 --5
       ('MARRIAGE', NULL, 6, 'Marriage', '', '1950-01-01', '1950-12-31'),                 --6
--       ('MARRIAGE', NULL, 7, 'Marriage', '', NULL, NULL),                                 --7
--       ('MARRIAGE', NULL, 8, 'Marriage', '', NULL, NULL),                                 --8
       ('MARRIAGE', NULL, 9, 'Marriage', '', '1944-01-01', '1944-01-01'), --9	<= Cousin3A1 (26), Cousin3A2 (27)
       ('DIVORCE', NULL,  9, 'Marriage', '', '1949-01-01', '1949-12-31'), --9	<= Cousin3A1 (26), Cousin3A2 (27)
       ('MARRIAGE', NULL,10, 'Marriage', '', '1950-01-01', '1950-01-01');                 --10			<= Cousin3AB (29), Cousin3B2 (30)
--       ('MARRIAGE',11, 'Marriage', '', NULL, NULL),                                 --11
--       ('MARRIAGE',12, 'Marriage', '', NULL, NULL);


INSERT INTO children (relation_id, child_id)
VALUES (1, 3),
       (1, 7),
       (1, 16),
       (2, 5),
       (2, 6),
       (3, 9),
       (3, 12),
       (4, 11),
       (5, 14),
       (5, 15),
       (6, 18),
       (6, 19),
       (7, 1),
       (7, 22),
       (8, 24),
       (9, 26),
       (9, 27),
       (10, 29),
       (10, 30),
       (11, 33),
       (11, 10),
       (12, 35);
