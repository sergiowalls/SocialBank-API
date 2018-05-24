CREATE TABLE comment (
  id            serial PRIMARY KEY,
  event_id      integer REFERENCES event (id) ON DELETE CASCADE NOT NULL,
  creator_email varchar(255) REFERENCES "user" (email) ON DELETE CASCADE NOT NULL,
  created_at    timestamp NOT NULL,
  updated_at    timestamp NOT NULL,
  reply_to      integer REFERENCES comment (id) ON DELETE CASCADE,
  content       text NOT NULL
);