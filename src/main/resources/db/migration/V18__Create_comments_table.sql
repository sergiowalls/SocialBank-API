CREATE TABLE comments (
  id            serial PRIMARY KEY,
  creator_email varchar(255) NOT NULL,
  created_at    timestamp    NOT NULL,
  updated_at    timestamp    NOT NULL,
  answer_to     integer REFERENCES comments (id) ON DELETE CASCADE,
  content       text         NOT NULL
);