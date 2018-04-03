DROP TABLE "event";
CREATE TABLE "event" (
  id SERIAL PRIMARY KEY,
  creatorEmail VARCHAR(255) NOT NULL,
  iniDate DATE NOT NULL,
  endDate DATE NOT NULL,
  hours INT NOT NULL,
  location VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT
);

ALTER TABLE "event"
  ADD CONSTRAINT CHK_DATE_INI_LT_END CHECK (iniDate < endDate);

ALTER TABLE "event"
  ADD CONSTRAINT CHK_DATE_NOW CHECK (now() < iniDate);