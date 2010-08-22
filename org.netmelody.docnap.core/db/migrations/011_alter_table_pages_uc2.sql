ALTER TABLE pages ADD CONSTRAINT uc_page_number UNIQUE (documentid, number);

--//@UNDO

ALTER TABLE pages DROP CONSTRAINT uc_page_number;