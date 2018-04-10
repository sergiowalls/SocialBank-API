DROP TABLE "user";

CREATE TABLE "user"(
  email VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  surname VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  birthdate DATE NOT NULL,
  gender VARCHAR(255) NOT NULL,
  balance FLOAT NOT NULL DEFAULT (0) CHECK (balance > -10),
  description TEXT,
  recovery VARCHAR(255) DEFAULT NULL);






