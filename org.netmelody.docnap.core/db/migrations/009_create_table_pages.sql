CREATE TABLE pages (
  pageid INTEGER NOT NULL IDENTITY,
  documentid INTEGER NOT NULL,
  number INTEGER NOT NULL,
  handle VARCHAR(2000) NOT NULL,
  original_filename VARCHAR(2000) NOT NULL
);

--//@UNDO

DROP TABLE pages;