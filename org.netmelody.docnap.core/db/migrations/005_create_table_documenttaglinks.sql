CREATE TABLE documenttaglinks (
  documenttaglinkid INTEGER NOT NULL IDENTITY,
  documentid INTEGER NOT NULL,
  tagid INTEGER NOT NULL,
  creation_dt TIMESTAMP DEFAULT 'NOW' NOT NULL
);

--//@UNDO

DROP TABLE documenttaglinks;