DROP TABLE IF EXISTS forecasts;

CREATE TABLE forecasts (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  request_date DATETIME NOT NULL,
  client_ip VARCHAR(250) NOT NULL,
  descritption CLOB NOT NULL,
  latitude DECIMAL NOT NULL,
  longitude DECIMAL NOT NULL
);