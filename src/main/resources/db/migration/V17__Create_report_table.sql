CREATE TABLE "report" (
  reporter VARCHAR(255) REFERENCES "user" (email),
  reported VARCHAR(255) REFERENCES "user" (email),
  PRIMARY KEY (reporter, reported)
);

ALTER TABLE "user"
  ADD COLUMN enabled BOOLEAN DEFAULT TRUE;