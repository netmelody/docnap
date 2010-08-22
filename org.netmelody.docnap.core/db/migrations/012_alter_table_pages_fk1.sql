ALTER TABLE pages ADD CONSTRAINT fk_page_document FOREIGN KEY (documentid)
    REFERENCES documents (documentid) ON DELETE CASCADE;

--//@UNDO

ALTER TABLE pages DROP CONSTRAINT fk_page_document;
