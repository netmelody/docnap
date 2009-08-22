ALTER TABLE documenttaglinks ADD CONSTRAINT fk_documenttaglink_tag FOREIGN KEY (tagid)
    REFERENCES tags (tagid) ON DELETE CASCADE;

--//@UNDO

ALTER TABLE documenttaglinks DROP CONSTRAINT fk_documenttaglink_tag;
