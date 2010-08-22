INSERT
  INTO pages (documentid, number, handle, original_filename)
SELECT d.documentid, 1, d.handle, d.original_filename
  FROM documents d;

--//@UNDO

DELETE FROM pages;
