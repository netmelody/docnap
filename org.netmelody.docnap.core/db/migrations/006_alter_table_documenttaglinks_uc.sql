ALTER TABLE documenttaglinks ADD CONSTRAINT uc_documenttaglinks_link UNIQUE (documentid, tagid);

--//@UNDO

ALTER TABLE documenttaglinks DROP CONSTRAINT uc_documenttaglinks_link;