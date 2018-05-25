CREATE OR REPLACE FUNCTION update_user_awards()
  RETURNS TRIGGER AS
$BODY$
BEGIN
  IF NEW.verified_account = TRUE
  THEN
    IF NOT EXISTS
    (
      SELECT *
      FROM award
      WHERE email = NEW.email AND award = 'VERIFIED_USER'
    )
    THEN
      INSERT INTO award VALUES (NEW.email, 'VERIFIED_USER');
    END IF;
  ELSE
    DELETE FROM award
    WHERE email = OLD.email AND award = 'VERIFIED_USER';
  END IF;
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION update_event_awards()
  RETURNS TRIGGER AS
$BODY$
DECLARE
  _num_events      INTEGER;
  _required_events INTEGER := 3; -- At X events, you get the award
BEGIN
  SELECT COUNT(*)
  INTO _num_events
  FROM event
  WHERE creatoremail = NEW.creatoremail;

  IF _num_events >= _required_events
  THEN
    IF NOT EXISTS
    (
        SELECT *
        FROM award
        WHERE email = NEW.creatoremail AND award = 'TOP_ORGANIZER'
    )
    THEN
      INSERT INTO award VALUES (NEW.creatoremail, 'TOP_ORGANIZER');
    END IF;
  END IF;
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION update_enrollment_awards()
  RETURNS TRIGGER AS
$BODY$
DECLARE
  _num_events      INTEGER;
  _required_events INTEGER := 2; -- At X events, you get the award
BEGIN
  SELECT COUNT(*)
  INTO _num_events
  FROM user_event_enrollment
  WHERE user_email = NEW.user_email;

  IF _num_events >= _required_events
  THEN
    IF NOT EXISTS
    (
        SELECT *
        FROM award
        WHERE email = NEW.user_email AND award = 'ACTIVE_USER'
    )
    THEN
      INSERT INTO award VALUES (NEW.user_email, 'ACTIVE_USER');
    END IF;
  END IF;
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE TRIGGER update_user_awards
  AFTER UPDATE
  ON "user"
  FOR EACH ROW
  EXECUTE PROCEDURE update_user_awards();

CREATE TRIGGER update_event_awards
  AFTER INSERT
  ON event
  FOR EACH ROW
EXECUTE PROCEDURE update_event_awards();

CREATE TRIGGER update_enrollment_awards
  AFTER INSERT
  ON user_event_enrollment
  FOR EACH ROW
EXECUTE PROCEDURE update_enrollment_awards();