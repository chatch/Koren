PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE search_history (id INTEGER PRIMARY KEY, search_string TEXT);
COMMIT;
