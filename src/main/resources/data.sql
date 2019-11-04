DROP TABLE IF EXISTS currencies;
DROP TABLE IF EXISTS request_log_entries;
DROP TABLE IF EXISTS forecasts;


CREATE TABLE currencies (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  code VARCHAR(255) NOT NULL,
  number INT(3) NOT NULL,
  exponent VARCHAR(10) NOT NULL,
  name VARCHAR(250) NOT NULL
);

CREATE TABLE request_log_entries (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  request_date DATETIME NOT NULL,
  currency_code VARCHAR(255) NOT NULL,
  client_ip VARCHAR(250) NOT NULL

);

CREATE TABLE forecasts (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  request_date DATETIME NOT NULL,
  client_ip VARCHAR(250) NOT NULL,
  descritption CLOB NOT NULL,
  latitude DECIMAL NOT NULL,
  longitude DECIMAL NOT NULL
);