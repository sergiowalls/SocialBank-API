CREATE TABLE "event" (
  id INT PRIMARY KEY,
  creatorEmail VARCHAR(255) NOT NULL,
  iniDate DATE NOT NULL,
  endDate DATE NOT NULL,
  hours INT NOT NULL CHECK(hours > 0),
  location VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT
);