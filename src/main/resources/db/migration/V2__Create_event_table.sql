CREATE TABLE "event" (
  id VARCHAR(255) PRIMARY KEY,
  creator VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  iniDate DATE NOT NULL,
  endDate DATE NOT NULL,
  hours INT NOT NULL,
  location VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT
);