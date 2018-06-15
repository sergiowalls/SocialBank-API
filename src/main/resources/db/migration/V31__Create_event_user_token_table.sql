CREATE TABLE event_user_token (
  "user" VARCHAR(255),
  event INTEGER,
  token VARCHAR(255),
  CONSTRAINT fk_enroll FOREIGN KEY ("user", event) REFERENCES user_event_enrollment(user_email, event_id) ON DELETE CASCADE,
  PRIMARY KEY ("user", event, token)
)