INSERT INTO persons (given_names, surname, sex)
VALUES ('Grandfather', 'Doe', 'M'),     -- 1
       ('Grandmother', 'Roe', 'F'),     -- 2
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

INSERT INTO events(event_type, person_id, relation_id, description, begin_date, end_date)
VALUES ('BIRTH',1, null, '', '1898-01-01', '1898-12-31'),
       ('BIRTH',2, null, '', '1899-01-01', '1900-12-31'),
       ('BIRTH',3, null, '', '1920-01-01', '1920-01-01'),
       ('BIRTH',4, null, '', '1922-01-01', '1922-01-01'),
       ('BIRTH',5, null, '', '2011-12-01', '2011-12-01'),
       ('BIRTH',6, null, '', '2011-12-02', '2011-12-02'),
       ('BIRTH',7, null, '', '1922-02-02', '1922-02-02'),
       ('BIRTH',8, null, '', '1922-02-11', '1922-02-11'),
       ('BIRTH',9, null, '', '1950-11-01', '1950-11-01'),
       ('BIRTH',10, null, '', '1955-11-11', '1955-11-11'),
       ('BIRTH',11, null, '', '1985-11-11', '1985-11-11'),
       ('BIRTH',12, null, '', '1955-11-02', '1955-11-02'),
       ('BIRTH',13, null, '', '1954-11-11', '1954-11-11'),
       ('BIRTH',14, null, '', '1980-12-01', '1980-12-01'),
       ('BIRTH',15, null, '', '1982-12-01', '1982-12-01'),
       ('BIRTH',16, null, '', '1925-12-01', '1925-12-31'),
       ('BIRTH',17, null, '', '1918-02-01', '1918-02-01'),
       ('BIRTH',18, null, '', '1940-01-01', '1940-12-31'),
       ('BIRTH',19, null, '', '1940-01-01', '1940-12-31'),
--       ('BIRTH',20, NULL, '', NULL, NULL),
--       ('BIRTH',21, NULL, '', NULL, NULL),
       ('BIRTH',22, NULL, '', '1899-01-01', '1899-12-31'),
--       ('BIRTH',23, NULL, '', NULL, NULL),
       ('BIRTH',24, NULL, '', '1923-02-01', '1923-02-01'),
       ('BIRTH',25, NULL, '', '1922-02-01', '1922-02-01'),
       ('BIRTH',26, NULL, '', '1945-02-01', '1945-02-01'),
       ('BIRTH',27, NULL, '', '1946-02-02', '1946-02-02'),
       ('BIRTH',28, NULL, '', '1918-02-01', '1918-02-01'),
       ('BIRTH',29, NULL, '', '1951-03-01', '1951-03-01'),
       ('BIRTH',30, NULL, '', '1952-03-02', '1952-03-02'),
       ('BIRTH',31, NULL, '', '1923-02-02', '1923-02-02'),
       ('BIRTH',32, NULL, '', '1924-02-11', '1924-02-11'),
       ('BIRTH',33, NULL, '', '1955-02-11', '1955-02-11'),
       ('BIRTH',34, NULL, '', '1954-02-11', '1954-02-11'),
       ('BIRTH',35, NULL, '', '1974-02-11', '1974-02-11');


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


INSERT INTO events(event_type, person_id, relation_id, description, begin_date, end_date)
VALUES ('MARRIAGE', NULL, 1, '', '1910-01-01', '1910-01-01'),                   --1
       ('MARRIAGE', NULL, 2, '', '1940-01-01', '1940-01-01'),                   --2
       ('MARRIAGE', NULL, 3, '', '1945-01-01', '1945-01-01'),                   --3
       ('MARRIAGE', NULL, 4, '', '1970-01-01', '1970-01-01'),                  --4
       ('MARRIAGE', NULL, 5, '', '1985-01-01', '1985-01-01'),                 --5
       ('MARRIAGE', NULL, 6, '', '1950-01-01', '1950-12-31'),                 --6
--       ('MARRIAGE', NULL, 7, '', NULL, NULL),                                 --7
--       ('MARRIAGE', NULL, 8, '', NULL, NULL),                                 --8
       ('MARRIAGE', NULL, 9, '', '1944-01-01', '1944-01-01'), --9	<= Cousin3A1 (26), Cousin3A2 (27)
       ('DIVORCE', NULL,  9, '', '1949-01-01', '1949-12-31'), --9	<= Cousin3A1 (26), Cousin3A2 (27)
       ('MARRIAGE', NULL,10, '', '1950-01-01', '1950-01-01');                 --10			<= Cousin3AB (29), Cousin3B2 (30)
--       ('MARRIAGE',11, '', NULL, NULL),                                 --11
--       ('MARRIAGE',12, '', NULL, NULL);


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
