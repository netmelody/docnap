ALTER TABLE documents ADD CONSTRAINT pk_documents PRIMARY KEY (documentid)

--//@UNDO

ALTER TABLE documents DROP CONSTRAINT pk_documents;