ALTER TABLE documents ADD COLUMN sent_dt TIMESTAMP;
ALTER TABLE documents ADD COLUMN received_dt TIMESTAMP;

--//@UNDO

ALTER TABLE documents DROP COLUMN received_dt;
ALTER TABLE documents DROP COLUMN sent_dt;
