CREATE OR REPLACE FUNCTION update_user_awards()
  RETURNS TRIGGER AS
$BODY$
BEGIN
  IF NEW.verified_account = TRUE
  THEN
    INSERT INTO award VALUES (NEW.email, 'VERIFIED_USER')
    ON CONFLICT DO NOTHING;
  ELSE
    DELETE FROM award
    WHERE email = OLD.email AND award = 'VERIFIED_USER';
  END IF;
END;
$BODY$;

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
    INSERT INTO award VALUES (NEW.creatoremail, 'TOP_ORGANIZER')
    ON CONFLICT DO NOTHING;
  END IF;

END;
$BODY$;

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
    INSERT INTO award VALUES (NEW.user_email, 'ACTIVE_USER')
    ON CONFLICT DO NOTHING;
  END IF;
END;
$BODY$