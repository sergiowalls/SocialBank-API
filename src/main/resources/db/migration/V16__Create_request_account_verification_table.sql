CREATE TABLE request_account_verification (
  "user"  varchar(255) PRIMARY KEY REFERENCES "user" (email) ON DELETE CASCADE,
  message varchar(255)
);