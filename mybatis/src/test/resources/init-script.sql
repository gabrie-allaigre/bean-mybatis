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
('T_TEST2', 'NAME1', 'fra', '1', 'Citrouille'),
('T_TEST2', 'NAME2', 'fra', '3', 'Courgette'),
('T_TEST2', 'NAME1', 'fra', '4', 'Pomme'),
('T_TEST2', 'NAME2', 'fra', '2', 'Poire');

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

DROP TABLE t_train
IF EXISTS;

CREATE TABLE t_train (
  id             VARCHAR(256),
  version        INT,
  code     VARCHAR(256),
  start_country_id     VARCHAR(256),
  end_country_id VARCHAR(256)
);

INSERT INTO t_train (id, version, code, start_country_id, end_country_id) VALUES
('1', 0, '123456', '1', '2'),
('2', 0, '145678', '1', '3'),
('3', 0, '999999', '2', '3'),
('4', 0, '987654', '5', '6'),
('5', 0, '222222', '1', '5');

DROP TABLE t_wagon
IF EXISTS;

CREATE TABLE t_wagon (
  id             VARCHAR(256),
  version        INT,
  train_id        VARCHAR(256),
  code     VARCHAR(256),
  position INT
);

INSERT INTO t_wagon (id, version, train_id, code, position) VALUES
('1', 0, '1', 'C', 3),
('2', 0, '1', 'B', 2),
('3', 0, '1', 'A', 1),
('4', 0, '2', '0', 1),
('5', 0, '2', '1', 2),
('6', 0, '1', 'D', 4);

DROP TABLE t_container
IF EXISTS;

CREATE TABLE t_container (
  id             VARCHAR(256),
  version        INT,
  wagon_id        VARCHAR(256),
  code     VARCHAR(256)
);

INSERT INTO t_container (id, version, wagon_id, code) VALUES
  ('1', 0, '1', '78946'),
  ('2', 0, '1', '7984564'),
  ('3', 0, '2', '7984651');

DROP TABLE t_wheel
IF EXISTS;

CREATE TABLE t_wheel (
  id             VARCHAR(256),
  version        INT,
  wagon_id        VARCHAR(256),
  size     VARCHAR(256)
);

INSERT INTO t_wheel (id, version, wagon_id, size) VALUES
  ('1', 0, '2', 'A'),
  ('2', 0, '2', 'A'),
  ('3', 0, '3', 'C');

DROP TABLE t_test2
IF EXISTS;

CREATE TABLE t_test2 (
  id             VARCHAR(256),
  version        INT,
  name1 VARCHAR(256),
  name2 VARCHAR(256),
  start_country_id     VARCHAR(256),
  end_country_id VARCHAR(256)
);

INSERT INTO t_test2 (id, version,name1,name2, start_country_id, end_country_id) VALUES
  ('1', 0,'Gaby1','Toto1', '1', '2'),
  ('2', 0,'Gaby2','Toto2', '1', '3'),
  ('3', 0,'Gaby3','Toto3', '2', '3'),
  ('4', 0,'Gaby4','Toto4', '5', '6'),
  ('5', 0,'Gaby5','Toto5', '1', '5');
