ALTER TABLE pages ADD CONSTRAINT uc_page_handle UNIQUE (handle);

--//@UNDO

ALTER TABLE pages DROP CONSTRAINT uc_page_handle;