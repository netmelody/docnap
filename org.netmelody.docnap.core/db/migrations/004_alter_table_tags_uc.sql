ALTER TABLE tags ADD CONSTRAINT uc_tags_title UNIQUE (title);

--//@UNDO

ALTER TABLE tags DROP CONSTRAINT uc_tags_title;