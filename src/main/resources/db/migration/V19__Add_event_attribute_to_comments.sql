DROP TABLE comments;

CREATE TABLE comment (
  id            serial PRIMARY KEY,
  event_id      integer REFERENCES event (id) ON DELETE CASCADE NOT NULL,
  creator_email varchar(255)                                    NOT NULL,
  created_at    timestamp                                       NOT NULL,
  updated_at    timestamp                                       NOT NULL,
  answer_to     integer REFERENCES comments (id) ON DELETE CASCADE,
  content       text                                            NOT NULL
);