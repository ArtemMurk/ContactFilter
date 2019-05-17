DROP TABLE IF EXISTS contacts;
DROP SEQUENCE IF EXISTS contacts_id_seq;

CREATE SEQUENCE contacts_id_seq;

CREATE TABLE IF NOT EXISTS contacts
(
	id bigint PRIMARY KEY DEFAULT nextval('contacts_id_seq') ,
	name VARCHAR
)