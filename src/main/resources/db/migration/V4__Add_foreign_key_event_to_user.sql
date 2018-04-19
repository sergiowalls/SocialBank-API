ALTER TABLE "event"
  ADD CONSTRAINT fk_creatorUser
FOREIGN KEY (creatoremail)
REFERENCES "user"(email);