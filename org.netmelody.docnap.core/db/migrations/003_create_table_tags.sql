CREATE TABLE tags (
  tagid INTEGER NOT NULL IDENTITY,
  creation_dt TIMESTAMP DEFAULT 'NOW' NOT NULL,
  title VARCHAR(2000) NOT NULL,
  description VARCHAR(2000)
);

--//@UNDO

DROP TABLE tags;