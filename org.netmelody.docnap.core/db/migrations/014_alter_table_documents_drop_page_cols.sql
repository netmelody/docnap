ALTER TABLE documents DROP CONSTRAINT uc_documents_handle;
ALTER TABLE documents DROP COLUMN handle;

--//@UNDO

ALTER TABLE documents ADD COLUMN handle VARCHAR(2000);
ALTER TABLE documents ADD CONSTRAINT uc_documents_handle UNIQUE (handle);