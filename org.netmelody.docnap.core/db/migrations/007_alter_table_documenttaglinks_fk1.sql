ALTER TABLE documenttaglinks ADD CONSTRAINT fk_documenttaglink_document FOREIGN KEY (documentid)
    REFERENCES documents (documentid) ON DELETE CASCADE;

--//@UNDO

ALTER TABLE documenttaglinks DROP CONSTRAINT fk_documenttaglink_document;
