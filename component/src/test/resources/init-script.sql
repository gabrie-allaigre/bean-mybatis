DROP TABLE t_user
IF EXISTS;

CREATE TABLE t_user (
  id           VARCHAR(256),
  version      INT,
  login        VARCHAR(256),
  country_code VARCHAR(256),
  country_id   VARCHAR(256),
  address_id   VARCHAR(256),
  created_date DATETIME,
  created_by   VARCHAR(240),
  updated_date DATETIME,
  updated_by   VARCHAR(240)
);

INSERT INTO t_user (id, version, login, country_code, country_id, address_id, created_date, created_by) VALUES
  ('1', 0, 'gabriel', 'FRA', '1', '2', SYSDATE, 'GABY'),
  ('2', 0, 'sandra', 'CHI', '1', '1', SYSDATE, 'GABY');

DROP TABLE t_group
IF EXISTS;

CREATE TABLE t_group (
  id            VARCHAR(256),
  version       INT,
  user_id       VARCHAR(256),
  name          VARCHAR(256),
  created_date  DATETIME,
  created_by    VARCHAR(240),
  updated_date  DATETIME,
  updated_by    VARCHAR(240),
  canceled      BOOLEAN DEFAULT FALSE,
  canceled_date DATETIME,
  canceled_by   VARCHAR(240)
);

INSERT INTO t_group (id, version, user_id, name, created_date, created_by, canceled, canceled_date, canceled_by) VALUES
  ('1', 0, '1', 'admin', SYSDATE, 'GABY',FALSE,null,null),
  ('2', 0, '1', 'system', SYSDATE, 'GABY',FALSE,null,null),
  ('3', 0, '2', 'user', SYSDATE, 'GABY',FALSE,null,null),
  ('4', 0, '1', 'simple', SYSDATE, 'GABY', TRUE, SYSDATE, 'GABY');

DROP TABLE t_address
IF EXISTS;

CREATE TABLE t_address (
  id            VARCHAR(256),
  version       INT,
  city          VARCHAR(256),
  postal_zip    VARCHAR(256),
  country_id    VARCHAR(256),
  created_date  DATETIME,
  created_by    VARCHAR(240),
  updated_date  DATETIME,
  updated_by    VARCHAR(240),
  canceled      BOOLEAN DEFAULT FALSE,
  canceled_date DATETIME,
  canceled_by   VARCHAR(240)
);

INSERT INTO t_address (id, version, city, postal_zip, country_id, created_date, created_by) VALUES
  ('1', 0, 'Versailles', '78000', '1', SYSDATE, 'GABY'),
  ('2', 0, 'Valence', '26000', '1', SYSDATE, 'GABY'),
  ('3', 0, 'London', '??', '3', SYSDATE, 'GABY'),
  ('4', 0, 'Xi''an', '??', '2', SYSDATE, 'GABY'),
  ('5', 0, 'Noisy-le-roi', '78230', '1', SYSDATE, 'GABY');

DROP TABLE t_country
IF EXISTS;

CREATE TABLE t_country (
  id            VARCHAR(256),
  version       INT,
  code          VARCHAR(256),
  name          VARCHAR(256),
  created_date  DATETIME,
  created_by    VARCHAR(240),
  updated_date  DATETIME,
  updated_by    VARCHAR(240),
  canceled      BOOLEAN DEFAULT FALSE,
  canceled_date DATETIME,
  canceled_by   VARCHAR(240)
);

INSERT INTO t_country (id, version, code, name, created_date, created_by) VALUES
  ('1', 0, 'FRA', 'france', SYSDATE, 'GABY'),
  ('2', 0, 'CHI', 'chine', SYSDATE, 'GABY'),
  ('3', 0, 'ENG', 'england', SYSDATE, 'GABY'),
  ('4', 0, 'ESP', 'espagne', SYSDATE, 'GABY'),
  ('5', 0, 'ITA', 'italie', SYSDATE, 'GABY'),
  ('6', 0, 'USA', 'etat-unis', SYSDATE, 'GABY'),
  ('7', 0, 'F%', 'f', SYSDATE, 'GABY');

DROP TABLE t_asso_user_address
IF EXISTS;

CREATE TABLE t_asso_user_address (
  user_id    VARCHAR(256),
  address_id VARCHAR(256)
);

INSERT INTO t_asso_user_address (user_id, address_id) VALUES
  ('1', '1'),
  ('1', '2'),
  ('1', '3'),
  ('2', '4');

DROP TABLE t_nls
IF EXISTS;

CREATE TABLE t_nls (
  table_name    VARCHAR(256),
  column_name   VARCHAR(256),
  language_code VARCHAR(3),
  table_id      VARCHAR(256),
  meaning       VARCHAR(256)
);

INSERT INTO t_nls (table_name, column_name, language_code, table_id, meaning) VALUES
  ('T_COUNTRY', 'NAME', 'eng', '1', 'Cheese'),
  ('T_COUNTRY', 'NAME', 'fra', '1', 'Fromage'),
  ('T_COUNTRY', 'NAME', 'fra', '6', 'United states of america'),
  ('T_ADDRESS', 'MEANING', 'eng', '1', 'Nothing');

DROP TABLE t_categorie
IF EXISTS;

CREATE TABLE t_categorie (
  id_categorie        INT,
  id_categorie_parent INT,
  LIB_CATEGORIE       VARCHAR(256)
);

INSERT INTO t_categorie (id_categorie, id_categorie_parent, LIB_CATEGORIE) VALUES
  (0, NULL, 'ELISE'),
  (1, 0, 'JOSE'),
  (2, 0, 'BEATRICE'),
  (3, 0, 'MARIE'),
  (4, 2, 'GABRIEL'),
  (5, 2, 'DAVID'),
  (6, 1, 'LEA');

DROP TABLE t_person
IF EXISTS;

CREATE TABLE t_person (
  id             VARCHAR(256),
  version        INT,
  first_name     VARCHAR(256),
  last_name      VARCHAR(256),
  sexe          VARCHAR(256),
  age            INT,
  height         FLOAT,
  WEIGHT          DOUBLE,
  birthday       DATE,
  address_id     VARCHAR(256),
  address_bis_id VARCHAR(256),
  created_date   DATETIME,
  created_by     VARCHAR(240),
  updated_date   DATETIME,
  updated_by     VARCHAR(240)
);

INSERT INTO t_person (id, version, first_name, last_name, age, height, birthday, address_id, created_date, created_by) VALUES
('1', 0, 'Gabriel', 'Allaigre', 36, 186, DATE '1980-02-07', '1', SYSDATE, 'GABY'),
('2', 0, 'Sandra', 'Allaigre', 38, 150, DATE '1978-04-05', '2', SYSDATE, 'GABY'),
('3', 0, 'Laureline', 'Allaigre', 4, 105, DATE '2012-05-17', '1', SYSDATE, 'GABY'),
('4', 0, 'Raphael', 'Allaigre', 1, 80, DATE '2015-05-13', '3', SYSDATE, 'GABY'),
('5', 0, 'David', 'Allaigre', 40, 180, DATE '1976-05-13', null, SYSDATE, 'GABY');

DROP TABLE T_ASSO_INT_ADDRESS
IF EXISTS;

CREATE TABLE T_ASSO_INT_ADDRESS (
  address_id VARCHAR(256),
  int_id     VARCHAR(256)
);

DROP TABLE T_ASSO_PERSON_INT
IF EXISTS;

CREATE TABLE T_ASSO_PERSON_INT (
  person_id VARCHAR(256),
  int_id    VARCHAR(256)
);

DROP TABLE T_ASSO_PERSON_ADDRESS
IF EXISTS;

CREATE TABLE T_ASSO_PERSON_ADDRESS (
  person_id  VARCHAR(256),
  address_id VARCHAR(256)
);

DROP TABLE t_train
IF EXISTS;

CREATE TABLE t_train (
  id      VARCHAR(256),
  version INT,
  code    VARCHAR(256)
);

INSERT INTO t_train (id, version, code) VALUES
  ('1', 0, '00001'),
  ('2', 0, '00002');

DROP TABLE t_wagon
IF EXISTS;

CREATE TABLE t_wagon (
  id       VARCHAR(256),
  version  INT,
  train_id VARCHAR(256),
  code     VARCHAR(256),
  position INT
);

INSERT INTO t_wagon (id, version, train_id, code, position) VALUES
  ('1', 0, '1', '000000000001', 5),
  ('2', 0, '1', '000000000002', 1),
  ('3', 0, '1', '000000000003', 2),
  ('4', 0, '1', '000000000004', 4),
  ('5', 0, '1', '000000000005', 3),
  ('6', 0, '2', '000000000001', 1),
  ('7', 0, '2', '000000000002', 2),
  ('8', 0, '2', '000000000004', 3),
  ('8', 0, '2', '000000000003', 3),
  ('8', 0, '2', '000000000001', 3);

DROP TABLE t_container
IF EXISTS;

CREATE TABLE t_container (
  wagon_id VARCHAR(256),
  code     VARCHAR(256)
);

INSERT INTO t_container (wagon_id, code) VALUES
  ('1', 'AAAAA'),
  ('1', 'BBBBB'),
  ('1', 'CCCCC'),
  ('2', 'DDDDD');

DROP TABLE t_wheel
IF EXISTS;

CREATE TABLE t_wheel (
  wagon_id VARCHAR(256),
  size     VARCHAR(256)
);

INSERT INTO t_wheel (wagon_id, size) VALUES
('1', 'A'),
('1', 'B'),
('1', 'C');

DROP TABLE t_parent1
IF EXISTS;

CREATE TABLE t_parent1 (
  id           VARCHAR(256),
  version      INT,
  name         VARCHAR(256),
  country_id   VARCHAR(256),
  created_date DATETIME,
  created_by   VARCHAR(240),
  updated_date DATETIME,
  updated_by   VARCHAR(240)
);

INSERT INTO t_parent1 (id, version, name, country_id, created_date, created_by) VALUES
('1', 0, 'Versailles', '1', SYSDATE, 'GABY'),
('2', 0, 'Paris', '2', SYSDATE, 'GABY');

DROP TABLE t_parent2
IF EXISTS;

CREATE TABLE t_parent2 (
  id           VARCHAR(256),
  version      INT,
  name         VARCHAR(256),
  country1_id  VARCHAR(256),
  country2_id  VARCHAR(256),
  created_date DATETIME,
  created_by   VARCHAR(240),
  updated_date DATETIME,
  updated_by   VARCHAR(240)
);

INSERT INTO t_parent2 (id, version, name, country1_id, country2_id, created_date, created_by) VALUES
('1', 0, 'Londre', '1', '2', SYSDATE, 'GABY');

DROP TABLE t_parent4
IF EXISTS;

CREATE TABLE t_parent4 (
  id           VARCHAR(256),
  version      INT,
  name         VARCHAR(256),
  parent1_id   VARCHAR(256),
  parent2_id   VARCHAR(256),
  parent3_id   VARCHAR(256),
  created_date DATETIME,
  created_by   VARCHAR(240),
  updated_date DATETIME,
  updated_by   VARCHAR(240)
);


INSERT INTO t_parent4 (id, version, name, parent1_id, parent2_id, parent3_id, created_date, created_by) VALUES
  ('1', 0, 'Valence', '1', '1', '2', SYSDATE, 'GABY');

DROP TABLE t_asso_parent_country
IF EXISTS;

CREATE TABLE t_asso_parent_country (
  parent_id  VARCHAR(256),
  country_id VARCHAR(256)
);

DROP TABLE t_asso_parent4_parent3
IF EXISTS;

CREATE TABLE t_asso_parent4_parent3 (
  parent4_id VARCHAR(256),
  parent3_id VARCHAR(256)
);

INSERT INTO t_asso_parent_country (parent_id, country_id) VALUES
('1', '1');
INSERT INTO t_asso_parent_country (parent_id, country_id) VALUES
('2', '2');

INSERT INTO t_asso_parent4_parent3 (parent4_id, parent3_id) VALUES
('1', '2');
