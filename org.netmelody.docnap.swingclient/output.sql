

----- START CHANGE SCRIPT #1: 001_create_table.sql -----





INSERT INTO changelog (change_number, delta_set, complete_dt, applied_by, description)
 VALUES (1, 'Main', CURRENT_TIMESTAMP, USER(), '001_create_table.sql');

COMMIT;

----- END CHANGE SCRIPT #1: 001_create_table.sql -----


----- START CHANGE SCRIPT #2: 002_insert_data.sql -----





INSERT INTO changelog (change_number, delta_set, complete_dt, applied_by, description)
 VALUES (2, 'Main', CURRENT_TIMESTAMP, USER(), '002_insert_data.sql');

COMMIT;

----- END CHANGE SCRIPT #2: 002_insert_data.sql -----

