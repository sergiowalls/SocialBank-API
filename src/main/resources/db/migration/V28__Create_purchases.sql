CREATE TABLE "purchase" (
  transaction_id VARCHAR(255) NOT NULL,
  user_email VARCHAR(255) REFERENCES "user"(email),
  package_name VARCHAR(255) REFERENCES "package"(name),
  PRIMARY KEY(transaction_id)
);
