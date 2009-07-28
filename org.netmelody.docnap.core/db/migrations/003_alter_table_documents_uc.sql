ALTER TABLE documents ADD CONSTRAINT uc_documents_handle UNIQUE (handle);

--//@UNDO

ALTER TABLE documents DROP CONSTRAINT uc_documents_handle;