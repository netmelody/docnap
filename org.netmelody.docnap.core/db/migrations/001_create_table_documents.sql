CREATE TABLE documents (
  documentid INTEGER NOT NULL,
  checkin_dt TIMESTAMP NOT NULL,
  handle VARCHAR(2000),
  title VARCHAR(2000),
  original_filename VARCHAR(2000)
);

--//@UNDO

DROP TABLE documents;