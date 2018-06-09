ALTER TABLE event
  ADD COLUMN number_enrolled integer CHECK (number_enrolled >= 0);
ALTER TABLE event
  ADD CONSTRAINT CHK_CAPACITY CHECK (number_enrolled < capacity);