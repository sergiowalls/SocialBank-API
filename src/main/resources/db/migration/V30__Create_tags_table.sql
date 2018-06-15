CREATE TABLE "event_tags" (
  event_id INT,
  tag VARCHAR(255),
  CONSTRAINT FK_id FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
  PRIMARY KEY (event_id, tag)
)