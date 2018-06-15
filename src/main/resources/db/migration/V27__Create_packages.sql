CREATE TABLE "package" (
  name VARCHAR(255),
  price DOUBLE PRECISION NOT NULL,
  hours INTEGER NOT NULL,
  PRIMARY KEY(name)
);

INSERT INTO "package" VALUES ('Basic Package', 99.9, 100);
INSERT INTO "package" VALUES ('Professional Organization Package', 399.9, 500);
INSERT INTO "package" VALUES ('Premium Organization Package', 1199.9, 2000);