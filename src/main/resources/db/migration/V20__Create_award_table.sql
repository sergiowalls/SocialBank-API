CREATE TABLE award (
  email VARCHAR(255) REFERENCES "user" (email) ON DELETE CASCADE,
  award VARCHAR(255),
  PRIMARY KEY (email, award)
);