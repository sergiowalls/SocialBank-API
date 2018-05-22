CREATE TABLE "user_event_enrollment" (
  user_email varchar(255) REFERENCES "user" (email) ON DELETE CASCADE,
  event_id integer REFERENCES event (id) ON DELETE CASCADE,
  PRIMARY KEY (user_email, event_id)
);